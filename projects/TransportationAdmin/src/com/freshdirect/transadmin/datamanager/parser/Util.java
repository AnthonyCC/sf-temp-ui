/**
 * Flatworm - A Java Flat File Importer Copyright (C) 2004 James M. Turner Extended by James Lawrence 2005
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 */

package com.freshdirect.transadmin.datamanager.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Static final class to hold string manipulation methods
 */
public final class Util
{

    private static Pattern numbersOnly = Pattern.compile("[\\D]+");

    private static Pattern lettersOnly = Pattern.compile("[^A-Za-z]+");

    private static Pattern numbersOrLettersOnly = Pattern.compile("[^A-Za-z0-9]+");

    /**
     * <code>split</code> divides a string into many strings based on a delimiter The main difference between this
     * split and the one that comes with Java is this one will ignore delimiters that are within quoted fields
     * <p>
     * <b>NOTE:</b> Delimiter will be ignored once chrQuote is encountered. Consideration will begin once matching
     * chrQuote is encountered
     * </p>
     * 
     * @param String you want to split
     * @param char you want to split the string on
     * @param char you want to be considered to be your quoting character
     * @return String[] String array containing results
     */
    public static String[] split(String str, char chrSplit, char chrQuote)
    {
        Vector v = new Vector();
        String str1 = new String();
        boolean inQuote = false;

        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == chrSplit && !inQuote)
            {
                v.add(str1);
                str1 = new String();
            } else if (str.charAt(i) == chrQuote)
            {
                inQuote = (!inQuote);
            } else
            {
                str1 += str.charAt(i);
            }
        }

        v.add(str1);
        String array[];
        array = new String[v.size()];

        for (int i = 0; i < array.length; i++)
        {
            array[i] = new String((String) v.elementAt(i));
        }

        return array;
    }

    /**
     * Different from the method in CoreConverters, this one is used for file creation
     * 
     * <br>
     * <br>
     * Specified in flatworm XML file like: <code>&lt;conversion-option name="format" value="yyyy-MM-dd"/&gt;</code>
     * 
     * @param Date to be converted to a string
     * @param String defaultDateFormat is provided by FileCreator and is used when the date format is not supplied in
     *            the Flatworm XML file
     * @param HashMap collection of ConversionOptions to gather further justification options
     * 
     * @return String
     * @throws Exception - if date format is not specified in the XML file or given to FileCreator. At least one must be
     *             specified.
     */
    public static String formatDate(Date date, String defaultDateFormat, HashMap options) throws Exception
    {
        String format = getValue(options, "format");

        // Default format, if none is supplied
        if (null == format)
            if (null != defaultDateFormat)
                format = defaultDateFormat;
            else
                throw new Exception(
                        "You must define a conversion-option with a date format or supply one, I can find neither");

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);

    }

    /**
     * Removes pre-determined chracters from string based on Java Patterns
     * 
     * <br>
     * <br>
     * Specified in flatworm XML file like: <code>&lt;conversion-option name="justify" value="right"/&gt;</code>
     * 
     * @param String field to be justified
     * @param String value can be ('left'|'right'|'both') - default value is 'both' if not specified
     * @param HashMap collection of ConversionOptions to gather further justification options
     * @param int used in file creation to ensure string is padded to the proper length
     * 
     * @return String
     */
    public static String justify(String str, String value, HashMap options, int length)
    {

        if (value == null)
        {
            value = "both";
        }

        boolean justifyLeft = false;
        boolean justifyRight = false;
        if (value.equalsIgnoreCase("left"))
        {
            justifyLeft = true;
        }
        if (value.equalsIgnoreCase("right"))
        {
            justifyRight = true;
        }
        if (value.equalsIgnoreCase("both"))
        {
            justifyLeft = true;
            justifyRight = true;
        }

        String strPadChar = " ";
        String arg = getValue(options, "pad-character");
        if (arg != null)
        {
            strPadChar = arg;
        }

        // if length is 0, we are removing padding, otherwise, we are adding it
        if (0 == length)
        {
            // Remove left justification
            if (justifyLeft)
            {
                int i;
                for (i = str.length() - 1; i > -1 && isPadChar(str.charAt(i), strPadChar); i--)
                    ;
                if (i != str.length() - 1)
                {
                    str = str.substring(0, i + 1);
                }
            }

            // Remove right justification
            if (justifyRight)
            {
                int i;
                for (i = 0; i < str.length() && isPadChar(str.charAt(i), strPadChar); i++)
                    ;
                if (i != 0)
                {
                    str = str.substring(i, str.length());
                }
            }
        } else
        {
            // pad only with first character
            strPadChar = strPadChar.substring(0, 1);

            if (str.length() < length)
            {
                // Figure out diffenence in length to create padding string
                int lenDiff = length - str.length();

                String padding = "";
                for (int i = 0; i < lenDiff; i++)
                    padding = padding + strPadChar;

                if (justifyLeft)
                {
                    str = str + padding;
                }

                if (justifyRight)
                {
                    str = padding + str;
                }
            }
        }

        return str;
    }

    private static boolean isPadChar(char c, String strPadChar)
    {
        return strPadChar.indexOf(c) != -1;
    }

    /**
     * Removes pre-determined chracters from string based on Java Patterns
     * 
     * <br>
     * <br>
     * Specified in flatworm XML file like:
     * <code>&lt;conversion-option name="strip-chars" value="non-numeric"/&gt;</code>
     * 
     * @param String field to be stripped
     * @param String value can be ('non-numeric'|'non-alpha'|'non-alphanumeric')
     * @param HashMap collection of ConversionOptions, for future enhancement
     * 
     * @return String
     */
    public static String strip(String str, String value, HashMap options)
    {

        if (value.equalsIgnoreCase("non-numeric"))
        {
            str = numbersOnly.matcher(str).replaceAll("");
        }
        if (value.equalsIgnoreCase("non-alpha"))
        {
            str = lettersOnly.matcher(str).replaceAll("");
        }
        if (value.equalsIgnoreCase("non-alphanumeric"))
        {
            str = numbersOrLettersOnly.matcher(str).replaceAll("");
        }

        return str;
    }

    /**
     * <br>
     * <br>
     * Specified in flatworm XML file like: <code>&lt;conversion-option name="substring" value="1,10"/&gt;</code>
     * 
     * @param String value of field
     * @param String default value
     * @param HashMap collection of ConversionOptions, for future enhancement
     * 
     * @return String
     */
    public static String substring(String str, String value, HashMap options)
    {

        String[] args = str.split(value, ',');
        str = str.substring(new Integer(args[0]).intValue(), new Integer(args[1]).intValue());

        return str;
    }

    /**
     * If str is length zero (after trimming), value is returned. Defualt values <b>should not</b> be specified in the
     * flatworm XML file when you want a string of only spaces.
     * 
     * @param String value of field
     * @param String default value
     * @param HashMap collection of ConversionOptions, for future enhancement
     * 
     * @return String
     */
    public static String defaultValue(String str, String value, HashMap options)
    {

        if (str.trim().length() == 0)
            if (value != null)
                return value;

        return str;
    }

    /**
     * Conversion-Option now stored in class, more than a simple Hashmap lookup
     * 
     * @param HashMap options The conversion-option values for the field
     * @param String The key to lookup appropriate ConversionOption
     * 
     * @return The string that contains value of the ConversionOption
     */
    public static String getValue(HashMap options, String key)
    {

        if (options.containsKey(key))
        {
            return ((ConversionOption) options.get(key)).getValue();
        } else
        {
            return null;
        }
    }

}
