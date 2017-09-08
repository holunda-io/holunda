package de.holisticon.camunda.example.caseinstancemigration.migration.command;

import de.holisticon.camunda.example.caseinstancemigration.migration.migrator.CaseInstanceMigrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MigrateCaseInstanceVersionCmd implements Command<Void> {

  private final CaseInstanceMigrator caseInstanceMigrator;

  @Override
  public Void execute(CommandContext commandContext) {
    caseInstanceMigrator.migrateCasesToLatestVersion("my_case_mock");

    return null;
  }

}
