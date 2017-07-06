package de.holisticon.camunda.example.customquery.process;

import de.holisticon.camunda.example.customquery.model.BookRepository;
import de.holisticon.camunda.example.customquery.model.OrderEntity;
import de.holisticon.camunda.example.customquery.model.OrderPositionEntity;
import de.holisticon.camunda.example.customquery.model.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CreateOrderDelegate implements JavaDelegate {

  private final OrderRepository orderRepository;
  private final BookRepository bookRepository;

  public CreateOrderDelegate(OrderRepository orderRepository, BookRepository bookRepository) {
    this.orderRepository = orderRepository;
    this.bookRepository = bookRepository;
  }

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    String csv = (String) execution.getVariable(OrderProcess.VARIABLE.ORDER_POSITIONS);

    OrderEntity order = createOrder(execution.getBusinessKey(),
      (String) execution.getVariable(OrderProcess.VARIABLE.ORDER_NAME),
      Arrays.asList(csv.split(",")));

    log.info("created order={}", orderRepository.findOne(order.getId()));
  }

  public OrderEntity createOrder(String id, String name, List<String> bookIds) {
    final AtomicInteger i = new AtomicInteger(1);

    return orderRepository.save(OrderEntity.builder()
      .id(id)
      .name(name)
      .positions(bookRepository.findAll(bookIds)
        .stream()
        .map(book -> OrderPositionEntity.builder()
          .book(book)
          .id(id + "-" + i.getAndIncrement())
          .build())
        .collect(Collectors.toList()))
      .build());
  }
}
