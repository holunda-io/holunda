package holunda.taskassignment.plugin.process.delegate;

import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.CandidateGroup;
import holunda.taskassignment.plugin.context.RequireNewTransaction;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.dmn.engine.DmnDecisionRuleResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Supplier;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.BUSINESS_DATA;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.CANDIDATE_GROUP;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.DMN_TABLE;

@Component("evaluateDmn")
@Slf4j
public class EvaluateDmnDelegate implements JavaDelegate {

  private final DecisionService decisionService;
  private final RequireNewTransaction transactionWrapper;

  public EvaluateDmnDelegate(DecisionService decisionService, RequireNewTransaction transactionWrapper) {
    this.decisionService = decisionService;
    this.transactionWrapper = transactionWrapper;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    final String decisionTable = DMN_TABLE.getValue(execution);
    final BusinessData businessData = new BusinessData(BUSINESS_DATA.getValue(execution));

    // this does the actual work
    final Supplier<CandidateGroup> evaluate = () ->
      Optional
        .ofNullable(decisionService.evaluateDecisionTableByKey(decisionTable, businessData.toVariables()).getSingleResult())
        .map(DmnDecisionRuleResult::getEntryMap)
        .map(CANDIDATE_GROUP::getValue)
        .map(CandidateGroup::new)
        .orElseGet(CandidateGroup::empty);


    final CandidateGroup candidateGroup = Try.of(() -> transactionWrapper.requireNewTransaction(evaluate).get())
      .onFailure(e -> log.warn("could not evaluate candidateGroup, {}", e.getMessage()))
      .getOrElse(CandidateGroup.empty());

    CANDIDATE_GROUP.setValue(execution, candidateGroup.getName());
  }

}
