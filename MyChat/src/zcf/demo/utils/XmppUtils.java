package zcf.demo.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.util.Log;
import zcf.demo.bean.User;
import zcf.demo.service.ImChatService;

public class XmppUtils {
	
	/** 
     * ��ѯ�û� 
     *  
     * @param userName 
     * @return 
     * @throws XMPPException 
     */  
    public static List<User> searchUsers(XMPPConnection mXMPPConnection,String userName) {  
    	List<User> listUser=new ArrayList<User>();
        try{
			UserSearchManager search = new UserSearchManager(mXMPPConnection);
			//�˴�һ��Ҫ���� search.
			Form searchForm = search.getSearchForm("search."+mXMPPConnection.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			ReportedData data = search.getSearchResults(answerForm,"search."+mXMPPConnection.getServiceName());	
			Iterator<Row> it = data.getRows();
			Row row=null;
			while(it.hasNext()){
				row=it.next();
				User user=new User();
				user.setCount(row.getValues("Username").next().toString());
				listUser.add(user);
			}
		}catch(Exception e){
			
		}
        return listUser;  
    }  
    
    /**
 	 * ���һ������  �޷���
 	 */
 	public static boolean addUser(Roster roster,String userName,String name)
 	{
 		try {
 			roster.createEntry(userName, name, null);
 			return true;
 		} catch (Exception e) {
 			e.printStackTrace();
 			return false;
 		}
 		
 	}
 	  /**
 		 * ������Ϣ
 		 * @param position
 		 * @param content
 		 * @param touser
 		 * @throws XMPPException
 		 */
 		public static void sendMessage(XMPPConnection mXMPPConnection,String content,String touser) throws XMPPException {
 			if(mXMPPConnection==null||!mXMPPConnection.isConnected()){
 				throw new XMPPException();
 			}
 			ChatManager chatmanager = mXMPPConnection.getChatManager();
 			Chat chat =chatmanager.createChat(touser + "@" + ImChatService.HOST, null);
 			if (chat != null) {
 				chat.sendMessage(content);
 				Log.e("jj", "���ͳɹ�");
 			}
 		}

 		/**
 		 * ɾ��һ������
 		 * @param roster
 		 * @param userJid
 		 * @return
 		 */
 		public static boolean removeUser(Roster roster,String userJid)
 		{
 			try {
 				RosterEntry entry = roster.getEntry(userJid);
 				roster.removeEntry(entry);
 				return true;
 			} catch (Exception e) {
 				e.printStackTrace();
 				return false;
 			}		
 		}
 		
 		/*
 		 * ͬ����Ӻ���
 		 */
 		public static void agreeAdd(String from)
 		{
 			//ͬ�����
 			Presence subscription=new Presence(Presence.Type.subscribed);
 			subscription.setTo(from);
 			ImChatService.getInstance().sendPacket(subscription);
 		}
 		public static void sendAdd(String from)
 		{
 			//ͬ��֮����ӶԷ�Ϊ����
 			Presence reSubscription=new Presence(Presence.Type.subscribe);
 			reSubscription.setTo(from);
 			ImChatService.getInstance().sendPacket(reSubscription);
 		}
}
