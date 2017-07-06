package de.holisticon.camunda.example.customquery.process;

import de.holisticon.camunda.example.customquery.model.CamundaDisabledTestApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = {CreateOrderDelegateTest.Config.class},
  webEnvironment = SpringBootTest.WebEnvironment.NONE,
  properties = {"camunda.bpm.enabled=false"}
)
public class CreateOrderDelegateTest {

  @SpringBootApplication
  public static class Config {}



}
