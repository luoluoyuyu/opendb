package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class DB {

	private int DB_size = 0;

	private java.util.List<com.ColumnFamilyHandle> c;
	public final static int c_Num = 1;
	public final static int c_Tag = 10;// the value is num<<3|wireType
	public final static int c_TagEncodeSize = 1;

	private void set_c(java.util.List<com.ColumnFamilyHandle> list_1) {
		this.c = new java.util.ArrayList<>(list_1.size());
		this.DB_size += c_TagEncodeSize * list_1.size();// add tag length
		for (com.ColumnFamilyHandle value_1 : list_1) {
			this.c.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
			length_1 += value_1.getByteSize();
			this.DB_size += length_1;
		}
	}
	public com.ColumnFamilyHandle getC(int index) {
		if (this.c == null) {
			return null;
		}

		return this.c.get(index);
	}

	public int getCSize() {
		if (this.c == null) {
			return 0;
		}

		return this.c.size();
	}

	private static void decode_c(ByteBuf buf, DB a_1) {
		com.ColumnFamilyHandle value_1 = null;
		value_1 = com.ColumnFamilyHandle.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.add_c(value_1);
	}

	private void encode_c(ByteBuf buf) {
		for (com.ColumnFamilyHandle value_1 : c) {
			Serializer.encodeVarInt32(buf, c_Tag);
			Serializer.encodeVarInt32(buf, value_1.getByteSize());
			value_1.encode(buf);
		}
	}

	private void add_c(com.ColumnFamilyHandle value) {
		if (this.c == null) {
			this.c = new java.util.ArrayList<>();
		}

		this.c.add(value);
	}

	public boolean hasC() {
		if (this.c == null) {
			return false;
		}
		return this.c.size() != 0;
	}

	private java.lang.Long columnfamilyhandleid;
	public final static int columnfamilyhandleid_Num = 2;
	public final static int columnfamilyhandleid_Tag = 16;// the value is num<<<3|wireType
	public final static int columnfamilyhandleid_TagEncodeSize = 1;

	private void encode_columnfamilyhandleid(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, columnfamilyhandleid_Tag);
		Serializer.encodeVarInt64(buf, this.columnfamilyhandleid);
	}

	private static void decode_columnfamilyhandleid(ByteBuf buf, DB a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.columnfamilyhandleid = value_1;
	}

	private void set_columnfamilyhandleid(java.lang.Long value_1) {
		DB_size += columnfamilyhandleid_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		DB_size += size_1;
		this.columnfamilyhandleid = value_1;
	}

	public java.lang.Long getColumnfamilyhandleid() {
		return this.columnfamilyhandleid;
	}

	private boolean hasColumnfamilyhandleid() {
		return this.columnfamilyhandleid != null;
	}

	public static DB decode(ByteBuf buf) {
		DB value_1 = new DB();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case c_Tag :
					decode_c(buf, value_1);
					break;
				case columnfamilyhandleid_Tag :
					decode_columnfamilyhandleid(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.DB_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasC()) {
			this.encode_c(buf);
		}

		if (hasColumnfamilyhandleid()) {
			this.encode_columnfamilyhandleid(buf);
		}

	}
	public static DB decode(ByteBuf buf, int length_1) {
		DB value_1 = new DB();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case c_Tag :
					decode_c(buf, value_1);
					break;
				case columnfamilyhandleid_Tag :
					decode_columnfamilyhandleid(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.DB_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.DB_size;
	}

	public static DBBuild newBuilder() {
		return new DBBuild();
	}

	private void verify() {
		if (this.columnfamilyhandleid == null) {
			throw new RuntimeException("required columnfamilyhandleid");
		}
	}

	public static class DBBuild {
		private java.util.List<com.ColumnFamilyHandle> c;
		private java.lang.Long columnfamilyhandleid;

		public DBBuild addC(com.ColumnFamilyHandle a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.c == null) {
				this.c = new java.util.ArrayList<>();
				this.c.add(a);
			} else {
				this.c.add(a);
			}
			return this;
		}

		public com.ColumnFamilyHandle getC(int index) {
			if (this.c == null || index >= this.c.size()) {
				throw new RuntimeException("c is null or index bigger than c size");
			}

			return this.c.get(index);
		}

		public DBBuild removeC(int index) {
			if (this.c == null || index >= this.c.size()) {
				throw new RuntimeException("c is null or index bigger than c size");
			}

			this.c.remove(index);
			return this;

		}

		public int sizeC() {
			if (this.c == null) {
				throw new RuntimeException("c is null");
			}

			return this.c.size();
		}

		public DBBuild clearC() {
			this.c = null;
			return this;
		}

		public boolean hasC() {
			if (this.c == null) {
				return false;
			}
			return this.c.size() != 0;
		}

		public DBBuild setColumnfamilyhandleid(java.lang.Long a) {
			this.columnfamilyhandleid = a;
			return this;
		}

		public java.lang.Long getColumnfamilyhandleid() {
			return this.columnfamilyhandleid;
		}

		public DBBuild clearColumnfamilyhandleid() {
			this.columnfamilyhandleid = null;
			return this;
		}

		public boolean hasColumnfamilyhandleid() {
			return this.columnfamilyhandleid != null;
		}

		public DB build() {
			DB value_1 = new DB();
			if (this.hasC()) {
				value_1.set_c(this.c);
			}
			if (this.columnfamilyhandleid == null) {
				throw new RuntimeException(" columnfamilyhandleid is required");
			}
			value_1.set_columnfamilyhandleid(this.columnfamilyhandleid);
			return value_1;
		}
		public DBBuild clear() {
			this.c = null;
			this.columnfamilyhandleid = null;
			return this;
		}

		private DBBuild() {
		}
	}

}
