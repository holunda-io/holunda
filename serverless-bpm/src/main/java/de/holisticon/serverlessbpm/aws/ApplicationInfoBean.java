package de.holisticon.serverlessbpm.aws;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ToString
@Slf4j
@Getter
public class ApplicationInfoBean {

    @Value("${public-hostname:N/A}")
    private String publicHostname;

    @PostConstruct
    public void dump() {
      log.info("Application Info {}", this.toString());
    }
}
