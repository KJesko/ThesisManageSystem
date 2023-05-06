package com.essay_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IUserService extends IService<User> {
    public Integer getAllUserCount();
    public List<User> getAllUserPageInfo(String pageNumber,String PageSize);
}
