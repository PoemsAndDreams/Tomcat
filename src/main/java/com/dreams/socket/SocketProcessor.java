package com.dreams.socket;

import com.dreams.Tomcat;
import com.dreams.common.Context;
import com.dreams.http.Request;
import com.dreams.http.Response;
import com.dreams.servlet.DefaultServlet;
import com.dreams.servlet.NotFindServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class SocketProcessor implements Runnable {

    private Socket socket;
    private Tomcat tomcat;

    public SocketProcessor(Socket socket, Tomcat tomcat) {
        this.socket = socket;
        this.tomcat = tomcat;
    }

    @Override
    public void run() {
        //具体处理
        process(socket);
    }

    private void process(Socket socket) {
        //处理逻辑
        try {
            //比如浏览器发送数据，在这里获取发送的数据
            InputStream inputStream = socket.getInputStream();
            //构造一个字节数组,1KB
            byte[] bytes = new byte[1024];
            //每次读取1KB，暂时先如此，今后优化
            inputStream.read(bytes);
            String re = new String(bytes);
            int index1, index2, index3;
            index1 = re.indexOf(' ');
            index2 = re.indexOf(' ', index1 + 1);
            index3 = re.indexOf('\r');
            String method = re.substring(0, index1);
            String url = re.substring(index1 + 1, index2);
            String protocl = re.substring(index2, index3);
            System.out.println(" " + method + " " + url + " " + protocl);
            Request request = new Request(method, url, protocl, socket);
            Response response = new Response(request);

            //直接处理静态资源,取巧只处理html文件
            if (url.contains(".html")) {
                //拿到当前目录下的webapps目录
                File file = new File(System.getProperty("user.dir") + "\\webapps" + url);
                //返回目标资源
                FileInputStream fileInputStream = new FileInputStream(file);
                //就直接使用等大的大小了，不再优化了
                byte[] disposeBytes = new byte[(int) file.length()];
                fileInputStream.read(disposeBytes);
                response.addHeader("Content-Length", disposeBytes.length + "");
                response.getOutputStream().write(disposeBytes);
                response.send();
                return;
            }
            //拆分/Text/text......如/应用名/servlet路径名
            String requestURI = request.getUrl().toString();
            requestURI = requestURI.substring(1);
            String[] paths = requestURI.split("/");
            //应用名
            String appName = paths[0];
            //获取到tomcat保存的servlet
            Context context = tomcat.getContextMap().get(appName);
            if (context != null) {
                //servlet路径名
                String servletName = paths[1];
                //根据名字获取到servlet
                Servlet servlet = context.getUrlurlPattern(servletName);
                if (servlet != null) {
                    //得到servlet对象，
                    servlet.service(request, response);
                    //发送响应
                    response.send();
                } else {
                    System.out.println("Not Find Path!");
                    NotFindServlet notFindServlet = new NotFindServlet();
                    notFindServlet.service(request, response);
                    //发送响应
                    response.send();

                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

    }

}
