package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class ColumnFamilyHandle {

	private int ColumnFamilyHandle_size = 0;

	private com.ColumnFamilyId id;
	public final static int id_Num = 1;
	public final static int id_Tag = 10;// the value is num<<<3|wireType
	public final static int id_TagEncodeSize = 1;

	private void encode_id(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, id_Tag);
		Serializer.encodeVarInt32(buf, this.id.getByteSize());
		this.id.encode(buf);
	}

	private static void decode_id(ByteBuf buf, ColumnFamilyHandle a_1) {
		com.ColumnFamilyId value_1 = null;
		value_1 = com.ColumnFamilyId.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.id = value_1;
	}

	private void set_id(com.ColumnFamilyId value_1) {
		ColumnFamilyHandle_size += id_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		ColumnFamilyHandle_size += size_1;
		this.id = value_1;
	}

	public com.ColumnFamilyId getId() {
		return this.id;
	}

	private boolean hasId() {
		return this.id != null;
	}

	private java.lang.String name;
	public final static int name_Num = 2;
	public final static int name_Tag = 18;// the value is num<<<3|wireType
	public final static int name_TagEncodeSize = 1;

	private void encode_name(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, name_Tag);
		Serializer.encodeString(buf, this.name);
	}

	private static void decode_name(ByteBuf buf, ColumnFamilyHandle a_1) {
		java.lang.String value_1 = null;
		value_1 = Serializer.decodeString(buf, Serializer.decodeVarInt32(buf));
		a_1.name = value_1;
	}

	private void set_name(java.lang.String value_1) {
		ColumnFamilyHandle_size += name_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(ByteBufUtil.utf8Bytes(value_1));
		size_1 += ByteBufUtil.utf8Bytes(value_1);// value length
		ColumnFamilyHandle_size += size_1;
		this.name = value_1;
	}

	public java.lang.String getName() {
		return this.name;
	}

	private boolean hasName() {
		return this.name != null;
	}

	private com.FileList filelist;
	public final static int filelist_Num = 3;
	public final static int filelist_Tag = 26;// the value is num<<<3|wireType
	public final static int filelist_TagEncodeSize = 1;

	private void encode_filelist(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, filelist_Tag);
		Serializer.encodeVarInt32(buf, this.filelist.getByteSize());
		this.filelist.encode(buf);
	}

	private static void decode_filelist(ByteBuf buf, ColumnFamilyHandle a_1) {
		com.FileList value_1 = null;
		value_1 = com.FileList.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.filelist = value_1;
	}

	private void set_filelist(com.FileList value_1) {
		ColumnFamilyHandle_size += filelist_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		ColumnFamilyHandle_size += size_1;
		this.filelist = value_1;
	}

	public com.FileList getFilelist() {
		return this.filelist;
	}

	private boolean hasFilelist() {
		return this.filelist != null;
	}

	private java.util.List<java.lang.String> walfile;
	public final static int walfile_Num = 4;
	public final static int walfile_Tag = 34;// the value is num<<3|wireType
	public final static int walfile_TagEncodeSize = 1;

	private void set_walfile(java.util.List<java.lang.String> list_1) {
		this.walfile = new java.util.ArrayList<>(list_1.size());
		this.ColumnFamilyHandle_size += walfile_TagEncodeSize * list_1.size();// add tag length
		for (java.lang.String value_1 : list_1) {
			this.walfile.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(ByteBufUtil.utf8Bytes(value_1));
			length_1 += ByteBufUtil.utf8Bytes(value_1);// value length
			this.ColumnFamilyHandle_size += length_1;
		}
	}
	public java.lang.String getWalfile(int index) {
		if (this.walfile == null) {
			return null;
		}

		return this.walfile.get(index);
	}

	public int getWalfileSize() {
		if (this.walfile == null) {
			return 0;
		}

		return this.walfile.size();
	}

	private static void decode_walfile(ByteBuf buf, ColumnFamilyHandle a_1) {
		java.lang.String value_1 = null;
		value_1 = Serializer.decodeString(buf, Serializer.decodeVarInt32(buf));
		a_1.add_walfile(value_1);
	}

	private void encode_walfile(ByteBuf buf) {
		for (java.lang.String value_1 : walfile) {
			Serializer.encodeVarInt32(buf, walfile_Tag);
			Serializer.encodeString(buf, value_1);
		}
	}

	private void add_walfile(java.lang.String value) {
		if (this.walfile == null) {
			this.walfile = new java.util.ArrayList<>();
		}

		this.walfile.add(value);
	}

	public boolean hasWalfile() {
		if (this.walfile == null) {
			return false;
		}
		return this.walfile.size() != 0;
	}

	private com.KeyType keytype;
	public final static int keytype_Num = 5;
	public final static int keytype_Tag = 40;// the value is num<<<3|wireType
	public final static int keytype_TagEncodeSize = 1;

	private void encode_keytype(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, keytype_Tag);
		Serializer.encodeVarInt32(buf, this.keytype.getNum());
	}

	private static void decode_keytype(ByteBuf buf, ColumnFamilyHandle a_1) {
		com.KeyType value_1 = null;
		value_1 = com.KeyType.get(Serializer.decodeVarInt32(buf));
		a_1.keytype = value_1;
	}

	private void set_keytype(com.KeyType value_1) {
		ColumnFamilyHandle_size += keytype_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getNum());
		ColumnFamilyHandle_size += size_1;
		this.keytype = value_1;
	}

	public com.KeyType getKeytype() {
		return this.keytype;
	}

	private boolean hasKeytype() {
		return this.keytype != null;
	}

	private com.ValueType valuetype;
	public final static int valuetype_Num = 6;
	public final static int valuetype_Tag = 48;// the value is num<<<3|wireType
	public final static int valuetype_TagEncodeSize = 1;

	private void encode_valuetype(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, valuetype_Tag);
		Serializer.encodeVarInt32(buf, this.valuetype.getNum());
	}

	private static void decode_valuetype(ByteBuf buf, ColumnFamilyHandle a_1) {
		com.ValueType value_1 = null;
		value_1 = com.ValueType.get(Serializer.decodeVarInt32(buf));
		a_1.valuetype = value_1;
	}

	private void set_valuetype(com.ValueType value_1) {
		ColumnFamilyHandle_size += valuetype_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getNum());
		ColumnFamilyHandle_size += size_1;
		this.valuetype = value_1;
	}

	public com.ValueType getValuetype() {
		return this.valuetype;
	}

	private boolean hasValuetype() {
		return this.valuetype != null;
	}

	public static ColumnFamilyHandle decode(ByteBuf buf) {
		ColumnFamilyHandle value_1 = new ColumnFamilyHandle();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case id_Tag :
					decode_id(buf, value_1);
					break;
				case name_Tag :
					decode_name(buf, value_1);
					break;
				case filelist_Tag :
					decode_filelist(buf, value_1);
					break;
				case walfile_Tag :
					decode_walfile(buf, value_1);
					break;
				case keytype_Tag :
					decode_keytype(buf, value_1);
					break;
				case valuetype_Tag :
					decode_valuetype(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.ColumnFamilyHandle_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasId()) {
			this.encode_id(buf);
		}

		if (hasName()) {
			this.encode_name(buf);
		}

		if (hasFilelist()) {
			this.encode_filelist(buf);
		}

		if (hasWalfile()) {
			this.encode_walfile(buf);
		}

		if (hasKeytype()) {
			this.encode_keytype(buf);
		}

		if (hasValuetype()) {
			this.encode_valuetype(buf);
		}

	}
	public static ColumnFamilyHandle decode(ByteBuf buf, int length_1) {
		ColumnFamilyHandle value_1 = new ColumnFamilyHandle();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case id_Tag :
					decode_id(buf, value_1);
					break;
				case name_Tag :
					decode_name(buf, value_1);
					break;
				case filelist_Tag :
					decode_filelist(buf, value_1);
					break;
				case walfile_Tag :
					decode_walfile(buf, value_1);
					break;
				case keytype_Tag :
					decode_keytype(buf, value_1);
					break;
				case valuetype_Tag :
					decode_valuetype(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.ColumnFamilyHandle_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.ColumnFamilyHandle_size;
	}

	public static ColumnFamilyHandleBuild newBuilder() {
		return new ColumnFamilyHandleBuild();
	}

	private void verify() {
		if (this.id == null) {
			throw new RuntimeException("required id");
		}
		if (this.name == null) {
			throw new RuntimeException("required name");
		}
		if (this.filelist == null) {
			throw new RuntimeException("required filelist");
		}
		if (this.keytype == null) {
			throw new RuntimeException("required keytype");
		}
		if (this.valuetype == null) {
			throw new RuntimeException("required valuetype");
		}
	}

	public static class ColumnFamilyHandleBuild {
		private com.ColumnFamilyId id;
		private java.lang.String name;
		private com.FileList filelist;
		private java.util.List<java.lang.String> walfile;
		private com.KeyType keytype;
		private com.ValueType valuetype;

		public ColumnFamilyHandleBuild setId(com.ColumnFamilyId a) {
			this.id = a;
			return this;
		}

		public com.ColumnFamilyId getId() {
			return this.id;
		}

		public ColumnFamilyHandleBuild clearId() {
			this.id = null;
			return this;
		}

		public boolean hasId() {
			return this.id != null;
		}

		public ColumnFamilyHandleBuild setName(java.lang.String a) {
			this.name = a;
			return this;
		}

		public java.lang.String getName() {
			return this.name;
		}

		public ColumnFamilyHandleBuild clearName() {
			this.name = null;
			return this;
		}

		public boolean hasName() {
			return this.name != null;
		}

		public ColumnFamilyHandleBuild setFilelist(com.FileList a) {
			this.filelist = a;
			return this;
		}

		public com.FileList getFilelist() {
			return this.filelist;
		}

		public ColumnFamilyHandleBuild clearFilelist() {
			this.filelist = null;
			return this;
		}

		public boolean hasFilelist() {
			return this.filelist != null;
		}

		public ColumnFamilyHandleBuild addWalfile(java.lang.String a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.walfile == null) {
				this.walfile = new java.util.ArrayList<>();
				this.walfile.add(a);
			} else {
				this.walfile.add(a);
			}
			return this;
		}

		public java.lang.String getWalfile(int index) {
			if (this.walfile == null || index >= this.walfile.size()) {
				throw new RuntimeException("walfile is null or index bigger than walfile size");
			}

			return this.walfile.get(index);
		}

		public ColumnFamilyHandleBuild removeWalfile(int index) {
			if (this.walfile == null || index >= this.walfile.size()) {
				throw new RuntimeException("walfile is null or index bigger than walfile size");
			}

			this.walfile.remove(index);
			return this;

		}

		public int sizeWalfile() {
			if (this.walfile == null) {
				throw new RuntimeException("walfile is null");
			}

			return this.walfile.size();
		}

		public ColumnFamilyHandleBuild clearWalfile() {
			this.walfile = null;
			return this;
		}

		public boolean hasWalfile() {
			if (this.walfile == null) {
				return false;
			}
			return this.walfile.size() != 0;
		}

		public ColumnFamilyHandleBuild setKeytype(com.KeyType a) {
			this.keytype = a;
			return this;
		}

		public com.KeyType getKeytype() {
			return this.keytype;
		}

		public ColumnFamilyHandleBuild clearKeytype() {
			this.keytype = null;
			return this;
		}

		public boolean hasKeytype() {
			return this.keytype != null;
		}

		public ColumnFamilyHandleBuild setValuetype(com.ValueType a) {
			this.valuetype = a;
			return this;
		}

		public com.ValueType getValuetype() {
			return this.valuetype;
		}

		public ColumnFamilyHandleBuild clearValuetype() {
			this.valuetype = null;
			return this;
		}

		public boolean hasValuetype() {
			return this.valuetype != null;
		}

		public ColumnFamilyHandle build() {
			ColumnFamilyHandle value_1 = new ColumnFamilyHandle();
			if (this.id == null) {
				throw new RuntimeException(" id is required");
			}
			value_1.set_id(this.id);
			if (this.name == null) {
				throw new RuntimeException(" name is required");
			}
			value_1.set_name(this.name);
			if (this.filelist == null) {
				throw new RuntimeException(" filelist is required");
			}
			value_1.set_filelist(this.filelist);
			if (this.hasWalfile()) {
				value_1.set_walfile(this.walfile);
			}
			if (this.keytype == null) {
				throw new RuntimeException(" keytype is required");
			}
			value_1.set_keytype(this.keytype);
			if (this.valuetype == null) {
				throw new RuntimeException(" valuetype is required");
			}
			value_1.set_valuetype(this.valuetype);
			return value_1;
		}
		public ColumnFamilyHandleBuild clear() {
			this.id = null;
			this.name = null;
			this.filelist = null;
			this.walfile = null;
			this.keytype = null;
			this.valuetype = null;
			return this;
		}

		private ColumnFamilyHandleBuild() {
		}
	}

}
