package tangerine.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tangerine.core.Utility;
import tangerine.enumeration.MembershipType;

import com.google.gson.reflect.TypeToken;

import core.model.GenericEntity;

public class User implements GenericEntity<Long> {

	private static Type typeStringStringSetMap = new TypeToken<Map<String, Set<String>>>() {
	}.getType();

	protected Long id;
	protected String textId;
	protected String name;
	protected String email;
	protected String emailNew;
	protected String password;
	protected MembershipType membershipType;
	protected Long subscriptionCredit;
	protected String phone;
	protected String school;
	protected String information;
	protected Boolean active;
	protected String state;
	protected Long created;

	// Transient
	protected List<UserSubscription> subscriptionList;

	// Transient
	protected Map<String, Object> stateMap;

	public void stateMap() {
		if (state != null && !state.isEmpty()) {
			stateMap = Utility.gson.fromJson(state, typeStringStringSetMap);
		} else {
			stateMap = Collections.emptyMap();
		}
	}

	public void state() {
		state = null;
		if (stateMap != null && !stateMap.isEmpty()) {
			state = Utility.gson.toJson(stateMap);
		}
	}

	public String getSubscriptionCreditCurrency() {
		return Utility.numberFormatCurrency.format(subscriptionCredit).toString();
	}

	public boolean isLearner() {
		return MembershipType.Learner == membershipType;
	}

	public boolean isEducator() {
		return MembershipType.Educator == membershipType;
	}

	public User() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailNew() {
		return emailNew;
	}

	public void setEmailNew(String emailNew) {
		this.emailNew = emailNew;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public MembershipType getMembershipType() {
		return membershipType;
	}

	public void setMembershipType(MembershipType membershipType) {
		this.membershipType = membershipType;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTextId() {
		return textId;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getSubscriptionCredit() {
		return subscriptionCredit;
	}

	public void setSubscriptionCredit(Long subscriptionCredit) {
		this.subscriptionCredit = subscriptionCredit;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public List<UserSubscription> getSubscriptionList() {
		return subscriptionList;
	}

	public void setSubscriptionList(List<UserSubscription> subscriptionList) {
		this.subscriptionList = subscriptionList;
	}

	public Map<String, Object> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, Object> stateMap) {
		this.stateMap = stateMap;
	}

}
