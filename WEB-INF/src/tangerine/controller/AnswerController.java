package tangerine.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import core.utility.DateTimeUtility;
import core.validation.field.RequiredField;
import core.validation.field.RequiredStringField;
import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.Language;
import tangerine.enumeration.LanguageKey;
import tangerine.enumeration.MembershipType;
import tangerine.enumeration.Page;
import tangerine.model.Answer;
import tangerine.model.Attachment;
import tangerine.model.Conversation;
import tangerine.model.Question;
import tangerine.model.User;
import tangerine.service.AnswerService;
import tangerine.service.ConversationService;
import tangerine.service.ShowService;
import tangerine.service.QuestionService;
import tangerine.service.SequenceService;
import tangerine.service.UserService;
import tangerine.websocket.Channel;

public class AnswerController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private QuestionService questionService;

	@Inject
	private ConversationService conversationService;

	@Inject
	private AnswerService answerService;

	@Inject
	private ShowService showService;

	@Inject
	private SequenceService sequenceService;

	private Long id;
	private Long questionId;
	private Long answerUserId;
	private String channelId;

	private Answer answer = new Answer();

	public static final int maxAttachment = 1;

	private orion.controller.Attachment[] fileArray = new orion.controller.Attachment[maxAttachment];
	private String[] fileDescriptionArray = new String[maxAttachment];

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View doAnswer() {

		if (!( //
		validateRequired( //
		new RequiredField("questionId", questionId) //
		) //
		)) {
			return viewNotification;
		}

		Question question = questionService.find(questionId);
		User user = userService.find(question.getUserId());

		if (isLearner()) {
			if (principal.getUserId().equals(question.getUserId())) {

			} else {
				notification.addError(language(language, LanguageKey.authorisationDenied));
				return viewNotification;
			}
		}

		Utility.nullIfBlank(answer, "text");
		Utility.stripText(answer, "text");
		Utility.sanitise(answer, "text");

		for (int i = 0; i < maxAttachment; i++) {
			fileDescriptionArray[i] = Utility.nullIfBlank(fileDescriptionArray[i]);
			fileDescriptionArray[i] = Utility.stripText(fileDescriptionArray[i]);
			fileDescriptionArray[i] = Utility.sanitise(fileDescriptionArray[i]);
		}

		answer.setUserId(principal.getUserId());
		answer.setCreated(DateTimeUtility.now());

		String fileSystemPath = Constant.fileSystemPathAsset + File.separator + "question" + File.separator + "attachment";
		String fileSystemUserPath = user.getTextId() + File.separator + question.getId() + File.separator;
		String webUserPath = user.getTextId() + "/" + question.getId() + "/";
		String attachmentFileName;
		String fileName;

		new File(fileSystemPath + File.separator + fileSystemUserPath).mkdirs();

		answer.setAttachmentList(new ArrayList<>());
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
				answer.getAttachmentList().add(attachment);
			}
		}

		Map actionMap = new HashMap<>();

		User answerUser = userService.find(answer.getUserId());

		Conversation conversation = conversationService.findByQuestionIdUserId(questionId, answerUserId);
		if (conversation == null) {
			conversation = new Conversation();
			conversation.setQuestionId(questionId);
			conversation.setUserId(answerUserId);
			conversationService.save(conversation);

			actionMap.put("conversationAdded", conversation);
			conversation.setLatestAnswer(new Answer());
			conversation.getLatestAnswer().setText(Utility.ellipsis(answer.getText()));
			conversation.setUser(new User());
			conversation.getUser().setName(answerUser.getName());

			question.createdTime();
			question.setUser(new User());
			question.getUser().setName(user.getName());
			question.setText(Utility.ellipsis(question.getText()));
			actionMap.put("conversationAddedQuestion", question);
		}

		answer.setConversationId(conversation.getId());
		answerService.save(answer);

		answer.createdTime();
		actionMap.put("answerAdded", answer);

		boolean otherUserIsOnline = false;

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
					if (!channel.getUserId().equals(principal.getUserId())) {
						otherUserIsOnline = true;
					}
				}
				map.putAll(actionMap);
				channel.send(Utility.gson.toJson(map));
			}
		}

		if (otherUserIsOnline) {
			answer.setShowed(DateTimeUtility.now());
			answerService.save(answer);
		}

		return new View(View.Type.REDIRECT, "/question/" + question.getId() + "/" + answerUserId + "#answer");
	}

	public View doDelete() {

		if (!( //
		validateRequired( //
		new RequiredField("questionId", questionId) //
		) //
		)) {
			return viewNotification;
		}

		Question question = questionService.find(questionId);
		User user = userService.find(question.getUserId());

		if (isLearner()) {
			if (principal.getUserId().equals(question.getUserId())) {

			} else {
				notification.addError(language(language, LanguageKey.authorisationDenied));
				return viewNotification;
			}
		}

		Map actionMap = new HashMap<>();

		String fileSystemPath = Constant.fileSystemPathAsset + File.separator + "question" + File.separator + "attachment";
		String fileSystemUserPath = user.getTextId() + File.separator + question.getId() + File.separator;
		String webUserPath = user.getTextId() + "/" + question.getId() + "/";
		int webUserPathLength = webUserPath.length();

		answer = answerService.find(id);
		if (answer != null) {
			if (principal.getUserId().equals(answer.getUserId())) {
				Conversation conversation = conversationService.find(answer.getConversationId());
				Long now = DateTimeUtility.now();
				if (now - answer.getCreated() <= Constant.timeThreshold) {
					answerService.delete(answer.getId());

					for (Attachment attachment : answer.getAttachmentList()) {
						if (attachment.getPath().startsWith(webUserPath)) {
							String path = attachment.getPath().substring(webUserPathLength);
							File file = new File(fileSystemPath + File.separator + fileSystemUserPath + File.separator + path);
							if (file.exists()) {
								file.delete();
							}
						}
					}

					actionMap.put("answerDeleted", answer.getId());

					List<Answer> answerList = answerService.listByConversationId(conversation.getId());
					if (answerList.isEmpty()) {
						conversationService.delete(conversation.getId());

						actionMap.put("conversationDeleted", conversation.getId());
						actionMap.put("conversationDeletedConversation", conversation);
					}
				} else {
					notification.addError(language(language, LanguageKey.answerTooLongAgo));
					return viewNotification;
				}
			}
		}

		Conversation conversation = new Conversation();
		conversation.setId(answer.getConversationId());

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
				}
				map.putAll(actionMap);
				channel.send(Utility.gson.toJson(map));
			}
		}

		return new View(View.Type.REDIRECT, "/question/" + question.getId() + "/" + answerUserId + "#answer");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Answer getAnswer() {
		return answer;
	}

	public Long getAnswerUserId() {
		return answerUserId;
	}

	public void setAnswerUserId(Long answerUserId) {
		this.answerUserId = answerUserId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
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

}
