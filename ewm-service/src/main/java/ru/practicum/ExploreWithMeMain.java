package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(value = {StatsClient.class})
public class ExploreWithMeMain {
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeMain.class, args);
    }
}