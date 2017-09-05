package holunda.taskassignment.plugin.process.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("determineDmnTable")
public class DetermineDmnTableDelegate implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) throws Exception {

  }
}
