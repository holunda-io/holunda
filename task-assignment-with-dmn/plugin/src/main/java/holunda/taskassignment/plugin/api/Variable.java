package holunda.taskassignment.plugin.api;

import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;

import java.util.Map;

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

  default T getValue(final Map<String,Object> variables) {
    return (T) variables.get(name());
  }
}
