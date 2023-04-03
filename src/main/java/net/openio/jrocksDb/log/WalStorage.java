package net.openio.jrocksDb.log;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.tool.Serializer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Data
public class WalStorage {

    private static String filePath = Config.WalLogPath;

    private MemArena memArena;

    //head columnFamilyId:8 isflush:1 off:4

    final static int fileHead = 24;

    final static int IdSeek = 0;

    final static int idSize = 8;

    final static int flushSeek = 8;

    final static int flushSize = 8;

    final static int offSeek = 16;

    final static int offsetSize = 8;


    public boolean write(String fileName, KeyValueEntry keyValueEntry, boolean isFlush) {
        File file = new File(filePath + fileName);
        if (!file.exists()) return false;
        RandomAccessFile randomAccessFile = null;
        if (isFlush) {
            ByteBuf buf = memArena.allocator(flushSeek);
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                buf.writeLong(1l);
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
            randomAccessFile.seek(offset);
            int size = keyValueEntry.getSize();
            ByteBuf buf = memArena.allocator(size);
            keyValueEntry.encode(buf);
            fileChannel.write(buf.nioBuffer());
            offset += size;
            buf.release();
            writeOffset(randomAccessFile, offset);
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public boolean write(String fileName, List<KeyValueEntry> list, boolean isFlush) {
        File file = new File(filePath + fileName);
        if (!file.exists()) return false;
        RandomAccessFile randomAccessFile = null;
        if (isFlush) {
            ByteBuf buf = memArena.allocator(flushSeek);
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                buf.writeLong(1l);
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
            randomAccessFile.seek(offset);
            for (KeyValueEntry keyValueEntry:list) {
                int size = keyValueEntry.getSize();
                ByteBuf buf = memArena.allocator(size);
                keyValueEntry.encode(buf);
                fileChannel.write(buf.nioBuffer());
                offset += size;
                buf.release();
            }
            writeOffset(randomAccessFile, offset);
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public List<KeyValueEntry> getAllMemTable(String fileName) {

        List<KeyValueEntry> list = new ArrayList<>();

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
            int po = fileHead;
            while (length > 0) {
                fileChannel.position(po);
                size.writerIndex(4);
                fileChannel.read(size.nioBuffer());
                int le = Serializer.decode32(size);
                data = memArena.allocator(le);
                data.writerIndex(le);
                fileChannel.read(data.nioBuffer());
                KeyValueEntry keyValueEntry = KeyValueEntry.decode(data, le);
                list.add(keyValueEntry);
                length = length - 4 - le;
                po = po + 4 + le;
                data.release();
                size.clear();
            }
            rfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean isFlush(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return false;
        RandomAccessFile randomAccessFile = null;
        ByteBuf flush = memArena.allocator(flushSize);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(flushSeek);
            randomAccessFile.getChannel().read(flush.nioBuffer());
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        boolean v = flush.readLong() != 0;
        flush.release();
        return v;
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
        ByteBuf buf = memArena.allocator(8);
        Serializer.encode64(buf, Long.MAX_VALUE - columnFamilyId.getCId());
        ByteBuf flush = memArena.allocator(8);
        flush.writeLong(Long.MAX_VALUE);
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();
            fileChannel = fileChannel.position(0);
            fileChannel.write(buf.nioBuffer());
            fileChannel.position(flushSeek);
            fileChannel.write(flush.nioBuffer());

            buf.clear();
            fileChannel.position(offSeek);
            buf.writeLong(Long.MAX_VALUE - fileHead);
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
        return (int) (Long.MAX_VALUE - length);
    }


    private int writeOffset(RandomAccessFile accessFile, int length) throws IOException {
        ByteBuf buf = memArena.allocator(offsetSize);

        buf.writeLong(Long.MAX_VALUE - length);
        accessFile.seek(offSeek);
        FileChannel file = accessFile.getChannel();
        file.write(buf.nioBuffer());
        file.force(true);
        buf.release();
        return length;
    }

    public WalStorage() {
        memArena = new MemArena();
    }


}
