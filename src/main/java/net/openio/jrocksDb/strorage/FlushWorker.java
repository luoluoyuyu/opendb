package net.openio.jrocksDb.strorage;

import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.log.WalTask;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FlushWorker implements Runnable{

    ConcurrentLinkedQueue<FlushTask> queue;

    Block block;


    @Override
    public void run() {
        while (true) {
            FlushTask flushTask = queue.poll();
            if (flushTask == null) continue;
            ColumnFamilyHandle columnFamilyHandle = flushTask.columnFamilyHandle;
            String fileName = columnFamilyHandle.getFileName();
            block.createFile(columnFamilyHandle.getFileName(), columnFamilyHandle.getColumnFamilyId());
            SSTable ssTable=block.flush(flushTask.memTable,fileName);
            columnFamilyHandle.addFileLevel(ssTable);
            if(queue.size()<5){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public FlushWorker(ConcurrentLinkedQueue concurrentLinkedQueue){
        this.queue=concurrentLinkedQueue;
        block=new Block();
    }
}
