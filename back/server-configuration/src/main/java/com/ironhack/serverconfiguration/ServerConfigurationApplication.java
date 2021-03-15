package com.ironhack.serverconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ServerConfigurationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerConfigurationApplication.class, args);
	}

}
