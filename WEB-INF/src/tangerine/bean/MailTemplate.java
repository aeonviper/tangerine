package tangerine.bean;

import java.util.List;

import core.utility.Utility;

public class MailTemplate {

	private String registrationHeader = "";
	private String registrationFooter = "";
	private String registrationTextHeader = "";
	private String registrationTextFooter = "";

	private String newEmailRegistrationHeader = "";
	private String newEmailRegistrationFooter = "";
	private String newEmailRegistrationTextHeader = "";
	private String newEmailRegistrationTextFooter = "";

	private String passwordResetHeader = "";
	private String passwordResetFooter = "";
	private String passwordResetTextHeader = "";
	private String passwordResetTextFooter = "";

	private String join(List<String> lines) {
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}

	public void reload() {
		registrationHeader = join(Utility.readFileResource("template/mail/registration-header.html"));
		registrationFooter = join(Utility.readFileResource("template/mail/registration-footer.html"));
		registrationTextHeader = join(Utility.readFileResource("template/mail/registration-header.txt"));
		registrationTextFooter = join(Utility.readFileResource("template/mail/registration-footer.txt"));

		newEmailRegistrationHeader = join(Utility.readFileResource("template/mail/new-email-registration-header.html"));
		newEmailRegistrationFooter = join(Utility.readFileResource("template/mail/new-email-registration-footer.html"));
		newEmailRegistrationTextHeader = join(Utility.readFileResource("template/mail/new-email-registration-header.txt"));
		newEmailRegistrationTextFooter = join(Utility.readFileResource("template/mail/new-email-registration-footer.txt"));

		passwordResetHeader = join(Utility.readFileResource("template/mail/password-reset-header.html"));
		passwordResetFooter = join(Utility.readFileResource("template/mail/password-reset-footer.html"));
		passwordResetTextHeader = join(Utility.readFileResource("template/mail/password-reset-header.txt"));
		passwordResetTextFooter = join(Utility.readFileResource("template/mail/password-reset-footer.txt"));
	}

	public String getRegistrationHeader() {
		return registrationHeader;
	}

	public void setRegistrationHeader(String registrationHeader) {
		this.registrationHeader = registrationHeader;
	}

	public String getRegistrationFooter() {
		return registrationFooter;
	}

	public void setRegistrationFooter(String registrationFooter) {
		this.registrationFooter = registrationFooter;
	}

	public String getRegistrationTextHeader() {
		return registrationTextHeader;
	}

	public void setRegistrationTextHeader(String registrationTextHeader) {
		this.registrationTextHeader = registrationTextHeader;
	}

	public String getRegistrationTextFooter() {
		return registrationTextFooter;
	}

	public void setRegistrationTextFooter(String registrationTextFooter) {
		this.registrationTextFooter = registrationTextFooter;
	}

	public String getNewEmailRegistrationHeader() {
		return newEmailRegistrationHeader;
	}

	public void setNewEmailRegistrationHeader(String newEmailRegistrationHeader) {
		this.newEmailRegistrationHeader = newEmailRegistrationHeader;
	}

	public String getNewEmailRegistrationFooter() {
		return newEmailRegistrationFooter;
	}

	public void setNewEmailRegistrationFooter(String newEmailRegistrationFooter) {
		this.newEmailRegistrationFooter = newEmailRegistrationFooter;
	}

	public String getNewEmailRegistrationTextHeader() {
		return newEmailRegistrationTextHeader;
	}

	public void setNewEmailRegistrationTextHeader(String newEmailRegistrationTextHeader) {
		this.newEmailRegistrationTextHeader = newEmailRegistrationTextHeader;
	}

	public String getNewEmailRegistrationTextFooter() {
		return newEmailRegistrationTextFooter;
	}

	public void setNewEmailRegistrationTextFooter(String newEmailRegistrationTextFooter) {
		this.newEmailRegistrationTextFooter = newEmailRegistrationTextFooter;
	}

	public String getPasswordResetHeader() {
		return passwordResetHeader;
	}

	public void setPasswordResetHeader(String passwordResetHeader) {
		this.passwordResetHeader = passwordResetHeader;
	}

	public String getPasswordResetFooter() {
		return passwordResetFooter;
	}

	public void setPasswordResetFooter(String passwordResetFooter) {
		this.passwordResetFooter = passwordResetFooter;
	}

	public String getPasswordResetTextHeader() {
		return passwordResetTextHeader;
	}

	public void setPasswordResetTextHeader(String passwordResetTextHeader) {
		this.passwordResetTextHeader = passwordResetTextHeader;
	}

	public String getPasswordResetTextFooter() {
		return passwordResetTextFooter;
	}

	public void setPasswordResetTextFooter(String passwordResetTextFooter) {
		this.passwordResetTextFooter = passwordResetTextFooter;
	}

}
