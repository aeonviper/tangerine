package tangerine.service;

import java.util.List;

import javax.inject.Inject;

import tangerine.mapper.UserSubscriptionMapper;
import tangerine.model.UserSubscription;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class UserSubscriptionService extends GenericService {

	@Inject
	UserSubscriptionMapper userSubscriptionMapper;

	@Transactional
	public void save(UserSubscription userSubscription) {
		if (userSubscription.getId() != null) {
			userSubscriptionMapper.update(userSubscription);
		} else {
			userSubscriptionMapper.insert(userSubscription);
		}
	}

	@Transactional
	public void deleteNonRenewal(Long date) {
		userSubscriptionMapper.deleteNonRenewal(date);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<UserSubscription> listRenewal(Long date) {
		return userSubscriptionMapper.listRenewal(date);
	}

	@Transactional(type = TransactionType.READONLY)
	public UserSubscription find(Long id) {
		return userSubscriptionMapper.find(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<UserSubscription> listByUserId(Long userId) {
		return userSubscriptionMapper.listByUserId(userId);
	}

	@Transactional
	public void delete(Long id) {
		userSubscriptionMapper.delete(id);
	}

}
