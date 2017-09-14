package holunda.taskassignment.plugin.dmn;

import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.CandidateGroup;
import org.camunda.bpm.dmn.engine.impl.DmnEvaluationException;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.spring.boot.starter.test.helper.StandaloneInMemoryTestConfiguration;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@Deployment(resources = "Box.dmn")
@Ignore
public class EvaluateDecisionTableTest {

  @Rule
  public final ProcessEngineRule camunda = new ProcessEngineRule(new StandaloneInMemoryTestConfiguration().buildProcessEngine());

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  private EvaluateDecisionTable evaluate;
  private BusinessData businessData;

  @Before
  public void setUp() throws Exception {


    evaluate = new EvaluateDecisionTable(camunda.getDecisionService());
  }

  @Test
  public void evaluate_3() throws Exception {

    final HashMap<String,Integer> data = new HashMap<>();
    data.put("in", 3);
    businessData = new BusinessData(data);

    CandidateGroup result = evaluate.apply("Box", businessData);


    assertThat(result).isNotNull();
    assertThat(result.isNotEmpty()).isTrue();
    assertThat(result.getName()).isEqualTo("foo");

  }

  @Test
  public void evaluate_0() throws Exception {

    final HashMap<String,Integer> data = new HashMap<>();
    data.put("in", 0);
    businessData = new BusinessData(data);

    CandidateGroup result = evaluate.apply("Box", businessData);


    assertThat(result).isNotNull();
    assertThat(result.isNotEmpty()).isTrue();
    assertThat(result.getName()).isEqualTo("bar");

  }

  @Test
  public void evaluate_table_wrong() throws Exception {

    thrown.expect(ProcessEngineException.class);
    thrown.expectMessage("no decision definition deployed with key 'Box1'");

    final HashMap<String,Integer> data = new HashMap<>();
    data.put("in", 3);
    businessData = new BusinessData(data);

    CandidateGroup result = evaluate.apply("Box1", businessData);
  }

  @Test
  public void evaluate_input_wrong() throws Exception {

    thrown.expect(ProcessEngineException.class);
    thrown.expectCause(CoreMatchers.is(DmnEvaluationException.class));

    final HashMap<String,Integer> data = new HashMap<>();
    data.put("abc", 3);
    businessData = new BusinessData(data);

    CandidateGroup result = evaluate.apply("Box", businessData);
  }


}
