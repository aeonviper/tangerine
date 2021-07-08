package tangerine.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.enumeration.Category;
import tangerine.enumeration.Level;
import tangerine.enumeration.QuestionStatus;
import tangerine.model.Question;

public interface QuestionMapper {

	@Insert("insert into question (id, userId, category, level, text, attachment, explanationType, active, created, edited, closed) values (#{id}, #{userId}, #{category}, #{level}, #{text}, #{attachment}, #{explanationType}, #{active}, #{created}, #{edited}, #{closed})")
	@SelectKey(statement = "select nextval('messageSequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(Question question);

	@Update("update question set text = #{text}, category = #{category}, level = #{level}, attachment = #{attachment}, active = #{active}, edited = #{edited}, closed = #{closed} where id = #{id}")
	void update(Question question);

	@Insert("insert into question (id, userId, category, level, text, attachment, explanationType, active, created, edited, closed) values (#{id}, #{userId}, #{category}, #{level}, #{text}, #{attachment}, #{explanationType}, #{active}, #{created}, #{edited}, #{closed})")
	void copy(Question question);

	@Select("select * from question where id = #{id}")
	Question find(Long id);

	@Select("select * from question where userId = #{userId} order by created desc limit #{itemPerPage} offset ((#{page} - 1) * #{itemPerPage})")
	List<Question> listPageByUserId(@Param("userId") Long userId, @Param("page") Integer page, @Param("itemPerPage") Integer itemPerPage);

	@Select("select * from question where id in (select questionId from conversation where id in (select conversationId from answer where userId = #{userId})) order by created desc limit #{itemPerPage} offset ((#{page} - 1) * #{itemPerPage})")
	List<Question> listPageAnsweredByUserId(@Param("userId") Long userId, @Param("page") Integer page, @Param("itemPerPage") Integer itemPerPage);

	@SelectProvider(type = tangerine.provider.sql.Question.class, method = "listPageActiveOrderByCreated")
	List<Question> listPageActiveOrderByCreated(@Param("questionStatusSet") Set<QuestionStatus> questionStatusSet, @Param("categorySet") Set<Category> categorySet, @Param("levelSet") Set<Level> levelSet, @Param("page") Integer page, @Param("itemPerPage") Integer itemPerPage);

	@Delete("delete from question where id = #{id}")
	void delete(Long id);

}
