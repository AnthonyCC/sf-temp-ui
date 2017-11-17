package com.freshdirect.storeapi.spelling.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SpellingHit implements Serializable, Comparable<SpellingHit> {

    private static final long serialVersionUID = -2387213302294463590L;

    public static Comparator<SpellingHit> SORT_BY_DISTANCE = new Comparator<SpellingHit>() {

        @Override
        public int compare(SpellingHit o1, SpellingHit o2) {
            int d = o1.distance - o2.distance;
            if (d != 0)
                return d;
            else
                return Double.compare(o2.score, o1.score);
        }
    };

    public static SpellingHit join(List<SpellingHit> hits) {
        int distance = 0;
        List<String> phrase = new ArrayList<String>(hits.size());
        for (SpellingHit h : hits) {
            phrase.add(h.phrase);
            distance += h.distance;
        }
        List<String> spellingMatch = new ArrayList<String>(hits.size());
        for (SpellingHit h : hits) {
            spellingMatch.add(h.spellingMatch);
        }
        SpellingHit hit = new SpellingHit(StringUtils.join(phrase, " "), StringUtils.join(spellingMatch, " "), distance);
        return hit;
    }

    public static double bestScore(List<SpellingHit> hits) {
        if (hits.isEmpty())
            return 0.;
        else
            return hits.get(0).score;
    }

    private String phrase;
    private String spellingMatch;
    private int distance;
    private double score;

    public SpellingHit(String phrase, int distance) {
        super();
        this.phrase = phrase;
        this.spellingMatch = phrase;
        this.distance = distance;
        this.score = Math.max(0.0f, (double) (phrase.length() - distance)) / phrase.length();
    }

    public SpellingHit(String phrase, String spellingMatch, int distance) {
        super();
        this.phrase = phrase;
        this.spellingMatch = spellingMatch;
        this.distance = distance;
        this.score = Math.max(0.0f, (double) (phrase.length() - distance)) / phrase.length();
    }

    public String getPhrase() {
        return phrase;
    }

    public String getSpellingMatch() {
        return spellingMatch;
    }

    public int getDistance() {
        return distance;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phrase == null) ? 0 : phrase.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpellingHit other = (SpellingHit) obj;
        if (phrase == null) {
            if (other.phrase != null)
                return false;
        } else if (!phrase.equals(other.phrase))
            return false;
        return true;
    }

    @Override
    public int compareTo(SpellingHit o) {
        return Double.compare(o.score, score);
    }

    @Override
    public String toString() {
        return "SpellingHit[word=" + phrase + ", spellingMatch=" + spellingMatch + ", distance=" + distance + "]";
    }
}
