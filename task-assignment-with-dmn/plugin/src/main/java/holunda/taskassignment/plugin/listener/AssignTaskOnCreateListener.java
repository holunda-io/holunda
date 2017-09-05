package holunda.taskassignment.plugin.listener;

import holunda.taskassignment.plugin.api.CandidateGroup;
import holunda.taskassignment.plugin.api.TaskAssignmentCommand;
import holunda.taskassignment.plugin.process.TaskAssignmentProcess;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.camunda.bpm.extension.reactor.spring.listener.ReactorTaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@CamundaSelector(event = TaskListener.EVENTNAME_CREATE)
public class AssignTaskOnCreateListener extends ReactorTaskListener {

  @Autowired
  private TaskAssignmentProcess taskAssignmentProcess;

  @Override
  public void notify(final DelegateTask delegateTask) {
    CandidateGroup candidateGroup = taskAssignmentProcess.apply(TaskAssignmentCommand.from(delegateTask));
    delegateTask.addCandidateGroup(candidateGroup.get());
  }
}
