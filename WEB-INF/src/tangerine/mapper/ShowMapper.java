package tangerine.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.model.Answer;

public interface ShowMapper {

	@Select( //
	"select q.id as questionId, c.id as conversationId, a.* " + //
	"from " + //
	"question q " + //
	"join conversation c on c.questionId = q.id " + //
	"join answer a on a.conversationId = c.id " + //
	"where " + //
	"a.showed is null and q.userId = #{userId} and a.userId != #{userId} " + //
	"order by q.id, c.id, a.created" //
	)
	//
	List<Map> learnerQuery(Long userId);

	@Select( //
	"select q.id as questionId, c.id as conversationId, a.* " + //
	"from " + //
	"question q " + //
	"join conversation c on c.questionId = q.id " + //
	"join answer a on a.conversationId = c.id " + //
	"where " + //
	"a.showed is null and q.userId = a.userId " + //
	"and q.id in (select questionId from conversation where id in (select conversationId from answer where userId = #{userId})) " + //
	"order by q.id, c.id, a.created" //
	)
	//
	List<Map> educatorQuery(Long userId);

}
