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

package com.freshdirect.transadmin.datamanager.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInputLineLengthException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;

/**
 * Class used to store the values from the Record XML tag. Also aids in parsing and matching lines in the inputfile.
 */

class Record
{

    private String name;

    private int lengthIdentMin;

    private int lengthIdentMax;

    private int fieldIdentStart;

    private int fieldIdentLength;

    private Vector fieldIdentMatchStrings;

    private char identTypeFlag;

    private RecordDefinition recordDefinition;

    static Logger cat = Logger.getLogger(Record.class);

    public Record()
    {
        lengthIdentMin = 0;
        lengthIdentMax = 0;
        fieldIdentStart = 0;
        fieldIdentLength = 0;
        fieldIdentMatchStrings = new Vector();
        identTypeFlag = '\0';
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getLengthIdentMin()
    {
        return lengthIdentMin;
    }

    public void setLengthIdentMin(int lengthIdentMin)
    {
        this.lengthIdentMin = lengthIdentMin;
    }

    public int getLengthIdentMax()
    {
        return lengthIdentMax;
    }

    public void setLengthIdentMax(int lengthIdentMax)
    {
        this.lengthIdentMax = lengthIdentMax;
    }

    public int getFieldIdentLength()
    {
        return fieldIdentLength;
    }

    public void setFieldIdentLength(int fieldIdentLength)
    {
        this.fieldIdentLength = fieldIdentLength;
    }

    public Vector getFieldIdentMatchStrings()
    {
        return fieldIdentMatchStrings;
    }

    public void setFieldIdentMatchStrings(Vector fieldIdentMatchStrings)
    {
        this.fieldIdentMatchStrings = fieldIdentMatchStrings;
    }

    public void addFieldIdentMatchString(String s)
    {
        fieldIdentMatchStrings.add(s);
    }

    public char getIdentTypeFlag()
    {
        return identTypeFlag;
    }

    public void setIdentTypeFlag(char identTypeFlag)
    {
        this.identTypeFlag = identTypeFlag;
    }

    public RecordDefinition getRecordDefinition()
    {
        return recordDefinition;
    }

    public void setRecordDefinition(RecordDefinition recordDefinition)
    {
        this.recordDefinition = recordDefinition;
    }

    public int getFieldIdentStart()
    {
        return fieldIdentStart;
    }

    public void setFieldIdentStart(int fieldIdentStart)
    {
        this.fieldIdentStart = fieldIdentStart;
    }

    /**
     * 
     * @param String line of input from the file
     * @param FileFormat not used at this time, for later expansion?
     * @return boolean does this line match according to the defined criteria?
     */
    public boolean matchesLine(String line, FileFormat ff)
    {
        switch (identTypeFlag)
        {

        // Recognition by value in a certain field
        // TODO: Will this work for delimited lines?
        case 'F':
            if (line.length() < fieldIdentStart + fieldIdentLength)
            {
                return false;
            } else
            {
                for (int i = 0; i < fieldIdentMatchStrings.size(); i++)
                {
                    String s = (String) fieldIdentMatchStrings.elementAt(i);
                    if (line.regionMatches(fieldIdentStart, s, 0, fieldIdentLength))
                    {
                        return true;
                    }
                }
            }
            return false;

            // Recognition by length of line
        case 'L':
            return line.length() >= lengthIdentMin && line.length() <= lengthIdentMax;
        }
        return true;
    }

    public String toString()
    {
        StringBuffer b = new StringBuffer();
        b.append(super.toString() + "[");
        b.append("name = " + getName());
        if (getIdentTypeFlag() == 'L')
            b.append(", identLength=(" + getLengthIdentMin() + "," + getLengthIdentMax() + ")");
        if (getIdentTypeFlag() == 'F')
            b.append(", identField=(" + getFieldIdentStart() + "," + getFieldIdentLength() + ","
                    + getFieldIdentMatchStrings().toString() + ")");
        if (getRecordDefinition() != null)
            b.append(", recordDefinition = " + getRecordDefinition().toString());
        b.append("]");
        return b.toString();
    }

    /**
     * Parse the record into the bean(s) <br>
     * 
     * @param String first line to be considered
     * @param BufferedReader used to retrieve addional lines of input for parsing multi-line records
     * @param ConversionHelper used to help convert datatypes and format strings
     * @return HashMap collection of beans populated with file data
     */
    public HashMap parseRecord(String firstLine, BufferedReader in, ConversionHelper convHelper)
            throws FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException
    {
        HashMap beans = new HashMap();
        try
        {
            Class zeroArgs[] = new Class[0];
            HashMap beanHash = recordDefinition.getBeansUsed();
            String beanName;
            Object beanObj;
            for (Iterator bean_it = beanHash.keySet().iterator(); bean_it.hasNext(); beans.put(beanName, beanObj))
            {
                beanName = (String) bean_it.next();
                Bean bean = (Bean) beanHash.get(beanName);
                Object objArgs[] = new Object[0];
                beanObj = Class.forName(bean.getBeanClass()).getDeclaredConstructor(zeroArgs).newInstance(objArgs);
            }

            Vector lines = recordDefinition.getLines();
            String inputLine = firstLine;
            for (int i = 0; i < lines.size(); i++)
            {
                Line line = (Line) lines.get(i);
                line.parseInput(inputLine, beans, convHelper);
                if (i + 1 < lines.size())
                    inputLine = in.readLine();
            }

        } catch (NoSuchMethodException e)
        {
            cat.error("Finding method", e);
            throw new FlatwormConversionException("Couldn't Find Method");
        } catch (SecurityException e)
        {
            cat.error("Invoking method", e);
            throw new FlatwormConversionException("Couldn't invoke Method");
        } catch (ClassNotFoundException e)
        {
            cat.error("Finding class", e);
            throw new FlatwormConversionException("Couldn't Find Class");
        } catch (IOException e)
        {
            cat.error("Reading input", e);
            throw new FlatwormConversionException("Couldn't read line");
        } catch (InstantiationException e)
        {
            cat.error("Creating bean", e);
            throw new FlatwormConversionException("Couldn't create bean");
        } catch (IllegalAccessException e)
        {
            cat.error("No access to class", e);
            throw new FlatwormConversionException("Couldn't access class");
        } catch (InvocationTargetException e)
        {
            cat.error("Invoking method", e);
            throw new FlatwormConversionException("Couldn't invoke Method");
        }
        return beans;
    }

    private String[] getFieldNames()
    {
        Vector names = new Vector();
        int count = 0;
        Vector lines = recordDefinition.getLines();
        for (int i = 0; i < lines.size(); i++)
        {
            Line l = (Line) lines.get(i);
            Vector el = l.getRecordElements();
            for (int j = 0; j < el.size(); j++)
            {
                RecordElement re = (RecordElement) el.get(j);
                names.add(re.getBeanRef());
            }

        }

        String propertyNames[] = new String[names.size()];
        for (int i = 0; i < names.size(); i++)
            propertyNames[i] = (String) names.get(i);

        return propertyNames;
    }

}