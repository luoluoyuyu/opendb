package net.openio.jrocksDb.transaction;

import lombok.Data;

import java.util.Date;

@Data
public class Snapshot {

    private static volatile long time=new Date().getTime();

    private static volatile int id=1;

    volatile PrepareId prepareId;

    volatile CommitId commitId;

    volatile SequenceNumber number;


    static synchronized PrepareId p() {
        Date date = new Date();
        if (date.getTime() == time) {
            return new PrepareId(date.getTime(), id++);
        }
        id = 1;
        time = date.getTime();
        return new PrepareId(date.getTime(), id++);
    }

    static synchronized CommitId c() {
        Date date = new Date();
        if (date.getTime() == time) {
            return new CommitId(date.getTime(), id++);
        }
        id = 1;
        time = date.getTime();
        return new CommitId(date.getTime(), id++);
    }

    static synchronized void c(CommitId commitId) {
        Date date = new Date();
        if (date.getTime() == time) {
             commitId.setId(id++);
             commitId.setTimes(date.getTime());
             commitId.setInit(true);
        }
        id = 1;
        time = date.getTime();
        commitId.setId(id++);
        commitId.setTimes(date.getTime());
        commitId.setInit(true);;
    }


    static synchronized SequenceNumber s() {
        Date date = new Date();
        if (date.getTime() == time) {
            return new SequenceNumber(date.getTime(), id++);
        }
        id = 1;
        time = date.getTime();
        return new SequenceNumber(date.getTime(), id++);
    }

    public Snapshot(){
        setPrepareId(p());
        setNumber(s());
        setCommitId(new CommitId(null,null));
    }

    public void commit(){
        c(this.commitId);
    }

}
