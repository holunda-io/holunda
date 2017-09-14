package holunda.taskassignment.api.model;

import lombok.Value;

import java.io.Serializable;

/**
 * Value bean for the global business is, used for business model and camunda processes.
 */
@Value
public class BusinessKey implements Serializable {

  private String value;

}
