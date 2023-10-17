/**
 * Licensed to the OpenIO.Net under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openio.opendb.tool.codec;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;


public class Codec {

  public static int decodeVarInt32(ByteBuf byteBuf) {
    int value; // decode first number
    int a = byteBuf.readByte();
    if (a >= 0) {
      return a;
    }
    value = a ^ 0xffffff80;

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 7);
    }

    value = value | ((a ^ 0xffffff80) << 7);

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 14);
    }

    value = value | ((a ^ (0xffffff80)) << 14);

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 21);
    }

    value = value | ((a ^ (0xffffff80)) << 21);

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 28);
    }
    value = value | ((a & 15) << 28); // the value is negative ,
    a = byteBuf.readByte();

    if (a != -1) {
      throw new RuntimeException("this code is wrong");
    }

    a = byteBuf.readByte();
    if (a != -1) {
      throw new RuntimeException("this code is wrong");
    }

    a = byteBuf.readByte();
    if (a != -1) {
      throw new RuntimeException("this code is wrong");
    }
    a = byteBuf.readByte();
    if (a != -1) {
      throw new RuntimeException("this code is wrong");
    }
    a = byteBuf.readByte();
    if (a != 1) {
      throw new RuntimeException("this code is wrong");
    }

    return value;
  }

  public static int decodeVarUInt32(ByteBuf byteBuf) {
    int value; // decode first number
    int a = byteBuf.readByte();
    if (a >= 0) {
      return a;
    }
    value = a ^ 0xffffff80;

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 7);
    }

    value = value | ((a ^ 0xffffff80) << 7);

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 14);
    }

    value = value | ((a ^ (0xffffff80)) << 14);

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 21);
    }

    value = value | ((a ^ (0xffffff80)) << 21);

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 28);
    }
    throw new RuntimeException("this code is wrong");
  }

  public static void encodeVarUInt32(ByteBuf byteBuf, int value) {
    while (true) {
      int value2 = value >>> 7;
      if (value2 == 0) {
        byteBuf.writeByte(value);
        return;
      }
      byteBuf.writeByte((value | 128));

      value = value2;
    }
  }

  public static long decodeVarInt64(ByteBuf byteBuf) {
    long value;
    long a = byteBuf.readByte();
    if (a >= 0L) {
      return a;
    }
    value = a ^ 0xffffffffffffff80L;

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 7L);
    }

    value = value | ((a ^ 0xffffffffffffff80L) << 7L);

    a = byteBuf.readByte();
    if (a >= 0) {
      return value | (a << 14L);
    }

    value = value | ((a ^ 0xffffffffffffff80L) << 14L);

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 21L);
    }

    value = value | ((a ^ 0xffffffffffffff80L) << 21L);

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 28L);
    }
    value = value | ((a ^ 0xffffffffffffff80L) << 28L);

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 35L);
    }
    value = value | ((a ^ 0xffffffffffffff80L) << 35L);

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 42L);
    }
    value = value | ((a ^ 0xffffffffffffff80L) << 42L);

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 49L);
    }
    value = value | ((a ^ 0xffffffffffffff80L) << 49L);

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 56L);
    }
    value = value | ((a ^ 0xffffffffffffff80L) << 56L);

    a = byteBuf.readByte();
    if (a >= 0L) {
      return value | (a << 63L);
    }
    throw new RuntimeException("this code is wrong");

  }

  public static void encodeVarInt32(ByteBuf byteBuf, int value) {
    if (value < 0) {
      encodeVarInt64(byteBuf, value);
      return;
    }
    while (true) {
      int value2 = value >>> 7;
      if (value2 == 0) {
        byteBuf.writeByte(value);
        return;
      }
      byteBuf.writeByte((value | 128));

      value = value2;
    }

  }

  public static void encodeVarInt64(ByteBuf byteBuf, long value) {
    while (true) {
      long value2 = value >>> 7L;
      if (value2 == 0L) {
        byteBuf.writeByte((int) value);
        return;
      }
      byteBuf.writeByte((int) (value | 128L));

      value = value2;
    }

  }

  public static int encodeZigzag32(int value) {
    return (value << 1) ^ (value >> 31);
  }

  public static long encodeZigzag64(long value) {
    return (value << 1L) ^ (value >> 63L);
  }

  public static int decodeZigzag32(int value) {
    return (value >>> 1) ^ -(value & 1);
  }

  public static long decodeZigzag64(long value) {
    return (value >>> 1L) ^ -(value & 1L);
  }

  public static void encodeString(ByteBuf byteBuf, String s) {
    byte[] a = s.getBytes(StandardCharsets.UTF_8);
    int length = a.length;
    Codec.encodeVarInt32(byteBuf, length);
    byteBuf.writeBytes(a);
  }

  public static void encodeByteString(ByteBuf byteBuf, byte[] s) {
    Codec.encodeVarInt32(byteBuf, s.length);
    byteBuf.writeBytes(s);

  }

  public static String decodeString(ByteBuf byteBuf, int length) {
    int read = byteBuf.readerIndex();
    byteBuf.readerIndex(read + length);
    return byteBuf.toString(read, length, StandardCharsets.UTF_8);
  }

  public static byte[] decodeByteString(ByteBuf byteBuf, int length) {
    byte[] bytes = new byte[length];
    byteBuf.readBytes(bytes, 0, length);
    return bytes;
  }

  public static boolean decodeBoolean(ByteBuf byteBuf) {
    return decodeVarInt64(byteBuf) != 0;
  }

  public static void encodeBoolean(ByteBuf byteBuf, Boolean b) {
    long a = b ? 1L : 0L;
    encodeVarInt64(byteBuf, a);
  }

  public static void encodeDouble(ByteBuf byteBuf, double b) {
    byteBuf.writeDoubleLE(b);
  }

  public static void encodeFloat(ByteBuf byteBuf, float b) {
    byteBuf.writeFloatLE(b);
  }

  public static double decodeDouble(ByteBuf byteBuf) {
    return byteBuf.readDoubleLE();
  }

  public static float decodeFloat(ByteBuf byteBuf) {
    return byteBuf.readFloatLE();

  }

  public static void encode32(ByteBuf byteBuf, int b) {
    byteBuf.writeIntLE(b);
  }

  public static void encode64(ByteBuf byteBuf, long b) {
    byteBuf.writeLongLE(b);

  }

  public static int decode32(ByteBuf byteBuf) {
    return byteBuf.readIntLE();
  }

  public static long decode64(ByteBuf byteBuf) {
    return byteBuf.readLongLE();
  }

  public static int computeVarInt32Size(final int value) {
    if (value < 0) {
      return 10;
    }
    if ((value & 0xffffff80) == 0) {
      return 1;
    } else if ((value & 0xffffc000) == 0) {
      return 2;
    } else if ((value & 0xffe00000) == 0) {
      return 3;
    } else if ((value & 0xf0000000) == 0) {
      return 4;
    } else {
      return 5;
    }
  }

  public static int computeVarUInt32Size(final int value) {
    if ((value & 0xffffff80) == 0) {
      return 1;
    } else if ((value & 0xffffc000) == 0) {
      return 2;
    } else if ((value & 0xffe00000) == 0) {
      return 3;
    } else if ((value & 0xf0000000) == 0) {
      return 4;
    } else {
      return 5;
    }
  }

  public static int computeVarInt64Size(final long value) {
    if ((value & 0xffffffffffffff80L) == 0) {
      return 1;
    } else if ((value & 0xffffffffffffc000L) == 0) {
      return 2;
    } else if ((value & 0xffffffffffe00000L) == 0) {
      return 3;
    } else if ((value & 0xfffffffff0000000L) == 0) {
      return 4;
    } else if ((value & 0xfffffff800000000L) == 0) {
      return 5;
    } else if ((value & 0xfffffc0000000000L) == 0) {
      return 6;
    } else if ((value & 0xfffe000000000000L) == 0) {
      return 7;
    } else if ((value & 0xff00000000000000L) == 0) {
      return 8;
    } else if ((value & 0x8000000000000000L) == 0) {
      return 9;
    } else {
      return 10;
    }
  }

  public static void skipUnknownField(int tag, ByteBuf buffer) {
    int tagType = tag & 7;
    switch (tagType) {
      case 0:
        decodeVarInt64(buffer);
        break;
      case 1:
        buffer.skipBytes(8);
        break;
      case 2:
        int len = decodeVarInt32(buffer);
        buffer.skipBytes(len);
        break;
      case 5:
        buffer.skipBytes(4);
        break;
      default:
        throw new IllegalArgumentException("Invalid unknonwn tag type: " + tagType);
    }
  }

}
