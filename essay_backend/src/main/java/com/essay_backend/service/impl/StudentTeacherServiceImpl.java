package com.essay_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.essay_backend.Dao.StudentTeacherDao;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.service.IStudentTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class StudentTeacherServiceImpl extends ServiceImpl<StudentTeacherDao, StudentTeacher> implements IStudentTeacherService {
    @Autowired
    StudentTeacherDao studentTeacherDao;

    public List<StudentTeacher> getAllMatchInfo(){
        return studentTeacherDao.getAllMatchInfo();
    }


    public List<StudentTeacher> getAllMatchInfo(Integer number, Integer size) {
        return studentTeacherDao.allMatchInfo((number-1)*size, size);
    }

    public void updateMatch(Integer instructorId, String instructorName,Integer supervisorId, String supervisorName,Integer sid){
        studentTeacherDao.updateMatch(instructorId,instructorName,supervisorId,supervisorName,sid);
    }

//    @Override
//    public StudentTeacher getById(Integer id) {
//        return studentTeacherDao.mySelectById(id);
//    }
//
//    @Override
//    public void removeById(Integer id) {
//        studentTeacherDao.myDeleteById(id);
//    }


}
