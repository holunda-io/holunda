package holunda.taskassignment.plugin.process;

import holunda.taskassignment.plugin.dmn.GenerateDmnTables;
import holunda.taskassignment.plugin.term.TermRepository;
import holunda.taskassignment.plugin.term.entity.TermEntity;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Inline maintenance process that is deployed on engine start and can be manually started to
 * add term expression to the {@link TermRepository}.
 */
@Component
public class AddDecisionProcess implements JavaDelegate {

  public static final String PROCESS_KEY = "addDecision";
  public static final String PROCESS_FILE = "AddDecision.bpmn";

  @Autowired
  private TermRepository termRepository;

  @Autowired
  private GenerateDmnTables generateDmnTables;

  private final BpmnModelInstance process;

  public AddDecisionProcess() {
    this.process = Bpmn.createExecutableProcess(PROCESS_KEY).name("_Add Decision")
      .startEvent().camundaFormField()
      .camundaId("term")
      .camundaLabel("Decision")
      .camundaType("string")
      .camundaFormFieldDone()
      .serviceTask("createTerm").name("Create Term").camundaDelegateExpression("${addDecisionProcess}")
      .endEvent()
      .done();
  }

  @EventListener
  void deploy(final PostDeployEvent event) {
    event.getProcessEngine().getRepositoryService()
      .createDeployment()
      .addModelInstance(PROCESS_FILE, process)
      .deploy();
  }


  @Override
  public void execute(DelegateExecution execution) throws Exception {
    String term = (String) execution.getVariable("term");

    Assert.isTrue(term != null, "term is null!");
    termRepository.save(new TermEntity(term));

    generateDmnTables.run();
  }
}
