package com.bblets.baibuy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.bblets.baibuy.services.AuditorAwareImpl;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BaibuyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaibuyApplication.class, args);
	}

	@Bean
	public AuditorAware<Integer> auditorAware() {
		return new AuditorAwareImpl();
	}

}
