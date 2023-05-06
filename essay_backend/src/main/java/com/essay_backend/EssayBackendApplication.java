package com.essay_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.essay_backend.utils.myUtils.createDir;

@SpringBootApplication
public class EssayBackendApplication {

	public static void main(String[] args) {
		createDir("E:\\EssayManageFile\\Init\\");
		createDir("E:\\EssayManageFile\\Student\\");
		createDir("E:\\EssayManageFile\\review\\");
		createDir("E:\\EssayManageFile\\template\\");
//		createDir("E:\\EssayManageFile\\Init\\");
		SpringApplication.run(EssayBackendApplication.class, args);
	}

}
