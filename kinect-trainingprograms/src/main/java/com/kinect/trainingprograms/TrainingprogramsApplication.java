package com.kinect.trainingprograms;

import com.kinect.contracts.config.LocalDateJacksonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(LocalDateJacksonConfiguration.class)
public class TrainingprogramsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingprogramsApplication.class, args);
	}

}
