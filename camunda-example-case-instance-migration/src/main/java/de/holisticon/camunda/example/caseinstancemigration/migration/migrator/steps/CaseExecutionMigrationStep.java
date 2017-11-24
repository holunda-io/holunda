package de.holisticon.camunda.example.caseinstancemigration.migration.migrator.steps;

import de.holisticon.camunda.example.caseinstancemigration.migration.domain.CamundaCaseExecution;
import lombok.NonNull;

public interface CaseExecutionMigrationStep {

    CamundaCaseExecution migrate(@NonNull final CamundaCaseExecution camundaCaseExecution);
}
