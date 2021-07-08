package tangerine.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tangerine.service.EmailService;
import tangerine.service.SequenceService;
import tangerine.service.VoucherService;
import core.service.TransactionBundle;
import core.service.TransactionService;
import core.utility.DateTimeUtility;
import core.utility.Logger;
import core.validation.field.RequiredField;
import core.validation.field.RequiredStringField;
import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.enumeration.Category;
import tangerine.enumeration.EmailSender;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import tangerine.enumeration.Level;
import tangerine.enumeration.QuestionStatus;
import tangerine.enumeration.VerificationType;
import tangerine.bean.Principal;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.SubscriptionPackage;
import tangerine.enumeration.MembershipType;
import tangerine.model.User;
import tangerine.model.Verification;
import tangerine.model.Voucher;
import tangerine.service.UserService;
import tangerine.service.VerificationService;

public class AuthenticationController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private SequenceService sequenceService;

	@Inject
	private VerificationService verificationService;

	@Inject
	private EmailService emailService;

	@Inject
	private VoucherService voucherService;

	@Inject
	private TransactionService transactionService;

	private HttpServletRequest request;

	private User user = new User();
	private String confirmPassword;
	private String isEducator;
	private String registrationCode;
	private String verificationCode;
	private String verificationTextId;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	@Request
	public void setRequest(HttpServletRequest request) {
		this.request = request;
		// language = Utility.determineLanguage(request);
	}

	public View register() {
		return new View(View.Type.FORWARD, "/WEB-INF/view/register.jsp");
	}

	public View doRegister() {

		if (!( //
		validateRequired(new RequiredField("user", user)) //
		&& validateRequiredString( //
		new RequiredStringField("user.email", user.getEmail()), //
		new RequiredStringField("user.phone", user.getPhone()), //
		new RequiredStringField("user.password", user.getPassword()), //
		new RequiredStringField("confirmPassword", confirmPassword) //
		) //
		)) {
			return viewNotification;
		}

		Utility.stripText(user, "name", "email", "phone");
		Utility.sanitise(user, "name", "email", "phone");

		registrationCode = Utility.stripText(registrationCode);
		registrationCode = Utility.sanitise(registrationCode);

		user.setStateMap(new HashMap<String, Object>());

		if (!Utility.isBlank(registrationCode)) {

			registrationCode = registrationCode.toUpperCase();

			Voucher voucher = voucherService.findByName(registrationCode);
			if (voucher != null) {

				Set<String> voucherSet = new HashSet<>();
				voucherSet.add(voucher.getName());
				user.getStateMap().put("voucherName", voucherSet);

			} else {
				notification.addError(language(language, LanguageKey.voucherNonExistent) + " " + registrationCode);
				return viewNotification;
			}
		}

		if (confirmPassword.equals(user.getPassword())) {
			if (user.getPassword().length() >= tangerine.core.Constant.minimumPasswordLength) {
				final User dbUser = userService.findByEmail(user.getEmail());
				if (dbUser == null) {

					transactionService.readWrite(new TransactionBundle() {
						public void execute() {
							String textId;
							while ((userService.findByTextId(textId = userService.generateTextId(10))) != null) {
							}
							user.setTextId(textId);
							user.setPassword(Utility.hashPassword(user.getPassword()));
							if ("yes".equals(isEducator)) {
								user.setMembershipType(MembershipType.Educator);

								Principal principal = new Principal(null, null, null);
								principal.setCategorySet(Category.enumerationSet);
								principal.setLevelSet(Level.enumerationSet);
								principal.setQuestionStatusSet(QuestionStatus.enumerationSet);
								principal.state();
								user.getStateMap().putAll(principal.getStateMap());

							} else {
								user.setMembershipType(MembershipType.Learner);
							}
							user.setActive(null);
							user.setSubscriptionCredit(0L);
							user.setCreated(DateTimeUtility.now());
							user.state();
							userService.save(user);

							User databaseUser = userService.find(user.getId());

							Verification verification = new Verification();
							verification.setTextId(Math.abs(random.nextLong()) + "-" + sequenceService.sequenceVerification() + "-" + UUID.randomUUID().toString());
							do {
								verification.setCode(verificationService.randomCode(7));
							} while (verificationService.findByUserIdCode(databaseUser.getId(), verification.getCode()) != null);
							verification.setType(VerificationType.MembershipActivation);
							verification.setUserId(databaseUser.getId());
							verification.setCreated(DateTimeUtility.now());
							verificationService.save(verification);

							if (emailService.email( //
							EmailSender.Account, //
							databaseUser.getEmail(), //
							databaseUser.getName(), //
							"viperblaster+tanyatutor@gmail.com", //
							"viperblaster", //
							"TanyaTutor.com - Registration", //
							Constant.mailTemplate.getRegistrationHeader() + //
							"Hi " + databaseUser.getName() + "<br/><br/>" + //
							"Your membership activation verification code is <b>" + verification.getCode() + "</b><br/><br/>" + //
							"Alternatively, you can go to <a href=\"http://tanyatutor.com/verify/" + verification.getTextId() + "\">http://tanyatutor.com/verify/" + verification.getTextId() + "</a><br/><br/>" + //
							Constant.mailTemplate.getRegistrationFooter(), //
							Constant.mailTemplate.getRegistrationTextHeader() + //
							"Hi " + databaseUser.getName() + "\n\n" + //
							"Your membership activation verification code is " + verification.getCode() + "\n" + //
							"Alternatively, you can go to http://tanyatutor.com/verify/" + verification.getTextId() + "\n" + //
							Constant.mailTemplate.getRegistrationTextFooter() //
							)) {
							} else {
								Logger.getLogger().error("Exception while trying to send registration email to " + databaseUser.getEmail());
							}

							user = new User();
							user.setId(databaseUser.getId());
							user.setTextId(databaseUser.getTextId());
						}
					});
					return new View(View.Type.REDIRECT, "/verify/user/" + user.getTextId());
				} else if (dbUser != null) {
					if (dbUser.getActive() == null) {

						transactionService.readWrite(new TransactionBundle() {
							public void execute() {
								dbUser.setName(user.getName());
								dbUser.setPhone(user.getPhone());
								dbUser.setPassword(Utility.hashPassword(user.getPassword()));
								if ("yes".equals(isEducator)) {
									dbUser.setMembershipType(MembershipType.Educator);

									Principal principal = new Principal(null, null, null);
									principal.setCategorySet(Category.enumerationSet);
									principal.setLevelSet(Level.enumerationSet);
									principal.setQuestionStatusSet(QuestionStatus.enumerationSet);
									principal.state();
									user.getStateMap().putAll(principal.getStateMap());

								} else {
									dbUser.setMembershipType(MembershipType.Learner);
								}
								dbUser.setSubscriptionCredit(0L);
								dbUser.setCreated(DateTimeUtility.now());
								dbUser.setStateMap(user.getStateMap());
								dbUser.state();
								userService.save(dbUser);

								User databaseUser = userService.find(dbUser.getId());

								Verification verification = new Verification();
								verification.setTextId(Math.abs(random.nextLong()) + "-" + sequenceService.sequenceVerification() + "-" + UUID.randomUUID().toString());
								do {
									verification.setCode(verificationService.randomCode(7));
								} while (verificationService.findByUserIdCode(databaseUser.getId(), verification.getCode()) != null);
								verification.setType(VerificationType.MembershipActivation);
								verification.setUserId(databaseUser.getId());
								verification.setCreated(DateTimeUtility.now());

								verificationService.deleteByTypeUserId(verification.getType(), verification.getUserId());

								verificationService.save(verification);

								if (emailService.email( //
								EmailSender.Account, //
								databaseUser.getEmail(), //
								databaseUser.getName(), //
								"viperblaster+tanyatutor@gmail.com", //
								"viperblaster", //
								"TanyaTutor.com - Registration", //
								Constant.mailTemplate.getRegistrationHeader() + //
								"Hi " + databaseUser.getName() + "<br/><br/>" + //
								"Your membership activation verification code is <b>" + verification.getCode() + "</b><br/><br/>" + //
								"Alternatively, you can go to <a href=\"http://tanyatutor.com/verify/" + verification.getTextId() + "\">http://tanyatutor.com/verify/" + verification.getTextId() + "</a><br/><br/>" + //
								Constant.mailTemplate.getRegistrationFooter(), //
								Constant.mailTemplate.getRegistrationTextHeader() + //
								"Hi " + databaseUser.getName() + "\n\n" + //
								"Your membership activation verification code is " + verification.getCode() + "\n" + //
								"Alternatively, you can go to http://tanyatutor.com/verify/" + verification.getTextId() + "\n" + //
								Constant.mailTemplate.getRegistrationTextFooter() //
								)) {
								} else {
									Logger.getLogger().error("Exception while trying to send registration email to " + databaseUser.getEmail());
								}

								user = new User();
								user.setId(databaseUser.getId());
							}
						});
						return new View(View.Type.REDIRECT, "/verify/user/" + dbUser.getTextId());
					} else {
						notification.addNotice(language(language, LanguageKey.userIsRegistered));
					}
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

	public View forgotPassword() {
		return new View(View.Type.FORWARD, "/WEB-INF/view/forgot-password.jsp");
	}

	public View resetPassword() {

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

		User dbUser = userService.findByEmail(user.getEmail());
		if (dbUser != null) {
			Verification verification = new Verification();
			verification.setTextId(Math.abs(random.nextLong()) + "-" + sequenceService.sequenceVerification() + "-" + UUID.randomUUID().toString());
			do {
				verification.setCode(verificationService.randomCode(7));
			} while (verificationService.findByUserIdCode(dbUser.getId(), verification.getCode()) != null);
			verification.setType(VerificationType.PasswordReset);
			verification.setUserId(dbUser.getId());
			verification.setCreated(DateTimeUtility.now());

			verificationService.deleteByTypeUserId(verification.getType(), verification.getUserId());

			verificationService.save(verification);

			if (emailService.email( //
			EmailSender.Account, //
			dbUser.getEmail(), //
			dbUser.getName(), //
			null, //
			null, //
			"TanyaTutor.com - Password Reset", //
			Constant.mailTemplate.getPasswordResetHeader() + //
			"Our system at TanyaTutor.com has received a password reset request for the email address <b>" + dbUser.getEmail() + "</b><br/><br/>" + //
			"The password reset verification code is <b>" + verification.getCode() + "</b><br/><br/>" + //
			// "Alternatively, you can go to <a href=\"http://tanyatutor.com/password/reset/" + verification.getTextId() + "\">http://tanyatutor.com/verify/" + verification.getTextId() + "</a><br/><br/>" + //
			Constant.mailTemplate.getPasswordResetFooter(), //
			Constant.mailTemplate.getPasswordResetTextHeader() + //
			"Our system at TanyaTutor.com has received a password reset request for the email address " + dbUser.getEmail() + "\n" + //
			"The password reset verification code is " + verification.getCode() + "\n" + //
			// "Alternatively, you can go to http://tanyatutor.com/password/reset/" + verification.getTextId() + "\n" + //
			Constant.mailTemplate.getPasswordResetTextFooter() //
			)) {
			} else {
				Logger.getLogger().error("Exception while trying to send password reset email to " + dbUser.getEmail());
			}
		}

		return new View(View.Type.FORWARD, "/WEB-INF/view/reset-password.jsp");

	}

	public View doResetPassword() {

		if (!( //
		validateRequired(new RequiredField("user", user)) //
		&& validateRequiredString( //
		new RequiredStringField("user.email", user.getEmail()), //
		new RequiredStringField("user.password", user.getPassword()), //
		new RequiredStringField("confirmPassword", confirmPassword) //
		) //
		)) {
			return viewNotification;
		}

		if (Utility.isBlank(verificationCode) && Utility.isBlank(verificationTextId)) {
			notification.addError(language(language, LanguageKey.verificationCodeRequired));
			return viewNotification;
		}

		Utility.stripText(user, "email");
		Utility.sanitise(user, "email");

		User dbUser = userService.findByEmail(user.getEmail());
		if (dbUser != null) {
			if (confirmPassword.equals(user.getPassword())) {
				if (user.getPassword().length() >= tangerine.core.Constant.minimumPasswordLength) {
					Verification verification = null;
					if (verificationCode != null) {
						verification = verificationService.findByUserIdCode(dbUser.getId(), verificationCode);
					} else if (verificationTextId != null) {
						verification = verificationService.findByTextId(verificationTextId);
						if (verification != null && dbUser.getId().equals(verification.getUserId())) {

						} else {
							verification = null;
						}
					}
					if (verificationService.verify(verification)) {
						dbUser.setPassword(Utility.hashPassword(user.getPassword()));
						userService.save(dbUser);

						return new View(View.Type.REDIRECT, "/login");
					} else {
						notification.addNotice(language(language, LanguageKey.wrongVerificationCode));
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
		} else {
			notification.addNotice(language(language, LanguageKey.emailNonExistent));
		}

		return viewNotification;
	}

	public View login() {
		return new View(View.Type.FORWARD, "/WEB-INF/view/login.jsp");
	}

	public View doLogin() {
		Utility.stripText(user, "email");

		User dbUser = userService.findByEmail(user.getEmail());
		if (dbUser != null && Utility.checkPassword(user.getPassword(), dbUser.getPassword())) {
			Principal principal = new Principal(dbUser.getId(), dbUser.getEmail(), dbUser.getMembershipType());

			principal.state(dbUser.getState());

			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			session = request.getSession(true);
			session.setMaxInactiveInterval(300 * 60);
			session.setAttribute(Constant.sessionKey, principal);
			session.setAttribute(Principal.class.getCanonicalName(), principal);

			return new View(View.Type.REDIRECT, "/dashboard");
		} else {
			notification.addError(language(language, LanguageKey.usernamePasswordMismatch));
			return viewNotification;
		}
	}

	public View doLogout() {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return new View(View.Type.REDIRECT, "/home");
	}

	public User getUser() {
		return user;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setIsEducator(String isEducator) {
		this.isEducator = isEducator;
	}

	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public void setVerificationTextId(String verificationTextId) {
		this.verificationTextId = verificationTextId;
	}

}
