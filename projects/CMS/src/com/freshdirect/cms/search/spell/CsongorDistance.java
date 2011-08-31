package com.freshdirect.cms.search.spell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.search.term.DiacriticsRemoval;
import com.freshdirect.framework.util.IndexArray;

public class CsongorDistance implements StringDistance {
	private final static String EMPTY = "";

	private static class Position {
		int i1;
		int i2;

		public Position() {
			i1 = 0;
			i2 = 0;
		}

		public Position(int i1, int i2) {
			super();
			this.i1 = i1;
			this.i2 = i2;
		}

		int sum() {
			return i1 + i2;
		}
	}

	private static class Alternative implements Cloneable {
		int d = 0;
		Position pos = new Position();
		String ch1 = EMPTY;
		String ch2 = EMPTY;
		boolean finished = false;

		@Override
		public Alternative clone() {
			Alternative alt = new Alternative();
			alt.d = d;
			alt.pos = new Position(pos.i1, pos.i2);
			alt.ch1 = ch1;
			alt.ch2 = ch2;
			return alt;
		}

		private void updateAltForPos(Position pos, String s1, String s2) {
			String t1 = s1.substring(this.pos.i1, this.pos.i1 + pos.i1);
			String t2 = s2.substring(this.pos.i2, this.pos.i2 + pos.i2);
			d += Math.max(t1.length(), t2.length());
			int c1 = 0;
			int c2 = 0;
			for (int i = 0; i < Math.min(t1.length(), ch2.length()); i++)
				if (t1.charAt(i) == ch2.charAt(i))
					c1++;
				else
					break;
			for (int i = 0; i < Math.min(t2.length(), ch1.length()); i++)
				if (t2.charAt(i) == ch1.charAt(i))
					c2++;
				else
					break;
			d -= Math.max(c1, c2);
			if (pos.i1 != pos.i2) {
				ch1 = new StringBuilder(t1).toString();
				ch2 = new StringBuilder(t2).toString();
			} else {
				ch1 = EMPTY;
				ch2 = EMPTY;
			}
			this.pos.i1 += pos.i1 + 1;
			this.pos.i2 += pos.i2 + 1;
		}

		static boolean step(List<Alternative> alts, String s1, String s2) {
			List<Alternative> newAlts = new ArrayList<Alternative>();
			for (Alternative alt : alts) {
				newAlts.add(alt);
				if (alt.finished)
					continue;
				if (alt.pos.i1 < s1.length() || alt.pos.i2 < s2.length()) {
					if (alt.pos.i1 >= s1.length()) {
						String t2 = s2.substring(alt.pos.i2);
						for (int i = 0; i < Math.min(t2.length(), alt.ch1.length()); i++)
							if (t2.charAt(i) == alt.ch1.charAt(i)) {
								alt.d--;
							} else
								break;
						while (alt.pos.i2 < s2.length()) {
							alt.d++;
							alt.pos.i2++;
						}
						alt.finished = true;
					} else if (alt.pos.i2 >= s2.length()) {
						String t1 = s1.substring(alt.pos.i1);
						for (int i = 0; i < Math.min(t1.length(), alt.ch2.length()); i++)
							if (t1.charAt(i) == alt.ch2.charAt(i)) {
								alt.d--;
							} else
								break;
						while (alt.pos.i1 < s1.length()) {
							alt.d++;
							alt.pos.i1++;
						}
						alt.finished = true;
					} else if (charAt(s1, alt.pos.i1) == charAt(s2, alt.pos.i2)) {
						alt.pos.i1++;
						alt.pos.i2++;
						alt.ch1 = EMPTY;
						alt.ch2 = EMPTY;
					} else {
						List<Position> qs = new ArrayList<Position>();
						IndexArray idx = new IndexArray(new int[] { s1.length() - alt.pos.i1, s2.length() - alt.pos.i2 });
						for (; idx.hasMoreStep(); idx.step()) {
							if (charAt(s1, alt.pos.i1 + idx.get(0)) == charAt(s2, alt.pos.i2 + idx.get(1))) {
								if (qs.isEmpty()) {
									qs.add(new Position(idx.get(0), idx.get(1)));
								} else {
									if (idx.get(0) + idx.get(1) > qs.get(0).sum())
										break;
									else {
										// must be equal
										qs.add(new Position(idx.get(0), idx.get(1)));
									}
								}
							}
						}
						if (qs.isEmpty()) {
							// the end of the string is a total mess
							alt.d += Math.max(s1.length() - alt.pos.i1, s2.length() - alt.pos.i2);
							alt.finished = true;
						} else {
							Iterator<Position> it = qs.iterator();
							Position pos = it.next();
							Alternative checkpoint = alt.clone();
							alt.updateAltForPos(pos, s1, s2);
							while (it.hasNext()) {
								Alternative newAlt = checkpoint.clone();
								Position pos1 = it.next();
								newAlt.updateAltForPos(pos1, s1, s2);
								newAlts.add(newAlt);
							}
						}
					}
				} else {
					alt.finished = true;
				}
			}
			alts.clear();
			alts.addAll(newAlts);
			for (Alternative alt : alts) {
				if (!alt.finished)
					return true;
			}
			return false;
		}

		static char charAt(String s, int i) {
			if (i < s.length())
				return s.charAt(i);
			else
				return '\0';
		}
	}

	public CsongorDistance() {
	}

	@Override
	public int getDistance(String s1, String s2) {
		s1 = purify(DiacriticsRemoval.removeDiactrics(s1));
		s2 = purify(DiacriticsRemoval.removeDiactrics(s2));
		// we have to build a search tree
		List<Alternative> alts = new ArrayList<Alternative>();
		alts.add(new Alternative());
		while (Alternative.step(alts, s1, s2))
			;

		int d = Integer.MAX_VALUE;
		for (Alternative alt : alts)
			if (alt.d < d)
				d = alt.d;

		return d;
	}

	public static String purify(String str) {
		StringBuilder buf = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '-' || c == '\'')
				continue;
			buf.append(c);
		}
		return buf.toString();
	}

	public static boolean isPure(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '-' || c == '\'')
				return false;
		}
		return true;
	}
}
