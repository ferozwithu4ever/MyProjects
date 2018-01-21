package com.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.logger.LoggerAppError;
import com.vo.EmailAuthenticationVO;
import com.vo.EmailServiceVO;

public class EmailAuthenticator {
	static final Logger logger = Logger.getLogger(EmailAuthenticator.class);
	public void emailAuthenticator(List<EmailAuthenticationVO> emailList, Properties properties,HttpServletRequest request) {
		// Get the Session object.
		try {
		Map<String,EmailServiceVO> sessionsMap = new HashMap<String,EmailServiceVO>(); 
		Map<String,Integer> countsMap = new HashMap<String,Integer>();
		for(EmailAuthenticationVO emailVO : emailList) {
			final String username = emailVO.getEmail();//change accordingly
			final String password = emailVO.getPassword();//change accordingly
			Session session = Session.getInstance(properties,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			EmailServiceVO emailServiceVO = new EmailServiceVO();
			emailServiceVO.setSession(session);
			emailServiceVO.setServiceProvider(emailVO.getServiceProvider());
			emailServiceVO.setFrom(username);
			sessionsMap.put(username, emailServiceVO);
			countsMap.put(username, 0);
		}
		request.getServletContext().setAttribute("EMAIL_COUNTS", countsMap);
		request.getServletContext().setAttribute("EMAIL_SESSIONS", sessionsMap);
		request.getServletContext().setAttribute("SESSIONS_DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}catch (Exception e) {
			LoggerAppError.printLogger(e, logger);
		}
	}
}
