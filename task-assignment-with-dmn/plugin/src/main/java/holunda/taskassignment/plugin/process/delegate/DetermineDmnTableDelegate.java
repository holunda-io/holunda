package holunda.taskassignment.plugin.process.delegate;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.Variable;
import holunda.taskassignment.plugin.process.TaskAssignmentProcess;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("determineDmnTable")
@Slf4j
public class DetermineDmnTableDelegate implements JavaDelegate {

  @Autowired
  private RepositoryService repositoryService;

  @Override
  public void execute(final DelegateExecution execution) throws Exception {
    final String type = Variable.TYPE.getValue(execution);

    log.info("\n-----\n determineDmnTable\ntable={}\n-----\n", type);

    if (repositoryService.createDecisionDefinitionQuery().decisionDefinitionKey(type).singleResult() == null) {
      log.error("no decisionTable found for key: {}", type);
      throw new BpmnError("NoDmnTableFound");
    } else {
      TaskAssignmentProcess.VARIABLES.DMN_TABLE.setValue(execution, type);
    }

  }
}
