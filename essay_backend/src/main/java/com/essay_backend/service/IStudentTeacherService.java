package com.essay_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.essay_backend.Entity.StudentTeacher;

import java.util.List;
import java.util.Map;

public interface IStudentTeacherService extends IService<StudentTeacher> {

    List<StudentTeacher> getAllMatchInfo();

    List<StudentTeacher> getAllMatchInfo(Integer number,Integer size);
    void updateMatch(Integer instructorId, String instructorName,Integer supervisorId, String supervisorName,Integer sid);


//    StudentTeacher getById(Integer id);

//    void removeById(Integer id);

}
