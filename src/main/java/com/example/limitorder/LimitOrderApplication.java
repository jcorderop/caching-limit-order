package com.example.limitorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@SpringBootApplication
public class LimitOrderApplication {

    // https://www.graalvm.org/22.1/guides/
    public static void main(String[] args) {
        SpringApplication.run(LimitOrderApplication.class, args);
    }

}
