package tangerine.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.bean.Principal;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.LanguageKey;
import tangerine.model.Administrator;
import tangerine.model.User;
import tangerine.service.AdministratorService;
import tangerine.service.UserService;

public class AdministrationController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private AdministratorService administratorService;

	private HttpServletRequest request;

	private String name;
	private String password;
	private String email;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	@Request
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public View supervision() {
		if (!principal.isEducator()) {
			notification.addError(language(language, LanguageKey.authorisationDenied));
			return viewNotification;
		}
		return new View(View.Type.FORWARD, "/WEB-INF/view/supervision.jsp");
	}

	public View login() {
		if (!principal.isEducator()) {
			notification.addError(language(language, LanguageKey.authorisationDenied));
			return viewNotification;
		}
		return new View(View.Type.FORWARD, "/WEB-INF/view/administration-login.jsp");
	}

	public View doLogin() {
		if (!principal.isEducator()) {
			notification.addError(language(language, LanguageKey.authorisationDenied));
			return viewNotification;
		}

		if (!Utility.isBlank(name, password)) {
			Administrator administrator = administratorService.findByName(name);
			if (administrator != null && Utility.checkPassword(password, administrator.getPassword())) {
				principal.setAdministrator(administrator.getId(), administrator.getName());
				return new View(View.Type.REDIRECT, "/administration");
			} else {
				notification.addError(language(language, LanguageKey.usernamePasswordMismatch));
				return viewNotification;
			}
		}

		return new View(View.Type.REDIRECT, "/administration/login");
	}

	public View doLogout() {
		if (principal.getAdministratorId() != null && principal.getAdministratorName() != null) {
			principal.setAdministrator(null, null);
		}
		return new View(View.Type.REDIRECT, "/administration/login");
	}

	public View administration() {
		if (principal.getAdministratorId() != null && principal.getAdministratorName() != null) {
			return new View(View.Type.FORWARD, "/WEB-INF/view/administration.jsp");
		} else {
			return new View(View.Type.REDIRECT, "/administration/login");
		}
	}

	public View doImpersonate() {
		if (principal.getAdministratorId() != null && principal.getAdministratorName() != null) {

			if (!Utility.isBlank(email)) {
				User user = userService.findByEmail(email);
				if (user != null) {
					principal = new Principal(user.getId(), user.getEmail(), user.getMembershipType());
					principal.state(user.getState());
					HttpSession session = request.getSession(false);
					session.setAttribute(Constant.sessionKey, principal);
					session.setAttribute(Principal.class.getCanonicalName(), principal);
					return new View(View.Type.REDIRECT, "/configuration");
				}
			}
			return new View(View.Type.REDIRECT, "/administration");
		} else {
			return new View(View.Type.REDIRECT, "/administration/login");
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
