package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class KeyValue {

	private int KeyValue_size = 0;

	private byte[] key;
	public final static int key_Num = 1;
	public final static int key_Tag = 10;// the value is num<<<3|wireType
	public final static int key_TagEncodeSize = 1;

	private void encode_key(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, key_Tag);
		Serializer.encodeByteString(buf, this.key);
	}

	private static void decode_key(ByteBuf buf, KeyValue a_1) {
		byte[] value_1 = null;
		value_1 = Serializer.decodeByteString(buf, Serializer.decodeVarInt32(buf));
		a_1.key = value_1;
	}

	private void set_key(byte[] value_1) {
		KeyValue_size += key_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.length);
		size_1 += value_1.length;
		KeyValue_size += size_1;
		this.key = value_1;
	}

	public byte[] getKey() {
		return this.key;
	}

	private boolean hasKey() {
		return this.key != null;
	}

	private java.lang.Integer intkey;
	public final static int intkey_Num = 2;
	public final static int intkey_Tag = 16;// the value is num<<<3|wireType
	public final static int intkey_TagEncodeSize = 1;

	private void encode_intkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, intkey_Tag);
		Serializer.encodeVarInt32(buf, this.intkey);
	}

	private static void decode_intkey(ByteBuf buf, KeyValue a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.intkey = value_1;
	}

	private void set_intkey(java.lang.Integer value_1) {
		KeyValue_size += intkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		KeyValue_size += size_1;
		this.intkey = value_1;
	}

	public java.lang.Integer getIntkey() {
		return this.intkey;
	}

	private boolean hasIntkey() {
		return this.intkey != null;
	}

	private java.lang.Long longkey;
	public final static int longkey_Num = 3;
	public final static int longkey_Tag = 24;// the value is num<<<3|wireType
	public final static int longkey_TagEncodeSize = 1;

	private void encode_longkey(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, longkey_Tag);
		Serializer.encodeVarInt64(buf, this.longkey);
	}

	private static void decode_longkey(ByteBuf buf, KeyValue a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.longkey = value_1;
	}

	private void set_longkey(java.lang.Long value_1) {
		KeyValue_size += longkey_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		KeyValue_size += size_1;
		this.longkey = value_1;
	}

	public java.lang.Long getLongkey() {
		return this.longkey;
	}

	private boolean hasLongkey() {
		return this.longkey != null;
	}

	private byte[] value;
	public final static int value_Num = 4;
	public final static int value_Tag = 34;// the value is num<<<3|wireType
	public final static int value_TagEncodeSize = 1;

	private void encode_value(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, value_Tag);
		Serializer.encodeByteString(buf, this.value);
	}

	private static void decode_value(ByteBuf buf, KeyValue a_1) {
		byte[] value_1 = null;
		value_1 = Serializer.decodeByteString(buf, Serializer.decodeVarInt32(buf));
		a_1.value = value_1;
	}

	private void set_value(byte[] value_1) {
		KeyValue_size += value_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.length);
		size_1 += value_1.length;
		KeyValue_size += size_1;
		this.value = value_1;
	}

	public byte[] getValue() {
		return this.value;
	}

	private boolean hasValue() {
		return this.value != null;
	}

	private java.lang.Integer intvalue;
	public final static int intvalue_Num = 5;
	public final static int intvalue_Tag = 40;// the value is num<<<3|wireType
	public final static int intvalue_TagEncodeSize = 1;

	private void encode_intvalue(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, intvalue_Tag);
		Serializer.encodeVarInt32(buf, this.intvalue);
	}

	private static void decode_intvalue(ByteBuf buf, KeyValue a_1) {
		java.lang.Integer value_1 = null;
		value_1 = Serializer.decodeVarInt32(buf);
		a_1.intvalue = value_1;
	}

	private void set_intvalue(java.lang.Integer value_1) {
		KeyValue_size += intvalue_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1);
		KeyValue_size += size_1;
		this.intvalue = value_1;
	}

	public java.lang.Integer getIntvalue() {
		return this.intvalue;
	}

	private boolean hasIntvalue() {
		return this.intvalue != null;
	}

	private java.lang.Long longvalue;
	public final static int longvalue_Num = 6;
	public final static int longvalue_Tag = 48;// the value is num<<<3|wireType
	public final static int longvalue_TagEncodeSize = 1;

	private void encode_longvalue(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, longvalue_Tag);
		Serializer.encodeVarInt64(buf, this.longvalue);
	}

	private static void decode_longvalue(ByteBuf buf, KeyValue a_1) {
		java.lang.Long value_1 = null;
		value_1 = Serializer.decodeVarInt64(buf);
		a_1.longvalue = value_1;
	}

	private void set_longvalue(java.lang.Long value_1) {
		KeyValue_size += longvalue_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt64Size(value_1);
		KeyValue_size += size_1;
		this.longvalue = value_1;
	}

	public java.lang.Long getLongvalue() {
		return this.longvalue;
	}

	private boolean hasLongvalue() {
		return this.longvalue != null;
	}

	private com.PrepareId prepareid;
	public final static int prepareid_Num = 7;
	public final static int prepareid_Tag = 58;// the value is num<<<3|wireType
	public final static int prepareid_TagEncodeSize = 1;

	private void encode_prepareid(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, prepareid_Tag);
		Serializer.encodeVarInt32(buf, this.prepareid.getByteSize());
		this.prepareid.encode(buf);
	}

	private static void decode_prepareid(ByteBuf buf, KeyValue a_1) {
		com.PrepareId value_1 = null;
		value_1 = com.PrepareId.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.prepareid = value_1;
	}

	private void set_prepareid(com.PrepareId value_1) {
		KeyValue_size += prepareid_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		KeyValue_size += size_1;
		this.prepareid = value_1;
	}

	public com.PrepareId getPrepareid() {
		return this.prepareid;
	}

	private boolean hasPrepareid() {
		return this.prepareid != null;
	}

	private com.SequenceNumber seq;
	public final static int seq_Num = 8;
	public final static int seq_Tag = 66;// the value is num<<<3|wireType
	public final static int seq_TagEncodeSize = 1;

	private void encode_seq(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, seq_Tag);
		Serializer.encodeVarInt32(buf, this.seq.getByteSize());
		this.seq.encode(buf);
	}

	private static void decode_seq(ByteBuf buf, KeyValue a_1) {
		com.SequenceNumber value_1 = null;
		value_1 = com.SequenceNumber.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.seq = value_1;
	}

	private void set_seq(com.SequenceNumber value_1) {
		KeyValue_size += seq_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		KeyValue_size += size_1;
		this.seq = value_1;
	}

	public com.SequenceNumber getSeq() {
		return this.seq;
	}

	private boolean hasSeq() {
		return this.seq != null;
	}

	private com.CommitId commitid;
	public final static int commitid_Num = 9;
	public final static int commitid_Tag = 74;// the value is num<<<3|wireType
	public final static int commitid_TagEncodeSize = 1;

	private void encode_commitid(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, commitid_Tag);
		Serializer.encodeVarInt32(buf, this.commitid.getByteSize());
		this.commitid.encode(buf);
	}

	private static void decode_commitid(ByteBuf buf, KeyValue a_1) {
		com.CommitId value_1 = null;
		value_1 = com.CommitId.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.commitid = value_1;
	}

	private void set_commitid(com.CommitId value_1) {
		KeyValue_size += commitid_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		KeyValue_size += size_1;
		this.commitid = value_1;
	}

	public com.CommitId getCommitid() {
		return this.commitid;
	}

	private boolean hasCommitid() {
		return this.commitid != null;
	}

	private com.KeyValue.Type type;
	public final static int type_Num = 10;
	public final static int type_Tag = 80;// the value is num<<<3|wireType
	public final static int type_TagEncodeSize = 1;

	private void encode_type(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, type_Tag);
		Serializer.encodeVarInt32(buf, this.type.getNum());
	}

	private static void decode_type(ByteBuf buf, KeyValue a_1) {
		com.KeyValue.Type value_1 = null;
		value_1 = com.KeyValue.Type.get(Serializer.decodeVarInt32(buf));
		a_1.type = value_1;
	}

	private void set_type(com.KeyValue.Type value_1) {
		KeyValue_size += type_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getNum());
		KeyValue_size += size_1;
		this.type = value_1;
	}

	public com.KeyValue.Type getType() {
		return this.type;
	}

	private boolean hasType() {
		return this.type != null;
	}

	public static KeyValue decode(ByteBuf buf) {
		KeyValue value_1 = new KeyValue();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case key_Tag :
					decode_key(buf, value_1);
					break;
				case intkey_Tag :
					decode_intkey(buf, value_1);
					break;
				case longkey_Tag :
					decode_longkey(buf, value_1);
					break;
				case value_Tag :
					decode_value(buf, value_1);
					break;
				case intvalue_Tag :
					decode_intvalue(buf, value_1);
					break;
				case longvalue_Tag :
					decode_longvalue(buf, value_1);
					break;
				case prepareid_Tag :
					decode_prepareid(buf, value_1);
					break;
				case seq_Tag :
					decode_seq(buf, value_1);
					break;
				case commitid_Tag :
					decode_commitid(buf, value_1);
					break;
				case type_Tag :
					decode_type(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.KeyValue_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasKey()) {
			this.encode_key(buf);
		}

		if (hasIntkey()) {
			this.encode_intkey(buf);
		}

		if (hasLongkey()) {
			this.encode_longkey(buf);
		}

		if (hasValue()) {
			this.encode_value(buf);
		}

		if (hasIntvalue()) {
			this.encode_intvalue(buf);
		}

		if (hasLongvalue()) {
			this.encode_longvalue(buf);
		}

		if (hasPrepareid()) {
			this.encode_prepareid(buf);
		}

		if (hasSeq()) {
			this.encode_seq(buf);
		}

		if (hasCommitid()) {
			this.encode_commitid(buf);
		}

		if (hasType()) {
			this.encode_type(buf);
		}

	}
	public static KeyValue decode(ByteBuf buf, int length_1) {
		KeyValue value_1 = new KeyValue();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case key_Tag :
					decode_key(buf, value_1);
					break;
				case intkey_Tag :
					decode_intkey(buf, value_1);
					break;
				case longkey_Tag :
					decode_longkey(buf, value_1);
					break;
				case value_Tag :
					decode_value(buf, value_1);
					break;
				case intvalue_Tag :
					decode_intvalue(buf, value_1);
					break;
				case longvalue_Tag :
					decode_longvalue(buf, value_1);
					break;
				case prepareid_Tag :
					decode_prepareid(buf, value_1);
					break;
				case seq_Tag :
					decode_seq(buf, value_1);
					break;
				case commitid_Tag :
					decode_commitid(buf, value_1);
					break;
				case type_Tag :
					decode_type(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.KeyValue_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.KeyValue_size;
	}

	public static KeyValueBuild newBuilder() {
		return new KeyValueBuild();
	}

	private void verify() {
		if (this.seq == null) {
			throw new RuntimeException("required seq");
		}
		if (this.type == null) {
			throw new RuntimeException("required type");
		}
	}

	public static class KeyValueBuild {
		private byte[] key;
		private java.lang.Integer intkey;
		private java.lang.Long longkey;
		private byte[] value;
		private java.lang.Integer intvalue;
		private java.lang.Long longvalue;
		private com.PrepareId prepareid;
		private com.SequenceNumber seq;
		private com.CommitId commitid;
		private com.KeyValue.Type type;

		public KeyValueBuild setKey(byte[] a) {
			this.key = a;
			return this;
		}

		public byte[] getKey() {
			return this.key;
		}

		public KeyValueBuild clearKey() {
			this.key = null;
			return this;
		}

		public boolean hasKey() {
			return this.key != null;
		}

		public KeyValueBuild setIntkey(java.lang.Integer a) {
			this.intkey = a;
			return this;
		}

		public java.lang.Integer getIntkey() {
			return this.intkey;
		}

		public KeyValueBuild clearIntkey() {
			this.intkey = null;
			return this;
		}

		public boolean hasIntkey() {
			return this.intkey != null;
		}

		public KeyValueBuild setLongkey(java.lang.Long a) {
			this.longkey = a;
			return this;
		}

		public java.lang.Long getLongkey() {
			return this.longkey;
		}

		public KeyValueBuild clearLongkey() {
			this.longkey = null;
			return this;
		}

		public boolean hasLongkey() {
			return this.longkey != null;
		}

		public KeyValueBuild setValue(byte[] a) {
			this.value = a;
			return this;
		}

		public byte[] getValue() {
			return this.value;
		}

		public KeyValueBuild clearValue() {
			this.value = null;
			return this;
		}

		public boolean hasValue() {
			return this.value != null;
		}

		public KeyValueBuild setIntvalue(java.lang.Integer a) {
			this.intvalue = a;
			return this;
		}

		public java.lang.Integer getIntvalue() {
			return this.intvalue;
		}

		public KeyValueBuild clearIntvalue() {
			this.intvalue = null;
			return this;
		}

		public boolean hasIntvalue() {
			return this.intvalue != null;
		}

		public KeyValueBuild setLongvalue(java.lang.Long a) {
			this.longvalue = a;
			return this;
		}

		public java.lang.Long getLongvalue() {
			return this.longvalue;
		}

		public KeyValueBuild clearLongvalue() {
			this.longvalue = null;
			return this;
		}

		public boolean hasLongvalue() {
			return this.longvalue != null;
		}

		public KeyValueBuild setPrepareid(com.PrepareId a) {
			this.prepareid = a;
			return this;
		}

		public com.PrepareId getPrepareid() {
			return this.prepareid;
		}

		public KeyValueBuild clearPrepareid() {
			this.prepareid = null;
			return this;
		}

		public boolean hasPrepareid() {
			return this.prepareid != null;
		}

		public KeyValueBuild setSeq(com.SequenceNumber a) {
			this.seq = a;
			return this;
		}

		public com.SequenceNumber getSeq() {
			return this.seq;
		}

		public KeyValueBuild clearSeq() {
			this.seq = null;
			return this;
		}

		public boolean hasSeq() {
			return this.seq != null;
		}

		public KeyValueBuild setCommitid(com.CommitId a) {
			this.commitid = a;
			return this;
		}

		public com.CommitId getCommitid() {
			return this.commitid;
		}

		public KeyValueBuild clearCommitid() {
			this.commitid = null;
			return this;
		}

		public boolean hasCommitid() {
			return this.commitid != null;
		}

		public KeyValueBuild setType(com.KeyValue.Type a) {
			this.type = a;
			return this;
		}

		public com.KeyValue.Type getType() {
			return this.type;
		}

		public KeyValueBuild clearType() {
			this.type = null;
			return this;
		}

		public boolean hasType() {
			return this.type != null;
		}

		public KeyValue build() {
			KeyValue value_1 = new KeyValue();
			if (this.key != null) {
				value_1.set_key(this.key);
			}
			if (this.intkey != null) {
				value_1.set_intkey(this.intkey);
			}
			if (this.longkey != null) {
				value_1.set_longkey(this.longkey);
			}
			if (this.value != null) {
				value_1.set_value(this.value);
			}
			if (this.intvalue != null) {
				value_1.set_intvalue(this.intvalue);
			}
			if (this.longvalue != null) {
				value_1.set_longvalue(this.longvalue);
			}
			if (this.prepareid != null) {
				value_1.set_prepareid(this.prepareid);
			}
			if (this.seq == null) {
				throw new RuntimeException(" seq is required");
			}
			value_1.set_seq(this.seq);
			if (this.commitid != null) {
				value_1.set_commitid(this.commitid);
			}
			if (this.type == null) {
				throw new RuntimeException(" type is required");
			}
			value_1.set_type(this.type);
			return value_1;
		}
		public KeyValueBuild clear() {
			this.key = null;
			this.intkey = null;
			this.longkey = null;
			this.value = null;
			this.intvalue = null;
			this.longvalue = null;
			this.prepareid = null;
			this.seq = null;
			this.commitid = null;
			this.type = null;
			return this;
		}

		private KeyValueBuild() {
		}
	}

	public static enum Type {

		delete(1),

		update(2),

		insert(3),

		;
		public static Type get(int tag) {
			if (tag == 1) {
				return Type.delete;
			}
			if (tag == 2) {
				return Type.update;
			}
			if (tag == 3) {
				return Type.insert;
			}
			return null;
		}
		int num;

		Type(int num) {
			this.num = num;
		}

		int getNum() {
			return this.num;
		}
	}

}
