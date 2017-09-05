package holunda.taskassignment.plugin.process.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.CANDIDATE_GROUP;

@Component("evaluateDmn")
public class EvaluateDmnDelegate implements JavaDelegate  {

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    CANDIDATE_GROUP.setValue(execution, "bar");
  }
}
