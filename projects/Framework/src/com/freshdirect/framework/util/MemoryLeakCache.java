package com.freshdirect.framework.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MemoryLeakCache {
	private static MemoryLeakCache INSTANCE;

	private static final int KILO = 1024;
	private static final int MEGA = 1024 * KILO;
	private static final int GIGA = 1024 * MEGA;

	private final Object sync = new Object();

	public synchronized static MemoryLeakCache getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MemoryLeakCache();
		return INSTANCE;
	}

	private List<byte[]> leaks;

	private MemoryLeakCache() {
		leaks = new ArrayList<byte[]>();
	}

	public List<Integer> getLeakSizes() {
		synchronized (sync) {
			List<Integer> sizes = new ArrayList<Integer>();
			for (byte[] leak : leaks)
				sizes.add(leak.length);
			return sizes;
		}
	}

	public boolean addLeak(int size) {
		synchronized (sync) {
			return leaks.add(new byte[size]);
		}
	}

	public void removeLeak(int index) {
		synchronized (sync) {
			leaks.remove(index);
		}
	}

	public static int parseLeakSize(String str) {
		int multiplier = 1;
		if (str.endsWith("G")) {
			str = str.substring(0, str.length() - 1);
			multiplier = GIGA;
		} else if (str.endsWith("M")) {
			str = str.substring(0, str.length() - 1);
			multiplier = MEGA;
		} else if (str.endsWith("k")) {
			str = str.substring(0, str.length() - 1);
			multiplier = KILO;
		}
		try {
			int size = Integer.parseInt(str);
			return size * multiplier;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
	}

	public static String formatSize(int size) {
		if (size % GIGA == 0) {
			return (size / GIGA) + "GB";
		} else if (size % MEGA == 0) {
			return (size / MEGA) + "MB";
		} else if (size % KILO == 0) {
			return (size / KILO) + "kB";
		} else
			return NumberFormat.getIntegerInstance().format(size) + "B";
	}
}
