package de.holisticon.camunda.example.customquery.process.task;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskWithOrderRepository extends org.springframework.data.repository.Repository<TaskWithOrder, String> {

  TaskWithOrder findOne(String id);

  List<TaskWithOrder> findAll();
}
