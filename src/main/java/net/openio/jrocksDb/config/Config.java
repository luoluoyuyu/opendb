package net.openio.jrocksDb.config;

public class Config {

    //memArena
    public static int memArenaSize=1<<12;

    public static String WalLogPath="src/main/resources/WalLog/";

    public static String transactionLog="src/main/resources/TransactionLog/";


    public static String DBMetaDataPath="src/main/resources/data/db";
}
