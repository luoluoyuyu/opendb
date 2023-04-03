package net.openio.jrocksDb.log;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@AllArgsConstructor
public class WalLogWorker implements Runnable{

    ConcurrentLinkedQueue<WalTask> queue;

    final int workerSize=16;

    final int sleepTime=1;

    @Override
    public void run() {
        if(queue.size()<workerSize){
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<queue.size();i++) {
            WalTask walTask=queue.poll();
            walTask.walStorage.write(walTask.fileName,walTask.keyValueEntry,walTask.isFlush);
        }

    }
}
