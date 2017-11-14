package com.freshdirect.cms.lucene.domain;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class FreshdirectAnalyzer extends Analyzer {

    private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);

    @Override
    public TokenStream tokenStream(String field, Reader reader) {
        if (field.startsWith(IndexingConstants.NAME_PREFIX)) {
            WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(reader);
            TokenStream stemmer = new SnowballFilter(tokenizer, "English");
            return stemmer;
        } else if (field.startsWith(IndexingConstants.TEXT_PREFIX)) {
            return new SnowballFilter(analyzer.tokenStream(field, reader), "English");
        } else if (field.startsWith("__")) {
            return new KeywordTokenizer(reader);
        } else {
            WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(reader);
            return tokenizer;
        }
    }
}
