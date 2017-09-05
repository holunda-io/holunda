package holunda.taskassignment.plugin.api;

import org.camunda.bpm.engine.delegate.VariableScope;

public interface Variable<T> {

  static <T> Variable<T> of(String name) {
    return () -> name;
  }

  String name();

  default T getValue(VariableScope scope) {
    return (T) scope.getVariable(name());
  }

  default void setValue(VariableScope scope, T value) {
    scope.setVariable(name(), value);
  }
}
