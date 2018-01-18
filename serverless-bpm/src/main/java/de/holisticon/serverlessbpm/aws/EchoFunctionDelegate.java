package de.holisticon.serverlessbpm.aws;

import com.amazonaws.services.sns.AmazonSNS;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EchoFunctionDelegate extends AbstractBpmnActivityBehavior {

    public static final String MESSAGE = "Hello World";

    @Autowired
    private ServerlessBpmConfiguration config;

    private final NotificationMessagingTemplate notificationMessagingTemplate;

    @Autowired
    public EchoFunctionDelegate(AmazonSNS amazonSns) {
        notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSns);
    }

    public void execute(final ActivityExecution execution) {
        log.info("Sending {}/{} to {}", execution.getId(), MESSAGE, config.aws.topic.publishArn);
        notificationMessagingTemplate.sendNotification(config.aws.topic.publishArn, MESSAGE, execution.getId());
    }

    @Override
    public void signal(ActivityExecution execution, String signalName, Object signalData) {
        log.info("Received signal {} with data {} for execution ID {}", signalName, signalData, execution.getId());
        execution.setVariable("echoResponse", signalData);
        // leave the service task activity:
        leave(execution);
    }

}
