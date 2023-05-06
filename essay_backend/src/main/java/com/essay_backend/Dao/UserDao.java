package com.essay_backend.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<User> {

    @Select(value = "select * from system_user limit #{offset},#{pageSize}")
    //limit  (curPage-1)*pageSize,pageSize
    List<User> getAllUserInfo(Integer offset,Integer pageSize);

    @Select(value = "select count(*) from system_user")
    Integer getAllUserCount();

}
