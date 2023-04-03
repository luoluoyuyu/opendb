package net.openio.jrocksDb.strorage;

import io.netty.buffer.ByteBuf;
import net.openio.jrocksDb.memArena.MemArena;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileHeadBlock {

    public final  static int tIdSeek=0;

    public final  static int tIdSize=8;

    public  final  static int DataOfferSeek=8;

    public  final  static int DataOfferSize=4;

    public  final  static int bloomOfferSeek=12;

    public  final  static int bloomOfferSize=12;

    public  final  static int metaOfferSeek=16;//二级索引元数据

    public  final  static int metaOfferSize=4;

    public  final  static int IndexOfferSeek=20;

    public   final  static int IndexOfferSize=4;



//|---tid---|----DataOffset---|------BloomOffer------|-------Meta-------|-------IndexOffset-------|
//|---8-----|-------4---------|------4---------------|--------4---------|------------4------------|

    public   final static  int FileHeadSize=tIdSize+DataOfferSize+bloomOfferSize+IndexOfferSize+metaOfferSize;


    private MemArena memArena;

    public long getColumnFamilyId(FileChannel fileChannel) throws IOException {
        ByteBuf buf=memArena.allocator(tIdSize);
        fileChannel.position(tIdSeek);
        buf.writerIndex(tIdSize);
        fileChannel.read(buf.nioBuffer());
        long id=buf.readLong();
        buf.release();
        return id;
    }

    public long setColumnFamilyId(FileChannel fileChannel, long id) throws IOException {
        ByteBuf buf=memArena.allocator(tIdSize);
        buf.writeLong(id);
        fileChannel.position(tIdSeek);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return id;
    }

    public int getDataOffset(FileChannel fileChannel) throws IOException {
        ByteBuf buf = memArena.allocator(DataOfferSize);
        fileChannel.position(DataOfferSeek);
        buf.writerIndex(DataOfferSize);
        fileChannel.read(buf.nioBuffer());
        int offset = buf.readInt();
        buf.release();
        return offset;
    }

    public int setDataOffset(FileChannel fileChannel, int offset) throws IOException {
        ByteBuf buf = memArena.allocator(DataOfferSize);
        buf.writeInt(offset);
        fileChannel.position(DataOfferSeek);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return offset;
    }

    public int getBloomFilterOffset(FileChannel fileChannel) throws IOException {
        ByteBuf buf = memArena.allocator(bloomOfferSize);
        fileChannel.position(bloomOfferSeek);
        buf.writerIndex(bloomOfferSize);
        fileChannel.read(buf.nioBuffer());
        int offset = buf.readInt();
        buf.release();
        return offset;
    }

    public int setBloomFilterOffset(FileChannel fileChannel, int offset) throws IOException {
        ByteBuf buf = memArena.allocator(bloomOfferSize);
        buf.writeInt(offset);
        fileChannel.position(bloomOfferSeek);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return offset;
    }

    public int getIndexOffset(FileChannel fileChannel) throws IOException {
        ByteBuf buf = memArena.allocator(IndexOfferSize);
        fileChannel.position(IndexOfferSeek);
        buf.writerIndex(IndexOfferSize);
        fileChannel.read(buf.nioBuffer());
        int offset = buf.readInt();
        buf.release();
        return offset;
    }

    public int setIndexOffset(FileChannel fileChannel, int offset) throws IOException {
        ByteBuf buf = memArena.allocator(IndexOfferSize);
        buf.writeInt(offset);
        fileChannel.position(IndexOfferSeek);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return offset;
    }

    public int getMetaOffset(FileChannel fileChannel) throws IOException {
        ByteBuf buf = memArena.allocator(metaOfferSize);
        fileChannel.position(metaOfferSeek);
        buf.writerIndex(metaOfferSize);
        fileChannel.read(buf.nioBuffer());
        int offset = buf.readInt();
        buf.release();
        return offset;
    }

    public int setMetaOffset(FileChannel fileChannel, int offset) throws IOException {
        ByteBuf buf = memArena.allocator(metaOfferSize);
        buf.writeInt(offset);
        fileChannel.position(metaOfferSeek);
        fileChannel.write(buf.nioBuffer());
        buf.release();
        return offset;
    }

    public FileHeadBlock(){
        memArena=new MemArena(true);
    }


}
