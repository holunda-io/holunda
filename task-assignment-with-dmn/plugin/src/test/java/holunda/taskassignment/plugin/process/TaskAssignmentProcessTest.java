package holunda.taskassignment.plugin.process;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessKey;
import holunda.taskassignment.plugin.PluginConfiguration;
import holunda.taskassignment.plugin.PluginTestConfiguration;
import holunda.taskassignment.plugin.TestApplication;
import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import holunda.taskassignment.plugin.process.delegate.DetermineDmnTableDelegate;
import holunda.taskassignment.plugin.process.delegate.EvaluateDmnDelegate;
import holunda.taskassignment.plugin.process.delegate.LoadRequiredDataDelegate;
import holunda.taskassignment.plugin.process.delegate.ReturnCandidateGroupDelegate;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.extension.mockito.CamundaMockito;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.camunda.bpm.spring.boot.starter.test.helper.StandaloneInMemoryTestConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.FALLBACK_GROUP;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@Deployment(resources = {
  TaskAssignmentProcess.PROCESS.BPMN,
  "TestProcess.bpmn"
})
public class TaskAssignmentProcessTest {

  @Rule
  @Autowired
  public ProcessEngineRule camunda;

  @Autowired
  private CamundaEventBus camundaEventBus;

  @Autowired
  private TaskAssignmentProcess process;

  @Autowired
  private RuntimeService runtimeService;

  @MockBean
  private BusinessDataService businessDataService;

  @Test
  public void start_via_command() throws Exception {
    final ProcessInstance instance = runtimeService.startProcessInstanceByKey("TestProcess", "1");
    assertThat(instance).isWaitingAt("TestTask");

    assertThat(task()).hasCandidateGroup(FALLBACK_GROUP.getName());

  }
}
