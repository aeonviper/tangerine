package tangerine.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import tangerine.core.Constant;
import tangerine.core.Utility;
import core.model.GenericEntity;
import tangerine.core.DateTimeUtility;

public class Answer implements GenericEntity<Long> {

	private static Type typeAttachmentList = new TypeToken<List<Attachment>>() {
	}.getType();

	protected Long id;
	protected Long conversationId;
	protected Long userId;
	protected String text;

	protected String attachment;

	protected Long created;
	protected Long showed;

	// Transient
	protected User user;
	protected List<Attachment> attachmentList;
	protected String createdTime;

	public void attachmentList() {
		if (attachment != null && !attachment.isEmpty()) {
			attachmentList = Utility.gson.fromJson(attachment, typeAttachmentList);
		} else {
			attachmentList = Collections.emptyList();
		}
	}

	public void attachment() {
		attachment = null;
		if (attachmentList != null && !attachmentList.isEmpty()) {
			attachment = Utility.gson.toJson(attachmentList);
		}
	}

	public boolean isDeletable(Long principalUserId) {
		if (created != null && userId.equals(principalUserId)) {
			return (DateTimeUtility.now() - created) <= Constant.timeThreshold;
		}
		return false;
	}

	public String createdTime() {
		return createdTime = DateTimeUtility.formatTime(created);
	}

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getConversationId() {
		return conversationId;
	}

	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getShowed() {
		return showed;
	}

	public void setShowed(Long showed) {
		this.showed = showed;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

}
