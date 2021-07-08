package tangerine.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import tangerine.core.Utility;
import tangerine.enumeration.MembershipType;
import tangerine.mapper.ShowMapper;
import tangerine.model.Answer;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class ShowService extends GenericService {

	@Inject
	ShowMapper showMapper;

	@Transactional(type = TransactionType.READONLY)
	public List<Map> query(MembershipType membershipType, Long userId) {
		List<Map> list = new ArrayList<>();
		List<Map> databaseList = Collections.emptyList();
		if (MembershipType.Learner == membershipType) {
			databaseList = showMapper.learnerQuery(userId);
		} else if (MembershipType.Educator == membershipType) {
			databaseList = showMapper.educatorQuery(userId);
		}

		for (Map entry : databaseList) {
			Map map = new HashMap<>();
			map.put("questionId", entry.get("questionid"));
			map.put("conversationId", entry.get("conversationid"));
			Answer answer = new Answer();
			answer.setId((Long) entry.get("id"));
			answer.setConversationId((Long) entry.get("conversationid"));
			answer.setText((String) entry.get("text"));
			answer.setUserId((Long) entry.get("userid"));
			answer.setAttachment((String) entry.get("attachment"));			
			answer.setCreated((Long) entry.get("created"));
			answer.attachmentList();
			map.put("answer", answer);
			list.add(map);
		}
		return list;
	}

	public Map summary(MembershipType membershipType, Long userId) {
		List<Map> list = query(membershipType, userId);
		Map<String, Object> map = new HashMap<>();
		map.put("total", list.size());
		return map;
	}

	public Map questionList(MembershipType membershipType, Long userId) {
		List<Map> list = query(membershipType, userId);
		Map<String, Object> map = new HashMap<>();
		map.put("total", list.size());

		Map<String, Object> questionMap = new HashMap<>();
		for (Map entry : list) {
			String questionId = ((Long) entry.get("questionId")).toString();
			Integer total = (Integer) questionMap.get(questionId);
			if (total == null) {
				questionMap.put(questionId, total = 0);
			}
			questionMap.put(questionId, ++total);
		}

		List<Map> questionList = new ArrayList<>();
		for (Map.Entry entry : questionMap.entrySet()) {
			Map<String, Object> element = new HashMap<>();
			element.put("id", entry.getKey());
			element.put("total", entry.getValue());
			questionList.add(element);
		}
		map.put("questionList", questionList);

		return map;
	}

	public Map conversationList(MembershipType membershipType, Long userId, Long questionId) {
		List<Map> list = query(membershipType, userId);
		Map<String, Object> map = new HashMap<>();
		map.put("total", list.size());

		Map<String, Object> conversationMap = new HashMap<>();
		Map<String, Object> attributeMap = new HashMap<>();
		for (Map entry : list) {
			if (questionId.equals((Long) entry.get("questionId"))) {
				String conversationId = ((Long) entry.get("conversationId")).toString();
				Integer total = (Integer) conversationMap.get(conversationId);
				if (total == null) {
					conversationMap.put(conversationId, total = 0);
				}
				conversationMap.put(conversationId, ++total);
				Answer answer = (Answer) entry.get("answer");
				attributeMap.put("lastAnswerText" + conversationId, Utility.ellipsis(answer.getText()));
				attributeMap.put("userName" + conversationId, answer.getUserId());
			}
		}

		List<Map> conversationList = new ArrayList<>();
		for (Map.Entry entry : conversationMap.entrySet()) {
			Map<String, Object> element = new HashMap<>();
			element.put("id", entry.getKey());
			element.put("total", entry.getValue());
			element.put("lastAnswerText", attributeMap.get("lastAnswerText" + entry.getKey()));
			element.put("userName", attributeMap.get("userName" + entry.getKey()));
			conversationList.add(element);
		}
		map.put("conversationList", conversationList);

		return map;
	}

	public Map answerList(MembershipType membershipType, Long userId) {
		List<Map> list = query(membershipType, userId);
		Map<String, Object> map = new HashMap<>();
		map.put("total", list.size());
		return map;
	}
}
