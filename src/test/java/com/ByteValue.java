package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class ByteValue {

	private int ByteValue_size = 0;

	private byte[] value;
	public final static int value_Num = 1;
	public final static int value_Tag = 10;// the value is num<<<3|wireType
	public final static int value_TagEncodeSize = 1;

	private void encode_value(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, value_Tag);
		Serializer.encodeByteString(buf, this.value);
	}

	private static void decode_value(ByteBuf buf, ByteValue a_1) {
		byte[] value_1 = null;
		value_1 = Serializer.decodeByteString(buf, Serializer.decodeVarInt32(buf));
		a_1.value = value_1;
	}

	private void set_value(byte[] value_1) {
		ByteValue_size += value_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.length);
		size_1 += value_1.length;
		ByteValue_size += size_1;
		this.value = value_1;
	}

	public byte[] getValue() {
		return this.value;
	}

	private boolean hasValue() {
		return this.value != null;
	}

	public static ByteValue decode(ByteBuf buf) {
		ByteValue value_1 = new ByteValue();
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
		value_1.ByteValue_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasValue()) {
			this.encode_value(buf);
		}

	}
	public static ByteValue decode(ByteBuf buf, int length_1) {
		ByteValue value_1 = new ByteValue();
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
		value_1.ByteValue_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.ByteValue_size;
	}

	public static ByteValueBuild newBuilder() {
		return new ByteValueBuild();
	}

	private void verify() {
		if (this.value == null) {
			throw new RuntimeException("required value");
		}
	}

	public static class ByteValueBuild {
		private byte[] value;

		public ByteValueBuild setValue(byte[] a) {
			this.value = a;
			return this;
		}

		public byte[] getValue() {
			return this.value;
		}

		public ByteValueBuild clearValue() {
			this.value = null;
			return this;
		}

		public boolean hasValue() {
			return this.value != null;
		}

		public ByteValue build() {
			ByteValue value_1 = new ByteValue();
			if (this.value == null) {
				throw new RuntimeException(" value is required");
			}
			value_1.set_value(this.value);
			return value_1;
		}
		public ByteValueBuild clear() {
			this.value = null;
			return this;
		}

		private ByteValueBuild() {
		}
	}

}
