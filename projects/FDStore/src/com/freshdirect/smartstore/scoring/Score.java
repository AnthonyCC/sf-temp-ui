package com.freshdirect.smartstore.scoring;

import com.freshdirect.fdstore.content.ContentNodeModel;

public class Score implements Comparable {
    ContentNodeModel node;
    double[]         values;

    public Score(int size) {
        this.values = new double[size];
    }

    public Score(ContentNodeModel node, double[] values) {
        this.node = node;
        this.values = values;
    }

    public void set(int pos, double value) {
        values[pos] = value;
    }

    public void set(int pos, int value) {
        values[pos] = value;
    }

    public double get(int pos) {
        return values[pos];
    }

    public int size() {
        return values.length;
    }

    public ContentNodeModel getNode() {
        return node;
    }

    public void setNode(ContentNodeModel node) {
        this.node = node;
    }

    public int compareTo(Object o) {
        Score sc = (Score) o;
        for (int i = 0; i < values.length; i++) {
            if (!(values[i] == sc.values[i])) {
                if (values[i] > sc.values[i]) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
//        int res = node.getFullName().compareTo(sc.node.getFullName());
//        if (res==0) {
          int res = node.getContentKey().getId().compareTo(sc.node.getContentKey().getId());
//        }
        return res;
    }
}
