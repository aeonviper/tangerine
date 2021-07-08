package tangerine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.model.UserSubscription;

public interface UserSubscriptionMapper {

	@Insert("insert into userSubscription (id, userId, fromDate, toDate, subscriptionQuestion, usedQuestion, subscriptionPackage, renewal, information, created) values (#{id}, #{userId}, #{fromDate}, #{toDate}, #{subscriptionQuestion}, #{usedQuestion}, #{subscriptionPackage}, #{renewal}, #{information}, #{created})")
	@SelectKey(statement = "select nextval('entitySequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(UserSubscription userSubscription);

	@Update("update userSubscription set fromDate = #{fromDate}, toDate = #{toDate}, subscriptionQuestion = #{subscriptionQuestion}, usedQuestion = #{usedQuestion}, subscriptionPackage = #{subscriptionPackage}, renewal = #{renewal}, information = #{information} where id = #{id}")
	void update(UserSubscription userSubscription);
	
	@Update("delete from userSubscription where toDate < #{date} and renewal is null")
	void deleteNonRenewal(Long date);
	
	@Select("select * from userSubscription where toDate < #{date} and renewal is not null order by userId")
	List<UserSubscription> listRenewal(Long date);

	@Select("select * from userSubscription where id = #{id}")
	UserSubscription find(Long id);

	@Select("select * from userSubscription where userId = #{userId} order by toDate")
	List<UserSubscription> listByUserId(Long userId);

	@Delete("delete from userSubscription where id = #{id}")
	void delete(Long id);

}
