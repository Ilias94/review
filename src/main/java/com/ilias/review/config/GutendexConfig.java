package com.ilias.review.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external.gutendex")
@Getter
@Setter
public class GutendexConfig {
    private String baseUrl;
}
