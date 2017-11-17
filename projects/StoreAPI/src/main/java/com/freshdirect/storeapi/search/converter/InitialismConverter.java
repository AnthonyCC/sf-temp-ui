package com.freshdirect.storeapi.search.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class InitialismConverter {

    protected List<List<String>> convertInner(String term) {
        List<String> inits = new ArrayList<String>();
        boolean initialism = true;
        boolean allDigits = true;
        for (int i = 0; i < term.length(); i++) {
            char ch = term.charAt(i);
            if (i % 2 == 0) {
                if (Character.isLetterOrDigit(ch)) {
                    if (Character.isLetter(ch))
                        allDigits = false;
                    inits.add(new String(new char[] { ch }));
                } else {
                    initialism = false;
                    break;
                }
            } else {
                if (ch != '.') {
                    initialism = false;
                    break;
                }
            }
        }
        if (allDigits)
            return Collections.singletonList(Collections.singletonList(term));
        else if (initialism && inits.size() > 1) {
            List<List<String>> result = new ArrayList<List<String>>();
            result.add(inits);
            result.add(Collections.singletonList(StringUtils.join(inits, "")));
            return result;
        } else
            return Collections.singletonList(Collections.singletonList(term));
    }

    private List<List<String>> normalize(List<List<String>> input) {
        List<List<String>> result = new ArrayList<List<String>>();
        if (input != null) {
            for (List<String> item : input) {
                if (item != null) {
                    List<String> n = new ArrayList<String>();
                    for (String s : item) {
                        if (s != null) {
                            if (!s.isEmpty())
                                n.add(s);
                        }
                    }
                    if (!n.isEmpty())
                        result.add(n);
                }
            }
        }
        return result;
    }

    private void permute(List<List<String>> sofar, List<List<String>> result) {
        if (result.size() == 0)
            return;
        else if (result.size() == 1) {
            List<String> r = result.get(0);
            for (List<String> item : sofar)
                item.addAll(r);
        } else {
            List<List<String>> permutations = new ArrayList<List<String>>();
            Iterator<List<String>> iterator = result.iterator();
            List<String> first = Collections.emptyList();
            if (iterator.hasNext())
                first = iterator.next();
            while (iterator.hasNext()) {
                List<String> add = iterator.next();
                List<List<String>> duplicate = new ArrayList<List<String>>();
                for (List<String> sf : sofar)
                    duplicate.add(new ArrayList<String>(sf));
                for (List<String> d : duplicate)
                    d.addAll(add);
                permutations.addAll(duplicate);
            }
            for (List<String> sf : sofar)
                sf.addAll(first);
            sofar.addAll(permutations);
        }
    }

    public String convert(String input) {
        List<String> result = new ArrayList<String>();
        for (String term : StringUtils.split(input)) {
            List<List<String>> sofar = new ArrayList<List<String>>();
            sofar.add(new ArrayList<String>());
            permute(sofar, normalize(convertInner(term)));
            for (List<String> sf : sofar)
                if (!sf.isEmpty()) {
                    result.addAll(new ArrayList<String>(sf));
                }
        }
        return StringUtils.join(result, " ");
    }
}
