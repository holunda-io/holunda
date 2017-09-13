package holunda.taskassignment.business.jpa;

import holunda.taskassignment.business.jpa.PackageRepository;
import holunda.taskassignment.business.jpa.entity.BoxEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SphereRepository extends PackageRepository<BoxEntity>, CrudRepository<BoxEntity, String> {
}
