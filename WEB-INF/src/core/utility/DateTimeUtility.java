package core.utility;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtility {

	public static final ZoneOffset zoneOffset = ZoneOffset.ofHours(7);
	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy h:mm a");

	public static Long now() {
		return LocalDateTime.now(zoneOffset).toEpochSecond(zoneOffset);
	}

	public static LocalDateTime toLocalDateTime(Long epochSecond) {
		return LocalDateTime.ofEpochSecond(epochSecond, 0, zoneOffset);
	}

	public static Long toBusinessDate(LocalDateTime localDateTime) {
		return new Long((localDateTime.getYear() * 10000) + (localDateTime.getMonthValue() * 100) + (localDateTime.getDayOfMonth()));
	}

	public static Long toBusinessDate(Long epochSecond) {
		LocalDateTime localDateTime = toLocalDateTime(epochSecond);
		return new Long((localDateTime.getYear() * 10000) + (localDateTime.getMonthValue() * 100) + (localDateTime.getDayOfMonth()));
	}

}
