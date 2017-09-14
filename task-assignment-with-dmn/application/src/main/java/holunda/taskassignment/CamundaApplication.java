package holunda.taskassignment;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application staring camunda + webapp.
 */
@SpringBootApplication
@EnableProcessApplication
@Slf4j
public class CamundaApplication {

  public static void main(String[] args) {
    SpringApplication.run(CamundaApplication.class, args);
  }

}
