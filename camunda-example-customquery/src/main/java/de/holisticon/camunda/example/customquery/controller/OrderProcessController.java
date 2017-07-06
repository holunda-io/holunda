package de.holisticon.camunda.example.customquery.controller;

import de.holisticon.camunda.example.customquery.model.OrderEntity;
import de.holisticon.camunda.example.customquery.model.OrderRepository;
import de.holisticon.camunda.example.customquery.process.OrderProcess;
import de.holisticon.camunda.example.customquery.process.task.TaskWithOrder;
import de.holisticon.camunda.example.customquery.process.task.TaskWithOrderRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderProcessController {

  private final OrderProcess orderProcess;
  private final OrderRepository orderRepository;
  private final TaskWithOrderRepository taskRepository;

  public OrderProcessController(OrderProcess orderProcess, OrderRepository orderRepository, TaskWithOrderRepository taskRepository) {
    this.orderProcess = orderProcess;
    this.orderRepository = orderRepository;
    this.taskRepository = taskRepository;
  }

  @PostMapping(path = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
  public String startProcess(@RequestParam String businessKey, @RequestParam String orderName, @RequestParam List<String> items) {
    return orderProcess.start(businessKey, orderName, items).getId();
  }

  @GetMapping(path = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public OrderEntity findOne(@PathVariable String id) {
    return orderRepository.findOne(id);
  }

  @GetMapping(path = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<TaskWithOrder> findAllTasks() {
    return taskRepository.findAll();
  }
}
