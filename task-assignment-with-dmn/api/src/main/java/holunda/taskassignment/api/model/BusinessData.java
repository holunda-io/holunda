package holunda.taskassignment.api.model;

import lombok.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic representation of business data values, so the assignment process does not need to know
 * the concrete model implementation.
 */
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
