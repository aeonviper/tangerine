package tangerine.bean;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.reflect.TypeToken;

import tangerine.core.Utility;
import tangerine.enumeration.Category;
import tangerine.enumeration.Level;
import tangerine.enumeration.MembershipType;
import tangerine.enumeration.QuestionStatus;

public class Principal {

	private Long userId;
	private String userEmail;
	private MembershipType membershipType;

	private Long administratorId;
	private String administratorName;

	private Map<String, Set<String>> stateMap = new HashMap<>();

	private Set<QuestionStatus> questionStatusSet = new HashSet<>();
	private Set<Category> categorySet = new HashSet<>();
	private Set<Level> levelSet = new HashSet<>();
	private Category lastUsedCategory;
	private Level lastUsedLevel;

	private static Type typeStringStringSetMap = new TypeToken<Map<String, Set<String>>>() {
	}.getType();

	public Principal(Long userId, String userEmail, MembershipType membershipType) {
		this.userId = userId;
		this.userEmail = userEmail;
		this.membershipType = membershipType;
	}

	public void setAdministrator(Long id, String name) {
		this.administratorId = id;
		this.administratorName = name;
	}

	public synchronized void state(String data) {
		if (!Utility.isBlank(data)) {
			stateMap = Utility.gson.fromJson(data, typeStringStringSetMap);
			for (Map.Entry<String, Set<String>> entry : stateMap.entrySet()) {
				String key = entry.getKey();
				Set<String> set = entry.getValue();
				if ("filter:questionStatus".equals(key)) {
					questionStatusSet.clear();
					for (String s : set) {
						questionStatusSet.add(QuestionStatus.valueOf(s));
					}
				} else if ("filter:level".equals(key)) {
					levelSet.clear();
					for (String s : set) {
						levelSet.add(Level.valueOf(s));
					}
				} else if ("filter:category".equals(key)) {
					categorySet.clear();
					for (String s : set) {
						categorySet.add(Category.valueOf(s));
					}
				} else if ("lastUsed:level".equals(key)) {
					for (String s : set) {
						lastUsedLevel = Level.valueOf(s);
					}
				} else if ("lastUsed:category".equals(key)) {
					for (String s : set) {
						lastUsedCategory = Category.valueOf(s);
					}
				}
			}
		}
	}

	public synchronized void state() {
		stateMap.clear();
		Set<String> set;

		set = new HashSet<>();
		for (QuestionStatus questionStatus : questionStatusSet) {
			set.add(questionStatus.name());
		}
		stateMap.put("filter:questionStatus", set);

		set = new HashSet<>();
		for (Level level : levelSet) {
			set.add(level.name());
		}
		stateMap.put("filter:level", set);

		set = new HashSet<>();
		for (Category category : categorySet) {
			set.add(category.name());
		}
		stateMap.put("filter:category", set);

		if (lastUsedLevel != null) {
			set = new HashSet<>();
			set.add(lastUsedLevel.name());
			stateMap.put("lastUsed:level", set);
		} else {
			stateMap.remove("lastUsed:level");
		}

		if (lastUsedCategory != null) {
			set = new HashSet<>();
			set.add(lastUsedCategory.name());
			stateMap.put("lastUsed:category", set);
		} else {
			stateMap.remove("lastUsed:category");
		}

	}

	public Long getUserId() {
		return userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public MembershipType getMembershipType() {
		return membershipType;
	}

	public boolean isLearner() {
		return MembershipType.Learner == membershipType;
	}

	public boolean isEducator() {
		return MembershipType.Educator == membershipType;
	}

	public Set<QuestionStatus> getQuestionStatusSet() {
		return questionStatusSet;
	}

	public void setQuestionStatusSet(Set<QuestionStatus> questionStatusSet) {
		this.questionStatusSet = questionStatusSet;
	}

	public Set<Category> getCategorySet() {
		return categorySet;
	}

	public void setCategorySet(Set<Category> categorySet) {
		this.categorySet = categorySet;
	}

	public Set<Level> getLevelSet() {
		return levelSet;
	}

	public void setLevelSet(Set<Level> levelSet) {
		this.levelSet = levelSet;
	}

	public Map<String, Set<String>> getStateMap() {
		return stateMap;
	}

	public Category getLastUsedCategory() {
		return lastUsedCategory;
	}

	public void setLastUsedCategory(Category lastUsedCategory) {
		this.lastUsedCategory = lastUsedCategory;
	}

	public Level getLastUsedLevel() {
		return lastUsedLevel;
	}

	public void setLastUsedLevel(Level lastUsedLevel) {
		this.lastUsedLevel = lastUsedLevel;
	}

	public Long getAdministratorId() {
		return administratorId;
	}

	public String getAdministratorName() {
		return administratorName;
	}

}
