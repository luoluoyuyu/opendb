package net.openio.jrocksDb.log;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.db.TransactionKeyValue;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.tool.Serializer;
import net.openio.jrocksDb.transaction.CommitId;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLogStorage {


    String filePath = Config.WalLogPath;

    MemArena memArena;

    //head transactionId:8 isflush:8 off:8



    final int IdSeek = 0;

    final int idSize = 8;

    final int commitSeek = 8;

    final int commitSize = 16;


    final int offSeek = 24;

    final int offsetSize = 8;

    final int fileHead = idSize+commitSize+offsetSize;


    public boolean write(String fileName, List<TransactionKeyValue> list, boolean isCommit, CommitId commitId) {
        File file = new File(filePath + fileName);
        if (!file.exists()) return false;
        RandomAccessFile randomAccessFile = null;
        if (isCommit) {
            ByteBuf buf=memArena.allocator(commitId.getByteSize()+4);
            try {
                buf.writerIndex(commitId.getByteSize());
                commitId.encode(buf);
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.getChannel().write(buf.nioBuffer());
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            buf.release();
        }
        int offset = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "rwd");
            FileChannel fileChannel = randomAccessFile.getChannel();
            offset = readOffset(randomAccessFile);
            for (TransactionKeyValue keyValueEntry : list) {
                randomAccessFile.seek(offset);
                int size = keyValueEntry.getByteSize();
                ByteBuf buf = memArena.allocator(size);
                keyValueEntry.encode(buf);
                fileChannel.write(buf.nioBuffer());
                offset += size;
                buf.release();
            }
            writeOffset(randomAccessFile,offset);
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }


    public List<TransactionKeyValue> getAllMemTable(String fileName) {

        List<TransactionKeyValue> list = new ArrayList<>();

        File file = new File(filePath + fileName);
        if (!file.exists()) return null;
        RandomAccessFile rfile = null;
        try {
            rfile = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = rfile.getChannel();
            int offset = readOffset(rfile);
            int length = offset - fileHead;
            ByteBuf data;
            ByteBuf size = memArena.allocator(4);
            int po=fileHead;
            while (length > 0) {
                fileChannel.position(po);
                size.writerIndex(4);
                fileChannel.read(size.nioBuffer());
                int le = Serializer.decode32(size);
                data = memArena.allocator(le);
                data.writerIndex(le);
                fileChannel.read(data.nioBuffer());
                TransactionKeyValue keyValueEntry= TransactionKeyValue.decode(data, le);
                list.add(keyValueEntry);
                length=length-4-le;
                po=po+4+le;
                data.release();
                size.clear();
            }
            rfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean isCommit(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return false;
        RandomAccessFile randomAccessFile = null;
        ByteBuf flush=memArena.allocator(commitSize);
        int l=0;
        flush.writerIndex(4);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(commitSeek);
            randomAccessFile.getChannel().read(flush.nioBuffer());
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        l=flush.readInt();
        boolean v= l != 0;
        flush.release();
        return v;
    }

    public long getTransactionId(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return 0;
        RandomAccessFile randomAccessFile = null;
        ByteBuf flush=memArena.allocator(idSize);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(IdSeek);
            flush.writerIndex(8);
            randomAccessFile.getChannel().read(flush.nioBuffer());
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        long id=flush.readLong();
        flush.release();
        return id;
    }

    public boolean commit(String fileName,CommitId commitId) {
        File file = new File(fileName);
        if (!file.exists()) return false;
        RandomAccessFile randomAccessFile = null;
        ByteBuf byteBuf=memArena.allocator(commitId.getByteSize());
        byteBuf.writeInt(commitId.getByteSize());
        commitId.encode(byteBuf);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(commitSeek);
            randomAccessFile.getChannel().write(byteBuf.nioBuffer());
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        byteBuf.release();
        return true;
    }


    public boolean delete(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return file.delete();
        }

        return false;

    }

    public boolean createFile(String fileName, ColumnFamilyId columnFamilyId) {
        File file = new File(filePath + fileName);
        if (file.exists()) {
            return false;
        }

        try {
            if (!file.createNewFile()) return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        RandomAccessFile randomAccessFile = null;
        ByteBuf buf = memArena.allocator(idSize);
        Serializer.encode64(buf, Long.MAX_VALUE-columnFamilyId.getCId());
        ByteBuf flush = memArena.allocator(commitSize);
        flush.writeLong(0);
        flush.writeLong(0);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();
            fileChannel=fileChannel.position(0);
            fileChannel.write(buf.nioBuffer());
            fileChannel.position(commitSeek);
            fileChannel.write(flush.nioBuffer());
            buf.clear();
            fileChannel.position(offSeek);
            buf.writeLong(Long.MAX_VALUE-fileHead);
            fileChannel.write(buf.nioBuffer());
            fileChannel.force(true);
            buf.release();
            flush.release();
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private int readOffset(RandomAccessFile accessFile) throws IOException {
        ByteBuf buf = memArena.allocator(offsetSize);
        accessFile.seek(offSeek);
        FileChannel fileChannel = accessFile.getChannel();
        buf.writerIndex(offsetSize);
        fileChannel.read(buf.nioBuffer());
        long length = buf.readLong();
        buf.release();
        return (int) (Long.MAX_VALUE-length);

    }


    private int writeOffset(RandomAccessFile accessFile, int length) throws IOException {
        ByteBuf buf = memArena.allocator(offsetSize);

        buf.writeLong(Long.MAX_VALUE-length);
        accessFile.seek(offSeek);
        FileChannel file = accessFile.getChannel();
        file.write(buf.nioBuffer());
        file.force(true);
        buf.release();
        return length;
    }
}
