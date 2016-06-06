package zcf.demo.utils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Test {
	public static final String HOST = "127.0.0.1";    // 主机ip
	public static final int PORT =9090; 
	public static void main(String[] args) {
		 	
			try {
				ConnectionConfiguration config = new ConnectionConfiguration(HOST, PORT);
			    // 额外的配置(方面我们开发,上线的时候,可以改回来)
			    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);// 明文传输
			    config.setDebuggerEnabled(true);// 开启调试模式,方便我们查看具体发送的内容
			    // 2.开始创建连接对象
			    XMPPConnection conn = new XMPPConnection(config);
			    // 开始连接
				conn.connect();
				conn.login("user","user");
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // 已经成功成功

	}

}
