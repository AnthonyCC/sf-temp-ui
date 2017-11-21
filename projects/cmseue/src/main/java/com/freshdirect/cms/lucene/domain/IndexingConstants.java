package com.freshdirect.cms.lucene.domain;

import org.apache.lucene.index.IndexWriter.MaxFieldLength;

public final class IndexingConstants {

    public static final String FIELD_CONTENT_KEY = "__contentKey_";

    public static final String FIELD_CONTENT_TYPE = "__contentType_";

    public static final String FIELD_CONTENT_ID = "__contentId_";

    public static final String FIELD_PREFIX = "__";

    public static final String NAME_PREFIX = "_name_";

    public static final String TEXT_PREFIX = "_text_";

    public static final String FULL_CONTENT = "FULL_CONTENT";

    public static final String F_SEARCH_TERM = "search_term";

    public static final String F_SPELLING_TERM = "spelling_term";

    public static final String F_IS_SYNONYM = "is_synonym";

    public static final org.apache.lucene.index.Term F_WORD_TERM = new org.apache.lucene.index.Term(F_SPELLING_TERM);

    public static final FreshdirectAnalyzer ANALYZER = new FreshdirectAnalyzer();

    public static final MaxFieldLength MAX_FIELD_LENGTH_1024 = new MaxFieldLength(1024);

    public static final int DICTIONARY_WRITER_RAM_BUFFER_SIZE = 50;

    public static final int DICTIONARY_WRITER_MERGE_FACTOR = 20;

    public static final String KEYWORD = "keyword";

    private IndexingConstants() {
    }
}
