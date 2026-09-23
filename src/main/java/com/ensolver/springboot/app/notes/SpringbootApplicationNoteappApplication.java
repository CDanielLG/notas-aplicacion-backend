package com.ensolver.springboot.app.notes;

import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApplicationNoteappApplication {

	private static final String DEFAULT_TIME_ZONE = "UTC";

	public static void main(String[] args) {
		String configuredTimeZone = System.getenv().getOrDefault("APP_TIME_ZONE", DEFAULT_TIME_ZONE);
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(configuredTimeZone)));
		SpringApplication.run(SpringbootApplicationNoteappApplication.class, args);
	}

}
