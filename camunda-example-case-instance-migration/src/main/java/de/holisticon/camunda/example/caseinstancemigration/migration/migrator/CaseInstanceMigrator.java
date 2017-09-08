package de.holisticon.camunda.example.caseinstancemigration.migration.migrator;

import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaCaseExecution;
import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaCaseExecutionRepository;
import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaTask;
import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaTaskRepository;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.impl.history.producer.DefaultCmmnHistoryEventProducer;
import org.camunda.bpm.engine.impl.history.producer.DefaultHistoryEventProducer;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaseInstanceMigrator {

  private final CamundaCaseExecutionRepository camundaCaseExecutionRepository;

  private final CamundaTaskRepository camundaTaskRepository;

  private final RepositoryService repositoryService;

  private final ProcessEngine processEngine;

  private final CaseService caseService;

  private final TaskService taskService;

  public void migrateCasesToLatestVersion(@NonNull final String caseDefinitionKey) {
    log.info("Starting to migrate case instances to latest version");

    final String latestCaseDefId = getLatestCaseDefinitionId(caseDefinitionKey);

    log.info(String.format("Latest case definition id is '%s'", latestCaseDefId));

    migrateAllCaseInstances(latestCaseDefId);

    migrateAllTasks(latestCaseDefId);

    log.info("Migration completed");
  }

  private void migrateAllCaseInstances(@NonNull final String targetCaseDefId) {
    final List<CamundaCaseExecution> executionsToMigrate = ImmutableList.copyOf(camundaCaseExecutionRepository.findByCaseDefinitionIdIsNot(targetCaseDefId));

    log.info(String.format("Found %d executions to migrate", executionsToMigrate.size()));

    executionsToMigrate.forEach(e -> migrateOneExecution(e, targetCaseDefId));

    produceCaseInstanceHistoryEvents(ImmutableList.copyOf(executionsToMigrate.stream().map(CamundaCaseExecution::getCaseInstanceId).distinct().collect(Collectors.toList())));
  }

  private void migrateOneExecution(@NonNull CamundaCaseExecution execution, @NonNull final String targetCaseDefId) {
    log.info(String.format("Migrating execution '%s'", execution.getId()));

    execution.setCaseDefinitionId(targetCaseDefId);

    camundaCaseExecutionRepository.save(execution);
  }


  private void produceCaseInstanceHistoryEvents(@NonNull final List<String> caseInstanceIds) {
    final HistoryLevel historyLevel = ((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration()).getHistoryLevel();
    if (historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_INSTANCE_UPDATE, null)) {
      log.info("Producing history events for case instances");
      caseInstanceIds.forEach(this::produceCaseInstanceHistoryEventsForOneCaseInstance);
    }
  }

  private void produceCaseInstanceHistoryEventsForOneCaseInstance(@NonNull final String caseInstanceId) {
    log.info(String.format("Creating history event for case instance '%s'", caseInstanceId));

    final CaseExecutionEntity caseInstance = (CaseExecutionEntity) caseService.createCaseInstanceQuery().caseInstanceId(caseInstanceId).singleResult();

    if (caseInstance == null) {
      throw new IllegalStateException(String.format("Case instance '%s' not found", caseInstanceId));
    }

    final ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();

    final HistoryEvent event = new DefaultCmmnHistoryEventProducer().createCaseInstanceUpdateEvt(caseInstance);
    configuration.getHistoryEventHandler().handleEvent(event);
  }

  private void migrateAllTasks(@NonNull final String targetCaseDefId) {
    final List<CamundaTask> tasksToMigrate = ImmutableList.copyOf(camundaTaskRepository.findByCaseDefinitionIdIsNotNullAndCaseDefinitionIdIsNot(targetCaseDefId));

    log.info(String.format("Found %d tasks to migrate", tasksToMigrate.size()));

    tasksToMigrate.forEach(t -> migrateOneTask(t, targetCaseDefId));
  }

  private void migrateOneTask(@NonNull CamundaTask task, @NonNull final String targetCaseDefId) {
    log.info(String.format("Migrating task '%s'", task.getId()));

    task.setCaseDefinitionId(targetCaseDefId);

    camundaTaskRepository.save(task);

    produceTaskHistoryEvent(task.getId());
  }

  private void produceTaskHistoryEvent(@NonNull final String taskId) {
    log.info(String.format("Creating history event for task '%s'", taskId));

    final TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();

    if (task == null) {
      throw new IllegalStateException(String.format("Task '%s' not found", taskId));
    }

    final ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();

    final HistoryEvent event = new DefaultHistoryEventProducer().createTaskInstanceMigrateEvt(task);
    configuration.getHistoryEventHandler().handleEvent(event);
  }

  private String getLatestCaseDefinitionId(@NonNull String caseDefinitionKey) {
    final CaseDefinition caseDefinition = repositoryService.createCaseDefinitionQuery().caseDefinitionKey(caseDefinitionKey).latestVersion().singleResult();

    if (caseDefinition == null) {
      throw new IllegalStateException("Could not determine latest case model version");
    }

    return caseDefinition.getId();
  }
}
