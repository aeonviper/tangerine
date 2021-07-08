package core.mybatis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.SqlSession;

import core.persistence.PersistenceService;

public class SqlSessionHolder implements InvocationHandler {

	private final PersistenceService persistenceService;

	public SqlSessionHolder(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		final SqlSession sqlSession = persistenceService.get() == null ? null : persistenceService.get().getSqlSession();
		if (sqlSession != null) {
			try {
				// System.out.println(this + "> sqlSession already exist:" + sqlSession + " persistenceService:" + persistenceService);
				return method.invoke(sqlSession, args);
			} catch (Throwable t) {
				throw ExceptionUtil.unwrapThrowable(t);
			}
		} else {
			// TODO change this
			final SqlSession autoSqlSession = persistenceService.getSqlSessionFactoryReadWrite().openSession();
			try {
				// System.out.println(this + "> autoSqlSession:" + autoSqlSession + " persistenceService:" + persistenceService);
				final Object result = method.invoke(autoSqlSession, args);
				autoSqlSession.commit();
				return result;
			} catch (Throwable t) {
				autoSqlSession.rollback();
				throw ExceptionUtil.unwrapThrowable(t);
			} finally {
				autoSqlSession.close();
			}
		}
	}

}
