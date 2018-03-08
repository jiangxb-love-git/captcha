package com.koradji.start.captcha;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaptchaServiceTests {

	@Autowired
	private CaptchaService captchaService;

	@Test
	public void testGenerateCaptcha() throws Exception {
		String captchaKey = captchaService.generateCaptchaKey();
		assertNotNull(captchaKey);

		byte[] captchaImage = captchaService.generateCaptchaImage(captchaKey);
		assertTrue(captchaImage.length > 0);

		File image = new File("target/" + captchaKey + ".jpg");
		OutputStream output = null;
		try {
			output = new FileOutputStream(image);
			output.write(captchaImage);
		} finally {
			if (output != null) {
				output.close();
			}
		}
		assertTrue(image.exists() && image.length() > 0);
	}

	@Test
	public void testValidateCaptchaCorrect() throws Exception {
		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("12345");
		preDefinedTexts.add("abcde");
		captchaService.setPreDefinedTexts(preDefinedTexts);

		String captchaKey = captchaService.generateCaptchaKey();
		captchaService.generateCaptchaImage(captchaKey);
		assertTrue(captchaService.validateCaptcha(captchaKey, "12345"));

		captchaKey = captchaService.generateCaptchaKey();
		captchaService.generateCaptchaImage(captchaKey);
		assertTrue(captchaService.validateCaptcha(captchaKey, "abcde"));
	}

	@Test
	public void testValidateCaptchaIncorrect() throws Exception {
		List<String> preDefinedTexts = new ArrayList<String>();
		preDefinedTexts.add("12345");
		captchaService.setPreDefinedTexts(preDefinedTexts);

		String captchaKey = captchaService.generateCaptchaKey();
		captchaService.generateCaptchaImage(captchaKey);
		assertFalse(captchaService.validateCaptcha(captchaKey, "67890"));
	}
}
