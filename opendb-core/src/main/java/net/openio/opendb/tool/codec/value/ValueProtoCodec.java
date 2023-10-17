package net.openio.opendb.tool.codec.value;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.value.Value;

public interface ValueProtoCodec {

  Value decode(ByteBuf buf, int length);

  void encode(ByteBuf buf, Value value);

  int getByteSize(Value value);
}
