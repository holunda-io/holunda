package holunda.taskassignment.plugin.api;

import holunda.taskassignment.api.model.BusinessKey;
import holunda.taskassignment.plugin.process.TaskAssignmentProcess;
import holunda.taskassignment.plugin.process.TaskAssignmentProcess.VARIABLES;
import lombok.Builder;
import lombok.Value;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import java.io.Serializable;
import java.util.UUID;

@Value
@Builder
public class TaskAssignmentCommand implements Serializable {

  public static String createTopic(final String taskId, final UUID uuid) {
    return String.format("/%s/%s/%s",
      TaskAssignmentCommand.class.getSimpleName(),
      taskId,
      uuid);
  }

  public static TaskAssignmentCommand from(final DelegateTask task) {
    return TaskAssignmentCommand.builder()
      .taskId(task.getId())
      .taskDefinitionKey(task.getTaskDefinitionKey())
      .businessKey(new BusinessKey(task.getExecution().getProcessBusinessKey()))
      .uuid(UUID.randomUUID())
      .build();
  }

  private String taskId;
  private UUID uuid;
  private BusinessKey businessKey;
  private String taskDefinitionKey;

  public String getTopic() {
    return createTopic(taskId, uuid);
  }

  public VariableMap toMap() {
    return Variables
      .putValue(VARIABLES.BUSINESS_KEY.name(), businessKey.getValue())
      .putValue(VARIABLES.TASK_DEFINITION_KEY.name(), taskDefinitionKey)
      .putValue(VARIABLES.TOPIC.name(), getTopic());
  }
}
