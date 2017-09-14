package holunda.taskassignment.plugin.dmn;

import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.CandidateGroup;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
@Slf4j
public class EvaluateDecisionTable implements BiFunction<String, BusinessData, CandidateGroup> {

  private final DecisionService decisionService;

  public EvaluateDecisionTable(DecisionService decisionService) {
    this.decisionService = decisionService;
  }

  @Override
  public CandidateGroup apply(final String decisionTable, final BusinessData businessData) {
    DmnDecisionTableResult result = decisionService.evaluateDecisionTableByKey(decisionTable, businessData.toVariables());

    DmnDecisionRuleResult singleResult = result.getSingleResult();

    String c = singleResult.getSingleEntry();

    CandidateGroup cg = new CandidateGroup(c);

    return cg;
  }
}
