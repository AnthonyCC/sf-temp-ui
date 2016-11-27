 // Copyright (c) 2004 FreshDirect, LLC, All rights reserved.

package com.freshdirect.framework.util;

import java.text.*;
import java.util.*;

/**
 * A formatter that populates template strings from a Map.
 *
 * @author Chia-Lin Wang, CapitalThinking, Inc.
 * @version $Id: HashMessageFormat.java,v 1.4 2003/07/16 16:21:20 cwang Exp $
 */
public class HashMessageFormat extends Format {
    /**
     * Construct from a pattern using the default locale
     */
	
	public static String DEF_NULL_STRING = "null";
	
    public HashMessageFormat(String pattern) {
        this(pattern, DEF_NULL_STRING);
    }

    public HashMessageFormat(String pattern, String nullString) {
        this(pattern, Locale.getDefault());
        this.nullString = nullString;
    }

    public HashMessageFormat(String pattern, boolean nullArgs) {
        this(pattern, DEF_NULL_STRING);
    }

    public HashMessageFormat(String pattern, boolean nullArgs, String nullString) {
        this(pattern, Locale.getDefault());
        setNullArgs(nullArgs);
        this.nullString = nullString;
    }

    /**
     * Construct from the specified pattern and locale (not that
     * the locale is used for anything...
     */
    public HashMessageFormat(String pattern, Locale locale) {
        setLocale(locale);
        applyPattern(pattern);
    }

    public static String[] getMessageParams(String pattern) {
        HashMessageFormat temp = getHashMessageFormat(pattern,true);
        return temp.getMessageParams();
    }

    /**
     * Formats the specified pattern with the specified arguments.
     */
    public static String format(String pattern,
                                Map arguments,
                                boolean nullArgs) {
        HashMessageFormat temp = getHashMessageFormat(pattern,nullArgs);
        return temp.format(arguments);
    }

    public static String format(String pattern,
            Map arguments,
            boolean nullArgs,
			String nullString) {
    	HashMessageFormat temp = getHashMessageFormat(pattern,nullArgs, nullString);
    	return temp.format(arguments);
    }

    public static String format(String pattern,
                                Map arguments) {
        HashMessageFormat temp = getHashMessageFormat(pattern,true);
        return temp.format(arguments);
    }

    public String format(Object arguments,
                         boolean nullArgs) {
        this.nullArgs = nullArgs;
        return format(arguments);
    }

    //overwrite//
    public final StringBuffer format(Object source,
                                     StringBuffer result,
                                     FieldPosition ignore) {
        return format(source, result, ignore, 0);
    }

    //overwrite//
    public Object parseObject(String text, ParsePosition status) {
        return null;
    }

    private void applyPattern(String newPattern) {
        StringBuffer[] segments = new StringBuffer[4];

        for (int i = 0; i < segments.length; ++i) {
            segments[i] = new StringBuffer();
        }

        int part = 0;
        int formatNumber = 0;
        int braceStack = 0;
        maxOffset = -1;

        for (int i = 0; i < newPattern.length(); ++i) {
            char ch = newPattern.charAt(i);
             if (part == 0) {
                if (ch == '{') {
                    part = 1;
                } else {
                    segments[part].append(ch);
                }
            } else { 
                switch (ch) {
                    case ',':
                        if (part < 3)
                            part += 1;
                        else
                            segments[part].append(ch);
                        break;
                    case '{':
                        ++braceStack;
                        segments[part].append(ch);
                        break;
                    case '}':
                        if (braceStack == 0) {
                            part = 0;
                            makeFormat(i, formatNumber, segments);
                            formatNumber++;
                        } else {
                            --braceStack;
                            segments[part].append(ch);
                        }
                        break;
                    default:
                        segments[part].append(ch);
                        break;
                }
             }
        }

        if (braceStack == 0 && part != 0) {
            maxOffset = -1;
            throw new IllegalArgumentException("Unmatched braces in the pattern.");
        }
        pattern = segments[0].toString();
        //Logger.logDebug("***********pattern:"+pattern);
    }

    private static final String[] typeList =
            {"", "", "number", "", "date", "", "time", "", "choice"};
    private static final String[] modifierList =
            {"", "", "currency", "", "percent", "", "integer"};
    private static final String[] dateModifierList =
            {"", "", "short", "", "medium", "", "long", "", "full"};

    private void makeFormat(int position, int offsetNumber,
                            StringBuffer[] segments) {
        int oldMaxOffset = maxOffset;
        maxOffset = offsetNumber;

        offsets.add(offsetNumber, new Integer(segments[0].length()));
        strArguments.add(offsetNumber, segments[1].toString());
        messageParams.add(segments[1].toString());

        Format newFormat = null;
        switch (findKeyword(segments[2].toString(), typeList)) {
            case 0:
                break;
            case 1:
            case 2:// number
                switch (findKeyword(segments[3].toString(), modifierList)) {
                    case 0: // default;
                        newFormat = NumberFormat.getInstance(locale);
                        break;
                    case 1:
                    case 2:// currency
                        newFormat = NumberFormat.getCurrencyInstance(locale);
                        break;
                    case 3:
                    case 4:// percent
                        newFormat = NumberFormat.getPercentInstance(locale);
                        break;
                    case 5:
                    case 6:// integer
                        newFormat = getIntegerFormat(locale);
                        break;
                    default: // pattern
                        newFormat = NumberFormat.getInstance(locale);
                        try {
                            ((DecimalFormat) newFormat).applyPattern(segments[3].toString());
                        } catch (Exception e) {
                            maxOffset = oldMaxOffset;
                            throw new IllegalArgumentException(
                                    "Pattern incorrect or locale does not support formats, error at ");
                        }
                        break;
                }
                break;
            case 3:
            case 4: // date
                switch (findKeyword(segments[3].toString(), dateModifierList)) {
                    case 0: // default
                        newFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
                        break;
                    case 1:
                    case 2: // short
                        newFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                        break;
                    case 3:
                    case 4: // medium
                        newFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
                        break;
                    case 5:
                    case 6: // long
                        newFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
                        break;
                    case 7:
                    case 8: // full
                        newFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
                        break;
                    default:
                        newFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
                        try {
                            ((SimpleDateFormat) newFormat).applyPattern(segments[3].toString());
                        } catch (Exception e) {
                            maxOffset = oldMaxOffset;
                            throw new IllegalArgumentException(
                                    "Pattern incorrect or locale does not support formats, error at ");
                        }
                        break;
                }
                break;
            case 5:
            case 6:// time
                switch (findKeyword(segments[3].toString(), dateModifierList)) {
                    case 0: // default
                        newFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
                        break;
                    case 1:
                    case 2: // short
                        newFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
                        break;
                    case 3:
                    case 4: // medium
                        newFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
                        break;
                    case 5:
                    case 6: // long
                        newFormat = DateFormat.getTimeInstance(DateFormat.LONG, locale);
                        break;
                    case 7:
                    case 8: // full
                        newFormat = DateFormat.getTimeInstance(DateFormat.FULL, locale);
                        break;
                    default:
                        newFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, locale);
                        try {
                            ((SimpleDateFormat) newFormat).applyPattern(segments[3].toString());
                        } catch (Exception e) {
                            maxOffset = oldMaxOffset;
                            throw new IllegalArgumentException(
                                    "Pattern incorrect or locale does not support formats, error at ");
                        }
                        break;
                }
                break;
            case 7:
            case 8:// choice
                try {
                    newFormat = new ChoiceFormat(segments[3].toString());
                } catch (Exception e) {
                    maxOffset = oldMaxOffset;
                    throw new IllegalArgumentException(
                            "Choice Pattern incorrect, error at ");
                }
                break;
            default:
                maxOffset = oldMaxOffset;
                throw new IllegalArgumentException("unknown format type at ");
        }
        formats.add(offsetNumber, newFormat);
        segments[1].setLength(0);   // throw away other segments
        segments[2].setLength(0);
        segments[3].setLength(0);
    }

    private static final int findKeyword(String s, String[] list) {
        s = s.trim().toLowerCase();
        for (int i = 0; i < list.length; ++i) {
            if (s.equals(list[i]))
                return i;
        }
        return -1;
    }

    NumberFormat getIntegerFormat(Locale locale) {
        NumberFormat temp = NumberFormat.getInstance(locale);
        if (temp instanceof DecimalFormat) {
            DecimalFormat temp2 = (DecimalFormat) temp;
            temp2.setMaximumFractionDigits(0);
            temp2.setDecimalSeparatorAlwaysShown(false);
            temp2.setParseIntegerOnly(true);
        }
        return temp;
    }

    private StringBuffer format(Object arguments, StringBuffer result,
                                FieldPosition status, int recursionProtection) {

        if (!(arguments instanceof Map))
            throw new IllegalArgumentException("Incorrect argument type");
        int lastOffset = 0;
        int nextOffset = 0;
        for (int i = 0; i <= maxOffset; ++i) {
            nextOffset = ((Integer) offsets.get(i)).intValue();
            result.append(pattern.substring(lastOffset, nextOffset));
            lastOffset = nextOffset;
            String argument = (String) strArguments.get(i);
            //Logger.logDebug("********argument:"+argument);
            if (arguments == null) {
                result.append("{" + argument + "}");
                continue;
            }
            Object obj = ((Map) arguments).get(argument);
            String arg;
            boolean tryRecursion = false;
            if (obj == null) {
                if (nullArgs || ((Map) arguments).containsKey(argument)) {
                    arg = nullString;
                } else {
                    arg = "{" + argument + "}";
                }
                // Logger.logDebug("########arg is null######:" + arg);
            } else if ((Format) formats.get(i) != null && !"".equals(obj)) {
                arg = ((Format) formats.get(i)).format(obj);
                tryRecursion = ((Format) formats.get(i)) instanceof ChoiceFormat;
            } else if (obj instanceof Number) {
                // no formatting is nessary
                // arg = NumberFormat.getInstance(locale).format(obj);
                arg = String.valueOf(obj);
            } else if (obj instanceof Date) {
                // format a Date if can
                arg = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                        DateFormat.SHORT,
                        locale).format(obj);
            } else if (obj instanceof String) {
                arg = (String) obj;

            } else {
                arg = obj.toString();
                if (arg == null) arg = nullString;
            }

            if (tryRecursion && arg.indexOf('{') >= 0) {
                HashMessageFormat temp = new HashMessageFormat(arg);
                temp.format(arguments, result, status, recursionProtection);
            } else {
                result.append(arg);
            }
        }
        result.append(pattern.substring(lastOffset, pattern.length()));

        return result;
    }

    /**
     *  Cache HashMessageFormat
     *  Can be retrived based on pattern and nullArgs
     */
    private static HashMessageFormat getHashMessageFormat (String pattern,
                                                              boolean nullArgs){
    	return getHashMessageFormat(pattern, nullArgs, DEF_NULL_STRING);
    }

    private static HashMessageFormat getHashMessageFormat (String pattern,
            boolean nullArgs, String nullString){
        HashMessageFormat fmt    = null;
        HashMessageFormat[] fmts =
            (HashMessageFormat[])msgFormatByPatternAndNullArgs.get(pattern);
        if (fmts != null) {
            fmt = (nullArgs) ? fmts[0] : fmts[1];
            if (fmt != null) {
                return fmt;
            }
        }

        synchronized(msgFormatByPatternAndNullArgs) {
            if(fmts == null) {
                fmts = new HashMessageFormat[2];
                msgFormatByPatternAndNullArgs.put(pattern,fmts);
            }
            if(nullArgs) {
               return (fmts[0] = new HashMessageFormat(pattern,true, nullString));
            }else{
               return (fmts[1] = new HashMessageFormat(pattern,false, nullString));
            }
        } 
    }

    /**
     * Specify a locale (which is not used for anything...)
     */
    public void setLocale(Locale theLocale) {
        locale = theLocale;
    }

    public void setFormat(int index, Format newFormat) {
        formats.add(index, newFormat);
    }

    public void setFormats(ArrayList newFormats) {
        try {
            formats = (ArrayList) newFormats.clone();
        } catch (Exception e) {
            return; // should never occur!
        }
    }

    public void setNullArgs(boolean nullArgs) {
        this.nullArgs = nullArgs;
    }

    public void setNullString(String nullString) {
    	this.nullString = nullString;
    }
    public String getNullString() {
    	return nullString;
    }

    public String[] getMessageParams() {
        return (String[]) messageParams.toArray(new String[]{});
    }

    private boolean nullArgs = true;
    private Locale locale;
    private String pattern;
    private ArrayList offsets = new ArrayList(20);
    private ArrayList formats = new ArrayList(20);
    private ArrayList strArguments = new ArrayList(20);
    private int maxOffset = -1;
    private Vector messageParams = new Vector();
    private String nullString = DEF_NULL_STRING;
    
    private static Map msgFormatByPatternAndNullArgs = new HashMap(350);

    public static void main(String[] args) {
    	HashMap m = new HashMap();
    	m.put("arg1", "3");
    	m.put("arg2", "C:");
        String fmt = HashMessageFormat.format
			("The disk {arg2} contains '' file(s).", m);
        
        testInstance();
        testCache();
        testCacheFaster();
        testNull();
        testMessageParams();
    }

    private static void testMessageParams() {
        HashMessageFormat f =
                new HashMessageFormat("The disk \"{arg2}\" contains {arg1} file(s) borrowed from {arg2}.");
        String[] params = f.getMessageParams();
        if(params.length != 3) {
            System.err.println("Should get more params. Test failed.");
        }
        if("arg2".equals(params[0]) &&
             "arg1".equals(params[1]) &&
                "arg2".equals(params[2])) {
            System.out.println("Param test paseed.\n");
        }else{
            System.err.println("Param test failed.\n");
        }
    }

    private static void testNull() {
        String fmt = HashMessageFormat.format
                ("The disk \"{arg2}\" contains {arg1} file(s).", new HashMap());
        System.out.println(fmt);
        if (!"The disk \"null\" contains null file(s).".equals(fmt)) {
            System.err.println("This is no good. Null test failed.\n");
        } else {
            System.out.println("Null test passed.\n");
        }

        fmt = HashMessageFormat.format
                ("The disk \"{arg2}\" contains {arg1} file(s).", new HashMap(),false);
        System.out.println(fmt);
        if (!"The disk \"{arg2}\" contains {arg1} file(s).".equals(fmt)) {
            System.err.println("This is no good. Null test failed.\n");
        } else {
            System.out.println("Null test passed.\n");
        }

        HashMessageFormat hmf = new HashMessageFormat("{arg1}.{arg2}");
        if("null.null".equals(hmf.format(new HashMap(),true))) {
            System.out.println("Null test passed.\n");
        }else {
            System.err.println("Null test failed.\n");
        }

        hmf = new HashMessageFormat("{arg1} . {arg2}", "");
        if(" . ".equals(hmf.format(new HashMap(),true))) {
            System.out.println("Null test passed.\n");
        }else {
            System.err.println("Null test failed.\n");
        }

        hmf = new HashMessageFormat("{arg1}.{arg2}");
        if("{arg1}.{arg2}".equals(hmf.format(new HashMap(),false))) {
            System.out.println("Null test passed.\n");
        }else {
            System.err.println("Null test  failed.\n");
        }
    }

    /**
     * HashMessageFormat.format()
     * should reuse HashMessageFormat instances
     */
    private static void testCache() {
        String pattern = "The disk \"{arg2}\" contains {arg1} file(s).";
        HashMap testArgs = new HashMap();
        testArgs.put("arg1",new Long(3));
        testArgs.put("arg2","MyDisk");

        String fmt = HashMessageFormat.format(pattern,testArgs);
        System.out.println(fmt);
        if(!"The disk \"MyDisk\" contains 3 file(s).".equals(fmt)) {
            System.err.println("This is no good. Instance test failed.\n");
        }else{
            System.out.println("Caching test passed.\n");
        }

        testArgs.put("arg1",new Long(0));
        fmt = HashMessageFormat.format(pattern,testArgs);
        System.out.println(fmt);
        if(!"The disk \"MyDisk\" contains 0 file(s).".equals(fmt)) {
            System.err.println("This is no good. Instance test failed.\n");
        }else{
            System.out.println("Caching test passed.\n");
        }
    }

    /**
     * Caching should improve performace
     */
    private static void testCacheFaster() {
        String pattern =
                "Now I lay me down to sleep," +
                "I pray this cushy life to keep,\n" +
                "I pray for toys that look like {arg1}, "+
                "and sofa cushions soft and nice.\n" +
                "I pray for gourmet kitty {arg2}, " +
                "and someone nice to scratch my {arg3},\n" +
                "For window sills all warm and bright, " +
                "for shadows to explore at night.\n" +
                "I pray I''ll always stay real cool, " +
                "and keep the secret feline rule, " +
                "to never tell a human that the world is really ruled by cats! ";

        HashMap testArgs = new HashMap();
        testArgs.put("arg1","mice");
        testArgs.put("arg2","snacks");
        testArgs.put("arg3","back");

        long start = System.currentTimeMillis();
        String fmt = HashMessageFormat.format(pattern,testArgs);
        System.out.println(fmt);
        long timeUsed1 = System.currentTimeMillis() - start;
        System.out.println("Time used: " + timeUsed1);

        testArgs.put("arg1","birds");
        testArgs.put("arg2","goody");
        testArgs.put("arg3","tummy");

        fmt = HashMessageFormat.format(pattern,testArgs);
        start = System.currentTimeMillis();
        System.out.println(fmt);
        long timeUsed2 = System.currentTimeMillis() - start;
        System.out.println("Time used: " + timeUsed2);

        if(timeUsed1 < timeUsed2) {
            System.err.println("Shouldn't caching save more time? Test failed.");
        }else{
            System.out.println("Caching test passed.");
        }
    }

    /**
      * use the same HashMessageFormat instance
      * with different arguments
      */
    private static void testInstance() {
        HashMap testArgs = new HashMap();
        testArgs.put("arg1",new Long(3));
        testArgs.put("arg2","MyDisk");

        HashMessageFormat form = new HashMessageFormat(
           "The disk \"{arg2}\" contains {arg1} file(s).");

        String fmt = form.format(testArgs);
        System.out.println(fmt);
        if(!"The disk \"MyDisk\" contains 3 file(s).".equals(fmt)) {
            System.err.println("This is no good. Instance test failed.\n");
        }else{
            System.out.println("Instance test passed.\n");
        }

        testArgs.put("arg1",new Long(0));
        fmt = form.format(testArgs);
        System.out.println(fmt);
        if(!"The disk \"MyDisk\" contains 0 file(s).".equals(fmt)) {
            System.err.println("This is no good. Instance test failed.\n");
        }else{
            System.out.println("Instance test passed.\n");
        }


        HashMessageFormat form2 = new HashMessageFormat(
           "The disk \"{arg2}\" contains {arg1,number,###,###} file(s).");

        testArgs.put("arg1",new Double("123456"));
        testArgs.put("arg2","MyDisk2");
        fmt = form2.format(testArgs);
        System.out.println(fmt);
        if(!"The disk \"MyDisk2\" contains 123,456 file(s).".equals(fmt)) {
           System.err.println("This is no good. Instance test failed.\n");
        }else{
           System.out.println("Instance test passed.\n");
        }

        testArgs.put("arg1",new Double("654321.22"));
        testArgs.put("arg2","MyDisk3");
        fmt = form2.format(testArgs);
        System.out.println(fmt);
        if(!"The disk \"MyDisk3\" contains 654,321 file(s).".equals(fmt)) {
           System.err.println("This is no good. Instance test failed.\n");
        }else{
           System.out.println("Instance test passed.\n");
        }
    }
}




