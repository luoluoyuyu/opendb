package com;
public enum ValueType {

	intValue(1),

	longValue(2),

	bytesValue(3),

	;
	public static ValueType get(int tag) {
		if (tag == 1) {
			return ValueType.intValue;
		}
		if (tag == 2) {
			return ValueType.longValue;
		}
		if (tag == 3) {
			return ValueType.bytesValue;
		}
		return null;
	}
	int num;

	ValueType(int num) {
		this.num = num;
	}

	int getNum() {
		return this.num;
	}
}
