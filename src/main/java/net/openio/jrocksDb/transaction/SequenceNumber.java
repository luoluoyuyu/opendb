package net.openio.jrocksDb.transaction;


import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.openio.jrocksDb.tool.Serializer;

@Getter
@AllArgsConstructor
@Data

public class SequenceNumber implements Comparable {

    private volatile Long times;

    private volatile Integer id;


    @Override
    public int compareTo(Object o) {
        if (!(o instanceof SequenceNumber)) {
            throw new RuntimeException("SequenceNumber is null or o type not SequenceNumber");
        }

        SequenceNumber SN = (SequenceNumber) o;
        if (times > (SN).times) return 1;

        if (times < SN.getTimes()) return -1;

        if (id > SN.id) return 1;

        if (id < SN.id) return -1;

        return 0;
    }




    public final static int time_Num = 1;
    public final static int time_Tag = 8;// the value is num<<<3|wireType
    public final static int time_TagEncodeSize = 1;

    private void encode_time(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, time_Tag);
        Serializer.encodeVarInt64(buf, this.times);
    }

    private static void decode_time(ByteBuf buf, SequenceNumber a_1) {
        java.lang.Long value_1 = null;
        value_1 = Serializer.decodeVarInt64(buf);
        a_1.times = value_1;
    }


    public final static int id_Num = 2;
    public final static int id_Tag = 16;// the value is num<<<3|wireType
    public final static int id_TagEncodeSize = 1;

    private void encode_id(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, id_Tag);
        Serializer.encodeVarInt32(buf, this.id);
    }

    private static void decode_id(ByteBuf buf, SequenceNumber a_1) {
        java.lang.Integer value_1 = null;
        value_1 = Serializer.decodeVarInt32(buf);
        a_1.id = value_1;
    }

    public static SequenceNumber decode(ByteBuf buf, int length_1) {
        SequenceNumber seq=new SequenceNumber();
        int f_Index = buf.readerIndex();
        int end = f_Index + length_1;
        while (buf.readerIndex() < end) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case time_Tag:
                    decode_time(buf, seq);
                    break;
                case id_Tag:
                    decode_id(buf, seq);
                    break;
                default:
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        return seq;
    }


    public int getByteSize(){
        int SequenceNumber_size = time_TagEncodeSize;
        int size_1 = 0;

        size_1 += Serializer.computeVarInt64Size(times);
        SequenceNumber_size += size_1;
        SequenceNumber_size += id_TagEncodeSize;

        int size_2 = Serializer.computeVarInt32Size(id);
        SequenceNumber_size += size_2;

        return SequenceNumber_size;
    }

    public void encode(ByteBuf buf) {
        if(times==null||id==null) throw new RuntimeException("SequenceNumber encode  data is null");
        this.encode_time(buf);

        this.encode_id(buf);
    }


    public SequenceNumber(){

    }

}
