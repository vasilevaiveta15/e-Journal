package com.example.ejournal;

import com.example.ejournal.security.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class EJournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(EJournalApplication.class, args);
    }

}
