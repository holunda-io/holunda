package de.holisticon.camunda.example.customquery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPositionEntity implements Serializable {

  @Id
  private String id;

  @OneToOne
  private BookEntity book;

}
