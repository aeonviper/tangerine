package tangerine.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.core.Constant;
import tangerine.core.Utility;
import tangerine.enumeration.Language;
import tangerine.model.Question;
import tangerine.model.User;
import tangerine.service.QuestionService;
import tangerine.service.UserService;

public class PanelController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private QuestionService questionService;

	private User user;
	private List<Question> questionList = Collections.emptyList();
	private Integer page = 1;
	private Integer itemPerPage = Constant.itemPerPage;

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);
	}

	public View show() {
		user = userService.find(principal.getUserId());

		if (page == null || !(page > 0)) {
			page = 1;
			return new View(View.Type.REDIRECT, "/panel?page=1");
		}
		if (itemPerPage == null || !(itemPerPage > 0)) {
			itemPerPage = Constant.itemPerPage;
		}

		if (isLearner()) {
			questionList = questionService.listPageByUserId(principal.getUserId(), page, itemPerPage);
		}

		if (isEducator()) {
			questionList = questionService.listPageAnsweredByUserId(principal.getUserId(), page, itemPerPage);
			Map<Long, User> userMap = userService.mapify(userService.listByAnswerUserId(principal.getUserId()));
			for (Question question : questionList) {
				question.setUser(userMap.get(question.getUserId()));
			}
		}

		return new View(View.Type.FORWARD, "/WEB-INF/view/panel.jsp");
	}

	public User getUser() {
		return user;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getItemPerPage() {
		return itemPerPage;
	}

	public void setItemPerPage(Integer itemPerPage) {
		this.itemPerPage = itemPerPage;
	}

}
