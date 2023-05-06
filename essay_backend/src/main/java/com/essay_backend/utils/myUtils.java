package com.essay_backend.utils;

import com.essay_backend.service.IFileMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class myUtils {

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) { // 判断文件或文件夹是否存在
            if (file.isFile()) { // 如果是文件，则直接删除
                return file.delete();
            } else { // 如果是文件夹，则递归删除其所有子文件和子文件夹
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        deleteFile(f.getAbsolutePath());
                    }
                }
                return file.delete(); // 删除空文件夹
            }
        } else {
            return false;
        }
    }
    public static void createDir(String pathString){
        File folder = new File(pathString);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("文件夹路径创建成功");
            } else {
                System.out.println("文件夹路径创建失败");
            }
        } else {
            System.out.println("文件夹路径已存在");
        }
    }
}
