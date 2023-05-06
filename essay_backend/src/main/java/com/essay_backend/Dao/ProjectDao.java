package com.essay_backend.Dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essay_backend.Entity.Project;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectDao extends BaseMapper<Project> {

}
