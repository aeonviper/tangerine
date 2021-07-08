package tangerine.provider.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Select;

import core.utility.Utility;
import tangerine.enumeration.Category;
import tangerine.enumeration.Level;
import tangerine.enumeration.QuestionStatus;

public class Question {

	/* example */
	public String listByPage(Map<String, Object> parameterMap) {
		String sortField = (String) parameterMap.get("sortField");
		Boolean sortDirection = (Boolean) parameterMap.get("sortDirection");
		Integer itemPerPage = (Integer) parameterMap.get("itemPerPage");
		Integer page = (Integer) parameterMap.get("page");
		return "select * from \"user\" order by " + sortField + " " + (Boolean.FALSE.equals(sortDirection) ? "desc" : "asc") + " limit " + itemPerPage + " offset ((" + page + " - 1) * " + itemPerPage + ")";
	}

	public String listPageActiveOrderByCreated(Map<String, Object> parameterMap) {
		Set<QuestionStatus> questionStatusSet = (Set<QuestionStatus>) parameterMap.get("questionStatusSet");
		Set<Category> categorySet = (Set<Category>) parameterMap.get("categorySet");
		Set<Level> levelSet = (Set<Level>) parameterMap.get("levelSet");
		Integer itemPerPage = (Integer) parameterMap.get("itemPerPage");
		Integer page = (Integer) parameterMap.get("page");

		StringBuilder sql = new StringBuilder();
		sql.append("select *, (select count(1) as conversationSize from conversation where conversation.questionId = question.id) from question where active");
		if (questionStatusSet != null && questionStatusSet.size() == 1) {
			if (questionStatusSet.contains(QuestionStatus.Open)) {
				sql.append(" and closed is null");
			} else if (questionStatusSet.contains(QuestionStatus.Closed)) {
				sql.append(" and closed is not null");
			}
		}

		if (categorySet != null && !categorySet.isEmpty()) {
			List<String> list = categorySet.stream().map(category -> "'" + category.name() + "'").collect(Collectors.toList());
			sql.append(" and (category is null or category in (").append(Utility.join(list.toArray(new String[0]), ",")).append("))");
		}

		if (levelSet != null && !levelSet.isEmpty()) {
			List<String> list = levelSet.stream().map(level -> "'" + level.name() + "'").collect(Collectors.toList());
			sql.append(" and (level is null or level in (").append(Utility.join(list.toArray(new String[0]), ",")).append("))");
		}

		sql.append(" order by created desc limit #{itemPerPage} offset ((#{page} - 1) * #{itemPerPage})");

		return sql.toString();
	}
}
