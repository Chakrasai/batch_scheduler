package com.chakram.batch_schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class BatchScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchScheduleApplication.class, args);
	}

}
