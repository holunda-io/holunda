package holunda.taskassignment.plugin;

import holunda.taskassignment.api.BusinessDataService;
import holunda.taskassignment.api.model.BusinessData;
import holunda.taskassignment.api.model.BusinessKey;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Set;

@SpringBootApplication
@EnableProcessApplication
@Import(PluginConfiguration.class)
public class TestApplication {



}
