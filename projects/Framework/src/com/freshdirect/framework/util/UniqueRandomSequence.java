package com.freshdirect.framework.util;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;


/**
 * An integer sequence that returns unique numbers.
 *
 * The sequence returns integers in [0,max). This is no limitation
 * since it can simply be adopted to generate numbers
 * in [min,max) (i.e. min = int UniqueRandomSequence(max - min).{@link #next()} 
 * or to pick unique random elements from a set of finite length.
 *
 * @author istvan
 */
public abstract class UniqueRandomSequence {

	protected int max;
	protected Random R;

	/**
	 * Constructor.
	 * @param R random number generator
	 * @param max upper bound (exclusive)
	 */
	protected UniqueRandomSequence(int max, Random R) {
		this.max = max;
		this.R = R;
	}

	/**
	 * Change random number generator.
	 * @param R random number generator
	 */
	public void setRandom(Random R) {
		this.R = R;
	}

	/**
	 * Return next number.
	 * @return next number.
	 */
	public abstract int next();

	/**
	 * Reset the sequence.
	 * This will start the sequence of numbers again.
	 */
	public abstract void reset();

	/**
	 * Generates an explicit random sequence.
	 *
	 * The random sequence is prepared beforehands. Appropriate for
	 * small sequences or when a large portion of the entire sequence
	 * will be picked.
	 *
	 */
	public static class ExplicitlyEnumerated extends UniqueRandomSequence {

		private int index = 0;
		private int[] sequence;

		/**
		 * Create a new random sequence.
		 * 
		 * Alaso invokes reset.
		 * @see #reset
		 */
		public void reShuffle() {
			// See D. Knuth's Art of Computer Programming
			// why it is truely uniformly random
			for(int i=1; i< max; ++i) {
				int z = R.nextInt(i+1);
				int tmp = sequence[z];
				sequence[z] = sequence[i];
				sequence[i] = tmp;
			}
		}

		public ExplicitlyEnumerated(int max, Random R) {
			super(max,R);
			sequence = new int[max];
			for(int i=0; i<max; ++i) sequence[i] = i;
			reShuffle();
		}

		public ExplicitlyEnumerated(int max) {
			this(max,new Random(System.currentTimeMillis()));
		}

		public int next() {
			if (index == max) throw new IndexOutOfBoundsException();
			return sequence[index++];
		}

		public void reset() {
			index = 0;
		}
	}

	/**
	 * Records already retrieved numbers.
	 *
	 * Appropriate when only a smaller proportion of the sequence is
	 * to be picked.
	 */
	public static class Recording extends UniqueRandomSequence {
		private Set used = new HashSet();

		public Recording(int max, Random R) {
			super(max,R);
		}

		public Recording(int max) {
			this(max,new Random(System.currentTimeMillis()));
		}

		public int next() {
			if (used.size() == max) throw new IndexOutOfBoundsException();
			while(true) {
				Integer i = new Integer(R.nextInt(max));

				if (used.contains(i)) continue;
				else {
					used.add(i);
					return i.intValue();
				}
			}
		}

		public void reset() {
			used.clear();
		}

	}


	/**
	 * Return a UniqueRandomSequence.
	 *
	 * If the numbers to be picked is at least half of the size of
	 * the sequence, return a {@link ExplicitlyEnumerated}, otherwise
	 * a {@link Recorder} instance. 
	 *
	 * @param picks (approximate) number values to be retrieved
	 * @param max size of random sequence
	 * @param R random number generator
	 * @return instance of UniqueRandomSequence
	 */
	public static UniqueRandomSequence getInstance(int picks, int max, Random R) {
		if (picks >= max/2) return new ExplicitlyEnumerated(max, R);
		else return new Recording(max,R);
	}

	/**
	 * Return a UniqueRandomSequence.
	 *
	 * @param picks (approximate) number values to be retrieved
	 * @param max size of random sequence
	 * @return instance of UniqueRandomSequence
	 * @see #getInstance(int, int, Random)
	 */
	public static UniqueRandomSequence getInstance(int picks, int max) {
		return getInstance(picks,max,new Random(System.currentTimeMillis()));
	}

}
