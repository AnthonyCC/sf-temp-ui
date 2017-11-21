package com.freshdirect.cms.ui.editor.index.domain;

import java.util.Set;

public class Synonym {

    private String word;
    private Set<String> synonymsOfWord;

    public Synonym(String word, Set<String> synonymsOfWord) {
        this.word = word;
        this.synonymsOfWord = synonymsOfWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Set<String> getSynonymsOfWord() {
        return synonymsOfWord;
    }

    public void setSynonymsOfWord(Set<String> synonymsOfWord) {
        this.synonymsOfWord = synonymsOfWord;
    }

    public void addSynonymsOfWord(Set<String> synonymsOfWord) {
        synonymsOfWord.addAll(synonymsOfWord);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(word).append(" :=> ");
        for (String synonym : synonymsOfWord) {
            stringBuilder.append(synonym).append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
