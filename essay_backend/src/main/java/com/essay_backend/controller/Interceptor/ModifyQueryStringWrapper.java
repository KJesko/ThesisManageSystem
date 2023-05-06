package com.essay_backend.controller.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModifyQueryStringWrapper extends HttpServletRequestWrapper {

    private final String queryString;
    private final Map<String,Object> map;

    public ModifyQueryStringWrapper(HttpServletRequest request, Map<String,Object> map) {
        super(request);
        this.map = map;

        StringBuilder stringBuilder = new StringBuilder();
        Set<String> set = map.keySet();

        set.forEach(s -> stringBuilder.append(s).append("=").append(map.get(s)).append("&"));

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        this.queryString = stringBuilder.toString();

    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getParameter(String name) {
        Object value = map.get(name);
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String[] newValue = new String[]{String.valueOf(value)};
            parameterMap.put(key, newValue);
        }
        return parameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        Object value = map.get(name);
        if (value == null) {
            return null;
        }
        return new String[]{String.valueOf(value)};
    }
}