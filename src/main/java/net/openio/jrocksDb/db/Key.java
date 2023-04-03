package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Data;


public interface Key extends Comparable{

    int getByteSize();

    void toByte(ByteBuf buf);


    int getHash();

    Object getKey();

    void SetKey(Object o);

    enum KeyType{
        intKey(1),
        longKey(2),
        bytesKey(3);

        public static KeyType get(int tag) {
            if (tag == 1) {
                return KeyType.intKey;
            }
            if (tag == 2) {
                return KeyType.longKey;
            }
            if (tag == 3) {
                return KeyType.bytesKey;
            }
            return null;
        }
        int num;

        KeyType(int num) {
            this.num = num;
        }

        int getNum() {
            return this.num;
        }
    }

}
