package tangerine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.model.Answer;
import tangerine.model.Conversation;

public interface ConversationMapper {

	@Insert("insert into conversation (id, questionId, userId) values (#{id}, #{questionId}, #{userId})")
	@SelectKey(statement = "select nextval('messageSequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(Conversation conversation);

	@Select("select * from conversation where id = #{id}")
	Conversation find(Long id);

	@Select("select * from conversation where questionId = #{questionId} and userId = #{userId}")
	Conversation findByQuestionIdUserId(@Param("questionId") Long questionId, @Param("userId") Long userId);

	@Select("select * from conversation where questionId = #{questionId} order by id")
	List<Conversation> listByQuestionId(Long questionId);

	@Delete("delete from conversation where id = #{id}")
	void delete(Long id);

}
