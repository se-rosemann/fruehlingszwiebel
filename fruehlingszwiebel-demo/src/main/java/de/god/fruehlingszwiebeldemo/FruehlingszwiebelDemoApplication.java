package de.god.fruehlingszwiebeldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
@EnableSwagger2
public class FruehlingszwiebelDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(FruehlingszwiebelDemoApplication.class, args);
    }
}