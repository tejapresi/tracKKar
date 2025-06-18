package com.trackkar.gatestatus;

import com.trackkar.gatestatus.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class GatestatusApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatestatusApplication.class, args);
		System.out.println("🚦 Railway Gate Status Application Started!");
	}

}
