package holunda.taskassignment.plugin.process.consumer;

import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.springframework.stereotype.Component;

/**
 * Creates a new {@link CandidateGroupConsumer}.
 */
@Component
public class CandidateGroupConsumerFactory {

  private final CamundaEventBus camundaEventBus;
  private final RuntimeService runtimeService;

  public CandidateGroupConsumerFactory(final CamundaEventBus camundaEventBus, final RuntimeService runtimeService) {
    this.camundaEventBus = camundaEventBus;
    this.runtimeService = runtimeService;
  }

  public CandidateGroupConsumer create(final TaskAssignmentCommand command) {
    return new CandidateGroupConsumer(command, camundaEventBus, runtimeService);
  }
}
