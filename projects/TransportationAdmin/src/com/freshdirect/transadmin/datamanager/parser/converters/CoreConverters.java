/**
 * Flatworm - A Java Flat File Importer Copyright (C) 2004 James M. Turner
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

package com.freshdirect.transadmin.datamanager.parser.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.freshdirect.transadmin.datamanager.parser.ConversionOption;
import com.freshdirect.transadmin.datamanager.parser.Util;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;

/**
 * <code>CoreConverters</code> contains methods to convert the most commonly encountered text types to native Java
 * types. It can be used as the <code>class</code> parameter in a Flatworm <code>converter</code> tag, with one of
 * the public methods listed below as the <code>method</code> parameter. <p/> All converters (included the ones listed
 * here and any supplied by the user) should expect to accept and handle the following <code>conversion-option</code>s:
 * <p/>
 * <dl>
 * <dt><code>justify</code></dt>
 * <dd>Defines the justification rule used to strip/add pad characters to this field. Used in conjunction with the
 * <code>pad-character</code> option. Valid values are:
 * <ul>
 * <li><code>left</code> to strip the right hand side of the field of pad chars</li>
 * <li><code>right</code> to strip the left hand side of the field of pad chars</li>
 * <li><code>both</code> to strip both end of the field of pad chars</li>
 * </ul>
 * The default if no value is specified is <code>both</code></dd>
 * <dt><code>pad-character</code></dt>
 * <dd>Defines the character to be used in conjunction with the <code>justify</code> option when removing or adding
 * pad characters from a field. The default value is the space character.</dd>
 * <dt><code>default-value</code></dt>
 * <dd>Defines the default value to be used if the field is empty after being stripped of pad characters.</dd>
 * </dl>
 * 
 * @author James M. Turner
 * @version $Id: CoreConverters.java,v 1.6 2005/09/21 04:01:44 blackbearnh Exp $
 */

public class CoreConverters
{

    static Logger cat = Logger.getLogger(CoreConverters.class);

    /**
     * Conversion function for <code>String</code>, returns the source string with padding removed if requested.
     * 
     * @param str The source string
     * @param options The conversion-option values for the field
     * @return The string with padding removed
     */

    public String convertChar(String str, HashMap options)
    {
        // nothing extra do do, since convHelper calls removePadding now
        return str.trim();
    }

    /**
     * Object->String conversion function.
     * 
     * @param obj source object
     * @param options The conversion-option values for the field
     * @return The string result
     */
    public String convertChar(Object obj, HashMap options)
    {
        return (String) obj;
    }

    /**
     * Conversion function for <code>Date</code>, returns the source string with padding removed if requested,
     * converted into a date. <p/> In addition to the standard conversion options, dates also support the following:
     * <p/>
     * <dl>
     * <dt><code>format</code></dt>
     * <dd>A date format string in <code>SimpleDateFormat</code> syntax that defines the format to expect, default is
     * yyyy-MM-dd.</dd>
     * </dl>
     * 
     * @param str The source string
     * @param options The conversion-option values for the field
     * @return The converted date
     * @throws FlatwormConversionException if the date fails to parse correctly.
     */

    public Date convertDate(String str, HashMap options) throws FlatwormConversionException
    {
        try
        {
            String format = (String) Util.getValue(options, "format");
            //System.out.println("format:"+format+"->"+str);
            SimpleDateFormat sdf;
            if (str.length() == 0)
                return null;
            if (format == null)
                format = "yyyy-MM-dd";
            sdf = new SimpleDateFormat(format);
            return sdf.parse(str);
        } catch (ParseException ex) {
        	
        	ex.printStackTrace();
            cat.error(ex);
            throw new FlatwormConversionException(str);
        }
    }

    /**
     * Date->String conversion function.
     * 
     * @param obj source object of type {@link Date}.
     * @param options The conversion-option values for the field
     * @return the string result
     */
    public String convertDate(Object obj, HashMap options)
    {
        Date date = (Date) obj;
        String format = (String) Util.getValue(options, "format");
        //System.out.println("format:"+format+"->"+date);
        if (obj == null)
            return null;
        if (format == null)
            format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Conversion function for <code>Double</code>, returns the source string with padding removed if requested,
     * converted into a double. <p/> In addition to the standard conversion options, doubles also support the following:
     * <p/>
     * <dl>
     * <dt><code>decimal-implied</code></dt>
     * <dd>If set to <code>true</code>, the decimal point is positionally implied rather than explicitly included.
     * If set, <code>decimal-places</code> is required.</dd>
     * <dt><code>decimal-places</code></dt>
     * <dd>The number of digits in the string which are to the right of the decimal point, if
     * <code>decimal-implied</code> is set.</dd>
     * </dl>
     * 
     * @param str The source string
     * @param options The conversion-option values for the field
     * @return The converted double value
     * @throws FlatwormConversionException If the source number fails to parse as a double or the decimal places option
     *             fails to parse as an integer value.
     */

    public Double convertDecimal(String str, HashMap options) throws FlatwormConversionException
    {
        try
        {
            int decimalPlaces = 0;
            ConversionOption conv = (ConversionOption) options.get("decimal-places");

            String decimalPlacesOption = null;
            if (null != conv)
                decimalPlacesOption = conv.getValue();

            boolean decimalImplied = "true".equals(Util.getValue(options, "decimal-implied"));

            if (decimalPlacesOption != null)
                decimalPlaces = Integer.parseInt(decimalPlacesOption);

            if (str.length() == 0)
                return new Double(0.0D);

            if (decimalImplied)
                return new Double(Double.parseDouble(str) / Math.pow(10D, decimalPlaces));
            else
                return Double.valueOf(str);

        } catch (NumberFormatException ex)
        {
            cat.error(ex);
            throw new FlatwormConversionException(str);
        }
    }

    public String convertDecimal(Object obj, HashMap options)
    {
        Double d = (Double) obj;
        if (d == null)
        {
            return null;
        }

        int decimalPlaces = 0;
        ConversionOption conv = (ConversionOption) options.get("decimal-places");

        String decimalPlacesOption = null;
        if (null != conv)
            decimalPlacesOption = conv.getValue();

        boolean decimalImplied = "true".equals(Util.getValue(options, "decimal-implied"));

        if (decimalPlacesOption != null)
            decimalPlaces = Integer.parseInt(decimalPlacesOption);

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(!decimalImplied);
        format.setGroupingUsed(false);
        if (decimalImplied)
        {
            format.setMaximumFractionDigits(0);
            d = new Double(d.doubleValue() * Math.pow(10D, decimalPlaces));
        } else
        {
            format.setMinimumFractionDigits(decimalPlaces);
            format.setMaximumFractionDigits(decimalPlaces);
        }
        return format.format(d);
    }

    /**
     * Conversion function for <code>Integer</code>, returns the source string with padding removed if requested,
     * converted into a integer.
     * 
     * @param str The source string
     * @param options The conversion-option values for the field
     * @return The converted integer value
     * @throws FlatwormConversionException If the source number fails to parse as an integer value.
     */

    public Integer convertInteger(String str, HashMap options) throws FlatwormConversionException
    {
        try
        {
            if (str.length() == 0)
            {
                return new Integer(0);
            } else
            {
                return Integer.valueOf(str);
            }
        } catch (NumberFormatException ex)
        {
            cat.error(ex);
            throw new FlatwormConversionException(str);
        }
    }

    public String convertInteger(Object obj, HashMap options)
    {
        if (obj == null)
        {
            return null;
        }
        Integer i = (Integer) obj;
        return Integer.toString(i.intValue());
    }

    /**
     * Conversion function for <code>Long</code>, returns the source string with padding removed if requested,
     * converted into a long.
     * 
     * @param str The source string
     * @param options The conversion-option values for the field
     * @return The converted long value
     * @throws FlatwormConversionException If the source number fails to parse as an long value.
     */

    public Long convertLong(String str, HashMap options) throws FlatwormConversionException
    {
        try
        {
            if (str.length() == 0)
            {
                return new Long(0L);
            } else
            {
                return Long.valueOf(str);
            }
        } catch (NumberFormatException ex)
        {
            cat.error(ex);
            throw new FlatwormConversionException(str);
        }
    }

    public String convertLong(Object obj, HashMap options)
    {
        if (obj == null)
        {
            return null;
        }
        Long l = (Long) obj;
        return Long.toString(l.longValue());
    }

    /**
     * Conversion function for <code>BigDecimal</code>, returns the source string with padding removed if requested,
     * converted into a big decimal. <p/> In addition to the standard conversion options, big decimals also support the
     * following: <p/>
     * <dl>
     * <dt><code>decimal-implied</code></dt>
     * <dd>If set to <code>true</code>, the decimal point is positionally implied rather than explicitly included.
     * If set, <code>decimal-places</code> is required.</dd>
     * <dt><code>decimal-places</code></dt>
     * <dd>The number of digits in the string which are to the right of the decimal point, if
     * <code>decimal-implied</code> is set.</dd>
     * </dl>
     * 
     * @param str The source string
     * @param options The conversion-option values for the field
     * @return The converted big decimal value
     * @throws FlatwormConversionException If the source number fails to parse as a big decimal or the decimal places
     *             option fails to parse as an integer value.
     */

    public BigDecimal convertBigDecimal(String str, HashMap options) throws FlatwormConversionException
    {
        try
        {
            int decimalPlaces = 0;
            String decimalPlacesOption = (String) Util.getValue(options, "decimal-places");
            boolean decimalImplied = "true".equals(Util.getValue(options, "decimal-implied"));

            if (decimalPlacesOption != null)
                decimalPlaces = Integer.parseInt(decimalPlacesOption);

            if (str.length() == 0)
                return new BigDecimal(0.0D);

            if (decimalImplied)
                return new BigDecimal(Double.parseDouble(str) / Math.pow(10D, decimalPlaces));
            else
                return new BigDecimal(Double.parseDouble(str));
        } catch (NumberFormatException ex)
        {
            cat.error(ex);
            throw new FlatwormConversionException(str);
        }
    }

    public String convertBigDecimal(Object obj, HashMap options)
    {
        if (obj == null)
        {
            return null;
        }
        BigDecimal bd = (BigDecimal) obj;

        int decimalPlaces = 0;
        String decimalPlacesOption = (String) Util.getValue(options, "decimal-places");
        boolean decimalImplied = "true".equals(Util.getValue(options, "decimal-implied"));

        if (decimalPlacesOption != null)
            decimalPlaces = Integer.parseInt(decimalPlacesOption);

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(!decimalImplied);
        format.setMinimumFractionDigits(decimalPlaces);
        format.setMaximumFractionDigits(decimalPlaces);

        return format.format(bd.doubleValue());
    }
}