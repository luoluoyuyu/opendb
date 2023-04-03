package com;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public final class log {

	private int log_size = 0;

	private com.KeyValue k;
	public final static int k_Num = 1;
	public final static int k_Tag = 10;// the value is num<<<3|wireType
	public final static int k_TagEncodeSize = 1;

	private void encode_k(ByteBuf buf) {
		Serializer.encodeVarInt32(buf, k_Tag);
		Serializer.encodeVarInt32(buf, this.k.getByteSize());
		this.k.encode(buf);
	}

	private static void decode_k(ByteBuf buf, log a_1) {
		com.KeyValue value_1 = null;
		value_1 = com.KeyValue.decode(buf, Serializer.decodeVarInt32(buf));
		a_1.k = value_1;
	}

	private void set_k(com.KeyValue value_1) {
		log_size += k_TagEncodeSize;
		int size_1 = 0;

		size_1 += Serializer.computeVarInt32Size(value_1.getByteSize());
		size_1 += value_1.getByteSize();
		log_size += size_1;
		this.k = value_1;
	}

	public com.KeyValue getK() {
		return this.k;
	}

	private boolean hasK() {
		return this.k != null;
	}

	public static log decode(ByteBuf buf) {
		log value_1 = new log();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < buf.writerIndex()) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case k_Tag :
					decode_k(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.log_size = buf.readerIndex() - f_Index;
		return value_1;
	}

	public void encode(ByteBuf buf) {
		if (hasK()) {
			this.encode_k(buf);
		}

	}
	public static log decode(ByteBuf buf, int length_1) {
		log value_1 = new log();
		int f_Index = buf.readerIndex();
		while (buf.readerIndex() < f_Index + length_1) {
			int num_1 = Serializer.decodeVarInt32(buf);
			switch (num_1) {
				case k_Tag :
					decode_k(buf, value_1);
					break;
				default :
					Serializer.skipUnknownField(num_1, buf);
			}
		}
		value_1.log_size = length_1;
		value_1.verify();
		return value_1;
	}

	public int getByteSize() {
		return this.log_size;
	}

	public static logBuild newBuilder() {
		return new logBuild();
	}

	private void verify() {
		if (this.k == null) {
			throw new RuntimeException("required k");
		}
	}

	public static class logBuild {
		private com.KeyValue k;

		public logBuild setK(com.KeyValue a) {
			this.k = a;
			return this;
		}

		public com.KeyValue getK() {
			return this.k;
		}

		public logBuild clearK() {
			this.k = null;
			return this;
		}

		public boolean hasK() {
			return this.k != null;
		}

		public log build() {
			log value_1 = new log();
			if (this.k == null) {
				throw new RuntimeException(" k is required");
			}
			value_1.set_k(this.k);
			return value_1;
		}
		public logBuild clear() {
			this.k = null;
			return this;
		}

		private logBuild() {
		}
	}

}
