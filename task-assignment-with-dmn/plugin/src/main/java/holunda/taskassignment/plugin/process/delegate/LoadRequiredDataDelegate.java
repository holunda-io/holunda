package holunda.taskassignment.plugin.process.delegate;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.BusinessKey;
import holunda.taskassignment.plugin.dmn.GetInputParameters;
import holunda.taskassignment.plugin.process.TaskAssignmentProcess;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.BUSINESS_DATA;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.BUSINESS_KEY;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.DMN_TABLE;

@Component("loadRequiredData")
@Slf4j
public class LoadRequiredDataDelegate implements JavaDelegate {

  @Autowired
  private BusinessDataService businessDataService;

  @Autowired
  private GetInputParameters getInputParameters;

  @Override
  public void execute(final DelegateExecution execution) throws Exception {
    final String dmnTable = DMN_TABLE.getValue(execution);
    final String businessKey = BUSINESS_KEY.getValue(execution);

    Set<String> requiredKeys = getInputParameters.apply(dmnTable);
    BusinessData businessData = businessDataService.loadBusinessData(new BusinessKey(businessKey), requiredKeys);

    log.info("\n-----\n loadRequiredData\n" +
      "table: {}\n" +
      "businessKey: {}\n" +
      "requiredKeys: {}\n" +
      "data: {}\n" +
      "-----\n", dmnTable, businessKey, requiredKeys, businessData);


    BUSINESS_DATA.setValue(execution, businessData.getData());
  }
}
