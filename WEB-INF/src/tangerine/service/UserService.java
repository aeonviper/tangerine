package tangerine.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import tangerine.bean.Principal;
import tangerine.core.Utility;
import tangerine.mapper.UserMapper;
import tangerine.model.User;
import tangerine.model.UserSubscription;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class UserService extends GenericService {

	@Inject
	UserMapper userMapper;

	@Inject
	UserSubscriptionService userSubscriptionService;

	private static Random random = new SecureRandom();

	public static String generateTextId(final int LENGTH) {
		char[] universe = { //
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z', //
		// 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x', 'y', 'z', //
		'0', '2', '5', '6', '7', '8', '9' //
		};

		int i;
		char[] randomString = new char[LENGTH];

		for (i = 0; i < LENGTH; i++) {
			randomString[i] = universe[random.nextInt(universe.length)];
		}

		return new String(randomString);
	}

	@Transactional
	public void save(User user) {
		if (user.getId() != null) {
			userMapper.update(user);
		} else {
			userMapper.insert(user);
		}
	}

	@Transactional
	public void save(Principal principal) {
		if (principal != null) {
			User user = find(principal.getUserId());
			if (user != null) {
				principal.state();
				user.setState(Utility.gson.toJson(principal.getStateMap()));
				save(user);
			}
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public User find(Long id) {
		return userMapper.find(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public User findByTextId(String textId) {
		return userMapper.findByTextId(textId);
	}

	@Transactional(type = TransactionType.READONLY)
	public User findByEmail(String email) {
		return userMapper.findByEmail(email);
	}

	/* example */
	@Transactional(type = TransactionType.READONLY)
	public List<User> listByPage(String sortField, Boolean sortDirection, Integer itemPerPage, Integer page) {
		if (itemPerPage != null && itemPerPage > 0 && page != null && page > 0) {
			if (sortField == null || !Arrays.asList(new String[] { "id", "name", "email", "active", "storeCredit", "authenticationType", "subscriptionPackage" }).contains(sortField)) {
				sortField = "id";
			}
			return new ArrayList<>();
		} else {
			return new ArrayList<>();
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public List<User> listByQuestionId(Long conversationId) {
		return userMapper.listByQuestionId(conversationId);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<User> listByAnswerUserId(Long userId) {
		return userMapper.listByAnswerUserId(userId);
	}

	@Transactional
	public void delete(Long id) {
		userMapper.delete(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public User loadFind(Long id) {
		User user = find(id);
		if (user != null) {
			user.setSubscriptionList(userSubscriptionService.listByUserId(user.getId()));
		}
		return user;
	}

}
