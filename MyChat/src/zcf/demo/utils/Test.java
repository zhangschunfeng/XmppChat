package zcf.demo.utils;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Test {
	public static final String HOST = "127.0.0.1";    // ����ip
	public static final int PORT =9090; 
	public static void main(String[] args) {
		 	
			try {
				ConnectionConfiguration config = new ConnectionConfiguration(HOST, PORT);
			    // ���������(�������ǿ���,���ߵ�ʱ��,���ԸĻ���)
			    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);// ���Ĵ���
			    config.setDebuggerEnabled(true);// ��������ģʽ,�������ǲ鿴���巢�͵�����
			    // 2.��ʼ�������Ӷ���
			    XMPPConnection conn = new XMPPConnection(config);
			    // ��ʼ����
				conn.connect();
				conn.login("user","user");
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // �Ѿ��ɹ��ɹ�

	}

}
