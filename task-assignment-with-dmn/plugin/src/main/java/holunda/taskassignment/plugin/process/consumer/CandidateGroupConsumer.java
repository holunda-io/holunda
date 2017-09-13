package holunda.taskassignment.plugin.process.consumer;

import holunda.taskassignment.api.model.CandidateGroup;
import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import holunda.taskassignment.plugin.process.TaskAssignmentProcess;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static reactor.bus.selector.Selectors.$;

/**
 * Consumer that listens to candidateGroupEvent fired at process end to synchronously report the task
 * assignment result back to the caller.
 *
 * Attention: not a spring bean, create via {@link CandidateGroupConsumerFactory#create(TaskAssignmentCommand)}.
 */
@Slf4j
class CandidateGroupConsumer implements Consumer<Event<CandidateGroup>>, Supplier<CandidateGroup> {

  private final AtomicReference<CandidateGroup> candidateGroup = new AtomicReference<>();

  public CandidateGroupConsumer(final TaskAssignmentCommand command,
                                final CamundaEventBus camundaEventBus,
                                final RuntimeService runtimeService) {
    log.debug("subscribing to '{}'", command.getTopic());

    camundaEventBus.get()
      .on($(command.getTopic()), this)
      .cancelAfterUse();

    runtimeService.startProcessInstanceByKey(
      TaskAssignmentProcess.PROCESS.KEY,
      command.getTaskId(),
      command.toMap());
  }

  @Override
  public void accept(final Event<CandidateGroup> event) {
    candidateGroup.set(event.getData());
  }

  @Override
  public CandidateGroup get() {
    return Objects.requireNonNull(candidateGroup.get(), "candidateGroup not initialized!");
  }
}
