package net.openio.opendb.tool.codec.key;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.model.key.Key;

public interface KeyProtoCodec {

  Key decode(ByteBuf buf, int length);

  void encode(ByteBuf buf, Key key);

  int getByteSize(Key key);

}
