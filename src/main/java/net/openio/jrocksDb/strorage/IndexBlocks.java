package net.openio.jrocksDb.strorage;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.tool.Serializer;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class IndexBlocks {

    private MemArena memArena;

    //return length
    public int flushFile(FileChannel fileChannel,int seek,IndexList list) throws IOException {
        fileChannel.position(seek);
        int size=list.getByteSize()+4;
        ByteBuf buf=memArena.allocator(size);
        buf.writeInt(size-4);
        list.encode(buf);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return size;
    }



    public IndexList getList(FileChannel fileChannel,int seek) throws IOException {
        fileChannel.position(seek);
        ByteBuf buf = memArena.allocator(4);
        buf.writerIndex(4);
        fileChannel.read(buf.nioBuffer());
        int size=buf.readInt();
        buf.release();
        buf=memArena.allocator(size);
        buf.writerIndex(size);
        fileChannel.read(buf.nioBuffer());
        IndexList list=IndexList.decode(buf,size);
        buf.release();
        return list;
    }

    //seek is index seek,not meta seek
    public IndexList getMetaIndexList(FileChannel fileChannel, int IndexSeek, int MetaSeek) throws IOException {

        IndexList indexList=getList(fileChannel,IndexSeek);
        fileChannel.position(MetaSeek);
        int lSize=indexList.size();
        IndexList list=new IndexList();
        for(int i=0;i<lSize;i++){
            list.add(getList(fileChannel,indexList.get(i).getOffset()));

        }

        return list;

    }

    public IndexBlocks(){
        memArena=new MemArena();
    }

}
