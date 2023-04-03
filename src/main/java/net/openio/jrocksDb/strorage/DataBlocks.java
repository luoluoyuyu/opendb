package net.openio.jrocksDb.strorage;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.tool.Serializer;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

@Data
public class DataBlocks {

    private MemArena memArena;

    public int flush(FileChannel fileChannel, int seek, KeyValueEntry keyValue) throws IOException {
        fileChannel.position(seek);
        int size=keyValue.getSize();
        ByteBuf buf=memArena.allocator(size);
        keyValue.encode(buf);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return size;
    }

    public int getKeyValue(FileChannel fileChannel, int seek, List<KeyValueEntry> list) throws IOException {
        fileChannel.position(seek);
        ByteBuf buf=memArena.allocator(4);
        buf.writerIndex(4);
        fileChannel.read(buf.nioBuffer());
        int size= Serializer.decode32(buf);
        buf.release();
        buf=memArena.allocator(size);
        buf.writerIndex(size);
        fileChannel.read(buf.nioBuffer());
        KeyValueEntry keyValueEntry=KeyValueEntry.decode(buf,size);
        buf.release();
        list.add(keyValueEntry);
        return size+4;
    }


    public KeyValueEntry getKeyValue(FileChannel fileChannel, int seek) throws IOException {
        fileChannel.position(seek);
        ByteBuf buf=memArena.allocator(4);
        buf.writerIndex(4);
        fileChannel.read(buf.nioBuffer());
        int size= Serializer.decode32(buf);
        buf.release();
        buf=memArena.allocator(size);
        buf.writerIndex(size);
        fileChannel.read(buf.nioBuffer());
        KeyValueEntry keyValueEntry=KeyValueEntry.decode(buf,size);
        buf.release();
        return keyValueEntry;
    }

    public DataBlocks(){
        memArena=new MemArena();
    }

}
