package tangerine.enumeration;

public enum EmailSender {

	Account("account@tanyatutor.com", "TanyaTutor.com (Account)"), //		
	Support("support@tanyatutor.com", "TanyaTutor.com (Support)"); //

	private String address;
	private String display;

	EmailSender(String address, String display) {
		this.display = display;
		this.address = address;
	}

	public String getName() {
		return this.name();
	}

	public String getAddress() {
		return address;
	}

	public String getDisplay() {
		return display;
	}

}
