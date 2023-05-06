package com.essay_backend;

import com.essay_backend.Dao.StudentTeacherDao;
import com.essay_backend.Dao.UserDao;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EssayBackendApplicationTests {
	@Autowired
	private UserDao userDao;
	@Autowired
	private StudentTeacherDao studentTeacherDao;

	@Test
	void insertUser(){
		User user = new User();
		user.setUid(10000000);
		user.setName("a");
		user.setPwd("123");
		userDao.insert(user);
	}

	@Test
	void allMatch(){
		System.out.println(studentTeacherDao.getAllMatchInfo());
	}

	@Test
	void selectByIdTest(){
		System.out.println(studentTeacherDao.selectById(4));
	}


}
