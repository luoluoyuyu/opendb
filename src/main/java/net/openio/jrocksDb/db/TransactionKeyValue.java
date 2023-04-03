package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.tool.Serializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionKeyValue {

    ColumnFamilyId columnFamilyId;

    KeyValueEntry keyValueEntry;


    public final static int columnfamilyid_Num = 1;
    public final static int columnfamilyid_Tag = 10;// the value is num<<<3|wireType
    public final static int columnfamilyid_TagEncodeSize = 1;

    private void encode_columnfamilyid(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, columnfamilyid_Tag);
        Serializer.encodeVarInt32(buf, this.columnFamilyId.getByteSize());
        this.columnFamilyId.encode(buf);
    }

    private static void decode_columnfamilyid(ByteBuf buf, TransactionKeyValue a_1) {
        ColumnFamilyId value_1 = null;
        value_1 = ColumnFamilyId.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.columnFamilyId = value_1;
    }

    public final static int keyvalue_Num = 2;
    public final static int keyvalue_Tag = 18;// the value is num<<<3|wireType
    public final static int keyvalue_TagEncodeSize = 1;

    private void encode_keyvalue(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, keyvalue_Tag);
        Serializer.encodeVarInt32(buf, this.keyValueEntry.size());
        this.keyValueEntry.encod(buf);
    }

    private static void decode_keyvalue(ByteBuf buf, TransactionKeyValue a_1) {
        a_1.keyValueEntry = KeyValueEntry.decode(buf, Serializer.decodeVarInt32(buf));

    }

    public static TransactionKeyValue decode(ByteBuf buf, int length) {
        TransactionKeyValue value_1 = new TransactionKeyValue();
        int f_Index = buf.readerIndex();
        int end = f_Index + length;
        while (buf.readerIndex() < end) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case columnfamilyid_Tag:
                    decode_columnfamilyid(buf, value_1);
                    break;
                case keyvalue_Tag:
                    decode_keyvalue(buf, value_1);
                    break;
                default:
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        return value_1;
    }

    public void encode(ByteBuf buf) {

        Serializer.encode32(buf,size());

        this.encode_columnfamilyid(buf);

        this.encode_keyvalue(buf);

    }

    public int getByteSize(){
        return size()+4;
    }

    public int size(){
        int TransactionKeyValue_size = keyvalue_TagEncodeSize;
        TransactionKeyValue_size+=Serializer.computeVarInt32Size(columnFamilyId.getByteSize());
        TransactionKeyValue_size+= columnFamilyId.getByteSize();

        TransactionKeyValue_size += keyvalue_TagEncodeSize;
        TransactionKeyValue_size += Serializer.computeVarInt32Size(keyValueEntry.size());
        TransactionKeyValue_size += keyValueEntry.size();
        return TransactionKeyValue_size;
    }


}
