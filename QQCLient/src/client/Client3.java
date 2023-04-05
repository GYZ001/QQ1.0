package client;

import bean.Qquser;
import pub.CommonUse;

import java.io.*;
import java.net.Socket;

public class Client3 {

	public static void main(String[] args) {
		String back = null;
		try {
			Socket socket = new Socket(CommonUse.SERVER_IP,CommonUse.SERVER_PORT);

			BufferedReader sysreader = new BufferedReader(new InputStreamReader(System.in));

			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			String info = sysreader.readLine();
			String[] infos = info.split("/");

			Qquser qquser = new Qquser();
			qquser.setAccount(infos[0]);
			qquser.setPassword(infos[1]);

			out.writeObject(qquser);
			out.flush();

			back = in.readObject().toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (CommonUse.SUCCESSFUL.equals(back)){
			System.out.println("登陆成功");
		}else {
			System.out.println("登陆失败");
		}
	}

}
