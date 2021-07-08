package tangerine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.model.UserExpense;

public interface UserExpenseMapper {

	@Insert("insert into userExpense (id, userId, fromDate, toDate, subscriptionQuestion, subscriptionPackage, amount, information, created) values (#{id}, #{userId}, #{fromDate}, #{toDate}, #{subscriptionQuestion}, #{subscriptionPackage}, #{amount} ,#{information}, #{created})")
	@SelectKey(statement = "select nextval('entitySequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(UserExpense userExpense);

	@Update("update userExpense set information = #{information} where id = #{id}")
	void update(UserExpense userExpense);

	@Select("select * from userExpense where id = #{id}")
	UserExpense find(Long id);

	@Select("select * from userExpense where userId = #{userId} order by toDate)")
	List<UserExpense> listByUserId(Long userId);

	@Delete("delete from userExpense where id = #{id}")
	void delete(Long id);

}
