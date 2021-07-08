package tangerine.service;

import java.util.List;

import javax.inject.Inject;

import tangerine.mapper.AnswerMapper;
import tangerine.model.Answer;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class AnswerService extends GenericService {

	@Inject
	AnswerMapper answerMapper;

	@Inject
	UserService userService;

	@Transactional
	public void save(Answer answer) {
		answer.attachment();
		if (answer.getId() != null) {
			answerMapper.update(answer);
		} else {
			answerMapper.insert(answer);
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Answer> listUnshowed(Long conversationId, Long userId) {
		return answerMapper.listUnshowed(conversationId, userId);
	}

	@Transactional
	public void updateShowed(Long conversationId, Long userId, Long showed) {
		answerMapper.updateShowed(conversationId, userId, showed);
	}

	@Transactional(type = TransactionType.READONLY)
	public Answer find(Long id) {
		Answer answer = answerMapper.find(id);
		if (answer != null) {
			answer.attachmentList();
		}
		return answer;
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Answer> listByConversationId(Long questionId) {
		return forEach(answerMapper.listByConversationId(questionId), Answer::attachmentList);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Answer> listPageByConversationId(Long questionId, Integer page, Integer itemPerPage) {
		return forEach(answerMapper.listPageByConversationId(questionId, page, itemPerPage), Answer::attachmentList);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Answer> listLatestByQuestionId(Long questionId) {
		return answerMapper.listLatestByQuestionId(questionId);
	}

	@Transactional
	public void delete(Long id) {
		answerMapper.delete(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public Answer loadFind(Long id) {
		Answer answer = find(id);
		if (answer != null) {
			answer.setUser(userService.find(answer.getUserId()));
		}
		return answer;
	}

}
