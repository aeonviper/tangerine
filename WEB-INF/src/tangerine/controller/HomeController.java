package tangerine.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.annotation.Response;
import orion.view.View;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.Language;

public class HomeController extends BaseController {

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Request
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Response
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View show() {
		return new View(View.Type.FORWARD, "/WEB-INF/view/home.jsp");
	}
}
