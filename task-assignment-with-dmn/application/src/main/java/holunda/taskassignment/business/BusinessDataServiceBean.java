package holunda.taskassignment.business;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.BusinessKey;
import holunda.taskassignment.business.jpa.PackageRepository;
import holunda.taskassignment.business.jpa.entity.PackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class BusinessDataServiceBean implements BusinessDataService {

  @Autowired
  @Qualifier("readOnly")
  private PackageRepository packageRepository;

  @Override
  public BusinessData loadBusinessData(BusinessKey businessKey, Set<String> keys) {
    final PackageEntity packageEntity = packageRepository.findOne(businessKey.getValue());
    final Map<String, Integer> values = new HashMap<>();

    if (packageEntity != null) {
      Map<String, Integer> p = packageEntity.toMap();
      for (final String key : keys) {
        values.put(key, p.get(key));
      }
    }

    return new BusinessData(values);
  }
}
