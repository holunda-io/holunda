package holunda.taskassignment.plugin.context;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.plugin.TestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class RequireNewTransactionTest {

  @Autowired
  private RequireNewTransaction transactionWrapper;

  @MockBean
  private BusinessDataService businessDataService;

  @Test
  public void requireNewTransaction() throws Exception {
    final DoSomething doIt = new DoSomething();
    final String testReturn = transactionWrapper.requireNewTransaction(doIt).apply("test");
    assertThat(testReturn).isEqualTo("test");
  }

  public static class DoSomething implements Function<String,String> {

    @Override
    public String apply(String s) {
      return s;
    }
  }

}
