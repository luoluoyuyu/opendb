package net.openio.jrocksDb.config;

public class Config {

    //memArena
    public final static int memArenaSize=1<<12;

    public final  static String WalLogPath="src/main/resources/WalLog/";

    public final  static String transactionLog="src/main/resources/TransactionLog/";


    public final  static String DBMetaDataPath="src/main/resources/data/db";
    public final  static String StoragePath="src/main/resources/data/";
    public final  static TransactionType type= TransactionType.repeatableRead;

    public final  static int leve0Num=16;
    public final  static int leve1Num=32;
    public final  static int leve2Num=48;
    public final  static int leve3Num=64;
    public final  static int leve4Num=128;

    public final static int serializerSize=1024*1024*4;


    public enum TransactionType{
        readUnCommit,
        readCommit,
        repeatableRead,
        Serializable,
    }
}
