package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class LongKey {

	private int LongKey_size = 0;

	private java.lang.Long key;
	public final static int key_Num = 1;
	public final static int key_Tag = 8;// the value is num<<<3|wireType
	public final static int key_TagEncodeSize = 1;

	private void encode_key(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, key_Tag);
		Serializer.encodeVarInt64(buf, this.key);
	}

	private static void decode_key(ByteBuf buf, LongKey a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.key = value_1;
	}

	private void set_key(java.lang.Long value_1) {
		LongKey_size += key_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		LongKey_size += size_1;
		this.key = value_1;
	}

	public java.lang.Long getKey() {
		return this.key;
	}

	private boolean hasKey() {
		return this.key != null;
	}

	public static LongKey decode(ByteBuf buf) {
		LongKey value_1 = new LongKey();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case key_Tag :
					decode_key(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.LongKey_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasKey()) {
			this.encode_key(buf);
		}

	}
	public static LongKey decode(ByteBuf buf, int length_1) {
		LongKey value_1 = new LongKey();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case key_Tag :
					decode_key(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.LongKey_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.LongKey_size;
	}

	public static LongKeyBuild newBuilder() {
		return new LongKeyBuild();
	}

	private void verify() {
		if (this.key == null) {
			throw new RuntimeException("required key");
		}
	}

	public static class LongKeyBuild {
		private java.lang.Long key;

		public LongKeyBuild setKey(java.lang.Long a) {
			this.key = a;
			return this;
		}

		public java.lang.Long getKey() {
			return this.key;
		}

		public LongKeyBuild clearKey() {
			this.key = null;
			return this;
		}

		public boolean hasKey() {
			return this.key != null;
		}

		public LongKey build() {
			LongKey value_1 = new LongKey();
			if (this.key == null) {
				throw new RuntimeException(" key is required");
			}
			value_1.set_key(this.key);
			return value_1;
		}
		public LongKeyBuild clear() {
			this.key = null;
			return this;
		}

		private LongKeyBuild() {
		}
	}

}
