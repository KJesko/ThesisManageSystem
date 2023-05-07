package com.essay_backend.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essay_backend.Entity.FileMap;
import com.essay_backend.Entity.Project;
import com.essay_backend.Entity.StudentTeacher;
import com.essay_backend.Entity.User;
import com.essay_backend.service.IFileMapService;
import com.essay_backend.service.IProjectService;
import com.essay_backend.service.IStudentTeacherService;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.essay_backend.utils.myUtils.createDir;
import static com.essay_backend.utils.myUtils.deleteFile;

@RestController
@RequestMapping("/review")
public class ReviewHandler {
    @Autowired
    IStudentTeacherService studentTeacherService;
    @Autowired
    IProjectService projectService;
    @Autowired
    IFileMapService fileMapService;

    @GetMapping("/studentForInstructor/{tid}/j87y5ndh4sbg678h")
    public List<Map<String, Object>> studentForInstructor(@PathVariable("tid") Integer tid) {
        List<Map<String, Object>> list = new ArrayList<>();
        QueryWrapper<StudentTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("instructor_id", tid);
        int[] order = new int[]{1, 0, 2, 0, 2};
        return getStudentMaps(list, wrapper).stream().sorted(
                (Comparator.comparingInt(o -> order[(int) o.get("state")])
                )).toList();
    }

    @GetMapping("/studentForSupervisor/{tid}/j87y5ndh4sbg678h")
    public List<Map<String, Object>> studentForSuperVisor(@PathVariable("tid") Integer tid) {
        List<Map<String, Object>> list = new ArrayList<>();
        QueryWrapper<StudentTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("supervisor_id", tid);
        int[] order = new int[]{1, 0, 0, 2, 2};
        return getStudentMaps(list, wrapper).stream().sorted(
                (Comparator.comparingInt(o -> order[(int) o.get("state")])
                )).toList();
    }

    private List<Map<String, Object>> getStudentMaps(List<Map<String, Object>> list, QueryWrapper<StudentTeacher> wrapper) {
        List<StudentTeacher> studentTeacherList = studentTeacherService.list(wrapper);
        for (StudentTeacher studentTeacher : studentTeacherList) {
            Map<String, Object> map = new HashMap<>();//每个学生返回一个map
            map.put("sid", studentTeacher.getSid());
            map.put("name", studentTeacher.getSName());
            Project project = projectService.getById(studentTeacher.getSid());
            if (project == null) {
                map.put("projName", null);
                map.put("state", null);
            } else {
                map.put("projName", project.getProjName());
                map.put("state", project.getState());
            }

            list.add(map);
        }
        return list;
    }


    @PostMapping("/submit/{num}/{projId}/j87y5ndh4sbg678h")
    public Map<String, String> uploadReview(@RequestParam(value = "file") MultipartFile file, @PathVariable("num") Integer num, @PathVariable("projId") Integer projId) throws IOException {
        Map<String, String> map = new HashMap<>();
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        System.out.println(projId);
        String fileId = UUID.randomUUID().toString();
        String path = "E:\\EssayManageFile\\review\\" + projId + "\\";
        createDir(path);

        file.transferTo(new File(path + fileId));

        Project project = projectService.getById(projId); //一定要有proj，
        if (project == null) {
            map.put("msg", "数据库错误");
        } else {
            projectService.removeById(projId);
            String oldFileId;
            if (num == 0) {
                if (project.getInstructorFileId() != null) {//原来存在指导意见，删除文件
                    oldFileId = project.getInstructorFileId();
                    deleteFile(path + oldFileId);
                    fileMapService.removeById(oldFileId);
                }
                FileMap fileMap = new FileMap();
                fileMap.setFileId(fileId);
                fileMap.setFileName(fileName);
                fileMapService.save(fileMap);
                project.setInstructorFileId(fileId);
                project.setInstructorFileName(fileName);
                if (project.getState() == 1) {
                    project.setState(2);
                } else if (project.getState() == 3) {
                    project.setState(4);

                }

            } else if (num == 1) {
                if (project.getSupervisorFileId() != null) {
                    oldFileId = project.getSupervisorFileId();
                    deleteFile(path + oldFileId);
                    fileMapService.removeById(oldFileId);

                }
                FileMap fileMap = new FileMap();
                fileMap.setFileId(fileId);
                fileMap.setFileName(fileName);
                fileMapService.save(fileMap);
                project.setSupervisorFileId(fileId);
                project.setSupervisorFileName(fileName);
                if (project.getState() == 1) {
                    project.setState(3);
                } else if (project.getState() == 2) {
                    project.setState(4);
                }
            }

            projectService.save(project);

            map.put("msg", "上传成功");

        }
        return map;

    }

    @GetMapping("remove/{num}/{uid}/{fileId}/j87y5ndh4sbg678h")
    public Map<String, String> removeReview(@PathVariable("num") Integer num, @PathVariable("uid") Integer uid, @PathVariable("fileId") String fileId) {
        Project project = projectService.getById(uid);
        String path = "E:\\EssayManageFile\\review\\" + uid + "\\";
        Map<String, String> map = new HashMap<>();
        try {
            if (project == null) {
                map.put("msg", "数据库出错");
            } else {
                if (num == 0) {
                    project.setInstructorFileId(null);
                    project.setInstructorFileName(null);
                    if (project.getState() == 2) {
                        project.setState(1);
                    } else if (project.getState() == 4) {
                        project.setProjId(3);
                    }
                    fileMapService.removeById(fileId);
                    projectService.removeById(uid);
                    projectService.save(project);

                } else if (num == 1) {
                    project.setSupervisorFileName(null);
                    project.setSupervisorFileId(null);
                    if (project.getState() == 3) {
                        project.setState(1);
                    } else if (project.getState() == 4) {
                        project.setProjId(2);
                    }
                    fileMapService.removeById(fileId);
                    projectService.removeById(uid);
                    projectService.save(project);
                }
            }
            deleteFile(path + fileId);
            map.put("msg", "success");
            return map;
        } catch (Exception e) {
            map.put("msg", "服务器出错");
            return map;
        }

    }

    @GetMapping("download/{num}/{uid}/j87y5ndh4sbg678h")
    public ResponseEntity<Resource> downloadReview(@PathVariable("num") Integer num, @PathVariable("uid") Integer uid) throws IOException {
        // 读取文件内容
        Project project = projectService.getById(uid);
        String path = "E:\\EssayManageFile\\review\\" + uid + "\\";

        String fileId = "";
        String fileName = "";
        if (project == null) {
            return null;
        } else if (num == 0) {
            if (project.getInstructorFileId() != null) {
                fileId = project.getInstructorFileId();
                fileName = project.getInstructorFileName();
            } else {
                return null;
            }
        } else if (num == 1) {
            if (project.getSupervisorFileName() != null) {
                fileId = project.getSupervisorFileId();
                fileName = project.getSupervisorFileName();
            } else {
                return null;
            }
        }


        Path filePath = Paths.get(path + fileId);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));

        // 设置 HTTP 响应头，指定文件名和下载方式
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        // 返回文件内容和响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("info/{num}/{uid}/j87y5ndh4sbg678h")
    public Map<String, Object> reviewInfo(@PathVariable("uid") Integer uid, @PathVariable("num") Integer num) {
        System.out.println("review info");
        Map<String, String> map = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        List<Map<String, String>> list = new ArrayList<>();
        Project project = projectService.getById(uid);
        if (project == null) {
            res.put("msg", "数据库出错");
            res.put("fileInfo", null);

        } else {
            if (num == 0 && project.getInstructorFileId() != null) {
                map.put("id", project.getInstructorFileId());
                map.put("name", project.getInstructorFileName());
                list.add(map);

            } else if (num == 1 && project.getSupervisorFileId() != null) {
                map.put("id", project.getSupervisorFileId());
                map.put("name", project.getSupervisorFileName());
                list.add(map);
            }

            res.put("fileInfo", list);
        }
        StudentTeacher studentTeacher = studentTeacherService.getById(uid);
        if (studentTeacher == null) {
            res.put("instructorName", "暂无");
            res.put("supervisorName", "暂无");
            res.put("instructorId", "暂无");
            res.put("supervisorId", "暂无");
        } else {
            res.put("instructorName", studentTeacher.getInstructorName());
            res.put("instructorId", studentTeacher.getInstructorId());
            res.put("supervisorName", studentTeacher.getSupervisorName());
            res.put("supervisorId", studentTeacher.getSupervisorId());
        }

        return res;
    }

    @GetMapping("template/{num}/{sid}/j87y5ndh4sbg678h")//0为指导，1为评阅
    public ResponseEntity<Resource> downloadTemplate(@PathVariable("num") Integer num, @PathVariable("sid") Integer sid) throws IOException {
        // 读取文件内容

        Path filePath;
        if (num == 0) {
            filePath = Paths.get("E:\\EssayManageFile\\template\\南方科技大学本科生毕业设计（论文）指导教师审阅意见表.docx");
        } else if (num == 1) {
            filePath = Paths.get("E:\\EssayManageFile\\template\\南方科技大学本科生毕业设计（论文）评阅人意见表.docx");
        } else {
            return null;
        }
        String tempFile = "E:\\EssayManageFile\\template\\temp.docx";

        Project project = projectService.getById(sid);
        StudentTeacher studentTeacher = studentTeacherService.getById(sid);
        XWPFDocument document = new XWPFDocument(new FileInputStream(filePath.toString()));
        XWPFTable table = document.getTableArray(0);
        XWPFTableCell projectNameCell = table.getRow(0).getCell(1);
        XWPFTableCell studentNameCell = table.getRow(1).getCell(1);
        XWPFTableCell studentIdCell = table.getRow(1).getCell(3);
        XWPFTableCell majorCell = table.getRow(1).getCell(5);
        projectNameCell.setText(project.getProjName());
        studentNameCell.setText(studentTeacher.getSName());
        studentIdCell.setText(sid.toString());
        majorCell.setText(studentTeacher.getMajor());
        FileOutputStream out = new FileOutputStream(tempFile);
        document.write(out);
        out.close();

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(tempFile)));
        deleteFile(tempFile);
        // 设置 HTTP 响应头，指定文件名和下载方式
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.docx");

        // 返回文件内容和响应头
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("feedback/{sid}/j87y5ndh4sbg678h")
    public Map<String, String> getFeedback(@PathVariable("sid") Integer sid) {
        // 读取文件内容
        Map<String, String> map = new HashMap<>();
        Project project = projectService.getById(sid);
        Path instructorFilePath = Paths.get("E:\\EssayManageFile\\review\\" + sid + "\\" + project.getInstructorFileId());
        Path supervisorFilePath = Paths.get("E:\\EssayManageFile\\review\\" + sid + "\\" + project.getSupervisorFileId());

        String instructorString = handleParseFeedback(parseFeedback(project.getInstructorFileName(), instructorFilePath));
        String supervisorString = handleParseFeedback(parseFeedback(project.getSupervisorFileName(), supervisorFilePath));
        map.put("instructorString", instructorString);
        map.put("supervisorString", supervisorString);
        map.put("title", project.getProjName());
        return map;
    }

    public String parseFeedback(String fileName, Path filePath) {
        if (fileName == null) {
            return null;
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            if (extension.equals("pdf")) {
                File file = new File(filePath.toString());
                PDFParser parser = new PDFParser(new RandomAccessFile(file, "rw"));
                // 对PDF文件进行解析
                parser.parse();
                // 获取解析后得到的PDF文档对象
                PDDocument pdfdocument = parser.getPDDocument();
                // 新建一个PDF文本剥离器
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(false); //sort:设置为true 则按照行进行读取，默认是false
                // 从PDF文档对象中剥离文本
                String text = stripper.getText(pdfdocument);
                int index = text.indexOf("评阅人意见：");
                if (index < 0){
                    index = text.indexOf("指导教师审阅意见：");
                }

                if (index != -1) {
                    text = text.substring(index);
                }
                System.out.println(text);
                return text.replace("\r\n", "");

            } else if (extension.equals("doc") || extension.equals("docx")) {
                File file = new File(filePath.toString());
                FileInputStream fis = new FileInputStream(file);
                XWPFDocument document = new XWPFDocument(fis);

                // Get the first table in the document
                XWPFTable table = document.getTables().get(0);

                // Iterate through the rows in the table
                XWPFTableCell feedbackCell = table.getRow(3).getCell(0);
                String text = feedbackCell.getText();
                System.out.println(text);
                // Close the document
                fis.close();
                document.close();
                return text;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String handleParseFeedback(String text) {
        if (text == null) {
            return "暂无";
        }

        String prefix1 = "指导教师审阅意见：";
        String prefix2 = "评阅人意见：";
        String suffix1 = "指导教师（签名）：";
        String suffix2 = "评阅人（签名）：";
        String result = null;

        if (text.startsWith(prefix1)) {
            result = text.substring(prefix1.length());
            int index = result.lastIndexOf(suffix1);
            if (index >= 0) {
                result = result.substring(0, index);
            }
        } else if (text.startsWith(prefix2)) {
            result = text.substring(prefix2.length());
            int index = result.lastIndexOf(suffix2);
            if (index >= 0) {
                result = result.substring(0, index);
            }
        }
        return Objects.requireNonNullElse(result, "解析失败");

    }

}
