package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import pub.CommonUse;

//		这是一个Java程序，代表了一个Socket通信系统的客户端。
//		该客户端使用CommonUse类中指定的IP地址和端口号建立与服务器的连接。
//		然后，它通过控制台读取用户的输入，并通过套接字连接将其发送到服务器。
//		该程序将继续读取输入并发送数据，直到用户输入“再见”。如果发生异常，则会打印出堆栈跟踪。

public class Client1 {

    public static void main(String[] args) {
        System.out.println("客户端启动");

        try {
            Socket socket = new Socket(CommonUse.SERVER_IP, CommonUse.SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String info = null;

            while (true) {
                info = reader.readLine();
                writer.write(info + "\n");
                writer.flush();
                if ("再见".equals(info)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
