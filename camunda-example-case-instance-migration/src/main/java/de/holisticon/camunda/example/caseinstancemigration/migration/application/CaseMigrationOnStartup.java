package de.holisticon.camunda.example.caseinstancemigration.migration.application;


import de.holisticon.camunda.example.caseinstancemigration.migration.command.MigrateCaseInstanceVersionCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CaseMigrationOnStartup {

    @Value("${camunda.bpm.migration.case-instance-migration-on-startup:false}")
    private Boolean migrateOnStartup;

    private final ProcessEngine processEngine;

    private final ApplicationContext ctx;

    @EventListener
    public void migrateOnStartup(ApplicationReadyEvent event) {
        if (migrateOnStartup) {
            ((ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration()).getCommandExecutorTxRequired().execute(new MigrateCaseInstanceVersionCmd("myCaseDefinitionKey", ctx));
        } else {
            log.info("CaseInstance migration on application startup is disabled");
        }
    }
}
