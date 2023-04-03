package net.openio.jrocksDb.mem;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.openio.jrocksDb.db.*;
import net.openio.jrocksDb.tool.Serializer;
import net.openio.jrocksDb.transaction.CommitId;
import net.openio.jrocksDb.transaction.PrepareId;
import net.openio.jrocksDb.transaction.SequenceNumber;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueEntry {

    Key key;

    Value value;

    CommitId commitId;

    SequenceNumber sqId;

    PrepareId prepareId;

    Type type;


    public static enum Type {

        delete(1),

        update(2),

        insert(3),

        ;

        public static Type get(int tag) {
            if (tag == 1) {
                return Type.delete;
            }
            if (tag == 2) {
                return Type.update;
            }
            if (tag == 3) {
                return Type.insert;
            }
            return null;
        }

        int num;

        Type(int num) {
            this.num = num;
        }

        int getNum() {
            return this.num;
        }
    }


    public final static int key_Num = 1;
    public final static int key_Tag = 10;// the value is num<<<3|wireType
    public final static int key_TagEncodeSize = 1;

    private void encode_key(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, key_Tag);
        key.toByte(buf);
    }

    private static void decode_key(ByteBuf buf, KeyValueEntry a_1) {
        a_1.key = BytesKey.getKey(buf, Serializer.decodeVarInt32(buf));
    }


    public final static int intkey_Num = 2;
    public final static int intkey_Tag = 16;// the value is num<<<3|wireType
    public final static int intkey_TagEncodeSize = 1;

    private void encode_intkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, intkey_Tag);
        key.toByte(buf);
    }

    private static void decode_intkey(ByteBuf buf, KeyValueEntry a_1) {
        a_1.key = IntKey.getKey(buf);
    }


    public final static int longkey_Num = 3;
    public final static int longkey_Tag = 24;// the value is num<<<3|wireType
    public final static int longkey_TagEncodeSize = 1;

    private void encode_longkey(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, longkey_Tag);
        key.toByte(buf);
    }

    private static void decode_longkey(ByteBuf buf, KeyValueEntry a_1) {
        a_1.key = LongKey.getKey(buf);
    }

    public final static int value_Num = 4;
    public final static int value_Tag = 34;// the value is num<<<3|wireType
    public final static int value_TagEncodeSize = 1;

    private void encode_value(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, value_Tag);
        value.toByte(buf);
    }

    private static void decode_value(ByteBuf buf, KeyValueEntry a_1) {
        a_1.value = BytesValue.getValue(buf, Serializer.decodeVarInt32(buf));
    }


    public final static int intvalue_Num = 5;
    public final static int intvalue_Tag = 40;// the value is num<<<3|wireType
    public final static int intvalue_TagEncodeSize = 1;

    private void encode_intvalue(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, intvalue_Tag);
        key.toByte(buf);
    }

    private static void decode_intvalue(ByteBuf buf, KeyValueEntry a_1) {
        a_1.value = IntValue.getValue(buf);
    }


    public final static int longvalue_Num = 6;
    public final static int longvalue_Tag = 48;// the value is num<<<3|wireType
    public final static int longvalue_TagEncodeSize = 1;

    private void encode_longvalue(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, longvalue_Tag);
        value.toByte(buf);
    }

    private static void decode_longvalue(ByteBuf buf, KeyValueEntry a_1) {
        a_1.value = LongValue.getValue(buf);
    }

    public final static int prepareid_Num = 7;
    public final static int prepareid_Tag = 58;// the value is num<<<3|wireType
    public final static int prepareid_TagEncodeSize = 1;

    private void encode_prepareid(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, prepareid_Tag);
        Serializer.encodeVarInt32(buf, this.prepareId.getByteSize());
        this.prepareId.encode(buf);
    }

    private static void decode_prepareid(ByteBuf buf, KeyValueEntry a_1) {

        a_1.prepareId = PrepareId.decode(buf, Serializer.decodeVarInt32(buf));


    }


    public final static int seq_Num = 8;
    public final static int seq_Tag = 66;// the value is num<<<3|wireType
    public final static int seq_TagEncodeSize = 1;

    private void encode_seq(ByteBuf buf) {

        Serializer.encodeVarInt32(buf, seq_Tag);


        Serializer.encodeVarInt32(buf, this.sqId.getByteSize());


        this.sqId.encode(buf);

    }

    private static void decode_seq(ByteBuf buf, KeyValueEntry a_1) {
        a_1.sqId = SequenceNumber.decode(buf, Serializer.decodeVarInt32(buf));
    }

    public final static int commitid_Num = 9;
    public final static int commitid_Tag = 74;// the value is num<<<3|wireType
    public final static int commitid_TagEncodeSize = 1;

    private void encode_commitid(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, commitid_Tag);
        Serializer.encodeVarInt32(buf, commitId.getByteSize());
        this.commitId.encode(buf);
    }

    private static void decode_commitid(ByteBuf buf, KeyValueEntry a_1) {
        a_1.commitId = CommitId.decode(buf, Serializer.decodeVarInt32(buf));
    }


    public final static int type_Num = 10;
    public final static int type_Tag = 80;// the value is num<<<3|wireType
    public final static int type_TagEncodeSize = 1;

    private void encode_type(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, type_Tag);
        Serializer.encodeVarInt32(buf, this.type.getNum());
    }

    private static void decode_type(ByteBuf buf, KeyValueEntry a_1) {
        KeyValueEntry.Type value_1 = null;
        value_1 = KeyValueEntry.Type.get(Serializer.decodeVarInt32(buf));
        a_1.type = value_1;
    }


    public static KeyValueEntry decode(ByteBuf buf, int dataLength) {
        KeyValueEntry value_1 = new KeyValueEntry();
        int f_Index = buf.readerIndex();
        int end = f_Index + dataLength;
        while (buf.readerIndex() < end) {

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
                case value_Tag:
                    decode_value(buf, value_1);
                    break;
                case intvalue_Tag:
                    decode_intvalue(buf, value_1);
                    break;
                case longvalue_Tag:
                    decode_longvalue(buf, value_1);
                    break;
                case prepareid_Tag:
                    decode_prepareid(buf, value_1);
                    break;
                case seq_Tag:
                    decode_seq(buf, value_1);
                    break;
                case commitid_Tag:
                    decode_commitid(buf, value_1);
                    break;
                case type_Tag:
                    decode_type(buf, value_1);
                    break;
                default:
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        return value_1;
    }


    public void encode(ByteBuf buf) {
        int size = size();
        Serializer.encode32(buf, size);
        encod(buf);

    }

    public int getSize() {
        return size() + 4;
    }


    public int size() {
        int size_1 = 0;
        if (key != null && key instanceof BytesKey) {
            size_1 += key_TagEncodeSize;
            size_1 += Serializer.computeVarInt32Size(key.getByteSize());
            size_1 += key.getByteSize();
        }

        if (value != null && value instanceof BytesValue) {
            size_1 += value_TagEncodeSize;
            size_1 += Serializer.computeVarInt32Size(value.getByteSize());
            size_1 += value.getByteSize();
        }

        if (key != null && key instanceof IntKey) {
            size_1 += intkey_TagEncodeSize;
            size_1 += key.getByteSize();
        }

        if (value != null && value instanceof IntValue) {
            size_1 += intvalue_TagEncodeSize;
            size_1 += value.getByteSize();
        }


        if (key != null && key instanceof LongKey) {
            size_1 += longkey_TagEncodeSize;
            size_1 += key.getByteSize();
        }


        if (value != null && value instanceof LongValue) {
            size_1 += longvalue_TagEncodeSize;
            size_1 += value.getByteSize();
        }

        if (prepareId != null) {
            size_1 += prepareid_TagEncodeSize;
            size_1 += Serializer.computeVarInt32Size(prepareId.getByteSize());
            size_1 += prepareId.getByteSize();
        }

        if (sqId != null) {
            size_1 += seq_TagEncodeSize;
            size_1 += Serializer.computeVarInt32Size(sqId.getByteSize());
            size_1 += sqId.getByteSize();
        }

        if (commitId != null && commitId.getId() != null) {
            size_1 += commitid_TagEncodeSize;
            size_1 += Serializer.computeVarInt32Size(commitId.getByteSize());
            size_1 += commitId.getByteSize();
        }

        if (type != null) {
            size_1+= type_TagEncodeSize;
            size_1 += Serializer.computeVarInt32Size(type.getNum());
        }

        return size_1;
    }

    public void encod(ByteBuf buf) {


        if (key instanceof BytesKey) {
            this.encode_key(buf);
        }

        if (key instanceof IntKey) {
            this.encode_intkey(buf);
        }

        if (key instanceof LongKey) {
            this.encode_longkey(buf);
        }

        if (value instanceof BytesValue) {
            this.encode_value(buf);
        }

        if (value instanceof IntValue) {
            this.encode_intvalue(buf);
        }

        if (value instanceof LongValue) {
            this.encode_longvalue(buf);
        }

        if (prepareId == null) {
            throw new RuntimeException("prepareId is null");
        }

        this.encode_prepareid(buf);

        if (sqId == null) {
            throw new RuntimeException("sqId is null");
        }


        this.encode_seq(buf);

        if (commitId != null && commitId.getId() != null) {
            this.encode_commitid(buf);
        }

        if (type == null) {
            throw new RuntimeException("type is null");
        }

        this.encode_type(buf);


    }

    public void setSqId(SequenceNumber sqId) {
        this.sqId = sqId;
    }
}
