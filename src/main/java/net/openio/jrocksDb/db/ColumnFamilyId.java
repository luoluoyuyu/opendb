package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.openio.jrocksDb.mem.Hash;
import net.openio.jrocksDb.tool.Serializer;

@Data
@AllArgsConstructor
public class ColumnFamilyId {

    long cId;

    @Override
    public int hashCode() {
        return Hash.longHash(cId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnFamilyId that = (ColumnFamilyId) o;
        return cId == that.cId;
    }

    public final static int id_Num = 1;
    public final static int id_Tag = 8;// the value is num<<<3|wireType
    public final static int id_TagEncodeSize = 1;

    private void encode_id(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, id_Tag);
        Serializer.encodeVarInt64(buf, this.cId);
    }

    private static void decode_id(ByteBuf buf, ColumnFamilyId a_1) {

        a_1.cId = Serializer.decodeVarInt64(buf);

    }

   public int getByteSize() {
        int ColumnFamilyId_size = id_TagEncodeSize;

        int size_1 = Serializer.computeVarInt64Size(cId);
        ColumnFamilyId_size += size_1;
        return ColumnFamilyId_size;
    }


    public static ColumnFamilyId decode(ByteBuf buf, int length_1) {
        ColumnFamilyId value_1 = new ColumnFamilyId(0);
        int f_Index = buf.readerIndex();
        while (buf.readerIndex() < f_Index + length_1) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case id_Tag :
                    decode_id(buf, value_1);
                    break;
                default :
                    Serializer.skipUnknownField(num_1, buf);
            }
        }

        return value_1;
    }


    public void encode(ByteBuf buf) {
        this.encode_id(buf);

    }
}
