package holunda.taskassignment.plugin.process.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.BUSINESS_DATA;

@Component
public class StartTaskAssignmentProcessListener implements ExecutionListener {

  @Override
  public void notify(final DelegateExecution execution) throws Exception {
    BUSINESS_DATA.setValue(execution, new HashMap<>());
  }
}
