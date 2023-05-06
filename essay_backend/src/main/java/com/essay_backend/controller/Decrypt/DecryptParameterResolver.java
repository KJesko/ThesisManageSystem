package com.essay_backend.controller.Decrypt;


import com.alibaba.fastjson.JSON;
import com.essay_backend.utils.AesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Component
public class DecryptParameterResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    public DecryptParameterResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DecryptParameter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;
        String encryptedString = request.getParameter("params");
        String decryptedString = AesUtils.AesDecrypt(encryptedString); // 解密方法需要自己实现
        System.out.println(decryptedString);
        Map<String,Object> map = JSON.parseObject(decryptedString);

        String decryptedValue =  map.get(Objects.requireNonNull(parameter.getParameterAnnotation(DecryptParameter.class)).value()).toString(); // 解密方法需要自己实现
        if (parameter.getParameterType() == String.class){
            return decryptedValue;
        }
        return objectMapper.readValue(decryptedValue, parameter.getParameterType());
    }
}


