package com.freshdirect.storeapi.autocomplete.domain;

public class HitCounter implements Comparable<HitCounter> {

    public String prefix;

    // the number of occurences of the prefix word in the whole database.
    public int number = 1;
    // number of words in the prefix
    public byte wordCount;

    // the number of distinct word which follows this prefix
    public int followCount;

    public HitCounter beforePrefix;

    public HitCounter(String prefix, int wordCount, HitCounter beforePrefix) {
        this.prefix = prefix;
        this.wordCount = (byte) wordCount;
        this.beforePrefix = beforePrefix;
        if (beforePrefix != null) {
            beforePrefix.followCount++;
        }
    }

    public void inc() {
        number++;
    }

    @Override
    public int compareTo(HitCounter o) {
        return prefix.compareTo(o.prefix);
    }

    @Override
    public String toString() {
        return prefix + '(' + number + ')';
    }
}