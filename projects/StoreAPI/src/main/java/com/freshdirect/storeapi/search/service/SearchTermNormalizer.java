package com.freshdirect.storeapi.search.service;

import com.freshdirect.storeapi.search.converter.AmpersandConverter;
import com.freshdirect.storeapi.search.converter.ApostropheConverter;
import com.freshdirect.storeapi.search.converter.CommaConverter;
import com.freshdirect.storeapi.search.converter.DashAsterixConverter;
import com.freshdirect.storeapi.search.converter.DiacriticsRemover;
import com.freshdirect.storeapi.search.converter.HtmlUnescapeConverter;
import com.freshdirect.storeapi.search.converter.InitialismConverter;
import com.freshdirect.storeapi.search.converter.IsoToLatin1Converter;
import com.freshdirect.storeapi.search.converter.LowerCaseConverter;
import com.freshdirect.storeapi.search.converter.NumberConverter;
import com.freshdirect.storeapi.search.converter.PercentConverter;
import com.freshdirect.storeapi.search.converter.PlusSignConverter;
import com.freshdirect.storeapi.search.converter.PunctuationConverter;
import com.freshdirect.storeapi.search.converter.QuotationConverter;
import com.freshdirect.storeapi.search.converter.SlashConverter;
import com.freshdirect.storeapi.term.service.TermNormalizer;

public class SearchTermNormalizer implements TermNormalizer {

    private boolean retainPunctuation;

    private HtmlUnescapeConverter htmlUnsecapeConverter = new HtmlUnescapeConverter();
    private IsoToLatin1Converter isoLatin1Converter = new IsoToLatin1Converter();
    private DiacriticsRemover diacriticsRemover = new DiacriticsRemover();
    private LowerCaseConverter lowerCaseConverter = new LowerCaseConverter();
    private InitialismConverter initialismConverter = new InitialismConverter();
    private PunctuationConverter punctuationConverter = new PunctuationConverter();
    private QuotationConverter quotationConverter = new QuotationConverter();
    private PercentConverter percentConverter = new PercentConverter();
    private AmpersandConverter ampersandConverter = new AmpersandConverter();
    private PlusSignConverter plusSignConverter = new PlusSignConverter();
    private SlashConverter slashConverter = new SlashConverter();
    private ApostropheConverter apostropheConverter = new ApostropheConverter();
    private DashAsterixConverter dashAsterixConverter = new DashAsterixConverter();
    private NumberConverter numberConverter = new NumberConverter();
    private CommaConverter commaConverter = new CommaConverter();

    public SearchTermNormalizer(boolean retainPunctuation) {
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
        input = punctuationConverter.convert(input, retainPunctuation);
        input = commaConverter.convert(input);
        input = quotationConverter.convert(input);
        input = percentConverter.convert(input);
        input = ampersandConverter.convert(input);
        input = plusSignConverter.convert(input);
        input = slashConverter.convert(input);
        input = apostropheConverter.convert(input);
        input = dashAsterixConverter.convert(input);
        input = numberConverter.convert(input);

        return input;
    }
}
