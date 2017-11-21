package com.freshdirect.storeapi.spelling.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.freshdirect.framework.util.PermutationGenerator;
import com.freshdirect.storeapi.spelling.domain.SpellingHit;
import com.freshdirect.storeapi.spelling.domain.StringDistance;

public class SpellingUtils {

    public static List<SpellingHit> filterBestSpellingHits(List<SpellingHit> spellingHits, double threshold) {
        List<SpellingHit> results = new ArrayList<SpellingHit>();
        Iterator<SpellingHit> it = spellingHits.iterator();
        int level = 0;
        int d = 0;
        while (it.hasNext()) {
            SpellingHit hit = it.next();
            if (hit.getDistance() != d && level == 2)
                break;
            if (hit.getScore() >= threshold)
                results.add(hit);
            if (hit.getDistance() != d) {
                d = hit.getDistance();
                level++;
            }
        }
        Collections.sort(results);
        return results;
    }

    public static boolean checkPartialThreshold(List<String> original, List<String> suggested, double threshold, StringDistance distance) {
        List<String> remOrig = new ArrayList<String>(original);
        List<String> remSug = new ArrayList<String>(suggested);

        while (!remOrig.isEmpty() && !remSug.isEmpty()) {
            double bestScore = 0.0;
            int i = 0;
            int j = 0;
            PermutationGenerator pg = new PermutationGenerator(new int[] { 3, 3 });
            for (; pg.hasMoreStep(); pg.step()) {
                int x = pg.get(0);
                int y = pg.get(1);
                if (x >= remOrig.size() || y >= remSug.size())
                    continue;
                if (x != 0 && y != 0)
                    continue;
                String s1 = StringUtils.join(remOrig.subList(0, x + 1), " ");
                String s2 = StringUtils.join(remSug.subList(0, y + 1), " ");
                int d = distance.getDistance(s1, s2);
                double score = Math.max(0., ((double) (s2.length() - d)) / s2.length());
                if (score >= bestScore) {
                    bestScore = score;
                    i = x;
                    j = y;
                }
            }
            if (bestScore < threshold)
                return false;
            for (int x = 0; x <= i; x++)
                remOrig.remove(0);
            for (int y = 0; y <= j; y++)
                remSug.remove(0);
        }

        return true;
    }
}
