package tangerine.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum Category {
	English("English", "Inggris"), //
	Mathematics("Mathematics", "Matematika"), //
	Physics("Physics", "Fisika");

	private String english;
	private String indonesian;

	Category(String english, String indonesian) {
		this.english = english;
		this.indonesian = indonesian;
	}

	public final static List<String> stringList;
	public final static Set<Category> enumerationSet;
	public final static List<Category> enumerationList = Collections.unmodifiableList(Arrays.asList(Category.values()));

	static {
		List<String> sList = new ArrayList<>();
		Set<Category> enumSet = new HashSet<>();
		for (Category category : Category.values()) {
			sList.add(category.name());
			enumSet.add(category);
		}
		stringList = Collections.unmodifiableList(sList);
		enumerationSet = Collections.unmodifiableSet(enumSet);
	}

	public String getDisplay(Language language) {
		switch (language) {
		case English:
			return english;
		case Indonesian:
			return indonesian;
		default:
			return "";
		}
	}

	public String getEnglish() {
		return english;
	}

	public String getIndonesian() {
		return indonesian;
	}

}
