package com.essay_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essay_backend.Entity.Project;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.Entity.User;
import com.essay_backend.service.IFileMapService;
import com.essay_backend.service.IProjectService;
import com.essay_backend.service.IStudentTeacherService;
import com.essay_backend.service.IUserService;
import com.essay_backend.service.impl.MailService;
import com.essay_backend.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.essay_backend.utils.myUtils.deleteFile;

@RestController
@RequestMapping("/auxiliary")
public class AuxiliaryHandler {
    @Autowired
    IFileMapService fileMapService;

    @Autowired
    IUserService userService;

    @Autowired
    IProjectService projectService;
    @Autowired
    IStudentTeacherService studentTeacherService;

    @Autowired
    MailService mailService;

    @GetMapping("/allReviewZip/j87y5ndh4sbg678h")
    public ResponseEntity<ByteArrayResource> allReviewZip() throws Exception {

        String sourceDirectory = "E:\\EssayManageFile\\review";
        System.out.println(sourceDirectory);
        File sourceDir = new File(sourceDirectory);
        ZipUtils zipUtils = new ZipUtils(fileMapService);
        zipUtils.fileToZip(sourceDir);
        Path zipPath = Paths.get(sourceDirectory + ".zip");
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

    @GetMapping("/allPaperZip/j87y5ndh4sbg678h")
    public ResponseEntity<ByteArrayResource> allPaperZip() throws Exception {

        String sourceDirectory = "E:\\EssayManageFile\\Student";
        System.out.println(sourceDirectory);
        File sourceDir = new File(sourceDirectory);
        ZipUtils zipUtils = new ZipUtils(fileMapService);
        zipUtils.fileToZip(sourceDir);
        Path zipPath = Paths.get(sourceDirectory + ".zip");
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

    @GetMapping("/projectInfo/j87y5ndh4sbg678h")
    public Map<String, Object> projectInfo() {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> projectInfo = new ArrayList<>();
        for (Project project : projectService.list()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", project.getProjId());
            map.put("name", userService.getById(project.getProjId()).getName());
            map.put("projName", project.getProjName());
            map.put("state", project.getState());
            projectInfo.add(map);
        }
        int[] order = new int[]{1, -3, -2, -1, 2};
        projectInfo = projectInfo.stream().sorted(Comparator.comparingInt(o -> order[(int) o.get("state")])).toList();
        res.put("msg", "success");
        res.put("projectInfo", projectInfo);
        return res;
    }

    @GetMapping("/teacherInfo/j87y5ndh4sbg678h")
    public Map<String, Object> teacherInfo() {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> teacherInfo = new ArrayList<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 1);
        List<User> teachers = userService.list(queryWrapper);

        List<StudentTeacher> studentTeacherList = studentTeacherService.list();

        for (User teacher : teachers) {
            int instructorTodoNum = 0;
            int instructorDoneNum = 0;
            int supervisorTodoNum = 0;
            int supervisorDoneNum = 0;
            Map<String, Object> map = new HashMap<>();
            for (StudentTeacher match : studentTeacherList) {
                if (Objects.equals(teacher.getUid(), match.getInstructorId())) {
                    instructorTodoNum++;
                    int state = projectService.getById(match.getSid()).getState();
                    if (state == 4 || state == 2) {
                        instructorDoneNum++;
                    }
                }
                if (Objects.equals(teacher.getUid(), match.getSupervisorId())) {
                    supervisorTodoNum++;
                    int state = projectService.getById(match.getSid()).getState();
                    if (state == 4 || state == 3) {
                        supervisorDoneNum++;
                    }
                }
            }
            if (instructorTodoNum == instructorDoneNum && supervisorTodoNum == supervisorDoneNum) {
                map.put("state", 1);
            } else {
                map.put("state", 0);
            }
            map.put("id", teacher.getUid());
            map.put("name", teacher.getName());
            map.put("instructorTodoNum", instructorTodoNum);
            map.put("instructorDoneNum", instructorDoneNum);
            map.put("supervisorTodoNum", supervisorTodoNum);
            map.put("supervisorDoneNum", supervisorDoneNum);
            teacherInfo.add(map);
        }
        teacherInfo = teacherInfo.stream().sorted(Comparator.comparingInt(e -> (int) e.get("state"))).toList();
        res.put("msg", "success");
        res.put("teacherInfo", teacherInfo);
        return res;
    }

    @GetMapping("/mailTeacher/j87y5ndh4sbg678h")
    public Map<String, String> mailTeacher() {
        List<Map<String, Object>> teacherInfo = (List<Map<String, Object>>) teacherInfo().get("teacherInfo");
        Map<String, String> map = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (Map<String, Object> teacherMap : teacherInfo) {
            int teacherId = (int) teacherMap.get("id");
            String teacherName = (String) teacherMap.get("name");
            String toEmail = userService.getById(teacherId).getEmail();
            try {
                mailService.sendEmail(toEmail, "毕业论文查看提醒",
                        "尊敬的" + teacherName + "老师，您好！\n 需您评审的毕业论文还未完成，请您前往 http://10.20.81.187:3000/ 查看论文后提交评阅/指导意见表，非常感谢!");
            } catch (MessagingException e) {
//                e.printStackTrace();
                stringBuilder.append(teacherName).append(",");
//                map.put("msg", teacherName + "邮件发送失败 ");
            }
        }
        System.out.println(stringBuilder.toString());
        if(stringBuilder.toString().equals("")) {
            map.put("msg","success");
        }else {
            map.put("msg", stringBuilder.toString() + " 邮件发送失败 ");
        }
        return map;

    }


}
