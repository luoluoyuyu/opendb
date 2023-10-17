package net.openio.opendb.storage.metadata;

import net.openio.opendb.db.ColumnFamily;
import net.openio.opendb.model.SequenceNumber;

import java.util.ArrayList;
import java.util.List;

public class DataMeta {

  private SequenceNumber maxNumber = new SequenceNumber(0L);

  private SequenceNumber unPersistedSeqNumberLow = new SequenceNumber(0L);

  private List<ColumnFamily> columnFamilies;

  private List<String> walLog;

  private List<String> undoLog;

  private List<String> checkpointFile;

  public DataMeta() {
    columnFamilies = new ArrayList<>();

    walLog = new ArrayList<>();

    undoLog = new ArrayList<>();

    checkpointFile = new ArrayList<>();
  }

  public void addColumnFamily(ColumnFamily columnFamily) {
    columnFamilies.add(columnFamily);
  }

  public void addWalLog(String walLog) {
    this.walLog.add(walLog);
  }

  public void addUndoWalLog(String log) {
    this.undoLog.add(log);
  }

  public void addCheckPointFile(String file) {
    this.checkpointFile.add(file);

  }

  public SequenceNumber getMaxNumber() {
    return maxNumber;
  }

  public void setMaxNumber(SequenceNumber maxNumber) {
    this.maxNumber = maxNumber;
  }

  public SequenceNumber getUnPersistedSeqNumberLow() {
    return unPersistedSeqNumberLow;
  }

  public void setUnPersistedSeqNumberLow(SequenceNumber unPersistedSeqNumberLow) {
    this.unPersistedSeqNumberLow = unPersistedSeqNumberLow;
  }

  public List<ColumnFamily> getColumnFamilies() {
    return columnFamilies;
  }

  public void setColumnFamilies(List<ColumnFamily> columnFamilies) {
    this.columnFamilies = columnFamilies;
  }

  public List<String> getWalLog() {
    return walLog;
  }

  public void setWalLog(List<String> walLog) {
    this.walLog = walLog;
  }

  public List<String> getUndoLog() {
    return undoLog;
  }

  public void setUndoLog(List<String> undoLog) {
    this.undoLog = undoLog;
  }

  public List<String> getCheckpointFile() {
    return checkpointFile;
  }

  public void setCheckpointFile(List<String> checkpointFile) {
    this.checkpointFile = checkpointFile;
  }
}
