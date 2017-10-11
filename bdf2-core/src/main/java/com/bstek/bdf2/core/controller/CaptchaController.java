package com.bstek.bdf2.core.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;


/**
 * @author Jacky.gao
 * @since 2013-2-21
 */
public class CaptchaController implements IController {
	private int defaultWidth=200;
	private int defaultHeight=60;
	private String defaultSessionKey=Constants.KAPTCHA_SESSION_KEY;
	private String url;
	public CaptchaController(){}
	public CaptchaController(String url){
		this.url=url;
	}
	public String getUrl() {
		return url;
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		
		int width=defaultWidth;
		int height=defaultHeight;
		String key=defaultSessionKey;
		if(StringUtils.isNotEmpty(request.getParameter("width"))){
			width=Integer.valueOf(request.getParameter("width"));
		}
		if(StringUtils.isNotEmpty(request.getParameter("height"))){
			height=Integer.valueOf(request.getParameter("height"));
		}
		if(StringUtils.isNotEmpty(request.getParameter("key"))){
			key=request.getParameter("key");
		}
		Producer captchaProducer = this.getProducer(width, height);
		String capText = captchaProducer.createText();
		request.getSession().setAttribute(key,capText);
		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		try {
			// write the data out
			ImageIO.write(bi,"jpg", out);
			out.flush();
		} finally {
			out.close();
		}
	}

	public boolean anonymousAccess() {
		return true;
	}
	private Producer getProducer(int width, int height) {
		DefaultKaptcha kaptcha = new DefaultKaptcha();
		Properties prop = new Properties();
		prop.put(Constants.KAPTCHA_IMAGE_WIDTH,String.valueOf(width));
		prop.put(Constants.KAPTCHA_IMAGE_HEIGHT,String.valueOf(height));
		prop.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, 5);
		prop.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE,"6");
		prop.put(
				Constants.KAPTCHA_BACKGROUND_CLR_FROM,
				String.valueOf(RandomUtils.nextInt(255)) + ","
						+ RandomUtils.nextInt(255) + ","
						+ RandomUtils.nextInt(255));
		prop.put(
				Constants.KAPTCHA_BACKGROUND_CLR_TO,
				String.valueOf(RandomUtils.nextInt(255)) + ","
						+ RandomUtils.nextInt(255) + ","
						+ RandomUtils.nextInt(255));
		prop.put(
				Constants.KAPTCHA_NOISE_COLOR,
				String.valueOf(RandomUtils.nextInt(255)) + ","
						+ RandomUtils.nextInt(255) + ","
						+ RandomUtils.nextInt(255));
		prop.put(
				Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,
				String.valueOf(RandomUtils.nextInt(255)) + ","
						+ RandomUtils.nextInt(255) + ","
						+ RandomUtils.nextInt(255));
		prop.put(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, String.valueOf(height-14));
		prop.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING,
				"123456789abcdefghijkmnlpqrstuvwxyz");
		Config config = new Config(prop);
		kaptcha.setConfig(config);
		return kaptcha;
	}
	public boolean isDisabled() {
		return false;
	}
}
