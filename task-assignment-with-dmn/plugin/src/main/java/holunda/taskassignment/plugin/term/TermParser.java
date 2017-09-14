package holunda.taskassignment.plugin.term;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TermParser implements Function<String, Term> {


  public static final String PATTERN = "^([a-zA-Z]+)([<>=]{1,2})(\\d+)";

  // (box|sphere|all): n*(+ key-op-number) := group
  // box: width>=7 & height<20 & weight=10 := foo
  @Override
  public Term apply(String term) {
    term = term.replaceAll(" ", "");

    String[] parts = term.split(":");
    Assert.isTrue(parts.length == 3, "no valid term");

    Term.TermBuilder b = Term.builder();
    b.type(parts[0]);
    b.result(parts[2].replaceFirst("=", ""));

    Map<String, String> expressions = new HashMap<>();

    Arrays.asList(parts[1].split("&")).forEach((exp) -> {
      String key = exp.replaceAll(PATTERN, "$1");

      String operator = exp.replaceAll(PATTERN, "$2");

      String number = exp.replaceAll(PATTERN, "$3");

      if (!"=".equals(operator)) {
        number = operator + number;
      }

      expressions.put(key, number);
    });
    b.expressions(expressions);

    return b.build();
  }

}
