package com.essay_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.essay_backend.Dao.OtherFileDao;
import com.essay_backend.Dao.ProjectDao;
import com.essay_backend.Entity.OtherFile;
import com.essay_backend.service.IOtherFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


@Service
public class OtherFileServiceImpl extends ServiceImpl<OtherFileDao, OtherFile> implements IOtherFileService {

    @Autowired
    OtherFileDao otherFileDao;
}
