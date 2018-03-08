package com.koradji.start.captcha;

import java.util.List;

import com.koradji.start.captcha.exception.CaptchaException;

public interface CaptchaService {

	String generateCaptchaKey() throws CaptchaException;

	byte[] generateCaptchaImage(String captchaKey) throws CaptchaException;

	boolean validateCaptcha(String captchaKey, String captchaValue) throws CaptchaException;

	List<String> getPreDefinedTexts();

	void setPreDefinedTexts(List<String> preDefinedTexts);
}
