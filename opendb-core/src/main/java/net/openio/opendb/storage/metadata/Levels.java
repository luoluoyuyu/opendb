package net.openio.opendb.storage.metadata;


import net.openio.opendb.storage.sstable.SSTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Levels {

  private List<Level> levels;

  private int level0CompactionNumTrigger;

  private int level0CompactionTrigger;

  private int allSize;

  private int beingCompactedLevel;

  private List<SSTable> waitToMerge;

  public Levels() {
    levels = new ArrayList<>();
    waitToMerge = new ArrayList<>();
  }

  public void addList(int level, List<SSTable> ssTables) {
    for (Level level1 : levels) {
      if (level1.getLevel() == level) {
        level1.addSSTables(ssTables);
        return;
      }
    }
    updateToil();
  }

  public Level getLevel(int level) {
    for (Level level1 : levels) {
      if (level1.getLevel() == level) {
        return level1;
      }
    }
    return null;
  }

  public void updateToil() {
    int i = 0;
    for (Level level1 : levels) {
      i += level1.getTotalSize();
    }
    allSize = i;
  }

  public Levels copy() {
    Levels copy = new Levels();

    for (Level level : levels) {
      copy.levels.add(level.copy());
    }
    copy.level0CompactionNumTrigger = this.level0CompactionNumTrigger;
    copy.level0CompactionTrigger = this.level0CompactionTrigger;
    copy.allSize = this.allSize;
    copy.beingCompactedLevel = this.beingCompactedLevel;

    copy.waitToMerge = new LinkedList<>(waitToMerge);

    return copy;
  }


  public List<Level> getLevels() {
    return levels;
  }

  public void setLevels(List<Level> levels) {
    this.levels = levels;
  }

  public void addLevels(Level level) {
    this.levels.add(level);
  }

  public int getLevel0CompactionNumTrigger() {
    return level0CompactionNumTrigger;
  }

  public void setLevel0CompactionNumTrigger(int level0CompactionNumTrigger) {
    this.level0CompactionNumTrigger = level0CompactionNumTrigger;
  }

  public int getLevel0CompactionTrigger() {
    return level0CompactionTrigger;
  }

  public void setLevel0CompactionTrigger(int level0CompactionTrigger) {
    this.level0CompactionTrigger = level0CompactionTrigger;
  }

  public int getAllSize() {
    return allSize;
  }

  public void setAllSize(int allSize) {
    this.allSize = allSize;
  }

  public int getBeingCompactedLevel() {
    return beingCompactedLevel;
  }

  public void setBeingCompactedLevel(int beingCompactedLevel) {
    this.beingCompactedLevel = beingCompactedLevel;
  }

  public List<SSTable> getWaitToMerge() {
    return waitToMerge;
  }

  public void setWaitToMerge(List<SSTable> waitToMerge) {
    this.waitToMerge = waitToMerge;
  }

  public void addWaitToMerge(SSTable waitToMerge) {
    this.waitToMerge.add(waitToMerge);
  }

}
