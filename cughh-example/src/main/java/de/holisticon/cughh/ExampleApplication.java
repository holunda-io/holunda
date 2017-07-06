package de.holisticon.cughh;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableProcessApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    private final Logger logger = LoggerFactory.getLogger(ExampleApplication.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Bean
    public CommandLineRunner run() {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                BpmnModelInstance modelInstance = Bpmn.createExecutableProcess("inlineProcess")
                        .startEvent()
                        .userTask("theTask").name("The Task")
                        .endEvent()
                        .done();

                logger.info("deploying: {}", repositoryService.createDeployment().addModelInstance("initialProcess", modelInstance).deploy());
            }
        };

    }
}
