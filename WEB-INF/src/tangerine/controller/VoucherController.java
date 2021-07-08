package tangerine.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import tangerine.service.VoucherService;
import core.validation.field.RequiredField;
import core.validation.field.RequiredStringField;

public class VoucherController extends BaseController {

	@Inject
	private VoucherService voucherService;

	private String voucherName;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View applyVoucher() {

		if (!( //
		validateRequired(new RequiredField("voucherName", voucherName)) //
		&& validateRequiredString( //
		new RequiredStringField("voucherName", voucherName) //
		) //
		)) {
			return viewNotification;
		}

		voucherName = Utility.stripText(voucherName);
		voucherName = Utility.sanitise(voucherName);
		voucherName = voucherName.toUpperCase();

		List<LanguageKey> notificationList = new ArrayList<>();
		Boolean result = voucherService.apply(principal.getUserId(), voucherName, notificationList);

		if (Boolean.TRUE.equals(result)) {
			return new View(View.Type.REDIRECT, "/dashboard");
		} else {
			for (LanguageKey languageKey : notificationList) {
				notification.addNotice(language(language, languageKey));
			}
			return viewNotification;
		}
	}

	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}

}
