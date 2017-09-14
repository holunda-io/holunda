package holunda.taskassignment.plugin.term.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Represents an evaluation expression.
 *
 * term is a String in form
 *
 * <code>box: weight>100 & volume<=1000 := bigBoxGroup</code>
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TermEntity {

  @Id
  private String id;

  private String term;

  public TermEntity(final String term) {
    this(UUID.randomUUID().toString(), term);
  }
}
