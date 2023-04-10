package net.openio.jrocksDb.log;

import lombok.AllArgsConstructor;
import net.openio.jrocksDb.mem.KeyValueEntry;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@AllArgsConstructor
public class WalLogWorker implements Runnable{

    ConcurrentLinkedQueue<WalTask> queue;

    final int workerSize=16;

    final int sleepTime=5;

    final int size=1024;

    public boolean end;

    @Override
    public void run() {
        Map<String,List<KeyValueEntry>> map=new HashMap<>();
        Map<String,WalStorage> walStorageMap=new HashMap<>();
        int l=0;
        for(;;) {
            if (queue.size() < workerSize) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (0 <queue.size() ) {
                WalTask walTask = queue.poll();
                List<KeyValueEntry> keyValues=map.get(walTask.fileName);
                if(keyValues==null) {
                    keyValues=new ArrayList<>();
                    keyValues.add(walTask.keyValueEntry);
                    walStorageMap.put(walTask.fileName, walTask.walStorage);
                    map.put(walTask.fileName,keyValues);
                }else {
                    keyValues.add(walTask.keyValueEntry);
                }
                l++;
                if(l>size||queue.size()==0){
                    for(String fileName:map.keySet()){
                        walStorageMap.get(fileName).write(fileName,map.get(fileName),false);
                    }

                    map=new HashMap<>();
                    walStorageMap=new HashMap<>();
                    l=0;

                }
            }
            if(end){
                break;
            }

        }

    }
}
