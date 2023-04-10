package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.openio.jrocksDb.strorage.SSTable;
import net.openio.jrocksDb.tool.Serializer;

import java.util.ArrayList;
import java.util.List;

public class FileList {

    private List<SSTable> level0;

    private List<SSTable> level1;

    private List<SSTable> level2;

    private List<SSTable> level3;

    private List<SSTable> level4;


    public FileList(){

        level0=new ArrayList<>();
        level1=new ArrayList<>();
        level2=new ArrayList<>();
        level3=new ArrayList<>();
        level4=new ArrayList<>();

    }


    public void setList(List<SSTable> list,int level){
        synchronized (this) {
            switch (level) {
                case 0:
                    level0 = list;
                    return;
                case 1:
                    level1 = list;
                    return;
                case 2:
                    level2 = list;
                    return;
                case 3:
                    level3 = list;
                    return;
                case 4:
                    level4 = list;
                    return;
            }
        }
    }

    public void deleteSSTable(int f,int e,int level){
        synchronized (this){
            List<SSTable> ssTables=null;
            switch (level) {
                case 0:
                    ssTables=level0;
                    break;
                case 1:
                    ssTables=level1;
                    break;
                case 2:
                    ssTables=level2;
                    break;
                case 3:
                    ssTables=level3;
                    break;
                case 4:
                    ssTables=level4;
                    break;
                default: return;
            }

            for(int i=e-f;i>0;i--) {
                ssTables.remove(f);
            }
        }

    }

    public List<SSTable> getSSTable(int level){
        synchronized (this){
            List<SSTable> ssTables=null;
            switch (level) {
                case 0:
                    return List.copyOf(level0);
                case 1:
                    return List.copyOf(level1);
                case 2:
                    return List.copyOf(level2);
                case 3:
                    return List.copyOf(level3);
                case 4:
                    return List.copyOf(level4);
                default: return null;
            }

        }
    }


    public void getSSTable(SSTable ssTable){
        synchronized (this){
            level0.add(ssTable);
        }
    }

    public int getLeve0Size(){
           return level0.size();
    }

    public int getLeve1Size(){
        return level0.size();
    }

    public int getLeve2Size(){
        return level0.size();
    }

    public int getLeve3Size(){
        return level0.size();
    }

    public int getLeve4Size(){
        return level0.size();
    }



    public final static int level0_Num = 1;
    public final static int level0_Tag = 10;// the value is num<<3|wireType
    public final static int level0_TagEncodeSize = 1;





    private static void decode_level0(ByteBuf buf, FileList a_1) {
        SSTable value_1 = SSTable.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.level0.add(value_1);
    }

    private void encode_level0(ByteBuf buf) {
        for (SSTable value_1 : level0) {
            Serializer.encodeVarInt32(buf, level0_Tag);
            Serializer.encodeVarInt32(buf, value_1.getByteSize());
            value_1.encode(buf);
        }
    }


    public final static int level1_Num = 2;
    public final static int level1_Tag = 18;// the value is num<<3|wireType
    public final static int level1_TagEncodeSize = 1;

    private static void decode_level1(ByteBuf buf, FileList a_1) {

        SSTable value_1 = SSTable.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.level1.add(value_1);
    }

    private void encode_level1(ByteBuf buf) {
        for (SSTable value_1 : level1) {
            Serializer.encodeVarInt32(buf, level1_Tag);
            Serializer.encodeVarInt32(buf, value_1.getByteSize());
            value_1.encode(buf);
        }
    }

    public final static int level2_Num = 3;
    public final static int level2_Tag = 26;// the value is num<<3|wireType
    public final static int level2_TagEncodeSize = 1;

    private static void decode_level2(ByteBuf buf, FileList a_1) {

        SSTable value_1 = SSTable.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.level2.add(value_1);
    }

    private void encode_level2(ByteBuf buf) {
        for (SSTable value_1 : level2) {
            Serializer.encodeVarInt32(buf, level2_Tag);
            Serializer.encodeVarInt32(buf, value_1.getByteSize());
            value_1.encode(buf);
        }
    }

    public final static int level3_Num = 4;
    public final static int level3_Tag = 34;// the value is num<<3|wireType
    public final static int level3_TagEncodeSize = 1;

    private static void decode_level3(ByteBuf buf, FileList a_1) {

        SSTable value_1 = SSTable.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.level3.add(value_1);
    }

    private void encode_level3(ByteBuf buf) {
        for (SSTable value_1 : level3) {
            Serializer.encodeVarInt32(buf, level3_Tag);
            Serializer.encodeVarInt32(buf, value_1.getByteSize());
            value_1.encode(buf);
        }
    }

    public final static int level4_Num = 5;
    public final static int level4_Tag = 42;// the value is num<<3|wireType
    public final static int level4_TagEncodeSize = 1;


    private static void decode_level4(ByteBuf buf, FileList a_1) {
        SSTable value_1 = SSTable.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.level4.add(value_1);
    }

    private void encode_level4(ByteBuf buf) {
        for (SSTable value_1 : level4) {
            Serializer.encodeVarInt32(buf, level4_Tag);
            Serializer.encodeVarInt32(buf, value_1.getByteSize());
            value_1.encode(buf);
        }
    }


    public static FileList decode(ByteBuf buf, int length_1) {
        FileList value_1 = new FileList();
        int f_Index = buf.readerIndex();
        int end=f_Index + length_1;
        while (buf.readerIndex() <end) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case level0_Tag :
                    decode_level0(buf, value_1);
                    break;
                case level1_Tag :
                    decode_level1(buf, value_1);
                    break;
                case level2_Tag :
                    decode_level2(buf, value_1);
                    break;
                case level3_Tag :
                    decode_level3(buf, value_1);
                    break;
                case level4_Tag :
                    decode_level4(buf, value_1);
                    break;
                default :
                    Serializer.skipUnknownField(num_1, buf);
            }
        }

        return value_1;
    }

    public void encode(ByteBuf buf) {
        synchronized (this) {
            if (level0 != null && level0.size() != 0) {
                this.encode_level0(buf);
            }

            if (level1 != null && level1.size() != 0) {
                this.encode_level1(buf);
            }

            if (level2 != null && level2.size() != 0) {
                this.encode_level2(buf);
            }

            if (level3 != null && level3.size() != 0) {
                this.encode_level3(buf);
            }

            if (level4 != null && level4.size() != 0) {
                this.encode_level4(buf);
            }
        }

    }

    public int getByteSize() {
        synchronized (this) {
            int FileList_size = 0;
            FileList_size += level0_TagEncodeSize * level0.size();// add tag length
            for (SSTable value_1 : level0) {
                int length_1 = 0;
                length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
                length_1 += value_1.getByteSize();
                FileList_size += length_1;
            }

            FileList_size += level1_TagEncodeSize * level1.size();// add tag length
            for (SSTable value_1 : level1) {
                int length_1 = 0;
                length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
                length_1 += value_1.getByteSize();
                FileList_size += length_1;
            }

            FileList_size += level2_TagEncodeSize * level2.size();// add tag length
            for (SSTable value_1 : level2) {
                int length_1 = 0;
                length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
                length_1 += value_1.getByteSize();
                FileList_size += length_1;
            }

            FileList_size += level3_TagEncodeSize * level3.size();// add tag length
            for (SSTable value_1 : level3) {
                int length_1 = 0;
                length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
                length_1 += value_1.getByteSize();
                FileList_size += length_1;
            }

            FileList_size += level4_TagEncodeSize * level4.size();// add tag length
            for (SSTable value_1 : level4) {
                int length_1 = 0;
                length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
                length_1 += value_1.getByteSize();
                FileList_size += length_1;
            }
            return FileList_size;
        }

    }
}
