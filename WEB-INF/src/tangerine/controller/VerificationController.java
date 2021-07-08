package tangerine.controller;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import tangerine.enumeration.VerificationType;
import tangerine.model.User;
import tangerine.model.Verification;
import tangerine.service.UserService;
import tangerine.service.VerificationService;
import tangerine.service.VoucherService;
import tangerine.core.Constant;
import tangerine.core.Utility;
import core.validation.field.RequiredField;
import core.validation.field.RequiredStringField;

public class VerificationController extends BaseController {

	@Inject
	private VerificationService verificationService;

	@Inject
	private VoucherService voucherService;

	@Inject
	private UserService userService;

	private Verification verification = new Verification();
	private String textId;
	private String userTextId;
	private String code;

	private User user;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View verifyTextId() {

		if (!( //
		validateRequiredString( //
		new RequiredStringField("textId", textId) //
		) //
		)) {
			return viewNotification;
		}

		verification = verificationService.findByTextId(textId);
		return verify(verification);
	}

	public View verifyCode() {

		if (!( //
		validateRequired( //
		new RequiredField("userTextId", userTextId)) //
		&& validateRequiredString( //
		new RequiredStringField("code", code) //
		) //
		)) {
			return viewNotification;
		}

		User user = userService.findByTextId(userTextId);
		if (user != null) {
			verification = verificationService.findByUserIdCode(user.getId(), code);
		}
		return verify(verification);
	}

	private View verify(Verification verification) {
		if (verificationService.verify(verification)) {
			if (VerificationType.MembershipActivation == verification.getType()) {

				User user = userService.find(verification.getUserId());
				user.stateMap();
				Set<String> voucherSet = (Set<String>) user.getStateMap().get("voucherName");
				if (voucherSet != null && !voucherSet.isEmpty()) {
					String voucherName = voucherSet.iterator().next();
					if (!Utility.isBlank(voucherName)) {
						if (user.isLearner()) {
							voucherService.apply(user.getId(), voucherName, new ArrayList<>());
						}
					}
				}

				return new View(View.Type.REDIRECT, "/login");
			}
		} else {
			notification.addError(language(language, LanguageKey.verificationFailed));
			return viewNotification;
		}
		return new View(View.Type.REDIRECT, "/home");
	}

	public View verifyUserTextId() {
		if (!( //
		validateRequired( //
		new RequiredField("userTextId", userTextId) //
		) //
		)) {
			return viewNotification;
		}

		user = userService.findByTextId(userTextId);

		if (user != null) {
			return new View(View.Type.FORWARD, "/WEB-INF/view/verification.jsp");
		}

		notification.addError(language(language, LanguageKey.userNonExistent));
		return viewNotification;
	}

	public View show() {

		if (!( //
		validateRequiredString( //
		new RequiredStringField("textId", textId) //
		) //
		)) {
			return viewNotification;
		}

		Verification dbVerification = verificationService.findByTextId(textId);
		if (dbVerification != null) {
			User dbUser = userService.find(dbVerification.getUserId());
			verification.setId(dbVerification.getId());
			verification.setUser(new User());
			verification.getUser().setEmail(dbUser.getEmail());
		}
		return new View(View.Type.JSON, verification);
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Verification getVerification() {
		return verification;
	}

	public User getUser() {
		return user;
	}

	public void setUserTextId(String userTextId) {
		this.userTextId = userTextId;
	}

}
