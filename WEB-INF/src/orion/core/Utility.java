package orion.core;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class Utility extends core.utility.Utility {

	public static final Gson gson = new Gson();
	public static final NumberFormat numberFormatCurrency;

	static {
		DecimalFormat decimalFormat = new DecimalFormat();
		DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
		decimalFormatSymbols.setGroupingSeparator('.');
		decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setCurrencySymbol("Rp ");
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		numberFormatCurrency = decimalFormat;
	}

	public static String getPath(HttpServletRequest request) {
		return request.getRequestURI().substring(request.getContextPath().length());
	}

	public static <T> T[] parseJsonArray(Class<T> type, String data) {
		JsonParser parser = new JsonParser();
		JsonArray jsonArray = parser.parse(data).getAsJsonArray();
		T[] array = (T[]) Array.newInstance(type, jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i++) {
			array[i] = gson.fromJson(jsonArray.get(i), type);
		}
		return array;
	}

}
