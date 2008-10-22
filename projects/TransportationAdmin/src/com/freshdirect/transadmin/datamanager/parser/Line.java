/**
 * Flatworm - A Java Flat File Importer Copyright (C) 2004 James M. Turner Extended by James Lawrence - 2005
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
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Category;

import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInputLineLengthException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;

/**
 * Bean class used to store the values from the Line XML tag
 */
class Line
{

    Category cat;

    private Vector recordElements;

    private String delimit;

    private char chrQuote;

    public Line()
    {
        cat = Category.getInstance(Line.class);
        recordElements = new Vector();

        delimit = null;

    }

    /**
     * <b>NOTE:</b> Only the first character in the string is considered
     */
    public void setQuoteChar(String quote)
    {
        chrQuote = quote.charAt(0);
    }

    public boolean isDelimeted()
    {
        return (null != delimit);
    }

    public void setDelimeter(String delimit)
    {
        this.delimit = delimit;
    }

    public String getDelimeter()
    {
        return delimit;
    }

    public Vector getRecordElements()
    {
        return recordElements;
    }

    public void setRecordElements(Vector recordElements)
    {
        this.recordElements = recordElements;
    }

    public void addRecordElement(RecordElement re)
    {
        recordElements.add(re);
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer();
        b.append(super.toString() + "[");
        b.append("elements = " + recordElements);
        b.append("]");
        return b.toString();
    }

    /**
     * 
     * @param String single line from file to be parsed into its corresponding bean
     * @param Hashmap contains a collection of beans which will be populated with parsed data
     * @param ConversionHelper aids in the conversion of datatypes and string formatting
     * 
     * @throws FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException
     */
    public void parseInput(String inputLine, HashMap beans, ConversionHelper convHelper)
            throws FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException
    {

        // JBL - check for delimited status
        if (isDelimeted())
        {
            parseInputDelimeted(inputLine, beans, convHelper);
            return;
        }

        int charPos = 0;
        for (int i = 0; i < recordElements.size(); i++)
        {
            RecordElement re = (RecordElement) recordElements.get(i);
            int start = charPos;
            int end = charPos;
            if (re.isFieldStartSet())
                start = re.getFieldStart();
            if (re.isFieldEndSet())
            {
                end = re.getFieldEnd();
                charPos = end;
            }
            if (re.isFieldLengthSet())
            {
                end = start + re.getFieldLength();
                charPos = end;
            }
            if (end > inputLine.length())
                throw new FlatwormInputLineLengthException("Looking for field " + re.getBeanRef() + " at pos " + start
                        + ", end " + end + ", input length = " + inputLine.length());
            String beanRef = re.getBeanRef();
            if (beanRef != null)
            {
                String fieldChars = inputLine.substring(start, end);

                // JBL - to keep from dup. code, moved this to a private method
                mapField(convHelper, fieldChars, re, beans);

            }
        }
    }

    /**
     * Convert string field from file into appropriate type and set bean's value<br>
     * 
     * @param ConversionHelper helps with type conversion and string transformations
     * @param String the field
     * @param RecordElement contains detailed information about the field
     * @param HashMap collection of beans to be populated
     * 
     * @throws FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException - wraps
     *             IllegalAccessException,InvocationTargetException,NoSuchMethodException
     */
    private void mapField(ConversionHelper convHelper, String fieldChars, RecordElement re, HashMap beans)
            throws FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException
    {

        Object value = convHelper.convert(re.getType(), fieldChars, re.getConversionOptions(), re.getBeanRef());

        String beanRef = re.getBeanRef();
        int posOfFirstDot = beanRef.indexOf('.');
        String beanName = beanRef.substring(0, posOfFirstDot);
        String property = beanRef.substring(posOfFirstDot + 1);

        Object bean = beans.get(beanName);
        try
        {
            if ("true".equalsIgnoreCase((String) re.getConversionOptions().get("append")))
            {
                Object currentValue = PropertyUtils.getProperty(bean, property);
                if (currentValue != null)
                    value = currentValue.toString() + value;
            }
            PropertyUtils.setProperty(bean, property, value);
        } catch (IllegalAccessException e)
        {
            cat.error("While running set property method for " + re.getBeanRef() + "with value '" + value + "'", e);
            throw new FlatwormConversionException("Setting field " + re.getBeanRef());
        } catch (InvocationTargetException e)
        {
            cat.error("While running set property method for " + re.getBeanRef() + "with value '" + value + "'", e);
            throw new FlatwormConversionException("Setting field " + re.getBeanRef());
        } catch (NoSuchMethodException e)
        {
            cat.error("While running set property method for " + re.getBeanRef() + "with value '" + value + "'", e);
            throw new FlatwormConversionException("Setting field " + re.getBeanRef());
        }

    }

    /**
     * Convert string field from file into appropriate type and set bean's value. This is used for delimited files only<br>
     * 
     * @param ConversionHelper helps with type conversion and string transformations
     * @param String the field
     * @param RecordElement contains detailed information about the field
     * @param HashMap collection of beans to be populated
     * 
     * @throws FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException - wraps
     *             IllegalAccessException,InvocationTargetException,NoSuchMethodException
     */
    private void parseInputDelimeted(String inputLine, HashMap beans, ConversionHelper convHelper)
            throws FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException
    {

        // JBL - gotcha, only 1st char of delimit is considered
        String[] fields = Util.split(inputLine, delimit.charAt(0), chrQuote);

        for (int i = 0; i < recordElements.size(); i++)
        {
            RecordElement re = (RecordElement) recordElements.get(i);

            String beanRef = re.getBeanRef();
            if (beanRef != null)
            {

                try
                {
                    String fieldChars = fields[i];
                    // JBL - to keep from dup. code, moved this to a private method
                    mapField(convHelper, fieldChars, re, beans);
                } catch (ArrayIndexOutOfBoundsException ex)
                {
                    throw new FlatwormInputLineLengthException("No data available for record-element " + i);
                }
            }
        }
    }

}