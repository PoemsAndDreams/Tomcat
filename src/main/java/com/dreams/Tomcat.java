package com.dreams;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tomcat {

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
                executorService.execute(new SocketProcessor(socket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.start();
        System.out.println("Hello world!");
    }
}