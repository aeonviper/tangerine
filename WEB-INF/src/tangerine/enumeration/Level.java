package tangerine.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Level {
	One("1 (SD)"), //
	Two("2 (SD)"), //
	Three("3 (SD)"), //
	Four("4 (SD)"), //
	Five("5 (SD)"), //
	Six("6 (SD)"), //
	Seven("7 (SMP)"), //
	Eight("8 (SMP)"), //
	Nine("9 (SMP)"), //
	Ten("10 (SMA)"), //
	Eleven("11 (SMA)"), //
	Twelve("12 (SMA)");

	private String display;

	Level(String display) {
		this.display = display;
	}

	public final static List<String> stringList;
	public final static Set<Level> enumerationSet;
	public final static List<Level> enumerationList = Collections.unmodifiableList(Arrays.asList(Level.values()));

	static {
		List<String> sList = new ArrayList<>();
		Set<Level> enumSet = new HashSet<>();
		for (Level level : Level.values()) {
			sList.add(level.name());
			enumSet.add(level);
		}
		stringList = Collections.unmodifiableList(sList);
		enumerationSet = Collections.unmodifiableSet(enumSet);
	}

	public String getDisplay() {
		return display;
	}

}
