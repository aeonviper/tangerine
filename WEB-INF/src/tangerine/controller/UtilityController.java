package tangerine.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import orion.annotation.Cookie;
import orion.view.View;
import tangerine.core.Constant;
import tangerine.core.DateTimeUtility;
import tangerine.core.Utility;

public class UtilityController extends BaseController {

	private Long number;
	private String text;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View midnight() {
		return new View(View.Type.JSON, DateTimeUtility.toEpoch(DateTimeUtility.midnight()));
	}

	public View now() {
		return new View(View.Type.JSON, DateTimeUtility.now());
	}

	public View dateTimeConvert() {
		if (number != null) {
			return new View(View.Type.JSON, DateTimeUtility.dateTimeFormatter.format(DateTimeUtility.toLocalDateTime(number)));
		} else if (!Utility.isBlank(text)) {
			if (text.endsWith(".000Z")) {
				int l = text.indexOf(".000Z");
				text = text.substring(0, l);
			}
			try {
				LocalDateTime localDateTime = LocalDateTime.parse(text);
				return new View(View.Type.JSON, localDateTime.toEpochSecond(DateTimeUtility.zoneOffset));
			} catch (DateTimeParseException e) {
				e.printStackTrace();
			}
		}
		return new View(View.Type.JSON, null);
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public void setText(String text) {
		this.text = text;
	}

}
