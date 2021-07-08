package tangerine.model;

import tangerine.enumeration.VerificationType;
import core.model.GenericEntity;

public class Verification implements GenericEntity<Long> {

	protected Long id;
	protected String textId;
	protected String code;
	protected Long userId;
	protected VerificationType type;
	protected String value;
	protected Long created;

	protected User user;

	public Verification() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTextId() {
		return textId;
	}

	public void setTextId(String textId) {
		this.textId = textId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public VerificationType getType() {
		return type;
	}

	public void setType(VerificationType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
