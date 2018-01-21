/**
 * 
 */
package com.standalone;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.concurrent.TimeUnit;

/**
 * @author User
 *
 */
public class TestClass {


	public static void main(String[] args) throws ParseException{
		try {

			/*SendEmail();*/

			/*		String host = "pop.gmail.com";// change accordingly
		String mailStoreType = "pop3";
		String username = "techleadsgen@gmail.com";// change accordingly
		String password = "techleadsgen@123";// change accordingly

		check(host, mailStoreType, username, password);*/

			//validateDates();
			/*checkTimeOut();*/
			testBounce();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void check(String host, String storeType, String user,
			String password) 
	{
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		try {

			//create properties field
			Properties properties = new Properties();

			properties.put("mail.pop3.host", host);
			properties.put("mail.pop3.port", "995");
			properties.put("mail.pop3.starttls.enable", "true");
			properties.put("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			//Set manual Properties
			properties.setProperty( "mail.pop3.socketFactory.class", SSL_FACTORY);
			properties.setProperty( "mail.pop3.socketFactory.fallback", "false");
			properties.setProperty( "mail.pop3.socketFactory.port", "995");
			Session emailSession = Session.getDefaultInstance(properties);

			//create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");

			store.connect(host, user, password);

			//create the folder object and open it
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			// retrieve the messages from the folder in an array and print it
			//Message[] messages = emailFolder.getMessages(emailFolder.getMessages().length-10, emailFolder.getMessages().length);

			System.out.println("getUnreadMessageCount:: "+emailFolder.getUnreadMessageCount());
			System.out.println("getNewMessageCount:: "+emailFolder.getNewMessageCount());
			System.out.println("getDeletedMessageCount:: "+emailFolder.getDeletedMessageCount());
			System.out.println("getFullName:: "+emailFolder.getFullName());
			System.out.println("getMessageCount:: "+emailFolder.getMessageCount());
			System.out.println("getMode:: "+emailFolder.getMode());
			System.out.println("getName:: "+emailFolder.getName());
			System.out.println("getSeparator:: "+emailFolder.getSeparator());
			System.out.println("getType:: "+emailFolder.getType());
			System.out.println("getParent:: "+emailFolder.getParent());
			System.out.println("getStore:: "+emailFolder.getStore());
			System.out.println("getURLName:: "+emailFolder.getURLName());
			Message[] messages = emailFolder.getMessages();
			System.out.println("messages.length---" + messages.length);

			for (int i = 1, n = messages.length; i < n; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());

			}

			//close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void checkTimeOut() throws Exception{
		try {
			for(int i=0;i<10;i++) {
				//sleep 5 seconds
				Thread.sleep(5000);
				System.out.println("Testing..." + new Date());

			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean validateDates() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/*Date date1 = sdf.parse("2018-01-03");
		Date date2 = new Date();//sdf.parse("2010-01-31");

		System.out.println("date1 : " + sdf.format(date1));
		System.out.println("date2 : " + sdf.format(date2));*/
		String storeDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Date startDate = sdf.parse("2018-02-01");
		Date endDate   = sdf.parse(storeDate);
		System.out.println(""+sdf.format(endDate));
		long duration  = endDate.getTime() - startDate.getTime();

		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
		System.out.println("diffInSeconds: "+diffInSeconds+" diffInMinutes:: "+diffInMinutes+" diffInHours:: "+diffInHours+" diffInDays: "+diffInDays);
		/*Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        if (cal1.after(cal2)) {
            System.out.println("Date1 is after Date2");
        }

        if (cal1.before(cal2)) {
            System.out.println("Date1 is before Date2");
        }

        if (cal1.equals(cal2)) {
            System.out.println("Date1 is equal Date2");
        }*/
		return true;
	}

	public static void SendEmail() {

		// Recipient's email ID needs to be mentioned.
		String to = "ferozmd4u@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "techleadsgen@gmail.com";

		final String username = "techleadsgen@gmail.com";//change accordingly
		final String password = "techleadsgen@123";//change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));

			message.addRecipients(Message.RecipientType.CC, 
					InternetAddress.parse("mohammedfyz2014@gmail.com"));

			message.addRecipients(Message.RecipientType.BCC, 
					InternetAddress.parse("mohammedfyz2014@gmail.com"));

			// Set Subject: header field
			message.setSubject("Testing Subject");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText("This is message body");

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
			System.out.println("mail sending");
			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * Bounce back email...
	 */
	public static void testBounce() throws Exception {
	      String smtpServer = "smtp.gmail.com";
	      int port = 587;
	      final String userid = "jangaon001@gmail.com";//change accordingly
	      final String password = "ghouse786";//change accordingly
	      String contentType = "text/html";
	      String subject = "test: bounce an email to a different address " +
					"from the sender";
	      String from = "jangaon001@gmail.com";
	      String to = "mkjhyg@meekmind.com";//some invalid address
	      String bounceAddr = "hades.hagen2@gmail.com";//change accordingly
	      String body = "Test: get message to bounce to a separate email address";

	      Properties props = new Properties();

	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", smtpServer);
	      props.put("mail.smtp.port", "587");
	      props.put("mail.transport.protocol", "smtp");
	      props.put("mail.smtp.from", bounceAddr);

	      Session mailSession = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(userid, password);
	            }
	         });

	      MimeMessage message = new MimeMessage(mailSession);
	      message.addFrom(InternetAddress.parse(from));
	      message.setRecipients(Message.RecipientType.TO, to);
	      message.setSubject(subject);
	      message.setContent(body, contentType);

	      Transport transport = mailSession.getTransport();
	      try {
	         System.out.println("Sending ....");
	         transport.connect(smtpServer, port, userid, password);
	         transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	         System.out.println("Sending done ...");
	      } catch (Exception e) {
	         System.err.println("Error Sending: ");
	         e.printStackTrace();

	      }
	      transport.close();
	   }// end function main()

}
