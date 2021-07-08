package core.persistence;

public enum TransactionIsolation {
	DEFAULT, NONE, READ_COMMITTED, READ_UNCOMMITTED, REPEATABLE_READ, SERIALIZABLE
}
