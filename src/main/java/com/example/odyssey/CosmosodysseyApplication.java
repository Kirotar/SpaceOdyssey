package com.example.odyssey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CosmosodysseyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmosodysseyApplication.class, args);
	}

}
