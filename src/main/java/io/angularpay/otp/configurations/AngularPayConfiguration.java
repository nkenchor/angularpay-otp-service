package io.angularpay.otp.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("angularpay")
@Data
public class AngularPayConfiguration {

    private int defaultOtpExpiryInSeconds;
    private Redis redis;

    @Data
    public static class Redis {
        private String host;
        private int port;
        private int timeout;
    }
}
