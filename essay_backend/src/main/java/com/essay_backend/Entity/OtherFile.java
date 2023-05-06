package com.essay_backend.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("proj_other_file")
public class OtherFile {
    @TableId
    String fileId;
    String fileName;
    Integer projId;
}

