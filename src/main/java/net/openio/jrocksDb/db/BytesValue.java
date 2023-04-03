package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.mem.Hash;
import net.openio.jrocksDb.tool.Serializer;


@Data
public class BytesValue implements Value{

    private byte[] value;


    public int getByteSize() {
        if(value==null) return 0;
        return value.length;
    }


    public void toByte(ByteBuf buf) {
        Serializer.encodeByteString(buf,value);
    }

    public static BytesValue getValue(ByteBuf date, int length) {
        return new BytesValue(Serializer.decodeByteString(date,length));
    }


    public int getHash() {
        return Hash.hash32(value,value.length);
    }


    public Object getValue() {
        return value;
    }


    public void SetValue(Object o) {
        if (o == null) throw new RuntimeException("o is null");
        if (!(o instanceof byte[])) throw new RuntimeException("o is not Byte[] Type");
        value = (byte[]) o;
    }


    public int compareTo(Object o) {
        if (o == null) throw new RuntimeException("o is null");
        if (!(o instanceof byte[])) throw new RuntimeException("o is not Byte[] Type");
        byte[] a=(byte[])o;
        for(int i=0,j=0;i<a.length&&j<value.length;i++,j++){
            if(a[i]==value[j]){
                continue;
            }
            if(a[i]>value[j]) return -1;

            return 1;
        }
        if(a.length>value.length) return -1;

        if(a.length==value.length) return 0;

        return 1;

    }


    public BytesValue(byte[] value){
        this.value=value;
    }
}
