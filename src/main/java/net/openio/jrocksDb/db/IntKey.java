package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.mem.Hash;
import net.openio.jrocksDb.tool.Serializer;

import java.util.Objects;


@Data
public class IntKey implements Key{

    private int key;

    @Override
    public int getByteSize() {
        return Serializer.computeVarInt32Size(key);
    }

    @Override
    public void toByte(ByteBuf buf) {
        Serializer.encodeVarInt32(buf,key);
    }


    public static IntKey getKey(ByteBuf date) {
        return new IntKey(Serializer.decodeVarInt32(date));
    }

    @Override
    public int getHash() {
        return Hash.intHash(key);
    }

    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public void SetKey(Object l) {
        if (l == null) throw new RuntimeException("o is null");
        if (!(l instanceof Integer)) throw new RuntimeException("o is not int Type");
        key = (int) l;
    }

    @Override
    public int compareTo(Object o) {
        if(o==null){
            throw new RuntimeException("o is null");
        }
        if(!(o instanceof IntKey)){
            throw new RuntimeException(" o is not IndKey Type");
        }
        long key=((IntKey) o).key;
        return (this.key < key) ? -1 : ((this.key == key) ? 0 : 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntKey intKey = (IntKey) o;
        return key == intKey.key;
    }

    @Override
    public int hashCode() {
        return Hash.intHash(key);
    }

    public IntKey(int key){
        this.key=key;
    }

    @Override
    public String toString() {
        return "IntKey{" +
                "key=" + key +
                '}';
    }
}
