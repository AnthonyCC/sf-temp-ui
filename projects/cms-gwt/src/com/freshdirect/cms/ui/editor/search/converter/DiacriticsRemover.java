package com.freshdirect.cms.ui.editor.search.converter;

public class DiacriticsRemover {

    public String convert(String term) {
        StringBuilder buf = new StringBuilder(term.length() + term.length() / 4);
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            switch (c) {
                case '\u00c0':
                case '\u00c1':
                case '\u00c2':
                case '\u00c3':
                case '\u00c4':
                case '\u00c5':
                    buf.append('A');
                    break;
                case '\u00c6':
                    buf.append("Ae");
                    break;
                case '\u00c7':
                    buf.append('C');
                    break;
                case '\u00c8':
                case '\u00c9':
                case '\u00ca':
                case '\u00cb':
                    buf.append('E');
                    break;
                case '\u00cc':
                case '\u00cd':
                case '\u00ce':
                case '\u00cf':
                    buf.append('I');
                    break;
                case '\u00d0':
                    buf.append('D');
                    break;
                case '\u00d1':
                    buf.append('N');
                    break;
                case '\u00d2':
                case '\u00d3':
                case '\u00d4':
                case '\u00d5':
                case '\u00d6':
                case '\u00d8':
                    buf.append('O');
                    break;
                case '\u00d9':
                case '\u00da':
                case '\u00db':
                case '\u00dc':
                    buf.append('U');
                    break;
                case '\u00dd':
                    buf.append('Y');
                    break;
                case '\u00de':
                    buf.append("Th");
                    break;
                case '\u00df':
                    buf.append("ss");
                    break;

                case '\u00e0':
                case '\u00e1':
                case '\u00e2':
                case '\u00e3':
                case '\u00e4':
                case '\u00e5':
                    buf.append('a');
                    break;
                case '\u00e6':
                    buf.append("ae");
                    break;
                case '\u00e7':
                    buf.append('c');
                    break;
                case '\u00e8':
                case '\u00e9':
                case '\u00ea':
                case '\u00eb':
                    buf.append('e');
                    break;
                case '\u00ec':
                case '\u00ed':
                case '\u00ee':
                case '\u00ef':
                    buf.append('i');
                    break;
                case '\u00f0':
                    buf.append('d');
                    break;
                case '\u00f1':
                    buf.append('n');
                    break;
                case '\u00f2':
                case '\u00f3':
                case '\u00f4':
                case '\u00f5':
                case '\u00f6':
                case '\u00f8':
                    buf.append('o');
                    break;
                case '\u00f9':
                case '\u00fa':
                case '\u00fb':
                case '\u00fc':
                    buf.append('u');
                    break;
                case '\u00fd':
                    buf.append('y');
                    break;
                case '\u00fe':
                    buf.append("th");
                    break;
                case '\u00ff':
                    buf.append("y");
                    break;

                default:
                    buf.append(c);
                    break;
            }
        }
        return buf.toString();
    }
}
