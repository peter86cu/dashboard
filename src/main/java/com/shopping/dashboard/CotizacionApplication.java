package com.shopping.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CotizacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CotizacionApplication.class, args);
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();

	}
}
