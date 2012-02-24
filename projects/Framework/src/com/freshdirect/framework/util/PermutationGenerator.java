package com.freshdirect.framework.util;

import java.util.Arrays;
import java.util.List;

public class PermutationGenerator {
	private int[] index;
	private int[] limit;
	private int i;
	private int R;
	private boolean overflow;

	public PermutationGenerator(List<? extends List<?>> candidates) {
		this(candidates, 10000);
	}

	public PermutationGenerator(List<? extends List<?>> candidates, int R) {
		int[] sizes = new int[candidates.size()];
		for (int i = 0; i < sizes.length; i++)
			sizes[i] = candidates.get(i).size();
		init(sizes, R);
	}

	public PermutationGenerator(int[] sizes) {
		this(sizes, 0);
	}

	public PermutationGenerator(int[] sizes, int R) {
		init(sizes, R);
	}

	private void init(int[] sizes, int R) {
		this.R = R;
		index = new int[sizes.length];
		limit = new int[sizes.length];
		overflow = false;
		for (int i = 0; i < limit.length; i++) {
			int s = sizes[i];
			limit[i] = s;
			if (s == 0)
				overflow = true;
		}
		i = 0;
	}

	public boolean hasMoreStep() {
		return !overflow;
	}

	public void step() {
		if (!hasMoreStep())
			return;

		if (isLast() || (R > 0 && i >= R)) {
			overflow = true;
			return;
		}

		int carry = 1;
		for (int j = 0; j < index.length; j++) {
			index[j] += carry;
			if (index[j] >= limit[j]) {
				index[j] = 0;
				carry = 1;
			} else
				break;
		}

		i++;
	}

	public boolean isFirst() {
		for (int i = 0; i < index.length; i++)
			if (index[i] != 0)
				return false;
		return true;
	}

	private boolean isLast() {
		for (int i = 0; i < index.length; i++)
			if (index[i] != limit[i] - 1)
				return false;
		return true;
	}

	public int length() {
		return index.length;
	}

	public int get(int i) {
		return index[i];
	}

	public int[] getIndex() {
		return index.clone();
	}

	@Override
	public String toString() {
		return "PermutationGenerator[i=" + i + ", index=" + Arrays.toString(index) + "]";
	}
}
