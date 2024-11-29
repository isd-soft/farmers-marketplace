package com.example.isdfarmersmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IsdFarmersMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsdFarmersMarketApplication.class, args);
	}

}
