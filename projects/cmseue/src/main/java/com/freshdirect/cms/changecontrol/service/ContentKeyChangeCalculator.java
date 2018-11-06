package com.freshdirect.cms.changecontrol.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;

@Service
public class ContentKeyChangeCalculator {

    public String[] describeContentKeyListChange(List<ContentKey> valueBefore, List<ContentKey> valueAfter) {
        List<String> oldKeys = new ArrayList<String>();
        for (ContentKey contentKey : valueBefore) {
            oldKeys.add(contentKey.getEncoded());
        }
        List<String> newKeys = new ArrayList<String>();
        for (ContentKey contentKey : valueAfter) {
            newKeys.add(contentKey.toString());
        }
        
        return describeContentKeysChange(oldKeys, newKeys);
    }

    public String[] describeContentKeysChange(List<String> oldKeys, List<String> newKeys) {
        String[] describedValues = null;

        Map<String, int[]> changeVector = computeItemPositions(oldKeys, newKeys);
        if (changeVector != null && !changeVector.isEmpty()) {

            Set<String> keysAdded = new HashSet<String>();
            Set<String> keysRemoved = new HashSet<String>();
            Map<String, String> keysMoved = new HashMap<String, String>();
            Map<String, String> keysPermuted = new HashMap<String, String>();

            for (Map.Entry<String, int[]> entry : changeVector.entrySet()) {
                final String aKey = entry.getKey();
                int[] positions = entry.getValue();
                if (positions[1] == -1) {
                    keysRemoved.add(aKey);
                } else if (positions[0] == -1) {
                    keysAdded.add(aKey);
                }
            }

            // check for permutations
            if (keysRemoved.size() < oldKeys.size() || keysAdded.size() < newKeys.size()) {
                List<String> filteredOldKeys = new ArrayList<String>();
                List<String> filteredNewKeys = new ArrayList<String>();

                for (String aKey : oldKeys) {
                    if (!keysRemoved.contains(aKey)) {
                        filteredOldKeys.add(aKey);
                    }
                }
                for (String aKey : newKeys) {
                    if (!keysAdded.contains(aKey)) {
                        filteredNewKeys.add(aKey);
                    }
                }

                // At this point we have two equally sized lists having the same elements
                // element order may or may not be the same
                Map<String, int[]> changeVector2 = computeItemPositions(filteredOldKeys, filteredNewKeys);
                if (changeVector2 != null) {
                    for (Map.Entry<String, int[]> entry : changeVector2.entrySet()) {
                        String movedKey = entry.getKey();
                        int[] positions = entry.getValue();
                        if (positions[0] != positions[1]) {
                            // detect two items are permuted
                            if (filteredNewKeys.get(positions[0]).equals(filteredOldKeys.get(positions[1]))) {
                                String thisKey = movedKey;
                                String otherKey = filteredNewKeys.get(positions[0]);

                                // don't put {A,B} pair if {B,A} is already in
                                if (!keysPermuted.containsKey(otherKey)) {
                                    keysPermuted.put(thisKey, otherKey);
                                }
                            } else {
                                keysMoved.put(movedKey, String.format("%d => %d", oldKeys.indexOf(movedKey), newKeys.indexOf(movedKey)));
                            }
                        }
                    }
                }
            }

            describedValues = describeKeyChanges(keysAdded, keysRemoved, keysPermuted, keysMoved);
        } else {
            describedValues = new String[] { "#error", null };
        }

        return describedValues;
    }

    private Map<String, int[]> computeItemPositions(List<String> left, List<String> right) {
        Set<String> keys = new HashSet<String>();
        if (left != null) {
            keys.addAll(left);
        }
        if (right != null) {
            keys.addAll(right);
        }

        Map<String, int[]> changeVector = new HashMap<String, int[]>(keys.size());
        for (String key : keys) {
            int[] positions = new int[2];
            positions[0] = left != null ? left.indexOf(key) : -1;
            positions[1] = right != null ? right.indexOf(key) : -1;

            changeVector.put(key, positions);
        }

        return changeVector;
    }

    private String[] describeKeyChanges(Collection<String> keysAdded, Collection<String> keysRemoved, Map<String, String> keysPermuted, Map<String, String> keysMoved) {
        StringBuilder oldValueBuilder = new StringBuilder();
        StringBuilder newValueBuilder = new StringBuilder();

        if (!keysRemoved.isEmpty()) {
            oldValueBuilder.append("Deleted: ").append(StringUtils.join(keysRemoved, ", "));
        }
        if (!keysAdded.isEmpty()) {
            newValueBuilder.append("Added: ").append(StringUtils.join(keysAdded, ", "));
        }
        if (!keysPermuted.isEmpty()) {
            if (newValueBuilder.length() > 0) {
                newValueBuilder.append("\n");
            }

            newValueBuilder.append("Exchanged: ");
            for (Map.Entry<String, String> entry : keysPermuted.entrySet()) {
                newValueBuilder.append(entry.getKey()).append("<->").append(entry.getValue()).append("; ");
            }
        }
        if (!keysMoved.isEmpty()) {
            if (newValueBuilder.length() > 0) {
                newValueBuilder.append("\n");
            }

            newValueBuilder.append("Moved: ");
            for (Map.Entry<String, String> entry : keysMoved.entrySet()) {
                newValueBuilder.append(entry.getKey()).append(" ").append(entry.getValue()).append("; ");
            }
        }

        String[] describedValues = new String[2];

        if (oldValueBuilder.length() > 0) {
            describedValues[0] = oldValueBuilder.toString();
        }
        if (newValueBuilder.length() > 0) {
            describedValues[1] = newValueBuilder.toString();
        }

        return describedValues;
    }

}
