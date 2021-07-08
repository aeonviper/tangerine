package tangerine.enumeration;

public enum ExplanationType {
	ExplanationOnly("Explanation Only"), //
	ExplanationAndAnswer("Explanation and Answer");

	private String display;

	ExplanationType(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}

}
