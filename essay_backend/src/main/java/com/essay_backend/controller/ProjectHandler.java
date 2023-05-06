package com.essay_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essay_backend.Entity.*;
import com.essay_backend.service.IFileMapService;
import com.essay_backend.service.IOtherFileService;
import com.essay_backend.service.IProjectService;
import com.essay_backend.service.IStudentTeacherService;
import com.essay_backend.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static com.essay_backend.utils.myUtils.*;


@RestController
@RequestMapping("/project")
public class ProjectHandler {
    @Autowired
    IProjectService projectService;
    @Autowired
    IStudentTeacherService studentTeacherService;

    @Autowired
    IFileMapService fileMapService;
    @Autowired
    IOtherFileService otherFileService;

    @PostMapping("/submit/essay/{projId}/j87y5ndh4sbg678h")
    public Map<String,String> uploadEssay(@RequestParam(value = "file") MultipartFile file, @PathVariable("projId") Integer projId) throws IOException {
        Map<String,String> map = new HashMap<>();
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        System.out.println(projId);
        String fileId = UUID.randomUUID().toString();
        String path = "E:\\EssayManageFile\\Student\\" + projId + "\\essay\\" + fileId;
        createDir(path);

        file.transferTo(new File(path));

        Project project = projectService.getById(projId);
        if (project == null) {
            map.put("msg","数据库出错");
        } else {
            FileMap fileMap = new FileMap();
            fileMap.setFileId(fileId);
            fileMap.setFileName(fileName);
            fileMapService.save(fileMap);
            projectService.removeById(projId);
            String oldFileId = project.getEssayFileId();
            deleteFile("E:\\EssayManageFile\\Student\\" + projId + "\\essay\\" + oldFileId);
            project.setEssayFileId(fileId);
            project.setEssayFileName(fileName);
            project.setState(1);
            projectService.save(project);
        }
        System.out.println("上传论文文件");
        map.put("msg","上传成功");
        return map;

    }

    @PostMapping("/submit/duplicate/{projId}/j87y5ndh4sbg678h")
    public Map<String,String> uploadDuplicate(@RequestParam(value = "file") MultipartFile file, @PathVariable("projId") Integer projId) throws IOException {
        Map<String,String> map = new HashMap<>();
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        System.out.println(projId);
        String fileId = UUID.randomUUID().toString();
        String path = "E:\\EssayManageFile\\Student\\" + projId + "\\duplicate\\" + fileId;

        createDir(path);


        file.transferTo(new File(path));

        Project project = projectService.getById(projId);
        if (project == null) {
            map.put("msg","数据库错误");
        } else {
            FileMap fileMap = new FileMap();
            fileMap.setFileId(fileId);
            fileMap.setFileName(fileName);
            fileMapService.save(fileMap);
            projectService.removeById(projId);
            String oldFileId = project.getDuplicateFileId();
            deleteFile("E:\\EssayManageFile\\Student\\" + projId + "\\duplicate\\" + oldFileId);
            project.setDuplicateFileId(fileId);
            project.setDuplicateFileName(fileName);
            projectService.save(project);
        }
        System.out.println("上传查重文件");
        map.put("msg","上传成功");
        return map;
    }

    @PostMapping("/submit/other/{projId}/j87y5ndh4sbg678h")
    public void uploadOther(@RequestParam("file") MultipartFile file, @PathVariable("projId") Integer projId) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileId = UUID.randomUUID().toString();
        String path = "E:\\EssayManageFile\\Student\\" + projId + "\\other\\"+fileId;

        createDir(path);

//        Files.write(filePath, file.getBytes());
        FileMap fileMap = new FileMap();
        fileMap.setFileId(fileId);
        fileMap.setFileName(fileName);
        fileMapService.save(fileMap);
        file.transferTo(new File(path));
        OtherFile otherFile = new OtherFile();
        otherFile.setProjId(projId);
        otherFile.setFileId(fileId);
        otherFile.setFileName(fileName);
        otherFileService.save(otherFile);
    }

    @GetMapping("/info/{projId}/j87y5ndh4sbg678h")
    public Map<String, Object> projectInfo(@PathVariable("projId") Integer projId) throws IOException {
        Project project = projectService.getById(projId);
        StudentTeacher studentTeacher = studentTeacherService.getById(projId);
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<OtherFile> wrapper = new QueryWrapper<>();
        wrapper.eq("proj_id", projId);
        List<OtherFile> otherFileList = otherFileService.list(wrapper);
        List<Map<String,String>> otherFileJson = new ArrayList<>();
        for (OtherFile file:otherFileList){
            Map<String,String> fileInfo = new HashMap<>();
            fileInfo.put("id",file.getFileId());
            fileInfo.put("name",file.getFileName());
            otherFileJson.add(fileInfo);
        }



        if (project == null) {
            map.put("title", "暂无");
            map.put("essayFileName", null);
            map.put("essayFileId", null);
            map.put("duplicateFileName", null);
            map.put("duplicateFileId", null);
            map.put("otherFileJson",null);
        } else {
            map.put("title", project.getProjName()==null? "暂无" : project.getProjName());
            map.put("essayFileName", project.getEssayFileName());
            map.put("essayFileId", project.getEssayFileId());
            map.put("duplicateFileName", project.getDuplicateFileName());
            map.put("duplicateFileId", project.getDuplicateFileId());
            map.put("otherFileJson",otherFileJson.size()==0?null:otherFileJson);
        }
        if (studentTeacher == null) {
            map.put("instructorName", "暂无");
//            map.put("supervisorName", "暂无");
            map.put("instructorId", "暂无");
//            map.put("supervisorId", "暂无");
        } else {
            map.put("instructorName", studentTeacher.getInstructorName());
            map.put("instructorId", studentTeacher.getInstructorId());
//            map.put("supervisorName", studentTeacher.getSupervisorName());
//            map.put("supervisorId", studentTeacher.getSupervisorId());
        }
        System.out.println(otherFileList.size());

        return map;
    }

    @PostMapping("editTitle/j87y5ndh4sbg678h")
    public Map<String, String> editTitle(@RequestParam("uid") Integer uid, @RequestParam("title") String title) {
        Map<String, String> map = new HashMap<>();
        try {
            Project project = projectService.getById(uid);
            if (project == null) {
                map.put("msg", "数据库错误");
                return map;
            } else {
                projectService.removeById(uid);
                project.setProjName(title);
                projectService.save(project);

            }
            map.put("msg", "success");
            return map;
        } catch (Exception e) {
            map.put("msg", "fail");
            return map;
        }

    }

    @GetMapping("removeEssay/{uid}/{fileId}/j87y5ndh4sbg678h")
    public Map<String, String> removeEssay(@PathVariable("uid") Integer uid, @PathVariable("fileId") String fileId) {
        Project project = projectService.getById(uid);
        Map<String, String> map = new HashMap<>();
        try {
            if (project == null) {
                map.put("msg", "项目不存在");
            } else {
                if (Objects.equals(project.getEssayFileId(), fileId)) {
                    projectService.removeById(uid);
                    String filePath = "E:\\EssayManageFile\\Student\\" + uid + "\\essay\\" + fileId;
                    boolean flag = deleteFile(filePath);
                    project.setEssayFileId(null);
                    project.setEssayFileName(null);
                    project.setState(0);
                    projectService.save(project);
                    fileMapService.removeById(fileId);
                    if (flag) {
                        map.put("msg", "success");
                    } else {
                        map.put("msg", "文件不可删除或已丢失");
                    }

                }
            }
            return map;
        } catch (Exception e) {
            map.put("msg", "服务器出错");
            return map;
        }

    }

    @GetMapping("removeDuplicate/{uid}/{fileId}/j87y5ndh4sbg678h")
    public Map<String, String> removeDuplicate(@PathVariable("uid") Integer uid, @PathVariable("fileId") String fileId) {
        Project project = projectService.getById(uid);
        Map<String, String> map = new HashMap<>();
        try {
            if (project == null) {
                map.put("msg", "项目不存在");
            } else {
                if (Objects.equals(project.getDuplicateFileId(), fileId)) {
                    projectService.removeById(uid);
                    String filePath = "E:\\EssayManageFile\\Student\\" + uid + "\\duplicate\\" + fileId;
                    boolean flag = deleteFile(filePath);
                    project.setDuplicateFileId(null);
                    project.setDuplicateFileName(null);
                    projectService.save(project);
                    fileMapService.removeById(fileId);
                    if (flag) {
                        map.put("msg", "success");
                    } else {
                        map.put("msg", "文件不可删除或已丢失");
                    }

                }
            }
            return map;
        } catch (Exception e) {
            map.put("msg", "服务器出错");
            return map;
        }

    }

    @GetMapping("removeOther/{uid}/{fileId}/j87y5ndh4sbg678h")
    public Map<String, String> removeOther(@PathVariable("uid") Integer uid,@PathVariable("fileId") String fileId) {

        Map<String, String> map = new HashMap<>();
        OtherFile otherFile = otherFileService.getById(fileId);
        if (otherFile == null){
            map.put("msg", "文件不存在");
        }else {
            otherFileService.removeById(fileId);
            String filePath = "E:\\EssayManageFile\\Student\\" + uid + "\\other\\" + fileId;
            map.put("msg", "success");
            boolean flag = deleteFile(filePath);
            fileMapService.removeById(fileId);

        }
        return map;

    }

    @GetMapping("downloadEssay/{uid}/j87y5ndh4sbg678h")
    public ResponseEntity<Resource> downloadEssay(@PathVariable("uid") Integer uid) throws IOException {
        // 读取文件内容
        Project project = projectService.getById(uid);
        if (project == null || project.getEssayFileId() == null){
            return null;
        }
        String fileId = project.getEssayFileId();
        String fileName = project.getEssayFileName();

        Path filePath = Paths.get("E:\\EssayManageFile\\Student\\" + uid + "\\essay\\" + fileId);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

        // 设置 HTTP 响应头，指定文件名和下载方式
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);

        // 返回文件内容和响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("downloadDuplicate/{uid}/j87y5ndh4sbg678h")
    public ResponseEntity<Resource> downloadDuplicate(@PathVariable("uid") Integer uid) throws IOException {
        // 读取文件内容
        Project project = projectService.getById(uid);
        if (project == null || project.getEssayFileId() == null){
            return null;
        }
        String fileId = project.getDuplicateFileId();
        String fileName = project.getDuplicateFileName();

        Path filePath = Paths.get("E:\\EssayManageFile\\Student\\" + uid + "\\duplicate\\" + fileId);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

        // 设置 HTTP 响应头，指定文件名和下载方式
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);

        // 返回文件内容和响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("downloadOther/{uid}/{fileId}/j87y5ndh4sbg678h")
    public ResponseEntity<Resource> downloadOther(@PathVariable("uid") Integer uid,@PathVariable("fileId") String fileId) throws IOException {
        // 读取文件内容
        OtherFile otherFile =  otherFileService.getById(fileId);
        if (otherFile== null){
            return  ResponseEntity.notFound().build();

        }

        String fileName = otherFile.getFileName();

        Path filePath = Paths.get("E:\\EssayManageFile\\Student\\" + uid + "\\other\\" + fileId);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

        // 设置 HTTP 响应头，指定文件名和下载方式
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);

        // 返回文件内容和响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/essayZip/{uid}/j87y5ndh4sbg678h")
    public ResponseEntity<ByteArrayResource> essayZip(@PathVariable("uid") Integer uid) throws Exception {

        String sourceDirectory = "E:\\EssayManageFile\\Student\\" + uid;
        System.out.println(sourceDirectory);
        File sourceDir = new File(sourceDirectory);
        ZipUtils zipUtils = new ZipUtils(fileMapService);
        zipUtils.fileToZip(sourceDir);
        Path zipPath = Paths.get(sourceDirectory+".zip");
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(zipPath));
        deleteFile(zipPath.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.zip");
//
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
