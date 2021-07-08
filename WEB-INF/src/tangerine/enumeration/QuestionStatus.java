package tangerine.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum QuestionStatus {
	Open("Open"), //
	Closed("Closed");

	private String display;

	QuestionStatus(String display) {
		this.display = display;
	}

	public final static List<String> stringList;
	public final static Set<QuestionStatus> enumerationSet;
	public final static List<QuestionStatus> enumerationList = Collections.unmodifiableList(Arrays.asList(QuestionStatus.values()));

	static {
		List<String> sList = new ArrayList<>();
		Set<QuestionStatus> enumSet = new HashSet<>();
		for (QuestionStatus questionStatus : QuestionStatus.values()) {
			sList.add(questionStatus.name());
			enumSet.add(questionStatus);
		}
		stringList = Collections.unmodifiableList(sList);
		enumerationSet = Collections.unmodifiableSet(enumSet);
	}

	public String getDisplay() {
		return display;
	}

}
