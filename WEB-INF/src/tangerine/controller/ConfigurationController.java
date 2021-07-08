package tangerine.controller;

import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.EmailSender;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import tangerine.enumeration.VerificationType;
import tangerine.model.User;
import tangerine.model.Verification;
import tangerine.service.EmailService;
import tangerine.service.SequenceService;
import tangerine.service.UserService;
import tangerine.service.VerificationService;
import core.service.TransactionBundle;
import core.service.TransactionService;
import core.utility.DateTimeUtility;
import core.utility.Logger;
import core.validation.field.RequiredField;
import core.validation.field.RequiredStringField;

public class ConfigurationController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private EmailService emailService;

	@Inject
	private VerificationService verificationService;

	@Inject
	private SequenceService sequenceService;

	@Inject
	private TransactionService transactionService;

	private User user = new User();
	private String newPassword;
	private String confirmPassword;
	private String verificationCode;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View view() {

		user = userService.find(principal.getUserId());

		return new View(View.Type.FORWARD, "/WEB-INF/view/configuration.jsp");
	}

	public View doEditProfile() {

		if (!( //
		validateRequired(new RequiredField("user", user)) //
		&& validateRequiredString( //
		new RequiredStringField("user.phone", user.getPhone()), //
		new RequiredStringField("user.name", user.getName()) //
		) //
		)) {
			return viewNotification;
		}

		Utility.stripText(user, "phone", "name", "school");
		Utility.sanitise(user, "phone", "name", "school");

		User dbUser = userService.find(principal.getUserId());
		if (dbUser != null) {
			dbUser.setName(user.getName());
			dbUser.setPhone(user.getPhone());
			dbUser.setSchool(user.getSchool());
			userService.save(dbUser);
			notification.addNotice(language(language, LanguageKey.profileUpdated));
		}

		return view();

	}

	public View doEditEmail() {

		if (!( //
		validateRequired(new RequiredField("user", user)) //
		&& validateRequiredString( //
		new RequiredStringField("user.email", user.getEmail()) //
		) //
		)) {
			return viewNotification;
		}

		Utility.stripText(user, "email");
		Utility.sanitise(user, "email");

		User dbUser = userService.find(principal.getUserId());
		if (dbUser != null) {

			if (!dbUser.getEmail().equals(user.getEmail())) {
				dbUser.setEmailNew(user.getEmail());
				userService.save(dbUser);

				if (dbUser.getEmailNew() != null) {
					Verification verification = new Verification();
					verification.setTextId(Math.abs(random.nextLong()) + "-" + sequenceService.sequenceVerification() + "-" + UUID.randomUUID().toString());
					do {
						verification.setCode(verificationService.randomCode(7));
					} while (verificationService.findByUserIdCode(dbUser.getId(), verification.getCode()) != null);
					verification.setType(VerificationType.NewEmailConfirmation);
					verification.setUserId(dbUser.getId());
					verification.setCreated(DateTimeUtility.now());

					verificationService.deleteByTypeUserId(verification.getType(), verification.getUserId());

					verificationService.save(verification);

					if (emailService.email( //
					EmailSender.Account, //
					dbUser.getEmailNew(), //
					dbUser.getName(), //
					null, //
					null, //
					"TanyaTutor.com - New Email Confirmation", //
					Constant.mailTemplate.getNewEmailRegistrationHeader() + //
					"Hi " + dbUser.getName() + "<br/><br/>" + //
					"The verification code to activate your new email address is <b>" + verification.getCode() + "</b><br/><br/>" + //
					Constant.mailTemplate.getNewEmailRegistrationFooter(), //
					Constant.mailTemplate.getNewEmailRegistrationTextHeader() + //
					"Hi " + dbUser.getName() + "\n\n" + //
					"The verification code to activate your new email address is " + verification.getCode() + "\n" + //
					Constant.mailTemplate.getNewEmailRegistrationTextFooter() //
					)) {
					} else {
						Logger.getLogger().error("Exception while trying to send new email confirmation email to " + dbUser.getEmail());
					}
				}
			}
		}

		return new View(View.Type.REDIRECT, "/configuration");

	}

	public View doVerifyEmailNew() {

		User dbUser = userService.find(principal.getUserId());
		if (dbUser != null) {
			Verification verification = verificationService.findByUserIdCode(dbUser.getId(), verificationCode);
			if (verificationService.verify(verification)) {

			}
		}

		return new View(View.Type.REDIRECT, "/configuration");
	}

	public View doCancelEmailNew() {

		User dbUser = userService.find(principal.getUserId());
		if (dbUser != null) {
			dbUser.setEmailNew(null);
			userService.save(dbUser);
		}

		return new View(View.Type.REDIRECT, "/configuration");
	}

	public View doEditPassword() {

		if (!( //
		validateRequired(new RequiredField("user", user)) //
		&& validateRequiredString( //
		new RequiredStringField("user.password", user.getPassword()), //
		new RequiredStringField("newPassword", newPassword), //
		new RequiredStringField("confirmPassword", confirmPassword) //
		) //
		)) {
			return viewNotification;
		}

		if (confirmPassword.equals(newPassword)) {
			if (newPassword.length() >= tangerine.core.Constant.minimumPasswordLength) {
				final User dbUser = userService.find(principal.getUserId());
				if (dbUser != null) {
					if (Utility.checkPassword(user.getPassword(), dbUser.getPassword())) {
						transactionService.readWrite(new TransactionBundle() {
							public void execute() {
								dbUser.setPassword(Utility.hashPassword(newPassword));
								userService.save(dbUser);
							}
						});
						notification.addNotice(language(language, LanguageKey.passwordChanged));
						return view();
					} else {
						notification.addError(language(language, LanguageKey.wrongCurrentPassword));
					}
				} else {
					notification.addError(language(language, LanguageKey.userNonExistent));
				}
			} else {
				if (language == Language.English) {
					notification.addNotice("Password must be minimum " + tangerine.core.Constant.minimumPasswordLength + " characters/numbers in length");
				} else {
					notification.addNotice("Password harus minimum " + tangerine.core.Constant.minimumPasswordLength + " karakter/angka");
				}
			}
		} else {
			notification.addNotice(language(language, LanguageKey.newPasswordAndConfirmPasswordMismatch));
		}

		return viewNotification;
	}

	public User getUser() {
		return user;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
