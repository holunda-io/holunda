package de.holisticon.camunda.example.customquery.model;


import java.util.List;

public enum TestData {
  ;

  public static List<BookEntity> create(BookRepository bookRepository) {
      bookRepository.save(BookEntity.of("1,Enterprise Java 3.1,40"));
      bookRepository.save(BookEntity.of("2,Effective Java,30"));
      bookRepository.save(BookEntity.of("3,BPMN in Practice,20"));
      bookRepository.save(BookEntity.of("4,Scala for Beginners,20"));

      return bookRepository.findAll();
  }
}
