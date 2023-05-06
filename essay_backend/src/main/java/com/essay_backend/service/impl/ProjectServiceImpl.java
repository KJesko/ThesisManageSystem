package com.essay_backend.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.essay_backend.Dao.ProjectDao;
import com.essay_backend.Entity.Project;
import com.essay_backend.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, Project> implements IProjectService {
    @Autowired
    ProjectDao projectDao;
}
