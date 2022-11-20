package com.adda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Back-end part of the ADDA project. Open Source
 *
 * @author Artem Komarov
 */

@SpringBootApplication
@EnableSwagger2
public class AddaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AddaApplication.class, args);
	}
}
