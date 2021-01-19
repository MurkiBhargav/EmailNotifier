package com.mail.notifier.utils;

import org.springframework.http.HttpStatus;

import com.mail.notifier.domain.ControllerResponse;

public class ControllerUtils {

	public static ControllerResponse returnResponse(HttpStatus responseStatus, String responseMessage) {
		
		ControllerResponse response = new ControllerResponse();
		response.setResponseStatus(responseStatus);
		response.setResponseMessage(responseMessage);
		
		return response;
	}
}
