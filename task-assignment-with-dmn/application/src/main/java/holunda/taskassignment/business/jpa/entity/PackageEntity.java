package holunda.taskassignment.business.jpa.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.HashMap;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PackageEntity {

  enum Type {
    ;

    public static final String BOX = "box";
    public static final String SPHERE = "sphere";
  }

  @Id
  private String id;

  @Column(insertable = false, updatable = false)
  private String type;

  private int weight;

  public Map<String,Integer> toMap() {
    Map<String,Integer> map = new HashMap<>();

    map.put("weight", weight);

    return map;
  }
}
