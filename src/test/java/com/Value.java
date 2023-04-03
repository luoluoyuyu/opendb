package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class Value {

	private int Value_size = 0;

	private com.IntValue intvalue;
	public final static int intvalue_Num = 1;
	public final static int intvalue_Tag = 10;// the value is num<<<3|wireType
	public final static int intvalue_TagEncodeSize = 1;

	private void encode_intvalue(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, intvalue_Tag);
		Serializer.encodeVarInt32(buf, this.intvalue.getByteSize());
		this.intvalue.encode(buf);
	}

	private static void decode_intvalue(ByteBuf buf, Value a_1) {
		com.IntValue value_1 = null;
		value_1 = com.IntValue.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.intvalue = value_1;
	}

	private void set_intvalue(com.IntValue value_1) {
		Value_size += intvalue_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		Value_size += size_1;
		this.intvalue = value_1;
	}

	public com.IntValue getIntvalue() {
		return this.intvalue;
	}

	private boolean hasIntvalue() {
		return this.intvalue != null;
	}

	private com.LongValue longvalue;
	public final static int longvalue_Num = 2;
	public final static int longvalue_Tag = 18;// the value is num<<<3|wireType
	public final static int longvalue_TagEncodeSize = 1;

	private void encode_longvalue(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, longvalue_Tag);
		Serializer.encodeVarInt32(buf, this.longvalue.getByteSize());
		this.longvalue.encode(buf);
	}

	private static void decode_longvalue(ByteBuf buf, Value a_1) {
		com.LongValue value_1 = null;
		value_1 = com.LongValue.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.longvalue = value_1;
	}

	private void set_longvalue(com.LongValue value_1) {
		Value_size += longvalue_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		Value_size += size_1;
		this.longvalue = value_1;
	}

	public com.LongValue getLongvalue() {
		return this.longvalue;
	}

	private boolean hasLongvalue() {
		return this.longvalue != null;
	}

	private com.ByteValue bytevalue;
	public final static int bytevalue_Num = 3;
	public final static int bytevalue_Tag = 26;// the value is num<<<3|wireType
	public final static int bytevalue_TagEncodeSize = 1;

	private void encode_bytevalue(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, bytevalue_Tag);
		Serializer.encodeVarInt32(buf, this.bytevalue.getByteSize());
		this.bytevalue.encode(buf);
	}

	private static void decode_bytevalue(ByteBuf buf, Value a_1) {
		com.ByteValue value_1 = null;
		value_1 = com.ByteValue.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.bytevalue = value_1;
	}

	private void set_bytevalue(com.ByteValue value_1) {
		Value_size += bytevalue_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		Value_size += size_1;
		this.bytevalue = value_1;
	}

	public com.ByteValue getBytevalue() {
		return this.bytevalue;
	}

	private boolean hasBytevalue() {
		return this.bytevalue != null;
	}

	public static Value decode(ByteBuf buf) {
		Value value_1 = new Value();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case intvalue_Tag :
					decode_intvalue(buf, value_1);
					break;
				case longvalue_Tag :
					decode_longvalue(buf, value_1);
					break;
				case bytevalue_Tag :
					decode_bytevalue(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.Value_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasIntvalue()) {
			this.encode_intvalue(buf);
		}

		if (hasLongvalue()) {
			this.encode_longvalue(buf);
		}

		if (hasBytevalue()) {
			this.encode_bytevalue(buf);
		}

	}
	public static Value decode(ByteBuf buf, int length_1) {
		Value value_1 = new Value();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case intvalue_Tag :
					decode_intvalue(buf, value_1);
					break;
				case longvalue_Tag :
					decode_longvalue(buf, value_1);
					break;
				case bytevalue_Tag :
					decode_bytevalue(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.Value_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.Value_size;
	}

	public static ValueBuild newBuilder() {
		return new ValueBuild();
	}

	private void verify() {
	}

	public static class ValueBuild {
		private com.IntValue intvalue;
		private com.LongValue longvalue;
		private com.ByteValue bytevalue;

		public ValueBuild setIntvalue(com.IntValue a) {
			this.intvalue = a;
			return this;
		}

		public com.IntValue getIntvalue() {
			return this.intvalue;
		}

		public ValueBuild clearIntvalue() {
			this.intvalue = null;
			return this;
		}

		public boolean hasIntvalue() {
			return this.intvalue != null;
		}

		public ValueBuild setLongvalue(com.LongValue a) {
			this.longvalue = a;
			return this;
		}

		public com.LongValue getLongvalue() {
			return this.longvalue;
		}

		public ValueBuild clearLongvalue() {
			this.longvalue = null;
			return this;
		}

		public boolean hasLongvalue() {
			return this.longvalue != null;
		}

		public ValueBuild setBytevalue(com.ByteValue a) {
			this.bytevalue = a;
			return this;
		}

		public com.ByteValue getBytevalue() {
			return this.bytevalue;
		}

		public ValueBuild clearBytevalue() {
			this.bytevalue = null;
			return this;
		}

		public boolean hasBytevalue() {
			return this.bytevalue != null;
		}

		public Value build() {
			Value value_1 = new Value();
			if (this.intvalue != null) {
				value_1.set_intvalue(this.intvalue);
			}
			if (this.longvalue != null) {
				value_1.set_longvalue(this.longvalue);
			}
			if (this.bytevalue != null) {
				value_1.set_bytevalue(this.bytevalue);
			}
			return value_1;
		}
		public ValueBuild clear() {
			this.intvalue = null;
			this.longvalue = null;
			this.bytevalue = null;
			return this;
		}

		private ValueBuild() {
		}
	}

}
