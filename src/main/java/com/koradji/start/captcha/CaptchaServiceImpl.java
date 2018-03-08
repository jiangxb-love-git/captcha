package com.koradji.start.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.koradji.start.captcha.exception.CaptchaException;
import com.koradji.start.captcha.util.RandomGenerator;

@Component
public class CaptchaServiceImpl implements CaptchaService, InitializingBean {

	private DefaultKaptcha producer;

	private Map<String, String> captchaMap = new HashMap<String, String>();

	private List<String> preDefinedTexts;

	private int textCount = 0;

	@Override
	public void afterPropertiesSet() throws Exception {
		producer = new DefaultKaptcha();

		producer.setConfig(new Config(new Properties()));
	}

	@Override
	public String generateCaptchaKey() throws CaptchaException {
		String key = RandomGenerator.getRandomString();
		String value = getCaptchaText();

		captchaMap.put(key, value);

		return key;
	}

	private String getCaptchaText() {
		if (preDefinedTexts != null && !preDefinedTexts.isEmpty()) {
			String text = preDefinedTexts.get(textCount);
			textCount = (textCount + 1) % preDefinedTexts.size();

			return text;
		} else {
			return producer.createText();
		}
	}

	@Override
	public byte[] generateCaptchaImage(String captchaKey) throws CaptchaException {
		String text = captchaMap.get(captchaKey);

		if (text == null) {
			throw new CaptchaException("Captcha key '" + captchaKey + "' not found!");
		}

		BufferedImage image = producer.createImage(text);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "jpg", out);
		} catch (IOException e) {
			throw new CaptchaException("Failed to write captcha stream!", e);
		}

		return out.toByteArray();
	}

	@Override
	public boolean validateCaptcha(String captchaKey, String captchaValue) throws CaptchaException {
		String text = captchaMap.get(captchaKey);

		if (text == null) {
			throw new CaptchaException("Captcha key '" + captchaKey + "' not found!");
		}

		if (text.equals(captchaValue)) {
			captchaMap.remove(captchaKey);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> getPreDefinedTexts() {
		return preDefinedTexts;
	}

	@Override
	public void setPreDefinedTexts(List<String> preDefinedTexts) {
		this.preDefinedTexts = preDefinedTexts;
	}

}
