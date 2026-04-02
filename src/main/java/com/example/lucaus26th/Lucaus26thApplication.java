package com.example.lucaus26th;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // basetimeEntity 관련
public class Lucaus26thApplication {

	public static void main(String[] args) {

		SpringApplication.run(Lucaus26thApplication.class, args);
	}

}
