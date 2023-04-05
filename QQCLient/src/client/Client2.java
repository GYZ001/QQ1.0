package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import pub.CommonUse;

public class Client2 {
    public static void main(String[] args) {

		try {
			BufferedReader reader = null;

			Socket socket = new Socket(CommonUse.SERVER_IP, CommonUse.SERVER_PORT);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader sysreader = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String info = sysreader.readLine();
			String[] infos = info.split("/");

			writer.write(infos[0] + "\n");
			writer.flush();
			writer.write(infos[1] + "\n");
			writer.flush();


			if (CommonUse.SUCCESSFUL.equals(reader.readLine())) {
				System.out.println("登陆成功");
			} else {
				System.out.println("登陆失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
