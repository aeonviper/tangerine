package tangerine.service;

import java.util.List;

import javax.inject.Inject;

import tangerine.mapper.UserExpenseMapper;
import tangerine.model.UserExpense;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class UserExpenseService extends GenericService {

	@Inject
	UserExpenseMapper userExpenseMapper;

	@Transactional
	public void save(UserExpense userExpense) {
		if (userExpense.getId() != null) {
			userExpenseMapper.update(userExpense);
		} else {
			userExpenseMapper.insert(userExpense);
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public UserExpense find(Long id) {
		return userExpenseMapper.find(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<UserExpense> listByUserId(Long userId) {
		return userExpenseMapper.listByUserId(userId);
	}

	@Transactional
	public void delete(Long id) {
		userExpenseMapper.delete(id);
	}

}
