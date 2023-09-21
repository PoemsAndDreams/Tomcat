package com.dreams;

import com.dreams.ClassLoader.WebaddsClassLoader;
import com.dreams.common.Context;
import com.dreams.socket.SocketProcessor;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tomcat {

    private Map<String,Context> contextMap = new HashMap<>();

    public void start(){
        try {
            //使用线程池
            ExecutorService executorService = Executors.newFixedThreadPool(30);
            //一个服务端的端口8080，socket连接TCP
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true){
                //得到socket连接
                Socket socket = serverSocket.accept();
                //交给线程池中的线程处理
                executorService.execute(new SocketProcessor(socket,this));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        //先初始化
        tomcat.init();
        tomcat.start();
        System.out.println("Hello world!");
    }

    private void init() {
        //先部署
        deploys();
    }

    private void deploys() {
        //拿到当前目录下的webapps目录
        File webapps = new File(System.getProperty("user.dir"), "webapps");
        //webapps目录下每一个app
        for (String app : webapps.list()) {
            //每一个app的部署
            deploy(webapps,app);
        }

    }
    
    

    private void deploy(File webapps, String appName) {
        //查看有哪些servlet

        //每个应用对应一个context对象
        Context context = new Context(appName);

        //获得appName文件夹
        File appDirectory = new File(webapps, appName);
        //获得classes文件夹
        File classesDirectory = new File(appDirectory, "classes");

        ////获得classes文件夹下所有文件
        List<File> files = getAllFilePath(classesDirectory);

        for (File file : files) {

            //获取全限定类名
            String path = file.getPath();
            path = path.replace(classesDirectory.getPath() + "\\", "");
            path = path.replace(".class","");
            path = path.replace("\\",".");

            //System.out.println(path);

            //加载一个类
            try {
                //自定义类加载器自定义加载目录
                WebaddsClassLoader webaddsClassLoader = new WebaddsClassLoader(new URL[]{classesDirectory.toURL()});
                Class<?> servletClass = webaddsClassLoader.loadClass(path);

                //判断是否是HttpServlet的子类
                if (HttpServlet.class.isAssignableFrom(servletClass)){
                    //判断是否存在WebServlet注解
                    if (servletClass.isAnnotationPresent(WebServlet.class)){
                        //获取WebServlet注解内容
                        WebServlet annotation = servletClass.getAnnotation(WebServlet.class);
                        //获取WebServlet参数urlPatterns内容
                        String[] urlPatterns = annotation.urlPatterns();

                        //这里保存在common下的context,一个应用保存在一个Map
                        for (String urlPattern : urlPatterns) {
                            context.addUrlurlPatternMap(urlPattern, (Servlet) servletClass.newInstance());
                        }

                    }
                }


            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }


        }

        contextMap.put(appName,context);

    }


    //获取tomcat也会保存有哪些应用
    public Map<String, Context> getContextMap() {
        return contextMap;
    }

    //递归调用获取所有目录
    public List<File> getAllFilePath(File srcFile){
        ArrayList<File> fileList = new ArrayList<>();
        File[] files = srcFile.listFiles();

        if (files != null){
            for (File file : files) {
                if (file.isDirectory()){
                    fileList.addAll(getAllFilePath(file));
                }
                else {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }


}