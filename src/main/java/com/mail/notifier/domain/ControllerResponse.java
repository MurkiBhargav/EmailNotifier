package com.mail.notifier.domain;

import org.springframework.http.HttpStatus;

public class ControllerResponse {

	private HttpStatus responseStatus;
	private String responseMessage;
	
	public ControllerResponse() {};
	
	public ControllerResponse(HttpStatus responseStatus, String responseMessage) {
		this.responseStatus = responseStatus;
		this.responseMessage = responseMessage;
	}
	
	public HttpStatus getResponseStatus() {
		return responseStatus;
	}
	
	public void setResponseStatus(HttpStatus responseStatus) {
		this.responseStatus = responseStatus;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	@Override
	public String toString() {
		return "ControllerResponse [responseStatus=" + responseStatus + ", responseMessage=" + responseMessage + "]";
	}
}
