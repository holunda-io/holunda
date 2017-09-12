package holunda.taskassignment.plugin;

import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.reactor.plugin.ReactorProcessEnginePlugin;
import org.camunda.bpm.spring.boot.starter.test.helper.StandaloneInMemoryTestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PluginConfiguration.class)
public class PluginTestConfiguration {

  @Bean
  ProcessEngineRule processEngineRule(ReactorProcessEnginePlugin reactorProcessEnginePlugin) {
    final StandaloneInMemoryTestConfiguration standaloneInMemoryTestConfiguration = new StandaloneInMemoryTestConfiguration();
    standaloneInMemoryTestConfiguration.getProcessEnginePlugins().add(reactorProcessEnginePlugin);
    return standaloneInMemoryTestConfiguration.rule();
  }

}
