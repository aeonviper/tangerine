package tangerine.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import orion.filter.GsonEnumTypeAdapterFactory;
import tangerine.crypto.BCrypt;
import tangerine.core.Constant;
import tangerine.enumeration.Language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import core.utility.DateTimeUtility;

public class Utility extends orion.core.Utility {

	public static Type typeStringStringMap = new TypeToken<Map<String, String>>() {
	}.getType();

	public static Gson gsonEnumBean = new GsonBuilder().registerTypeAdapterFactory(new GsonEnumTypeAdapterFactory()).create();

	public static String hashPassword(String plaintext) {
		return BCrypt.hashpw(plaintext, BCrypt.gensalt(12));
	}

	public static boolean checkPassword(String candidate, String hashed) {
		return BCrypt.checkpw(candidate, hashed);
	}

	public static String sanitise(String value) {
		if (value == null) {
			return value;
		}
		return Jsoup.clean(value, "", Whitelist.basic(), new Document.OutputSettings().prettyPrint(false));
	}

	public static void sanitise(Object bean, String... names) {
		String value;
		for (String name : names) {
			try {
				value = BeanUtils.getProperty(bean, name);
			} catch (Exception e) {
				continue;
			}

			try {
				BeanUtils.setProperty(bean, name, sanitise(value));
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static String textId(String name) {
		String replacement = "_";
		String textId = "";
		if (name != null) {
			textId = name.trim().toLowerCase();
			textId = textId.replaceAll("\\ +", replacement).replaceAll("[^a-zA-Z0-9-_]", "").replaceAll("\\_+", "_").replaceAll("\\-+", "-");
			if (textId.startsWith(replacement)) {
				textId = textId.substring(1);
			}
			if (textId.endsWith(replacement)) {
				textId = textId.substring(0, textId.length() - 1);
			}
		}
		return textId;
	}

	public static String fileName(String fileName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9')) || (c == '-') || (c == '_') || (c == '.')) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static boolean hasMethod(Class clazz, String methodName) {
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				return true;
			}
		}
		return false;
	}

	public static Object invokeMethod(Object object, String methodName, Object... argumentArray) {
		for (Method method : object.getClass().getMethods()) {
			if (method.getName().equals(methodName)) {
				try {
					return method.invoke(object, argumentArray);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean same(Object a, Object b) {
		boolean same = false;
		if (a != null && b != null && (a.equals(b))) {
			same = true;
		} else if (a == null && b == null) {
			same = true;
		}
		return same;
	}

	public static boolean different(Object a, Object b) {
		return !same(a, b);
	}

	public static String nullIfBlank(String text) {
		if (isBlank(text)) {
			return null;
		} else {
			return text;
		}
	}

	public static void nullIfBlank(Object bean, String... names) {
		String value;
		for (String name : names) {
			try {
				value = BeanUtils.getProperty(bean, name);
			} catch (Exception e) {
				continue;
			}

			try {
				BeanUtils.setProperty(bean, name, nullIfBlank(value));
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static String ellipsis(String text) {
		text = strip(text);
		if (text.isEmpty()) {
			text = "<span style='color:#777;text-shadow:1px 1px 1px #999;text-decoration:italic;'>...</span>";
		} else {
			if (text.length() > 128) {
				text = text.substring(0, 128) + " ...";
			}
		}
		return text;
	}

	public static String showDate(Long epochSecond) {
		if (epochSecond != null) {
			return DateTimeUtility.dateTimeFormatter.format(DateTimeUtility.toLocalDateTime(epochSecond));
		}
		return "";
	}

	public static Language determineLanguage(String value, Language defaultLanguage) {
		for (Language language : Language.values()) {
			if (language.name().toLowerCase().equals(value)) {
				return language;
			}
		}
		return defaultLanguage;
	}

	public static Language determineLanguage(String value) {
		return determineLanguage(value, Language.Indonesian);
	}

}
