package com.freshdirect.storeapi.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.stereotype.Service;

@Service
public class TermTokenizer {

    public static final String DEFAULT_TOKENIZERS = " \t\n\r\f";
    public static final String DEFAULT_JOIN_SEPARATOR = " ";

    public List<String> tokenize(String toTokenize, String delimiter) {
        List<String> tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(toTokenize, delimiter);
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }

}
