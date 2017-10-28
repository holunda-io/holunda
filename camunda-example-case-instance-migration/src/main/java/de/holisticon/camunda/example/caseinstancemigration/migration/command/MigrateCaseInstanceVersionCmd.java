package de.holisticon.camunda.example.caseinstancemigration.migration.command;

import de.holisticon.camunda.example.caseinstancemigration.migration.migrator.CaseInstanceMigrator;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.repository.CaseDefinition;
import org.camunda.bpm.engine.repository.ResourceDefinition;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MigrateCaseInstanceVersionCmd implements Command<Void> {

    private String caseDefinitionKey;

    private CaseInstanceMigrator migrator;

    private RepositoryService repositoryService;

    private CaseService caseService;

    public MigrateCaseInstanceVersionCmd(@NonNull final String caseDefinitionKey, @NonNull final ApplicationContext ctx) {
        this.caseDefinitionKey = caseDefinitionKey;

        migrator = ctx.getBean(CaseInstanceMigrator.class);
        repositoryService = ctx.getBean(RepositoryService.class);
        caseService = ctx.getBean(CaseService.class);
    }

    @Override
    public Void execute(CommandContext commandContext) {

        migrateCasesToLatestVersion(caseDefinitionKey);

        return null;
    }

    private void migrateCasesToLatestVersion(@NonNull final String caseDefinitionKey) {
        log.info("Starting to migrate case instances to latest version");

        final String latestCaseDefId = getLatestCaseDefinitionId(caseDefinitionKey);

        log.info(String.format("Latest case definition id is '%s'", latestCaseDefId));

        migrateAllCaseInstances(latestCaseDefId, caseDefinitionKey);

        log.info("Migration completed");
    }

    private void migrateAllCaseInstances(@NonNull final String targetCaseDefId, @NonNull final String caseDefinitionKey) {
        final List<String> caseDefinitionIds = ImmutableList.copyOf(getAllButTargetCaseDefinitionIds(caseDefinitionKey, targetCaseDefId));

        final List<CaseInstance> caseInstancesToMigrate = new ArrayList<>();

        caseDefinitionIds.forEach(caseDefinitionId -> caseInstancesToMigrate.addAll(caseService.createCaseInstanceQuery().caseDefinitionId(caseDefinitionId).list()));

        log.info(String.format("Found %d case instances to migrate", caseInstancesToMigrate.size()));

        caseInstancesToMigrate.forEach(caseInstance -> {
            try {
                migrator.migrateOneCaseInstance(caseInstance.getCaseInstanceId(), targetCaseDefId);
            } catch (Exception e) {
                log.error(String.format("Exception during migration of case instance '%s", caseInstance.getCaseInstanceId()), e);
            }
        });
    }

    private String getLatestCaseDefinitionId(@NonNull String caseDefinitionKey) {
        final CaseDefinition caseDefinition = repositoryService.createCaseDefinitionQuery().caseDefinitionKey(caseDefinitionKey).latestVersion().singleResult();

        if (caseDefinition == null) {
            throw new IllegalStateException("Could not determine latest case model version");
        }

        return caseDefinition.getId();
    }

    private List<String> getAllButTargetCaseDefinitionIds(@NonNull final String caseDefinitionKey, @NonNull final String targetCaseDefinitionId) {
        final List<CaseDefinition> caseDefinitions = ImmutableList.copyOf(repositoryService.createCaseDefinitionQuery().caseDefinitionKey(caseDefinitionKey).list());

        return ImmutableList.copyOf(caseDefinitions.stream().filter(def -> !def.getId().equals(targetCaseDefinitionId)).map(ResourceDefinition::getId).collect(Collectors.toList()));
    }
}
