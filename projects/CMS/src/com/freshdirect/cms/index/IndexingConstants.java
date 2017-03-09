package com.freshdirect.cms.index;

import org.apache.lucene.index.IndexWriter.MaxFieldLength;

public class IndexingConstants {

    public final static String FIELD_CONTENT_KEY = "__contentKey_";

    public final static String FIELD_CONTENT_TYPE = "__contentType_";

    public final static String FIELD_CONTENT_ID = "__contentId_";

    public final static String FIELD_PREFIX = "__";

    public final static String NAME_PREFIX = "_name_";

    public final static String TEXT_PREFIX = "_text_";

    public final static String FULL_CONTENT = "FULL_CONTENT";

    public static final String F_SEARCH_TERM = "search_term";

    public static final String F_SPELLING_TERM = "spelling_term";

    public static final String F_IS_SYNONYM = "is_synonym";

    public static final org.apache.lucene.index.Term F_WORD_TERM = new org.apache.lucene.index.Term(F_SPELLING_TERM);

    public static final FreshdirectAnalyzer ANALYZER = new FreshdirectAnalyzer();

    public static final MaxFieldLength MAX_FIELD_LENGTH_1024 = new MaxFieldLength(1024);

}
