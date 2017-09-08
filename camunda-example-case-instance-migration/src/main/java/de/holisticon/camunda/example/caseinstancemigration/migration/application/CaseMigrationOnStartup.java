package de.holisticon.camunda.example.caseinstancemigration.migration.application;

import de.holisticon.camunda.example.caseinstancemigration.migration.migrator.CaseInstanceMigrator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CaseMigrationOnStartup {

    @EventListener
    public void migrateOnStartup(ApplicationReadyEvent event) {
        //event.getApplicationContext().getBean(CaseInstanceMigrator.class).migrateCasesToLatestVersion(null);
    }
}
