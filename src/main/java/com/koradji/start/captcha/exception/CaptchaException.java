package com.koradji.start.captcha.exception;

import java.io.IOException;

public class CaptchaException extends Exception {

	private static final long serialVersionUID = 1L;

	public CaptchaException(String message) {
		super(message);
	}

	public CaptchaException(String message, IOException e) {
		super(message, e);
	}

}
