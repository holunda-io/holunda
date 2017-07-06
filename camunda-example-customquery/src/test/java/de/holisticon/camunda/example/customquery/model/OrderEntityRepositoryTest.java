package de.holisticon.camunda.example.customquery.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(
  classes = {OrderEntityRepositoryTest.Config.class},
  webEnvironment = SpringBootTest.WebEnvironment.NONE,
  properties = {"camunda.bpm.enabled=false"}
)
public class OrderEntityRepositoryTest {

  @SpringBootApplication
  public static class Config{
  }

  @Autowired
  private TestEntityManager em;

  @Autowired
  private OrderRepository repository;

  @Autowired
  private BookRepository bookRepository;

  @Before
  public void setUp() throws Exception {
    TestData.create(bookRepository);
  }

  @Test
  public void create_orderEntity() throws Exception {
    repository.save(OrderEntity.builder()
      .id("o1")
      .name("foo")
      .position(OrderPositionEntity.builder()
        .id("o1-1")
        .book(bookRepository.findOne("1"))
        .build())
      .position(OrderPositionEntity.builder()
        .id("o1-2")
        .book(bookRepository.findOne("2"))
        .build())
      .position(OrderPositionEntity.builder()
        .id("o1-3")
        .book(bookRepository.findOne("4"))
        .build())
      .build());

    OrderEntity order = repository.findOne("o1");

    assertThat(order.getPositions().get(0).getBook().getPrice()).isEqualTo(40);
  }
}
