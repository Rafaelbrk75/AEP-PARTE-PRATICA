package br.edu.unicesumar.aep_parte2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AepParte2Application {

    public static void main(String[] args) {
        SpringApplication.run(AepParte2Application.class, args);
    }

}
