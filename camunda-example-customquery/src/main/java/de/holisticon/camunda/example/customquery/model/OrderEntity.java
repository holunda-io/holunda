package de.holisticon.camunda.example.customquery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity implements Serializable {

  @Id
  private String id;

  private String name;

  @OneToMany(cascade = ALL, fetch = EAGER)
  @Singular
  private List<OrderPositionEntity> positions;

}
