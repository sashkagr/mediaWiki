package org.example.mediawiki;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for starting the Java MediaWiki application.
 */
@SpringBootApplication
public class MediaWikiApplication {
    /**
     * The main method to start the Java MediaWiki application.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(final String[] args) {
        SpringApplication.run(MediaWikiApplication.class, args);
    }
}