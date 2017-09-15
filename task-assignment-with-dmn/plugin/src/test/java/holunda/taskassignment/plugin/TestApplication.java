package holunda.taskassignment.plugin;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableProcessApplication
@Import(PluginConfiguration.class)
public class TestApplication {

}
