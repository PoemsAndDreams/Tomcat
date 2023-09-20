package com.dreams;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class SocketProcessor implements Runnable{

    private Socket socket;

    public SocketProcessor(Socket socket) {
        this.socket = socket;
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
            int index1,index2,index3;
            index1 = re.indexOf(' ');
            index2 = re.indexOf(' ',index1+1);
            index3 = re.indexOf('\r');
            String method = re.substring(0,index1);
            String url = re.substring(index1,index2);
            String protocl = re.substring(index2,index3);
            System.out.println(" " + method + " " + url + " " + protocl);
            Request request = new Request(method, url, protocl,socket);
            Response response = new Response(request);
            Servlet servlet = new Servlet();
            servlet.service(request,response);
            //发送响应
            response.send();

        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }

    }

}
