package holunda.taskassignment.process.listener;

import holunda.taskassignment.api.model.Variable;
import holunda.taskassignment.business.jpa.PackageRepository;
import holunda.taskassignment.business.jpa.entity.PackageEntity;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.extension.reactor.bus.CamundaSelector;
import org.camunda.bpm.extension.reactor.spring.listener.ReactorExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SetTypeListener implements ExecutionListener {

  @Autowired
  private PackageRepository packageRepository;

  @Override
  public void notify(final DelegateExecution execution) throws Exception {
    final PackageEntity packageEntity = packageRepository.findOne(execution.getProcessBusinessKey());

    log.info("process started for: {}", packageEntity);

    Variable.TYPE.setValue(execution, packageEntity.getType());
  }
}
