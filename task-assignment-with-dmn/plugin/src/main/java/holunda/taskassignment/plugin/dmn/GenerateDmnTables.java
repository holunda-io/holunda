package holunda.taskassignment.plugin.dmn;

import holunda.taskassignment.plugin.term.Term;
import holunda.taskassignment.plugin.term.TermParser;
import holunda.taskassignment.plugin.term.TermRepository;
import holunda.taskassignment.plugin.term.entity.TermEntity;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.HitPolicy;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.camunda.bpm.model.dmn.instance.Input;
import org.camunda.bpm.model.dmn.instance.InputEntry;
import org.camunda.bpm.model.dmn.instance.InputExpression;
import org.camunda.bpm.model.dmn.instance.Output;
import org.camunda.bpm.model.dmn.instance.OutputEntry;
import org.camunda.bpm.model.dmn.instance.Rule;
import org.camunda.bpm.model.dmn.instance.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.camunda.bpm.model.dmn.impl.DmnModelConstants.DMN_ELEMENT_DECISION_TABLE;

@Component
@Slf4j
public class GenerateDmnTables implements Runnable {

  public static DmnModelInstance generateTable(String name, List<Term> terms) {
    // Build DMN model
    DmnModelInstance dmnModelInstance = DmnUtils.initializeEmptyDmnModel();
    Decision decision = DmnUtils.generateNamedElement(dmnModelInstance, Decision.class, name);
    dmnModelInstance.getDefinitions().addChildElement(decision);
    DecisionTable decisionTable = DmnUtils.generateElement(dmnModelInstance, DecisionTable.class, DMN_ELEMENT_DECISION_TABLE);
    decisionTable.setHitPolicy(HitPolicy.FIRST);
    decision.addChildElement(decisionTable);

    //Build Header
    Set<String> inputs = new LinkedHashSet<>();

    for (Term t : terms) {
      Map<String, String> exp = t.getExpressions();
      for (String k : exp.keySet()) {
        inputs.add(k);
      }
    }

    inputs.forEach(k -> {
      final Input input = DmnUtils.generateElement(dmnModelInstance, Input.class);
      input.setLabel(k);
      decisionTable.addChildElement(input);

      final InputExpression inputExpression = DmnUtils.generateElement(dmnModelInstance, InputExpression.class);
      final Text text = DmnUtils.generateText(dmnModelInstance, k);
      inputExpression.setText(text);
      inputExpression.setTypeRef("integer");
      input.setInputExpression(inputExpression);
    });

    final Output candidateGroupOutput = DmnUtils.generateElement(dmnModelInstance, Output.class);
    candidateGroupOutput.setName("candidateGroup");
    candidateGroupOutput.setLabel("Candidate Group");
    candidateGroupOutput.setTypeRef("string");
    decisionTable.addChildElement(candidateGroupOutput);

    // Build Body
    terms.forEach(t -> {
      final Rule rule = DmnUtils.generateElement(dmnModelInstance, Rule.class);
      decisionTable.addChildElement(rule);

      Map<String, String> expr = t.getExpressions();


      inputs.forEach(k -> {
        final InputEntry inputEntry = DmnUtils.generateElement(dmnModelInstance, InputEntry.class);
        inputEntry.setText(DmnUtils.generateText(dmnModelInstance, expr.get(k)));
        rule.addChildElement(inputEntry);
      });

      final OutputEntry outputEntry = DmnUtils.generateElement(dmnModelInstance, OutputEntry.class);
      outputEntry.setText(DmnUtils.generateText(dmnModelInstance, '"' + t.getResult() + '"'));
      rule.addChildElement(outputEntry);

    });

    return dmnModelInstance;
  }

  @Autowired
  private TermRepository termRepository;

  @Autowired
  private TermParser termParser;

  @Autowired
  private RepositoryService repositoryService;


  @Override
  public void run() {
    final List<Term> terms = termRepository.findAll()
      .stream()
      .map(TermEntity::getTerm)
      .map(termParser)
      .collect(toList());

    log.info("terms: {}", terms);

    final Map<String, List<Term>> map = terms.stream().collect(Collectors.groupingBy(Term::getType));
    final List<Term> all = map.getOrDefault("all", new ArrayList<>());

    final DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
    for (Map.Entry<String, List<Term>> e : map.entrySet()) {
      if ("all".equals(e.getKey())) {
        continue;
      }

      e.getValue().addAll(all);

      final DmnModelInstance dmnModelInstance = generateTable(e.getKey(), e.getValue());

      deploymentBuilder.addModelInstance(e.getKey() + ".dmn", dmnModelInstance);
    }

    if(deploymentBuilder.getResourceNames().size() > 0) {
      deploymentBuilder.deploy();
    }

  }

}
