package com.mail.notifier.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mail.notifier.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Value("${sms.toNumber}")
	private String toNumber;

	@Value("${sms.apiKey}")
	private String apiKey;

	@Value("${mail.senderList}")
	private List<String> senderList;

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private int port;

	@Value("${mail.protocol}")
	private String protocol;

	@Value("${mail.username}")
	private String username;

	@Value("${mail.password}")
	private String password;

	@Override
	@Scheduled(cron = "0 0/60 * * * *") // cron expression to trigger for every 60 minutes.
	public void notifyEmail() {
		HttpURLConnection connection = null;
		Store store = null;
		Folder folder = null;
		try {
			System.out.println("Triggered email notifier at : " + String.valueOf(LocalDateTime.now()));
			Properties properties = new Properties();

			properties.put("mail.imap.host", host);
			properties.put("mail.imap.port", port);
			properties.put("mail.imap.starttls.enable", "true");
			Session session = Session.getDefaultInstance(properties);

			store = session.getStore(protocol);
			store.connect(host, username, password);

			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);

			Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

			StringBuilder smsMessage = new StringBuilder();

			smsMessage.append("Unread mails from your priority list are").append(System.lineSeparator());
			int initialLength = smsMessage.length();

			for (int i = 0; i < messages.length; i++) {
				int start = messages[i].getFrom()[0].toString().indexOf('<');
				int end = messages[i].getFrom()[0].toString().indexOf('>');
				String mailId = messages[i].getFrom()[0].toString().substring(start + 1, end);
				for (String sender : senderList) {
					if (mailId.contains(sender)) {
						smsMessage.append("From : ").append(mailId).append(System.lineSeparator()).append("Subject : ")
								.append(messages[i].getSubject()).append(System.lineSeparator())
								.append(System.lineSeparator());
					}
				}

			}
			if (smsMessage.length() == initialLength) {
				smsMessage.delete(0, initialLength - 1);
				smsMessage.append("No unread messages from your priority list!");
			}

			/** code to send SMS using clickatell SMS API */

			String requestUrl = "https://platform.clickatell.com/messages/http/send?apiKey="
					+ URLEncoder.encode(apiKey, "UTF-8") + "&to=" + URLEncoder.encode(toNumber, "UTF-8") + "&content="
					+ URLEncoder.encode(smsMessage.toString(), "UTF-8");
			URL url = new URL(requestUrl);

			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			if(connection.getResponseMessage().equalsIgnoreCase("ACCEPTED")) {
				System.out.println("SMS sent to you registered mobile number");
			}else {
				System.out.println("Encountered some problem :(");
			}
			folder.close();
			store.close();
			connection.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			connection.disconnect();
		}
	}

}
