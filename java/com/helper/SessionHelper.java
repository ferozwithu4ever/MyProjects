package com.helper;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.dao.daoimpl.EmailDaoImpl;
import com.exception.EmailException;
import com.logger.LoggerAppError;
import com.vo.EmailAuthenticationVO;
import com.vo.EmailServiceVO;
import com.vo.SmtpVO;

public class SessionHelper {
	static final Logger logger = Logger.getLogger(SessionHelper.class);
	HttpServletRequest request = null;
	public SessionHelper(HttpServletRequest request) {
		this.request = request;
	}
	
	public void createSessions() {
		try {
			EmailDaoImpl emailDao = new EmailDaoImpl();
			Map<String,SmtpVO> smtpSettings = emailDao.getSmtpSettings();
			for(String serviceProvider : smtpSettings.keySet()) {
				Map<String,List<EmailAuthenticationVO>> emailSettings = emailDao.getEmailSettings(serviceProvider);
				Properties properties = prepareProperties(smtpSettings.get(serviceProvider)); 
				for(String key : emailSettings.keySet()) {
					new EmailAuthenticator().emailAuthenticator(emailSettings.get(key), properties,request);	
				}
			}
		}catch (Exception e) {
			LoggerAppError.printLogger(e, logger);
		}
	}
	
	private Properties prepareProperties(SmtpVO smtpVO) {
		Properties props = null;
		try {
			// Assuming you are sending email through relay.jangosmtp.net
			//String host = smtpVO.getHost();//"smtp.gmail.com";
			props = new Properties();
			props.put(smtpVO.getAuth(),smtpVO.isAuthValue());//"mail.smtp.auth", "true");
			props.put(smtpVO.getStartTls(),smtpVO.isStartTlsValue());//"mail.smtp.starttls.enable", "true");
			props.put(smtpVO.getHost(),smtpVO.getHostValue());//"mail.smtp.host", host);
			props.put(smtpVO.getPort(),smtpVO.getPortValue());//"mail.smtp.port", "587");
			props.put("mail.smtp.from", "techleadsgen@gmail.com");
		}catch (Exception e) {
			LoggerAppError.printLogger(e, logger);
		}
		return props;

	}
	
	
	public EmailServiceVO getEmailSession(Map<String,Integer> emailsCount) throws EmailException{
		EmailServiceVO emailServiceVO = null;
		try {
			Map<String,EmailServiceVO> emailsSessions = (Map<String,EmailServiceVO>)request.getServletContext().getAttribute("EMAIL_SESSIONS");
			if(null != emailsSessions) {
				for(String key : emailsSessions.keySet()) {
					if(EmailCountHelper.validateEmailCount(emailsSessions.get(key),emailsCount))
					{
						emailServiceVO = emailsSessions.get(key);
						break;
					}
				}
			}else {
				throw new EmailException("Unable to establish email session");
			}
		}catch (EmailException e) {
			LoggerAppError.printLogger(e, logger);
		}
		return emailServiceVO;
	}
	
}
