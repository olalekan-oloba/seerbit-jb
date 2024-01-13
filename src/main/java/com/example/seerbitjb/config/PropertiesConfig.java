package com.example.seerbitjb.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Getter
@Setter
public class PropertiesConfig {

    private final App app = new App();
    @Data
    public static class App {
        private int transactionDateAge;
    }
}
