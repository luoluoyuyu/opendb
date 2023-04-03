package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class IntValue {

	private int IntValue_size = 0;

	private java.lang.Integer value;
	public final static int value_Num = 1;
	public final static int value_Tag = 8;// the value is num<<<3|wireType
	public final static int value_TagEncodeSize = 1;

	private void encode_value(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, value_Tag);
		Serializer.encodeVarInt32(buf, this.value);
	}

	private static void decode_value(ByteBuf buf, IntValue a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.value = value_1;
	}

	private void set_value(java.lang.Integer value_1) {
		IntValue_size += value_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		IntValue_size += size_1;
		this.value = value_1;
	}

	public java.lang.Integer getValue() {
		return this.value;
	}

	private boolean hasValue() {
		return this.value != null;
	}

	public static IntValue decode(ByteBuf buf) {
		IntValue value_1 = new IntValue();
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
		value_1.IntValue_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasValue()) {
			this.encode_value(buf);
		}

	}
	public static IntValue decode(ByteBuf buf, int length_1) {
		IntValue value_1 = new IntValue();
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
		value_1.IntValue_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.IntValue_size;
	}

	public static IntValueBuild newBuilder() {
		return new IntValueBuild();
	}

	private void verify() {
		if (this.value == null) {
			throw new RuntimeException("required value");
		}
	}

	public static class IntValueBuild {
		private java.lang.Integer value;

		public IntValueBuild setValue(java.lang.Integer a) {
			this.value = a;
			return this;
		}

		public java.lang.Integer getValue() {
			return this.value;
		}

		public IntValueBuild clearValue() {
			this.value = null;
			return this;
		}

		public boolean hasValue() {
			return this.value != null;
		}

		public IntValue build() {
			IntValue value_1 = new IntValue();
			if (this.value == null) {
				throw new RuntimeException(" value is required");
			}
			value_1.set_value(this.value);
			return value_1;
		}
		public IntValueBuild clear() {
			this.value = null;
			return this;
		}

		private IntValueBuild() {
		}
	}

}
