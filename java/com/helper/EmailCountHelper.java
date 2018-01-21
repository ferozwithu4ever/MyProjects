package com.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.logger.LoggerAppError;
import com.vo.EmailServiceVO;

public class EmailCountHelper {
	static final Logger logger = Logger.getLogger(EmailCountHelper.class);
	static Map<String,Integer> emailsCount = null;
	HttpServletRequest request = null;
	public EmailCountHelper(HttpServletRequest request) {
		this.request = request;
	}

	public EmailCountHelper() {
	}
	
	public Map<String,Integer> validateAndResetCount() {
		try {
			if(null != request.getServletContext().getAttribute("EMAIL_COUNTS")) {
				emailsCount = (Map<String,Integer>)request.getServletContext().getAttribute("EMAIL_COUNTS");
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(null == request.getServletContext().getAttribute("SESSIONS_DATE"))
				return emailsCount;
			Date startDate = sdf.parse((String)request.getServletContext().getAttribute("SESSIONS_DATE"));
			Date endDate   = new Date();

			long duration  = endDate.getTime() - startDate.getTime();

			if(TimeUnit.MILLISECONDS.toDays(duration) >= 1) {
				for(String email : emailsCount.keySet()) {
					emailsCount.put(email, 0);
				}
			}
		}catch(Exception e) {
			LoggerAppError.printLogger(e, logger);
		}
		return emailsCount;
	}

	//Could not convert socket to TLS
	public static boolean validateEmailCount(EmailServiceVO emailServiceVO,Map<String,Integer> emailsCount) {
		try {
			if((emailServiceVO.getServiceProvider().equals("gmail") && emailsCount.get(emailServiceVO.getFrom())  < 498)) { 
				return true;
			}
		}catch (Exception e) {
			LoggerAppError.printLogger(e, logger);
		}
		return false;
	}

	public void incrementMailsCount(String from) {
		emailsCount.put(from,emailsCount.get(from)+1);
	}
	

	public void updateMailsCount(String from) {
		emailsCount.put(from,500);
	}
	
}
