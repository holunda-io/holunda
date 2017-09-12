package holunda.taskassignment.api.model;

import lombok.Value;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import java.io.Serializable;
import java.util.Map;

@Value
public class BusinessData implements Serializable {

  private Map<String,Integer> data;

  public VariableMap toVariables() {
    final VariableMap variables = Variables.createVariables();

    for (Map.Entry<String,Integer> entry : getData().entrySet()) {
      variables.putValue(entry.getKey(), entry.getValue());
    }

    return variables;
  }
}
