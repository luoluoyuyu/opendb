package net.openio.jrocksDb.strorage;

import net.openio.jrocksDb.db.ColumnFamilyHandle;
import net.openio.jrocksDb.log.WalTask;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FlushWorker implements Runnable {

    private ConcurrentLinkedQueue<FlushTask> queue;

    private Block block;

    private boolean end;


    @Override
    public void run() {
        while (true) {
            while ( 0< queue.size() ) {
                FlushTask flushTask = queue.poll();
                if (flushTask == null) continue;
                ColumnFamilyHandle columnFamilyHandle = flushTask.columnFamilyHandle;
                String fileName = columnFamilyHandle.getFileName();
                block.createFile(fileName, columnFamilyHandle.getColumnFamilyId());
                SSTable ssTable = block.flush(flushTask.memTable, fileName);
                columnFamilyHandle.addFileLevel(ssTable);
                flushTask.memTable.needFlush();
                columnFamilyHandle.getMemTableList().flush(flushTask.memTable);
            }

            if (end){
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    public FlushWorker(ConcurrentLinkedQueue<FlushTask> concurrentLinkedQueue) {
        this.queue = concurrentLinkedQueue;
        block = new Block();
    }
}
