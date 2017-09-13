package holunda.taskassignment.business.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue(PackageEntity.Type.SPHERE)
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class SphereEntity extends PackageEntity {

  private int radius;

  public Map<String,Integer> toMap() {
    Map<String,Integer> map = super.toMap();

    map.put("radius", radius);

    return map;
  }
}
