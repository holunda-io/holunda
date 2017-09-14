package holunda.taskassignment.api;

import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.BusinessKey;

import java.util.Set;

/**
 * Service that allows extraction of business values by key, so the caller does not have
 * to know the concrete model implementation to evaluate dmn.
 */
public interface BusinessDataService {
  BusinessData loadBusinessData(final BusinessKey businessKey, final Set<String> keys);
}
