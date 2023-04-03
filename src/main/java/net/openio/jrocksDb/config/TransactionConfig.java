package net.openio.jrocksDb.config;

public class TransactionConfig {
    public enum TransactionType{
        readUnCommit,
        readCommit,
        repeatableRead,
        Serializable,
    }

    public static TransactionType type=TransactionType.repeatableRead;

}
