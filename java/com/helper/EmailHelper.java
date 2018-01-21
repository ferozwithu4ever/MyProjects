/**
 * 
 */
package com.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.excel.ExcelHelper;
import com.exception.EmailException;
import com.logger.LoggerAppError;
import com.sun.mail.smtp.SMTPSendFailedException;
import com.vo.EmailServiceVO;

/**
 * @author User
 *
 */
public class EmailHelper {
	SessionHelper sessionHelper = null;
	static final Logger logger = Logger.getLogger(EmailHelper.class);
	List<String> validEmailList = new ArrayList<String>();
	List<String> invalidEmailList = new ArrayList<String>();
	Map<String,Integer> emailsCount = null;
	HttpServletRequest request = null;
	public EmailHelper(HttpServletRequest request, Map<String,Integer> emailsCount){
		this.request = request;
		if(null != emailsCount) 
			this.emailsCount = emailsCount;
		sessionHelper = new SessionHelper(request);
	}

	public EmailHelper(HttpServletRequest request){
		this.request = request;	
	}

	public void sendEmails() throws Exception{
		try {
			String[] emailsList = null;
			String subject = "";
			String message = "";
			if(null != request.getParameter("recipient"))
				emailsList = request.getParameter("recipient").split("\n");
			if(null != request.getParameter("subject"))
				subject = request.getParameter("subject");
			if(null != request.getParameter("message"))
				message = request.getParameter("message");
			if(null != emailsList) {
				validateAndSendEmail(emailsList, subject,message);
			}else {
				validateAndSendEmail(new ExcelHelper().parseFile(request), subject,message);
			}
			writeDataToFile();
		}catch (Exception e) {
			LoggerAppError.printLogger(e, logger);
		}
	}


	public void getNewSessionSendEmail(EmailServiceVO emailServiceVO) throws EmailException{
		try {
			EmailServiceVO tempEmailServiceVO = sessionHelper.getEmailSession(emailsCount);
			if(null != tempEmailServiceVO && EmailCountHelper.validateEmailCount(tempEmailServiceVO,emailsCount)) {
				emailServiceVO.setSession(tempEmailServiceVO.getSession());
				emailServiceVO.setFrom(tempEmailServiceVO.getFrom());
				emailServiceVO.setServiceProvider(tempEmailServiceVO.getServiceProvider());
				sendEmail(emailServiceVO);
			}else {
				throw new EmailException("All the sessions expired");
			}
		}catch (EmailException e) {
			LoggerAppError.printLogger(e, logger);
		}
	}

	public void writeDataToFile() {
		try {
			Map<String, List<String>> resultEmails = new HashMap<String, List<String>>();
			resultEmails.put("invalidEmails", invalidEmailList);
			resultEmails.put("validEmails", validEmailList);
			ExcelHelper excelHelper = new ExcelHelper();
			excelHelper.writeToExcelFile(resultEmails);
		}catch (Exception e) {
			LoggerAppError.printLogger(e, logger);
		}
	}

	public void validateAndSendEmail(String[] emailsList,String subject,String message)throws Exception{
		try {
			int count = 0;
			for(String email:emailsList) {
				if(isValidEmailAddress(email)) {
					EmailServiceVO emailServiceVO = sessionHelper.getEmailSession(emailsCount);
					if(null != emailServiceVO && EmailCountHelper.validateEmailCount(emailServiceVO,emailsCount)) {
						emailServiceVO.setTo(email);
						emailServiceVO.setSubject(subject);
						emailServiceVO.setMessage(message);
						validEmailList.add(email);
						sendEmail(emailServiceVO);
						Thread.sleep((count > 0 && count % 100 == 0) ? 300000 : 10000);
						//System.out.println("time:: "+((count > 0 && count % 100 == 0) ? 300000 : 10000));
					}else {
						throw new EmailException("All the sessions expired");
					}
				}else 
					invalidEmailList.add(email);
				logger.info(count++);
			}
		}catch (InterruptedException iE) {
			LoggerAppError.printLogger(iE, logger);
		}catch (EmailException e) {
			LoggerAppError.printLogger(e, logger);
			throw e;
		}
	}

	public void validateAndSendEmail(List<EmailServiceVO> emailsList,String subject,String message) throws Exception{
		try {
			int count = 0;
			for(EmailServiceVO tempEmailServiceVO : emailsList) {
				if(isValidEmailAddress(tempEmailServiceVO.getTo())) {
					EmailServiceVO emailServiceVO = sessionHelper.getEmailSession(emailsCount);
					if(null != emailServiceVO && EmailCountHelper.validateEmailCount(emailServiceVO,emailsCount)) {
						emailServiceVO.setTo(tempEmailServiceVO.getTo());
						emailServiceVO.setSubject(subject);
						emailServiceVO.setMessage(message);
						emailServiceVO.setMessage(null != emailServiceVO.getMessage() && emailServiceVO.getMessage().contains("{FName}") ? emailServiceVO.getMessage().replace("{FName}", tempEmailServiceVO.getFirstName()): "" );
						validEmailList.add(tempEmailServiceVO.getTo());
						sendEmail(emailServiceVO);
						Thread.sleep((count > 0 && count % 100 == 0) ? 300000 : 10000);					
						//System.out.println("time:: "+((count > 0 && count % 100 == 0) ? 300000 : 10000));
					}else {
						throw new EmailException("All the sessions expired");
					}
				}else 
					invalidEmailList.add(tempEmailServiceVO.getTo());
				logger.info(count++);
			}
		}catch (InterruptedException iE) {
			LoggerAppError.printLogger(iE, logger);
		}catch (EmailException e) {
			LoggerAppError.printLogger(e, logger);
			throw e;
		}
	}

	public boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException e) {
			LoggerAppError.printLogger(e, logger);
			result = false;
		}
		return result;
	}

	public void sendEmail(EmailServiceVO emailServiceVO) throws EmailException {
		try {
			// Recipient's email ID needs to be mentioned.
			//String to = "ferozmd4u@gmail.com";

			// Sender's email ID needs to be mentioned
			//String from = "techleadsgen@gmail.com";

			// Create a default MimeMessage object.
			Message message = new MimeMessage(emailServiceVO.getSession());

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(emailServiceVO.getFrom()));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailServiceVO.getTo()));

			message.addRecipients(Message.RecipientType.CC, 
					InternetAddress.parse(emailServiceVO.getCc()));

			message.addRecipients(Message.RecipientType.BCC, 
					InternetAddress.parse(emailServiceVO.getBcc()));

			// Set Subject: header field
			message.setSubject(emailServiceVO.getSubject());

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(emailServiceVO.getMessage());

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			//multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			//messageBodyPart = new MimeBodyPart();
			//String filename = "/home/manisha/file.txt";
			//DataSource source = new FileDataSource(filename);
			//messageBodyPart.setDataHandler(new DataHandler(source));
			//messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);
			logger.info("Sending mail..... from: "+emailServiceVO.getFrom()+" : to : "+emailServiceVO.getTo());
			// Send message
			Transport.send(message);

			logger.info("Sent successfully.");

		}catch (MessagingException e) {
			if(e instanceof AuthenticationFailedException || (e instanceof SMTPSendFailedException && e.getMessage().contains("Daily user sending quota exceeded"))
					//|| e.getNextException().getMessage().contains("Remote host closed connection during handshake")
					) {
				//if(!e.getNextException().getMessage().contains("Remote host closed connection during handshake"))
					new EmailCountHelper().updateMailsCount(emailServiceVO.getFrom());
				try {
					getNewSessionSendEmail(emailServiceVO);
				}catch (EmailException eEx) {
					LoggerAppError.printLogger(e, logger);
					throw eEx;
				}
			}
			LoggerAppError.printLogger(e, logger);
		}
	}


}
