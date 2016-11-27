/*
 * Created on Aug 2, 2005
 */
package com.freshdirect.cms.search;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

/**
 * A filter that replaces accented characters in the ISO Latin 1 character set
 * by their unaccented equivalent. The case will not be altered.
 * <p>
 * For instance, '&agrave;' will be replaced by 'a'.
 * <p>
 * 
 * See original at: <blockquote>
 * 
 * <pre>
 *     <a href="http://svn.apache.org/repos/asf/lucene/java/trunk/contrib/analyzers/src/java/org/apache/lucene/analysis/ISOLatin1AccentFilter.java">http://svn.apache.org/repos/asf/lucene/java/trunk/contrib/analyzers/src/java/org/apache/lucene/analysis/ISOLatin1AccentFilter.java</a>
 * </pre>
 * 
 * </blockquote>
 */
public class ISOLatin1AccentFilter extends TokenFilter {

    private TermAttribute termAtt;

    public ISOLatin1AccentFilter(TokenStream input) {
        super(input);
        this.termAtt = ((TermAttribute) addAttribute(TermAttribute.class));
    }

    public final boolean incrementToken() throws IOException {
        if (this.input.incrementToken()) {
            char[] buffer = this.termAtt.termBuffer();
            int length = this.termAtt.termLength();

            for (int i = 0; i < length; ++i) {
                char c = buffer[i];
                if ((c >= 192) && (c <= 64262)) {
                    String x = removeAccents(buffer, length);
                    this.termAtt.setTermBuffer(x);
                    break;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * To replace accented characters in a String by unaccented equivalents.
     */
    public final static String removeAccents(char[] buffer, int length) {
        final StringBuilder output = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            switch (buffer[i]) {
            case '\u00C0': // À
            case '\u00C1': // 
            case '\u00C2': // Â
            case '\u00C3': // Ã
            case '\u00C4': // Ä
            case '\u00C5': // Å
                output.append('A');
                break;
            case '\u00C6': // Æ
                output.append("AE");
                break;
            case '\u00C7': // Ç
                output.append('C');
                break;
            case '\u00C8': // È
            case '\u00C9': // É
            case '\u00CA': // Ê
            case '\u00CB': // Ë
                output.append('E');
                break;
            case '\u00CC': // Ì
            case '\u00CD': // 
            case '\u00CE': // Î
            case '\u00CF': // 
                output.append('I');
                break;
            case '\u00D0': // 
                output.append('D');
                break;
            case '\u00D1': // Ñ
                output.append('N');
                break;
            case '\u00D2': // Ò
            case '\u00D3': // Ó
            case '\u00D4': // Ô
            case '\u00D5': // Õ
            case '\u00D6': // Ö
            case '\u00D8': // Ø
                output.append('O');
                break;
            case '\u0152': // Œ
                output.append("OE");
                break;
            case '\u00DE': // Þ
                output.append("TH");
                break;
            case '\u00D9': // Ù
            case '\u00DA': // Ú
            case '\u00DB': // Û
            case '\u00DC': // Ü
                output.append('U');
                break;
            case '\u00DD': // 
            case '\u0178': // Ÿ
                output.append('Y');
                break;
            case '\u00E0': // 
            case '\u00E1': // á
            case '\u00E2': // â
            case '\u00E3': // ã
            case '\u00E4': // ä
            case '\u00E5': // å
                output.append('a');
                break;
            case '\u00E6': // æ
                output.append("ae");
                break;
            case '\u00E7': // ç
                output.append('c');
                break;
            case '\u00E8': // è
            case '\u00E9': // é
            case '\u00EA': // ê
            case '\u00EB': // ë
                output.append('e');
                break;
            case '\u00EC': // ì
            case '\u00ED': // í
            case '\u00EE': // î
            case '\u00EF': // ï
                output.append('i');
                break;
            case '\u00F0': // ð
                output.append('d');
                break;
            case '\u00F1': // ñ
                output.append('n');
                break;
            case '\u00F2': // ò
            case '\u00F3': // ó
            case '\u00F4': // ô
            case '\u00F5': // õ
            case '\u00F6': // ö
            case '\u00F8': // ø
                output.append('o');
                break;
            case '\u0153': // œ
                output.append("oe");
                break;
            case '\u00DF': // ß
                output.append("ss");
                break;
            case '\u00FE': // þ
                output.append("th");
                break;
            case '\u00F9': // ù
            case '\u00FA': // ú
            case '\u00FB': // û
            case '\u00FC': // ü
                output.append('u');
                break;
            case '\u00FD': // ý
            case '\u00FF': // ÿ
                output.append('y');
                break;
            default:
                output.append(buffer[i]);
                break;
            }
        }
        return output.toString();
    }

}
