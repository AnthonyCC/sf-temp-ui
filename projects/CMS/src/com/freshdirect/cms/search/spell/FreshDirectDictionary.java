package com.freshdirect.cms.search.spell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.search.AttributeIndex;
import com.freshdirect.cms.search.SearchUtils;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.search.term.DictionaryPermuter;
import com.freshdirect.cms.search.term.IdentityConv;
import com.freshdirect.cms.search.term.SpellingTermNormalizer;
import com.freshdirect.cms.search.term.SynonymPermuter;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.cms.search.term.TermCoder;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FreshDirectDictionary implements Dictionary {
	private final static Logger LOGGER = LoggerFactory.getInstance(FreshDirectDictionary.class);
	private Collection<ContentNodeI> contentNodes;
	private Map<ContentType, List<AttributeIndex>> indexes;
	private List<SynonymDictionary> synonymDictionaries;
	private List<SynonymDictionary> spellingDictionaries;
	private final boolean skipKeywords;
	private final boolean primaryHomeKeywordsEnabled;
	private final boolean parentRecursionEnabled;
	private final StoreContentSource storeContentSource;

	public FreshDirectDictionary(Collection<ContentNodeI> contentNodes, Map<ContentType, List<AttributeIndex>> indexes,
			List<SynonymDictionary> synonymDictionaries, List<SynonymDictionary> spellingDictionaries, boolean skipKeywords, StoreContentSource storeContentSource) {
		this.contentNodes = Collections.unmodifiableCollection(contentNodes);
		this.indexes = indexes;
		this.synonymDictionaries = synonymDictionaries;
		this.spellingDictionaries = spellingDictionaries;
		this.skipKeywords = skipKeywords;
		this.primaryHomeKeywordsEnabled = FDStoreProperties.isPrimaryHomeKeywordsEnabled();
		this.parentRecursionEnabled = FDStoreProperties.isSearchRecurseParentAttributesEnabled();
		this.storeContentSource = storeContentSource;
	}

	@Override
	public Iterator<DictionaryItem> getWordsIterator() {
		return new PermuterIterator();
	}

	private class PermuterIterator implements Iterator<DictionaryItem> {
		private int nodeIndex = 0;
		private int termIndex = 0;
		private Iterator<ContentNodeI> nodeIterator = contentNodes.iterator();
		private Iterator<Term> valuesIterator;
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

		private Iterator<Term> nextValuesIterator() {
			if (nodeIterator.hasNext()) {
				if (nodeIndex % 10000 == 0)
					LOGGER.debug("processed " + nodeIndex + " nodes (" + termIndex + " terms) so far");
				nodeIndex++;
				return getValues(nodeIterator.next()).iterator();
			} else {
				LOGGER.info("completed processing total of " + nodeIndex + " nodes (" + termIndex + " terms)");
				return null;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		private List<Term> getValues(ContentNodeI node) {
			List<AttributeIndex> indexes = getSpelledIndexes(node);
			List<Term> values = new ArrayList<Term>();
			for (AttributeIndex index : indexes) {
				List<Term> collectedValues = SearchUtils.collectValues(node, index, !skipKeywords, primaryHomeKeywordsEnabled,
						parentRecursionEnabled, storeContentSource);
				values.addAll(collectedValues);
			}
			return values;
		}
	
		private List<AttributeIndex> getSpelledIndexes(ContentNodeI node) {
			ContentType type = node.getKey().getType();
			List<AttributeIndex> _indexes = indexes.get(type);
			List<AttributeIndex> spelled = new ArrayList<AttributeIndex>();
			if (_indexes != null) {
				for (AttributeIndex index : _indexes)
					if (index.isSpelled())
						spelled.add(index);
			}
			return spelled;
		}

		private List<DictionaryItem> createPermutation(Term term) {
			TermCoder filter = new SpellingTermNormalizer(term);
			if (synonymDictionaries != null)
				for (SynonymDictionary synonyms : synonymDictionaries)
					filter = new SynonymPermuter(synonyms, filter);
			filter = new DictionaryPermuter(filter);
			List<Term> terms = filter.getTerms();
			List<DictionaryItem> items = new ArrayList<DictionaryItem>(terms.size());
			for (Term t : terms) {
				if (spellingDictionaries != null) {
					filter = new IdentityConv(t);
					for (SynonymDictionary synonyms : spellingDictionaries)
						filter = new SynonymPermuter(synonyms, filter);
					Iterator<Term> it = filter.getTerms().iterator();
					if (it.hasNext()) {
						Term t1 = it.next();
						items.add(new DictionaryItem(CsongorDistance.purify(t1.toString()), t1.toString()));
					}
					// spelling synonyms
					while (it.hasNext()) {
						Term t1 = it.next();
						items.add(new DictionaryItem(CsongorDistance.purify(t1.toString()), t.toString(), true));
					}
				} else
					items.add(new DictionaryItem(CsongorDistance.purify(t.toString()), t.toString()));
			}
			return items;
		}
	}
}
