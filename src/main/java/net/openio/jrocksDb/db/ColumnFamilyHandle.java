package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.openio.jrocksDb.compression.CompressionTask;
import net.openio.jrocksDb.log.WALLog;
import net.openio.jrocksDb.log.WalStorage;
import net.openio.jrocksDb.log.WalTask;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.mem.MemTableList;
import net.openio.jrocksDb.strorage.Block;
import net.openio.jrocksDb.strorage.FlushTask;
import net.openio.jrocksDb.strorage.SSTable;
import net.openio.jrocksDb.tool.Serializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public class ColumnFamilyHandle {

    @Getter
    JRocksDB db;

    ColumnFamilyId columnFamilyId;

    String name;

    FileList fileList;

    List<String> WalFiles;

    MemTableList memTableList;

    ConcurrentLinkedQueue<CompressionTask> compressionQueue;

    ConcurrentLinkedQueue<WalTask> WalQueue;

    ConcurrentLinkedQueue<FlushTask> flushQueue;

    Block block;

    Key.KeyType keyType;

    Value.ValueType valueType;


    public void addWalFile(String fileName){
        synchronized (this) {
            WalFiles.add(fileName);
        }
    }


    public void addFileLevel(SSTable ssTable){
        synchronized (this) {
            fileList.getLevel0().add(ssTable);
        }

        if(fileList.getLevel0().size()>4){
            compressionQueue.add(new CompressionTask(fileList,this));
        }
    }

    public KeyValueEntry get(KeyValueEntry keyValueEntry){
        KeyValueEntry keyValue=memTableList.getValue(keyValueEntry);
        if(keyValue==null){
            int length=fileList.getLevel0().size();
            List<SSTable> list=fileList.getLevel0();
            Key key=keyValueEntry.getKey();
            for (int i=length-1;i>=0;i--){
                SSTable ssTable=list.get(i);
                Key min=ssTable.getMinKey();
                Key max=ssTable.getMaxKey();
                if(min.compareTo(key)>=0&&max.compareTo(key)<=0){
                    keyValue=block.getKeyValue(key,ssTable.getFileName());
                    if(keyValue!=null){
                        return keyValue;
                    }
                }
            }

            list=fileList.getLevel1();
            length=list.size();
            for (int i=0;i<length;i++){
                SSTable ssTable=list.get(i);
                Key min=ssTable.getMinKey();
                Key max=ssTable.getMaxKey();
                if(min.compareTo(key)>=0&&max.compareTo(key)<=0){
                    keyValue=block.getKeyValue(key,ssTable.getFileName());
                    if(keyValue!=null){
                        return keyValue;
                    }
                    break;
                }
            }

            list=fileList.getLevel2();
            length=list.size();
            for (int i=0;i<length;i++){
                SSTable ssTable=list.get(i);
                Key min=ssTable.getMinKey();
                Key max=ssTable.getMaxKey();
                if(min.compareTo(key)>=0&&max.compareTo(key)<=0){
                    keyValue=block.getKeyValue(key,ssTable.getFileName());
                    if(keyValue!=null){
                        return keyValue;
                    }
                    break;
                }
            }

            list=fileList.getLevel3();
            length=list.size();
            for (int i=0;i<length;i++){
                SSTable ssTable=list.get(i);
                Key min=ssTable.getMinKey();
                Key max=ssTable.getMaxKey();
                if(min.compareTo(key)>=0&&max.compareTo(key)<=0){
                    keyValue=block.getKeyValue(key,ssTable.getFileName());
                    if(keyValue!=null){
                        return keyValue;
                    }
                    break;
                }
            }

            list=fileList.getLevel4();
            length=list.size();
            for (int i=0;i<length;i++){
                SSTable ssTable=list.get(i);
                Key min=ssTable.getMinKey();
                Key max=ssTable.getMaxKey();
                if(min.compareTo(key)>=0&&max.compareTo(key)<=0){
                    keyValue=block.getKeyValue(key,ssTable.getFileName());
                    if(keyValue!=null){
                        return keyValue;
                    }
                    break;
                }
            }
        }
        return keyValue;
    }


    public void put(KeyValueEntry keyValueEntry){
        memTableList.putValue(keyValueEntry);
    }


    private int id=0;

    private long LastCreateFileTime=new Date().getTime();

    public String getFileName(){
        synchronized (this){
            long newTime=new Date().getTime();
            if(LastCreateFileTime==newTime){
                id++;
            }else {
                id=0;
                LastCreateFileTime=newTime;
            }
            return ""+columnFamilyId.getCId()+newTime+id;
        }
    }

    public boolean verify(Key key){
        if(key instanceof IntKey){
            return keyType== Key.KeyType.intKey;
        }
        if(key instanceof LongKey){
            return keyType== Key.KeyType.longKey;
        }
        if(key instanceof BytesKey){
            return keyType== Key.KeyType.bytesKey;
        }
        return false;
    }

    public boolean verify(Value value){
        if(value instanceof IntValue){
            return valueType== Value.ValueType.intValue;
        }
        if(value instanceof LongValue){
            return valueType== Value.ValueType.longValue;
        }
        if(value instanceof BytesValue){
            return valueType== Value.ValueType.ByteValue;
        }
        return false;
    }


    public ColumnFamilyId getColumnFamilyId(){
        return columnFamilyId;
    }

    public ColumnFamilyHandle(ColumnFamilyId columnFamilyId,JRocksDB jRocksDB,String name, ConcurrentLinkedQueue<CompressionTask> compressionQueue, ConcurrentLinkedQueue<WalTask> walQueue,
                              ConcurrentLinkedQueue<FlushTask> flushQueue, List<String> walFiles, FileList fileList, Key.KeyType keyType, Value.ValueType valueType){
        this.db=jRocksDB;
        this.columnFamilyId=columnFamilyId;
        this.keyType=keyType;
        this.valueType=valueType;
        this.name=name;
        this.fileList=fileList;
        this.block=new Block();
        this.compressionQueue=compressionQueue;
        this.WalFiles=walFiles;
        this.WalQueue=walQueue;
        this.flushQueue=flushQueue;
        WALLog walLog=new WALLog(walQueue,new WalStorage());
        memTableList=new MemTableList(walLog,this,walFiles,flushQueue);
    }



    public final static int id_Num = 1;
    public final static int id_Tag = 10;// the value is num<<<3|wireType
    public final static int id_TagEncodeSize = 1;

    private void encode_id(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, id_Tag);
        Serializer.encodeVarInt32(buf, this.columnFamilyId.getByteSize());
        this.columnFamilyId.encode(buf);
    }

    private static void decode_id(ByteBuf buf, ColumnFamilyHandle a_1) {
        a_1.columnFamilyId = ColumnFamilyId.decode(buf, Serializer.decodeVarInt32(buf));
    }



    public final static int name_Num = 2;
    public final static int name_Tag = 18;// the value is num<<<3|wireType
    public final static int name_TagEncodeSize = 1;

    private void encode_name(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, name_Tag);
        Serializer.encodeString(buf, this.name);
    }

    private static void decode_name(ByteBuf buf, ColumnFamilyHandle a_1) {
        a_1.name = Serializer.decodeString(buf, Serializer.decodeVarInt32(buf));

    }

    public final static int filelist_Num = 3;
    public final static int filelist_Tag = 26;// the value is num<<<3|wireType
    public final static int filelist_TagEncodeSize = 1;

    private void encode_filelist(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, filelist_Tag);
        Serializer.encodeVarInt32(buf, this.fileList.getByteSize());
        this.fileList.encode(buf);
    }

    private static void decode_filelist(ByteBuf buf, ColumnFamilyHandle a_1) {

        a_1.fileList = FileList.decode(buf, Serializer.decodeVarInt32(buf));

    }

    public final static int walfile_Num = 4;
    public final static int walfile_Tag = 34;// the value is num<<3|wireType
    public final static int walfile_TagEncodeSize = 1;


    private static void decode_walfile(ByteBuf buf, ColumnFamilyHandle a_1) {
        java.lang.String value_1 = Serializer.decodeString(buf, Serializer.decodeVarInt32(buf));
        a_1.WalFiles.add(value_1);
    }

    private void encode_walfile(ByteBuf buf) {
        for (java.lang.String value_1 : WalFiles) {
            Serializer.encodeVarInt32(buf, walfile_Tag);
            Serializer.encodeString(buf, value_1);
        }
    }

    public final static int keytype_Num = 5;
    public final static int keytype_Tag = 40;// the value is num<<<3|wireType
    public final static int keytype_TagEncodeSize = 1;

    private void encode_keytype(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, keytype_Tag);
        Serializer.encodeVarInt32(buf, this.keyType.getNum());
    }

    private static void decode_keytype(ByteBuf buf, ColumnFamilyHandle a_1) {

        a_1.keyType = Key.KeyType.get(Serializer.decodeVarInt32(buf));

    }


    public final static int valuetype_Num = 6;
    public final static int valuetype_Tag = 48;// the value is num<<<3|wireType
    public final static int valuetype_TagEncodeSize = 1;

    private void encode_valuetype(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, valuetype_Tag);
        Serializer.encodeVarInt32(buf, this.valueType.getNum());
    }

    private static void decode_valuetype(ByteBuf buf, ColumnFamilyHandle a_1) {

        a_1.valueType= Value.ValueType.get(Serializer.decodeVarInt32(buf));

    }

    public static ColumnFamilyHandle decode(ByteBuf buf, int length_1) {
        ColumnFamilyHandle value_1 = new ColumnFamilyHandle();
        int f_Index = buf.readerIndex();
        int end=f_Index + length_1;
        while (buf.readerIndex() < end) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case id_Tag :
                    decode_id(buf, value_1);
                    break;
                case name_Tag :
                    decode_name(buf, value_1);
                    break;
                case filelist_Tag :
                    decode_filelist(buf, value_1);
                    break;
                case walfile_Tag :
                    decode_walfile(buf, value_1);
                    break;
                case keytype_Tag :

                    decode_keytype(buf, value_1);

                    break;
                case valuetype_Tag :
                    decode_valuetype(buf, value_1);
                    break;
                default :
                    Serializer.skipUnknownField(num_1, buf);
            }
        }

        return value_1;
    }

    public void encode(ByteBuf buf) {
        if (columnFamilyId!=null) {
            this.encode_id(buf);
        }

        if (name!=null) {
            this.encode_name(buf);
        }

        if (fileList!=null) {
            this.encode_filelist(buf);
        }

        if (WalFiles!=null) {
            this.encode_walfile(buf);
        }

        if (keyType!=null) {
            this.encode_keytype(buf);
        }

        if (valueType!=null) {
            this.encode_valuetype(buf);
        }

    }


    public int getByteSize() {

        int ColumnFamilyHandle_size =0;
        ColumnFamilyHandle_size+= id_TagEncodeSize;
        ColumnFamilyHandle_size += Serializer.computeVarInt32Size(columnFamilyId.getByteSize());
        ColumnFamilyHandle_size += columnFamilyId.getByteSize();


        ColumnFamilyHandle_size += filelist_TagEncodeSize;
        ColumnFamilyHandle_size += Serializer.computeVarInt32Size(fileList.getByteSize());
        ColumnFamilyHandle_size += fileList.getByteSize();

        ColumnFamilyHandle_size+=name_TagEncodeSize;
        ColumnFamilyHandle_size += Serializer.computeVarInt32Size(ByteBufUtil.utf8Bytes(name));
        ColumnFamilyHandle_size += ByteBufUtil.utf8Bytes(name);

        ColumnFamilyHandle_size += walfile_TagEncodeSize * WalFiles.size();// add tag length
        for (java.lang.String value_1 : WalFiles) {
            int length_1 = 0;
            length_1 += Serializer.computeVarInt32Size(ByteBufUtil.utf8Bytes(value_1));
            length_1 += ByteBufUtil.utf8Bytes(value_1);// value length
            ColumnFamilyHandle_size += length_1;
        }


        ColumnFamilyHandle_size += keytype_TagEncodeSize;
        ColumnFamilyHandle_size += Serializer.computeVarInt32Size(keyType.getNum());



        ColumnFamilyHandle_size += valuetype_TagEncodeSize;
        ColumnFamilyHandle_size += Serializer.computeVarInt32Size(valueType.getNum());


        return ColumnFamilyHandle_size;
    }

    public ColumnFamilyHandle(){
        this.fileList=new FileList();

        this.WalFiles=new ArrayList<>();
    }


}
