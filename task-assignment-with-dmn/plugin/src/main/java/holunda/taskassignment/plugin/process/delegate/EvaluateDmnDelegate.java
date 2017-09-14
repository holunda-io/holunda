package holunda.taskassignment.plugin.process.delegate;

import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.CandidateGroup;
import holunda.taskassignment.plugin.context.RequireNewTransaction;
import holunda.taskassignment.plugin.dmn.EvaluateDecisionTable;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.BUSINESS_DATA;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.CANDIDATE_GROUP;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.DMN_TABLE;

@Component("evaluateDmn")
@Slf4j
public class EvaluateDmnDelegate implements JavaDelegate {

  private final DecisionService decisionService;
  private final RequireNewTransaction transactionWrapper;
  private final EvaluateDecisionTable evaluateDecisionTable;

  public EvaluateDmnDelegate(DecisionService decisionService,
                             RequireNewTransaction transactionWrapper, EvaluateDecisionTable evaluateDecisionTable) {
    this.decisionService = decisionService;
    this.transactionWrapper = transactionWrapper;
    this.evaluateDecisionTable = evaluateDecisionTable;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    final String decisionTable = DMN_TABLE.getValue(execution);
    final BusinessData businessData = new BusinessData(BUSINESS_DATA.getValue(execution));

    log.info("\n-----\n" +
      "evaluateDmn\n\n" +
      "table: {}\n" +
      "data: {}\n" +
      "-----\n", decisionTable, businessData.toVariables());

    CandidateGroup candidateGroup = evaluateDecisionTable.apply(decisionTable, businessData);

    CANDIDATE_GROUP.setValue(execution, candidateGroup.getName());
  }

}
