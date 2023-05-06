package com.essay_backend.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("student_project")
public class Project {
    @TableId
    Integer projId;
    String projName;
    String essayFileId;
    String essayFileName;
    String duplicateFileId;
    String duplicateFileName;
    String instructorFileId;
    String instructorFileName;
    String supervisorFileId;
    String supervisorFileName;
    Integer state;
    Integer stage;
}
