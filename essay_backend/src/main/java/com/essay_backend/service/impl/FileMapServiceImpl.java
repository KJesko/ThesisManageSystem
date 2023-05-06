package com.essay_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.essay_backend.Dao.FileMapDao;
import com.essay_backend.Entity.FileMap;
import com.essay_backend.service.IFileMapService;
import com.essay_backend.service.IOtherFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileMapServiceImpl extends ServiceImpl<FileMapDao, FileMap> implements IFileMapService {
    @Autowired
    FileMapDao fileMapDao;
}
