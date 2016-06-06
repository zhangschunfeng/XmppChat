package zcf.demo.service;

import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;

import android.os.AsyncTask;
import zcf.demo.bean.User;

public class GetInfoService {

	
	public static String getUserNikeName(String account)
	{

		XMPPConnection conn=ImChatService.getInstance();
		Roster roster=conn.getRoster();
		Collection<RosterEntry> rosterentrys = roster.getEntries();
		for(RosterEntry entry:rosterentrys)
		{

			if(account.equalsIgnoreCase(entry.getUser())||entry.getUser().contains(account))
			{
				return entry.getName();
				
			}
	
		}
		return null;
	}
	
}
