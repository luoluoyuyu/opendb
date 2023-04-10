package LogTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import net.openio.jrocksDb.db.FileList;
import net.openio.jrocksDb.db.IntKey;
import net.openio.jrocksDb.strorage.SSTable;

public class FileListTest {

    public static void main(String[] args) {
        SSTable ssTable=new SSTable("12354",new IntKey(1),new IntKey(2),1231);
        int size1=ssTable.getByteSize();
        ByteBuf buf1=UnpooledByteBufAllocator.DEFAULT.buffer(size1);
        System.out.println(size1);
        ssTable.encode(buf1);
        System.out.println(buf1);
        System.out.println(SSTable.decode(buf1,size1));


        FileList fileListTest=new FileList();
//        fileListTestadd(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getSSTable(0).add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getSSTable(0).add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getSSTable(0).add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getSSTable(0).add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getLevel2().add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getLevel3().add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getLevel3().add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getLevel4().add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));
//        fileListTest.getLevel4().add(new SSTable("12354",new IntKey(1),new IntKey(2),1231));

        int size= fileListTest.getByteSize();
        ByteBuf buf=  UnpooledByteBufAllocator.DEFAULT.buffer(size);
        System.out.println(size);
        fileListTest.encode(buf);
        System.out.println(buf);
        FileList.decode(buf,size);

    }
}
