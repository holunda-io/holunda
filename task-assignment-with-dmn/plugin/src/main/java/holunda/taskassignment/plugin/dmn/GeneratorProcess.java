package holunda.taskassignment.plugin.dmn;

import holunda.taskassignment.plugin.jpa.entity.TermEntity;
import holunda.taskassignment.plugin.jpa.entity.TermRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class GeneratorProcess implements JavaDelegate {

  public static final String PROCESS_KEY = "generateDmn";
  public static final String PROCESS_FILE = PROCESS_KEY + ".bpmn";

  private final BpmnModelInstance process;
  private Deployment deployment;

  public GeneratorProcess() {
    this.process = Bpmn.createExecutableProcess(PROCESS_KEY)
      .startEvent()
      .serviceTask("generate").camundaDelegateExpression("${generatorProcess}")
      .endEvent()
      .done();

  }

  @EventListener
  public void deploy(PostDeployEvent event) {
      deployment = event.getProcessEngine().getRepositoryService().createDeployment()
        .addModelInstance(PROCESS_FILE, process)
        .deploy();
  }

  @Autowired
  private GenerateDmnTables generateDmnTables;

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    generateDmnTables.run();
  }
}
