package holunda.taskassignment.plugin.api;

import java.util.function.Supplier;

public interface CandidateGroup extends Supplier<String> {

  static CandidateGroup of(String name) {
    return () -> name;
  }

}
