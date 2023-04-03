package net.openio.jrocksDb.strorage;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.tool.Serializer;

import java.util.ArrayList;
import java.util.List;
@Data
public class IndexList {

    List<IndexOffset> list;


    public IndexList() {
        list = new ArrayList<>();
    }


    public final static int list_Num = 1;
    public final static int list_Tag = 10;// the value is num<<3|wireType
    public final static int list_TagEncodeSize = 1;


    private static void decode_list(ByteBuf buf, IndexList a_1) {
        IndexOffset value_1 = null;
        value_1 = IndexOffset.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.add(value_1);
    }

    private void encode_list(ByteBuf buf) {
        for (IndexOffset value_1 : list) {
            Serializer.encodeVarInt32(buf, list_Tag);
            Serializer.encodeVarInt32(buf, value_1.getByteSize());
            value_1.encode(buf);
        }
    }

    public void add(IndexOffset value) {
        if (value == null) {
            throw new RuntimeException("value is null");
        }
        this.list.add(value);
    }

    public static IndexList decode(ByteBuf buf,int length) {
        IndexList value_1 = new IndexList();
        int f_Index = buf.readerIndex()+length;
        while (buf.readerIndex() < f_Index) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case list_Tag:
                    decode_list(buf, value_1);
                    break;
                default:
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        return value_1;
    }

    public void encode(ByteBuf buf) {
        this.encode_list(buf);
    }


    public int getByteSize() {
        int OffSetList_size = 0;
        OffSetList_size += list_TagEncodeSize * list.size();// add tag length
        for (IndexOffset value_1 : list) {
            OffSetList_size += Serializer.computeVarInt32Size(value_1.getByteSize());
            OffSetList_size += value_1.getByteSize();
        }
        return OffSetList_size;
    }


}
