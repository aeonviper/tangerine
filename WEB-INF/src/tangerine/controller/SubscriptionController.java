package tangerine.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import tangerine.enumeration.SubscriptionPackage;
import tangerine.model.User;
import tangerine.model.UserExpense;
import tangerine.model.UserSubscription;
import tangerine.service.UserExpenseService;
import tangerine.service.UserService;
import tangerine.service.UserSubscriptionService;
import core.service.TransactionBundle;
import core.service.TransactionService;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.core.DateTimeUtility;

public class SubscriptionController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private UserSubscriptionService userSubscriptionService;

	@Inject
	private UserExpenseService userExpenseService;

	@Inject
	private TransactionService transactionService;

	private User user;
	private Long id;
	private Long date;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View buyStandard() {
		user = userService.find(principal.getUserId());
		if (user.getSubscriptionCredit() >= SubscriptionPackage.Standard.getPrice()) {

		} else {
			notification.addError(language(language, LanguageKey.insufficientCredit));
		}
		return new View(View.Type.FORWARD, "/WEB-INF/view/buy-standard.jsp");
	}

	public View buyGold() {
		user = userService.find(principal.getUserId());
		if (user.getSubscriptionCredit() >= SubscriptionPackage.Gold.getPrice()) {

		} else {
			notification.addError(language(language, LanguageKey.insufficientCredit));
		}
		return new View(View.Type.FORWARD, "/WEB-INF/view/buy-gold.jsp");
	}

	public View buyPlatinum() {
		user = userService.find(principal.getUserId());
		if (user.getSubscriptionCredit() >= SubscriptionPackage.Platinum.getPrice()) {

		} else {
			notification.addError(language(language, LanguageKey.insufficientCredit));
		}
		return new View(View.Type.FORWARD, "/WEB-INF/view/buy-platinum.jsp");
	}

	public View doBuySubscription(SubscriptionPackage subscriptionPackage) {
		user = userService.find(principal.getUserId());
		if (user.getSubscriptionCredit() >= subscriptionPackage.getPrice()) {
			transactionService.readWrite(new TransactionBundle() {
				public void execute() {
					Long now = DateTimeUtility.now();
					LocalDateTime midnight = DateTimeUtility.midnight();
					Long currentBusinessDate = DateTimeUtility.toBusinessDate(midnight);
					LocalDateTime expiration = midnight.plusMonths(1);
					Long expirationBusinessDate = DateTimeUtility.toBusinessDate(expiration);

					user.setSubscriptionCredit(user.getSubscriptionCredit() - subscriptionPackage.getPrice());
					userService.save(user);

					UserExpense userExpense = new UserExpense();
					userExpense.setUserId(user.getId());
					userExpense.setFromDate(currentBusinessDate);
					userExpense.setToDate(expirationBusinessDate);
					userExpense.setSubscriptionQuestion(subscriptionPackage.getTotalQuestion());
					userExpense.setSubscriptionPackage(subscriptionPackage);
					userExpense.setAmount(subscriptionPackage.getPrice());
					userExpense.setCreated(now);
					userExpenseService.save(userExpense);

					// set auto-renew
					UserSubscription userSubscription = new UserSubscription();
					userSubscription.setUserId(user.getId());
					userSubscription.setFromDate(currentBusinessDate);
					userSubscription.setToDate(expirationBusinessDate);
					userSubscription.setSubscriptionQuestion(subscriptionPackage.getTotalQuestion());
					userSubscription.setUsedQuestion(0);
					userSubscription.setSubscriptionPackage(subscriptionPackage);
					// read from request
					userSubscription.setRenewal(Integer.MAX_VALUE);
					userSubscription.setInformation("Bought on " + currentBusinessDate);
					userSubscription.setCreated(now);
					userSubscriptionService.save(userSubscription);
				}
			});
		}
		return new View(View.Type.REDIRECT, "/dashboard");
	}

	public View doBuyStandard() {
		return doBuySubscription(SubscriptionPackage.Standard);
	}

	public View doBuyGold() {
		return doBuySubscription(SubscriptionPackage.Gold);
	}

	public View doBuyPlatinum() {
		return doBuySubscription(SubscriptionPackage.Platinum);
	}

	public View doToggleRenewal() {
		user = userService.loadFind(principal.getUserId());
		for (UserSubscription userSubscription : user.getSubscriptionList()) {
			if (userSubscription.getId().equals(id)) {
				if (userSubscription.getRenewal() == null) {
					userSubscription.setRenewal(1);
				} else {
					userSubscription.setRenewal(null);
				}
				userSubscriptionService.save(userSubscription);
				break;
			}
		}
		return new View(View.Type.REDIRECT, "/dashboard");
	}

	public View subscriptionJob() {
		if (date == null) {
			date = DateTimeUtility.toBusinessDate(DateTimeUtility.midnight());
		}
		userSubscriptionService.deleteNonRenewal(date);
		List<UserSubscription> subscriptionList = userSubscriptionService.listRenewal(date);
		for (UserSubscription userSubscription : subscriptionList) {
			User user = userService.find(userSubscription.getUserId());
			SubscriptionPackage subscriptionPackage = userSubscription.getSubscriptionPackage();

			transactionService.readWrite(new TransactionBundle() {
				public void execute() {
					Long now = DateTimeUtility.now();
					Long currentBusinessDate = DateTimeUtility.toBusinessDate(now);

					while (userSubscription.getToDate() < currentBusinessDate) {
						if (user.getSubscriptionCredit() >= subscriptionPackage.getPrice()) {
							user.setSubscriptionCredit(user.getSubscriptionCredit() - subscriptionPackage.getPrice());
							userService.save(user);

							LocalDateTime expiration = DateTimeUtility.fromBusinessDate(userSubscription.getToDate());
							Long fromDate = DateTimeUtility.toBusinessDate(expiration.plusDays(1));
							Long toDate = DateTimeUtility.toBusinessDate(expiration.plusDays(1).plusMonths(1));

							UserExpense userExpense = new UserExpense();
							userExpense.setUserId(user.getId());
							userExpense.setFromDate(fromDate);
							userExpense.setToDate(toDate);
							userExpense.setSubscriptionQuestion(subscriptionPackage.getTotalQuestion());
							userExpense.setSubscriptionPackage(subscriptionPackage);
							userExpense.setAmount(subscriptionPackage.getPrice());
							userExpense.setCreated(now);
							userExpenseService.save(userExpense);

							userSubscription.setFromDate(fromDate);
							userSubscription.setToDate(toDate);
							userSubscription.setSubscriptionQuestion(subscriptionPackage.getTotalQuestion());
							userSubscription.setUsedQuestion(subscriptionPackage.getTotalQuestion());
							String information = Utility.strip(userSubscription.getInformation()) + " Renewed on " + DateTimeUtility.formatBusinessDate(DateTimeUtility.toBusinessDate(now));
							userSubscription.setInformation(information.trim());
							userSubscriptionService.save(userSubscription);

							notification.addNotice("User: " + user.getEmail() + " Credit: " + user.getSubscriptionCreditCurrency() + " Subscription: " + userSubscription.getSubscriptionPackage() + " - Renewed from " + userSubscription.getFromDate() + " to " + userSubscription.getToDate());
						} else {
							userSubscriptionService.delete(userSubscription.getId());
							notification.addNotice("User: " + user.getEmail() + " Credit: " + user.getSubscriptionCreditCurrency() + " Subscription: " + userSubscription.getSubscriptionPackage() + " - Not enough credit subscription not renewed");
							break;
						}
					}
				}
			});

		}
		return viewNotification;
	}

	public User getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
