package net.openio.opendb.storage.sstable;


public class FileHeadBlock {

  public int dataOfferSeek;

  public int dataOfferSize;

  public int indexOfferSeek;

  public int indexOfferSize;

  public int metaOfferSeek;

  public int metaOfferSize;

  public int bloomOfferSeek;

  public int bloomOfferSize;

  public FileHeadBlock() {
  }

  public int getDataOfferSeek() {
    return dataOfferSeek;
  }

  public void setDataOfferSeek(int dataOfferSeek) {
    this.dataOfferSeek = dataOfferSeek;
  }

  public int getDataOfferSize() {
    return dataOfferSize;
  }

  public void setDataOfferSize(int dataOfferSize) {
    this.dataOfferSize = dataOfferSize;
  }

  public int getIndexOfferSeek() {
    return indexOfferSeek;
  }

  public void setIndexOfferSeek(int indexOfferSeek) {
    this.indexOfferSeek = indexOfferSeek;
  }

  public int getIndexOfferSize() {
    return indexOfferSize;
  }

  public void setIndexOfferSize(int indexOfferSize) {
    this.indexOfferSize = indexOfferSize;
  }

  public int getMetaOfferSeek() {
    return metaOfferSeek;
  }

  public void setMetaOfferSeek(int metaOfferSeek) {
    this.metaOfferSeek = metaOfferSeek;
  }

  public int getMetaOfferSize() {
    return metaOfferSize;
  }

  public void setMetaOfferSize(int metaOfferSize) {
    this.metaOfferSize = metaOfferSize;
  }

  public int getBloomOfferSeek() {
    return bloomOfferSeek;
  }

  public void setBloomOfferSeek(int bloomOfferSeek) {
    this.bloomOfferSeek = bloomOfferSeek;
  }

  public int getBloomOfferSize() {
    return bloomOfferSize;
  }

  public void setBloomOfferSize(int bloomOfferSize) {
    this.bloomOfferSize = bloomOfferSize;
  }
}
