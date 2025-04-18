package com.retry.replay.RetryReplayApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.retry.replay.RetryReplayApplication.repository")
@EntityScan(basePackages = "com.retry.replay.RetryReplayApplication.model")
public class RetryReplayApplication {
	public static void main(String[] args) {
		SpringApplication.run(RetryReplayApplication.class, args);
	}
}
