package tangerine.mapper;

import org.apache.ibatis.annotations.Select;

import tangerine.model.Administrator;

public interface AdministratorMapper {

	@Select("select * from administrator where name = #{name}")
	Administrator findByName(String name);

}
