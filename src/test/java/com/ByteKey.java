package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class ByteKey {

	private int ByteKey_size = 0;

	private byte[] key;
	public final static int key_Num = 1;
	public final static int key_Tag = 10;// the value is num<<<3|wireType
	public final static int key_TagEncodeSize = 1;

	private void encode_key(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, key_Tag);
		Serializer.encodeByteString(buf, this.key);
	}

	private static void decode_key(ByteBuf buf, ByteKey a_1) {
		byte[] value_1 = null;
		value_1 = Serializer.decodeByteString(buf, Serializer.decodeVarInt32(buf));
		a_1.key = value_1;
	}

	private void set_key(byte[] value_1) {
		ByteKey_size += key_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.length);
		size_1 += value_1.length;
		ByteKey_size += size_1;
		this.key = value_1;
	}

	public byte[] getKey() {
		return this.key;
	}

	private boolean hasKey() {
		return this.key != null;
	}

	public static ByteKey decode(ByteBuf buf) {
		ByteKey value_1 = new ByteKey();
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
		value_1.ByteKey_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasKey()) {
			this.encode_key(buf);
		}

	}
	public static ByteKey decode(ByteBuf buf, int length_1) {
		ByteKey value_1 = new ByteKey();
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
		value_1.ByteKey_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.ByteKey_size;
	}

	public static ByteKeyBuild newBuilder() {
		return new ByteKeyBuild();
	}

	private void verify() {
		if (this.key == null) {
			throw new RuntimeException("required key");
		}
	}

	public static class ByteKeyBuild {
		private byte[] key;

		public ByteKeyBuild setKey(byte[] a) {
			this.key = a;
			return this;
		}

		public byte[] getKey() {
			return this.key;
		}

		public ByteKeyBuild clearKey() {
			this.key = null;
			return this;
		}

		public boolean hasKey() {
			return this.key != null;
		}

		public ByteKey build() {
			ByteKey value_1 = new ByteKey();
			if (this.key == null) {
				throw new RuntimeException(" key is required");
			}
			value_1.set_key(this.key);
			return value_1;
		}
		public ByteKeyBuild clear() {
			this.key = null;
			return this;
		}

		private ByteKeyBuild() {
		}
	}

}
