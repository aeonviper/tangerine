package core.persistence;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

import core.utility.Logger;

import com.google.inject.Inject;

public class TransactionInterceptor implements MethodInterceptor {

	@Inject
	PersistenceService persistenceService;

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Transactional transactional = methodInvocation.getMethod().getAnnotation(Transactional.class);
		if (transactional != null) {

			SqlSessionFactory sqlSessionFactory = null;
			TransactionType transactionType = null;

			if (persistenceService.isDual()) {
				if (persistenceService.get() == null) {

					if (TransactionTypeService.isSet()) {
						if (TransactionTypeService.isReadOnly()) {
							sqlSessionFactory = persistenceService.getSqlSessionFactoryReadOnly();
							transactionType = TransactionType.READONLY;
							// Logger.getLogger().info("Txn override: readOnly");
						} else {
							sqlSessionFactory = persistenceService.getSqlSessionFactoryReadWrite();
							transactionType = TransactionType.READWRITE;
							// Logger.getLogger().info("Txn override: readWrite");
						}
					} else {
						if (transactional.type() == TransactionType.READONLY) {
							sqlSessionFactory = persistenceService.getSqlSessionFactoryReadOnly();
							transactionType = TransactionType.READONLY;
							// Logger.getLogger().info("Txn: readOnly");
						} else {
							sqlSessionFactory = persistenceService.getSqlSessionFactoryReadWrite();
							transactionType = TransactionType.READWRITE;
							// Logger.getLogger().info("Txn: readWrite");
						}
					}

				} else {

					if (persistenceService.get().getTransactionType() == TransactionType.READONLY) {
						// must always use if, not if-else
						if (TransactionType.READWRITE == transactional.type()) {
							String message = "Transaction Type mismatch ! Current Transaction is " + persistenceService.get().getTransactionType() + " but method " + methodInvocation.getClass().getCanonicalName() + "." + methodInvocation.getMethod().getName() + " has Transactional Type " + transactional.type();
							Logger.getLogger().error(message);
							throw new RuntimeException(this.getClass().getCanonicalName() + " - " + message);
						}
						// this is probably not going to happen, since override can only happen at the beginning not during a thread execution
						// transaction override is only honored by the framework only at the beginning of thread execution. if it wanted read write it should have specified read write in the beginning thus persistService would hold a read write entity manager
						// in order for this to happen it means ThreadLocal of transaction type at beginning of thread execution is one value (read only) and got changed to a different value (read write) in the middle of thread execution, now the framework is re-checking and finding a different value
						if (TransactionTypeService.isSet() && TransactionTypeService.isReadWrite()) {
							String message = "Transaction Type mismatch ! Current Transaction is " + persistenceService.get().getTransactionType() + " but transaction type override is Read Write";
							Logger.getLogger().error(message);
							throw new RuntimeException(this.getClass().getCanonicalName() + " - " + message);
						}
					}

				}

			} else if (persistenceService.isSingle()) {

				if (persistenceService.get() == null) {
					sqlSessionFactory = persistenceService.getSqlSessionFactoryReadWrite();
					transactionType = TransactionType.READWRITE;
					Logger.getLogger().info("Txn: readWrite");
				}

			}

			SqlSession sqlSession = null;
			if (persistenceService.get() == null) {

				ExecutorType transactionalExecutorType = transactional.executorType();
				org.apache.ibatis.session.ExecutorType executorType = null;
				if (transactionalExecutorType != ExecutorType.DEFAULT) {
					switch (transactional.executorType()) {
					case SIMPLE:
						executorType = org.apache.ibatis.session.ExecutorType.SIMPLE;
						break;
					case REUSE:
						executorType = org.apache.ibatis.session.ExecutorType.REUSE;
						break;
					default:
						executorType = org.apache.ibatis.session.ExecutorType.BATCH;
						break;
					}
				}

				TransactionIsolation transactionalIsolation = transactional.isolation();
				TransactionIsolationLevel transactionIsolationLevel = null;
				// was isolation specified ?
				if (transactionalIsolation != TransactionIsolation.DEFAULT) {
					switch (transactionalIsolation) {
					case READ_COMMITTED:
						transactionIsolationLevel = TransactionIsolationLevel.READ_COMMITTED;
						break;
					case READ_UNCOMMITTED:
						transactionIsolationLevel = TransactionIsolationLevel.READ_UNCOMMITTED;
						break;
					case REPEATABLE_READ:
						transactionIsolationLevel = TransactionIsolationLevel.REPEATABLE_READ;
						break;
					case SERIALIZABLE:
						transactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE;
						break;
					default:
						transactionIsolationLevel = TransactionIsolationLevel.NONE;
						break;
					}
				}

				if (executorType != null && transactionIsolationLevel != null) {
					sqlSession = sqlSessionFactory.openSession(executorType, transactionIsolationLevel);
				} else if (executorType != null && transactionIsolationLevel == null) {
					sqlSession = sqlSessionFactory.openSession(executorType);
				} else if (executorType == null && transactionIsolationLevel != null) {
					sqlSession = sqlSessionFactory.openSession(transactionIsolationLevel);
				} else {
					sqlSession = sqlSessionFactory.openSession();
				}

				persistenceService.set(new PersistenceTransaction(sqlSession, transactionType));

			} else {

				sqlSession = persistenceService.get().getSqlSession();

			}

			// System.out.println(this.getClass().getSimpleName() + "> sqlSession:" + sqlSession);

			Object result = null;

			if (sqlSessionFactory != null) {

				// if sqlSessionFactory != null that means this is the beginning of the transaction

				try {
					result = methodInvocation.proceed();
					sqlSession.commit();
				} catch (Exception e) {
					if (commitWhenException(transactional, e)) {
						sqlSession.commit();
					} else {
						sqlSession.rollback();
					}

					System.err.println(e);
					throw e;
				} finally {
					sqlSession.close();
					persistenceService.remove();
				}

			} else {

				// an active transaction is already is progress, since persistenceService.get() != null, thus sqlSessionFactory is null

				result = methodInvocation.proceed();

			}

			return result;

		} else {
			return methodInvocation.proceed();
		}
	}

	private boolean commitWhenException(Transactional transactional, Exception e) {
		boolean commit = true;

		for (Class<? extends Exception> rollBackOn : transactional.rollbackOn()) {
			if (rollBackOn.isInstance(e)) {
				commit = false;

				for (Class<? extends Exception> exceptOn : transactional.ignore()) {
					if (exceptOn.isInstance(e)) {
						commit = true;
						break;
					}
				}

				break;
			}
		}

		return commit;
	}
}
