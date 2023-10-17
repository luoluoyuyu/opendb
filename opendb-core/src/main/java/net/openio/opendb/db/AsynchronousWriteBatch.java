package net.openio.opendb.db;

import net.openio.opendb.log.Log;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.storage.wal.LogStorage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class AsynchronousWriteBatch extends WriteBatch {

  private final List<String> file;

  private final LogStorage walStorage;

  private final int size;

  ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

  public AsynchronousWriteBatch(List<String> file, LogStorage walStorage, int size, long maxSueque) {
    super(maxSueque);
    this.file = file;
    this.walStorage = walStorage;
    this.size = size;

    executorService.scheduleAtFixedRate(new WalTask(this), 1, 1, TimeUnit.SECONDS);

  }


  @Override
  public List<String> getFile() {
    return file;
  }

  @Override
  public List<Log> getWal(SequenceNumber sequenceNumber) {
    List<Log> walLogs = new LinkedList<>();
    for (int i = file.size() - 1; i >= 0; i--) {
      try {
        List<Log> l = null;
        walLogs.addAll(l = walStorage.getLogs(sequenceNumber, file.get(i)));
        if (l.size() == 0) {
          return walLogs;
        }
      } catch (IOException e) {
        e.printStackTrace();
        return walLogs;
      }
    }
    return walLogs;
  }

  @Override
  boolean isSyn() {
    return false;
  }

  @Override
  void step(List<Log> list) {
    try {
      this.notifyAll();
    } catch (Exception e) {
    }
  }

  @Override
  void waitForNextNode(WriteBatch.Node snapshot) {
    do {
      try {
        this.wait();
      } catch (Exception e) {
      }
    } while (this.writeSequenceNumber < snapshot.sequenceNumber.getTimes());

    snapshot.pre.next = null;
  }

  @Override
  void close() {
    executorService.shutdown();
  }

  class WalTask implements Runnable {

    final WriteBatch writeBatch;

    @Override
    public void run() {
      List<Log> list = null;
      synchronized (writeBatch) {
        if (writeBatch.logs.size() == 0) {
          return;
        }
        list = writeBatch.logs;
        this.writeBatch.logs = new LinkedList<>();
      }
      try {
        walStorage.addLogs(list);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("can not write logs");
      }
      if (walStorage.logFileSize() > size) {
        file.add(walStorage.createNewFile());
      }
    }


    WalTask(WriteBatch writeBatch) {
      this.writeBatch = writeBatch;
    }
  }
}
