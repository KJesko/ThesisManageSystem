package com.essay_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.essay_backend.Dao.UserDao;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.Entity.User;
import com.essay_backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    UserDao userDao;

    public List<User> getAllUserPageInfo(String pageNumber,String PageSize){
        int offset = (Integer.parseInt(pageNumber)-1)*Integer.parseInt(PageSize);
        return userDao.getAllUserInfo(offset,Integer.parseInt(PageSize));
    }
    public Integer getAllUserCount(){
        return userDao.getAllUserCount();
    }



}
