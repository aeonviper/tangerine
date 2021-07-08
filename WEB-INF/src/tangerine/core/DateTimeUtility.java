package tangerine.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtility extends core.utility.DateTimeUtility {

	public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

	public static LocalDateTime fromBusinessDate(Long input) {
		if (input != null) {
			int businessDate = input.intValue();
			int year = businessDate / 10000;
			int month = (businessDate - (year * 10000)) / 100;
			int dayOfMonth = businessDate - (year * 10000) - (month * 100);
			return LocalDateTime.now(zoneOffset).withHour(0).withMinute(0).withSecond(0).withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);
		}
		return null;
	}

	public static LocalDateTime midnight() {
		return LocalDateTime.now(zoneOffset).withHour(0).withMinute(0).withSecond(0);
	}

	public static String formatTime(Long input) {
		if (input != null) {
			LocalDateTime time = toLocalDateTime(input);
			LocalDateTime midnight = midnight();
			if (time.isBefore(midnight)) {
				return dateTimeFormatter.format(time);
			} else {
				return timeFormatter.format(time);
			}
		}
		return "";
	}

	public static String formatBusinessDate(Long input) {
		if (input != null) {
			LocalDateTime time = fromBusinessDate(input);
			return dateFormatter.format(time);
		}
		return "";
	}

	public static Long toEpoch(LocalDateTime localDateTime) {
		if (localDateTime != null) {
			return localDateTime.toEpochSecond(zoneOffset);
		} else {
			return null;
		}
	}

}
