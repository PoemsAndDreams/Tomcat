package com.dreams.ClassLoader;

import java.net.URL;
import java.net.URLClassLoader;

public class WebaddsClassLoader extends URLClassLoader {

    //自定义类加载器自定义加载目录
    public WebaddsClassLoader(URL[] urls) {
        super(urls);
    }
}
