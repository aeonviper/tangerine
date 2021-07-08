package core.persistence;

import org.apache.ibatis.session.SqlSessionFactory;

import com.google.inject.Singleton;

@Singleton
public class PersistenceService {

	private enum Type {
		SINGLE, DUAL;
	}

	private Type type;

	private final ThreadLocal<PersistenceTransaction> persistenceTransactionRepository = new ThreadLocal<PersistenceTransaction>();

	public PersistenceTransaction get() {
		return persistenceTransactionRepository.get();
	}

	public void set(PersistenceTransaction persistenceTransaction) {
		persistenceTransactionRepository.set(persistenceTransaction);
	}

	public void remove() {
		persistenceTransactionRepository.remove();
	}

	private final SqlSessionFactory sqlSessionFactoryReadWrite;
	private final SqlSessionFactory sqlSessionFactoryReadOnly;

	public PersistenceService(SqlSessionFactory... sqlSessionFactoryList) {
		if (sqlSessionFactoryList != null && sqlSessionFactoryList.length == 1) {
			this.sqlSessionFactoryReadWrite = sqlSessionFactoryList[0];
			this.sqlSessionFactoryReadOnly = null;
			type = Type.SINGLE;
		} else if (sqlSessionFactoryList != null && sqlSessionFactoryList.length == 2) {
			this.sqlSessionFactoryReadWrite = sqlSessionFactoryList[0];
			this.sqlSessionFactoryReadOnly = sqlSessionFactoryList[1];
			type = Type.DUAL;
		} else {
			this.sqlSessionFactoryReadWrite = null;
			this.sqlSessionFactoryReadOnly = null;
		}
	}

	public SqlSessionFactory getSqlSessionFactoryReadWrite() {
		return sqlSessionFactoryReadWrite;
	}

	public SqlSessionFactory getSqlSessionFactoryReadOnly() {
		return sqlSessionFactoryReadOnly;
	}

	public boolean isSingle() {
		return type == Type.SINGLE;
	}

	public boolean isDual() {
		return type == Type.DUAL;
	}

}
