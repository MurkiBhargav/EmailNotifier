package com.mail.notifier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mail.notifier.domain.ConnectionDetails;
import com.mail.notifier.domain.ControllerResponse;
import com.mail.notifier.service.NotificationService;
import com.mail.notifier.utils.ControllerUtils;

@RestController
@RequestMapping(value = "/notify")
public class GmailController {

	@Autowired
	private NotificationService notificationService;
	
	@PostMapping(value = "/email")
	public ControllerResponse triggerNotifyEmail(@RequestBody ConnectionDetails details) {
		try{
			notificationService.notifyEmail(details);
			return  ControllerUtils.returnResponse(HttpStatus.OK, "Fetched mails from inbox");
		}catch(Exception e) {
			return  ControllerUtils.returnResponse(HttpStatus.OK, e.getMessage());
		}
	}
}
