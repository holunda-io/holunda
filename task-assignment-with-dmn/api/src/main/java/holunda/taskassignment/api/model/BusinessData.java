package holunda.taskassignment.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Value
public class BusinessData implements Serializable {

  private Map<String,Integer> data;

  public Map<String,Object> toVariables() {
    final HashMap<String,Object> variables = new HashMap<>();

    for (Map.Entry<String,Integer> entry : getData().entrySet()) {
      variables.put(entry.getKey(), entry.getValue());
    }

    return variables;
  }
}
