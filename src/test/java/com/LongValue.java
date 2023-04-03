package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class LongValue {

	private int LongValue_size = 0;

	private java.lang.Long value;
	public final static int value_Num = 1;
	public final static int value_Tag = 8;// the value is num<<<3|wireType
	public final static int value_TagEncodeSize = 1;

	private void encode_value(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, value_Tag);
		Serializer.encodeVarInt64(buf, this.value);
	}

	private static void decode_value(ByteBuf buf, LongValue a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.value = value_1;
	}

	private void set_value(java.lang.Long value_1) {
		LongValue_size += value_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		LongValue_size += size_1;
		this.value = value_1;
	}

	public java.lang.Long getValue() {
		return this.value;
	}

	private boolean hasValue() {
		return this.value != null;
	}

	public static LongValue decode(ByteBuf buf) {
		LongValue value_1 = new LongValue();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case value_Tag :
					decode_value(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.LongValue_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasValue()) {
			this.encode_value(buf);
		}

	}
	public static LongValue decode(ByteBuf buf, int length_1) {
		LongValue value_1 = new LongValue();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case value_Tag :
					decode_value(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.LongValue_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.LongValue_size;
	}

	public static LongValueBuild newBuilder() {
		return new LongValueBuild();
	}

	private void verify() {
		if (this.value == null) {
			throw new RuntimeException("required value");
		}
	}

	public static class LongValueBuild {
		private java.lang.Long value;

		public LongValueBuild setValue(java.lang.Long a) {
			this.value = a;
			return this;
		}

		public java.lang.Long getValue() {
			return this.value;
		}

		public LongValueBuild clearValue() {
			this.value = null;
			return this;
		}

		public boolean hasValue() {
			return this.value != null;
		}

		public LongValue build() {
			LongValue value_1 = new LongValue();
			if (this.value == null) {
				throw new RuntimeException(" value is required");
			}
			value_1.set_value(this.value);
			return value_1;
		}
		public LongValueBuild clear() {
			this.value = null;
			return this;
		}

		private LongValueBuild() {
		}
	}

}
