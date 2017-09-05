package de.holisticon.camunda.example.customquery;

import de.holisticon.camunda.example.customquery.model.BookEntity;
import de.holisticon.camunda.example.customquery.model.BookRepository;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CamundaCustomQueryApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(CamundaCustomQueryApplication.class, args);
  }

  @Autowired
  private BookRepository bookRepository;

  @Override
  public void run(String... strings) throws Exception {
    bookRepository.save(BookEntity.of("1,Enterprise Java 3.1,40"));
    bookRepository.save(BookEntity.of("2,Effective Java,30"));
    bookRepository.save(BookEntity.of("3,BPMN in Practice,20"));
    bookRepository.save(BookEntity.of("4,Scala for Beginners,20"));
  }
}
