package com.cagan.walletapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WalletapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletapiApplication.class, args);
	}

}
