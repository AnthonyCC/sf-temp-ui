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
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Category;

import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInputLineLengthException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInvalidRecordException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;

/**
 * The <code>FileFormat</code> is the point of entry into the Flatworm parser. It is generated from the XML Flatworm
 * description, and can then be used to read a line or lines from a file, determine if there is a matching record
 * definition for the line(s), and return a <code>HashMap</code> with the beans created by parsing the input.
 */

public class FileFormat
{
    private HashMap records;

    private Vector recordOrder;

    private ConversionHelper convHelper = null;

    // JBL - Used when parsing fails, gives access to bad line
    private String lastLine = "";

    Category cat = Category.getInstance(FileFormat.class);

    public FileFormat()
    {
        records = new HashMap();
        recordOrder = new Vector();

        // JBL
        convHelper = new ConversionHelper();
    }

    // JBL - getter
    public String getLastLine()
    {
        return lastLine;
    }

    public HashMap getRecords()
    {
        return records;
    }

    public void setRecords(HashMap records)
    {
        this.records = records;
    }

    public void addRecord(Record r)
    {
        records.put(r.getName(), r);
        recordOrder.add(r);
    }

    public Record getRecord(String name)
    {
        return (Record) records.get(name);
    }

    private Record findMatchingRecord(String firstLine)
    {
        for (int i = 0; i < recordOrder.size(); i++)
        {
            Record record = (Record) recordOrder.get(i);
            if (record.matchesLine(firstLine, this))
            {
                return record;
            }
        }
        return null;
    }

    /**
     * Facilitates the storage of multiple converters. However, actual storage is delegated to the COnversionHelper
     * class.
     * 
     * @param Converter converter to store
     */
    public void addConverter(Converter converter)
    {
        convHelper.addConverter(converter);
    }

    /**
     * FileFormat is the keeper of ConversionHelper, but does not actually use it. This allows access
     * 
     * @return ConversionHelper
     */
    public ConversionHelper getConvertionHelper()
    {
        return convHelper;
    }

    /**
     * When called with a <code>BufferedReader</code>, reads sufficient lines to parse a record, and returns the
     * beans created.
     * 
     * @param in The stream to read from
     * @return The created beans in a MatchedRecord object
     * @throws FlatwormInvalidRecordException
     * @throws FlatwormInputLineLengthException
     * @throws FlatwormConversionException
     * @throws FlatwormUnsetFieldValueException
     */
    public MatchedRecord getNextRecord(BufferedReader in) throws FlatwormInvalidRecordException,
            FlatwormInputLineLengthException, FlatwormConversionException, FlatwormUnsetFieldValueException
    {
        try
        {
            String firstLine;
            firstLine = in.readLine();
            lastLine = firstLine;

            if (firstLine == null)
                return null;
            Record rd;
            rd = this.findMatchingRecord(firstLine);
            if (rd == null)
                throw new FlatwormInvalidRecordException("Unmatched line in input file");

            HashMap beans = rd.parseRecord(firstLine, in, convHelper);
            return new MatchedRecord(rd.getName(), beans);
        } catch (IOException e)
        {
            return null;
        }
    }

}
