package de.holisticon.camunda.example.customquery.process.task;

import de.holisticon.camunda.example.customquery.CamundaCustomQueryApplication;
import de.holisticon.camunda.example.customquery.process.OrderProcess;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareAssertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CamundaCustomQueryApplication.class, webEnvironment = NONE)
public class TaskWithOrderRepositoryTest {

  @Autowired
  private OrderProcess orderProcess;

  @Autowired
  private TaskWithOrderRepository taskWithOrderRepository;

  @Autowired
  private EntityManager em;

  @Test
  public void find_task() throws Exception {
    ProcessInstance processInstance = orderProcess.start("1", "jan", Arrays.asList("1", "2"));
    assertThat(processInstance).isWaitingAt("task_pack_order");


    assertThat(taskWithOrderRepository.findAll()).hasSize(1);
    TaskWithOrder task = taskWithOrderRepository.findAll().get(0);

    assertThat(task.getOrder().getPositions().get(0).getBook().getPrice()).isEqualTo(40);
  }
}
