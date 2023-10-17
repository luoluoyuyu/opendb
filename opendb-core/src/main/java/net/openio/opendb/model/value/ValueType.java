package net.openio.opendb.model.value;

import io.netty.buffer.ByteBuf;
import net.openio.opendb.tool.codec.value.BytesValueProtoCodec;
import net.openio.opendb.tool.codec.value.DoubleValueProtoCodec;
import net.openio.opendb.tool.codec.value.FloatValueProtoCodec;
import net.openio.opendb.tool.codec.value.IntValueProtoCodec;
import net.openio.opendb.tool.codec.value.LongValueProtoCodec;
import net.openio.opendb.tool.codec.value.StringValueProtoCodec;
import net.openio.opendb.tool.codec.value.ValueProtoCodec;


public enum ValueType {
  intValue(1, new IntValueProtoCodec()),

  longValue(2, new LongValueProtoCodec()),

  bytesValue(3, new BytesValueProtoCodec()),

  doubleValue(4, new DoubleValueProtoCodec()),

  floatValue(5, new FloatValueProtoCodec()),

  stringValue(6, new StringValueProtoCodec());

  public static ValueType get(int tag) {
    if (tag == 1) {
      return ValueType.intValue;
    }
    if (tag == 2) {
      return ValueType.longValue;
    }
    if (tag == 3) {
      return ValueType.bytesValue;
    }
    if (tag == 4) {
      return ValueType.doubleValue;
    }
    if (tag == 5) {
      return ValueType.floatValue;
    }
    if (tag == 6) {
      return ValueType.stringValue;
    }
    return null;
  }

  private final int num;

  private final ValueProtoCodec valueProtoCodec;

  ValueType(int num, ValueProtoCodec valueProtoCodec) {
    this.num = num;
    this.valueProtoCodec = valueProtoCodec;
  }

  public int getNum() {
    return this.num;
  }

  public Value decode(ByteBuf buf, int length) {
    return valueProtoCodec.decode(buf, length);
  }

  public void encode(ByteBuf buf, Value value) {
    valueProtoCodec.encode(buf, value);
  }

  public int getByteSize(Value value) {
    return valueProtoCodec.getByteSize(value);
  }


  public boolean isType(Value value) {
    switch (num) {
      case 1:
        return value instanceof IntValue;
      case 2:
        return value instanceof LongValue;
      case 3:
        return value instanceof BytesValue;
      case 4:
        return value instanceof DoubleValue;
      case 5:
        return value instanceof FloatValue;
      case 6:
        return value instanceof StringValue;
      default:
        return false;
    }
  }
}

