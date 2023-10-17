package net.openio.opendb.model;


public class SequenceNumber implements Comparable {

  private volatile Long times;


  @Override
  public int compareTo(Object o) {
    if (!(o instanceof SequenceNumber)) {
      throw new RuntimeException("SequenceNumber is null or o type not SequenceNumber");
    }
    return Long.compare(times, ((SequenceNumber) o).times);
  }

  public SequenceNumber() {
  }

  public SequenceNumber(Long times) {
    this.times = times;
  }


  public Long getTimes() {
    return times;
  }

  public void setTimes(Long times) {
    this.times = times;
  }

}
