package holunda.taskassignment.business.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@Entity
@DiscriminatorValue(PackageEntity.Type.BOX)
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class BoxEntity extends PackageEntity {

  private int height;
  private int length;
  private int width;

  public Map<String,Integer> toMap() {
    Map<String,Integer> map = super.toMap();

    map.put("height", height);
    map.put("length", length);
    map.put("width", width);

    return map;
  }

  @Override
  public int getVolume() {
    return width * height * length;
  }
}
