package de.holisticon.camunda.example.tenant;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Jo Ehm (Holisticon)
 */
@SpringBootApplication
@EnableProcessApplication
public class DigitsProcessApplication {

  public static void main(final String... args) throws Exception {
    SpringApplication.run(DigitsProcessApplication.class, args);
  }

}
