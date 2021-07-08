package tangerine.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import core.utility.Logger;
import tangerine.core.Constant;
import tangerine.enumeration.EmailSender;

public class EmailService {
	private boolean sendEmail(HtmlEmail email, EmailSender emailSender, String recipientEmail, String recipientName, String bccEmail, String bccName, String subject, String emailBody, String txtBody) {
		boolean success = false;
		if (email != null) {
			try {
				email.setSSLOnConnect(false);

				// email.setFrom(emailSender.getAddress(), emailSender.getDisplay());
				email.setFrom("tanyatutor@cerahceria.com", "Tanya Tutor");

				email.addTo(recipientEmail, recipientName);

				if (bccEmail != null && bccName != null) {
					email.addBcc(bccEmail, bccName);
				}

				email.setSubject(subject);
				email.setHtmlMsg(emailBody);
				if (txtBody != null) {
					email.setTextMsg(txtBody);
				}
				if (Constant.deploymentMode == Constant.DeploymentMode.PRODUCTION) {
					email.send();
				} else {
					System.out.println("--- email begin ---");
					System.out.println("From    : " + emailSender.getDisplay() + " <" + emailSender.getAddress() + ">");
					System.out.println("To      : " + recipientName + " <" + recipientEmail + ">");
					System.out.println("Subject : " + subject);
					System.out.println(emailBody);
					System.out.println(txtBody);
					System.out.println("--- email end ---");
				}
				success = true;
			} catch (EmailException ee) {
				Logger.getLogger().error(ee.toString());
				ee.printStackTrace();
			}
		}
		return success;
	}

	public boolean email(EmailSender emailSender, String recipientEmail, String recipientName, String bccEmail, String bccName, String subject, String emailBody, String txtBody) {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(Constant.mailHost);
		email.setSmtpPort(Constant.mailSmtpPort);
		// email.setAuthentication(emailSender.getAddress(), Constant.mailAuthenticationPassword);
		return sendEmail(email, emailSender, recipientEmail, recipientName, bccEmail, bccName, subject, emailBody, txtBody);
	}

}
