package tangerine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.model.User;

public interface UserMapper {

	@Insert("insert into \"user\" (id, textId, name, email, password, membershipType, subscriptionCredit, school, phone, information, active, created, state) values (#{id}, #{textId}, #{name}, #{email}, #{password}, #{membershipType}, #{subscriptionCredit}, #{school}, #{phone}, #{information}, #{active}, #{created}, #{state})")
	@SelectKey(statement = "select nextval('userSequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(User user);

	@Update("update \"user\" set name = #{name}, email = #{email}, emailNew = #{emailNew}, password = #{password}, membershipType = #{membershipType}, subscriptionCredit = #{subscriptionCredit}, school = #{school}, phone = #{phone}, information = #{information}, active = #{active}, created = #{created}, state = #{state} where id = #{id}")
	void update(User user);

	@Select("select * from \"user\" where id = #{id}")
	User find(Long id);

	@Select("select * from \"user\" where textId = #{textId}")
	User findByTextId(String textId);

	@Select("select * from \"user\" where email = #{email}")
	User findByEmail(String email);

	@Select("select * from \"user\" where id in (select userId from conversation where questionId = #{questionId})")
	List<User> listByQuestionId(Long questionId);

	@Select("select * from \"user\" where id in (select userId from question where id in (select questionId from conversation where id in (select conversationId from answer where userId = #{userId})))")
	List<User> listByAnswerUserId(Long userId);

	@Delete("delete from \"user\" where id = #{id}")
	void delete(Long id);

}
