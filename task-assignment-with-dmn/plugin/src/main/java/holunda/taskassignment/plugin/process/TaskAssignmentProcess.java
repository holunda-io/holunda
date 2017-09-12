package holunda.taskassignment.plugin.process;

import holunda.taskassignment.api.model.CandidateGroup;
import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import holunda.taskassignment.plugin.api.Variable;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_CREATE;
import static reactor.bus.selector.Selectors.$;

/**
 * Class resembling the assignment process. Registers on {@link CamundaEventBus} and is called
 * everytime a task gets created.
 */
@Component
@Slf4j
public class TaskAssignmentProcess implements Function<TaskAssignmentCommand, CandidateGroup> {

  /**
   * Processvariable contstants for process.
   */
  public enum VARIABLES {
    ;

    public static final Variable<String> BUSINESS_KEY = Variable.of("businessKey");
    public static final Variable<Map<String, Integer>> BUSINESS_DATA = Variable.of("businessData");
    public static final Variable<String> TASK_DEFINITION_KEY = Variable.of("taskDefinitionKey");
    public static final Variable<String> DMN_TABLE = Variable.of("dmnTable");
    public static final Variable<String> TOPIC = Variable.of("topic");
    public static final Variable<String> CANDIDATE_GROUP = Variable.of("candidateGroup");
  }

  public enum PROCESS {
    ;
    /**
     * The processDefinitionKey.
     */
    public static final String KEY = "TaskAssignmentProcess";

    /**
     * The process bpmn-resource.
     */
    public static final String BPMN = KEY + ".bpmn";
  }

  /**
   * Fallback group to chose when task assignment fails.
   */
  public static final CandidateGroup FALLBACK_GROUP = new CandidateGroup("dispatcher");

  /**
   * Selector for "task#create".
   */
  public static final SelectorBuilder ON_TASK_CREATE = SelectorBuilder.selector()
    .bpmn()
    .task()
    .event(EVENTNAME_CREATE);

  /**
   * Consumer that listens to candidateGroupEvent fired at process end to synchronously report the task
   * assignment result back to the caller.
   */
  class CandidateGroupConsumer implements Consumer<Event<CandidateGroup>>, Supplier<CandidateGroup> {

    private final AtomicReference<CandidateGroup> candidateGroup = new AtomicReference<>();

    public CandidateGroupConsumer(final TaskAssignmentCommand command) {
      log.debug("subscribing to '{}'", command.getTopic());

      camundaEventBus.get()
        .on($(command.getTopic()), this)
        .cancelAfterUse();

      runtimeService.startProcessInstanceByKey(
        PROCESS.KEY,
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

  private final CamundaEventBus camundaEventBus;
  private final RuntimeService runtimeService;

  public TaskAssignmentProcess(final CamundaEventBus camundaEventBus, final RuntimeService runtimeService) {
    this.camundaEventBus = camundaEventBus;
    this.runtimeService = runtimeService;

    camundaEventBus.register(ON_TASK_CREATE,
      (TaskListener) delegateTask -> {
        final TaskAssignmentCommand command = TaskAssignmentCommand.from(delegateTask);
        CandidateGroup candidateGroup = this.apply(command);
        delegateTask.addCandidateGroup(candidateGroup.getName());
      });
  }

  @Override
  public CandidateGroup apply(final TaskAssignmentCommand command) {

    return Try.ofSupplier(new CandidateGroupConsumer(command))
      .filter(CandidateGroup::isNotEmpty)
      .getOrElse(FALLBACK_GROUP);
  }
}
