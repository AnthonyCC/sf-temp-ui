package com.freshdirect.cms.ui.editor.index.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.ui.editor.index.service.SpellingTermNormalizer;
import com.freshdirect.cms.ui.editor.search.service.SearchTermNormalizer;
import com.freshdirect.cms.ui.editor.term.service.TermNormalizer;
import com.google.common.base.Optional;

/**
 * @author zsombor
 * @author csongor
 */
public class SynonymDictionary {

    private static ContextualContentProvider contentProviderService = CmsServiceLocator.contentProviderService();
    private static ContentTypeInfoService contentTypeInfoService = CmsServiceLocator.contentTypeInfoService();

    private static String[] NUMBER_TEXT = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten" };

    private final Map<String, List<Synonym>> wordMap;

    public SynonymDictionary() {
        wordMap = new HashMap<String, List<Synonym>>();
    }

    private void addSynonym(String term, Set<String> alternates) {
        synchronized (wordMap) {
            if (term != null && !term.isEmpty()) {
                String firstWord = StringUtils.split(term.trim())[0];
                List<Synonym> result = wordMap.get(firstWord);
                if (result == null) {
                    result = new ArrayList<Synonym>();
                    result.add(new Synonym(term, alternates));
                    wordMap.put(firstWord, result);
                } else {
                    for (Synonym s : result) {
                        if (term.equals(s.getWord())) {
                            s.addSynonymsOfWord(alternates);
                            return;
                        }
                    }
                    result.add(new Synonym(term, alternates));
                    Collections.sort(result, new Comparator<Synonym>() {
                        @Override
                        public int compare(Synonym s1, Synonym s2) {
                            // reverse order by word count
                            int i = -(s1.getWord().length() - s2.getWord().length());
                            if (i == 0) {
                                return s1.getWord().compareTo(s2.getWord());
                            } else {
                                return i;
                            }
                        }
                    });
                }
            }
        }
    }

    public void addSynonyms(Set<String> synonyms, TermNormalizer termNormalizer) {
        synchronized (wordMap) {
            Set<String> normalized = new HashSet<String>();
            for (String synonym : synonyms) {
                normalized.add(termNormalizer.convert(synonym));
            }
            if (normalized.size() > 1) {
                for (String synonym : normalized) {
                    Set<String> exclude = new HashSet<String>(normalized);
                    exclude.remove(synonym);
                    addSynonym(synonym, exclude);
                }
            }
        }
    }

    public void addSynonyms(String[] from, String[] to, TermNormalizer termNormalizer) {
        Set<String> fromTerms = new HashSet<String>();
        for (String s : from) {
            fromTerms.add(termNormalizer.convert(s));
        }
        Set<String> toTerms = new HashSet<String>();
        for (String s : to) {
            toTerms.add(termNormalizer.convert(s));
        }
        for (String synonym : fromTerms) {
            addSynonym(synonym, toTerms);
        }
    }

    public void addSynonyms(SynonymDictionary dictionary) {
        synchronized (dictionary.wordMap) {
            for (List<Synonym> synonyms : dictionary.wordMap.values()) {
                for (Synonym synonym : synonyms) {
                    addSynonym(synonym.getWord(), synonym.getSynonymsOfWord());
                }
            }
        }
    }

    public Synonym[] getSynonymsForPrefix(String firstWord) {
        synchronized (wordMap) {
            List<Synonym> synonyms = wordMap.get(firstWord);
            if (synonyms == null) {
                return new Synonym[0];
            }
            Collections.sort(synonyms, new Comparator<Synonym>() {
                @Override
                public int compare(Synonym o1, Synonym o2) {
                    int d = o2.getWord().length() - o1.getWord().length();
                    if (d != 0) {
                        return d;
                    }
                    return o1.getWord().compareTo(o2.getWord());
                }
            });
            return synonyms.toArray(new Synonym[0]);
        }
    }

    public Synonym getSynonymForWord(String word) {
        synchronized (wordMap) {
            List<Synonym> synonyms = wordMap.get(word);
            if (synonyms != null) {
                for (Synonym synonym : synonyms) {
                    if (StringUtils.split(synonym.getWord()).length == 1) {
                        return synonym;
                    }
                }
            }
            return null;
        }
    }

    public Map<String, List<Synonym>> getSynonyms() {
        return Collections.unmodifiableMap(wordMap);
    }

    public static SynonymDictionary createNumberSynonyms() {
        SynonymDictionary dict = new SynonymDictionary();
        SpellingTermNormalizer spellingTermNormalizer = new SpellingTermNormalizer(true);
        {
            Set<String> synonyms = new HashSet<String>();
            synonyms.add("0");
            synonyms.add("null");
            dict.addSynonyms(synonyms, spellingTermNormalizer);
        }
        for (int i = 0; i < NUMBER_TEXT.length; i++) {
            String text = NUMBER_TEXT[i];
            Set<String> synonyms = new HashSet<String>();
            synonyms.add(Integer.toString(i));
            synonyms.add(text);
            dict.addSynonyms(synonyms, spellingTermNormalizer);
        }
        return dict;
    }

    public static SynonymDictionary createFromCms() {
        return createSynonyms(ContentKeyFactory.get("FDFolder:synonymList"), ContentType.Synonym, new SearchTermNormalizer(false));
    }

    public static SynonymDictionary createSpellingFromCms() {
        return createSynonyms(ContentKeyFactory.get("FDFolder:spellingSynonymList"), ContentType.SpellingSynonym, new SpellingTermNormalizer(false));
    }

    private static Set<ContentKey> collectReachableKeys(ContentKey root, ContentType targetType, ContextualContentProvider contentProviderService) {
        Set<ContentKey> reachableKeys = new HashSet<ContentKey>();
        Set<ContentKey> childKeys = contentProviderService.getChildKeys(root, false);
        for (ContentKey child : childKeys) {
            if (targetType == null || child.type.equals(targetType)) {
                reachableKeys.addAll(collectReachableKeys(child, targetType, contentProviderService));
                reachableKeys.add(child);
            }
        }
        return reachableKeys;
    }

    public static SynonymDictionary createSynonyms(ContentKey synonymContentKey, ContentType synonymContentType, TermNormalizer termNormalizer) {
        return createSynonyms(synonymContentKey, synonymContentType, contentProviderService, termNormalizer);
    }

    public static SynonymDictionary createSynonyms(ContentKey synonymContentKey, ContentType synonymContentType, ContextualContentProvider contentProviderService,
            TermNormalizer termNormalizer) {
        SynonymDictionary dict = new SynonymDictionary();

        Map<Attribute, Object> synonymRootNode = contentProviderService.getAllAttributesForContentKey(synonymContentKey);
        if (synonymRootNode == null || synonymRootNode.size() == 0) {
            return dict;
        }
        Set<ContentKey> synonymKeys = collectReachableKeys(synonymContentKey, synonymContentType, contentProviderService);
        Map<ContentKey, Map<Attribute, Object>> synonymNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey synynomKey : synonymKeys) {
            synonymNodes.put(synynomKey, contentProviderService.getAllAttributesForContentKey(synynomKey));
        }
        for (ContentKey nodeKey : synonymNodes.keySet()) {
            Map<Attribute, Object> node = synonymNodes.get(nodeKey);
            String[] from = getSynonymFromValues(node);
            String[] to = getSynonymToValues(node);
            if (from != null && to != null && to.length > 0) {
                Optional<Attribute> bidirectionalAttribute = contentTypeInfoService.findAttributeByName(nodeKey.type, "bidirectional");
                if (bidirectionalAttribute.isPresent() && Boolean.TRUE.equals(node.get(bidirectionalAttribute.get()))) {
                    Set<String> synonyms = new HashSet<String>(to.length + 1);
                    for (String word : from) {
                        synonyms.add(termNormalizer.convert(word));
                    }
                    for (String word : to) {
                        synonyms.add(termNormalizer.convert(word));
                    }
                    dict.addSynonyms(synonyms, termNormalizer);
                } else {
                    dict.addSynonyms(from, to, termNormalizer);
                }
            }
        }
        return dict;
    }

    public static String[] getSynonymToValues(Map<Attribute, Object> node) {
        String attributeValue = "";
        for (Attribute attribute : node.keySet()) {
            if (attribute.getName().equalsIgnoreCase("synonymValue")) {
                attributeValue = (String) node.get(attribute);
                break;
            }
        }
        return trimAndLowerCase(StringUtils.split(attributeValue, ','));
    }

    public static String[] getSynonymFromValues(Map<Attribute, Object> node) {
        String attributeValue = "";
        for (Attribute attribute : node.keySet()) {
            if (attribute.getName().equalsIgnoreCase("word")) {
                attributeValue = (String) node.get(attribute);
                break;
            }
        }
        return trimAndLowerCase(StringUtils.split(attributeValue, ','));
    }

    private static String[] trimAndLowerCase(String[] ret) {
        for (int i = 0; i < ret.length; i++) {
            ret[i] = ret[i].toLowerCase().trim();
        }
        return ret;
    }
}
