package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.openio.jrocksDb.compression.CompressionTask;
import net.openio.jrocksDb.config.Config;
import net.openio.jrocksDb.log.WalLogWorker;
import net.openio.jrocksDb.log.WalTask;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.strorage.FlushTask;
import net.openio.jrocksDb.strorage.FlushWorker;
import net.openio.jrocksDb.tool.Serializer;
import net.openio.jrocksDb.transaction.*;
import net.openio.jrocksDb.transaction.lock.LockManager;
import net.openio.jrocksDb.transaction.lock.PointLockManager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class OptimisticTransactionDB implements JRocksDB {


    private volatile PrepareId minId;

    private volatile CommitId MaxId;

    Map<String, ColumnFamilyHandle> columnFamilyHandleMap;

    private List<ColumnFamilyHandle> columnFamilyHandles;

    LockManager lockManager;

    private List<Snapshot> snapshots;

    Map<SequenceNumber, Snapshot> snapshotMap;

    private AtomicLong maxTransactionId;

    private ConcurrentLinkedQueue<CompressionTask> compressionQueue;

    private ConcurrentLinkedQueue<WalTask> WalQueue;

    private ConcurrentLinkedQueue<FlushTask> flushQueue;

    private long columnFamilyHandleId=1;



    @Override
    public Status<Value> get(Key key, String columnFamilyName) {

        ColumnFamilyHandle columnFamilyHandle = null;
        if ((columnFamilyHandle = columnFamilyHandleMap.get(columnFamilyName)) == null) {
            return Status.NOTColumnFamilyHandle(columnFamilyName);
        }
        if (key == null || !columnFamilyHandle.verify(key)) {
            return Status.CKeyTypeVerify();
        }
        Snapshot snapshot = new Snapshot();
        KeyValueEntry keyValueEntry = new KeyValueEntry(key, null, snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), null);
        KeyValueEntry keyValue = columnFamilyHandle.get(keyValueEntry);
        if (keyValue == null || keyValue.getType() == KeyValueEntry.Type.delete) {
            return Status.GetDeleteValue();
        }

        return Status.Get(keyValue.getValue());
    }

    @Override
    public Status<Value> put(Key key, Value value, String columnFamilyName) {

        ColumnFamilyHandle columnFamilyHandle = null;
        if ((columnFamilyHandle = columnFamilyHandleMap.get(columnFamilyName)) == null) {
            return Status.NOTColumnFamilyHandle(columnFamilyName);
        }
        if (key == null || !columnFamilyHandle.verify(key)) {
            return Status.CKeyTypeVerify();
        }
        if (value != null && !columnFamilyHandle.verify(value)) {
            return Status.CValueTypeVerify();
        }

        Snapshot snapshot = new Snapshot();

        snapshotMap.put(snapshot.getNumber(), snapshot);

        KeyValueEntry keyValueEntry = new KeyValueEntry(key, value, snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), null);

        long transactionId = getNewTransactionId();

        tryLock(transactionId, snapshot.getNumber(), columnFamilyHandle.columnFamilyId, key);

        add(snapshot);
        KeyValueEntry keyValue = columnFamilyHandle.get(keyValueEntry);
        if (keyValue != null && keyValue.getType() != KeyValueEntry.Type.delete) {
            snapshotMap.remove(snapshot.getNumber());
            return Status.hasValue();
        }

        commit(snapshot);

        keyValueEntry.setType(KeyValueEntry.Type.insert);

        columnFamilyHandle.put(keyValueEntry);

        unLock(transactionId, snapshot.getNumber(), columnFamilyHandle.columnFamilyId, key);

        return Status.put();
    }

    @Override
    public Status<Value> getForUpdate(Key key, Value value, String columnFamilyName) {
        ColumnFamilyHandle columnFamilyHandle = null;
        if ((columnFamilyHandle = columnFamilyHandleMap.get(columnFamilyName)) == null) {
            return Status.NOTColumnFamilyHandle(columnFamilyName);
        }
        if (key == null || !columnFamilyHandle.verify(key)) {
            return Status.CKeyTypeVerify();
        }
        if (value != null && !columnFamilyHandle.verify(value)) {
            return Status.CValueTypeVerify();
        }
        Snapshot snapshot = new Snapshot();
        snapshotMap.put(snapshot.getNumber(), snapshot);
        KeyValueEntry keyValueEntry = new KeyValueEntry(key, value, snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), null);
        long transactionId = getNewTransactionId();
        tryLock(transactionId, snapshot.getNumber(), columnFamilyHandle.columnFamilyId, key);
        add(snapshot);
        KeyValueEntry keyValue = columnFamilyHandle.get(keyValueEntry);
        if (keyValue == null) {
            snapshotMap.remove(snapshot.getNumber());
            return Status.NotHasValue();
        }
        commit(snapshot);
        keyValueEntry.setType(KeyValueEntry.Type.update);
        columnFamilyHandle.put(keyValueEntry);
        unLock(transactionId, snapshot.getNumber(), columnFamilyHandle.columnFamilyId, key);
        return Status.update(keyValue.getValue());
    }


    @Override
    public Status<Value> delete(Key key, String columnFamilyName) {
        ColumnFamilyHandle columnFamilyHandle = null;
        if ((columnFamilyHandle = columnFamilyHandleMap.get(columnFamilyName)) == null) {
            return Status.NOTColumnFamilyHandle(columnFamilyName);
        }
        if (key == null || !columnFamilyHandle.verify(key)) {
            return Status.CKeyTypeVerify();
        }
        Snapshot snapshot = new Snapshot();
        snapshotMap.put(snapshot.getNumber(), snapshot);
        KeyValueEntry keyValueEntry = new KeyValueEntry(key, null, snapshot.getCommitId(), snapshot.getNumber(), snapshot.getPrepareId(), null);
        long transactionId = getNewTransactionId();
        tryLock(transactionId, snapshot.getNumber(), columnFamilyHandle.columnFamilyId, key);
        add(snapshot);
        KeyValueEntry keyValue = columnFamilyHandle.get(keyValueEntry);
        if (keyValue == null) {
            snapshotMap.remove(snapshot.getNumber());
            return Status.NotHasValue();
        }
        commit(snapshot);
        keyValueEntry.setType(KeyValueEntry.Type.delete);
        columnFamilyHandle.put(keyValueEntry);
        unLock(transactionId, snapshot.getNumber(), columnFamilyHandle.columnFamilyId, key);
        return Status.Delete(keyValue.getValue());
    }

    @Override
    public Status<ColumnFamily> getColumnFamily(String name) {
        ColumnFamilyHandle columnFamilyHandle = columnFamilyHandleMap.get(name);
        ColumnFamily columnFamily = new ColumnFamily();
        columnFamily.columnFamilyId = columnFamilyHandle.getColumnFamilyId();
        columnFamily.name = columnFamilyHandle.name;
        columnFamily.keyType = columnFamilyHandle.keyType;
        columnFamily.valueType = columnFamilyHandle.valueType;
        return Status.ColumnFamily(columnFamily);
    }

    private void add(Snapshot snapshot){
        synchronized (this) {
            snapshots.add(snapshot);
        }
    }

    private void commit(Snapshot snapshot){
        snapshot.commit();
        synchronized (this){
            MaxId=snapshot.getCommitId();

            while (snapshots.size()>0) {

                Snapshot snapshot1=snapshots.get(0);
                if (snapshot1!=null&&snapshot1.getCommitId().isInit()) {
                    snapshots.remove(0);

                }else {
                    break;
                }
            }
            if(snapshots.size()>0){
                minId=snapshots.get(0).getPrepareId();
            }else {
                minId.setTimes(0l);
            }
        }
    }

    @Override
    public Status<ColumnFamily> createColumnFamily(String name, Key.KeyType keyType, Value.ValueType valueType) {
        if(columnFamilyHandleMap.get(name)!=null){
            return Status.f();
        }

        ColumnFamilyId columnFamilyId;
        synchronized (this){
            columnFamilyHandleId++;
            columnFamilyId=new ColumnFamilyId(columnFamilyHandleId);
        }
        ColumnFamilyHandle c1=new ColumnFamilyHandle(columnFamilyId,this,name, this.compressionQueue,
                this.WalQueue,
                this.flushQueue, new ArrayList<>(), new FileList(), keyType, valueType);
        synchronized (columnFamilyHandles) {
            columnFamilyHandles.add(c1);
        }
        columnFamilyHandleMap.put(name,c1);
        loadDB();

        return Status.ColumnFamily(new ColumnFamily());
    }

    @Override
    public Status<List<ColumnFamily>> getAllColumnFamily() {
        List<ColumnFamily> list=new ArrayList<>();
        for (String name : columnFamilyHandleMap.keySet()) {
            ColumnFamilyHandle columnFamilyHandle = columnFamilyHandleMap.get(name);
            ColumnFamily columnFamily = new ColumnFamily();
            columnFamily.columnFamilyId = columnFamilyHandle.getColumnFamilyId();
            columnFamily.name = columnFamilyHandle.name;
            columnFamily.keyType = columnFamilyHandle.keyType;
            columnFamily.valueType = columnFamilyHandle.valueType;
            list.add(columnFamily);
        }
        return Status.ColumnFamily(list);
    }

    @Override
    public Status<Transaction> createTransaction() {
        Transaction transaction=new OptimisticTransaction(this, maxTransactionId.incrementAndGet());
        return null;
    }

    private boolean tryLock(long transactionId, SequenceNumber sequenceNumber, ColumnFamilyId columnFamilyId, Key key) {
        return lockManager.TryLock(transactionId, columnFamilyId, key, false, false, sequenceNumber,true) != null;

    }

    private void unLock(long transactionId, SequenceNumber sequenceNumber, ColumnFamilyId columnFamilyId, Key key) {
        lockManager.UnLock(transactionId, columnFamilyId, key, false, false,true);

    }


    private long getNewTransactionId() {
        return maxTransactionId.incrementAndGet();
    }




    private OptimisticTransactionDB(){
        this.columnFamilyHandles=new ArrayList<>();
    }

    public static OptimisticTransactionDB createDB(){
        OptimisticTransactionDB optimisticTransactionDB=null;

        try {
            int size=0;
            ByteBuf buf= Unpooled.directBuffer(4);
            buf.writerIndex(4);
            File file=new File(Config.DBMetaDataPath);
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
            randomAccessFile.seek(0);
            randomAccessFile.getChannel().read(buf.nioBuffer());
            size=buf.readInt();
            buf.release();
            buf=Unpooled.directBuffer(size);
            buf.writerIndex(size);
            randomAccessFile.seek(4);
            randomAccessFile.getChannel().read(buf.nioBuffer());
            optimisticTransactionDB=OptimisticTransactionDB.decode(buf,size);
            buf.release();
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        optimisticTransactionDB.compressionQueue=new ConcurrentLinkedQueue<>();
        optimisticTransactionDB.WalQueue=new ConcurrentLinkedQueue<>();
        optimisticTransactionDB.flushQueue=new ConcurrentLinkedQueue<>();
        optimisticTransactionDB.lockManager=new PointLockManager();
        optimisticTransactionDB.columnFamilyHandleMap= new ConcurrentHashMap<>();
        optimisticTransactionDB.snapshots=new LinkedList<>();
        optimisticTransactionDB.snapshotMap=new ConcurrentHashMap<>();
        optimisticTransactionDB.maxTransactionId =new AtomicLong(1);
        optimisticTransactionDB.minId=new PrepareId(Long.MAX_VALUE,0);
        optimisticTransactionDB.MaxId=new CommitId(Long.MIN_VALUE,0);
        List<ColumnFamilyHandle> columnFamilyHandles=new CopyOnWriteArrayList<>();
        for(int i=optimisticTransactionDB.columnFamilyHandles.size()-1;i>=0;i--){
            ColumnFamilyHandle c=optimisticTransactionDB.columnFamilyHandles.get(i);
            ColumnFamilyHandle c1=new ColumnFamilyHandle(c.getColumnFamilyId(),optimisticTransactionDB,c.name, optimisticTransactionDB.compressionQueue,
                    optimisticTransactionDB.WalQueue,
                    optimisticTransactionDB.flushQueue, c.WalFiles, c.fileList, c.keyType, c.valueType);
            columnFamilyHandles.add(c1);
            optimisticTransactionDB.columnFamilyHandleMap.put(c1.name,c1);
        }
        optimisticTransactionDB.columnFamilyHandles=columnFamilyHandles;
        Thread thread=new Thread(new WalLogWorker(optimisticTransactionDB.WalQueue));
        thread.start();
        thread=new Thread(new FlushWorker(optimisticTransactionDB.flushQueue));
        thread.start();
        return optimisticTransactionDB;
    }


    public void loadDB(){
        int size=0;
        ByteBuf buf= Unpooled.directBuffer((size=this.getByteSize())+4);
        buf.writeInt(size);
        this.encode(buf);
        File file=new File(Config.DBMetaDataPath);
        try {
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
            randomAccessFile.seek(0);
            randomAccessFile.getChannel().write(buf.nioBuffer());
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PrepareId getMinPrepareId() {
        return minId;
    }














    public final static int c_Num = 1;
    public final static int c_Tag = 10;// the value is num<<3|wireType
    public final static int c_TagEncodeSize = 1;


    private static void decode_c(ByteBuf buf, OptimisticTransactionDB a_1) {
        ColumnFamilyHandle value_1 = null;
        value_1 = ColumnFamilyHandle.decode(buf, Serializer.decodeVarInt32(buf));
        a_1.columnFamilyHandles.add(value_1);
    }

    private void encode_c(ByteBuf buf) {
        for (ColumnFamilyHandle value_1 : columnFamilyHandles) {
            Serializer.encodeVarInt32(buf, c_Tag);
            Serializer.encodeVarInt32(buf, value_1.getByteSize());
            value_1.encode(buf);
        }
    }

    public final static int columnfamilyhandleid_Num = 2;
    public final static int columnfamilyhandleid_Tag = 16;// the value is num<<<3|wireType
    public final static int columnfamilyhandleid_TagEncodeSize = 1;

    private void encode_columnfamilyhandleid(ByteBuf buf) {
        Serializer.encodeVarInt32(buf, columnfamilyhandleid_Tag);
        Serializer.encodeVarInt64(buf,columnFamilyHandleId );
    }

    private static void decode_columnfamilyhandleid(ByteBuf buf, OptimisticTransactionDB a_1) {
        a_1.columnFamilyHandleId = Serializer.decodeVarInt64(buf);
    }

    public static OptimisticTransactionDB decode(ByteBuf buf, int length_1) {
        OptimisticTransactionDB value_1 = new OptimisticTransactionDB();
        int f_Index = buf.readerIndex();
        int end=f_Index + length_1;
        while (buf.readerIndex() < end) {
            int num_1 = Serializer.decodeVarInt32(buf);
            switch (num_1) {
                case c_Tag :
                    decode_c(buf, value_1);
                    break;
                case columnfamilyhandleid_Tag :
                    decode_columnfamilyhandleid(buf, value_1);
                    break;
                default :
                    Serializer.skipUnknownField(num_1, buf);
            }
        }
        return value_1;
    }

    public void encode(ByteBuf buf) {
        if (columnFamilyHandles!=null) {
            this.encode_c(buf);
        }

    }

    public int getByteSize() {
        int DB_size = c_TagEncodeSize * columnFamilyHandles.size();// add tag length
        for (ColumnFamilyHandle value_1 : columnFamilyHandles) {
            int length_1 = 0;

            length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
            length_1 += value_1.getByteSize();
            DB_size += length_1;
        }

        DB_size += columnfamilyhandleid_TagEncodeSize;
        DB_size +=Serializer.computeVarInt64Size(columnFamilyHandleId);
        return DB_size;
    }

}
