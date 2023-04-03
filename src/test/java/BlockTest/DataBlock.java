package BlockTest;

import net.openio.jrocksDb.db.IntValue;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.db.LongKey;
import net.openio.jrocksDb.db.Value;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.strorage.DataBlocks;
import net.openio.jrocksDb.transaction.CommitId;
import net.openio.jrocksDb.transaction.PrepareId;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBlock {


    static int p = 1;

    static long b = new Date().getTime();

    public static void main(String[] args) {
        File file=new File("src/main/resources/Block/DataBlock");

        List<KeyValueEntry> list=new ArrayList<>();
        for(int i=0;i<1000;i++){
            Key key=new LongKey(i);
            Value value=new IntValue(i);
            KeyValueEntry keyValue = new KeyValueEntry();
            keyValue.setKey(key);
            keyValue.setValue(value);
            keyValue.setPrepareId(p());
            keyValue.setCommitId(c());
            keyValue.setSqId(s());
            keyValue.setType(KeyValueEntry.Type.insert);
            list.add(keyValue);
        }
        try {
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");

            DataBlocks dataBlocks=new DataBlocks();
            dataBlocks.setMemArena(new MemArena());
            int seek=0;
            for(KeyValueEntry keyValueEntry: list) {
                seek+=dataBlocks.flush(randomAccessFile.getChannel(), seek, keyValueEntry);
            }

            seek=0;
            List<KeyValueEntry> list1=new ArrayList<>();
            for(int i=0;i<list.size();i++){
                seek+=dataBlocks.getKeyValue(randomAccessFile.getChannel(),seek,list1);
                System.out.println(list.get(i)+"----"+list1.get(i));
            }
        }catch (IOException e){
            e.printStackTrace();
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
