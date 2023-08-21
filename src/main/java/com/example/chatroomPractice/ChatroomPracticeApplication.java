package com.example.chatroomPractice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ChatroomPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatroomPracticeApplication.class, args);
	}
}
