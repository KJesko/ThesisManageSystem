package com.essay_backend.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student_teacher")
public class StudentTeacher {
    @TableId
    private Integer sid;
    private String sName;
    private String major;
    private Integer instructorId;
    private String instructorName;
    private Integer supervisorId;
    private String supervisorName;


    public String toString2() {
        return ""+sid+" " +sName+" "+major+" "+instructorId+" "+ instructorName+" "+supervisorId+" "+supervisorName;
    }

}
