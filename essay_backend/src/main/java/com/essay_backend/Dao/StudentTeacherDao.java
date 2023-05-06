package com.essay_backend.Dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.essay_backend.Entity.StudentTeacher;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentTeacherDao extends BaseMapper<StudentTeacher> {
    @Select(value = "select * from student_teacher")
    List<StudentTeacher> getAllMatchInfo();

    @Update(value = "update student_teacher set instructor_id=#{instructorId},instructor_name=#{instructorName}," +
            "supervisor_id=#{supervisorId},supervisor_name=#{supervisorName} where sid =#{sid}")
    void updateMatch(Integer instructorId, String instructorName,Integer supervisorId, String supervisorName,Integer sid);

    @Select(value = "select * from student_teacher where sid =#{arg0}")
    StudentTeacher mySelectById(Integer id);

    @Delete(value = "delete from student_teacher where sid =#{arg0}")
    void myDeleteById(Integer id);

    @Select(value = "select * from student_teacher limit #{start},#{offset}")
    List<StudentTeacher> allMatchInfo(Integer start, Integer offset);



}
