package net.openio.jrocksDb.transaction;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.openio.jrocksDb.tool.Serializer;

@Getter
@Data
public class CommitId {

    private volatile boolean isInit;

    private volatile Long times;

    private volatile Integer id;


    public int compareTo(PrepareId prepareId) {
        if(!isInit) throw new RuntimeException("CommitId is Not Init");

        if (times > prepareId.getTimes()) return 1;

        if (times < prepareId.getTimes()) return -1;

        if (id > prepareId.getId()) return 1;

        if (id < prepareId.getId()) return -1;
        return 0;
    }

    public int compareTo(CommitId commitId) {

        if(!commitId.isInit) throw new RuntimeException("CommitId is Not Init");

        if (times > commitId.getTimes()) return 1;

        if (times < commitId.getTimes()) return -1;

        if (id > commitId.getId()) return 1;

        if (id < commitId.getId()) return -1;
        return 0;
    }





    public final static int time_Num = 1;
    public final static int time_Tag = 8;// the value is num<<<3|wireType
    public final static int time_TagEncodeSize = 1;

    private void encode_time(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, time_Tag);
        Serializer.encodeVarInt64(buf, this.times);
    }

    private static void decode_time(ByteBuf buf, CommitId a_1) {
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

    private static void decode_id(ByteBuf buf, CommitId a_1) {
        java.lang.Integer value_1 = null;
        value_1 = Serializer.decodeVarInt32(buf);
        a_1.id = value_1;
    }

    public static CommitId decode(ByteBuf buf, int length_1) {
        CommitId commit=new CommitId();
        int f_Index = buf.readerIndex();
        int end = f_Index + length_1;
        while (buf.readerIndex() < end) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case time_Tag:
                    decode_time(buf, commit);
                    break;
                case id_Tag:
                    decode_id(buf, commit);
                    break;
                default:
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        commit.isInit=commit.id!=null&&commit.times!=null;
        return commit;
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
        if(times==null||id==null)
            throw new RuntimeException("CommitId encode  data is null");
        this.encode_time(buf);

        this.encode_id(buf);


    }

    private CommitId (){

    }

    public CommitId(Long times,Integer id){
        this.times=times;
        this.id=id;
        isInit=times!=null&&id!=null;
    }


}
