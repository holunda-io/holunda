package holunda.taskassignment.plugin.dmn;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.camunda.bpm.model.dmn.instance.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.camunda.bpm.model.dmn.impl.DmnModelConstants.DMN_ELEMENT_DECISION_TABLE;

@Component
public class GetInputParameters implements Function<String, Set<String>> {

  @Autowired
  private RepositoryService repositoryService;

  @Override
  public Set<String> apply(final String decisionDefinitionKey) {
    // load decisionDefinition by key
    Optional<DecisionDefinition> decisionDefinition = Optional
      .ofNullable(repositoryService
        .createDecisionDefinitionQuery()
        .decisionDefinitionKey(decisionDefinitionKey)
        .latestVersion()
        .singleResult());

    //Load ModelInstance for given definition id form repository
    Optional<DmnModelInstance> dmnModelInstance = decisionDefinition.map(DecisionDefinition::getId)
      .map(repositoryService::getDmnModelInstance);

    //Get input columns of dmn
    return dmnModelInstance
      .map(instance -> (DecisionTable) instance.getModelElementById(DMN_ELEMENT_DECISION_TABLE))
      .map(DecisionTable::getInputs)
      .orElse(Collections.emptyList())
      .stream()
      .map(Input::getTextContent)
      .collect(Collectors.toSet());
  }
}
