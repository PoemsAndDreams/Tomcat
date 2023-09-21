package com.dreams.common;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private String appName;
    private Map<String,Servlet> urlPatternMap = new HashMap<>();

    public Context(String appName) {
        this.appName = appName;
    }

    //保存
    public void addUrlurlPatternMap(String urlPattern, Servlet servlet) {
        urlPatternMap.put(urlPattern,servlet);
    }

    //获取
    public Servlet getUrlurlPattern(String urlPattern){
        return urlPatternMap.get("/" + urlPattern);
    }
}
