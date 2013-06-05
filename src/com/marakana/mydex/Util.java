package com.marakana.mydex;

public final class Util {
	private Util() {

	}

	public static <T> T notNull(T o, String what) {
		if (o == null) {
			throw new NullPointerException(what + " must not be null");
		}
		return o;
	}

	public static <T extends Comparable<T>> int compare(T o1, T o2) {
		return o1 == null ? (o2 == null ? 0 : -1) : o2 == null ? 1 : o1
				.compareTo(o2);
	}
}
