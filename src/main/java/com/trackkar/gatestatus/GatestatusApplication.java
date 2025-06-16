package com.trackkar.gatestatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatestatusApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatestatusApplication.class, args);
		System.out.println("🚦 Railway Gate Status Application Started!");
	}

}
