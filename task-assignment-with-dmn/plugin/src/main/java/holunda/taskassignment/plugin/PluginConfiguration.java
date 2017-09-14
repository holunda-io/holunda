package holunda.taskassignment.plugin;

import org.camunda.bpm.extension.reactor.spring.CamundaReactorConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Import this to use the dynamic task assignment.
 */
@Configuration
@Import(CamundaReactorConfiguration.class)
@ComponentScan(excludeFilters = @Filter(Configuration.class))
public class PluginConfiguration {

}
