package BlockTest;

import net.openio.jrocksDb.memArena.MemArena;
import net.openio.jrocksDb.strorage.BloomBlocks;
import net.openio.jrocksDb.strorage.IndexOffset;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class BloomBlocksTest {

    public static void main(String[] args) {
        int[] b=new int[1024];
        Random random=new Random();
        for(int i=0;i<1024;i++){
            b[i]=(int)(Math.random()*100000000);
        }

        BloomBlocks bloomBlocks=new BloomBlocks();
        bloomBlocks.setMemArena(new MemArena());
        File file=new File("src/main/resources/Block/BloomBlocks");
        try {

            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
            bloomBlocks.flush(randomAccessFile.getChannel(),0,b);
            int in=0;
            for(int i: bloomBlocks.getBloomFile(randomAccessFile.getChannel(),0)){
                
                if(!(i==b[in++])){
                    System.out.println(i);
                }
            }
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
