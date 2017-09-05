package holunda.taskassignment.plugin.process;

import holunda.taskassignment.plugin.api.CandidateGroup;
import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import holunda.taskassignment.plugin.api.Variable;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static holunda.taskassignment.plugin.api.TaskAssignmentCommand.createTopic;
import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_CREATE;
import static reactor.bus.selector.Selectors.$;

@Component
public class TaskAssignmentProcess implements Function<TaskAssignmentCommand, CandidateGroup> {

  public enum VARIABLES {
    ;

    public static final Variable<String> BUSINESS_KEY = Variable.of("businessKey");
    public static final Variable<String> TASK_DEFINITION_KEY = Variable.of("taskDefinitionKey");
    public static final Variable<String> TOPIC = Variable.of("topic");
    public static final Variable<String> CANDIDATE_GROUP = Variable.of("candidateGroup");
  }

  public static final CandidateGroup FALLBACK_GROUP = CandidateGroup.of("dispatcher");
  private static final Logger log = LoggerFactory.getLogger(CandidateGroupConsumer.class);
  public static final String KEY = "TaskAssignmentProcess";
  public static final String BPMN = KEY + ".bpmn";


  public static final SelectorBuilder ON_TASK_CREATE = SelectorBuilder.selector()
    .bpmn()
    .task()
    .event(EVENTNAME_CREATE);

  public static class CandidateGroupConsumer implements Consumer<Event<CandidateGroup>>, Supplier<CandidateGroup> {


    public static CandidateGroupConsumer register(final CamundaEventBus eventBus, final TaskAssignmentCommand command) {
      log.debug("subscribing to '{}'", command.getTopic());

      final CandidateGroupConsumer consumer = new CandidateGroupConsumer();
      eventBus.get().on($(command.getTopic()), consumer).cancelAfterUse();

      return consumer;
    }

    private CandidateGroup candidateGroup;

    @Override
    public void accept(final Event<CandidateGroup> event) {
      candidateGroup = event.getData();
    }

    @Override
    public CandidateGroup get() {
      return Objects.requireNonNull(candidateGroup, "candidateGroup not initialized!");
    }
  }

  private final CamundaEventBus camundaEventBus;
  private final RuntimeService runtimeService;

  public TaskAssignmentProcess(CamundaEventBus camundaEventBus, RuntimeService runtimeService) {
    this.camundaEventBus = camundaEventBus;
    this.runtimeService = runtimeService;

    camundaEventBus.register(ON_TASK_CREATE,
      (TaskListener) delegateTask -> {
        TaskAssignmentCommand command = TaskAssignmentCommand.from(delegateTask);
        CandidateGroup candidateGroup = this.apply(command);
        delegateTask.addCandidateGroup(candidateGroup.get());
      });
  }


  @Override
  public CandidateGroup apply(final TaskAssignmentCommand command) {
    final CandidateGroupConsumer consumer = CandidateGroupConsumer.register(camundaEventBus, command);
    CandidateGroup candidateGroup = null;
    try {
      runtimeService.startProcessInstanceByKey(
        KEY,
        command.getTaskId(),
        command.toMap()
      );

      candidateGroup = consumer.get();
    } catch (Exception e) {
      log.error("TaskAssignment failed, using fallback - {} {}, exception={}", command, FALLBACK_GROUP, e.getMessage());
      candidateGroup = FALLBACK_GROUP;
    }
    finally {
      camundaEventBus.get().getConsumerRegistry().unregister(consumer);
    }
    return candidateGroup;
  }
}
