package com.bstek.bdf2.core.message;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.core.business.IUser;

/**
 * @author Jacky.gao
 * @since 2013-3-28
 */
public class EmailSender{
	public static final String BEAN_ID="bdf2.emailSender";
	private Session session;
	private String smtpHost;
	private String smtpPort;
	private String smtpUser;
	private String smtpPassword;
	private boolean smtpIsAuth;
	private String defaultSenderEmailAddress;
	
	public void sendMail(MessagePacket message,Collection<File> files) throws Exception {
		Session session = this.getMailSession();
		MimeMessage mimeMsg = new MimeMessage(session);
		if(message.getTitle() != null){
			mimeMsg.setSubject(MimeUtility.encodeText(message.getTitle(),"GBK","B"));
		}
		if (message.getSender()!=null && StringUtils.isNotEmpty(message.getSender().getEmail())) {
			mimeMsg.setFrom(new InternetAddress(message.getSender().getEmail()));
		} else {
			mimeMsg.setFrom(new InternetAddress(defaultSenderEmailAddress));
		}

		if (message.getTo()!=null && message.getTo().size()>0) {
			List<InternetAddress> addressList = new ArrayList<InternetAddress>();
			for (IUser toUser:message.getTo()) {
				if(StringUtils.isNotEmpty(toUser.getEmail())){
					addressList.add(new InternetAddress(toUser.getEmail()));					
				}
			}
			if(addressList.size()==0){
				throw new IllegalArgumentException("All mail [receivers] was not assign email information");
			}
			InternetAddress[] addressArray = new InternetAddress[addressList.size()];
			addressList.toArray(addressArray);
			mimeMsg.setRecipients(javax.mail.Message.RecipientType.TO,addressArray);
		} else {
			throw new IllegalArgumentException("Mail [receivers] not assigned.");
		}
		
		if (message.getCc()!=null && message.getCc().size()>0) {
			List<InternetAddress> addressList = new ArrayList<InternetAddress>();
			for (IUser ccUser:message.getCc()){
				if(StringUtils.isNotEmpty(ccUser.getEmail())){
					addressList.add(new InternetAddress(ccUser.getEmail()));					
				}
			}
			if(addressList.size()>0){
				InternetAddress[] addressArray = new InternetAddress[addressList.size()];
				addressList.toArray(addressArray);
				mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC,addressArray);				
			}
		}
	
		Multipart multipart = new MimeMultipart();
		if (message.getContent()!= null) {
			MimeBodyPart mimeBodyPartMessage = new MimeBodyPart();
			String charset ="text/html; charset=utf-8";
			mimeBodyPartMessage.setContent(message.getContent(), charset);
			multipart.addBodyPart(mimeBodyPartMessage);
		}
		
		if(files!=null && files.size()>0){
			for(File file:files){
				if(null==file || !file.exists() || file.isDirectory()){
					continue;
				}
				String filename=file.getName();
				String encodedFilename=MimeUtility.encodeText(filename);
				MimeBodyPart mimeBodyPartFile=new MimeBodyPart();
				DataSource dsFile=new FileDataSource(file);
				mimeBodyPartFile.setDataHandler(new DataHandler(dsFile));
				mimeBodyPartFile.setFileName(encodedFilename);
				multipart.addBodyPart(mimeBodyPartFile);
				
			}
		}
		mimeMsg.setContent(multipart);
		mimeMsg.setSentDate(new Date());
		Transport.send(mimeMsg);
	}
	
	private Session getMailSession(){
		if(session==null){
			Properties properties = new Properties();
			properties.put("mail.smtp.host",smtpHost);
			if(StringUtils.isNotEmpty(smtpPort)){
				properties.put("mail.smtp.port",Integer.parseInt(smtpPort));
			}
			if(smtpIsAuth) {
				properties.put("mail.smtp.auth","true");
				session = Session.getDefaultInstance(properties,
						new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(smtpUser,smtpPassword);
					}
				});
			} else {
				session = Session.getDefaultInstance(properties);
			}
		}
		return session;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public boolean isSmtpIsAuth() {
		return smtpIsAuth;
	}

	public void setSmtpIsAuth(boolean smtpIsAuth) {
		this.smtpIsAuth = smtpIsAuth;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	public String getDefaultSenderEmailAddress() {
		return defaultSenderEmailAddress;
	}

	public void setDefaultSenderEmailAddress(String defaultSenderEmailAddress) {
		this.defaultSenderEmailAddress = defaultSenderEmailAddress;
	}
}
