package net.openio.opendb.tool;

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {

  private static final AtomicLong FileNameCounter = new AtomicLong(System.currentTimeMillis());

  public static String generateUniqueFileName() {
    long currentTime = System.currentTimeMillis();
    long uniqueNumber = FileNameCounter.getAndIncrement();

    return currentTime + "_" + uniqueNumber;
  }
}
