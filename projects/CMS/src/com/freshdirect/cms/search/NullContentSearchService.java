package com.freshdirect.cms.search;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.search.spell.SpellingHit;
import com.freshdirect.framework.util.log.LoggerFactory;

public class NullContentSearchService implements ContentSearchServiceI {
	public static Logger LOGGER = LoggerFactory.getInstance(NullContentSearchService.class.getSimpleName());

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Collection<SearchHit> search(String query, boolean exact, int maxHits) {
		LOGGER.info("search() called");
		
		return Collections.emptySet();
	}

	@Override
	public Collection<SearchHit> searchProducts(String query, boolean exact,
			boolean approximate, int maxHits) {
		LOGGER.info("search() called");

		return Collections.emptySet();
	}

	@Override
	public Collection<SearchHit> searchFaqs(String query, boolean exact,
			int maxHits) {
		LOGGER.info("searchFaqs() called");

		return Collections.emptySet();
	}

	@Override
	public Collection<SearchHit> searchRecipes(String query, boolean exact,
			int maxHits) {
		LOGGER.info("searchRecipes() called");

		return Collections.emptySet();
	}

	@Override
	public Collection<SpellingHit> suggestSpelling(String query,
			double threshold, int maxHits) {
		LOGGER.info("suggestSpelling() called");

		return Collections.emptySet();
	}

	@Override
	public Collection<SpellingHit> reconstructSpelling(String query,
			double threshold, int maxHits) {
		LOGGER.info("reconstructSpelling() called");

		return Collections.emptySet();
	}

	@Override
	public void index(Collection<ContentNodeI> contentNodes, boolean rebuild) {
		LOGGER.info("index() called");
	}

	@Override
	public void indexSpelling(Collection<ContentNodeI> contentNodes) {
		LOGGER.info("indexSpelling() called");

	}

	@Override
	public void optimize() {
		LOGGER.info("optimize() called");

	}

	@Override
	public Set<ContentType> getIndexedTypes() {
		LOGGER.info("getIndexedTypes() called");

		return Collections.emptySet();
	}

	@Override
	public List<AttributeIndex> getIndexesForType(ContentType type) {
		LOGGER.info("getIndexesForType() called");

		return Collections.emptyList();
	}

	@Override
	public IndexReader getReader() throws IOException {
		LOGGER.info("getReader() called");

		return null;
	}

	@Override
	public void closeReader() {
		LOGGER.info("closeReader() called");

	}

	@Override
	public Analyzer getAnalyzer() {
		LOGGER.info("getAnalyzer() called");
		return null;
	}

	@Override
	public SpellingSuggestionsServiceI getSpellService() {
		LOGGER.info("getSpellService() called");
		return null;
	}

	@Override
	public void setSynonymsDisabled(boolean synonymsDisabled) {
		LOGGER.info("setSynonymsDisabled() called");
	}

	@Override
	public boolean isSynonymsDisabled() {
		LOGGER.info("isSynonymsDisabled() called");

		return false;
	}

	@Override
	public void setKeywordsDisabled(boolean keywordsDisabled) {
		LOGGER.info("setKeywordsDisabled() called");
	}

	@Override
	public boolean isKeywordsDisabled() {
		LOGGER.info("isKeywordsDisabled() called");

		return false;
	}

	@Override
	public List<SpellingHit> suggestSpellingInternal(String searchTerm,
			double threshold, int maxHits) {
		LOGGER.info("suggestSpellingInternal() called");

		return Collections.emptyList();
	}

	@Override
	public List<List<SpellingHit>> generateSpellingParticles(
			List<String> searchPhrase, double threshold, int maxHits) {
		LOGGER.info("generateSpellingParticles() called");

		return Collections.emptyList();
	}

}
