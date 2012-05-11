package com.percussion.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;

import com.percussion.workflow.mail.IPSMailMessageContext;
import com.percussion.workflow.mail.IPSMailProgram;
import com.percussion.workflow.mail.PSMailException;

/**
 * Example workflow notification email class
 * 
 * You can use http://java.sun.com/developer/onlineTraining/JavaMail/contents.html
 * to set up email class within sendMessage
 * 
 * @author rileyw
 * @see com.percussion.workflow.mail.IPSMailProgram
 */
public class WorkflowJavaxMailProgram implements IPSMailProgram{

	public void init() throws PSMailException {}

	/**
	 * Prepare and send the notification email
	 * 
	 * Using the following classes:
	 * 	- Sending email: 		javax.mail.Transport
	 *  - Constructing email: 	javax.mail.internet.MimeMessage
	 *  - RecipientTypes:		javax.mail.internet.MimeMessage.RecipientType
	 *  - Mail session:			javax.mail.Session
	 *  - Properties			java.util.Properties
	 *  @see com.percussion.workflow.mail.IPSMailMessageContext
	 */
	public void sendMessage(IPSMailMessageContext arg0) throws PSMailException {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", arg0.getSmtpHost());
		Session session = Session.getDefaultInstance(properties,null);
		try {
			MimeMessage message = new MimeMessage(session);
			
			String mailDomain = arg0.getMailDomain();
			
			if((mailDomain == null) || (mailDomain.length() == 0)){
				throw new PSMailException(7447);
			}
			
			String CC = arg0.getCc();			
			String URL = arg0.getURL();
			
			if(StringUtils.isNotBlank(arg0.getFrom())){
				message.setFrom(makeAddresses(arg0.getFrom(),mailDomain)[0]);
			}
			message.setRecipients(Message.RecipientType.TO, arg0.getTo());
			if((CC != null) && (CC.length() > 0)){
				message.setRecipients(Message.RecipientType.CC, makeAddresses(CC,mailDomain));	
			}
			message.setSubject(arg0.getSubject());
			message.setSentDate(new Date());

			if((URL == null) || (URL.length() == 0)){
				message.setText(arg0.getBody());
			} else { 
				message.setText(arg0.getBody() + "\r\n\r\n" + URL);
			}
			Transport.send(message);
		} catch(AddressException e){
			throw new PSMailException(e);
		} catch(MessagingException e) {
			throw new PSMailException(e);
		}
	}

	private InternetAddress[] makeAddresses(String addresses, String mailDomain) throws AddressException {
		
		List<Object> addressArray = new ArrayList<Object>();
		
		StringTokenizer _addresses = new StringTokenizer(addresses,",");
		
		String m, m1 = null;
		
		if(!mailDomain.startsWith("@")){
			mailDomain = "@"+mailDomain;
		}
		
		/**
		 * Confirming email address format since Percussion CM may return users with and without a mail domain.
		 */
		while(_addresses.hasMoreElements()){
			m = _addresses.nextToken().trim();
			if(m.length() == 0){
				continue;
			}
			
			if(m.indexOf("@") == -1){
				m1 = m + mailDomain;
			} else {
				m1 = m;
			}
			Object address = new InternetAddress(m1);
			addressArray.add(address);
		}
		InternetAddress[] Address = new InternetAddress[addressArray.size()];
		for(int i = 0; i < addressArray.size(); i++){
			Address[i] = ((InternetAddress)addressArray.get(i));
		}
		
		return (InternetAddress[])Address;
		
	}

	public void terminate() throws PSMailException {}

}
