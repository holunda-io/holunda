package holunda.taskassignment.plugin.process;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.Variable;
import holunda.taskassignment.plugin.TestApplication;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.extension.reactor.bus.CamundaEventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static holunda.taskassignment.plugin.process.TaskAssignmentProcess.FALLBACK_GROUP;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;
import static org.camunda.bpm.engine.variable.Variables.putValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class TaskAssignmentProcessTest {

  @Autowired
  private CamundaEventBus camundaEventBus;

  @Autowired
  private TaskAssignmentProcess process;

  @Autowired
  private RuntimeService runtimeService;

  @MockBean
  private BusinessDataService businessDataService;

  @Before
  public void setUp() throws Exception {
    Map<String,Integer> data = new HashMap<>();
    data.put("in", 3);
    when(businessDataService.loadBusinessData(any(), anySet())).thenReturn(new BusinessData(data));

  }

  @Test
  public void start_via_command() throws Exception {

    final ProcessInstance instance = runtimeService.startProcessInstanceByKey("TestProcess",
      "1",
      putValue(Variable.TYPE.name(), "Box")
    );
    assertThat(instance).isWaitingAt("TestTask");

    assertThat(task()).hasCandidateGroup(FALLBACK_GROUP.getName());

  }
}
