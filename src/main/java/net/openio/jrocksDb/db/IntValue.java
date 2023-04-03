package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.mem.Hash;
import net.openio.jrocksDb.tool.Serializer;


@Data
public class IntValue implements Value {

    Integer value;

    @Override
    public int getByteSize() {
        if( value ==null) return 0;
        return Serializer.computeVarInt32Size(value);
    }

    @Override
    public void toByte(ByteBuf buf) {
        if(value==null)return;
        Serializer.encodeVarInt32(buf,value);
    }

    @Override
    public int getHash() {
        if(value==null) return 0;
        return Hash.intHash(value);
    }

    public static IntValue getValue(ByteBuf buf){
        return new IntValue(Serializer.decodeVarInt32(buf));
    }

    public IntValue(Integer value){
        this.value=value;
    }

    public void setValue(Integer value){
        this.value=value;
    }

    public Integer getValue(Integer value){
        return this.value;
    }

    @Override
    public String toString() {
        return "IntValue{" +
                "value=" + value +
                '}';
    }
}
