package com.quijano.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = "com.quijano.authserver.entities")
public class AuthServerApplication implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}


	/**
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Password: " + this.encoder.encode("secret"));

	}
}
