package com.mail.notifier.service;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.mail.notifier.domain.ConnectionDetails;

public interface NotificationService {
	public void notifyEmail(ConnectionDetails details) throws NoSuchProviderException, MessagingException;
}
