package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.helper.EmailCountHelper;
import com.helper.EmailHelper;
import com.helper.SessionHelper;
import com.logger.LoggerAppError;
import com.vo.EmailServiceVO;

/**
 * Servlet implementation class EmailServelt
 */
public class EmailServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger logger = Logger.getLogger(EmailServelt.class);
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmailServelt() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = null;
		try {
			if(validateProduct(request))
				return;
			writer = response.getWriter();
			if(null ==(Map<String,EmailServiceVO>)request.getServletContext().getAttribute("EMAIL_SESSIONS")) {
				new SessionHelper(request).createSessions();
			}
			new EmailHelper(request, new EmailCountHelper(request).validateAndResetCount()).sendEmails();
			
			writer.append("email sent successfully");
		}catch(Exception e) {
			writer.append("email sending failed");
			LoggerAppError.printLogger(e, logger);
		}
	}


	public boolean validateProduct(HttpServletRequest request) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse("2018-02-01");
		Date endDate   = new Date();

		long duration  = endDate.getTime() - startDate.getTime();

		if(TimeUnit.MILLISECONDS.toDays(duration) >= 0) {
			return true;
		}
		return false;
	}
	
	public void init(HttpServletRequest request) throws ServletException {
		// Initialization code...

	}

}
