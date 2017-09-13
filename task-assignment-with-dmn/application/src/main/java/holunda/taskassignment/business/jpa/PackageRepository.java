package holunda.taskassignment.business.jpa;

import holunda.taskassignment.business.jpa.entity.PackageEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("readOnly")
public interface PackageRepository<T extends PackageEntity> extends org.springframework.data.repository.Repository<T, String> {

  T findOne(String id);

  Iterable<T> findAll();

  Iterable<T> findAll(Sort sort);

  Page<T> findAll(Pageable pageable);
}

