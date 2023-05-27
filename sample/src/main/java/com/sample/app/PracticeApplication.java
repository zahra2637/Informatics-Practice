package com.sample.app;

import com.sample.app.common.config.AppConfiguration;
import com.sample.app.common.config.JacksonConfiguration;
import com.sample.app.common.config.SecurityConfiguration;
import com.sample.app.common.config.WebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		AppConfiguration.class,
		JacksonConfiguration.class,
		SecurityConfiguration.class,
		WebConfiguration.class



})
@ComponentScan(basePackages = {"com.sample.app"}, excludeFilters = {@ComponentScan.Filter(value = {Configuration.class})})
public class PracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
	}

}
