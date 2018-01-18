package de.holisticon.serverlessbpm.aws;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "serverlessbpm")
@Setter
public class ServerlessBpmConfiguration {

  Aws aws = new Aws();

  public static class Aws {

    Topic topic = new Topic();

    public static class Topic {
      String publishArn;
      String subscribeArn;
    }
  }

}
