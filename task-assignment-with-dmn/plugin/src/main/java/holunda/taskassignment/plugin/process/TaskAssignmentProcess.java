package holunda.taskassignment.plugin.process;

import holunda.taskassignment.api.model.CandidateGroup;
import holunda.taskassignment.api.model.Variable;
import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import holunda.taskassignment.plugin.process.consumer.CandidateGroupConsumerFactory;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.bus.SelectorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

import static org.camunda.bpm.engine.delegate.TaskListener.EVENTNAME_CREATE;

/**
 * Class resembling the assignment process. Registers on {@link CamundaEventBus} and is called
 * every time a task gets created.
 */
@Component
@Slf4j
public class TaskAssignmentProcess implements Function<TaskAssignmentCommand, CandidateGroup> {

  /**
   * Process variable contstants for process.
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

  private final CandidateGroupConsumerFactory consumerFactory;

  public TaskAssignmentProcess(CandidateGroupConsumerFactory consumerFactory) {
    this.consumerFactory = consumerFactory;
  }

  /**
   * Registers the {@link #apply(TaskAssignmentCommand)} method to run on each
   * task creation.
   *
   * @param camundaEventBus the camundaEventBus to register on
   */
  @Autowired
  void registerOnTaskCreate(CamundaEventBus camundaEventBus) {
    camundaEventBus.register(SelectorBuilder.selector().bpmn().task().event(EVENTNAME_CREATE),
      (TaskListener) delegateTask -> {
        final TaskAssignmentCommand command = TaskAssignmentCommand.from(delegateTask);
        CandidateGroup candidateGroup = this.apply(command);
        delegateTask.addCandidateGroup(candidateGroup.getName());
      });
  }

  @Override
  public CandidateGroup apply(final TaskAssignmentCommand command) {
    return Try.ofSupplier(consumerFactory.create(command))
      .filter(CandidateGroup::isNotEmpty)
      .getOrElse(FALLBACK_GROUP);
  }
}
