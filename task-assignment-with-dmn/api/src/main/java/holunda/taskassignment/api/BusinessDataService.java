package holunda.taskassignment.api;

import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.BusinessKey;

import java.util.Set;

public interface BusinessDataService {
  BusinessData loadBusinessData(BusinessKey businessKey, Set<String> keys);
}
