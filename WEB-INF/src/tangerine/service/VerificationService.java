package tangerine.service;

import java.security.SecureRandom;
import java.util.Random;

import javax.inject.Inject;

import tangerine.enumeration.VerificationType;
import tangerine.mapper.VerificationMapper;
import tangerine.model.User;
import tangerine.model.Verification;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class VerificationService extends GenericService {

	@Inject
	VerificationMapper verificationMapper;

	@Inject
	UserService userService;

	private static Random random = new SecureRandom();

	public static String randomCode(final int LENGTH) {
		char[] universe = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

		int i;
		char[] randomString = new char[LENGTH];

		for (i = 0; i < LENGTH; i++) {
			randomString[i] = universe[random.nextInt(universe.length)];
		}

		return new String(randomString);
	}

	@Transactional
	public void save(Verification verification) {
		if (verification.getId() != null) {

		} else {
			verificationMapper.insert(verification);
		}
	}

	@Transactional(type = TransactionType.READONLY)
	public Verification findByTextId(String textId) {
		return verificationMapper.findByTextId(textId);
	}

	@Transactional(type = TransactionType.READONLY)
	public Verification findByUserIdCode(Long userId, String code) {
		return verificationMapper.findByUserIdCode(userId, code);
	}

	@Transactional
	public void delete(Long id) {
		verificationMapper.delete(id);
	}

	@Transactional
	public void deleteByTypeUserId(VerificationType type, Long userId) {
		verificationMapper.deleteByTypeUserId(type, userId);
	}

	public boolean verify(Verification verification) {
		if (verification != null) {
			if (VerificationType.MembershipActivation == verification.getType()) {
				User user = userService.find(verification.getUserId());
				user.setActive(true);
				userService.save(user);
			} else if (VerificationType.NewEmailConfirmation == verification.getType()) {
				User user = userService.find(verification.getUserId());
				if (user.getEmailNew() != null) {
					user.setEmail(user.getEmailNew());
					user.setEmailNew(null);
					userService.save(user);
				}
			} else if (VerificationType.PasswordReset == verification.getType()) {

			}
			delete(verification.getId());
			return true;
		}
		return false;
	}
}
