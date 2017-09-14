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

@SpringBootApplication
@EnableProcessApplication
@Slf4j
public class CamundaApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(CamundaApplication.class, args);
  }

  @Autowired
  @Qualifier("readOnly")
  private PackageRepository r;

  @Autowired
  private BusinessDataService businessDataService;

  @Autowired
  private RuntimeService runtimeService;

  @Override
  public void run(String... strings) throws Exception {
    log.info("all:\n {}", r.findAll());

    log.info("gold: {}", businessDataService.loadBusinessData(new BusinessKey("gold"), Collections.singleton("weight")));
    log.info("gold: {}", businessDataService.loadBusinessData(new BusinessKey("gold"), Collections.singleton("length")));
  }
}
