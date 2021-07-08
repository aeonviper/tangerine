package core.service;

import com.google.inject.Singleton;

import core.persistence.TransactionType;
import core.persistence.Transactional;

@Singleton
public class TransactionService {
	@Transactional
	public void readWrite(TransactionBundle transactionBundle) {
		transactionBundle.execute();
	}

	@Transactional(type = TransactionType.READONLY)
	public void readOnly(TransactionBundle transactionBundle) {
		transactionBundle.execute();
	}
}
