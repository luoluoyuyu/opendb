package net.openio.jrocksDb.db;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.openio.jrocksDb.mem.Hash;
import net.openio.jrocksDb.tool.Serializer;


@Data
public class LongValue implements Value{

    Long value;

    @Override
    public int getByteSize() {
        if( value ==null) return 0;
        return Serializer.computeVarInt64Size(value);
    }

    @Override
    public void toByte(ByteBuf buf) {
        if(value==null)return;
        Serializer.encodeVarInt64(buf,value);
    }

    @Override
    public int getHash() {
        if(value==null) return 0;
        return Hash.longHash(value);
    }

    public static LongValue getValue(ByteBuf buf){
        return new LongValue(Serializer.decodeVarInt64(buf));
    }

    public LongValue(Long value){
        this.value=value;
    }

    public void setValue(Long value){
        this.value=value;
    }

    public Long getValue(Long value){
        return this.value;
    }
}
