package tangerine.service;

import java.util.List;

import javax.inject.Inject;

import tangerine.mapper.ConversationMapper;
import tangerine.model.Conversation;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class ConversationService extends GenericService {

	@Inject
	ConversationMapper conversationMapper;

	@Transactional
	public void save(Conversation conversation) {
		if (conversation.getId() != null) {

		} else {
			conversationMapper.insert(conversation);
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public Conversation find(Long id) {
		return conversationMapper.find(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public Conversation findByQuestionIdUserId(Long questionId, Long userId) {
		return conversationMapper.findByQuestionIdUserId(questionId, userId);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<Conversation> listByQuestionId(Long questionId) {
		return conversationMapper.listByQuestionId(questionId);
	}

	@Transactional
	public void delete(Long id) {
		conversationMapper.delete(id);
	}

}
