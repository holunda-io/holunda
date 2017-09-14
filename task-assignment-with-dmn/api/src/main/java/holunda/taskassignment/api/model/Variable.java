package holunda.taskassignment.api.model;

import org.camunda.bpm.engine.delegate.VariableScope;
import sun.awt.SunHints;

import java.util.Map;

/**
 * Abstraction for read/write access to typed process variables.
 *
 * @param <T> type of the variable
 */
public interface Variable<T> {

  /**
   * Global variable type that has to be set on every instance.
   */
  Variable<String> TYPE = Variable.of("type");

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
