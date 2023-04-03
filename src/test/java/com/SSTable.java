package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class SSTable {

	private int SSTable_size = 0;

	private java.lang.String filename;
	public final static int filename_Num = 1;
	public final static int filename_Tag = 10;// the value is num<<<3|wireType
	public final static int filename_TagEncodeSize = 1;

	private void encode_filename(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, filename_Tag);
		Serializer.encodeString(buf, this.filename);
	}

	private static void decode_filename(ByteBuf buf, SSTable a_1) {
		java.lang.String value_1 = null;
		value_1 = Serializer.decodeString(buf, Serializer.decodeVarInt32(buf));
		a_1.filename = value_1;
	}

	private void set_filename(java.lang.String value_1) {
		SSTable_size += filename_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(ByteBufUtil.utf8Bytes(value_1));
		size_1 += ByteBufUtil.utf8Bytes(value_1);// value length
		SSTable_size += size_1;
		this.filename = value_1;
	}

	public java.lang.String getFilename() {
		return this.filename;
	}

	private boolean hasFilename() {
		return this.filename != null;
	}

	private java.lang.Integer size;
	public final static int size_Num = 2;
	public final static int size_Tag = 16;// the value is num<<<3|wireType
	public final static int size_TagEncodeSize = 1;

	private void encode_size(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, size_Tag);
		Serializer.encodeVarInt32(buf, this.size);
	}

	private static void decode_size(ByteBuf buf, SSTable a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.size = value_1;
	}

	private void set_size(java.lang.Integer value_1) {
		SSTable_size += size_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		SSTable_size += size_1;
		this.size = value_1;
	}

	public java.lang.Integer getSize() {
		return this.size;
	}

	private boolean hasSize() {
		return this.size != null;
	}

	private com.IntKey minkey;
	public final static int minkey_Num = 3;
	public final static int minkey_Tag = 26;// the value is num<<<3|wireType
	public final static int minkey_TagEncodeSize = 1;

	private void encode_minkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, minkey_Tag);
		Serializer.encodeVarInt32(buf, this.minkey.getByteSize());
		this.minkey.encode(buf);
	}

	private static void decode_minkey(ByteBuf buf, SSTable a_1) {
		com.IntKey value_1 = null;
		value_1 = com.IntKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.minkey = value_1;
	}

	private void set_minkey(com.IntKey value_1) {
		SSTable_size += minkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		SSTable_size += size_1;
		this.minkey = value_1;
	}

	public com.IntKey getMinkey() {
		return this.minkey;
	}

	private boolean hasMinkey() {
		return this.minkey != null;
	}

	private com.LongKey longminkey;
	public final static int longminkey_Num = 4;
	public final static int longminkey_Tag = 34;// the value is num<<<3|wireType
	public final static int longminkey_TagEncodeSize = 1;

	private void encode_longminkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, longminkey_Tag);
		Serializer.encodeVarInt32(buf, this.longminkey.getByteSize());
		this.longminkey.encode(buf);
	}

	private static void decode_longminkey(ByteBuf buf, SSTable a_1) {
		com.LongKey value_1 = null;
		value_1 = com.LongKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.longminkey = value_1;
	}

	private void set_longminkey(com.LongKey value_1) {
		SSTable_size += longminkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		SSTable_size += size_1;
		this.longminkey = value_1;
	}

	public com.LongKey getLongminkey() {
		return this.longminkey;
	}

	private boolean hasLongminkey() {
		return this.longminkey != null;
	}

	private com.ByteKey bytesminkey;
	public final static int bytesminkey_Num = 5;
	public final static int bytesminkey_Tag = 42;// the value is num<<<3|wireType
	public final static int bytesminkey_TagEncodeSize = 1;

	private void encode_bytesminkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, bytesminkey_Tag);
		Serializer.encodeVarInt32(buf, this.bytesminkey.getByteSize());
		this.bytesminkey.encode(buf);
	}

	private static void decode_bytesminkey(ByteBuf buf, SSTable a_1) {
		com.ByteKey value_1 = null;
		value_1 = com.ByteKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.bytesminkey = value_1;
	}

	private void set_bytesminkey(com.ByteKey value_1) {
		SSTable_size += bytesminkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		SSTable_size += size_1;
		this.bytesminkey = value_1;
	}

	public com.ByteKey getBytesminkey() {
		return this.bytesminkey;
	}

	private boolean hasBytesminkey() {
		return this.bytesminkey != null;
	}

	private com.IntKey maxkey;
	public final static int maxkey_Num = 6;
	public final static int maxkey_Tag = 50;// the value is num<<<3|wireType
	public final static int maxkey_TagEncodeSize = 1;

	private void encode_maxkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, maxkey_Tag);
		Serializer.encodeVarInt32(buf, this.maxkey.getByteSize());
		this.maxkey.encode(buf);
	}

	private static void decode_maxkey(ByteBuf buf, SSTable a_1) {
		com.IntKey value_1 = null;
		value_1 = com.IntKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.maxkey = value_1;
	}

	private void set_maxkey(com.IntKey value_1) {
		SSTable_size += maxkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		SSTable_size += size_1;
		this.maxkey = value_1;
	}

	public com.IntKey getMaxkey() {
		return this.maxkey;
	}

	private boolean hasMaxkey() {
		return this.maxkey != null;
	}

	private com.LongKey longmaxkey;
	public final static int longmaxkey_Num = 7;
	public final static int longmaxkey_Tag = 58;// the value is num<<<3|wireType
	public final static int longmaxkey_TagEncodeSize = 1;

	private void encode_longmaxkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, longmaxkey_Tag);
		Serializer.encodeVarInt32(buf, this.longmaxkey.getByteSize());
		this.longmaxkey.encode(buf);
	}

	private static void decode_longmaxkey(ByteBuf buf, SSTable a_1) {
		com.LongKey value_1 = null;
		value_1 = com.LongKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.longmaxkey = value_1;
	}

	private void set_longmaxkey(com.LongKey value_1) {
		SSTable_size += longmaxkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		SSTable_size += size_1;
		this.longmaxkey = value_1;
	}

	public com.LongKey getLongmaxkey() {
		return this.longmaxkey;
	}

	private boolean hasLongmaxkey() {
		return this.longmaxkey != null;
	}

	private com.ByteKey bytesmaxkey;
	public final static int bytesmaxkey_Num = 8;
	public final static int bytesmaxkey_Tag = 66;// the value is num<<<3|wireType
	public final static int bytesmaxkey_TagEncodeSize = 1;

	private void encode_bytesmaxkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, bytesmaxkey_Tag);
		Serializer.encodeVarInt32(buf, this.bytesmaxkey.getByteSize());
		this.bytesmaxkey.encode(buf);
	}

	private static void decode_bytesmaxkey(ByteBuf buf, SSTable a_1) {
		com.ByteKey value_1 = null;
		value_1 = com.ByteKey.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.bytesmaxkey = value_1;
	}

	private void set_bytesmaxkey(com.ByteKey value_1) {
		SSTable_size += bytesmaxkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		SSTable_size += size_1;
		this.bytesmaxkey = value_1;
	}

	public com.ByteKey getBytesmaxkey() {
		return this.bytesmaxkey;
	}

	private boolean hasBytesmaxkey() {
		return this.bytesmaxkey != null;
	}

	public static SSTable decode(ByteBuf buf) {
		SSTable value_1 = new SSTable();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case filename_Tag :
					decode_filename(buf, value_1);
					break;
				case size_Tag :
					decode_size(buf, value_1);
					break;
				case minkey_Tag :
					decode_minkey(buf, value_1);
					break;
				case longminkey_Tag :
					decode_longminkey(buf, value_1);
					break;
				case bytesminkey_Tag :
					decode_bytesminkey(buf, value_1);
					break;
				case maxkey_Tag :
					decode_maxkey(buf, value_1);
					break;
				case longmaxkey_Tag :
					decode_longmaxkey(buf, value_1);
					break;
				case bytesmaxkey_Tag :
					decode_bytesmaxkey(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.SSTable_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasFilename()) {
			this.encode_filename(buf);
		}

		if (hasSize()) {
			this.encode_size(buf);
		}

		if (hasMinkey()) {
			this.encode_minkey(buf);
		}

		if (hasLongminkey()) {
			this.encode_longminkey(buf);
		}

		if (hasBytesminkey()) {
			this.encode_bytesminkey(buf);
		}

		if (hasMaxkey()) {
			this.encode_maxkey(buf);
		}

		if (hasLongmaxkey()) {
			this.encode_longmaxkey(buf);
		}

		if (hasBytesmaxkey()) {
			this.encode_bytesmaxkey(buf);
		}

	}
	public static SSTable decode(ByteBuf buf, int length_1) {
		SSTable value_1 = new SSTable();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case filename_Tag :
					decode_filename(buf, value_1);
					break;
				case size_Tag :
					decode_size(buf, value_1);
					break;
				case minkey_Tag :
					decode_minkey(buf, value_1);
					break;
				case longminkey_Tag :
					decode_longminkey(buf, value_1);
					break;
				case bytesminkey_Tag :
					decode_bytesminkey(buf, value_1);
					break;
				case maxkey_Tag :
					decode_maxkey(buf, value_1);
					break;
				case longmaxkey_Tag :
					decode_longmaxkey(buf, value_1);
					break;
				case bytesmaxkey_Tag :
					decode_bytesmaxkey(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.SSTable_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.SSTable_size;
	}

	public static SSTableBuild newBuilder() {
		return new SSTableBuild();
	}

	private void verify() {
		if (this.filename == null) {
			throw new RuntimeException("required filename");
		}
		if (this.size == null) {
			throw new RuntimeException("required size");
		}
		if (this.minkey == null) {
			throw new RuntimeException("required minkey");
		}
		if (this.longminkey == null) {
			throw new RuntimeException("required longminkey");
		}
		if (this.bytesminkey == null) {
			throw new RuntimeException("required bytesminkey");
		}
		if (this.maxkey == null) {
			throw new RuntimeException("required maxkey");
		}
		if (this.longmaxkey == null) {
			throw new RuntimeException("required longmaxkey");
		}
		if (this.bytesmaxkey == null) {
			throw new RuntimeException("required bytesmaxkey");
		}
	}

	public static class SSTableBuild {
		private java.lang.String filename;
		private java.lang.Integer size;
		private com.IntKey minkey;
		private com.LongKey longminkey;
		private com.ByteKey bytesminkey;
		private com.IntKey maxkey;
		private com.LongKey longmaxkey;
		private com.ByteKey bytesmaxkey;

		public SSTableBuild setFilename(java.lang.String a) {
			this.filename = a;
			return this;
		}

		public java.lang.String getFilename() {
			return this.filename;
		}

		public SSTableBuild clearFilename() {
			this.filename = null;
			return this;
		}

		public boolean hasFilename() {
			return this.filename != null;
		}

		public SSTableBuild setSize(java.lang.Integer a) {
			this.size = a;
			return this;
		}

		public java.lang.Integer getSize() {
			return this.size;
		}

		public SSTableBuild clearSize() {
			this.size = null;
			return this;
		}

		public boolean hasSize() {
			return this.size != null;
		}

		public SSTableBuild setMinkey(com.IntKey a) {
			this.minkey = a;
			return this;
		}

		public com.IntKey getMinkey() {
			return this.minkey;
		}

		public SSTableBuild clearMinkey() {
			this.minkey = null;
			return this;
		}

		public boolean hasMinkey() {
			return this.minkey != null;
		}

		public SSTableBuild setLongminkey(com.LongKey a) {
			this.longminkey = a;
			return this;
		}

		public com.LongKey getLongminkey() {
			return this.longminkey;
		}

		public SSTableBuild clearLongminkey() {
			this.longminkey = null;
			return this;
		}

		public boolean hasLongminkey() {
			return this.longminkey != null;
		}

		public SSTableBuild setBytesminkey(com.ByteKey a) {
			this.bytesminkey = a;
			return this;
		}

		public com.ByteKey getBytesminkey() {
			return this.bytesminkey;
		}

		public SSTableBuild clearBytesminkey() {
			this.bytesminkey = null;
			return this;
		}

		public boolean hasBytesminkey() {
			return this.bytesminkey != null;
		}

		public SSTableBuild setMaxkey(com.IntKey a) {
			this.maxkey = a;
			return this;
		}

		public com.IntKey getMaxkey() {
			return this.maxkey;
		}

		public SSTableBuild clearMaxkey() {
			this.maxkey = null;
			return this;
		}

		public boolean hasMaxkey() {
			return this.maxkey != null;
		}

		public SSTableBuild setLongmaxkey(com.LongKey a) {
			this.longmaxkey = a;
			return this;
		}

		public com.LongKey getLongmaxkey() {
			return this.longmaxkey;
		}

		public SSTableBuild clearLongmaxkey() {
			this.longmaxkey = null;
			return this;
		}

		public boolean hasLongmaxkey() {
			return this.longmaxkey != null;
		}

		public SSTableBuild setBytesmaxkey(com.ByteKey a) {
			this.bytesmaxkey = a;
			return this;
		}

		public com.ByteKey getBytesmaxkey() {
			return this.bytesmaxkey;
		}

		public SSTableBuild clearBytesmaxkey() {
			this.bytesmaxkey = null;
			return this;
		}

		public boolean hasBytesmaxkey() {
			return this.bytesmaxkey != null;
		}

		public SSTable build() {
			SSTable value_1 = new SSTable();
			if (this.filename == null) {
				throw new RuntimeException(" filename is required");
			}
			value_1.set_filename(this.filename);
			if (this.size == null) {
				throw new RuntimeException(" size is required");
			}
			value_1.set_size(this.size);
			if (this.minkey == null) {
				throw new RuntimeException(" minkey is required");
			}
			value_1.set_minkey(this.minkey);
			if (this.longminkey == null) {
				throw new RuntimeException(" longminkey is required");
			}
			value_1.set_longminkey(this.longminkey);
			if (this.bytesminkey == null) {
				throw new RuntimeException(" bytesminkey is required");
			}
			value_1.set_bytesminkey(this.bytesminkey);
			if (this.maxkey == null) {
				throw new RuntimeException(" maxkey is required");
			}
			value_1.set_maxkey(this.maxkey);
			if (this.longmaxkey == null) {
				throw new RuntimeException(" longmaxkey is required");
			}
			value_1.set_longmaxkey(this.longmaxkey);
			if (this.bytesmaxkey == null) {
				throw new RuntimeException(" bytesmaxkey is required");
			}
			value_1.set_bytesmaxkey(this.bytesmaxkey);
			return value_1;
		}
		public SSTableBuild clear() {
			this.filename = null;
			this.size = null;
			this.minkey = null;
			this.longminkey = null;
			this.bytesminkey = null;
			this.maxkey = null;
			this.longmaxkey = null;
			this.bytesmaxkey = null;
			return this;
		}

		private SSTableBuild() {
		}
	}

}
