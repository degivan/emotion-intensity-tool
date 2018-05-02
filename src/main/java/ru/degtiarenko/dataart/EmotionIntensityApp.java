package ru.degtiarenko.dataart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
public class EmotionIntensityApp {
    public static void main(String[] args)  {
        SpringApplication.run(EmotionIntensityApp.class, args);
    }
}

