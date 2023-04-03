package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class Key {

	private int Key_size = 0;

	private com.IntKey intkey;
	public final static int intkey_Num = 1;
	public final static int intkey_Tag = 10;// the value is num<<<3|wireType
	public final static int intkey_TagEncodeSize = 1;

	private void encode_intkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, intkey_Tag);
		Serializer.encodeVarInt32(buf, this.intkey.getByteSize());
		this.intkey.encode(buf);
	}

	private static void decode_intkey(ByteBuf buf, Key a_1) {
		com.IntKey value_1 = null;
		value_1 = com.IntKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.intkey = value_1;
	}

	private void set_intkey(com.IntKey value_1) {
		Key_size += intkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		Key_size += size_1;
		this.intkey = value_1;
	}

	public com.IntKey getIntkey() {
		return this.intkey;
	}

	private boolean hasIntkey() {
		return this.intkey != null;
	}

	private com.LongKey longkey;
	public final static int longkey_Num = 2;
	public final static int longkey_Tag = 18;// the value is num<<<3|wireType
	public final static int longkey_TagEncodeSize = 1;

	private void encode_longkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, longkey_Tag);
		Serializer.encodeVarInt32(buf, this.longkey.getByteSize());
		this.longkey.encode(buf);
	}

	private static void decode_longkey(ByteBuf buf, Key a_1) {
		com.LongKey value_1 = null;
		value_1 = com.LongKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.longkey = value_1;
	}

	private void set_longkey(com.LongKey value_1) {
		Key_size += longkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		Key_size += size_1;
		this.longkey = value_1;
	}

	public com.LongKey getLongkey() {
		return this.longkey;
	}

	private boolean hasLongkey() {
		return this.longkey != null;
	}

	private com.ByteKey bytekey;
	public final static int bytekey_Num = 3;
	public final static int bytekey_Tag = 26;// the value is num<<<3|wireType
	public final static int bytekey_TagEncodeSize = 1;

	private void encode_bytekey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, bytekey_Tag);
		Serializer.encodeVarInt32(buf, this.bytekey.getByteSize());
		this.bytekey.encode(buf);
	}

	private static void decode_bytekey(ByteBuf buf, Key a_1) {
		com.ByteKey value_1 = null;
		value_1 = com.ByteKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.bytekey = value_1;
	}

	private void set_bytekey(com.ByteKey value_1) {
		Key_size += bytekey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		Key_size += size_1;
		this.bytekey = value_1;
	}

	public com.ByteKey getBytekey() {
		return this.bytekey;
	}

	private boolean hasBytekey() {
		return this.bytekey != null;
	}

	public static Key decode(ByteBuf buf) {
		Key value_1 = new Key();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case intkey_Tag :
					decode_intkey(buf, value_1);
					break;
				case longkey_Tag :
					decode_longkey(buf, value_1);
					break;
				case bytekey_Tag :
					decode_bytekey(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.Key_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasIntkey()) {
			this.encode_intkey(buf);
		}

		if (hasLongkey()) {
			this.encode_longkey(buf);
		}

		if (hasBytekey()) {
			this.encode_bytekey(buf);
		}

	}
	public static Key decode(ByteBuf buf, int length_1) {
		Key value_1 = new Key();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case intkey_Tag :
					decode_intkey(buf, value_1);
					break;
				case longkey_Tag :
					decode_longkey(buf, value_1);
					break;
				case bytekey_Tag :
					decode_bytekey(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.Key_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.Key_size;
	}

	public static KeyBuild newBuilder() {
		return new KeyBuild();
	}

	private void verify() {
	}

	public static class KeyBuild {
		private com.IntKey intkey;
		private com.LongKey longkey;
		private com.ByteKey bytekey;

		public KeyBuild setIntkey(com.IntKey a) {
			this.intkey = a;
			return this;
		}

		public com.IntKey getIntkey() {
			return this.intkey;
		}

		public KeyBuild clearIntkey() {
			this.intkey = null;
			return this;
		}

		public boolean hasIntkey() {
			return this.intkey != null;
		}

		public KeyBuild setLongkey(com.LongKey a) {
			this.longkey = a;
			return this;
		}

		public com.LongKey getLongkey() {
			return this.longkey;
		}

		public KeyBuild clearLongkey() {
			this.longkey = null;
			return this;
		}

		public boolean hasLongkey() {
			return this.longkey != null;
		}

		public KeyBuild setBytekey(com.ByteKey a) {
			this.bytekey = a;
			return this;
		}

		public com.ByteKey getBytekey() {
			return this.bytekey;
		}

		public KeyBuild clearBytekey() {
			this.bytekey = null;
			return this;
		}

		public boolean hasBytekey() {
			return this.bytekey != null;
		}

		public Key build() {
			Key value_1 = new Key();
			if (this.intkey != null) {
				value_1.set_intkey(this.intkey);
			}
			if (this.longkey != null) {
				value_1.set_longkey(this.longkey);
			}
			if (this.bytekey != null) {
				value_1.set_bytekey(this.bytekey);
			}
			return value_1;
		}
		public KeyBuild clear() {
			this.intkey = null;
			this.longkey = null;
			this.bytekey = null;
			return this;
		}

		private KeyBuild() {
		}
	}

}
