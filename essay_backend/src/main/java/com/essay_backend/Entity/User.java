package com.essay_backend.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.intellij.lang.annotations.Identifier;

@Data
@TableName("system_user")
public class User {
    @TableId
    private Integer uid;
    private String name;
    private String pwd;
    private Integer type;
    private String email;
}
