package holunda.taskassignment.plugin.term;

import holunda.taskassignment.plugin.term.entity.TermEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Saves and loads terms.
 */
@Repository
@Transactional
public interface TermRepository extends CrudRepository<TermEntity, String> {

  List<TermEntity> findAll();
}
