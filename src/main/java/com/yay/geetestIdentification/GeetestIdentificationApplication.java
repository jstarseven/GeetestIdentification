package com.yay.geetestIdentification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sun.applet.Main;

@SpringBootApplication
public class GeetestIdentificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeetestIdentificationApplication.class, args);
//		new SpringApplicationBuilder(GeetestIdentificationApplication.class).web(false).run(args);
	}
}
