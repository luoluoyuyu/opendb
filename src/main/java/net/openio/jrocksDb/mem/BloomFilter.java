package net.openio.jrocksDb.mem;

import io.netty.buffer.ByteBuf;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.tool.Serializer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public  class BloomFilter {

    private int[] data;

    private int length;

    private Lock lock;

    public BloomFilter(){
        length=1024;
        data = new int[length];
        lock=new ReentrantLock();
    }

    public BloomFilter(int length){
        this.length=roundUpToPowerOf2(length);
        if(length<=0){
            throw new RuntimeException("bloomFiler array size mem out ");
        }
        data = new int[this.length];
        lock=new ReentrantLock();
    }

    public BloomFilter(int[] bloom){
        length=bloom.length;
        if(length<=0){
            throw new RuntimeException("bloomFiler array size mem out ");
        }
        data = bloom;
        lock=new ReentrantLock();
    }

    private   int roundUpToPowerOf2(int x) {
        x--; // 先将 x 减 1
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        x++; // 最后将 x 加 1
        return x;
    }


    public void add(Key k){

        int index= getIndex(k.getHash());

        if(data[index]==Integer.MAX_VALUE){
            throw new RuntimeException("bloomFilter mem out");
        }
        lock.lock();
        data[index]++;
        lock.unlock();
    }





    public void delete(Key key){
        int index= getIndex(key.getHash());
        lock.lock();
        data[index]--;
        lock.unlock();
    }

    public void delete(Key key,int length){
        int index= getIndex(key.getHash());
        lock.lock();
        data[index]-=length;
        lock.unlock();
    }



    public boolean get(Key key){
        int index= getIndex(key.getHash());
        return data[index]!=0;
    }




    public int[] getData (){
        return data;
    }


    public void serializer(ByteBuf byteBuf){
        for(int i: data){
            Serializer.encode32(byteBuf,i);
        }
    }


    private int getIndex(int hash){
        return hash & (length - 1);
    }


}

