package tangerine.controller;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import tangerine.core.Constant;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import orion.annotation.Session;
import orion.controller.Notification;
import orion.view.View;
import tangerine.bean.Principal;
import core.validation.field.DoubleRangeField;
import core.validation.field.EmailField;
import core.validation.field.GenericRangeField;
import core.validation.field.RegexField;
import core.validation.field.RequiredField;
import core.validation.field.RequiredStringField;
import core.validation.field.StringLengthField;
import core.validation.field.URLField;
import core.validation.validator.DateRangeValidator;
import core.validation.validator.DoubleRangeValidator;
import core.validation.validator.EmailValidator;
import core.validation.validator.IntegerRangeValidator;
import core.validation.validator.LongRangeValidator;
import core.validation.validator.RegexValidator;
import core.validation.validator.RequiredStringValidator;
import core.validation.validator.RequiredValidator;
import core.validation.validator.ShortRangeValidator;
import core.validation.validator.StringLengthValidator;
import core.validation.validator.URLValidator;

public class BaseController {

	protected static final Random random = new SecureRandom();

	protected Principal principal;

	protected String parameter;

	protected Language language;

	@Session
	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	protected Notification notification = new Notification();
	protected View viewNotification = new View(View.Type.FORWARD, "/WEB-INF/view/notification.jsp");

	protected RequiredValidator requiredValidator = new RequiredValidator();
	protected RequiredStringValidator requiredStringValidator = new RequiredStringValidator();
	protected StringLengthValidator stringLengthValidator = new StringLengthValidator();
	protected IntegerRangeValidator integerRangeValidator = new IntegerRangeValidator();
	protected LongRangeValidator longRangeValidator = new LongRangeValidator();
	protected ShortRangeValidator shortRangeValidator = new ShortRangeValidator();
	protected DateRangeValidator dateRangeValidator = new DateRangeValidator();
	protected DoubleRangeValidator doubleRangeValidator = new DoubleRangeValidator();
	protected RegexValidator regexValidator = new RegexValidator();
	protected EmailValidator emailValidator = new EmailValidator();
	protected URLValidator urlValidator = new URLValidator();

	protected boolean validateRequired(RequiredField... fields) {
		return requiredValidator.validate(notification, fields);
	}

	protected boolean validateRequiredString(RequiredStringField... fields) {
		return requiredStringValidator.validate(notification, fields);
	}

	protected boolean validateStringLength(StringLengthField... fields) {
		return stringLengthValidator.validate(notification, fields);
	}

	protected boolean validateIntegerRange(GenericRangeField<Integer>... fields) {
		return integerRangeValidator.validate(notification, fields);
	}

	protected boolean validateLongRange(GenericRangeField<Long>... fields) {
		return longRangeValidator.validate(notification, fields);
	}

	protected boolean validateShortRange(GenericRangeField<Short>... fields) {
		return shortRangeValidator.validate(notification, fields);
	}

	protected boolean validateDateRange(GenericRangeField<Date>... fields) {
		return dateRangeValidator.validate(notification, fields);
	}

	protected boolean validateDoubleRange(DoubleRangeField... fields) {
		return doubleRangeValidator.validate(notification, fields);
	}

	protected boolean validateRegex(RegexField... fields) {
		return regexValidator.validate(notification, fields);
	}

	protected boolean validateEmail(EmailField... fields) {
		return emailValidator.validate(notification, fields);
	}

	protected boolean validateURL(URLField... fields) {
		return urlValidator.validate(notification, fields);
	}

	public Notification getNotification() {
		return notification;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public static String language(Language language, LanguageKey languageKey) {
		if (language != null && languageKey != null) {
			Map<String, String> languageKeyMap = Constant.languageKeyMap;
			String key = "languages." + language.getCode() + "." + languageKey.name();
			String value = languageKeyMap.get(key);
			if (value != null) {
				return value;
			}
		}
		return "";
	}

	public Principal getPrincipal() {
		return principal;
	}

	public boolean isEducator() {
		if (principal != null) {
			return principal.isEducator();
		}
		return false;
	}

	public boolean isLearner() {
		if (principal != null) {
			return principal.isLearner();
		}
		return false;
	}

	public Language getLanguage() {
		return language;
	}

}
