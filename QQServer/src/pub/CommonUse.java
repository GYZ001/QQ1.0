package pub;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serializable;

/**
 * 
 * @author jzyqd_
 */
public class CommonUse implements Serializable{
    private static final long serialVersionUID = 23456789L;
	
	public static final String FLAG="FLAG";//标志
	
	public static final String REGISTER="REGISTER";//注册标志
	
	public static final String LOGIN="LOGIN";//登录标志
	
	public static final String ONLINE="ONLINE";//上线标志
	
	public static final String OFFLINE="OFFLINE";//下线标志
	
	public static final String FIND="FIND";//查询标志
	
	public static final String QQ_USER="QQ_USER";//查询标志
	
	public static final String SUCCESSFUL="SUCCESSFUL";//成功标志
	
	public static final String FAILURE="FAILURE";//失败标志
	
	public static final String FRIENDS_INFO="FRIENDS_INFO";//好友信息

	public static final String SERVER_IP = "127.0.0.1";//服务器IP

	public static final int SERVER_PORT = 9999;//服务器端口号
//
//    public static final String SERVER_IP = "106.15.76.240";//服务器IP
//
//    public static final int SERVER_PORT = 65535;//服务器端口号
	
	public static final String FIND_FRIEND="FIND_FRIEND";//查询标志
	
    public static final String UDP_PACKET_SYMBOL = "\n\r\n\r"; //分割符号
	
	public static final String MESSAGE = "MESSAGE";

    /**
     * 提示信息
     */
    public static void showMessage(JComponent j, String message, String title, int i) {
        JOptionPane.showMessageDialog(j, message, title, i);
    }

    /**
     * 获取系统�?��的路�?
     */
    public static String getSystempath() {
        File f = new File("");
        return f.getAbsolutePath();
    }

    /**
     * 获取系统日期时间
     */
    public static String getSystemTime() {
        //java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("HH:mm:ss");
        String time = f.format(new java.util.Date());
        return time;
    }
    
    /**
     * 设置窗体居中
     * @param frame 被设置的窗体
     * @param width 被设置窗体宽�?
     * @param high 被设置窗体高�?
     */
    public static void setComponentBounts(JFrame frame, int width, int high) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setLocation((screenWidth - width) / 2, (screenHeight - high) / 2);
    }
}
