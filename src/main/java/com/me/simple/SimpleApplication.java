package com.me.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SimpleApplication {

	@RequestMapping("/list")
	public String home() {
		return "Hello World 123 !!!";
	}

	public static void main(String[] args) {
		SpringApplication.run(SimpleApplication.class, args);
	}

}
