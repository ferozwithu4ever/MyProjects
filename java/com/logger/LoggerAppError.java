package com.logger;

import org.apache.log4j.Logger;

public class LoggerAppError {
	
	public static void printLogger(Exception e, Logger logger) {
		
		logger.error("Exception is : "+e);
	    StackTraceElement[] s = e.getStackTrace();
	    for(StackTraceElement st : s){
	        logger.error("\tat " + st);
	    }
	}
	
}