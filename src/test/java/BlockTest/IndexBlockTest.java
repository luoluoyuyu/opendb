package BlockTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.openio.jrocksDb.db.IntKey;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.strorage.IndexBlocks;
import net.openio.jrocksDb.strorage.IndexList;
import net.openio.jrocksDb.strorage.IndexOffset;
import net.openio.jrocksDb.transaction.CommitId;
import net.openio.jrocksDb.transaction.PrepareId;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class IndexBlockTest {

    static int p = 1;

    static long b = new Date().getTime();

    public static void main(String[] args) {

        IndexList indexList = new IndexList();

        for (int i = 0; i < 100; i++) {
            Key key = new IntKey(2 + i);
//            Value value = new IntValue(2);
//            KeyValueEntry keyValue = new KeyValueEntry();
//            keyValue.setKey(key);
//            keyValue.setValue(value);
//            keyValue.setPrepareId(p());
//            keyValue.setCommitId(c());
//            keyValue.setSqId(s());
//            keyValue.setType(KeyValueEntry.Type.insert);
            IndexOffset indexOffset = new IndexOffset();
            indexOffset.setOffset(12345 + i);
            indexOffset.setKey(key);
            indexList.add(indexOffset);
        }
        IndexBlocks indexBlocks=new IndexBlocks();
        indexBlocks.setMemArena(new MemArena());
        try {

            RandomAccessFile randomAccessFile=new RandomAccessFile(new File("src/main/resources/Block/indexBlock"),"rw");
            indexBlocks.flushFile(randomAccessFile.getChannel(),0,indexList);
            for(IndexOffset indexOffset:indexBlocks.getList(randomAccessFile.getChannel(),0).getList()){
                System.out.println(indexOffset);
            }
            randomAccessFile.close();
        } catch (IOException e) {
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
