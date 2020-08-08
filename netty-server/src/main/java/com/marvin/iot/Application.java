package com.marvin.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author marvinwjs
 */
@SpringBootApplication(scanBasePackages = "com.marvin.iot")
public class Application {
    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }
}
