package com.mela.com.mela.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统socket
 */
public class TraditionalSocketDemo {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);

        System.out.println("服务端启动...");

        while (true) {
            // 获取socket客户端套接字,【这里会阻塞，等待客户端连接】
            final Socket socket = serverSocket.accept();
            System.out.println("有客户端连接上来了...");

            // 获取输入流
            InputStream inputStream = socket.getInputStream();

            byte[] bytes = new byte[1024];
            while (true) {
                int data = inputStream.read(bytes);
                if (data != -1) {
                    String info = new String(bytes, 0, data);

                    System.out.println("接收到客户端消息：" + info);
                }
            }

        }
    }

}
