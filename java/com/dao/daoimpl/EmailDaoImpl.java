package com.dao.daoimpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vo.EmailAuthenticationVO;
import com.vo.SmtpVO;

public class EmailDaoImpl{

	public Map<String, List<EmailAuthenticationVO>> getEmailSettings(String serviceProvider) {
		Map<String, List<EmailAuthenticationVO>> emailSettings = null;
		try {		  
			emailSettings = new HashMap<String, List<EmailAuthenticationVO>>();
			List<EmailAuthenticationVO> emailList = new LinkedList<EmailAuthenticationVO>();
			EmailAuthenticationVO emailVO = new EmailAuthenticationVO();
			
			emailVO.setEmail("email");
			emailVO.setPassword("pwd");
			emailVO.setServiceProvider("gmail");
			emailList.add(emailVO);

			emailSettings.put("gmail", emailList);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return emailSettings;

	}

	public static void main(String args[]) {
		new EmailDaoImpl().getSmtpSettings();
		new EmailDaoImpl().getEmailSettings("gmail");

	}


	public void saveEmailData() {

	}

	public Map<String,SmtpVO> getSmtpSettings() {
		Map<String,SmtpVO> smtpSettings = null;
		try {
			smtpSettings = new HashMap<String,SmtpVO>();
			SmtpVO smtpVO = new SmtpVO();
			smtpVO.setAuth("mail.smtp.auth");
			smtpVO.setAuthValue(true);
			smtpVO.setHost("mail.smtp.host");
			smtpVO.setHostValue("smtp.gmail.com");
			smtpVO.setPort("mail.smtp.port");
			smtpVO.setPortValue("587");
			smtpVO.setServiceProvider("gmail");
			smtpVO.setStartTls("mail.smtp.starttls.enable");
			smtpVO.setStartTlsValue(true);
			smtpSettings.put("gmail", smtpVO);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return smtpSettings;
	}

}
