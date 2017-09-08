package de.holisticon.holunda.springboot;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class MinimalExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(MinimalExampleApplication.class, args);
  }
}
