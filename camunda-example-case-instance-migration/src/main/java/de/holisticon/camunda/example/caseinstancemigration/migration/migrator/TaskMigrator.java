package de.holisticon.camunda.example.caseinstancemigration.migration.migrator;

import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaTask;
import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaTaskRepository;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.engine.impl.history.producer.DefaultHistoryEventProducer;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskMigrator {

    private final CamundaTaskRepository camundaTaskRepository;

    private final TaskService taskService;

    private final ProcessEngine processEngine;

    @Transactional(propagation = Propagation.MANDATORY)
    public void migrateAllTasksForCaseInstance(@NonNull final String caseInstanceId, @NonNull final String targetCaseDefId) {
        final List<CamundaTask> tasksToMigrate = ImmutableList.copyOf(camundaTaskRepository.findByCaseInstanceId(caseInstanceId));

        log.info(String.format("Found %d tasks to migrate for case instance '%s'", tasksToMigrate.size(), caseInstanceId));

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

        if (getHistoryLevel().isHistoryEventProduced(HistoryEventTypes.CASE_INSTANCE_UPDATE, null)) {
            final HistoryEvent event = new DefaultHistoryEventProducer().createTaskInstanceMigrateEvt(task);
            getHistoryEventHandler().handleEvent(event);
        }
    }


    private HistoryEventHandler getHistoryEventHandler() {
        return ((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration()).getHistoryEventHandler();
    }

    private HistoryLevel getHistoryLevel() {
        return ((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration()).getHistoryLevel();
    }
}
