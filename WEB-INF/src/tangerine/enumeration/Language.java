package tangerine.enumeration;

public enum Language {
	Indonesian("id"), //
	English("en");

	private String code;

	Language(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
