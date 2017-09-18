package holunda.taskassignment.plugin.context;


import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

import java.util.Properties;

/**
 * Wraps target call with new transaction.
 * Required for "straight through" processes, otherwise transaction is rolled back even
 * when the exception is caught.
 */
@Component
public class RequireNewTransaction {

  private final Properties transactionAttributes;
  private final PlatformTransactionManager transactionManager;

  public RequireNewTransaction(final PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
    this.transactionAttributes = new Properties() {{
      put("*", "PROPAGATION_REQUIRES_NEW");
    }};
  }

  public <T> T requireNewTransaction(T target) {
    final TransactionProxyFactoryBean proxy = new TransactionProxyFactoryBean();

    // Inject transaction manager here
    proxy.setTransactionManager(transactionManager);

    // Define which object instance is to be proxied (your bean)
    proxy.setTarget(target);

    // Programmatically setup transaction attributes
    proxy.setTransactionAttributes(transactionAttributes);

    // Is needed since Spring 4.3.1
    proxy.setProxyTargetClass(true);

    // Finish FactoryBean setup
    proxy.afterPropertiesSet();
    return (T) proxy.getObject();
  }
}
