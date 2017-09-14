package holunda.taskassignment;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessKey;
import holunda.taskassignment.business.jpa.PackageRepository;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

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
