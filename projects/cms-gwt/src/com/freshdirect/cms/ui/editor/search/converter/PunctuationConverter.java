package com.freshdirect.cms.ui.editor.search.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PunctuationConverter {

    private final static String regulars = "!?[]{}<>:;_=~@\\¡¦|§¨©«¬®¯±`´¶·¸»¿";

    public String convert(String term, boolean retainPunctuation) {
        List<String> result = new ArrayList<String>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < term.length(); i++) {
            char character = term.charAt(i);
            if (regulars.indexOf(character) != -1) {
                if (retainPunctuation) {
                    stringBuilder.append(character);
                } else if (stringBuilder.length() != 0) {
                    result.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
            } else if (character == '(' || character == ')') {
                if (isBeforeWhiteSpace(term, i) || isAfterWhiteSpace(term, i)) {
                    if (stringBuilder.length() != 0) {
                        result.add(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                    }
                }
                // else stripped
            } else if (character == '.') {
                if (isBeforeDigit(term, i) && isAfterDigit(term, i))
                    stringBuilder.append(character);
                else if (stringBuilder.length() != 0) {
                    result.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                }
            } else if (character == ',') {
                if (retainPunctuation && i != 0 && i != term.length() - 1) {
                    stringBuilder.append(character);
                } else if (!(isBeforeDigit(term, i) && isAfterDigit(term, i))) {
                    if (stringBuilder.length() != 0) {
                        result.add(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                    }
                }
                // else stripped
            } else
                stringBuilder.append(character);
        }
        if (stringBuilder.length() != 0) {
            result.add(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
        return StringUtils.join(result, " ");
    }

    private static boolean isBeforeWhiteSpace(String term, int i) {
        if (i == 0)
            return true;
        else
            return Character.isWhitespace(term.charAt(i - 1));
    }

    private static boolean isAfterWhiteSpace(String term, int i) {
        if (i >= term.length() - 1)
            return true;
        else
            return Character.isWhitespace(term.charAt(i + 1));
    }

    private static boolean isBeforeDigit(String term, int i) {
        if (i == 0)
            return true;
        else
            return Character.isDigit(term.charAt(i - 1));
    }

    private static boolean isAfterDigit(String term, int i) {
        if (i >= term.length() - 1)
            return true;
        else
            return Character.isDigit(term.charAt(i + 1));
    }
}
