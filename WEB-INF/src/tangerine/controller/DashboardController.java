package tangerine.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import tangerine.core.Utility;
import tangerine.enumeration.Language;
import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.view.View;
import tangerine.core.Constant;
import tangerine.enumeration.Category;
import tangerine.enumeration.Level;
import tangerine.enumeration.QuestionStatus;
import tangerine.model.Question;
import tangerine.model.User;
import tangerine.service.QuestionService;
import tangerine.service.UserService;

public class DashboardController extends BaseController {

	@Inject
	private UserService userService;

	@Inject
	private QuestionService questionService;

	private User user;
	private Set<QuestionStatus> questionStatusSet = new HashSet<>();
	private Set<Category> categorySet = new HashSet<>();
	private Set<Level> levelSet = new HashSet<>();
	private Integer page = 1;
	private Integer itemPerPage = Constant.itemPerPage;

	private List<Question> questionList = Collections.emptyList();

	@Cookie(name = Constant.cookieLanguage)
	public void setLanguange(String language) {
		this.language = Utility.determineLanguage(language);		
	}

	public View show() {
		user = userService.loadFind(principal.getUserId());

		if (isEducator()) {
			if (page == null || !(page > 0)) {
				page = 1;
				return new View(View.Type.REDIRECT, "/dashboard?page=1");
			}
			if (itemPerPage == null || !(itemPerPage > 0)) {
				itemPerPage = Constant.itemPerPage;
			}

			questionList = questionService.listPageActiveOrderByCreated(principal.getQuestionStatusSet(), principal.getCategorySet(), principal.getLevelSet(), page, itemPerPage);
		}

		return new View(View.Type.FORWARD, "/WEB-INF/view/dashboard.jsp");
	}

	public View doFilter() {

		principal.setQuestionStatusSet(questionStatusSet);
		principal.setLevelSet(levelSet);
		principal.setCategorySet(categorySet);
		userService.save(principal);

		return new View(View.Type.REDIRECT, "/dashboard");
	}

	public User getUser() {
		return user;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void setQuestionStatus(String[] array) {
		for (String s : array) {
			questionStatusSet.add(QuestionStatus.valueOf(s));
		}
	}

	public void setCategory(String[] array) {
		for (String s : array) {
			categorySet.add(Category.valueOf(s));
		}
	}

	public void setLevel(String[] array) {
		for (String s : array) {
			levelSet.add(Level.valueOf(s));
		}
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
