package LogTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.db.*;
import net.openio.jrocksDb.log.WalStorage;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.transaction.CommitId;
import net.openio.jrocksDb.transaction.PrepareId;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogTest {


    static int p=1;

    static long b=new Date().getTime();

    static long isnull=0;

    static long nonull=0;



    public static void main(String[] args) throws IOException {
        WalStorage walStorage=new WalStorage();
        walStorage.setMemArena(new MemArena());
//        MemArena mem=new MemArena();
//        Key key=new LongKey(1);
//        Value value=new IntValue(1);
//        KeyValueEntry keyValue = new KeyValueEntry();
//        keyValue.setKey(key);
//        keyValue.setValue(value);
//        keyValue.setPrepareId(p());
//        keyValue.setCommitId(c());
//        keyValue.setSqId(s());
//
//        ByteBuf b=mem.allocator(keyValue.getSize());
//        keyValue.encode(b);
//
//        int a=Serializer.decode32(b);
//
//        System.out.println(KeyValueEntry.decode(b, a));

//        writeOffset(13);
        walStorage.createFile("luo",new ColumnFamilyId(1));


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
            walStorage.write("luo",keyValue,false);
        }
//        writeOffset(100);
//        readOffset();


//          writeOffset(123213);
//        walStorage.createFile("luo",new ColumnFamilyId(1));
        walStorage.write("luo",list,false);


        for(KeyValueEntry value: walStorage.getAllMemTable("luo")){
            System.out.println(value);
        }

    }






    static synchronized PrepareId p(){
        Date date=new Date();
        if(date.getTime()==b){
            return new PrepareId(date.getTime(),p++);
        }
        p=1;
        b=date.getTime();
        return new PrepareId(date.getTime(),p++);
    }

    static synchronized CommitId c(){
        Date date=new Date();
        if(date.getTime()==b){
            return new CommitId(date.getTime(),p++);
        }
        p=1;
        b=date.getTime();
        return new CommitId(date.getTime(),p++);    }

    static synchronized SequenceNumber s(){
        Date date=new Date();
        if(date.getTime()==b){
            return new SequenceNumber(date.getTime(),p++);
        }
        p=1;
        b=date.getTime();
        return new SequenceNumber(date.getTime(),p++);    }


    private static int writeOffseta( int length) throws IOException {

        File file2=new File(Config.WalLogPath+"ll");
        RandomAccessFile accessFile;
        accessFile=new RandomAccessFile(file2,"rw");
        ByteBuf buf = new PooledByteBufAllocator().buffer(8);
        buf.writeLong(Long.MAX_VALUE);
        FileChannel file = accessFile.getChannel();
        file.write(buf.nioBuffer());
        buf=new PooledByteBufAllocator().buffer(8);
        buf.writeLong(Long.MAX_VALUE);
        file.write(buf.nioBuffer());
        buf = new PooledByteBufAllocator().buffer(4);
        file.position(16);
        buf.writeLong(Long.MAX_VALUE);
        file.write(buf.nioBuffer());
        file.force(true);
        buf.release();
        accessFile.close();
        return length;
    }



    private static int readOffset() throws IOException {
        File file2=new File(Config.WalLogPath+"ll");
        RandomAccessFile accessFile;
        accessFile=new RandomAccessFile(file2,"rw");
        ByteBuf buf = new PooledByteBufAllocator().buffer(8);
        accessFile.seek(16);
        FileChannel fileChannel = accessFile.getChannel();
        accessFile.seek(16);
        buf.writerIndex(8);
        fileChannel.read(buf.nioBuffer());
        long length = buf.readLong();
        buf.release();
        fileChannel.close();
        accessFile.close();
        return (int) (Long.MAX_VALUE-length);

    }


    private static int writeOffset( int length) throws IOException {
        File file2=new File(Config.WalLogPath+"ll");
        RandomAccessFile accessFile;
        accessFile=new RandomAccessFile(file2,"rw");
        ByteBuf buf = new PooledByteBufAllocator().buffer(8);
        buf.writeLong(Long.MAX_VALUE-length);
        accessFile.seek(16);
        FileChannel file = accessFile.getChannel();
        file.write(buf.nioBuffer());
        file.force(true);
        buf.clear();
        accessFile.seek(16);
        FileChannel fileChannel = accessFile.getChannel();
        fileChannel.read(buf.nioBuffer());
        buf.writerIndex(8);
        buf.readerIndex(0);
        System.out.println(Long.MAX_VALUE-buf.readLong());
        file.force(true);
        buf.release();
        file.close();
        accessFile.close();
        return length;
    }

}
