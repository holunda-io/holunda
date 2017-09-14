package holunda.taskassignment.plugin.dmn;

import holunda.taskassignment.plugin.term.Term;
import holunda.taskassignment.plugin.term.TermParser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TermParserTest {

  private static final String T1 = "  box  :    width > = 7   &   height < 20 &   weight =   10  : =  fooGroup";

  private final TermParser parser = new TermParser();

  @Test
  public void parseT1() throws Exception {
    Term term = parser.apply(T1);
    assertThat(term.getType()).isEqualTo("box");
    assertThat(term.getExpressions()).containsKeys("width", "height", "weight");
    assertThat(term.getResult()).isEqualTo("fooGroup");

    assertThat(term.getExpressions().get("width")).isEqualTo(">=7");
    assertThat(term.getExpressions().get("height")).isEqualTo("<20");
    assertThat(term.getExpressions().get("weight")).isEqualTo("10");
  }
}
