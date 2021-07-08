package tangerine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.model.Question;

public interface DraftQuestionMapper {

	@Insert("insert into draftQuestion (id, userId, category, level, text, attachment, explanationType, active, created, edited, closed) values (#{id}, #{userId}, #{category}, #{level}, #{text}, #{attachment}, #{explanationType}, #{active}, #{created}, #{edited}, #{closed})")
	@SelectKey(statement = "select nextval('messageSequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(Question question);

	@Update("update draftQuestion set text = #{text}, category = #{category}, level = #{level}, attachment = #{attachment}, explanationType = #{explanationType}, active = #{active}, edited = #{edited}, closed = #{closed} where id = #{id}")
	void update(Question question);

	@Select("select * from draftQuestion where id = #{id}")
	Question find(Long id);

	@Select("select * from draftQuestion where userId = #{userId}")
	List<Question> listByUserId(@Param("userId") Long userId);

	@Delete("delete from draftQuestion where id = #{id}")
	void delete(Long id);

}
