package com.github.mqtask;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MqtaskApplication {

	public static void main(String[] args) {
//		SpringApplication.run(MqtaskApplication.class, args);
        SpringApplication springApplication = new SpringApplication(MqtaskApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
	}
}
