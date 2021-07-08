package tangerine.service;

import java.util.List;

import javax.inject.Inject;

import tangerine.mapper.DraftQuestionMapper;
import tangerine.model.Question;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class DraftQuestionService extends GenericService {

	@Inject
	DraftQuestionMapper draftQuestionMapper;

	@Inject
	UserService userService;

	@Transactional
	public void save(Question question) {
		question.attachment();
		if (question.getId() != null) {
			draftQuestionMapper.update(question);
		} else {
			draftQuestionMapper.insert(question);
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public Question find(Long id) {
		Question question = draftQuestionMapper.find(id);
		if (question != null) {
			question.attachmentList();
			question.createdTime();
		}
		return question;
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Question> listByUserId(Long userId) {
		return draftQuestionMapper.listByUserId(userId);
	}

	@Transactional
	public void delete(Long id) {
		draftQuestionMapper.delete(id);
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
