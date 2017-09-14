package holunda.taskassignment.plugin.term;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class Term {

  private String type;

  private Map<String, String> expressions;

  private String result;
}
