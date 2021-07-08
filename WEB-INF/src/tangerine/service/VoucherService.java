package tangerine.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import tangerine.core.DateTimeUtility;
import tangerine.enumeration.LanguageKey;
import tangerine.enumeration.SubscriptionPackage;
import tangerine.enumeration.VoucherType;
import tangerine.mapper.VoucherMapper;
import tangerine.model.User;
import tangerine.model.UserSubscription;
import tangerine.model.UserVoucher;
import tangerine.model.Voucher;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;
import core.service.TransactionBundle;
import core.service.TransactionService;

@Singleton
public class VoucherService extends GenericService {

	@Inject
	VoucherMapper voucherMapper;

	@Inject
	private UserService userService;

	@Inject
	private UserSubscriptionService userSubscriptionService;

	@Inject
	private UserVoucherService userVoucherService;

	@Inject
	private VoucherService voucherService;

	@Inject
	private TransactionService transactionService;

	@Transactional(type = TransactionType.READONLY)
	public Voucher find(Long id) {
		return voucherMapper.find(id);
	}

	@Transactional(type = TransactionType.READONLY)
	public Voucher findByName(String name) {
		return voucherMapper.findByName(name);
	}

	public Boolean apply(Long userId, String voucherName, List<LanguageKey> notificationList) {
		User user = userService.find(userId);
		Voucher voucher = voucherService.findByName(voucherName);

		if (voucher != null) {
			Long currentBusinessDate = DateTimeUtility.toBusinessDate(DateTimeUtility.midnight());
			boolean valid = true;

			if (Boolean.TRUE.equals(voucher.getActive())) {
			} else {
				valid = false;
				notificationList.add(LanguageKey.voucherInactive);
			}

			if (voucher.getStart() != null) {
				if (currentBusinessDate >= voucher.getStart()) {
				} else {
					valid = false;
					notificationList.add(LanguageKey.voucherNotValid);
				}
			}

			if (voucher.getFinish() != null) {
				if (currentBusinessDate <= voucher.getFinish()) {
				} else {
					valid = false;
					notificationList.add(LanguageKey.voucherExpired);
				}
			}

			if (Boolean.TRUE.equals(voucher.getMultiple())) {
			} else {
				List<UserVoucher> userVoucherList = userVoucherService.listByUserIdVoucherId(user.getId(), voucher.getId());
				if (!userVoucherList.isEmpty()) {
					valid = false;
					notificationList.add(LanguageKey.voucherUsed);
				}
			}

			if (valid) {
				voucher.dataMap();

				transactionService.readWrite(new TransactionBundle() {
					public void execute() {
						if (VoucherType.Free == voucher.getVoucherType()) {
							VoucherType type = VoucherType.Free;
							Map<String, Object> dataMap = voucher.getDataMap();
							Long duration = type.duration(dataMap);
							SubscriptionPackage subscriptionPackage = type.subscriptionPackage(dataMap);

							Long now = DateTimeUtility.now();
							LocalDateTime midnight = DateTimeUtility.midnight();
							Long currentBusinessDate = DateTimeUtility.toBusinessDate(midnight);
							LocalDateTime expiration = midnight.plusDays(duration);
							Long expirationBusinessDate = DateTimeUtility.toBusinessDate(expiration);

							UserVoucher userVoucher = new UserVoucher();
							userVoucher.setUserId(user.getId());
							userVoucher.setVoucherId(voucher.getId());
							userVoucher.setDate(currentBusinessDate);
							userVoucher.setCreated(now);
							userVoucherService.save(userVoucher);

							UserSubscription userSubscription = new UserSubscription();
							userSubscription.setUserId(user.getId());
							userSubscription.setFromDate(currentBusinessDate);
							userSubscription.setToDate(expirationBusinessDate);
							userSubscription.setSubscriptionQuestion(subscriptionPackage.getTotalQuestion());
							userSubscription.setUsedQuestion(0);
							userSubscription.setSubscriptionPackage(subscriptionPackage);
							userSubscription.setRenewal(Integer.MAX_VALUE);
							userSubscription.setInformation("Voucher " + voucher.getName() + " applied on " + currentBusinessDate);
							userSubscription.setCreated(now);
							userSubscriptionService.save(userSubscription);
						}

					}
				});

				return true;

			} else {
				return false;
			}
		} else {
			notificationList.add(LanguageKey.voucherNonExistent);
		}

		return false;
	}

}
