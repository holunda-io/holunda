package holunda.taskassignment.plugin.process;

import holunda.taskassignment.plugin.api.CandidateGroup;
import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import holunda.taskassignment.plugin.process.delegate.DetermineDmnTableDelegate;
import holunda.taskassignment.plugin.process.delegate.EvaluateDmnDelegate;
import holunda.taskassignment.plugin.process.delegate.LoadRequiredDataDelegate;
import holunda.taskassignment.plugin.process.delegate.ReturnCandidateGroupDelegate;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.CamundaMockito;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.camunda.bpm.spring.boot.starter.test.helper.StandaloneInMemoryTestConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;

@Deployment(resources = TaskAssignmentProcess.BPMN)
public class TaskAssignmentProcessTest {

  private final CamundaEventBus camundaEventBus = new CamundaEventBus();

  @Rule
  public final ProcessEngineRule camunda = new StandaloneInMemoryTestConfiguration().rule();

  private TaskAssignmentProcess process;

  @Before
  public void setUp() throws Exception {
   process = new TaskAssignmentProcess(camundaEventBus, runtimeService());

    CamundaMockito.registerInstance(new DetermineDmnTableDelegate());
    CamundaMockito.registerInstance(new LoadRequiredDataDelegate());
    CamundaMockito.registerInstance(new EvaluateDmnDelegate());
    CamundaMockito.registerInstance(new ReturnCandidateGroupDelegate(camundaEventBus));

  }

  @Test
  public void start_via_command() throws Exception {
    TaskAssignmentCommand command = TaskAssignmentCommand.builder()
      .taskId("1")
      .taskDefinitionKey("task")
      .businessKey("12345")
      .uuid(UUID.randomUUID())
      .build();

    assertThat(process.apply(command).get()).isEqualTo("bar");

  }
}
