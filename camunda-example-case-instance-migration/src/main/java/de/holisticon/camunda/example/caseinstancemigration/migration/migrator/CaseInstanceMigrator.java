package de.holisticon.camunda.example.caseinstancemigration.migration.migrator;

import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaCaseExecution;
import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaCaseExecutionRepository;
import de.holisticon.camunda.example.caseinstancemigration.migration.migrator.steps.CaseExecutionMigrationStep;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.engine.impl.history.producer.DefaultCmmnHistoryEventProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaseInstanceMigrator {

    private final CamundaCaseExecutionRepository camundaCaseExecutionRepository;

    private final ProcessEngine processEngine;

    private final CaseService caseService;

    private final TaskMigrator taskMigrator;

    private final List<CaseExecutionMigrationStep> migrationSteps;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrateOneCaseInstance(@NonNull final String caseInstanceId, @NonNull final String targetCaseDefId) {
        log.info(String.format("Migrating case instance '%s'", caseInstanceId));

        final List<CamundaCaseExecution> executionsToMigrate = ImmutableList.copyOf(camundaCaseExecutionRepository.findByCaseInstanceId(caseInstanceId));

        log.info(String.format("Found %d executions for case instance '%s'", executionsToMigrate.size(), caseInstanceId));

        executionsToMigrate.forEach(execution -> migrateOneExecution(execution, targetCaseDefId));

        produceCaseInstanceHistoryEventsForOneCaseInstance(caseInstanceId);

        taskMigrator.migrateAllTasksForCaseInstance(caseInstanceId, targetCaseDefId);
    }

    private void migrateOneExecution(@NonNull CamundaCaseExecution execution, @NonNull final String targetCaseDefId) {
        log.info(String.format("Migrating execution '%s'", execution.getId()));

        execution.setCaseDefinitionId(targetCaseDefId);

        final AtomicReference<CamundaCaseExecution> executionRef = new AtomicReference<>(execution);

        migrationSteps.forEach(step -> executionRef.set(step.migrate(executionRef.get())));

        camundaCaseExecutionRepository.save(executionRef.get());
    }

    private void produceCaseInstanceHistoryEventsForOneCaseInstance(@NonNull final String caseInstanceId) {
        log.info(String.format("Creating history event for case instance '%s'", caseInstanceId));

        final CaseExecutionEntity caseInstance = (CaseExecutionEntity) caseService.createCaseInstanceQuery().caseInstanceId(caseInstanceId).singleResult();

        if (caseInstance == null) {
            throw new IllegalStateException(String.format("Case instance '%s' not found", caseInstanceId));
        }

        if (getHistoryLevel().isHistoryEventProduced(HistoryEventTypes.CASE_INSTANCE_UPDATE, null)) {
            final HistoryEvent event = new DefaultCmmnHistoryEventProducer().createCaseInstanceUpdateEvt(caseInstance);
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
