package core.persistence;

import org.apache.ibatis.session.SqlSession;

public class PersistenceTransaction {

	private SqlSession sqlSession;
	private TransactionType transactionType;

	public PersistenceTransaction(SqlSession sqlSession, TransactionType transactionType) {
		this.sqlSession = sqlSession;
		this.transactionType = transactionType;
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

}
