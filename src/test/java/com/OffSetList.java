package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class OffSetList {

	private int OffSetList_size = 0;

	private java.util.List<com.Offset> list;
	public final static int list_Num = 1;
	public final static int list_Tag = 10;// the value is num<<3|wireType
	public final static int list_TagEncodeSize = 1;

	private void set_list(java.util.List<com.Offset> list_1) {
		this.list = new java.util.ArrayList<>(list_1.size());
		this.OffSetList_size += list_TagEncodeSize * list_1.size();// add tag length
		for (com.Offset value_1 : list_1) {
			this.list.add(value_1);
			int length_1 = 0;
			length_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
			length_1 += value_1.getByteSize();
			this.OffSetList_size += length_1;
		}
	}
	public com.Offset getList(int index) {
		if (this.list == null) {
			return null;
		}

		return this.list.get(index);
	}

	public int getListSize() {
		if (this.list == null) {
			return 0;
		}

		return this.list.size();
	}

	private static void decode_list(ByteBuf buf, OffSetList a_1) {
		com.Offset value_1 = null;
		value_1 = com.Offset.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.add_list(value_1);
	}

	private void encode_list(ByteBuf buf) {
		for (com.Offset value_1 : list) {
			Serializer.encodeVarInt32(buf, list_Tag);
			Serializer.encodeVarInt32(buf, value_1.getByteSize());
			value_1.encode(buf);
		}
	}

	private void add_list(com.Offset value) {
		if (this.list == null) {
			this.list = new java.util.ArrayList<>();
		}

		this.list.add(value);
	}

	public boolean hasList() {
		if (this.list == null) {
			return false;
		}
		return this.list.size() != 0;
	}

	public static OffSetList decode(ByteBuf buf) {
		OffSetList value_1 = new OffSetList();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case list_Tag :
					decode_list(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.OffSetList_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasList()) {
			this.encode_list(buf);
		}

	}
	public static OffSetList decode(ByteBuf buf, int length_1) {
		OffSetList value_1 = new OffSetList();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case list_Tag :
					decode_list(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.OffSetList_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.OffSetList_size;
	}

	public static OffSetListBuild newBuilder() {
		return new OffSetListBuild();
	}

	private void verify() {
	}

	public static class OffSetListBuild {
		private java.util.List<com.Offset> list;

		public OffSetListBuild addList(com.Offset a) {
			if (a == null) {
				throw new RuntimeException("a is null");
			}
			if (this.list == null) {
				this.list = new java.util.ArrayList<>();
				this.list.add(a);
			} else {
				this.list.add(a);
			}
			return this;
		}

		public com.Offset getList(int index) {
			if (this.list == null || index >= this.list.size()) {
				throw new RuntimeException("list is null or index bigger than list size");
			}

			return this.list.get(index);
		}

		public OffSetListBuild removeList(int index) {
			if (this.list == null || index >= this.list.size()) {
				throw new RuntimeException("list is null or index bigger than list size");
			}

			this.list.remove(index);
			return this;

		}

		public int sizeList() {
			if (this.list == null) {
				throw new RuntimeException("list is null");
			}

			return this.list.size();
		}

		public OffSetListBuild clearList() {
			this.list = null;
			return this;
		}

		public boolean hasList() {
			if (this.list == null) {
				return false;
			}
			return this.list.size() != 0;
		}

		public OffSetList build() {
			OffSetList value_1 = new OffSetList();
			if (this.hasList()) {
				value_1.set_list(this.list);
			}
			return value_1;
		}
		public OffSetListBuild clear() {
			this.list = null;
			return this;
		}

		private OffSetListBuild() {
		}
	}

}
