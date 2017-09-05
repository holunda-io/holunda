package holunda.taskassignment.plugin;

import org.camunda.bpm.extension.reactor.spring.CamundaReactorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CamundaReactorConfiguration.class)
public class PluginConfiguration {
}
