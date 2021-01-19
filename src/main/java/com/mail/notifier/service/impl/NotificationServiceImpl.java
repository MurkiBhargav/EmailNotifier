package com.mail.notifier.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.springframework.stereotype.Service;

import com.mail.notifier.domain.ConnectionDetails;
import com.mail.notifier.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Override
	public void notifyEmail(ConnectionDetails details) throws NoSuchProviderException, MessagingException {
		
		Properties properties = new Properties();
		
		List<String> senderList = new ArrayList<>();
		
		senderList.add("murkibhargav1997@gmail.com");
		
		properties.put("mail.imap.host", details.getHost());
		properties.put("mail.imap.port", details.getPort());
		properties.put("mail.imap.starttls.enable", "true");
		Session session = Session.getDefaultInstance(properties);
		
		Store store = session.getStore("imaps");
		store.connect(details.getHost(), details.getUsername(), details.getPassword());
		
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		
//		int newMessageCount = folder.getMessageCount();
		
		Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN),false));
		
		for(int i=0;i<messages.length;i++) {
			int start = messages[i].getFrom()[0].toString().indexOf('<');
			int end = messages[i].getFrom()[0].toString().indexOf('>');
			String mailId = messages[i].getFrom()[0].toString().substring(start+1, end);
			if(senderList.contains(mailId)) {
				System.out.println("From : "+mailId);
				System.out.println("Subject :"+messages[i].getSubject());
			}
		}
		folder.close();
		store.close();
	}

}
