package net.openio.jrocksDb.strorage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.openio.jrocksDb.db.BytesKey;
import net.openio.jrocksDb.db.IntKey;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.db.LongKey;
import net.openio.jrocksDb.mem.MemTable;
import net.openio.jrocksDb.tool.Serializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SSTable {

    private String fileName;

    private Key minKey;

    private Key maxKey;

    private int size;

    public final static int filename_Num = 1;
    public final static int filename_Tag = 10;// the value is num<<<3|wireType
    public final static int filename_TagEncodeSize = 1;

    private void encode_filename(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, filename_Tag);
        Serializer.encodeString(buf, this.fileName);
    }

    private static void decode_filename(ByteBuf buf, SSTable a_1) {
        a_1.fileName = Serializer.decodeString(buf, Serializer.decodeVarInt32(buf));
    }

    public final static int size_Num = 2;
    public final static int size_Tag = 16;// the value is num<<<3|wireType
    public final static int size_TagEncodeSize = 1;

    private void encode_size(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, size_Tag);
        Serializer.encodeVarInt32(buf, this.size);
    }

    private static void decode_size(ByteBuf buf, SSTable a_1) {
        a_1.size = Serializer.decodeVarInt32(buf);
    }

    public final static int minkey_Num = 3;
    public final static int minkey_Tag = 26;// the value is num<<<3|wireType
    public final static int minkey_TagEncodeSize = 1;

    private void encode_minkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, minkey_Tag);
        Serializer.encodeVarInt32(buf, this.minKey.getByteSize());
        this.minKey.toByte(buf);
    }

    private static void decode_minkey(ByteBuf buf, SSTable a_1) {
        a_1.minKey = IntKey.getKey(buf);

    }

    public final static int longminkey_Num = 4;
    public final static int longminkey_Tag = 34;// the value is num<<<3|wireType
    public final static int longminkey_TagEncodeSize = 1;

    private void encode_longminkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, longminkey_Tag);
        Serializer.encodeVarInt32(buf, this.minKey.getByteSize());
        this.minKey.toByte(buf);
    }

    private static void decode_longminkey(ByteBuf buf, SSTable a_1) {
        a_1.minKey = LongKey.getKey(buf);
    }


    public final static int bytesminkey_Num = 5;
    public final static int bytesminkey_Tag = 42;// the value is num<<<3|wireType
    public final static int bytesminkey_TagEncodeSize = 1;

    private void encode_bytesminkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, bytesminkey_Tag);
        Serializer.encodeVarInt32(buf, this.minKey.getByteSize());
        this.minKey.toByte(buf);
    }

    private static void decode_bytesminkey(ByteBuf buf, SSTable a_1) {
        a_1.minKey = BytesKey.getKey(buf,Serializer.decodeVarInt32(buf));
    }

    public final static int maxkey_Num = 6;
    public final static int maxkey_Tag = 50;// the value is num<<<3|wireType
    public final static int maxkey_TagEncodeSize = 1;

    private void encode_maxkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, maxkey_Tag);
        Serializer.encodeVarInt32(buf, this.maxKey.getByteSize());
        this.maxKey.toByte(buf);
    }

    private static void decode_maxkey(ByteBuf buf, SSTable a_1) {
        a_1.maxKey = IntKey.getKey(buf);
    }

    public final static int longmaxkey_Num = 7;
    public final static int longmaxkey_Tag = 58;// the value is num<<<3|wireType
    public final static int longmaxkey_TagEncodeSize = 1;

    private void encode_longmaxkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, longmaxkey_Tag);
        Serializer.encodeVarInt32(buf, this.maxKey.getByteSize());
        this.maxKey.toByte(buf);
    }

    private static void decode_longmaxkey(ByteBuf buf, SSTable a_1) {
        a_1.maxKey = LongKey.getKey(buf);
    }

    public final static int bytesmaxkey_Num = 8;
    public final static int bytesmaxkey_Tag = 66;// the value is num<<<3|wireType
    public final static int bytesmaxkey_TagEncodeSize = 1;

    private void encode_bytesmaxkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, bytesmaxkey_Tag);
        Serializer.encodeVarInt32(buf, this.maxKey.getByteSize());
        this.maxKey.toByte(buf);
    }

    private static void decode_bytesmaxkey(ByteBuf buf, SSTable a_1) {
        a_1.maxKey = BytesKey.getKey(buf, Serializer.decodeVarInt32(buf));
    }


    public static SSTable decode(ByteBuf buf, int length_1) {
        SSTable value_1 = new SSTable();
        int f_Index = buf.readerIndex();
        int end=f_Index+length_1;
        while (buf.readerIndex() < end) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case filename_Tag :
                    decode_filename(buf, value_1);
                    break;
                case size_Tag :
                    decode_size(buf, value_1);
                    break;
                case minkey_Tag :
                    decode_minkey(buf, value_1);
                    break;
                case longminkey_Tag :
                    decode_longminkey(buf, value_1);
                    break;
                case bytesminkey_Tag :
                    decode_bytesminkey(buf, value_1);
                    break;
                case maxkey_Tag :
                    decode_maxkey(buf, value_1);
                    break;
                case longmaxkey_Tag :
                    decode_longmaxkey(buf, value_1);
                    break;
                case bytesmaxkey_Tag :
                    decode_bytesmaxkey(buf, value_1);
                    break;
                default :
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        return value_1;
    }

    public void encode(ByteBuf buf) {

            this.encode_filename(buf);


        this.encode_size(buf);

        if (minKey instanceof IntKey) {
            this.encode_minkey(buf);
        }

        if (minKey instanceof LongKey) {
            this.encode_longminkey(buf);
        }

        if (minKey instanceof BytesKey) {
            this.encode_bytesminkey(buf);
        }

        if (maxKey instanceof IntKey) {
            this.encode_maxkey(buf);
        }

        if (maxKey instanceof LongKey) {
            this.encode_longmaxkey(buf);
        }

        if (maxKey instanceof BytesKey) {
            this.encode_bytesmaxkey(buf);
        }

    }

    public int getByteSize() {
        int SSTable_size =0;
        SSTable_size += filename_TagEncodeSize;
        SSTable_size += Serializer.computeVarInt32Size(ByteBufUtil.utf8Bytes(fileName));
        SSTable_size += ByteBufUtil.utf8Bytes(fileName);

        SSTable_size += size_TagEncodeSize;
        SSTable_size += Serializer.computeVarInt32Size(size);

        if (minKey instanceof IntKey) {
            SSTable_size += minkey_TagEncodeSize;

            SSTable_size += Serializer.computeVarInt32Size(minKey.getByteSize());
            SSTable_size += minKey.getByteSize();
        }

        if (minKey instanceof LongKey) {
            SSTable_size += longminkey_TagEncodeSize;
            SSTable_size += Serializer.computeVarInt32Size(minKey.getByteSize());
            SSTable_size += minKey.getByteSize();
        }

        if (minKey instanceof BytesKey) {
            SSTable_size += bytesminkey_TagEncodeSize;
            SSTable_size+= Serializer.computeVarInt32Size(minKey.getByteSize());
            SSTable_size += minKey.getByteSize();
        }

        if (maxKey instanceof IntKey) {
            SSTable_size += maxkey_TagEncodeSize;
            SSTable_size += Serializer.computeVarInt32Size(maxKey.getByteSize());
            SSTable_size += maxKey.getByteSize();
        }

        if (maxKey instanceof LongKey) {
            SSTable_size += longmaxkey_TagEncodeSize;
            SSTable_size += Serializer.computeVarInt32Size(maxKey.getByteSize());
            SSTable_size += maxKey.getByteSize();
        }

        if (maxKey instanceof BytesKey) {
            SSTable_size += bytesmaxkey_TagEncodeSize;

            SSTable_size += Serializer.computeVarInt32Size(maxKey.getByteSize());
            SSTable_size += maxKey.getByteSize();
        }
        return SSTable_size;
    }



}
