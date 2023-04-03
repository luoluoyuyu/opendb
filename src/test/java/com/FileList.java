package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class FileList {

	private int FileList_size = 0;

	private java.util.List<com.SSTable> level0;
	public final static int level0_Num = 1;
	public final static int level0_Tag = 10;// the value is num<<3|wireType
	public final static int level0_TagEncodeSize = 1;

	private void set_level0(java.util.List<com.SSTable> list_1) {
		this.level0 = new java.util.ArrayList<>(list_1.size());
		this.FileList_size += level0_TagEncodeSize * list_1.size();// add tag length
		for (com.SSTable value_1 : list_1) {
			this.level0.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
			length_1 += value_1.getByteSize();
			this.FileList_size += length_1;
		}
	}
	public com.SSTable getLevel0(int index) {
		if (this.level0 == null) {
			return null;
		}

		return this.level0.get(index);
	}

	public int getLevel0Size() {
		if (this.level0 == null) {
			return 0;
		}

		return this.level0.size();
	}

	private static void decode_level0(ByteBuf buf, FileList a_1) {
		com.SSTable value_1 = null;
		value_1 = com.SSTable.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.add_level0(value_1);
	}

	private void encode_level0(ByteBuf buf) {
		for (com.SSTable value_1 : level0) {
			Serializer.encodeVarInt32(buf, level0_Tag);
			Serializer.encodeVarInt32(buf, value_1.getByteSize());
			value_1.encode(buf);
		}
	}

	private void add_level0(com.SSTable value) {
		if (this.level0 == null) {
			this.level0 = new java.util.ArrayList<>();
		}

		this.level0.add(value);
	}

	public boolean hasLevel0() {
		if (this.level0 == null) {
			return false;
		}
		return this.level0.size() != 0;
	}

	private java.util.List<com.SSTable> level1;
	public final static int level1_Num = 2;
	public final static int level1_Tag = 18;// the value is num<<3|wireType
	public final static int level1_TagEncodeSize = 1;

	private void set_level1(java.util.List<com.SSTable> list_1) {
		this.level1 = new java.util.ArrayList<>(list_1.size());
		this.FileList_size += level1_TagEncodeSize * list_1.size();// add tag length
		for (com.SSTable value_1 : list_1) {
			this.level1.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
			length_1 += value_1.getByteSize();
			this.FileList_size += length_1;
		}
	}
	public com.SSTable getLevel1(int index) {
		if (this.level1 == null) {
			return null;
		}

		return this.level1.get(index);
	}

	public int getLevel1Size() {
		if (this.level1 == null) {
			return 0;
		}

		return this.level1.size();
	}

	private static void decode_level1(ByteBuf buf, FileList a_1) {
		com.SSTable value_1 = null;
		value_1 = com.SSTable.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.add_level1(value_1);
	}

	private void encode_level1(ByteBuf buf) {
		for (com.SSTable value_1 : level1) {
			Serializer.encodeVarInt32(buf, level1_Tag);
			Serializer.encodeVarInt32(buf, value_1.getByteSize());
			value_1.encode(buf);
		}
	}

	private void add_level1(com.SSTable value) {
		if (this.level1 == null) {
			this.level1 = new java.util.ArrayList<>();
		}

		this.level1.add(value);
	}

	public boolean hasLevel1() {
		if (this.level1 == null) {
			return false;
		}
		return this.level1.size() != 0;
	}

	private java.util.List<com.SSTable> level2;
	public final static int level2_Num = 3;
	public final static int level2_Tag = 26;// the value is num<<3|wireType
	public final static int level2_TagEncodeSize = 1;

	private void set_level2(java.util.List<com.SSTable> list_1) {
		this.level2 = new java.util.ArrayList<>(list_1.size());
		this.FileList_size += level2_TagEncodeSize * list_1.size();// add tag length
		for (com.SSTable value_1 : list_1) {
			this.level2.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
			length_1 += value_1.getByteSize();
			this.FileList_size += length_1;
		}
	}
	public com.SSTable getLevel2(int index) {
		if (this.level2 == null) {
			return null;
		}

		return this.level2.get(index);
	}

	public int getLevel2Size() {
		if (this.level2 == null) {
			return 0;
		}

		return this.level2.size();
	}

	private static void decode_level2(ByteBuf buf, FileList a_1) {
		com.SSTable value_1 = null;
		value_1 = com.SSTable.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.add_level2(value_1);
	}

	private void encode_level2(ByteBuf buf) {
		for (com.SSTable value_1 : level2) {
			Serializer.encodeVarInt32(buf, level2_Tag);
			Serializer.encodeVarInt32(buf, value_1.getByteSize());
			value_1.encode(buf);
		}
	}

	private void add_level2(com.SSTable value) {
		if (this.level2 == null) {
			this.level2 = new java.util.ArrayList<>();
		}

		this.level2.add(value);
	}

	public boolean hasLevel2() {
		if (this.level2 == null) {
			return false;
		}
		return this.level2.size() != 0;
	}

	private java.util.List<com.SSTable> level3;
	public final static int level3_Num = 4;
	public final static int level3_Tag = 34;// the value is num<<3|wireType
	public final static int level3_TagEncodeSize = 1;

	private void set_level3(java.util.List<com.SSTable> list_1) {
		this.level3 = new java.util.ArrayList<>(list_1.size());
		this.FileList_size += level3_TagEncodeSize * list_1.size();// add tag length
		for (com.SSTable value_1 : list_1) {
			this.level3.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
			length_1 += value_1.getByteSize();
			this.FileList_size += length_1;
		}
	}
	public com.SSTable getLevel3(int index) {
		if (this.level3 == null) {
			return null;
		}

		return this.level3.get(index);
	}

	public int getLevel3Size() {
		if (this.level3 == null) {
			return 0;
		}

		return this.level3.size();
	}

	private static void decode_level3(ByteBuf buf, FileList a_1) {
		com.SSTable value_1 = null;
		value_1 = com.SSTable.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.add_level3(value_1);
	}

	private void encode_level3(ByteBuf buf) {
		for (com.SSTable value_1 : level3) {
			Serializer.encodeVarInt32(buf, level3_Tag);
			Serializer.encodeVarInt32(buf, value_1.getByteSize());
			value_1.encode(buf);
		}
	}

	private void add_level3(com.SSTable value) {
		if (this.level3 == null) {
			this.level3 = new java.util.ArrayList<>();
		}

		this.level3.add(value);
	}

	public boolean hasLevel3() {
		if (this.level3 == null) {
			return false;
		}
		return this.level3.size() != 0;
	}

	private java.util.List<com.SSTable> level4;
	public final static int level4_Num = 5;
	public final static int level4_Tag = 42;// the value is num<<3|wireType
	public final static int level4_TagEncodeSize = 1;

	private void set_level4(java.util.List<com.SSTable> list_1) {
		this.level4 = new java.util.ArrayList<>(list_1.size());
		this.FileList_size += level4_TagEncodeSize * list_1.size();// add tag length
		for (com.SSTable value_1 : list_1) {
			this.level4.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
			length_1 += value_1.getByteSize();
			this.FileList_size += length_1;
		}
	}
	public com.SSTable getLevel4(int index) {
		if (this.level4 == null) {
			return null;
		}

		return this.level4.get(index);
	}

	public int getLevel4Size() {
		if (this.level4 == null) {
			return 0;
		}

		return this.level4.size();
	}

	private static void decode_level4(ByteBuf buf, FileList a_1) {
		com.SSTable value_1 = null;
		value_1 = com.SSTable.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.add_level4(value_1);
	}

	private void encode_level4(ByteBuf buf) {
		for (com.SSTable value_1 : level4) {
			Serializer.encodeVarInt32(buf, level4_Tag);
			Serializer.encodeVarInt32(buf, value_1.getByteSize());
			value_1.encode(buf);
		}
	}

	private void add_level4(com.SSTable value) {
		if (this.level4 == null) {
			this.level4 = new java.util.ArrayList<>();
		}

		this.level4.add(value);
	}

	public boolean hasLevel4() {
		if (this.level4 == null) {
			return false;
		}
		return this.level4.size() != 0;
	}

	public static FileList decode(ByteBuf buf) {
		FileList value_1 = new FileList();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case level0_Tag :
					decode_level0(buf, value_1);
					break;
				case level1_Tag :
					decode_level1(buf, value_1);
					break;
				case level2_Tag :
					decode_level2(buf, value_1);
					break;
				case level3_Tag :
					decode_level3(buf, value_1);
					break;
				case level4_Tag :
					decode_level4(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.FileList_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasLevel0()) {
			this.encode_level0(buf);
		}

		if (hasLevel1()) {
			this.encode_level1(buf);
		}

		if (hasLevel2()) {
			this.encode_level2(buf);
		}

		if (hasLevel3()) {
			this.encode_level3(buf);
		}

		if (hasLevel4()) {
			this.encode_level4(buf);
		}

	}
	public static FileList decode(ByteBuf buf, int length_1) {
		FileList value_1 = new FileList();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case level0_Tag :
					decode_level0(buf, value_1);
					break;
				case level1_Tag :
					decode_level1(buf, value_1);
					break;
				case level2_Tag :
					decode_level2(buf, value_1);
					break;
				case level3_Tag :
					decode_level3(buf, value_1);
					break;
				case level4_Tag :
					decode_level4(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.FileList_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.FileList_size;
	}

	public static FileListBuild newBuilder() {
		return new FileListBuild();
	}

	private void verify() {
	}

	public static class FileListBuild {
		private java.util.List<com.SSTable> level0;
		private java.util.List<com.SSTable> level1;
		private java.util.List<com.SSTable> level2;
		private java.util.List<com.SSTable> level3;
		private java.util.List<com.SSTable> level4;

		public FileListBuild addLevel0(com.SSTable a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.level0 == null) {
				this.level0 = new java.util.ArrayList<>();
				this.level0.add(a);
			} else {
				this.level0.add(a);
			}
			return this;
		}

		public com.SSTable getLevel0(int index) {
			if (this.level0 == null || index >= this.level0.size()) {
				throw new RuntimeException("level0 is null or index bigger than level0 size");
			}

			return this.level0.get(index);
		}

		public FileListBuild removeLevel0(int index) {
			if (this.level0 == null || index >= this.level0.size()) {
				throw new RuntimeException("level0 is null or index bigger than level0 size");
			}

			this.level0.remove(index);
			return this;

		}

		public int sizeLevel0() {
			if (this.level0 == null) {
				throw new RuntimeException("level0 is null");
			}

			return this.level0.size();
		}

		public FileListBuild clearLevel0() {
			this.level0 = null;
			return this;
		}

		public boolean hasLevel0() {
			if (this.level0 == null) {
				return false;
			}
			return this.level0.size() != 0;
		}

		public FileListBuild addLevel1(com.SSTable a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.level1 == null) {
				this.level1 = new java.util.ArrayList<>();
				this.level1.add(a);
			} else {
				this.level1.add(a);
			}
			return this;
		}

		public com.SSTable getLevel1(int index) {
			if (this.level1 == null || index >= this.level1.size()) {
				throw new RuntimeException("level1 is null or index bigger than level1 size");
			}

			return this.level1.get(index);
		}

		public FileListBuild removeLevel1(int index) {
			if (this.level1 == null || index >= this.level1.size()) {
				throw new RuntimeException("level1 is null or index bigger than level1 size");
			}

			this.level1.remove(index);
			return this;

		}

		public int sizeLevel1() {
			if (this.level1 == null) {
				throw new RuntimeException("level1 is null");
			}

			return this.level1.size();
		}

		public FileListBuild clearLevel1() {
			this.level1 = null;
			return this;
		}

		public boolean hasLevel1() {
			if (this.level1 == null) {
				return false;
			}
			return this.level1.size() != 0;
		}

		public FileListBuild addLevel2(com.SSTable a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.level2 == null) {
				this.level2 = new java.util.ArrayList<>();
				this.level2.add(a);
			} else {
				this.level2.add(a);
			}
			return this;
		}

		public com.SSTable getLevel2(int index) {
			if (this.level2 == null || index >= this.level2.size()) {
				throw new RuntimeException("level2 is null or index bigger than level2 size");
			}

			return this.level2.get(index);
		}

		public FileListBuild removeLevel2(int index) {
			if (this.level2 == null || index >= this.level2.size()) {
				throw new RuntimeException("level2 is null or index bigger than level2 size");
			}

			this.level2.remove(index);
			return this;

		}

		public int sizeLevel2() {
			if (this.level2 == null) {
				throw new RuntimeException("level2 is null");
			}

			return this.level2.size();
		}

		public FileListBuild clearLevel2() {
			this.level2 = null;
			return this;
		}

		public boolean hasLevel2() {
			if (this.level2 == null) {
				return false;
			}
			return this.level2.size() != 0;
		}

		public FileListBuild addLevel3(com.SSTable a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.level3 == null) {
				this.level3 = new java.util.ArrayList<>();
				this.level3.add(a);
			} else {
				this.level3.add(a);
			}
			return this;
		}

		public com.SSTable getLevel3(int index) {
			if (this.level3 == null || index >= this.level3.size()) {
				throw new RuntimeException("level3 is null or index bigger than level3 size");
			}

			return this.level3.get(index);
		}

		public FileListBuild removeLevel3(int index) {
			if (this.level3 == null || index >= this.level3.size()) {
				throw new RuntimeException("level3 is null or index bigger than level3 size");
			}

			this.level3.remove(index);
			return this;

		}

		public int sizeLevel3() {
			if (this.level3 == null) {
				throw new RuntimeException("level3 is null");
			}

			return this.level3.size();
		}

		public FileListBuild clearLevel3() {
			this.level3 = null;
			return this;
		}

		public boolean hasLevel3() {
			if (this.level3 == null) {
				return false;
			}
			return this.level3.size() != 0;
		}

		public FileListBuild addLevel4(com.SSTable a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.level4 == null) {
				this.level4 = new java.util.ArrayList<>();
				this.level4.add(a);
			} else {
				this.level4.add(a);
			}
			return this;
		}

		public com.SSTable getLevel4(int index) {
			if (this.level4 == null || index >= this.level4.size()) {
				throw new RuntimeException("level4 is null or index bigger than level4 size");
			}

			return this.level4.get(index);
		}

		public FileListBuild removeLevel4(int index) {
			if (this.level4 == null || index >= this.level4.size()) {
				throw new RuntimeException("level4 is null or index bigger than level4 size");
			}

			this.level4.remove(index);
			return this;

		}

		public int sizeLevel4() {
			if (this.level4 == null) {
				throw new RuntimeException("level4 is null");
			}

			return this.level4.size();
		}

		public FileListBuild clearLevel4() {
			this.level4 = null;
			return this;
		}

		public boolean hasLevel4() {
			if (this.level4 == null) {
				return false;
			}
			return this.level4.size() != 0;
		}

		public FileList build() {
			FileList value_1 = new FileList();
			if (this.hasLevel0()) {
				value_1.set_level0(this.level0);
			}
			if (this.hasLevel1()) {
				value_1.set_level1(this.level1);
			}
			if (this.hasLevel2()) {
				value_1.set_level2(this.level2);
			}
			if (this.hasLevel3()) {
				value_1.set_level3(this.level3);
			}
			if (this.hasLevel4()) {
				value_1.set_level4(this.level4);
			}
			return value_1;
		}
		public FileListBuild clear() {
			this.level0 = null;
			this.level1 = null;
			this.level2 = null;
			this.level3 = null;
			this.level4 = null;
			return this;
		}

		private FileListBuild() {
		}
	}

}
