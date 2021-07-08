package tangerine.service;

import java.util.List;

import javax.inject.Inject;

import tangerine.mapper.UserVoucherMapper;
import tangerine.model.UserVoucher;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class UserVoucherService extends GenericService {

	@Inject
	UserVoucherMapper userVoucherMapper;

	@Transactional
	public void save(UserVoucher userVoucher) {
		if (userVoucher.getId() != null) {
			userVoucherMapper.update(userVoucher);
		} else {
			userVoucherMapper.insert(userVoucher);
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public UserVoucher find(Long id) {
		return userVoucherMapper.find(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<UserVoucher> listByUserId(Long userId) {
		return userVoucherMapper.listByUserId(userId);
	}

	@Transactional(type = TransactionType.READONLY)
	public List<UserVoucher> listByUserIdVoucherId(Long userId, Long voucherId) {
		return userVoucherMapper.listByUserIdVoucherId(userId, voucherId);
	}

	@Transactional
	public void delete(Long id) {
		userVoucherMapper.delete(id);
	}

}
