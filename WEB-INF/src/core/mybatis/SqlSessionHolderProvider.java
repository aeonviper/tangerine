package core.mybatis;

import java.lang.reflect.Proxy;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;

import com.google.inject.Provider;
import com.google.inject.Singleton;

import core.persistence.PersistenceService;

@Singleton
public class SqlSessionHolderProvider implements Provider<SqlSession> {

	@Inject
	private PersistenceService persistenceService;

	@Override
	public SqlSession get() {
		SqlSessionHolder sqlSessionHolder;
		SqlSession sqlSession = (SqlSession) Proxy.newProxyInstance(SqlSessionHolder.class.getClassLoader(), new Class[] { SqlSession.class }, sqlSessionHolder = new SqlSessionHolder(persistenceService));
		// System.out.println(this + "> sqlSession:" + sqlSession + " sqlSessionHolder:" + sqlSessionHolder + " persistenceService:" + persistenceService);
		return sqlSession;
	}

}
