package TransactionTest;

import net.openio.jrocksDb.db.ColumnFamilyId;
import net.openio.jrocksDb.db.IntKey;
import net.openio.jrocksDb.db.Key;
import net.openio.jrocksDb.transaction.SequenceNumber;
import net.openio.jrocksDb.transaction.lock.PointLockManager;

import java.util.Date;

public class LockTest {

    static long a=1;
    public static void main(String[] args) {

     int i=0;
     for( i=0;i<10;i++) {
         Runnable runnable = new Runnable() {

             @Override
             public void run() {
                 testTryLockAndUnlock(a++,1,1,(int) a++);
             }
         };

         Thread thread=new Thread(runnable);
         thread.start();

     }


    }


    public static void testTryLockAndUnlock(long tid,long cid,int k,int s ) {
        PointLockManager lockManager = new PointLockManager(1000, 500);
        ColumnFamilyId cId = new ColumnFamilyId(cid);
        Key key = new IntKey(k);
        SequenceNumber sequenceNumber = new SequenceNumber(new Date().getTime(),s);

        SequenceNumber tryLockResult = lockManager.TryLock(tid, cId, key, true, false, sequenceNumber);
        System.out.println(tryLockResult);

        System.out.println(lockManager.GetPointLockStatus(cId,key));

        lockManager.UnLock(1, cId, key, true, false);
    }
}
