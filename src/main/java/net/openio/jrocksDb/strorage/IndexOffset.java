package net.openio.jrocksDb.strorage;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.openio.jrocksDb.db.BytesKey;
import net.openio.jrocksDb.db.IntKey;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.db.LongKey;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.tool.Serializer;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexOffset {

    private Key key;

    private int offset;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexOffset that = (IndexOffset) o;
        return offset == that.offset && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return key.getHash();
    }


    public final static int key_Num = 1;
    public final static int key_Tag = 10;// the value is num<<<3|wireType
    public final static int key_TagEncodeSize = 1;

    private void encode_key(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, key_Tag);
        key.toByte(buf);
    }

    private static void decode_key(ByteBuf buf, IndexOffset a_1) {
        a_1.key = BytesKey.getKey(buf, Serializer.decodeVarInt32(buf));
    }


    public final static int intkey_Num = 2;
    public final static int intkey_Tag = 16;// the value is num<<<3|wireType
    public final static int intkey_TagEncodeSize = 1;

    private void encode_intkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, intkey_Tag);
        key.toByte(buf);
    }

    private static void decode_intkey(ByteBuf buf, IndexOffset a_1) {
        a_1.key = IntKey.getKey(buf);
    }


    public final static int longkey_Num = 3;
    public final static int longkey_Tag = 24;// the value is num<<<3|wireType
    public final static int longkey_TagEncodeSize = 1;

    private void encode_longkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, longkey_Tag);
        key.toByte(buf);
    }

    private static void decode_longkey(ByteBuf buf, IndexOffset a_1) {
        a_1.key = LongKey.getKey(buf);
    }

    public final static int offset_Num = 4;
    public final static int offset_Tag = 32;// the value is num<<<3|wireType
    public final static int offset_TagEncodeSize = 1;

    private void encode_offset(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, offset_Tag);
        Serializer.encodeVarInt32(buf, this.offset);
    }

    private static void decode_offset(ByteBuf buf, IndexOffset a_1) {
        a_1.offset = Serializer.decodeVarInt32(buf);
    }


    public static IndexOffset decode(ByteBuf buf, int length_1) {
        IndexOffset value_1 = new IndexOffset();
        int f_Index = buf.readerIndex();
        while (buf.readerIndex() < f_Index + length_1) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case key_Tag:
                    decode_key(buf, value_1);
                    break;
                case intkey_Tag:
                    decode_intkey(buf, value_1);
                    break;
                case longkey_Tag:
                    decode_longkey(buf, value_1);
                    break;
                case offset_Tag:
                    decode_offset(buf, value_1);
                    break;
                default:
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        return value_1;
    }


    public void encode(ByteBuf buf) {

        if (key instanceof BytesKey) {
            this.encode_key(buf);
        }

        if (key instanceof IntKey) {
            this.encode_intkey(buf);
        }

        if (key instanceof LongKey) {
            this.encode_longkey(buf);
        }

        this.encode_offset(buf);

    }

    public int getByteSize(){
        int size_1=0;
        if(key!=null&&key instanceof BytesKey){
            size_1+=key_TagEncodeSize;
            size_1+=Serializer.computeVarInt32Size(key.getByteSize());
            size_1 += key.getByteSize();
        }

        if(key!=null&&key instanceof IntKey){
            size_1+= intkey_TagEncodeSize;
            size_1 += key.getByteSize();
        }

        if(key!=null&&key instanceof LongKey){
            size_1+= longkey_TagEncodeSize;
            size_1 += key.getByteSize();
        }

        size_1+=offset_TagEncodeSize;
        size_1+=Serializer.computeVarInt32Size(offset);

        return size_1;
    }

}
