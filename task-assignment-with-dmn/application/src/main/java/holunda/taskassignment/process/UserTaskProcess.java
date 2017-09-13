package holunda.taskassignment.process;

import holunda.taskassignment.api.model.Variable;
import org.springframework.stereotype.Component;

@Component
public class UserTaskProcess {

  public enum VARIABLES {
    ;

    public static final Variable<String> TYPE = Variable.TYPE;
  }
}
