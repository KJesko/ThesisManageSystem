package com.essay_backend.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("file_map")
public class FileMap {
    @TableId
    String fileId;
    String fileName;
}
