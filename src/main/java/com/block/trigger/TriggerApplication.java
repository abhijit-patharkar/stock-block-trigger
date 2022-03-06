package com.block.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.block.trigger")
@EntityScan("com.block.trigger.model")
@EnableScheduling
public class TriggerApplication {

	// Logger
	private final static Logger logger = LoggerFactory.getLogger(TriggerApplication.class);
	
	public static void main(String[] args) {
		logger.info("starting application..");
		SpringApplication.run(TriggerApplication.class, args);
		logger.info("started application..");
	}
}
