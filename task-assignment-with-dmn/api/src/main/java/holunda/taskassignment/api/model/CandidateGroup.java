package holunda.taskassignment.api.model;

import lombok.Value;

import java.io.Serializable;

@Value
public class CandidateGroup implements Serializable {

  public static CandidateGroup empty() {
    return new CandidateGroup(null);
  }

  private String name;

  public boolean isNotEmpty() {
    return name != null;
  }
}
