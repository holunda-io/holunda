package holunda.taskassignment.plugin.process.delegate;

import holunda.taskassignment.plugin.api.CandidateGroup;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.springframework.stereotype.Component;
import reactor.bus.Event;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.CANDIDATE_GROUP;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.TOPIC;

@Component("returnCandidateGroup")
public class ReturnCandidateGroupDelegate implements JavaDelegate{

  private final CamundaEventBus camundaEventBus;

  public ReturnCandidateGroupDelegate(CamundaEventBus camundaEventBus) {
    this.camundaEventBus = camundaEventBus;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    String candidateGroup = CANDIDATE_GROUP.getValue(execution);
    String topic = TOPIC.getValue(execution);

    camundaEventBus.get().notify(topic, Event.wrap(CandidateGroup.of(candidateGroup)));
  }
}
