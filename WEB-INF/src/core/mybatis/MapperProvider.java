package core.mybatis;

import java.lang.reflect.Proxy;

import javax.inject.Inject;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.google.inject.Provider;

public class MapperProvider<T> implements Provider<T> {

	private final Class<T> mapperType;

	@Inject
	private Configuration configuration;

	@Inject
	private SqlSession sqlSession;

	public MapperProvider(Class<T> mapperType) {
		this.mapperType = mapperType;
	}

	@Override
	public T get() {
		// System.out.println(this.getClass().getSimpleName() + "> configuration:" + configuration + " sqlSessionHolder:" + ((Proxy) sqlSession).getInvocationHandler(sqlSession));
		return configuration.getMapper(mapperType, sqlSession);
	}

}
