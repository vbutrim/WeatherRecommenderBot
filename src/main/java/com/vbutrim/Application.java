package com.vbutrim;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySource("classpath:default.properties")
@EnableScheduling
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {

        logger.log(Level.INFO, "Env: " + System.getenv("env"));

        SpringApplication.run(Application.class, args);
    }
}
