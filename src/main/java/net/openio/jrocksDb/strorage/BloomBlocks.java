package net.openio.jrocksDb.strorage;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.tool.Serializer;

import java.io.IOException;
import java.nio.channels.FileChannel;

@Data
public class BloomBlocks {

    MemArena memArena;

    public int[] getBloomFile(FileChannel fileChannel,int seek) throws IOException {
        ByteBuf buf=memArena.allocator(4);
        fileChannel.position(seek);
        buf.writerIndex(4);
        fileChannel.read(buf.nioBuffer());
        int size = Serializer.decode32(buf);
        buf.clear();
        buf.writerIndex(4);
        fileChannel.read(buf.nioBuffer());
        int length=Serializer.decode32(buf);
        buf.release();
        int[] bloom=new int[length];
        buf=memArena.allocator(size);
        buf.writerIndex(size);
        fileChannel.read(buf.nioBuffer());
        int end=buf.readerIndex()+size;
        int i=0;
        while (buf.readerIndex()<end){
            bloom[i++]=Serializer.decodeVarInt32(buf);
        }
        buf.release();
        return bloom;
    }

    public int flush(FileChannel fileChannel,int seek,int[] bloom) throws IOException {
        int size=0;
        for(int i: bloom){
            size+= Serializer.computeVarInt32Size(i);
        }
        fileChannel.position(seek);
        ByteBuf buf=memArena.allocator(4);
        Serializer.encode32(buf,size);
        fileChannel.write(buf.nioBuffer());
        buf.clear();
        Serializer.encode32(buf,bloom.length);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        buf=memArena.allocator(size);
        for(int i:bloom){
            Serializer.encodeVarInt32(buf,i);
        }
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return size+8;
    }

    public BloomBlocks(){
        memArena=new MemArena();
    }
}
