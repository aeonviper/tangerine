package tangerine.mapper;

import org.apache.ibatis.annotations.Select;

import tangerine.model.Voucher;

public interface VoucherMapper {

	@Select("select * from voucher where id = #{id}")
	Voucher find(Long id);

	@Select("select * from voucher where name = #{name}")
	Voucher findByName(String name);

}
