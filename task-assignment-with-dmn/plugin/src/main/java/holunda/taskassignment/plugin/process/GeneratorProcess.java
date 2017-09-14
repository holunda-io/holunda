package holunda.taskassignment.plugin.process;

import holunda.taskassignment.plugin.dmn.GenerateDmnTables;
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

/**
 * Inline maintenance process that is deployed on engine start and can be manually started to generate dmn tables
 * based on terms found in {@link holunda.taskassignment.plugin.term.TermRepository}.
 */
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
