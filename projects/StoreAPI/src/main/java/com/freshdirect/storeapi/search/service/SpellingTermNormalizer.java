package com.freshdirect.storeapi.search.service;

import com.freshdirect.storeapi.search.converter.AmpersandConverter;
import com.freshdirect.storeapi.search.converter.ApostropheConverter;
import com.freshdirect.storeapi.search.converter.CommaConverter;
import com.freshdirect.storeapi.search.converter.DashAsterixConverter;
import com.freshdirect.storeapi.search.converter.DiacriticsRemover;
import com.freshdirect.storeapi.search.converter.DotConverter;
import com.freshdirect.storeapi.search.converter.HtmlUnescapeConverter;
import com.freshdirect.storeapi.search.converter.IsoToLatin1Converter;
import com.freshdirect.storeapi.search.converter.LowerCaseConverter;
import com.freshdirect.storeapi.search.converter.QuotationConverter;
import com.freshdirect.storeapi.search.converter.SlashConverter;
import com.freshdirect.storeapi.search.converter.SpellingInitialismConverter;
import com.freshdirect.storeapi.search.converter.SpellingPunctuationConverter;
import com.freshdirect.storeapi.term.service.TermNormalizer;

public class SpellingTermNormalizer implements TermNormalizer {

    private boolean retainPunctuation;

    private HtmlUnescapeConverter htmlUnsecapeConverter = new HtmlUnescapeConverter();
    private IsoToLatin1Converter isoLatin1Converter = new IsoToLatin1Converter();
    private DiacriticsRemover diacriticsRemover = new DiacriticsRemover();
    private LowerCaseConverter lowerCaseConverter = new LowerCaseConverter();
    private SpellingInitialismConverter initialismConverter = new SpellingInitialismConverter();
    private SpellingPunctuationConverter punctuationConverter = new SpellingPunctuationConverter();
    private QuotationConverter quotationConverter = new QuotationConverter();
    private AmpersandConverter ampersandConverter = new AmpersandConverter();
    private SlashConverter slashConverter = new SlashConverter();
    private ApostropheConverter apostropheConverter = new ApostropheConverter();
    private DashAsterixConverter dashAsterixConverter = new DashAsterixConverter();
    private CommaConverter commaConverter = new CommaConverter();
    private DotConverter dotConverter = new DotConverter();

    public SpellingTermNormalizer(boolean retainPunctuation) {
        this.retainPunctuation = retainPunctuation;
    }

    @Override
    public String convert(String input) {
        input = input.trim();
        input = htmlUnsecapeConverter.convert(input);
        input = isoLatin1Converter.convert(input);
        input = diacriticsRemover.convert(input);
        input = lowerCaseConverter.convert(input);
        input = initialismConverter.convert(input);
        input = commaConverter.convert(input);
        input = dotConverter.convert(input);
        input = punctuationConverter.convert(input, retainPunctuation);
        input = quotationConverter.convert(input);

        input = ampersandConverter.convert(input);

        input = slashConverter.convert(input);
        input = apostropheConverter.convert(input);
        input = dashAsterixConverter.convert(input);
        return input;
    }
}
