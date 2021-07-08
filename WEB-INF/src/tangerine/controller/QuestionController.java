package tangerine.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import core.utility.DateTimeUtility;
import core.utility.Logger;
import core.validation.field.RequiredField;
import core.validation.field.RequiredStringField;
import tangerine.model.Attachment;
import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.bean.Principal;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.EmailSender;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import tangerine.enumeration.MembershipType;
import tangerine.enumeration.Page;
import tangerine.enumeration.QuestionStatus;
import tangerine.model.Answer;
import tangerine.model.Conversation;
import tangerine.model.Question;
import tangerine.model.User;
import tangerine.model.UserSubscription;
import tangerine.service.AnswerService;
import tangerine.service.ConversationService;
import tangerine.service.DraftQuestionService;
import tangerine.service.EmailService;
import tangerine.service.ShowService;
import tangerine.service.QuestionService;
import tangerine.service.SequenceService;
import tangerine.service.UserService;
import tangerine.service.UserSubscriptionService;
import tangerine.websocket.Channel;

public class QuestionController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private DraftQuestionService draftQuestionService;

	@Inject
	private QuestionService questionService;

	@Inject
	private ConversationService conversationService;

	@Inject
	private AnswerService answerService;

	@Inject
	private EmailService emailService;

	@Inject
	private UserSubscriptionService userSubscriptionService;

	@Inject
	private ShowService showService;

	@Inject
	private SequenceService sequenceService;

	private Long id;
	private Long answerUserId;
	private String channelId;
	private User otherUser;

	private Question question = new Question();

	public static final int maxAttachment = 10;

	private orion.controller.Attachment[] fileArray = new orion.controller.Attachment[maxAttachment];
	private String[] fileDescriptionArray = new String[maxAttachment];

	private List<Conversation> conversationList = Collections.emptyList();
	private List<Answer> answerList = Collections.emptyList();

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View view() {
		Map<Long, User> userMap = new HashMap<>();

		question = questionService.loadFind(id);

		if (question == null) {
			notification.addError(language(language, LanguageKey.questionNonExistent));
			return viewNotification;
		}

		if (isLearner()) {
			if (principal.getUserId().equals(question.getUserId())) {
			} else {
				notification.addError(language(language, LanguageKey.authorisationDenied));
				return viewNotification;
			}
		} else if (isEducator()) {
			notification.addError(language(language, LanguageKey.authorisationDenied));
			return viewNotification;
		}

		userMap.put(question.getUser().getId(), question.getUser());
		conversationList = conversationService.listByQuestionId(question.getId());
		Map<Long, Conversation> conversationMap = conversationService.mapify(conversationList);
		userMap.putAll(userService.mapify(userService.listByQuestionId(question.getId())));
		for (Conversation conversation : conversationList) {
			conversation.setUser(userMap.get(conversation.getUserId()));
		}
		for (Answer answer : answerService.listLatestByQuestionId(question.getId())) {
			conversationMap.get(answer.getConversationId()).setLatestAnswer(answer);
		}
		return new View(View.Type.FORWARD, "/WEB-INF/view/conversation-list.jsp");
	}

	public View viewConversation() {

		question = questionService.loadFind(id);

		if (question == null) {
			notification.addError(language(language, LanguageKey.questionNonExistent));
			return viewNotification;
		}

		Conversation conversation = conversationService.findByQuestionIdUserId(question.getId(), answerUserId);
		if (conversation == null) {
			conversation = new Conversation();
			conversation.setUserId(answerUserId);
		}

		if (isLearner()) {
			if (principal.getUserId().equals(question.getUserId())) {
			} else {
				notification.addError(language(language, LanguageKey.authorisationDenied));
				return viewNotification;
			}
		} else if (isEducator()) {
			if (principal.getUserId().equals(conversation.getUserId())) {
			} else {
				notification.addError(language(language, LanguageKey.authorisationDenied));
				return viewNotification;
			}
		}

		Long now = DateTimeUtility.now();
		Long otherUserId = question.getUserId();
		if (principal.getUserId().equals(question.getUserId())) {
			otherUserId = answerUserId;
		}
		otherUser = userService.find(otherUserId);

		List<Long> recentlyShowedList = new ArrayList<>();
		for (Answer answer : answerService.listUnshowed(conversation.getId(), otherUserId)) {
			recentlyShowedList.add(answer.getId());
		}

		answerService.updateShowed(conversation.getId(), otherUserId, now);

		List<Channel> channelList = Channel.listChannel(question.getUserId(), answerUserId);
		for (Channel channel : channelList) {
			if (!channel.getChannelId().equals(channelId)) {
				Map map = new HashMap<>();
				Page page = channel.getPage();
				if (page == Page.Summary) {
					map = showService.summary(channel.getMembershipType(), channel.getUserId());
				} else if (page == Page.QuestionList) {
					map = showService.questionList(channel.getMembershipType(), channel.getUserId());
				} else if (page == Page.ConversationList) {
					map = showService.conversationList(channel.getMembershipType(), channel.getUserId(), question.getId());
				} else if (page == Page.AnswerList) {
					map = showService.answerList(channel.getMembershipType(), channel.getUserId());
					map.put("recentlyShowedList", recentlyShowedList);
				}
				channel.send(Utility.gson.toJson(map));
			}
		}

		answerList = answerService.listByConversationId(conversation.getId());

		return new View(View.Type.FORWARD, "/WEB-INF/view/answer-list.jsp");
	}

	public View ask() {
		question.setUserId(principal.getUserId());
		question.setCreated(DateTimeUtility.now());
		question.setAttachmentList(new ArrayList<>());
		draftQuestionService.save(question);
		return new View(View.Type.FORWARD, "/WEB-INF/view/question-ask.jsp");
	}

	public View inquire() {
		return new View(View.Type.FORWARD, "/WEB-INF/view/question-inquire.jsp");
	}

	public View doUpload() {
		if (!( //
		validateRequired(new RequiredField("id", id)) //
		)) {
			return viewNotification;
		}

		question.setId(id);

		question = draftQuestionService.find(question.getId());
		if (question.getAttachmentList().isEmpty()) {
			// change to proper ArrayList
			question.setAttachmentList(new ArrayList<>());
		}

		User user = userService.loadFind(principal.getUserId());

		String fileSystemPath = Constant.fileSystemPathAsset + File.separator + "question" + File.separator + "attachment";
		String fileSystemUserPath = user.getTextId() + File.separator + question.getId() + File.separator;
		String webUserPath = user.getTextId() + "/" + question.getId() + "/";
		String attachmentFileName;
		String fileName;

		new File(fileSystemPath + File.separator + fileSystemUserPath).mkdirs();

		int i = 0;
		orion.controller.Attachment file = fileArray[i];
		// String fileDescription = fileDescriptionArray[i];

		if (file != null) {
			fileName = fileSystemPath + File.separator + fileSystemUserPath + (attachmentFileName = sequenceService.sequenceAttachment() + "_" + Utility.fileName(file.getName()));
			file.accept(new File(fileName));

			Attachment attachment = new Attachment();
			attachment.setId(sequenceService.sequenceAttachment());
			attachment.setPath(webUserPath + attachmentFileName);
			// attachment.setDescription(fileDescription);
			question.getAttachmentList().add(attachment);
		}

		draftQuestionService.save(question);

		return new View(View.Type.JSON, "ok");
	}

	public View doInquire() {

		if (!( //
		validateRequired(new RequiredField("question", question)) //
		&& validateRequiredString( //
		new RequiredStringField("question.text", question.getText()) //
		) //
		)) {
			return viewNotification;
		}

		Utility.nullIfBlank(question, "text");
		Utility.stripText(question, "text");
		Utility.sanitise(question, "text");

		for (int i = 0; i < maxAttachment; i++) {
			fileDescriptionArray[i] = Utility.nullIfBlank(fileDescriptionArray[i]);
			fileDescriptionArray[i] = Utility.stripText(fileDescriptionArray[i]);
			fileDescriptionArray[i] = Utility.sanitise(fileDescriptionArray[i]);
		}

		User user = userService.loadFind(principal.getUserId());

		int remaining = 0;
		// check dates
		for (UserSubscription userSubscription : user.getSubscriptionList()) {
			remaining += (userSubscription.getSubscriptionQuestion() - userSubscription.getUsedQuestion());
		}

		if (!(remaining > 0)) {
			notification.addError(language(language, LanguageKey.questionQuotaUsed));
			return viewNotification;
		}

		String fileSystemPath = Constant.fileSystemPathAsset + File.separator + "question" + File.separator + "attachment";
		String fileSystemUserPath = user.getTextId() + File.separator;
		String webUserPath = user.getTextId() + "/";
		int webUserPathLength = webUserPath.length();
		String attachmentFileName;
		String fileName;

		Long yesterday = DateTimeUtility.now() - (24 * 60 * 60);

		for (Question draftQuestion : draftQuestionService.forEach(draftQuestionService.listByUserId(user.getId()), Question::attachmentList).stream().filter(q -> q.getCreated() < yesterday).collect(Collectors.toList())) {
			if (draftQuestion.getCreated() < yesterday) {
				for (Attachment attachment : draftQuestion.getAttachmentList()) {
					if (attachment.getPath().startsWith(webUserPath)) {
						String path = attachment.getPath().substring(webUserPathLength);
						File file = new File(fileSystemPath + File.separator + fileSystemUserPath + path);
						if (file.exists()) {
							file.delete();
						}
					}
				}
				draftQuestionService.delete(draftQuestion.getId());
			}
		}

		question.setUserId(principal.getUserId());
		question.setCreated(DateTimeUtility.now());
		draftQuestionService.save(question);

		fileSystemUserPath = user.getTextId() + File.separator + question.getId() + File.separator;
		webUserPath = user.getTextId() + "/" + question.getId() + "/";

		new File(fileSystemPath + File.separator + fileSystemUserPath).mkdirs();

		question.setAttachmentList(new ArrayList<>());
		for (int i = 0; i < maxAttachment; i++) {
			orion.controller.Attachment file = fileArray[i];
			String fileDescription = fileDescriptionArray[i];

			if (file != null) {
				fileName = fileSystemPath + File.separator + fileSystemUserPath + (attachmentFileName = sequenceService.sequenceAttachment() + "_" + Utility.fileName(file.getName()));
				file.accept(new File(fileName));

				Attachment attachment = new Attachment();
				attachment.setId(sequenceService.sequenceAttachment());
				attachment.setPath(webUserPath + attachmentFileName);
				attachment.setDescription(fileDescription);
				question.getAttachmentList().add(attachment);
			}
		}

		draftQuestionService.save(question);
		question.createdTime();

		return new View(View.Type.FORWARD, "/WEB-INF/view/question-review.jsp");
	}

	public View doAsk() {

		if (!( //
		validateRequired( //
		new RequiredField("question", question), //
		new RequiredField("question.id", question.getId()) //
		) //
		&& validateRequiredString( //
		new RequiredStringField("question.text", question.getText()) //
		) //
		)) {
			return viewNotification;
		}

		Utility.nullIfBlank(question, "text");
		Utility.stripText(question, "text");
		Utility.sanitise(question, "text");

		// for (int i = 0; i < maxAttachment; i++) {
		// fileDescriptionArray[i] = Utility.nullIfBlank(fileDescriptionArray[i]);
		// fileDescriptionArray[i] = Utility.stripText(fileDescriptionArray[i]);
		// fileDescriptionArray[i] = Utility.sanitise(fileDescriptionArray[i]);
		// }

		User user = userService.loadFind(principal.getUserId());

		int remaining = 0;
		// check dates
		for (UserSubscription userSubscription : user.getSubscriptionList()) {
			remaining += (userSubscription.getSubscriptionQuestion() - userSubscription.getUsedQuestion());
		}

		if (!(remaining > 0)) {
			notification.addError(language(language, LanguageKey.questionQuotaUsed));
			return viewNotification;
		}

		String fileSystemPath = Constant.fileSystemPathAsset + File.separator + "question" + File.separator + "attachment";
		String fileSystemUserPath = user.getTextId() + File.separator;
		String webUserPath = user.getTextId() + "/";
		int webUserPathLength = webUserPath.length();

		Long yesterday = DateTimeUtility.now() - (24 * 60 * 60);

		for (Question draftQuestion : draftQuestionService.forEach(draftQuestionService.listByUserId(user.getId()), Question::attachmentList).stream().filter(q -> q.getCreated() < yesterday).collect(Collectors.toList())) {
			if (draftQuestion.getCreated() < yesterday) {
				for (Attachment attachment : draftQuestion.getAttachmentList()) {
					if (attachment.getPath().startsWith(webUserPath)) {
						String path = attachment.getPath().substring(webUserPathLength);
						File file = new File(fileSystemPath + File.separator + fileSystemUserPath + path);
						if (file.exists()) {
							file.delete();
						}
					}
				}
				draftQuestionService.delete(draftQuestion.getId());
			}
		}

		Question dbQuestion = draftQuestionService.find(question.getId());
		dbQuestion.setText(question.getText());
		dbQuestion.setCategory(question.getCategory());
		dbQuestion.setLevel(question.getLevel());
		dbQuestion.setExplanationType(question.getExplanationType());
		dbQuestion.setActive(question.getActive());
		dbQuestion.setEdited(question.getEdited());
		dbQuestion.setClosed(question.getClosed());
		draftQuestionService.save(dbQuestion);
		dbQuestion.createdTime();

		question = dbQuestion;

		return new View(View.Type.FORWARD, "/WEB-INF/view/question-review.jsp");
	}

	public View doReview() {

		if (!( //
		validateRequired( //
		new RequiredField("question", question), //
		new RequiredField("question.id", question.getId()) //
		) //
		)) {
			return viewNotification;
		}

		question = draftQuestionService.find(question.getId());

		if (isLearner()) {
			if (principal.getUserId().equals(question.getUserId())) {
			} else {
				notification.addError(language(language, LanguageKey.authorisationDenied));
				return viewNotification;
			}
		} else {
			notification.addError(language(language, LanguageKey.authorisationDenied));
			return viewNotification;
		}

		if (question != null) {
			boolean found = false;
			User user = userService.loadFind(principal.getUserId());
			for (UserSubscription userSubscription : user.getSubscriptionList()) {
				if (userSubscription.getUsedQuestion() < userSubscription.getSubscriptionQuestion()) {
					userSubscription.setUsedQuestion(userSubscription.getUsedQuestion() + 1);
					userSubscriptionService.save(userSubscription);
					found = true;
					break;
				}
			}

			if (!found) {
				notification.addError(language(language, LanguageKey.questionQuotaUsed));
				return viewNotification;
			}

			question.setActive(Boolean.TRUE);
			questionService.copy(question);
			draftQuestionService.delete(question.getId());

			principal.setLastUsedCategory(question.getCategory());
			principal.setLastUsedLevel(question.getLevel());
			userService.save(principal);

			/**/

			emailService.email( //
			EmailSender.Account, "viperblaster+tanyatutor@gmail.com", "viperblaster", null, null, //
			"TanyaTutor.com - New Question", "<html><body><pre>" + question.getText() + "</pre><div>by " + principal.getUserEmail() + "</div></body></html>", question.getText() + "\n\nby " + principal.getUserEmail() //
			);

			/**/

			Map actionMap = new HashMap<>();
			actionMap.put("questionAdded", question);

			List<Channel> channelList = Channel.listAllChannel();
			for (Channel channel : channelList) {
				if (!channel.getChannelId().equals(channelId)) {
					if (MembershipType.Educator == channel.getMembershipType() || channel.getUserId().equals(principal.getUserId())) {
						Map map = new HashMap<>();
						map.putAll(actionMap);

						Page page = channel.getPage();
						if (page == Page.Summary) {
							if (MembershipType.Educator == channel.getMembershipType()) {
								Principal channelPrincipal = channel.getPrincipal();
								boolean notify = true;

								if (question.getCategory() != null) {
									if (!channelPrincipal.getCategorySet().contains(question.getCategory())) {
										notify = false;
									}
								}

								if (question.getLevel() != null) {
									if (!channelPrincipal.getLevelSet().contains(question.getLevel())) {
										notify = false;
									}
								}

								if (notify) {
									channel.send(Utility.gsonEnumBean.toJson(map));
								}
							}
						} else if (page == Page.QuestionList) {
							if (channel.getUserId().equals(principal.getUserId())) {
								channel.send(Utility.gsonEnumBean.toJson(map));
							}
						} else if (page == Page.ConversationList) {
						} else if (page == Page.AnswerList) {
						}
					}
				}
			}

		}

		return new View(View.Type.REDIRECT, "/question/" + question.getId());
	}

	public View doStatus(QuestionStatus questionStatus) {

		if (!( //
		validateRequired( //
		new RequiredField("id", id) //
		) //
		)) {
			return viewNotification;
		}

		if (isLearner()) {
			question = questionService.find(id);
			if (question != null) {
				if (principal.getUserId().equals(question.getUserId())) {
					Map actionMap = new HashMap<>();
					if (QuestionStatus.Open == questionStatus) {
						if (question.getClosed() != null) {
							question.setClosed(null);
							questionService.save(question);
							actionMap.put("questionOpened", question.getId());
						}
					}
					if (QuestionStatus.Closed == questionStatus) {
						if (question.getClosed() == null) {
							question.setClosed(DateTimeUtility.now());
							questionService.save(question);
							actionMap.put("questionClosed", question.getId());
						}
					}

					if (!actionMap.isEmpty()) {
						List<Channel> channelList = Channel.listAllChannel();
						for (Channel channel : channelList) {
							if (!channel.getChannelId().equals(channelId)) {
								if (MembershipType.Educator == channel.getMembershipType() || channel.getUserId().equals(principal.getUserId())) {
									Map map = new HashMap<>();
									map.putAll(actionMap);

									Page page = channel.getPage();
									if (page == Page.Summary) {
										if (MembershipType.Educator == channel.getMembershipType()) {
											channel.send(Utility.gson.toJson(map));
										}
									} else if (page == Page.QuestionList) {
										channel.send(Utility.gson.toJson(map));
									} else if (page == Page.ConversationList) {
										if (id.equals(channel.getQuestionId())) {
											channel.send(Utility.gson.toJson(map));
										}
									} else if (page == Page.AnswerList) {
										if (id.equals(channel.getQuestionId())) {
											channel.send(Utility.gson.toJson(map));
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (answerUserId != null) {
			return new View(View.Type.REDIRECT, "/question/" + id + "/" + answerUserId);
		} else {
			return new View(View.Type.REDIRECT, "/question/" + id);
		}
	}

	public View doClose() {
		return doStatus(QuestionStatus.Closed);
	}

	public View doOpen() {
		return doStatus(QuestionStatus.Open);
	}

	public View doDelete() {

		if (!( //
		validateRequired( //
		new RequiredField("id", id) //
		) //
		)) {
			return viewNotification;
		}

		User user = userService.find(principal.getUserId());

		String fileSystemPath = Constant.fileSystemPathAsset + File.separator + "question" + File.separator + "attachment";
		String fileSystemUserPath = user.getTextId() + File.separator + id + File.separator;
		String webUserPath = user.getTextId() + "/" + id + "/";
		int webUserPathLength = webUserPath.length();

		if (isLearner()) {
			question = questionService.loadFind(id);
			if (question != null) {
				if (principal.getUserId().equals(question.getUserId())) {
					Long now = DateTimeUtility.now();
					if (now - question.getCreated() <= Constant.timeThreshold) {
						List<Conversation> conversationList = conversationService.listByQuestionId(question.getId());
						if (conversationList.isEmpty()) {
							questionService.delete(question.getId());

							for (Attachment attachment : question.getAttachmentList()) {
								if (attachment.getPath().startsWith(webUserPath)) {
									String path = attachment.getPath().substring(webUserPathLength);
									File file = new File(fileSystemPath + File.separator + fileSystemUserPath + File.separator + path);
									if (file.exists()) {
										file.delete();
									}
								}
							}

							Map actionMap = new HashMap<>();
							actionMap.put("questionDeleted", question.getId());

							List<Channel> channelList = Channel.listAllChannel();
							for (Channel channel : channelList) {
								if (!channel.getChannelId().equals(channelId)) {
									if (MembershipType.Educator == channel.getMembershipType() || channel.getUserId().equals(principal.getUserId())) {
										Map map = new HashMap<>();
										map.putAll(actionMap);

										Page page = channel.getPage();
										if (page == Page.Summary) {
											if (MembershipType.Educator == channel.getMembershipType()) {
												channel.send(Utility.gson.toJson(map));
											}
										} else if (page == Page.QuestionList) {
											channel.send(Utility.gson.toJson(map));
										} else if (page == Page.ConversationList) {
											if (question.getId().equals(channel.getQuestionId())) {
												channel.send(Utility.gson.toJson(map));
											}
										} else if (page == Page.AnswerList) {
											if (question.getId().equals(channel.getQuestionId())) {
												channel.send(Utility.gson.toJson(map));
											}
										}
									}
								}
							}
						} else {
							notification.addError(language(language, LanguageKey.conversationListNotEmpty));
							return viewNotification;
						}
					} else {
						notification.addError(language(language, LanguageKey.questionTooLongAgo));
						return viewNotification;
					}
				}
			}
		}
		return new View(View.Type.REDIRECT, "/panel");
	}

	public Question getQuestion() {
		return question;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public Long getAnswerUserId() {
		return answerUserId;
	}

	public void setAnswerUserId(Long answerUserId) {
		this.answerUserId = answerUserId;
	}

	public List<Conversation> getConversationList() {
		return conversationList;
	}

	public void setFile(int index, orion.controller.Attachment file) {
		if (index >= 0 && index <= this.fileArray.length) {
			this.fileArray[index] = file;
		}
	}

	public void setFileDescription(int index, String fileDescription) {
		if (index >= 0 && index <= this.fileDescriptionArray.length) {
			this.fileDescriptionArray[index] = fileDescription;
		}
	}

	public User getOtherUser() {
		return otherUser;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
