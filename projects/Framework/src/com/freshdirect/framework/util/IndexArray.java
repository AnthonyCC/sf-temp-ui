/**
 * 
 */
package com.freshdirect.framework.util;

import java.util.List;

public class IndexArray {
	private int[] index;
	private int[] limit;
	private int q;
	private int Q;
	private int i;
	private int R;
	private boolean overflow;

	public IndexArray(List<? extends List<?>> candidates, int R) {
		int[] sizes = new int[candidates.size()];
		for (int i = 0; i < sizes.length; i++)
			sizes[i] = candidates.get(i).size();
		init(sizes, R);
	}

	public IndexArray(List<? extends List<?>> candidates) {
		this(candidates, 0);
	}
	
	public IndexArray(int[] sizes) {
		init(sizes, 0);
	}

	public IndexArray(int[] sizes, int R) {
		init(sizes, R);
	}

	private void init(int[] sizes, int R) {
		this.R = R;
		index = new int[sizes.length];
		limit = new int[sizes.length];
		Q = 0;
		overflow = false;
		for (int i = 0; i < limit.length; i++) {
			int s = sizes[i];
			limit[i] = s;
			if (s == 0)
				overflow = true;
			Q += s;
		}
		Q -= index.length;
		i = 0;
		initQ(0);
	}

	void initQ(int q) {
		this.q = q;
		if (q > Q) {
			overflow = true;
			return;
		}
		index[0] = q;
		for (int i = 1; i < index.length; i++)
			index[i] = 0;
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

		do {
			BLOCK: {
				int k;
				for (k = 0; k < index.length; k++)
					if (index[k] > 0)
						break;
				if (k >= index.length - 1) {
					initQ(q + 1);
					break BLOCK;
				}

				index[k + 1]++;
				index[0] = index[k] - 1;
				for (int j = 1; j <= k; j++)
					index[j] = 0;
			}
		} while (outOfBounds() && !isLast());
		i++;
	}

	private boolean outOfBounds() {
		for (int i = 0; i < index.length; i++)
			if (index[i] >= limit[i])
				return true;
		return false;
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
}