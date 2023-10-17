package net.openio.opendb.tool.codec.sstable;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.storage.sstable.IndexBlock;
import net.openio.opendb.storage.sstable.IndexData;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;


public class IndexBlockProtoCodec {


  public static final int ID_NUM = 1;
  public static final int ID_TAG = 8;
  public static final int ID_TAG_ENCODE_SIZE = 1;


  public static final int OFFSET_NUM = 2;
  public static final int OFFSET_TAG = 16;
  public static final int OFFSET_TAG_ENCODE_SIZE = 1;


  public static final int INDEX_DATA_SIZE_NUM = 3;
  public static final int INDEX_DATA_SIZE_TAG = 24;
  public static final int INDEX_DATA_SIZE_TAG_ENCODE_SIZE = 1;


  public static final int INDEX_DATA_NUM = 4;
  public static final int INDEX_DATA_TAG = 34;
  public static final int INDEX_DATA_TAG_ENCODE_SIZE = 1;


  private static void encodeId(ByteBuf buf, IndexBlock indexBlock) {
    Codec.encodeVarInt32(buf, ID_TAG);
    Codec.encodeVarInt64(buf, Codec.encodeZigzag64(indexBlock.getId()));
  }

  private static void decodeId(ByteBuf buf, IndexBlock indexBlock) {
    indexBlock.setId(Codec.decodeZigzag64(Codec.decodeVarInt64(buf)));
  }

  private static void encodeOffset(ByteBuf buf, IndexBlock indexBlock) {
    Codec.encodeVarInt32(buf, OFFSET_TAG);
    Codec.encodeVarUInt32(buf, Codec.encodeZigzag32(indexBlock.getOffset()));
  }

  private static void decodeOffset(ByteBuf buf, IndexBlock indexBlock) {
    indexBlock.setOffset(Codec.decodeZigzag32(Codec.decodeVarInt32(buf)));
  }

  private static void encodeIndexDataSize(ByteBuf buf, IndexBlock indexBlock) {
    Codec.encodeVarInt32(buf, INDEX_DATA_SIZE_TAG);
    Codec.encodeVarUInt32(buf, Codec.encodeZigzag32(indexBlock.getIndexDataSize()));
  }

  private static void decodeIndexDataSize(ByteBuf buf, IndexBlock indexBlock) {
    indexBlock.setIndexDataSize(Codec.decodeZigzag32(Codec.decodeVarInt32(buf)));
  }

  private static void decodeIndexData(ByteBuf buf, IndexBlock indexBlock) {
    indexBlock.addNotComSize(IndexDataProtoCodec.decode(buf, Codec.decodeVarInt32(buf), indexBlock.getSsTable()));
  }

  private static void encode_indexdata(ByteBuf buf, IndexBlock indexBlock) {
    for (IndexData indexData : indexBlock.getDataList()) {
      Codec.encodeVarInt32(buf, INDEX_DATA_TAG);
      Codec.encodeVarInt32(buf, indexData.getSize());
      IndexDataProtoCodec.encode(buf, indexData);

    }
  }

  public static IndexBlock decode(ByteBuf buf, SSTable ssTable) {
    IndexBlock value = new IndexBlock();
    value.setSsTable(ssTable);
    int fIndex = buf.readerIndex();
    while (buf.readerIndex() < buf.writerIndex()) {
      int rIndex = buf.readerIndex();
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case ID_TAG:
          decodeId(buf, value);
          break;
        case OFFSET_TAG:
          decodeOffset(buf, value);
          break;
        case INDEX_DATA_SIZE_TAG:
          decodeIndexDataSize(buf, value);
          break;
        case INDEX_DATA_TAG:
          decodeIndexData(buf, value);
          break;
        default:
          value.setSize(rIndex - fIndex - getHeadSize(value));
          return value;
      }
    }
    value.setSize(buf.readerIndex() - fIndex - getHeadSize(value));
    return value;
  }

  public static void encode(ByteBuf buf, IndexBlock indexBlock) {

    encodeId(buf, indexBlock);

    encodeOffset(buf, indexBlock);

    encodeIndexDataSize(buf, indexBlock);

    encode_indexdata(buf, indexBlock);
    if (buf.capacity() > buf.writerIndex()) {
      buf.writeByte(0);
    }
  }

  public static int getByteSize(IndexBlock indexBlock) {

    int length = getHeadSize(indexBlock);
    for (IndexData indexData : indexBlock.getDataList()) {
      int i = indexData.getSize();
      length += Codec.computeVarInt32Size(i) + i;
    }
    length += indexBlock.getDataList().size() * INDEX_DATA_TAG_ENCODE_SIZE;
    return length;
  }

  public static int getHeadSize(IndexBlock indexBlock) {
    int length = INDEX_DATA_SIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(Codec.encodeZigzag32(indexBlock.getIndexDataSize()));

    length += ID_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt64Size(Codec.encodeZigzag64(indexBlock.getId()));

    length += OFFSET_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(Codec.encodeZigzag32(indexBlock.getOffset()));
    return length;
  }

  public static int getIndexDataSize(IndexData indexData) {
    int l = indexData.getSize();
    l += Codec.computeVarInt32Size(l);
    return INDEX_DATA_TAG_ENCODE_SIZE + l;
  }

  public static int getIndexDataSize(int indexDataSize) {
    indexDataSize += Codec.computeVarInt32Size(indexDataSize) + indexDataSize;
    return INDEX_DATA_TAG_ENCODE_SIZE + indexDataSize;
  }


}
