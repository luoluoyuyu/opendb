package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class TransactionKeyValue {

	private int TransactionKeyValue_size = 0;

	private com.ColumnFamilyId columnfamilyid;
	public final static int columnfamilyid_Num = 1;
	public final static int columnfamilyid_Tag = 10;// the value is num<<<3|wireType
	public final static int columnfamilyid_TagEncodeSize = 1;

	private void encode_columnfamilyid(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, columnfamilyid_Tag);
		Serializer.encodeVarInt32(buf, this.columnfamilyid.getByteSize());
		this.columnfamilyid.encode(buf);
	}

	private static void decode_columnfamilyid(ByteBuf buf, TransactionKeyValue a_1) {
		com.ColumnFamilyId value_1 = null;
		value_1 = com.ColumnFamilyId.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.columnfamilyid = value_1;
	}

	private void set_columnfamilyid(com.ColumnFamilyId value_1) {
		TransactionKeyValue_size += columnfamilyid_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		TransactionKeyValue_size += size_1;
		this.columnfamilyid = value_1;
	}

	public com.ColumnFamilyId getColumnfamilyid() {
		return this.columnfamilyid;
	}

	private boolean hasColumnfamilyid() {
		return this.columnfamilyid != null;
	}

	private com.KeyValue keyvalue;
	public final static int keyvalue_Num = 2;
	public final static int keyvalue_Tag = 18;// the value is num<<<3|wireType
	public final static int keyvalue_TagEncodeSize = 1;

	private void encode_keyvalue(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, keyvalue_Tag);
		Serializer.encodeVarInt32(buf, this.keyvalue.getByteSize());
		this.keyvalue.encode(buf);
	}

	private static void decode_keyvalue(ByteBuf buf, TransactionKeyValue a_1) {
		com.KeyValue value_1 = null;
		value_1 = com.KeyValue.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.keyvalue = value_1;
	}

	private void set_keyvalue(com.KeyValue value_1) {
		TransactionKeyValue_size += keyvalue_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		TransactionKeyValue_size += size_1;
		this.keyvalue = value_1;
	}

	public com.KeyValue getKeyvalue() {
		return this.keyvalue;
	}

	private boolean hasKeyvalue() {
		return this.keyvalue != null;
	}

	public static TransactionKeyValue decode(ByteBuf buf) {
		TransactionKeyValue value_1 = new TransactionKeyValue();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case columnfamilyid_Tag :
					decode_columnfamilyid(buf, value_1);
					break;
				case keyvalue_Tag :
					decode_keyvalue(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.TransactionKeyValue_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasColumnfamilyid()) {
			this.encode_columnfamilyid(buf);
		}

		if (hasKeyvalue()) {
			this.encode_keyvalue(buf);
		}

	}
	public static TransactionKeyValue decode(ByteBuf buf, int length_1) {
		TransactionKeyValue value_1 = new TransactionKeyValue();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case columnfamilyid_Tag :
					decode_columnfamilyid(buf, value_1);
					break;
				case keyvalue_Tag :
					decode_keyvalue(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.TransactionKeyValue_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.TransactionKeyValue_size;
	}

	public static TransactionKeyValueBuild newBuilder() {
		return new TransactionKeyValueBuild();
	}

	private void verify() {
		if (this.columnfamilyid == null) {
			throw new RuntimeException("required columnfamilyid");
		}
		if (this.keyvalue == null) {
			throw new RuntimeException("required keyvalue");
		}
	}

	public static class TransactionKeyValueBuild {
		private com.ColumnFamilyId columnfamilyid;
		private com.KeyValue keyvalue;

		public TransactionKeyValueBuild setColumnfamilyid(com.ColumnFamilyId a) {
			this.columnfamilyid = a;
			return this;
		}

		public com.ColumnFamilyId getColumnfamilyid() {
			return this.columnfamilyid;
		}

		public TransactionKeyValueBuild clearColumnfamilyid() {
			this.columnfamilyid = null;
			return this;
		}

		public boolean hasColumnfamilyid() {
			return this.columnfamilyid != null;
		}

		public TransactionKeyValueBuild setKeyvalue(com.KeyValue a) {
			this.keyvalue = a;
			return this;
		}

		public com.KeyValue getKeyvalue() {
			return this.keyvalue;
		}

		public TransactionKeyValueBuild clearKeyvalue() {
			this.keyvalue = null;
			return this;
		}

		public boolean hasKeyvalue() {
			return this.keyvalue != null;
		}

		public TransactionKeyValue build() {
			TransactionKeyValue value_1 = new TransactionKeyValue();
			if (this.columnfamilyid == null) {
				throw new RuntimeException(" columnfamilyid is required");
			}
			value_1.set_columnfamilyid(this.columnfamilyid);
			if (this.keyvalue == null) {
				throw new RuntimeException(" keyvalue is required");
			}
			value_1.set_keyvalue(this.keyvalue);
			return value_1;
		}
		public TransactionKeyValueBuild clear() {
			this.columnfamilyid = null;
			this.keyvalue = null;
			return this;
		}

		private TransactionKeyValueBuild() {
		}
	}

}
