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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.datamanager.parser.converters.CoreConverters;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;

/**
 * The <code>ConversionHelper<code> was created to seperate formatting responsibility into a seperate class.
 *  This class also makes writing your own converter more of a reality by seperating type conversion from string formatting.
 *  String formatting has moved to a seperate class called Util.
 */

public class ConversionHelper
{

    private HashMap converters;

    private HashMap converterMethodCache;

    private HashMap converterToStringMethodCache;

    private HashMap converterObjectCache;

    static Category cat = Category.getInstance(ConversionHelper.class);

    public ConversionHelper()
    {

        converters = new HashMap();
        converterMethodCache = new HashMap();
        converterToStringMethodCache = new HashMap();
        converterObjectCache = new HashMap();
    }

    /**
     * 
     * @param String type from the xml configuration file
     * @param String as read from the input file
     * @param Hashmap contains ConversionOoptions (if any) for this field
     * @param String format - "class.property", used for more descriptive exception messages, should something go wrong
     * 
     * @throws FlatwormConversionException - if problems are encountered during the conversion process (wraps other
     *             exceptions)
     * @return Object Java type corresponding to the field type, post conversion
     */
    public Object convert(String type, String fieldChars, HashMap options, String beanRef)
            throws FlatwormConversionException
    {

        Object value = null;

        try
        {
            Object object = getConverterObject(type);
            Method method = getConverterMethod(type);

            fieldChars = transformString(fieldChars, options, 0);

            Object[] args = { fieldChars, options };
            value = method.invoke(object, args);

        } catch (IllegalAccessException e)
        {
            cat.error("While running convert method for " + beanRef, e);
            throw new FlatwormConversionException("Converting field " + beanRef + " with value '" + fieldChars + "'");
        } catch (InvocationTargetException e)
        {
            cat.error("While running convert method for " + beanRef, e);
            throw new FlatwormConversionException("Converting field " + beanRef + " with value '" + fieldChars + "'");
        } catch (IllegalArgumentException e)
        {
            cat.error("While running convert method for " + beanRef, e);
            throw new FlatwormConversionException("Converting field " + beanRef + " with value '" + fieldChars + "'");
        }
        return value;
    }

    public String convert(String type, Object obj, HashMap options, String beanRef) throws FlatwormConversionException
    {
        try
        {
            Object converter = getConverterObject(type);
            Method method = getToStringConverterMethod(type);
            Object[] args = { obj, options };
            String result = (String) method.invoke(converter, args);
            return result;
        } catch (IllegalArgumentException e)
        {
            cat.error("While running toString convert method for " + beanRef, e);
            throw new FlatwormConversionException("Converting field " + beanRef + " to string for value '" + obj + "'");
        } catch (IllegalAccessException e)
        {
            cat.error("While running toString convert method for " + beanRef, e);
            throw new FlatwormConversionException("Converting field " + beanRef + " to string for value '" + obj + "'");
        } catch (InvocationTargetException e)
        {
            cat.error("While running toString convert method for " + beanRef, e);
            throw new FlatwormConversionException("Converting field " + beanRef + " to string for value '" + obj + "'");
        }
    }

    /**
     * Handles the processing of the Conversion-Options from the flatworm XML file
     * 
     * @param String string you wish to transform
     * @param HashMap collection of ConversionOption objects
     * @param int used in justification to ensure proper formatting
     * 
     * @return String - string after transformation
     */
    public String transformString(String fieldChars, HashMap options, int length)
    {
        // JBL - Implement iteration of conversion-options
        // Iterate over conversion-options, that way, the xml file
        // can drive the order of conversions, instead of having them
        // hard-coded like in 'removePadding' (old way)
        CoreConverters cvt = new CoreConverters();
        Set keys = options.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();)
        {
            ConversionOption conv = (ConversionOption) options.get(it.next());

            if (conv.getName().equals("justify"))
                fieldChars = Util.justify(fieldChars, conv.getValue(), options, length);
            if (conv.getName().equals("strip-chars"))
                fieldChars = Util.strip(fieldChars, conv.getValue(), options);
            if (conv.getName().equals("substring"))
                fieldChars = Util.substring(fieldChars, conv.getValue(), options);
            if (conv.getName().equals("default-value"))
                fieldChars = Util.defaultValue(fieldChars, conv.getValue(), options);
        }

        if (length > 0) // Never request string to be zero length
            if (fieldChars.length() > length) // too long, chop it off
                fieldChars = fieldChars.substring(0, length);

        return StringUtil.rightPad(fieldChars, length,' ');
    }

    /**
     * Facilitates the storage of multiple converters used by the <code>convert</code> method during processing
     * 
     * @param Converter converter to store
     */
    public void addConverter(Converter converter)
    {
        converters.put(converter.getName(), converter);
    }

    /**
     * @param String key used to lookup method
     * @return Method java reflection Object used to represent the conversion method
     */
    private Method getConverterMethod(String type) throws FlatwormConversionException
    {
        try
        {
            Converter c = (Converter) converters.get(type);
            if (converterMethodCache.get(c) != null)
                return (Method) converterMethodCache.get(c);
            Method meth;
            Class cl = Class.forName(c.getConverterClass());
            Class args[] = { String.class, HashMap.class };
            meth = cl.getMethod(c.getMethod(), args);
            converterMethodCache.put(c, meth);
            return meth;
        } catch (NoSuchMethodException e)
        {
            cat.error("Finding method", e);
            throw new FlatwormConversionException("Couldn't Find Method");
        } catch (ClassNotFoundException e)
        {
            cat.error("Finding class", e);
            throw new FlatwormConversionException("Couldn't Find Class");
        }
    }

    private Method getToStringConverterMethod(String type) throws FlatwormConversionException
    {
        Converter c = (Converter) converters.get(type);
        if (converterToStringMethodCache.get(c) != null)
            return (Method) converterToStringMethodCache.get(c);
        try
        {
            Method meth;
            Class cl = Class.forName(c.getConverterClass());
            Class args[] = { Object.class, HashMap.class };
            meth = cl.getMethod(c.getMethod(), args);
            converterToStringMethodCache.put(c, meth);
            return meth;
        } catch (NoSuchMethodException e)
        {
            cat.error("Finding method", e);
            throw new FlatwormConversionException("Couldn't Find Method 'String " + c.getMethod()
                    + "(Object, HashMap)'");
        } catch (ClassNotFoundException e)
        {
            cat.error("Finding class", e);
            throw new FlatwormConversionException("Couldn't Find Class");
        }
    }

    /**
     * @param String key used to lookup object
     * @return Object instance of the conversion class
     */
    private Object getConverterObject(String type) throws FlatwormConversionException
    {
        try
        {
            Converter c = (Converter) converters.get(type);
            if (c == null)
            {
                throw new FlatwormConversionException("type '" + type + "' not registered");
            }
            if (converterObjectCache.get(c.getConverterClass()) != null)
                return converterObjectCache.get(c.getConverterClass());
            Object o;
            Class cl = Class.forName(c.getConverterClass());
            Class args[] = new Class[0];
            Object objArgs[] = new Object[0];
            o = cl.getConstructor(args).newInstance(objArgs);
            converterObjectCache.put(c.getConverterClass(), o);
            return o;
        } catch (NoSuchMethodException e)
        {
            cat.error("Finding method", e);
            throw new FlatwormConversionException("Couldn't Find Method");
        } catch (IllegalAccessException e)
        {
            cat.error("No access to class", e);
            throw new FlatwormConversionException("Couldn't access class");
        } catch (InvocationTargetException e)
        {
            cat.error("Invoking method", e);
            throw new FlatwormConversionException("Couldn't invoke method");
        } catch (InstantiationException e)
        {
            cat.error("Instantiating", e);
            throw new FlatwormConversionException("Couldn't instantiate converter");
        } catch (ClassNotFoundException e)
        {
            cat.error("Finding class", e);
            throw new FlatwormConversionException("Couldn't Find Class");
        }
    }

}
