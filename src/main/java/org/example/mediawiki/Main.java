package org.example.mediawiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class Main {
    private static final Logger LOGGER = Logger.
            getLogger(Main.class.getName());
    public static void main(final String[] args) {
        LOGGER.info("Hello world!");
        SpringApplication.run(Main.class, args);
    }
}