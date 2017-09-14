package holunda.taskassignment.plugin.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

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

  public TermEntity(String term) {
    this(UUID.randomUUID().toString(), term);
  }
}
