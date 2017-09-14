package holunda.taskassignment.plugin.jpa.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TermRepository extends CrudRepository<TermEntity, Integer> {

  List<TermEntity> findAll();
}
