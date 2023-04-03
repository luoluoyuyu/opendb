package memTest;

import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.db.*;
import net.openio.jrocksDb.log.TransactionLogStorage;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.transaction.CommitId;
import net.openio.jrocksDb.transaction.PrepareId;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TKVEntry {

    static int p = 1;

    static long b = new Date().getTime();

    public static void main(String[] args) {

        List<TransactionKeyValue> list=new ArrayList<>();
        for (int i=0;i<1000;i++) {
            TransactionKeyValue keyValue1 = new TransactionKeyValue();
            Key key = new IntKey(2);
            Value value = new IntValue(2);
            KeyValueEntry keyValue = new KeyValueEntry();
            keyValue.setKey(key);
            keyValue.setValue(value);
            keyValue.setPrepareId(p());
            keyValue.setCommitId(c());
            keyValue.setSqId(s());
            keyValue1.setKeyValueEntry(keyValue);
            keyValue1.setColumnFamilyId(new ColumnFamilyId(1));
            keyValue.setType(KeyValueEntry.Type.insert);

            list.add(keyValue1);
           }

        TransactionLogStorage transactionLogStorage=new TransactionLogStorage();

        transactionLogStorage.setMemArena(new MemArena());
        transactionLogStorage.setFilePath(Config.transactionLog);

        transactionLogStorage.createFile("luu",new ColumnFamilyId(12l));

        transactionLogStorage.write("luu",list,false,null);

        for(TransactionKeyValue value: transactionLogStorage.getAllMemTable("luu")){
            System.out.println(value);
        }


    }


    static synchronized PrepareId p() {
        Date date = new Date();
        if (date.getTime() == b) {
            return new PrepareId(date.getTime(), p++);
        }
        p = 1;
        b = date.getTime();
        return new PrepareId(date.getTime(), p++);
    }

    static synchronized CommitId c() {
        Date date = new Date();
        if (date.getTime() == b) {
            return new CommitId(date.getTime(), p++);
        }
        p = 1;
        b = date.getTime();
        return new CommitId(date.getTime(), p++);
    }


    static synchronized SequenceNumber s() {
        Date date = new Date();
        if (date.getTime() == b) {
            return new SequenceNumber(date.getTime(), p++);
        }
        p = 1;
        b = date.getTime();
        return new SequenceNumber(date.getTime(), p++);
    }
}
