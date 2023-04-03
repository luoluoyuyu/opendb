package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class IntKey {

	private int IntKey_size = 0;

	private java.lang.Integer key;
	public final static int key_Num = 1;
	public final static int key_Tag = 8;// the value is num<<<3|wireType
	public final static int key_TagEncodeSize = 1;

	private void encode_key(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, key_Tag);
		Serializer.encodeVarInt32(buf, this.key);
	}

	private static void decode_key(ByteBuf buf, IntKey a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.key = value_1;
	}

	private void set_key(java.lang.Integer value_1) {
		IntKey_size += key_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		IntKey_size += size_1;
		this.key = value_1;
	}

	public java.lang.Integer getKey() {
		return this.key;
	}

	private boolean hasKey() {
		return this.key != null;
	}

	public static IntKey decode(ByteBuf buf) {
		IntKey value_1 = new IntKey();
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
		value_1.IntKey_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasKey()) {
			this.encode_key(buf);
		}

	}
	public static IntKey decode(ByteBuf buf, int length_1) {
		IntKey value_1 = new IntKey();
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
		value_1.IntKey_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.IntKey_size;
	}

	public static IntKeyBuild newBuilder() {
		return new IntKeyBuild();
	}

	private void verify() {
		if (this.key == null) {
			throw new RuntimeException("required key");
		}
	}

	public static class IntKeyBuild {
		private java.lang.Integer key;

		public IntKeyBuild setKey(java.lang.Integer a) {
			this.key = a;
			return this;
		}

		public java.lang.Integer getKey() {
			return this.key;
		}

		public IntKeyBuild clearKey() {
			this.key = null;
			return this;
		}

		public boolean hasKey() {
			return this.key != null;
		}

		public IntKey build() {
			IntKey value_1 = new IntKey();
			if (this.key == null) {
				throw new RuntimeException(" key is required");
			}
			value_1.set_key(this.key);
			return value_1;
		}
		public IntKeyBuild clear() {
			this.key = null;
			return this;
		}

		private IntKeyBuild() {
		}
	}

}
