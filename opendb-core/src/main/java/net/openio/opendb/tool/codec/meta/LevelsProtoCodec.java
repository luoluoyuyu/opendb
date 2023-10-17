package net.openio.opendb.tool.codec.meta;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.storage.metadata.Level;
import net.openio.opendb.storage.metadata.Levels;
import net.openio.opendb.storage.sstable.SSTable;
import net.openio.opendb.tool.codec.Codec;

public class LevelsProtoCodec {

  public static final int LEVELS_NUM = 1;
  public static final int LEVELS_TAG = 10;
  public static final int LEVELS_TAG_ENCODE_SIZE = 1;

  public static final int LEVEL_0_STOPWRITESTRIGGER_NUM = 2;
  public static final int LEVEL_0_STOPWRITESTRIGGER_TAG = 16;
  public static final int LEVEL_0_STOPWRITESTRIGGER_TAG_ENCODE_SIZE = 1;

  public static final int LEVEL_0_SLOWDOWNWRITESTRIGGER_NUM = 3;
  public static final int LEVEL_0_SLOWDOWNWRITESTRIGGER_TAG = 24;
  public static final int LEVEL_0_SLOWDOWNWRITESTRIGGER_TAG_ENCODE_SIZE = 1;


  public static final int ALLSIZE_NUM = 4;
  public static final int ALLSIZE_TAG = 32;
  public static final int ALLSIZE_TAG_ENCODE_SIZE = 1;

  public static final int BEINGCOMPACTEDLEVEL_NUM = 5;
  public static final int BEINGCOMPACTEDLEVEL_TAG = 40;
  public static final int BEINGCOMPACTEDLEVEL_TAG_ENCODE_SIZE = 1;

  public static final int WAITTOMERGE_NUM = 6;
  public static final int WAITTOMERGE_TAG = 50;
  public static final int WAITTOMERGE_TAG_ENCODE_SIZE = 1;


  private static void decodeLevels(ByteBuf buf, Levels levels) {
    levels.addLevels(LevelProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeLevels(ByteBuf buf, Levels levels) {
    for (Level value : levels.getLevels()) {
      Codec.encodeVarInt32(buf, LEVELS_TAG);
      Codec.encodeVarInt32(buf, LevelProtoCodec.getByteSize(value));
      LevelProtoCodec.encode(buf, value);
    }
  }

  private static void encodeLevel0StopWritesTrigger(ByteBuf buf, Levels levels) {
    Codec.encodeVarInt32(buf, LEVEL_0_STOPWRITESTRIGGER_TAG);
    Codec.encodeVarInt32(buf, levels.getLevel0CompactionTrigger());
  }

  private static void decodeLevel0StopWritesTrigger(ByteBuf buf, Levels levels) {
    levels.setLevel0CompactionNumTrigger(Codec.decodeVarInt32(buf));
  }

  private static void encodeLevel0SlowdownWritesTrigger(ByteBuf buf, Levels levels) {
    Codec.encodeVarInt32(buf, LEVEL_0_SLOWDOWNWRITESTRIGGER_TAG);
    Codec.encodeVarInt32(buf, levels.getLevel0CompactionTrigger());
  }

  private static void decodeLevel0SlowdownWritesTrigger(ByteBuf buf, Levels levels) {
    levels.setLevel0CompactionTrigger(Codec.decodeVarInt32(buf));
  }

  private static void encodeAllSize(ByteBuf buf, Levels levels) {
    Codec.encodeVarInt32(buf, ALLSIZE_TAG);
    Codec.encodeVarInt32(buf, levels.getAllSize());
  }

  private static void decodeAllSize(ByteBuf buf, Levels levels) {
    levels.setAllSize(Codec.decodeVarInt32(buf));
  }

  private static void encodeBeingCompactedLevel(ByteBuf buf, Levels levels) {
    Codec.encodeVarInt32(buf, BEINGCOMPACTEDLEVEL_TAG);
    Codec.encodeVarInt32(buf, levels.getBeingCompactedLevel());
  }

  private static void decodeBeingCompactedLevel(ByteBuf buf, Levels levels) {
    levels.setBeingCompactedLevel(Codec.decodeVarInt32(buf));
  }

  private static void decodeWaitToMerge(ByteBuf buf, Levels levels) {
    levels.addWaitToMerge(SSTableProtoCodec.decode(buf, Codec.decodeVarInt32(buf)));
  }

  private static void encodeWaitToMerge(ByteBuf buf, Levels levels) {
    for (SSTable value : levels.getWaitToMerge()) {
      Codec.encodeVarInt32(buf, WAITTOMERGE_TAG);
      Codec.encodeVarInt32(buf, SSTableProtoCodec.getByteSize(value));
      SSTableProtoCodec.encode(buf, value);
    }
  }

  public static void encode(ByteBuf buf, Levels levels) {

    encodeLevels(buf, levels);

    encodeLevel0StopWritesTrigger(buf, levels);

    encodeLevel0SlowdownWritesTrigger(buf, levels);

    encodeAllSize(buf, levels);

    encodeBeingCompactedLevel(buf, levels);

    encodeWaitToMerge(buf, levels);

  }

  public static Levels decode(ByteBuf buf, int length) {
    Levels value = new Levels();
    int end = buf.readerIndex() + length;
    while (buf.readerIndex() < end) {
      int num = Codec.decodeVarInt32(buf);
      switch (num) {
        case LEVELS_TAG:
          decodeLevels(buf, value);
          break;
        case LEVEL_0_STOPWRITESTRIGGER_TAG:
          decodeLevel0StopWritesTrigger(buf, value);
          break;
        case LEVEL_0_SLOWDOWNWRITESTRIGGER_TAG:
          decodeLevel0SlowdownWritesTrigger(buf, value);
          break;
        case ALLSIZE_TAG:
          decodeAllSize(buf, value);
          break;
        case BEINGCOMPACTEDLEVEL_TAG:
          decodeBeingCompactedLevel(buf, value);
          break;
        case WAITTOMERGE_TAG:
          decodeWaitToMerge(buf, value);
          break;
        default:
          Codec.skipUnknownField(num, buf);
      }
    }
    return value;
  }

  public static int getByteSize(Levels levels) {
    int length = LEVELS_TAG_ENCODE_SIZE * levels.getLevels().size();
    int i = 0;
    for (Level value : levels.getLevels()) {
      i = LevelProtoCodec.getByteSize(value);
      length += Codec.computeVarInt32Size(i) + i;
    }

    length += LEVEL_0_STOPWRITESTRIGGER_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(levels.getLevel0CompactionNumTrigger());

    length += LEVEL_0_SLOWDOWNWRITESTRIGGER_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(levels.getLevel0CompactionTrigger());

    length += ALLSIZE_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(levels.getAllSize());

    length += BEINGCOMPACTEDLEVEL_TAG_ENCODE_SIZE;
    length += Codec.computeVarInt32Size(levels.getBeingCompactedLevel());

    length += WAITTOMERGE_TAG_ENCODE_SIZE * levels.getWaitToMerge().size();
    for (SSTable value : levels.getWaitToMerge()) {
      i = SSTableProtoCodec.getByteSize(value);
      length += Codec.computeVarInt32Size(i) + i;
    }

    return length;
  }

}
