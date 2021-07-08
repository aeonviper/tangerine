package core.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import core.model.GenericEntity;

public class Utility {

	public static int max(int[] array) {
		int max = 0;
		for (int a : array) {
			max = Math.max(max, a);
		}
		return max;
	}

	public static int min(int[] array) {
		int min = Integer.MAX_VALUE;
		for (int a : array) {
			min = Math.min(min, a);
		}
		return min;
	}

	public static <T> String join(T[] array, String delimiter) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T t : array) {
			if (!first) {
				sb.append(delimiter);
			} else {
				first = false;
			}
			sb.append(t);
		}
		return sb.toString();
	}

	public static <T> List<T> toList(List<T> inputList) {
		List<T> outputList = new ArrayList<T>();
		for (T element : inputList) {
			outputList.add(element);
		}
		return outputList;
	}

	public static <K, V> Map<K, V> toMap(Map<K, V> inputMap) {
		Map<K, V> outputMap = new HashMap<K, V>();
		for (Map.Entry<K, V> entry : inputMap.entrySet()) {
			outputMap.put(entry.getKey(), entry.getValue());
		}
		return outputMap;
	}

	public static <T> List<T> asList(T[] array) {
		List<T> list = new ArrayList<T>();
		for (T element : array) {
			list.add(element);
		}
		return list;
	}

	public static <V extends GenericEntity<K>, K extends Serializable> Map<K, V> asMap(V[] array) {
		Map<K, V> map = new HashMap<K, V>();
		for (V element : array) {
			map.put(element.getId(), element);
		}
		return map;
	}

	public static <V extends GenericEntity<K>, K extends Serializable> Map<K, V> asMap(List<V> list) {
		Map<K, V> map = new HashMap<K, V>();
		for (V element : list) {
			map.put(element.getId(), element);
		}
		return map;
	}

	public static String digestSHA(String s) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return s;
		}
		try {
			messageDigest.update(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return s;
		}
		return byteToHex(messageDigest.digest());
	}

	public static String byteToHex(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		BigInteger bi = new BigInteger(1, bytes);
		String result = bi.toString(16);
		if (result.length() % 2 != 0) {
			return "0" + result;
		}
		return result;
	}

	public static byte[] hexToByte(String hexString) {
		if (hexString == null) {
			return null;
		}
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
		}
		return data;
	}

	public static List<String> readFile(File file) {
		FileReader fileReader = null;
		BufferedReader reader = null;
		List<String> lines = new ArrayList<String>();
		try {
			String line = null;
			reader = new BufferedReader(fileReader = new FileReader(file));
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException eio) {
			eio.printStackTrace();
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return lines;
	}

	public static boolean writeFile(File file, List<String> lines) {
		FileWriter fileWriter = null;
		PrintWriter outputStream = null;
		boolean success = false;
		try {
			outputStream = new PrintWriter(fileWriter = new FileWriter(file));
			for (String line : lines) {
				outputStream.println(line);
			}
			success = true;
		} catch (IOException eio) {
			eio.printStackTrace();
		} finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	/* SQL */

	public static String escapeSQLLike(String escape, String sql) {
		return sql.replace("%", escape + "%").replace("_", escape + "_");
	}

	public static String findSQLLikeEscape(String s) {
		for (String candidate : new String[] { "!", "^", "#", "|", "~", "$", "-", "_", "?" }) {
			if (!s.contains(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	/* IO */

	public static List<String> readFileResource(String filename) {
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		String line;
		URL url;
		List<String> lines = new ArrayList<String>();

		try {
			url = Thread.currentThread().getContextClassLoader().getResource(filename);
			inputStreamReader = new InputStreamReader(url.openStream());
			reader = new BufferedReader(inputStreamReader);
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading file. Cause: " + e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException eio) {
				eio.printStackTrace();
			}

			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException eio) {
				eio.printStackTrace();
			}
		}

		return lines;
	}

	public static boolean deleteQuietly(File file) {
		if (file == null) {
			return false;
		}

		try {
			return file.delete();
		} catch (Exception ignored) {
			return false;
		}
	}

	public static boolean isWhitespace(char ch) {
		return Character.isSpaceChar(ch) || Character.isWhitespace(ch);
	}

	/* String */

	protected static String stripStart(String str, String stripChars) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		int start = 0;
		if (stripChars == null) {
			while (start != strLen && isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
				start++;
			}
		}
		return str.substring(start);
	}

	protected static String stripEnd(String str, String stripChars) {
		int end;
		if (str == null || (end = str.length()) == 0) {
			return str;
		}

		if (stripChars == null) {
			while (end != 0 && isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.length() == 0) {
			return str;
		} else {
			while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	public static String stripTextUsingStripCharacters(String str, String stripChars) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return stripEnd(stripStart(str, stripChars), stripChars);
	}

	public static String stripText(String str) {
		return stripTextUsingStripCharacters(str, null);
	}

	public static boolean isBlank(String... inputs) {
		for (String input : inputs) {
			if (input == null || stripText(input).length() == 0) {
				return true;
			}
		}
		return false;
	}

	public static void stripText(Object bean, String... names) {
		String value;
		for (String name : names) {
			try {
				value = BeanUtils.getProperty(bean, name);
			} catch (Exception e) {
				continue;
			}

			try {
				BeanUtils.setProperty(bean, name, stripText(value));
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static void strip(Object bean, String... names) {
		String value;
		for (String name : names) {
			try {
				value = BeanUtils.getProperty(bean, name);
			} catch (Exception e) {
				continue;
			}

			try {
				BeanUtils.setProperty(bean, name, strip(value));
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static String strip(String text) {
		return text == null ? "" : Utility.stripText(text);
	}

	public static String stringify(String text) {
		return text == null ? "" : text;
	}

	public static String stringify(Object object) {
		return object == null ? "" : object.toString();
	}

	public static String snippet(String input, int limit) {
		if (limit > 0) {
			if (input != null && input.length() > limit) {
				int i;
				for (i = (limit - 1); i < input.length() && !Character.isWhitespace(input.charAt(i)); i++) {
				}
				return input.substring(0, i) + " ...";
			}
			return input;
		} else {
			return "";
		}
	}

	/* Net */

	private static void disableSSLValidation() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String urlHostName, SSLSession session) {
					return true;
				}
			});
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	public static void deleteCookie(HttpServletResponse response, String key, String value) {
		Cookie deleteCookie = new Cookie(key, value);
		deleteCookie.setPath("/");
		deleteCookie.setMaxAge(0);
		response.addCookie(deleteCookie);
	}

	public static void addCookie(HttpServletResponse response, String key, String value) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
		cookie.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(cookie);
	}

	public static String getCookie(HttpServletRequest request, String key) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static String getRequesterAddress(HttpServletRequest request) {
		String result = "";
		if (request != null) {
			String remoteAddr = request.getRemoteAddr();
			String remoteHost = request.getRemoteHost();

			if (remoteAddr == null) {
				remoteAddr = "";
			}
			if (remoteHost == null) {
				remoteHost = "";
			}

			remoteAddr = remoteAddr.trim();
			remoteHost = remoteHost.trim();

			if (remoteAddr.equals(remoteHost)) {
				result = remoteAddr;
			} else {
				result = remoteAddr + " " + remoteHost;
			}
		}
		return result;
	}

	// URLEncoder.encode(input, "UTF-8");
	public static String encodeQueryString(String input) {
		StringBuilder output = new StringBuilder();
		int i, l;
		char c;

		if (input == null) {
			return "";
		}

		for (l = input.length(), i = 0; i < l; i++) {
			c = input.charAt(i);
			switch (c) {

			case '$':
				output.append("%24");
				break;
			case '&':
				output.append("%26");
				break;
			case '+':
				output.append("%2B");
				break;
			case ',':
				output.append("%2C");
				break;
			case '/':
				output.append("%2F");
				break;
			case ':':
				output.append("%3A");
				break;
			case '=':
				output.append("%3D");
				break;
			case '?':
				output.append("%3F");
				break;
			case '@':
				output.append("%40");
				break;

			// or %20
			case ' ':
				output.append("+");
				break;
			case '\'':
				output.append("%27");
				break;
			case '<':
				output.append("%3C");
				break;
			case '>':
				output.append("%3E");
				break;
			case '#':
				output.append("%23");
				break;
			case '%':
				output.append("%25");
				break;
			case '{':
				output.append("%7B");
				break;
			case '}':
				output.append("%7D");
				break;
			case '|':
				output.append("%7C");
				break;
			case '\\':
				output.append("%5C");
				break;
			case '^':
				output.append("%5E");
				break;
			case '~':
				output.append("%7E");
				break;
			case '[':
				output.append("%5B");
				break;
			case ']':
				output.append("%5D");
				break;
			case '`':
				output.append("%60");
				break;

			default:
				output.append(c);
			}
		}

		return output.toString();
	}

	public static String encodeQueryString(ArrayList messageList) {
		StringBuilder output = new StringBuilder();
		int i, l;

		if (messageList != null) {
			for (l = messageList.size(), i = 0; i < l; i++) {
				output.append(encodeQueryString((String) messageList.get(i)));
			}
		}

		return output.toString();
	}

}
