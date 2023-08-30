package com.springboot.spingboottexting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpingbootTextingApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpingbootTextingApplication.class, args);
	}
}
