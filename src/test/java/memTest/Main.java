package memTest;

import net.openio.jrocksDb.db.*;
import net.openio.jrocksDb.mem.KeyValueEntry;
import net.openio.jrocksDb.mem.SkipListRep;
import net.openio.jrocksDb.transaction.CommitId;
import net.openio.jrocksDb.transaction.PrepareId;
import net.openio.jrocksDb.transaction.SequenceNumber;

import java.util.ArrayList;
import java.util.Date;

public class Main {



    static int p=1;

    static long b=new Date().getTime();

    static long isnull=0;

    static long nonull=0;



    public static void main(String[] args) {
        ArrayList<Thread> list=new ArrayList<>();
        Thread t=Thread.currentThread();
        SkipListRep skipListRep=new SkipListRep(false);

        new KeyValueEntry();
        Key key = new IntKey(2);
        Value value = new IntValue(2);
        KeyValueEntry keyValue = new KeyValueEntry();
        keyValue.setKey(key);
        keyValue.setValue(value);
        keyValue.setPrepareId(p());
        keyValue.setCommitId(c());
        keyValue.setSqId(s());
        keyValue.setType(KeyValueEntry.Type.insert);
        KeyValueEntry keyValue1 = new KeyValueEntry();
        keyValue1.setKey(key);
        keyValue1.setPrepareId(p());
        keyValue1.setCommitId(c());
        keyValue1.setType(KeyValueEntry.Type.insert);
        skipListRep.addKeyValue(keyValue);

        for(int i=20;i>0;i--) {
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i <  1000; i++) {
                        new KeyValueEntry();
                        Key key = new IntKey(i);
                        Value value = new IntValue(i);
                        KeyValueEntry keyValue1 = new KeyValueEntry();
                        keyValue1.setKey(key);
                        keyValue1.setPrepareId(p());
                        keyValue1.setCommitId(c());
                        skipListRep.addKeyValue(keyValue);
                    }


                }
            };
            Thread thread=new Thread(runnable);
            thread.start();
            list.add(thread);

        }

        for(int i=20;i>0;i--) {

            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <  1000; i++ ) {
//                        try {
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        new KeyValueEntry();
                        Key key = new IntKey(i);
                        Value value = new IntValue(i);
                        KeyValueEntry keyValue = new KeyValueEntry();
                        keyValue.setKey(key);
                        keyValue.setValue(value);

                        keyValue.setPrepareId(p());


                        if(skipListRep.getValue(keyValue1)==null){
                            synchronized(Main.class){
                                isnull++;
                            }
                        }else {
                            synchronized(Main.class) {
                                nonull++;
                            }
                        }
                    }

                }
            };
            Thread thread=new Thread(runnable);
            thread.start();
            list.add(thread);

        }


        for(Thread a: list){
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(nonull);
        System.out.println(isnull);

    }

    static synchronized PrepareId p(){
        Date date=new Date();
        if(date.getTime()==b){
            return new PrepareId(date.getTime(),p++);
        }
        p=1;
        b=date.getTime();
        return new PrepareId(date.getTime(),p++);
    }

    static synchronized CommitId c(){
        Date date=new Date();
        if(date.getTime()==b){
            return new CommitId(date.getTime(),p++);
        }
        p=1;
        b=date.getTime();
        return new CommitId(date.getTime(),p++);    }



    static synchronized SequenceNumber s(){
        Date date=new Date();
        if(date.getTime()==b){
            return new SequenceNumber(date.getTime(),p++);
        }
        p=1;
        b=date.getTime();
        return new SequenceNumber(date.getTime(),p++);    }

}
