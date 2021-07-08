package tangerine.model;

import tangerine.enumeration.SubscriptionPackage;
import core.model.GenericEntity;

public class UserSubscription implements GenericEntity<Long> {

	protected Long id;
	protected Long userId;
	protected Long fromDate;
	protected Long toDate;
	protected Integer subscriptionQuestion;
	protected Integer usedQuestion;
	protected SubscriptionPackage subscriptionPackage;
	protected Integer renewal;
	protected String information;
	protected Long created;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public SubscriptionPackage getSubscriptionPackage() {
		return subscriptionPackage;
	}

	public void setSubscriptionPackage(SubscriptionPackage subscriptionPackage) {
		this.subscriptionPackage = subscriptionPackage;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public Long getToDate() {
		return toDate;
	}

	public void setToDate(Long toDate) {
		this.toDate = toDate;
	}

	public Integer getUsedQuestion() {
		return usedQuestion;
	}

	public void setUsedQuestion(Integer usedQuestion) {
		this.usedQuestion = usedQuestion;
	}

	public Integer getRenewal() {
		return renewal;
	}

	public void setRenewal(Integer renewal) {
		this.renewal = renewal;
	}

	public Integer getSubscriptionQuestion() {
		return subscriptionQuestion;
	}

	public void setSubscriptionQuestion(Integer subscriptionQuestion) {
		this.subscriptionQuestion = subscriptionQuestion;
	}

}
