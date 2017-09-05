package holunda.taskassignment.plugin.api;

import holunda.taskassignment.plugin.process.TaskAssignmentProcess;
import org.camunda.bpm.extension.mockito.delegate.DelegateExecutionFake;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskAssignmentCommandTest {

  private final DelegateExecutionFake execution = new DelegateExecutionFake();

  @Test
  public void creates_topic() throws Exception {
    String taskId = "12345";
    UUID uuid = UUID.randomUUID();

    String topic = TaskAssignmentCommand.createTopic(taskId, uuid);

    assertThat(topic).isEqualTo("/TaskAssignmentCommand/" + taskId + "/" + uuid);
  }

  @Test
  public void creates_command() throws Exception {

  }
}
