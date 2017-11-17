package com.freshdirect.storeapi.spelling.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.freshdirect.framework.util.IndexArray;
import com.freshdirect.storeapi.search.converter.DiacriticsRemover;

@Component
public class CsongorDistance implements StringDistance {

    private static class Alternative implements Cloneable {

        static char charAt(String s, int i) {
            if (i < s.length())
                return s.charAt(i);
            else
                return '\0';
        }
        static boolean step(List<Alternative> alts, String s1, String s2) {
            List<Alternative> newAlts = new ArrayList<Alternative>();
            for (Alternative alt : alts) {
                newAlts.add(alt);
                if (alt.finished)
                    continue;
                if (alt.pos.i1 < s1.length() || alt.pos.i2 < s2.length()) {
                    if (alt.pos.i1 >= s1.length()) {
                        alt.compensateSwaps(s1, s2);
                        while (alt.pos.i2 < s2.length()) {
                            alt.d++;
                            alt.pos.i2++;
                        }
                        alt.finished = true;
                    } else if (alt.pos.i2 >= s2.length()) {
                        alt.compensateSwaps(s1, s2);
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
                        Alternative newAlt = alt.compensateSwaps(s1, s2);
                        if (newAlt != null) {
                            newAlts.add(newAlt);
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
                                int d1 = s1.length() - alt.pos.i1;
                                int d2 = s2.length() - alt.pos.i2;
                                if (d1 == 0)
                                    alt.d += d2;
                                else if (d2 == 0)
                                    alt.d += d1;
                                else
                                    alt.d += d1 * d2;
                                alt.finished = true;
                            } else {
                                Iterator<Position> it = qs.iterator();
                                Position pos = it.next();
                                Alternative checkpoint = alt.clone();
                                alt.updateAltForPos(pos, s1, s2);
                                while (it.hasNext()) {
                                    Alternative newAlt2 = checkpoint.clone();
                                    Position pos1 = it.next();
                                    newAlt2.updateAltForPos(pos1, s1, s2);
                                    newAlts.add(newAlt2);
                                }
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
        char ch1 = EMPTY;
        char ch2 = EMPTY;
        int d = 0;

        boolean finished = false;

        Position pos = new Position();

        @Override
        public Alternative clone() {
            Alternative alt = new Alternative();
            alt.d = d;
            alt.pos = new Position(pos.i1, pos.i2);
            alt.ch1 = ch1;
            alt.ch2 = ch2;
            return alt;
        }

        private Alternative compensateSwaps(String s1, String s2) {
            boolean cs1 = false;
            boolean cs2 = false;
            if (ch1 != EMPTY && ch1 == charAt(s2, pos.i2))
                cs1 = true;
            if (ch2 != EMPTY && ch2 == charAt(s1, pos.i1))
                cs2 = true;
            if (cs1 && cs2) {
                Alternative newAlt = this.clone();
                ch1 = EMPTY;
                pos.i2++;
                newAlt.ch2 = EMPTY;
                newAlt.pos.i1++;
                return newAlt;
            } else if (cs1 && !cs2) {
                ch1 = EMPTY;
                pos.i2++;
            } else if (!cs1 && cs2) {
                ch2 = EMPTY;
                pos.i1++;
            }
            return null;
        }

        private void updateAltForPos(Position pos, String s1, String s2) {
            String t1 = s1.substring(this.pos.i1, this.pos.i1 + pos.i1);
            String t2 = s2.substring(this.pos.i2, this.pos.i2 + pos.i2);
            if (t1.length() == 0)
                d += t2.length();
            else if (t2.length() == 0)
                d += t1.length();
            else
                d += t1.length() * t2.length();
            ch1 = t1.isEmpty() || t1.length() == t2.length() ? EMPTY : t1.charAt(t1.length() - 1);
            ch2 = t2.isEmpty() || t1.length() == t2.length() ? EMPTY : t2.charAt(t2.length() - 1);
            this.pos.i1 += pos.i1 + 1;
            this.pos.i2 += pos.i2 + 1;
        }
    }

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

    private final static char EMPTY = '\0';

    public static boolean isPure(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '-' || c == '\'')
                return false;
        }
        return true;
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

    public CsongorDistance() {
    }

    @Override
    public int getDistance(String s1, String s2) {
        s1 = purify(new DiacriticsRemover().convert(s1));
        s2 = purify(new DiacriticsRemover().convert(s2));
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
}
