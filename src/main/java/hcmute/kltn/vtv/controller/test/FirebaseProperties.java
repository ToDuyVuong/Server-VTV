package hcmute.kltn.vtv.controller.test;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private String googleCredentials;
}
