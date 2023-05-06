package com.essay_backend.controller.Interceptor;


import com.alibaba.fastjson.JSON;
import com.essay_backend.utils.AesUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//@Component
//public class DecryptInterceptor implements HandlerInterceptor {
//
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String encryptedParams = request.getParameter("params");
//        String decryptedParams = AesUtils.AesDecrypt(encryptedParams); // 解密方法需要自己实现
//        Map<String,Object> map = JSON.parseObject(decryptedParams);
//
//        request = new ModifyQueryStringWrapper(request,map);
//        System.out.println(request.getQueryString());
//        return true;
//    }
//
//}
