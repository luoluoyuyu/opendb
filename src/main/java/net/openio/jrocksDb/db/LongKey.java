package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.mem.Hash;
import net.openio.jrocksDb.tool.Serializer;

import java.util.Objects;

@Data
public class LongKey implements Key {

    long key;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongKey key1 = (LongKey) o;
        return key == key1.key;
    }

    @Override
    public int hashCode() {
       return Hash.longHash(key);
    }

    @Override
    public int getByteSize() {
        return Serializer.computeVarInt64Size(key);
    }

    @Override
    public void toByte(ByteBuf buf) {
        Serializer.encodeVarInt64(buf, key);
    }


    public static LongKey getKey(ByteBuf date) {
        return new LongKey(Serializer.decodeVarInt64(date));
    }

    @Override
    public int getHash() {
        return Hash.longHash(key);
    }

    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public void SetKey(Object l) {
        if (l == null) throw new RuntimeException("o is null");
        if (!(l instanceof Long)) throw new RuntimeException("o is not Long Type");
        key = (long) l;
    }


    @Override
    public int compareTo(Object o) {
        if (o == null) {
            throw new RuntimeException("o is null");
        }
        if (!(o instanceof LongKey)) {
            throw new RuntimeException(" o is not LongKey Type");
        }
        long key = ((LongKey) o).key;
        return (this.key < key) ? -1 : ((this.key == key) ? 0 : 1);
    }

    public LongKey(long key) {
        this.key = key;
    }
}
