package tangerine.model;

import java.util.List;

import core.model.GenericEntity;

public class Conversation implements GenericEntity<Long> {

	protected Long id;
	protected Long questionId;
	protected Long userId;

	// Transient
	protected User user;
	protected List<Answer> answerList;
	protected Answer latestAnswer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Answer getLatestAnswer() {
		return latestAnswer;
	}

	public void setLatestAnswer(Answer latestAnswer) {
		this.latestAnswer = latestAnswer;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}

}
