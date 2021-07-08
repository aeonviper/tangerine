package tangerine.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import tangerine.enumeration.VerificationType;
import tangerine.model.Verification;

public interface VerificationMapper {

	@Insert("insert into verification (id, textId, code, userId, type, value, created) values (#{id}, #{textId}, #{code}, #{userId}, #{type}, #{value}, #{created})")
	@SelectKey(statement = "select nextval('verificationSequence')", keyProperty = "id", before = true, resultType = Long.class)
	void insert(Verification verification);

	@Select("select * from verification where textId = #{textId}")
	Verification findByTextId(String textId);

	@Select("select * from verification where userId = #{userId} and code = #{code}")
	Verification findByUserIdCode(@Param("userId") Long userId, @Param("code") String code);

	@Delete("delete from verification where id = #{id}")
	void delete(Long id);

	@Delete("delete from verification where type = #{type} and userId = #{userId}")
	void deleteByTypeUserId(@Param("type") VerificationType type, @Param("userId") Long userId);

}
