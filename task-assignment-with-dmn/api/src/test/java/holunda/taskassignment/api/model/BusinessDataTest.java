package holunda.taskassignment.api.model;

import org.camunda.bpm.engine.variable.VariableMap;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BusinessDataTest {

  @Test
  public void transform() throws Exception {
    final Map<String, Integer> businessData = new HashMap<>();
    businessData.put("foo", 1);
    businessData.put("bar", null);

    final BusinessData data = new BusinessData(businessData);

    Map<String, Object> variables = data.toVariables();

    assertThat(variables.get("foo")).isEqualTo(1);
    assertThat(variables.get("bar")).isNull();
  }
}
