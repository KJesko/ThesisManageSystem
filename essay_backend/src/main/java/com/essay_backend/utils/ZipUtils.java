package com.essay_backend.utils;

import com.essay_backend.service.IFileMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class ZipUtils {


    IFileMapService fileMapService;
    public ZipUtils(IFileMapService fileMapService){
        this.fileMapService = fileMapService;
    }
    /**
     * sourceFile一定要是文件夹
     * 默认会在同目录下生成zip文件
     *
     * @param sourceFilePath
     * @throws Exception
     */
    public void fileToZip(String sourceFilePath) throws Exception {
        fileToZip(new File(sourceFilePath));
    }

    /**
     * sourceFile一定要是文件夹
     * 默认会在同目录下生成zip文件
     *
     * @param sourceFile
     * @throws Exception
     */
    public void fileToZip(File sourceFile) throws Exception {

        if (!sourceFile.exists()) {
            throw new RuntimeException("不存在");
        }
        if (!sourceFile.isDirectory()) {
            throw new RuntimeException("不是文件夹");
        }
        //zip文件生成位置
        File zipFile = new File(sourceFile.getAbsolutePath() + ".zip");
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
        fileToZip(zos, sourceFile, "");
        zos.close();
        fos.close();
    }

    private void fileToZip(ZipOutputStream zos, File sourceFile, String path) throws Exception {

        System.out.println(sourceFile.getAbsolutePath());

        //如果是文件夹只创建zip实体即可，如果是文件，创建zip实体后还要读取文件内容并写入
        if (sourceFile.isDirectory()) {
            path = path + sourceFile.getName() + "/";
            ZipEntry zipEntry = new ZipEntry(path);
            zos.putNextEntry(zipEntry);
            for (File file : Objects.requireNonNull(sourceFile.listFiles())) {
                fileToZip(zos, file, path);
            }
        } else {
            //创建ZIP实体，并添加进压缩包
            System.out.println(sourceFile.getName());
            String fileName = fileMapService.getById(sourceFile.getName()).getFileName();
            ZipEntry zipEntry = new ZipEntry(path + fileName);
            zos.putNextEntry(zipEntry);
            byte[] bufs = new byte[1024 * 10];
            //读取待压缩的文件并写进压缩包里
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fis, 1024 * 10);
            int read = 0;
            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                zos.write(bufs, 0, read);
            }
            bis.close();
            fis.close();
        }
    }

}
