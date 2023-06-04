package ru.practicum.shareit;

import org.apache.catalina.LifecycleException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ShareItApp {

    public static void main(String[] args) throws LifecycleException {
        SpringApplication.run(ShareItApp.class, args);
    }

}
