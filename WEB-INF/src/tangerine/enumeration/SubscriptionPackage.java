package tangerine.enumeration;

public enum SubscriptionPackage {

	Standard("Standard Package", 200000L, 30), //
	Gold("Gold Package", 500000L, 60), //
	Platinum("Platinum Package", 1000000L, 9000);

	private String display;
	private Long price;
	private Integer totalQuestion;

	SubscriptionPackage(String display, Long price, Integer totalQuestion) {
		this.display = display;
		this.price = price;
		this.totalQuestion = totalQuestion;
	}

	public String getName() {
		return this.name();
	}

	public String getDisplay() {
		return display;
	}

	public Long getPrice() {
		return price;
	}

	public Integer getTotalQuestion() {
		return totalQuestion;
	}

}
