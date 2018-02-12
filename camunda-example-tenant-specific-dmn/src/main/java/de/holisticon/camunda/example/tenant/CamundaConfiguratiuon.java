package de.holisticon.camunda.example.tenant;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jo Ehm (Holisticon)
 */
@Configuration
public class CamundaConfiguratiuon {

    @Bean
    public ProcessEnginePlugin processEnginePlugin() {

        return new ProcessEnginePlugin() {

            @Override
            public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
                processEngineConfiguration.setTenantIdProvider(new AuthenticatedUserTenantIdProvider());
            }

            @Override
            public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {

            }

            @Override
            public void postProcessEngineBuild(ProcessEngine processEngine) {

            }
        };
    }
}
