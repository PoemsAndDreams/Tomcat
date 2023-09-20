package com.dreams;

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
            //暂时输出测试，解析字节流
            for (byte aByte : bytes) {
                System.out.print((char)aByte);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
