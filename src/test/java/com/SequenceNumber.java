package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class SequenceNumber {

	private int SequenceNumber_size = 0;

	private java.lang.Long time;
	public final static int time_Num = 1;
	public final static int time_Tag = 8;// the value is num<<<3|wireType
	public final static int time_TagEncodeSize = 1;

	private void encode_time(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, time_Tag);
		Serializer.encodeVarInt64(buf, this.time);
	}

	private static void decode_time(ByteBuf buf, SequenceNumber a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.time = value_1;
	}

	private void set_time(java.lang.Long value_1) {
		SequenceNumber_size += time_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		SequenceNumber_size += size_1;
		this.time = value_1;
	}

	public java.lang.Long getTime() {
		return this.time;
	}

	private boolean hasTime() {
		return this.time != null;
	}

	private java.lang.Integer id;
	public final static int id_Num = 2;
	public final static int id_Tag = 16;// the value is num<<<3|wireType
	public final static int id_TagEncodeSize = 1;

	private void encode_id(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, id_Tag);
		Serializer.encodeVarInt32(buf, this.id);
	}

	private static void decode_id(ByteBuf buf, SequenceNumber a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.id = value_1;
	}

	private void set_id(java.lang.Integer value_1) {
		SequenceNumber_size += id_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		SequenceNumber_size += size_1;
		this.id = value_1;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	private boolean hasId() {
		return this.id != null;
	}

	public static SequenceNumber decode(ByteBuf buf) {
		SequenceNumber value_1 = new SequenceNumber();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case time_Tag :
					decode_time(buf, value_1);
					break;
				case id_Tag :
					decode_id(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.SequenceNumber_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasTime()) {
			this.encode_time(buf);
		}

		if (hasId()) {
			this.encode_id(buf);
		}

	}
	public static SequenceNumber decode(ByteBuf buf, int length_1) {
		SequenceNumber value_1 = new SequenceNumber();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case time_Tag :
					decode_time(buf, value_1);
					break;
				case id_Tag :
					decode_id(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.SequenceNumber_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.SequenceNumber_size;
	}

	public static SequenceNumberBuild newBuilder() {
		return new SequenceNumberBuild();
	}

	private void verify() {
		if (this.time == null) {
			throw new RuntimeException("required time");
		}
		if (this.id == null) {
			throw new RuntimeException("required id");
		}
	}

	public static class SequenceNumberBuild {
		private java.lang.Long time;
		private java.lang.Integer id;

		public SequenceNumberBuild setTime(java.lang.Long a) {
			this.time = a;
			return this;
		}

		public java.lang.Long getTime() {
			return this.time;
		}

		public SequenceNumberBuild clearTime() {
			this.time = null;
			return this;
		}

		public boolean hasTime() {
			return this.time != null;
		}

		public SequenceNumberBuild setId(java.lang.Integer a) {
			this.id = a;
			return this;
		}

		public java.lang.Integer getId() {
			return this.id;
		}

		public SequenceNumberBuild clearId() {
			this.id = null;
			return this;
		}

		public boolean hasId() {
			return this.id != null;
		}

		public SequenceNumber build() {
			SequenceNumber value_1 = new SequenceNumber();
			if (this.time == null) {
				throw new RuntimeException(" time is required");
			}
			value_1.set_time(this.time);
			if (this.id == null) {
				throw new RuntimeException(" id is required");
			}
			value_1.set_id(this.id);
			return value_1;
		}
		public SequenceNumberBuild clear() {
			this.time = null;
			this.id = null;
			return this;
		}

		private SequenceNumberBuild() {
		}
	}

}
