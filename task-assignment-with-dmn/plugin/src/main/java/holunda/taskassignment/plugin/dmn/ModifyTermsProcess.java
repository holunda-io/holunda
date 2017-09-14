package holunda.taskassignment.plugin.dmn;

import holunda.taskassignment.plugin.jpa.entity.TermEntity;
import holunda.taskassignment.plugin.jpa.entity.TermRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ModifyTermsProcess implements JavaDelegate {

  public static final String PROCESS_KEY = "modifyTerms";
  public static final String PROCESS_FILE = PROCESS_KEY + ".bpmn";
  private final BpmnModelInstance process;

  public ModifyTermsProcess() {
    this.process = Bpmn.createExecutableProcess(PROCESS_KEY)
      .startEvent().camundaFormField()
      .camundaId("term")
      .camundaLabel("Term")
      .camundaType("string")
      .camundaFormFieldDone()
      .serviceTask("createTerm").name("Create Term").camundaDelegateExpression("${modifyTermsProcess}")
      .endEvent()
      .done();
  }

  @EventListener
  void deploy(PostDeployEvent event) {
    event.getProcessEngine().getRepositoryService()
      .createDeployment()
      .addModelInstance(PROCESS_FILE, process)
      .deploy();
  }

  @Autowired
  private TermRepository termRepository;

  @Autowired
  private GenerateDmnTables generateDmnTables;


  @Override
  public void execute(DelegateExecution execution) throws Exception {
    String term = (String) execution.getVariable("term");

    Assert.isTrue(term != null, "term is null!");
    termRepository.save(new TermEntity(term));

    generateDmnTables.run();
  }
}
