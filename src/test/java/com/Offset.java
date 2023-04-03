package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class Offset {

	private int Offset_size = 0;

	private byte[] key;
	public final static int key_Num = 1;
	public final static int key_Tag = 10;// the value is num<<<3|wireType
	public final static int key_TagEncodeSize = 1;

	private void encode_key(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, key_Tag);
		Serializer.encodeByteString(buf, this.key);
	}

	private static void decode_key(ByteBuf buf, Offset a_1) {
		byte[] value_1 = null;
		value_1 = Serializer.decodeByteString(buf, Serializer.decodeVarInt32(buf));
		a_1.key = value_1;
	}

	private void set_key(byte[] value_1) {
		Offset_size += key_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.length);
		size_1 += value_1.length;
		Offset_size += size_1;
		this.key = value_1;
	}

	public byte[] getKey() {
		return this.key;
	}

	private boolean hasKey() {
		return this.key != null;
	}

	private java.lang.Integer intkey;
	public final static int intkey_Num = 2;
	public final static int intkey_Tag = 16;// the value is num<<<3|wireType
	public final static int intkey_TagEncodeSize = 1;

	private void encode_intkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, intkey_Tag);
		Serializer.encodeVarInt32(buf, this.intkey);
	}

	private static void decode_intkey(ByteBuf buf, Offset a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.intkey = value_1;
	}

	private void set_intkey(java.lang.Integer value_1) {
		Offset_size += intkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		Offset_size += size_1;
		this.intkey = value_1;
	}

	public java.lang.Integer getIntkey() {
		return this.intkey;
	}

	private boolean hasIntkey() {
		return this.intkey != null;
	}

	private java.lang.Long longkey;
	public final static int longkey_Num = 3;
	public final static int longkey_Tag = 24;// the value is num<<<3|wireType
	public final static int longkey_TagEncodeSize = 1;

	private void encode_longkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, longkey_Tag);
		Serializer.encodeVarInt64(buf, this.longkey);
	}

	private static void decode_longkey(ByteBuf buf, Offset a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.longkey = value_1;
	}

	private void set_longkey(java.lang.Long value_1) {
		Offset_size += longkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		Offset_size += size_1;
		this.longkey = value_1;
	}

	public java.lang.Long getLongkey() {
		return this.longkey;
	}

	private boolean hasLongkey() {
		return this.longkey != null;
	}

	private java.lang.Integer offset;
	public final static int offset_Num = 4;
	public final static int offset_Tag = 32;// the value is num<<<3|wireType
	public final static int offset_TagEncodeSize = 1;

	private void encode_offset(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, offset_Tag);
		Serializer.encodeVarInt32(buf, this.offset);
	}

	private static void decode_offset(ByteBuf buf, Offset a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.offset = value_1;
	}

	private void set_offset(java.lang.Integer value_1) {
		Offset_size += offset_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		Offset_size += size_1;
		this.offset = value_1;
	}

	public java.lang.Integer getOffset() {
		return this.offset;
	}

	private boolean hasOffset() {
		return this.offset != null;
	}

	public static Offset decode(ByteBuf buf) {
		Offset value_1 = new Offset();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case key_Tag :
					decode_key(buf, value_1);
					break;
				case intkey_Tag :
					decode_intkey(buf, value_1);
					break;
				case longkey_Tag :
					decode_longkey(buf, value_1);
					break;
				case offset_Tag :
					decode_offset(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.Offset_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasKey()) {
			this.encode_key(buf);
		}

		if (hasIntkey()) {
			this.encode_intkey(buf);
		}

		if (hasLongkey()) {
			this.encode_longkey(buf);
		}

		if (hasOffset()) {
			this.encode_offset(buf);
		}

	}
	public static Offset decode(ByteBuf buf, int length_1) {
		Offset value_1 = new Offset();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case key_Tag :
					decode_key(buf, value_1);
					break;
				case intkey_Tag :
					decode_intkey(buf, value_1);
					break;
				case longkey_Tag :
					decode_longkey(buf, value_1);
					break;
				case offset_Tag :
					decode_offset(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.Offset_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.Offset_size;
	}

	public static OffsetBuild newBuilder() {
		return new OffsetBuild();
	}

	private void verify() {
		if (this.offset == null) {
			throw new RuntimeException("required offset");
		}
	}

	public static class OffsetBuild {
		private byte[] key;
		private java.lang.Integer intkey;
		private java.lang.Long longkey;
		private java.lang.Integer offset;

		public OffsetBuild setKey(byte[] a) {
			this.key = a;
			return this;
		}

		public byte[] getKey() {
			return this.key;
		}

		public OffsetBuild clearKey() {
			this.key = null;
			return this;
		}

		public boolean hasKey() {
			return this.key != null;
		}

		public OffsetBuild setIntkey(java.lang.Integer a) {
			this.intkey = a;
			return this;
		}

		public java.lang.Integer getIntkey() {
			return this.intkey;
		}

		public OffsetBuild clearIntkey() {
			this.intkey = null;
			return this;
		}

		public boolean hasIntkey() {
			return this.intkey != null;
		}

		public OffsetBuild setLongkey(java.lang.Long a) {
			this.longkey = a;
			return this;
		}

		public java.lang.Long getLongkey() {
			return this.longkey;
		}

		public OffsetBuild clearLongkey() {
			this.longkey = null;
			return this;
		}

		public boolean hasLongkey() {
			return this.longkey != null;
		}

		public OffsetBuild setOffset(java.lang.Integer a) {
			this.offset = a;
			return this;
		}

		public java.lang.Integer getOffset() {
			return this.offset;
		}

		public OffsetBuild clearOffset() {
			this.offset = null;
			return this;
		}

		public boolean hasOffset() {
			return this.offset != null;
		}

		public Offset build() {
			Offset value_1 = new Offset();
			if (this.key != null) {
				value_1.set_key(this.key);
			}
			if (this.intkey != null) {
				value_1.set_intkey(this.intkey);
			}
			if (this.longkey != null) {
				value_1.set_longkey(this.longkey);
			}
			if (this.offset == null) {
				throw new RuntimeException(" offset is required");
			}
			value_1.set_offset(this.offset);
			return value_1;
		}
		public OffsetBuild clear() {
			this.key = null;
			this.intkey = null;
			this.longkey = null;
			this.offset = null;
			return this;
		}

		private OffsetBuild() {
		}
	}

}
