package de.holisticon.camunda.example.customquery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity implements Serializable {

  public static BookEntity of(String book) {
    String[] s = book.split(",");

    return BookEntity.builder()
      .id(s[0])
      .name(s[1])
      .price(Integer.parseInt(s[2]))
      .build();
  }

  @Id
  private String id;

  private String name;

  private Integer price;
}
