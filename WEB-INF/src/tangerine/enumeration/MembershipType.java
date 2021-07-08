package tangerine.enumeration;


public enum MembershipType {

	Learner("Student"), //
	Educator("Tutor");

	private String display;

	MembershipType(String display) {
		this.display = display;
	}

	public String getName() {
		return this.name();
	}

	public String getDisplay() {
		return display;
	}

}
