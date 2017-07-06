package de.holisticon.camunda.example.customquery.process;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderProcess {

  public enum VARIABLE {
    ;

    public static final String ORDER_NAME = "orderName";
    public static final String ORDER_POSITIONS = "orderPositions";
  }

  public static final String PROCESS_KEY = "order_example_process";


  private final RuntimeService runtimeService;

  public OrderProcess(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  public ProcessInstance start(String businessKey, String orderName, List<String> bookIds) {
    return runtimeService.startProcessInstanceByKey("order_example_process",
      businessKey,
      Variables.putValue(VARIABLE.ORDER_NAME, orderName)
        .putValue(VARIABLE.ORDER_POSITIONS, String.join(",", bookIds)));
  }
}
