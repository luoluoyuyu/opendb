package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class ColumnFamilyId {

	private int ColumnFamilyId_size = 0;

	private java.lang.Long id;
	public final static int id_Num = 1;
	public final static int id_Tag = 8;// the value is num<<<3|wireType
	public final static int id_TagEncodeSize = 1;

	private void encode_id(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, id_Tag);
		Serializer.encodeVarInt64(buf, this.id);
	}

	private static void decode_id(ByteBuf buf, ColumnFamilyId a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.id = value_1;
	}

	private void set_id(java.lang.Long value_1) {
		ColumnFamilyId_size += id_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		ColumnFamilyId_size += size_1;
		this.id = value_1;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	private boolean hasId() {
		return this.id != null;
	}

	public static ColumnFamilyId decode(ByteBuf buf) {
		ColumnFamilyId value_1 = new ColumnFamilyId();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case id_Tag :
					decode_id(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.ColumnFamilyId_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasId()) {
			this.encode_id(buf);
		}

	}
	public static ColumnFamilyId decode(ByteBuf buf, int length_1) {
		ColumnFamilyId value_1 = new ColumnFamilyId();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case id_Tag :
					decode_id(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.ColumnFamilyId_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.ColumnFamilyId_size;
	}

	public static ColumnFamilyIdBuild newBuilder() {
		return new ColumnFamilyIdBuild();
	}

	private void verify() {
		if (this.id == null) {
			throw new RuntimeException("required id");
		}
	}

	public static class ColumnFamilyIdBuild {
		private java.lang.Long id;

		public ColumnFamilyIdBuild setId(java.lang.Long a) {
			this.id = a;
			return this;
		}

		public java.lang.Long getId() {
			return this.id;
		}

		public ColumnFamilyIdBuild clearId() {
			this.id = null;
			return this;
		}

		public boolean hasId() {
			return this.id != null;
		}

		public ColumnFamilyId build() {
			ColumnFamilyId value_1 = new ColumnFamilyId();
			if (this.id == null) {
				throw new RuntimeException(" id is required");
			}
			value_1.set_id(this.id);
			return value_1;
		}
		public ColumnFamilyIdBuild clear() {
			this.id = null;
			return this;
		}

		private ColumnFamilyIdBuild() {
		}
	}

}
