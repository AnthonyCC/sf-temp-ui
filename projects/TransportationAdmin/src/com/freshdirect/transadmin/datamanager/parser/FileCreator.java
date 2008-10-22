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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Category;

import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConfigurationValueException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormCreatorException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;

/**
 * Used to create a flatfile. This class wraps the functionality that used to be in the main() of the examples. This
 * way, the client knows less about the internal workings of FlatWorm.
 */
public class FileCreator
{

    private String file;

    private FileFormat ff;

    private BufferedWriter bufOut;

    private HashMap beans = new HashMap();

    private String recordSeperator = null;

    private Category cat = Category.getInstance(FileCreator.class);

    private OutputStream outputStream;

    /**
     * Constructor for FileCreator<br>
     * 
     * @param String full path to the FlatWorm XML configuration file
     * @param String full path to output file
     * @throws FlatwormCreatorException - wraps FlatwormConfigurationValueException & FlatwormUnsetFieldValueException
     *             (to reduce number of exceptions clients have to be aware of)
     */
    public FileCreator(String config, String file) throws FlatwormCreatorException
    {
        this.file = file;
        this.outputStream = null;
        loadConfigurationFile(config);
    }

    public FileCreator(String config, OutputStream stream) throws FlatwormCreatorException
    {
        this.file = null;
        this.outputStream = stream;
        loadConfigurationFile(config);
    }

    public FileCreator(InputStream config, String file) throws FlatwormCreatorException
    {
        this.file = file;
        this.outputStream = null;
        loadConfigurationFile(config);
    }

    public FileCreator(InputStream config, OutputStream stream) throws FlatwormCreatorException
    {
        this.file = null;
        this.outputStream = stream;
        loadConfigurationFile(config);
    }

    private void loadConfigurationFile(InputStream configStream) throws FlatwormCreatorException
    {
        ConfigurationReader parser = new ConfigurationReader();
        try
        {
            ff = parser.loadConfigurationFile(configStream);
        } catch (FlatwormConfigurationValueException ex)
        {
            throw new FlatwormCreatorException(ex.getMessage());
        } catch (FlatwormUnsetFieldValueException ex)
        {
            throw new FlatwormCreatorException(ex.getMessage());
        }
    }

    private void loadConfigurationFile(String config) throws FlatwormCreatorException
    {
        // Load configuration xml file
        try
        {
            ConfigurationReader parser = new ConfigurationReader();
            InputStream configStream = this.getClass().getClassLoader().getResourceAsStream(config);
            if (configStream != null)
            {
                ff = parser.loadConfigurationFile(configStream);
            } else
            {
                ff = parser.loadConfigurationFile(config);
            }
        } catch (FlatwormConfigurationValueException ex)
        {
            throw new FlatwormCreatorException(ex.getMessage());
        } catch (FlatwormUnsetFieldValueException ex)
        {
            throw new FlatwormCreatorException(ex.getMessage());
        }
    }

    /**
     * Open the newfile for writing<br>
     * 
     * @throws IOException - if there is some sort of filesystem related problem
     */
    public void open() throws FlatwormCreatorException
    {

        // Setup buffered writer
        try
        {
            if (file != null)
            {
                outputStream = new FileOutputStream(file);
            }
            bufOut = new BufferedWriter(new OutputStreamWriter(outputStream));
        } catch (FileNotFoundException ex)
        {
            throw new FlatwormCreatorException(ex.getMessage());
        }

    }

    /**
     * This is a convienence method that lets the writer know about your bean without having to pass a HashMap to
     * write()<br>
     * 
     * @param String name of bean as defined in your flatworm XML file
     * @param Object the bean
     */
    public void setBean(String name, Object bean)
    {
        beans.put(name, bean);
    }

    /**
     * Flatworm does not assume you want a newline between records, call this method to set your record delimiter.<br>
     * 
     * @param String what you want to seperate your records. Could be "\n"
     */
    public void setRecordSeperator(String recordSeperator)
    {
        this.recordSeperator = recordSeperator;
    }

    /**
     * Close the output file, since we are using buffered IO, this is very important.<br>
     * 
     * @param String what you want to seperate your records. Could be "\n"
     * @throws IOException - If the file system chooses not to close your file for some unknown reason
     */
    public void close() throws IOException
    {
        bufOut.close();
    }

    /**
     * Write information to the output file. Make sure you have called the setBean() method with the needed beans before
     * calling this method.<br>
     * 
     * @param String recordname as defined in your flatworm XML file
     * @throws IOException - If the file system has a problem with you writing information to the recently opened file.
     * @throws FlatwormCreationException - wraps varius Exceptions so client doesn't have to handle too many
     */
    public void write(String recordName) throws IOException, FlatwormCreatorException
    {
        Record record = ff.getRecord(recordName);
        RecordDefinition recDef = record.getRecordDefinition();

        Vector lines = recDef.getLines();

        // Iterate over lines
        for (Iterator itLines = lines.iterator(); itLines.hasNext();)
        {
            Line line = (Line) itLines.next();

            String delimit = line.getDelimeter();
            if (null == delimit)
                delimit = "";

            // record-ident contain what is considered hard-coded data
            // for the output line, these can be used to uniquely identify
            // lines for parsers. We need to write them out.
            Vector recIdents = record.getFieldIdentMatchStrings();
            for (Iterator itRecIdents = recIdents.iterator(); itRecIdents.hasNext();)
            {
                String id = (String) itRecIdents.next();
                bufOut.write(id + delimit);
            }

            // Iterate over record-element items
            Vector recElements = line.getRecordElements();
            for (Iterator itRecElements = recElements.iterator(); itRecElements.hasNext();)
            {
                RecordElement recElement = (RecordElement) itRecElements.next();

                HashMap convOptions = recElement.getConversionOptions();
                int length = 0;
                String beanRef = "";
                String type = "";
                try
                {
                    beanRef = recElement.getBeanRef();
                    type = recElement.getType();
                    length = recElement.getFieldLength();
                } catch (FlatwormUnsetFieldValueException ex)
                {
                    throw new FlatwormCreatorException(
                            "Could not deduce field length (please provide more data in your xml file for : " + beanRef
                                    + " " + ex.getMessage());
                }

                String val = "";
                ConversionHelper convHelper = ff.getConvertionHelper();
                try
                {
                    if (beanRef != null)
                    {
                        // Extract property name
                        Object bean = null;
                        String property = "";
                        try
                        {
                            int posOfFirstDot = beanRef.indexOf('.');
                            bean = beans.get(beanRef.substring(0, posOfFirstDot));
                            property = beanRef.substring(posOfFirstDot + 1);
                        } catch (ArrayIndexOutOfBoundsException ex)
                        {
                            throw new FlatwormCreatorException("Had trouble parsing : " + beanRef
                                    + " Its format should be <bean_name>.<property_name>");
                        }

                        // Convert to String for output
                        Object value = PropertyUtils.getProperty(bean, property);
                        val = convHelper.convert(type, value, convOptions, beanRef);
                        PropertyUtils.setProperty(bean, property, value);
                    } // end beanRef != null
                    // Handle any conversions that need to occur
                    if (val == null)
                    {
                        val = "";
                    }
                    val = convHelper.transformString(val, recElement.getConversionOptions(), recElement
                            .getFieldLength());

                    if (itRecElements.hasNext())
                        bufOut.write(val + delimit);
                    else
                        bufOut.write(val);

                } catch (Exception ex)    {                	
                	throw new FlatwormCreatorException("Exception getting/converting bean property : " + beanRef
                            + " : " + ex.getMessage());
                }

            } // end for all record elements

            if (null != recordSeperator)
                bufOut.write(recordSeperator);

        } // end for all lines

    } // end method

} // end class
