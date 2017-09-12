package holunda.taskassignment.plugin.process.delegate;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessData;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.BUSINESS_DATA;

@Component("loadRequiredData")
public class LoadRequiredDataDelegate implements JavaDelegate {

  private final BusinessDataService businessDataService;

  public LoadRequiredDataDelegate(BusinessDataService businessDataService) {
    this.businessDataService = businessDataService;
  }

  @Override
  public void execute(final DelegateExecution execution) throws Exception {
    BusinessData businessData = new BusinessData(BUSINESS_DATA.getValue(execution));
  }
}
