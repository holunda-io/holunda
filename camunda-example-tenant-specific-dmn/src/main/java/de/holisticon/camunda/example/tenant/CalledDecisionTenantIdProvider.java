package de.holisticon.camunda.example.tenant;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.model.bpmn.instance.BusinessRuleTask;
import org.springframework.stereotype.Component;

/**
 * @author Jo Ehm (Holisticon)
 */
@Component
@RequiredArgsConstructor
public class CalledDecisionTenantIdProvider {

    private final RepositoryService repositoryService;

    public String resolveTenantId(DelegateExecution execution) {

        final String tenantId = execution.getTenantId();

        if (tenantId != null) {
            final BusinessRuleTask ruleTask = (BusinessRuleTask) execution.getBpmnModelElementInstance();

            final DecisionDefinition decisionDefinition = repositoryService.createDecisionDefinitionQuery()
                    .tenantIdIn(tenantId)
                    .decisionDefinitionKey(ruleTask.getCamundaDecisionRef())
                    .singleResult();

            if (decisionDefinition == null) {
                // no tenant-specific DMN deployment found, so don't use any tenant ID
                return null;
            }
        }

        return tenantId;
    }
}
