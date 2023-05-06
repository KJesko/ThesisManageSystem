package com.essay_backend.controller;

import com.essay_backend.controller.Decrypt.DecryptParameter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
@CrossOrigin
public class PermissionHandler {
    @PostMapping("/routes")
    public Map<String, Object> routes(@DecryptParameter("roleName") String roleName) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        List<String> authedRoutes = new ArrayList<>();
        map.put("status",0);
        map.put("success",true);
        map.put("message","成功");
        if (roleName.equals("Admin")) {
            authedRoutes.add("/userManagement");
            authedRoutes.add("/Auxiliary");
        }
        if (roleName.equals("teacher")) {
            authedRoutes.add("/review");
        }
        if (roleName.equals("student")) {
            authedRoutes.add("/studentEssay");
            authedRoutes.add("/Feedback");
        }
        data.put("authedRoutes",authedRoutes);
        map.put("data",data);
        return map;
    }

    @PostMapping("/permissions")
    public Map<String, Object> permission(@DecryptParameter("roleName") String roleName) {
        System.out.println(roleName);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        List<String> permissions = new ArrayList<>();
        map.put("status",0);
        map.put("success",true);
        map.put("message","成功");
        if (roleName.equals("Admin")) {
            permissions.add("mode:Admin");
        }
        if (roleName.equals("teacher")) {
            permissions.add("mode:teacher");
        }
        if (roleName.equals("student")) {
            permissions.add("mode:student");
        }
        // permissions.add("test:permission-btn3");
        data.put("permissions",permissions);
        map.put("data",data);
        return map;
    }

}
