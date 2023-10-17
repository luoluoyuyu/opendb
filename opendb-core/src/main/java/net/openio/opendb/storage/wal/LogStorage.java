package net.openio.opendb.storage.wal;


import net.openio.opendb.log.Log;
import net.openio.opendb.memarena.MemArena;
import net.openio.opendb.model.SequenceNumber;
import net.openio.opendb.tool.FileUtils;
import net.openio.opendb.tool.IDGenerator;
import net.openio.opendb.tool.codec.log.LogProtoCodec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class LogStorage {


  private final String fileDir;

  private final int walFileHeadSize;

  private final int dataStartSeek;

  private final int pageSize;

  private String fileName;


  private LogFileHead logFileHead;


  private final LogHeadStorage headStorage;

  private final LogBodyStorage bodyStorage;

  public int logFileSize() {
    return logFileHead.getLength();
  }

  public String getFileName() {
    return fileName;
  }

  public boolean addLogs(List<Log> list) throws IOException {
    FileChannel fileChannel = null;
    RandomAccessFile randomAccessFile = null;
    try {
      File file = new File(fileDir + fileName);
      randomAccessFile = new RandomAccessFile(file, "rw");
      fileChannel = randomAccessFile.getChannel();
      int l = bodyStorage.flush(fileChannel,
        logFileHead.getLength() + dataStartSeek, list);
      logFileHead.setLength(logFileHead.getLength() + l);
      logFileHead.add(l, pageSize);
      headStorage.flush(fileChannel, 0, logFileHead);
      randomAccessFile.getChannel().force(true);
      headStorage.flush(fileChannel, walFileHeadSize, logFileHead);
      fileChannel.close();
      randomAccessFile.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      close(fileChannel, randomAccessFile);
    }
    return true;
  }

  public void setFileName(String fileName) throws IOException {
    this.fileName = fileName;
    FileChannel fileChannel = null;
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = getRandomAccessFile(fileDir + fileName);
      fileChannel = randomAccessFile.getChannel();
      logFileHead = getFileHead(fileChannel);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(fileChannel, randomAccessFile);
    }
  }

  private RandomAccessFile getRandomAccessFile(String fileName) throws FileNotFoundException {
    File file = new File(fileDir + fileName);
    return new RandomAccessFile(file, "rw");

  }

  public boolean addLogsNot(List<Log> list) throws IOException {
    FileChannel fileChannel = null;
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = getRandomAccessFile(fileDir + fileName);
      fileChannel = randomAccessFile.getChannel();
      int l = bodyStorage.flush(fileChannel,
        logFileHead.getLength() + dataStartSeek, list);
      logFileHead.setLength(logFileHead.getLength() + l);
      logFileHead.add(logFileHead.getLength());
      headStorage.flush(fileChannel, 0, logFileHead);
      randomAccessFile.getChannel().force(true);
      headStorage.flush(fileChannel, walFileHeadSize, logFileHead);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      close(fileChannel, randomAccessFile);
    }
    return true;
  }

  public boolean creatNewFile(String fileName) {
    logFileHead = new LogFileHead();
    this.fileName = fileName;
    logFileHead.setCreateTime(new Date().getTime());

    return createFile(fileName);
  }

  public String createNewFile() {
    String fileName = IDGenerator.generateUniqueFileName();
    FileUtils.createFileIfNotExists(fileDir + fileName);
    logFileHead = new LogFileHead();
    this.fileName = fileName;
    logFileHead.setCreateTime(new Date().getTime());

    return fileName;
  }

  public List<Log> getLogs(SequenceNumber sequenceNumber, String fileName) throws IOException {
    List<Log> list = new LinkedList<>();
    LogFileHead logFileHead = null;
    FileChannel fileChannel = null;
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = getRandomAccessFile(fileDir + fileName);
      fileChannel = randomAccessFile.getChannel();
      logFileHead = getFileHead(fileChannel);
      randomAccessFile.close();
    } catch (Exception e) {
      return list;
    } finally {
      close(fileChannel, randomAccessFile);
    }
    if (logFileHead == null) {
      return list;
    }
    int f = dataStartSeek;
    boolean is = false;
    try {
      for (int seek : logFileHead.getBlockEndSeek()) {
        int i = 0;
        List<Log> logs = bodyStorage.getLogs(fileChannel, f, seek - f);
        f = seek;
        if (is) {
          list.addAll(logs);
        } else {
          for (Log log : logs) {
            if (log.compare(sequenceNumber) >= 0) {
              list.addAll(logs.subList(i, logs.size()));
              is = true;
              break;
            }
            i++;
          }
        }
      }

    } catch (IOException ioException) {
      ioException.printStackTrace();
    } finally {
      close(fileChannel, randomAccessFile);
    }

    return list;

  }

  private boolean createFile(String fileName) {
    File file = new File(fileName);
    if (file.exists()) {
      return false;
    }
    try {
      return file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public List<Log> getByTransactionId(long transactionId, String fileName) throws IOException {
    List<Log> list = new LinkedList<>();
    LogFileHead logFileHead = null;
    FileChannel fileChannel = null;
    RandomAccessFile randomAccessFile = null;
    try {
      File file = new File(fileDir + fileName);
      randomAccessFile = new RandomAccessFile(file, "rw");
      fileChannel = randomAccessFile.getChannel();
      logFileHead = getFileHead(fileChannel);
    } catch (Exception e) {
      return list;
    } finally {
      close(fileChannel, randomAccessFile);
    }
    if (logFileHead == null) {
      return list;
    }

    int f = dataStartSeek;
    try {
      for (int seek : logFileHead.getBlockEndSeek()) {
        List<Log> logs = bodyStorage.getLogs(fileChannel, f, seek - f);
        f = seek;
        if (logs.get(0).getTransactionId() == transactionId) {
          return logs;
        }
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
    } finally {
      close(fileChannel, randomAccessFile);
    }
    return list;
  }

  private LogFileHead getFileHead(FileChannel fileChannel) {

    LogFileHead logFileHead = null;
    boolean canCodec = true;
    try {
      logFileHead = headStorage.getFileHead(fileChannel, 0);
    } catch (Exception e) {
      e.printStackTrace();
      canCodec = false;
    }

    if (!canCodec) {
      try {
        logFileHead = headStorage.getFileHead(fileChannel, walFileHeadSize);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
    return logFileHead;
  }

  public LogStorage(MemArena memArena, int pageSize,
                    int walFileHeadSize,
                    LogProtoCodec logProtoCodec,
                    String fileDir) {
    this.walFileHeadSize = walFileHeadSize;
    headStorage = new LogHeadStorage(memArena, walFileHeadSize);
    bodyStorage = new LogBodyStorage(memArena, logProtoCodec);
    dataStartSeek = walFileHeadSize << 1;
    this.fileDir = fileDir;
    this.pageSize = pageSize;
    fileName = createNewFile();
  }


  private void close(FileChannel fileChannel, RandomAccessFile randomAccessFile) throws IOException {
    if (randomAccessFile != null) {
      randomAccessFile.close();
    }
    if (fileChannel != null) {
      fileChannel.close();
    }
  }
}
