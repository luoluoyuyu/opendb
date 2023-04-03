package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.mem.Hash;
import net.openio.jrocksDb.tool.Serializer;

import java.util.Arrays;

@Data
public class BytesKey implements Key{

    private byte[] key;

    @Override
    public int getByteSize() {
        return key.length;
    }

    @Override
    public void toByte(ByteBuf buf) {
        Serializer.encodeByteString(buf,key);
    }

    public static Key getKey(ByteBuf date, int length) {
        return new BytesKey(Serializer.decodeByteString(date,length));
    }

    @Override
    public int getHash() {
        return Hash.hash32(key,key.length);
    }

    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public void SetKey(Object o) {
        if (o == null) throw new RuntimeException("o is null");
        if (!(o instanceof byte[])) throw new RuntimeException("o is not Byte[] Type");
        key = (byte[]) o;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) throw new RuntimeException("o is null");
        if (!(o instanceof byte[])) throw new RuntimeException("o is not Byte[] Type");
        byte[] a=(byte[])o;
        for(int i=0,j=0;i<a.length&&j<key.length;i++,j++){
                if(a[i]==key[j]){
                    continue;
                }
                if(a[i]>key[j]) return -1;

                return 1;
        }
        if(a.length>key.length) return -1;

        if(a.length==key.length) return 0;

        return 1;

    }


    public BytesKey(byte[] key){
        this.key=key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BytesKey bytesKey = (BytesKey) o;
        return Arrays.equals(key, bytesKey.key);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
    }
}
