package com.example.demo;

import com.example.demo.service.IndexerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	IndexerService indexerService;

	@Bean
	CommandLineRunner runner() {
		return (args) -> {
			indexerService.bulkIndex();
		};
	}
}
