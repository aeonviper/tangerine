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

public interface AnswerMapper {

	@Insert("insert into answer (id, conversationId, userId, text, attachment, showed, created) values (#{id}, #{conversationId}, #{userId}, #{text}, #{attachment}, #{showed}, #{created})")
	@SelectKey(statement = "select nextval('messageSequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(Answer answer);

	@Update("update answer set text = #{text}, showed = #{showed} where id = #{id}")
	void update(Answer answer);

	@Select("select * from answer where showed is null and conversationId = #{conversationId} and userId = #{userId} order by created")
	List<Answer> listUnshowed(@Param("conversationId") Long conversationId, @Param("userId") Long userId);

	@Update("update answer set showed = #{showed} where showed is null and conversationId = #{conversationId} and userId = #{userId}")
	void updateShowed(@Param("conversationId") Long conversationId, @Param("userId") Long userId, @Param("showed") Long showed);

	@Select("select * from answer where id = #{id}")
	Answer find(Long id);

	@Select("select * from answer where conversationId = #{conversationId} order by created")
	List<Answer> listByConversationId(Long conversationId);

	@Select("select * from answer where conversationId = #{conversationId} order by created limit #{itemPerPage} offset ((#{page} - 1) * #{itemPerPage})")
	List<Answer> listPageByConversationId(@Param("conversationId") Long conversationId, @Param("page") Integer page, @Param("itemPerPage") Integer itemPerPage);

	@Delete("delete from answer where id = #{id}")
	void delete(Long id);

	@Select("select * from answer where id in (select max(id) from answer where conversationId in (select id from conversation where questionId = #{questionId}) group by conversationId)")
	List<Answer> listLatestByQuestionId(Long questionId);

}
