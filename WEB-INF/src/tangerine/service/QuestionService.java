package tangerine.service;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import tangerine.enumeration.Category;
import tangerine.enumeration.Level;
import tangerine.enumeration.QuestionStatus;
import tangerine.mapper.QuestionMapper;
import tangerine.model.Question;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class QuestionService extends GenericService {

	@Inject
	QuestionMapper questionMapper;

	@Inject
	UserService userService;

	@Transactional
	public void save(Question question) {
		question.attachment();
		if (question.getId() != null) {
			questionMapper.update(question);
		} else {
			questionMapper.insert(question);
		}
	}

	@Transactional
	public void copy(Question question) {
		questionMapper.copy(question);
	}

	@Transactional(type = TransactionType.READONLY)
	public Question find(Long id) {
		Question question = questionMapper.find(id);
		if (question != null) {
			question.attachmentList();
			question.createdTime();
		}
		return question;
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Question> listPageByUserId(Long userId, Integer page, Integer itemPerPage) {
		return forEach(questionMapper.listPageByUserId(userId, page, itemPerPage), Question::createdTime);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Question> listPageActiveOrderByCreated(Set<QuestionStatus> questionStatusSet, Set<Category> categorySet, Set<Level> levelSet, Integer page, Integer itemPerPage) {
		return forEach(questionMapper.listPageActiveOrderByCreated(questionStatusSet, categorySet, levelSet, page, itemPerPage), Question::createdTime);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Question> listPageAnsweredByUserId(Long userId, Integer page, Integer itemPerPage) {
		return forEach(questionMapper.listPageAnsweredByUserId(userId, page, itemPerPage), Question::createdTime);
	}

	@Transactional
	public void delete(Long id) {
		questionMapper.delete(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public Question loadFind(Long id) {
		Question question = find(id);
		if (question != null) {
			question.setUser(userService.find(question.getUserId()));
		}
		return question;
	}

}
