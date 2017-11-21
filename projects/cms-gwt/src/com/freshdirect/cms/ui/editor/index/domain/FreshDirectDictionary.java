package com.freshdirect.cms.ui.editor.index.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.lucene.domain.AttributeIndex;
import com.freshdirect.cms.ui.editor.index.service.IndexerUtil;
import com.freshdirect.cms.ui.editor.index.service.SpellingTermNormalizer;

public class FreshDirectDictionary implements Dictionary {

    private final static Logger LOGGER = LoggerFactory.getLogger(FreshDirectDictionary.class);
    private Map<ContentKey, Map<Attribute, Object>> contentNodes;
    private Map<ContentType, List<AttributeIndex>> indexes;
    private List<SynonymDictionary> synonymDictionaries;
    private List<SynonymDictionary> spellingDictionaries;
    private final boolean skipKeywords;
    private final boolean primaryHomeKeywordsEnabled;
    private final boolean parentRecursionEnabled;
    private final ContentKey storeKey;
    private final ContextualContentProvider storeContentSource; // TODO: why???

    public FreshDirectDictionary(Map<ContentKey, Map<Attribute, Object>> contentNodes, Map<ContentType, List<AttributeIndex>> indexes, List<SynonymDictionary> synonymDictionaries,
            List<SynonymDictionary> spellingDictionaries, boolean skipKeywords, ContextualContentProvider storeContentSource, ContentKey storeKey, boolean isPrimaryHomeKeywordsEnabled,
            boolean isSearchRecurseParentAttributesEnabled) {
        this.contentNodes = contentNodes;
        this.indexes = indexes;
        this.synonymDictionaries = synonymDictionaries;
        this.spellingDictionaries = spellingDictionaries;
        this.skipKeywords = skipKeywords;
        this.primaryHomeKeywordsEnabled = isPrimaryHomeKeywordsEnabled;
        this.parentRecursionEnabled = isSearchRecurseParentAttributesEnabled;
        this.storeContentSource = storeContentSource;
        this.storeKey = storeKey;
    }

    @Override
    public Iterator<DictionaryItem> getWordsIterator() {
        return new PermuterIterator();
    }

    private class PermuterIterator implements Iterator<DictionaryItem> {

        private int nodeIndex = 0;
        private int termIndex = 0;
        private Iterator<Map.Entry<ContentKey, Map<Attribute, Object>>> nodeIterator = contentNodes.entrySet().iterator();
        private Iterator<String> valuesIterator;
        private Iterator<DictionaryItem> dictionaryItemIterator;
        private DictionaryItem currentItem;

        public PermuterIterator() {
            super();
            currentItem = nextTerm();
        }

        @Override
        public boolean hasNext() {
            return currentItem != null;
        }

        @Override
        public DictionaryItem next() {
            DictionaryItem item = currentItem;
            if (hasNext()) {
                currentItem = nextTerm();
                return item;
            } else {
                throw new NoSuchElementException();
            }
        }

        private DictionaryItem nextTerm() {
            if (dictionaryItemIterator == null)
                dictionaryItemIterator = nextTermIterator();

            while (dictionaryItemIterator != null) {
                if (dictionaryItemIterator.hasNext()) {
                    termIndex++;
                    return dictionaryItemIterator.next();
                }
                dictionaryItemIterator = nextTermIterator();
            }
            return null;
        }

        private Iterator<DictionaryItem> nextTermIterator() {
            if (valuesIterator == null)
                valuesIterator = nextValuesIterator();

            while (valuesIterator != null) {
                if (valuesIterator.hasNext())
                    return createPermutation(valuesIterator.next()).iterator();
                valuesIterator = nextValuesIterator();
            }
            return null;
        }

        private Iterator<String> nextValuesIterator() {
            if (nodeIterator.hasNext()) {
                if (nodeIndex % 10000 == 0)
                    LOGGER.debug("processed " + nodeIndex + " nodes (" + termIndex + " terms) so far");
                nodeIndex++;
                Map.Entry<ContentKey, Map<Attribute, Object>> nextItem = nodeIterator.next();
                return getValues(nextItem.getKey(), nextItem.getValue()).iterator();
            } else {
                LOGGER.info("completed processing total of " + nodeIndex + " nodes (" + termIndex + " terms)");
                return null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private List<String> getValues(ContentKey nodeKey, Map<Attribute, Object> nodeAttributesWithValues) {
            List<AttributeIndex> indexes = getSpelledIndexes(nodeKey, nodeAttributesWithValues);
            List<String> values = new ArrayList<String>();
            for (AttributeIndex index : indexes) {
                List<String> collectedValues = IndexerUtil.collectValues(nodeKey, nodeAttributesWithValues, index, !skipKeywords, primaryHomeKeywordsEnabled,
                        parentRecursionEnabled, storeContentSource, storeKey);
                values.addAll(collectedValues);
            }
            return values;
        }

        private List<AttributeIndex> getSpelledIndexes(ContentKey nodeKey, Map<Attribute, Object> nodeAttributesWithValues) {
            ContentType type = nodeKey.type;
            List<AttributeIndex> _indexes = indexes.get(type);
            List<AttributeIndex> spelled = new ArrayList<AttributeIndex>();
            if (_indexes != null) {
                for (AttributeIndex index : _indexes)
                    if (index.isSpelled())
                        spelled.add(index);
            }
            return spelled;
        }

        private List<DictionaryItem> createPermutation(String term) {
            String trimmedTerm = term.trim();
            SpellingTermNormalizer spellingTermNormalizer = new SpellingTermNormalizer(false);

            List<DictionaryItem> items = new ArrayList<DictionaryItem>();
            term = spellingTermNormalizer.convert(trimmedTerm);
            if (synonymDictionaries != null) {
                for (SynonymDictionary synonymDictionary : synonymDictionaries) {
                    for (String word : synonymDictionary.getSynonyms().keySet()) {
                        if (word.contains(term)) {
                            items.add(new DictionaryItem(purify(term), purify(word)));
                        }
                    }
                }
            }

            if (spellingDictionaries != null) {
                for (SynonymDictionary spellingDictionary : spellingDictionaries) {
                    for (String word : spellingDictionary.getSynonyms().keySet()) {
                        if (word.contains(term)) {
                            items.add(new DictionaryItem(purify(term), purify(word), true));
                        }
                    }
                }

            }

            if (items.isEmpty()) {
                items.add(new DictionaryItem(purify(term), term));
            }
            return items;
        }

        private String purify(String toPurify) {
            toPurify = toPurify.replaceAll("-", "");
            return toPurify.replaceAll("'", "");
        }

    }
}
