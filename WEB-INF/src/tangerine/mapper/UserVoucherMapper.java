package tangerine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import tangerine.model.UserVoucher;

public interface UserVoucherMapper {

	@Insert("insert into userVoucher (id, userId, voucherId, date, information, created) values (#{id}, #{userId}, #{voucherId}, #{date}, #{information}, #{created})")
	@SelectKey(statement = "select nextval('entitySequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(UserVoucher userVoucher);

	@Update("update userVoucher set information = #{information} where id = #{id}")
	void update(UserVoucher userVoucher);

	@Select("select * from userVoucher where id = #{id}")
	UserVoucher find(Long id);

	@Select("select * from userVoucher where userId = #{userId})")
	List<UserVoucher> listByUserId(Long userId);

	@Select("select * from userVoucher where userId = #{userId} and voucherid = #{voucherId}")
	List<UserVoucher> listByUserIdVoucherId(@Param("userId") Long userId, @Param("voucherId") Long voucherId);

	@Delete("delete from userVoucher where id = #{id}")
	void delete(Long id);

}
