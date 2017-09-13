package holunda.taskassignment.plugin.process.delegate;

import holunda.taskassignment.api.model.CandidateGroup;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.springframework.stereotype.Component;
import reactor.bus.Event;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.CANDIDATE_GROUP;
import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES.TOPIC;

/**
 * Returns candidateGroup back to {@link holunda.taskassignment.plugin.process.consumer.CandidateGroupConsumer}
 * using eventBus.
 */
@Slf4j
@Component("returnCandidateGroup")
public class ReturnCandidateGroupDelegate implements JavaDelegate{

  private final CamundaEventBus camundaEventBus;

  public ReturnCandidateGroupDelegate(final CamundaEventBus camundaEventBus) {
    this.camundaEventBus = camundaEventBus;
  }

  @Override
  public void execute(final DelegateExecution execution) throws Exception {
    log.info("\n-----\n returnCandidateGroup \n-----\n");

    final CandidateGroup candidateGroup = new CandidateGroup(CANDIDATE_GROUP.getValue(execution));
    final String topic = TOPIC.getValue(execution);

    camundaEventBus.get().notify(topic, Event.wrap(candidateGroup));
  }
}
