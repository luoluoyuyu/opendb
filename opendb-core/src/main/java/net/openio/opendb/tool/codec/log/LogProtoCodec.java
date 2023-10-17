package net.openio.opendb.tool.codec.log;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.log.Log;

import java.util.List;

public interface LogProtoCodec {

  List<Log> decode(ByteBuf buf, int length);

  void encode(ByteBuf buf, Log log);

  void encode(ByteBuf buf, List<Log> logs);

  int getByteSize(Log log);

  int getByteSize(List<Log> log);
}
