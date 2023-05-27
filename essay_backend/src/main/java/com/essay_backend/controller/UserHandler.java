package com.essay_backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essay_backend.Entity.Project;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.Entity.User;
import com.essay_backend.controller.Decrypt.DecryptParameter;
import com.essay_backend.service.IProjectService;
import com.essay_backend.service.IStudentTeacherService;
import com.essay_backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class UserHandler {
    @Autowired
    private IUserService userService;
    @Autowired
    private IStudentTeacherService studentTeacherService;
    @Autowired
    private IProjectService projectService;

    @GetMapping
    public List<User> getAll() {
        return userService.list();
    }

    @PostMapping("/login")
    public Map<String, Object> login(@DecryptParameter(value = "uid") Integer uid, @DecryptParameter(value = "password") String pwd) {

        User user = userService.getById(uid);
//        System.out.println(res);

        Map<String, Object> map = new HashMap<>();

        if (user != null) {

            if (user.getPwd().equals(pwd)) {
                map.put("status", 0);
                Map<String, String> data = new HashMap<String, String>();
                data.put("accessToken", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6IjIyODA1MjAxMjhAcXEuY29tIiwic3ViIjo5LCJpYXQiOjE2MjU4MzQ3MTksImV4cCI6MTYyODQyNjcxOX0.YQLVi-zw4XWQEd8Hy2YZGlFaqX8c7xyRPrYuxcFywFE");
                map.put("data", data);
                map.put("success", true);
                map.put("message", "成功");
                return map;
            } else {
                map.put("status", 0);
                map.put("data", null);
                map.put("message", "账号密码错误!");
            }

        } else {
            map.put("status", 0);
            map.put("data", null);
            map.put("message", "没有这个用户!请确认用户id或者联系管理员");
        }

        return map;
    }

    @PostMapping("/userInfo")
    public Map<String, Object> userInfo(@DecryptParameter("uid") Integer uid) {
        User user = userService.getById(uid);

        Map<String, Object> map = new HashMap<>();

        if (user != null) {

            map.put("uid", uid);
            map.put("name", user.getName());
            map.put("status", 0);
            Map<String, String> data = new HashMap<String, String>();
            switch (user.getType()) {
                case 0:
                    data.put("roleName", "student");
                    break;
                case 1:
                    data.put("roleName", "teacher");
                    break;
                case 2:
                    data.put("roleName", "Admin");
                    break;
            }
//            data.put("tag",user.getTag());
            map.put("data", data);
            map.put("success", true);
            map.put("message", "成功");
            return map;
        }
        map.put("status", 0);
        map.put("data", null);
        map.put("message", "Login failed !");
        return map;
    }

    @PostMapping("/editPassword")
    public Map<String, String> editPassword(@DecryptParameter("id") Integer id,@DecryptParameter("old") String old,@DecryptParameter("new")String nev){
        Map<String,String> res = new HashMap<>();
        User user = userService.getById(id);
        if (user == null){
            res.put("msg","数据库错误");
        }else {
            if (Objects.equals(user.getPwd(), old)){
                user.setPwd(nev);
                userService.removeById(id);
                userService.save(user);
                res.put("msg","success");
            }  else {
                res.put("msg","验证失败，请重试！");
            }

        }
        return res;
    }


    @PostMapping("/createMatchByCSV/j87y5ndh4sbg678h")
    public void createMatchByCSV(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        String path = "E:\\EssayManageFile\\Init\\" + fileName;
        file.transferTo(new File(path));
        String[] lineStringArray;
        List<String> lineStringList;
        String colName;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), Charset.forName("GBK")))) {
            String lineString = br.readLine();
            colName = lineString; //first row -- column name
            System.out.println(colName);
            if (colName.startsWith("学号,姓名,专业,指导教师工号,指导教师,指导教师工号,评阅老师")) {
                System.out.println("学号,姓名,专业,指导教师工号,指导教师,指导教师工号,评阅老师");
                while ((lineString = br.readLine()) != null) {
                    lineStringArray = lineString.trim().
                            split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                    StudentTeacher studentTeacher = new StudentTeacher();
                    studentTeacher.setSid(Integer.parseInt(lineStringArray[0]));
                    studentTeacher.setSName(lineStringArray[1]);
                    studentTeacher.setMajor(lineStringArray[2]);
                    studentTeacher.setInstructorId(Integer.parseInt(lineStringArray[3]));
                    studentTeacher.setInstructorName(lineStringArray[4]);
                    studentTeacher.setSupervisorId(Integer.parseInt(lineStringArray[5]));
                    studentTeacher.setSupervisorName(lineStringArray[6]);
//
                    if (studentTeacherService.getById(Integer.parseInt(lineStringArray[0])) != null) {
                        studentTeacherService.removeById(Integer.parseInt(lineStringArray[0]));
                    }
                    studentTeacherService.save(studentTeacher);
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }


    }

    @GetMapping("/deleteAllMatch/j87y5ndh4sbg678h")
    public void deleteAllMatch() {
        studentTeacherService.remove(null);
    }

    @GetMapping("/deleteMatch/{sid}/j87y5ndh4sbg678h")
    public Map<String, String> deleteMatch(@PathVariable("sid") Integer sid) {
        Map<String, String> map = new HashMap<>();
        try {
            studentTeacherService.removeById(sid);
            map.put("msg", "success");
        } catch (Exception e) {
            map.put("msg", "fail");
        }
        return map;
    }

    @GetMapping("/allMatchInfo/{number}/{size}/j87y5ndh4sbg678h")
    public List<StudentTeacher> allStudentTeacherInfo(@PathVariable("number") Integer number, @PathVariable("size") Integer size) {
        return studentTeacherService.getAllMatchInfo(number, size);
    }

    @GetMapping("/allMatchInfo/count/j87y5ndh4sbg678h")
    public Integer allStudentTeacherCount() {
        return studentTeacherService.getAllMatchInfo().size();
    }

    @PostMapping("/allUserPageInfo/j87y5ndh4sbg678h")
    public List<User> getAllUserPageInfo(@DecryptParameter("limit") String pageSize, @DecryptParameter("page") String pageNumber) {
        return userService.getAllUserPageInfo(pageNumber, pageSize);
    }

    @GetMapping("/allUserCount/j87y5ndh4sbg678h")
    public Integer getAllUserCount() {
        return userService.getAllUserCount();
    }

    @PostMapping("/createUserByCSV/j87y5ndh4sbg678h")
    public Map<String, String> createUserByCSV(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        String path = "E:\\EssayManageFile\\Init\\" + fileName;
        file.transferTo(new File(path));
        String[] lineStringArray;
        List<String> lineStringList;
        String colName;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), Charset.forName("GBK")))) {
            String lineString = br.readLine();
            colName = lineString; //first row -- column name
            System.out.println(colName);
            if (colName.startsWith("学号/工号,姓名,密码,身份,邮箱")) {
                while ((lineString = br.readLine()) != null) {
                    lineStringArray = lineString.trim().
                            split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
                    System.out.println(lineStringArray.length);

                    User user = new User();
                    user.setUid(Integer.parseInt(lineStringArray[0]));
                    user.setName(lineStringArray[1]);
                    user.setPwd(lineStringArray[2]);
                    if (lineStringArray[3].equals("学生")) {
                        user.setType(0);
                    } else if (lineStringArray[3].equals("老师")) {
                        user.setType(1);
                    } else if (lineStringArray[3].equals("管理员")) {
                        user.setType(2);
                    } else {
                        continue;
                    }

                    user.setEmail(lineStringArray[4]);
//
                    if (userService.getById(Integer.parseInt(lineStringArray[0])) != null) {
                        userService.removeById(Integer.parseInt(lineStringArray[0]));
                    }
                    userService.save(user);
                    if (user.getType() == 0) {
                        Project project = projectService.getById(user.getUid());
                        if (project == null) {
                            project = new Project();
                            project.setProjId(user.getUid());
                            projectService.save(project);
                            System.out.println("创建学生同时创建项目");
                        }

                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
        System.out.println("用户创建完成");
        Map<String, String> map = new HashMap<>();
        map.put("message", "success");
        System.out.println("成功返回");

        return map;
    }

    @GetMapping("/deleteUser/{uid}/j87y5ndh4sbg678h")
    public void deleteUser(@PathVariable String uid) {
        userService.removeById(uid);
    }

    @GetMapping("/deleteAllUser/j87y5ndh4sbg678h")
    public void deleteAllUser() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.ne("uid", 10000001);
        userService.remove(wrapper);
    }

    @GetMapping("/getAllTeacher/j87y5ndh4sbg678h")
    public List<User> getAllTeacher() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("type", 1);
        return userService.list(wrapper);
    }

    @PostMapping("/updateMatch/j87y5ndh4sbg678h")
    public void updateMatch(@RequestParam("sid") Integer sid,
                            @RequestParam("instructorId") Integer instructorId,
                            @RequestParam("supervisorId") Integer supervisorId) {
        String instructorName = userService.getById(instructorId).getName();
        String supervisorName = userService.getById(supervisorId).getName();
//        studentTeacherService.updateMatch(Integer.parseInt(instructorId),instructorName,Integer.parseInt(supervisorId),supervisorName,Integer.parseInt(sid));
        studentTeacherService.updateMatch(instructorId, instructorName, supervisorId, supervisorName, sid);
    }

    @PostMapping("/MatchSearch/j87y5ndh4sbg678h")
    public List<StudentTeacher> SearchInfo(@RequestParam("searchString") String search,
                                           @RequestParam("number") Integer number,
                                           @RequestParam("size") Integer size) {

        Predicate<StudentTeacher> predicate = studentTeacher ->
                studentTeacher.toString2().contains(search);
        return studentTeacherService.getAllMatchInfo().stream().filter(predicate).skip((long) (number - 1) * size).limit(size).collect(Collectors.toList());
    }

    @PostMapping("/newMatch/j87y5ndh4sbg678h")
    public Map<String, String> newMatch(@RequestParam("sid") Integer sid,
                                        @RequestParam("name") String name,
                                        @RequestParam("major") String major,
                                        @RequestParam("instructorId") Integer instructorId,
                                        @RequestParam("supervisorId") Integer supervisorId) {
        Map<String, String> map = new HashMap<>();
        User user = userService.getById(sid);
        if (user == null) { //创建关系前先保证user非空，最后关系创建成功，同时也创建一个账号
            user = new User();
            user.setUid(sid);
            user.setName(name);
            user.setPwd("123456");
            user.setType(0);
        }

        if (!Objects.equals(user.getName(), name)) {
            map.put("msg", "学生id已存在但是名字不一致，关系创建失败");
            return map;
        } else {
            StudentTeacher studentTeacher = studentTeacherService.getById(sid);
            if (studentTeacher == null) {
                User instructor = userService.getById(instructorId);
                User supervisor = userService.getById(supervisorId);
                studentTeacher = new StudentTeacher();
                studentTeacher.setSid(sid);
                studentTeacher.setSName(name);
                studentTeacher.setMajor(major);
                studentTeacher.setInstructorId(instructorId);
                studentTeacher.setInstructorName(instructor.getName());
                studentTeacher.setSupervisorId(supervisorId);
                studentTeacher.setSupervisorName(supervisor.getName());
                userService.save(user);
                studentTeacherService.save(studentTeacher);
                Project project = new Project();
                project.setProjId(sid);
                projectService.save(project);
                map.put("msg", "success");
                return map;
            } else {
                map.put("msg", "学生已有配对关系，关系创建失败");
                return map;
            }
        }

    }
    @PostMapping("/newUser/j87y5ndh4sbg678h")
    public Map<String, String> newUser(@RequestParam("uid") Integer uid,
                                        @RequestParam("name") String name,
                                        @RequestParam("pwd") String pwd,
                                        @RequestParam("type") Integer type,
                                        @RequestParam("email") String email) {
        Map<String, String> map = new HashMap<>();
        User user = userService.getById(uid);
        if (user == null) { //创建关系前先保证user非空，最后关系创建成功，同时也创建一个账号
            user = new User();
            user.setUid(uid);
            user.setName(name);
            user.setPwd(pwd);
            user.setType(type);
            user.setEmail(email);
            userService.save(user);
            Project project = new Project();
            project.setProjId(uid);
            projectService.save(project);
            map.put("msg", "success");
            return map;
        }else {
            map.put("msg", "该学号已存在，用户创建失败");
            return map;
        }

    }

    @PostMapping("/updateUser/j87y5ndh4sbg678h")
    public Map<String, String> updateUser(@RequestParam("uid") Integer uid,
                                       @RequestParam("name") String name,
                                       @RequestParam("pwd") String pwd,
                                       @RequestParam("type") Integer type,
                                       @RequestParam("email") String email) {
        Map<String, String> map = new HashMap<>();
        User user = userService.getById(uid);
        if (user != null) { //user存在于表中，更新user信息
            user.setName(name);
            user.setPwd(pwd);
            user.setType(type);
            user.setEmail(email);
            userService.removeById(uid);
            userService.save(user);

            StudentTeacher studentTeacher = studentTeacherService.getById(uid);
            if (studentTeacher != null){
                studentTeacher.setSName(name);
                studentTeacherService.removeById(uid);
                studentTeacherService.save(studentTeacher);
            }
            map.put("msg", "success");
            System.out.println("更新用户");
            return map;
        }else {
            map.put("msg", "该学号不存在");
            System.out.println("更新用户");
            return map;
        }


    }



}
