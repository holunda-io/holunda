package holunda.taskassignment.business;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.BusinessKey;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class BusinessDataServiceBean implements BusinessDataService {

  @Override
  public BusinessData loadBusinessData(BusinessKey businessKey, Set<String> keys) {
    final Map<String,Integer> values = new HashMap<>();

    return new BusinessData(values);
  }
}
