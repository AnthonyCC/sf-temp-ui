package com.freshdirect.cms.ui.editor.index.converter;

public class SpellingPunctuationConverter {

    private final static String[] REGULARS = { "!", "?", "[", "]", "{", "}", "<", ">", ":", ";", "_", "=", "~", "@", "\\", "¡", "¦", "|", "§", "¨", "©", "«", "¬", "®", "¯", "±",
            "`", "´", "¶", "·", "¸", "»", "¿" };

    public String convert(String term, boolean retainPunctiation) {
        if (!retainPunctiation) {
            for (String regular : REGULARS) {
                if (term.contains(regular)) {
                    term = term.replace(regular, "");
                }
            }
        }
        return term;
    }

}
