package core.utility;

import java.util.Random;

public class RandomUtility {

	public static final Random random = new Random();

	public static int randomInt(int limit) {
		return random.nextInt(limit);
	}

	public static String randomString() {
		return randomString(8);
	}

	public static String randomWords(int limit) {
		return randomWords(0, limit);
	}

	public static String randomWords(int limitStart, int limitEnd) {
		if (limitEnd > limitStart && limitStart > 0) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			int l = limitStart + random.nextInt(limitEnd - limitStart);
			for (i = 0; i < l; i++) {
				sb.append(randomString(12) + " ");
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	public static String randomColor() {
		String[] colors = { "#00CBE7", "#00DA3C", "#F4F328", "#FD8603", "#DF151A", "#E42692" };
		return colors[random.nextInt(colors.length)];
	}

	public static String randomString(final int LENGTH) {
		char[] universe = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		int i;
		char[] randomString = new char[LENGTH];

		for (i = 0; i < LENGTH; i++) {
			randomString[i] = universe[random.nextInt(universe.length)];
		}

		return new String(randomString);
	}

}
