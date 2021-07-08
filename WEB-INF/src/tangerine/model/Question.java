package tangerine.model;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import tangerine.core.Constant;
import tangerine.core.DateTimeUtility;
import tangerine.core.Utility;
import tangerine.enumeration.Category;
import tangerine.enumeration.ExplanationType;
import tangerine.enumeration.Level;

import com.google.gson.reflect.TypeToken;

import core.model.GenericEntity;

public class Question implements GenericEntity<Long> {

	private static Type typeAttachmentList = new TypeToken<List<Attachment>>() {
	}.getType();

	protected Long id;
	protected Long userId;
	protected String text;

	protected String attachment;

	protected ExplanationType explanationType;
	protected Boolean active;
	protected Long created;
	protected Long edited;
	protected Long closed;

	protected Category category;
	protected Level level;

	// Transient
	protected User user;
	protected List<Conversation> conversationList;
	protected List<Attachment> attachmentList;
	protected String createdTime;
	protected Integer conversationSize;

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

	public String createdTime() {
		return createdTime = DateTimeUtility.formatTime(created);
	}

	public boolean isDeletable(Long principalUserId) {
		if (created != null && userId.equals(principalUserId)) {
			return (DateTimeUtility.now() - created) <= Constant.timeThreshold;
		}
		return false;
	}

	public boolean isCloseable(Long principalUserId) {
		if (closed == null && userId.equals(principalUserId)) {
			return true;
		}
		return false;
	}

	public boolean isOpenable(Long principalUserId) {
		if (closed != null && userId.equals(principalUserId)) {
			return true;
		}
		return false;
	}

	public boolean isOpen() {
		return closed == null;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getEdited() {
		return edited;
	}

	public void setEdited(Long edited) {
		this.edited = edited;
	}

	public Long getClosed() {
		return closed;
	}

	public void setClosed(Long closed) {
		this.closed = closed;
	}

	public ExplanationType getExplanationType() {
		return explanationType;
	}

	public void setExplanationType(ExplanationType explanationType) {
		this.explanationType = explanationType;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Conversation> getConversationList() {
		return conversationList;
	}

	public void setConversationList(List<Conversation> conversationList) {
		this.conversationList = conversationList;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
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

	public Integer getConversationSize() {
		return conversationSize;
	}

	public void setConversationSize(Integer conversationSize) {
		this.conversationSize = conversationSize;
	}

}
