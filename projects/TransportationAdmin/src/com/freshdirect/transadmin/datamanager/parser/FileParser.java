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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConfigurationValueException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormConversionException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInputLineLengthException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormInvalidRecordException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormParserException;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormUnsetFieldValueException;

/**
 * Used to read a flatfile. Encapsulates parser setup and callback mechanism. This class wraps the functionality that
 * used to be in the main() of the examples. This way, the client knows less about the internal workings of FlatWorm.
 */

public class FileParser
{

    private HashMap callbacks;

    private String file;

    private FileFormat ff;

    private static Class[] METHODSIG = { MatchedRecord.class };

    private static Class[] EXCEPTIONSIG = { String.class, String.class };

    private static String EXCEPTIONS = "exception";

    private Logger cat = Logger.getLogger(FileParser.class);

    private BufferedReader bufIn = null;

    /**
     * Constructor for FileParser<br>
     * 
     * @param String full path to the FlatWorm XML configuration file
     * @param String full path to input file
     * @throws FlatwormParserException - wraps FlatwormConfigurationValueException & FlatwormUnsetFieldValueException
     *             (to reduce number of exceptions clients have to be aware of)
     */
    public FileParser(String config, String file) throws FlatwormParserException
    {

        this.file = file;
        callbacks = new HashMap();

        try
        {

            ConfigurationReader parser = new ConfigurationReader();
            ff = parser.loadConfigurationFile(config);

        } catch (FlatwormConfigurationValueException ex)
        {
            throw new FlatwormParserException(ex.getMessage());
        } catch (FlatwormUnsetFieldValueException ex)
        {
            throw new FlatwormParserException(ex.getMessage());
        }
    }

    /**
     * Let's the parser know which object and method to call when a record has been successfully parsed and needs
     * handling<br>
     * 
     * @param String recordname from your FlatWorm XML file
     * @param Object handler object which will responding to parse events
     * @param String name of handler method from the handler class (passing this prevents java.reflection code in
     *            client)
     * @throws NoSuchMethodException - This will most likely be caused by a oversight on your part (i.e. not yet
     *             implemented this class in your handler or mispelled the method name when passing it to this method)
     */
    public void setBeanHandler(String recordName, Object obj, String methodName) throws NoSuchMethodException
    {
        Method method = obj.getClass().getMethod(methodName, METHODSIG);

        callbacks.put(recordName, new Callback(obj, method));
    }

    /**
     * Let's the parser know which object and method to call when an exception occurs during record processing.<br>
     * 
     * @param String recordname from your FlatWorm XML file
     * @param Object handler object which will responding to parse events
     * @param String name of handler method from the handler class (passing this prevents java.reflection code in
     *            client)
     * @throws NoSuchMethodException - This will most likely be caused by a oversight on your part (i.e. not yet
     *             implemented this class in your handler or mispelled the method name when passing it to this method)
     */
    public void setExceptionHandler(Object obj, String methodName) throws NoSuchMethodException
    {
        Method method = obj.getClass().getMethod(methodName, EXCEPTIONSIG);

        callbacks.put(EXCEPTIONS, new Callback(obj, method));
    }

    /**
     * Open the buffered reader for the input file<br>
     * 
     * @throws FileNotFoundException - If the file you supplied does not happen to exist.
     */
    public void open() throws FileNotFoundException
    {
        InputStream in = new FileInputStream(file);
        bufIn = new BufferedReader(new InputStreamReader(in));

    }

    /**
     * Close the input file<br>
     * 
     * @throws IOException - Should the filesystem choose to complain about closing an existing file opened for reading.
     */
    public void close() throws IOException
    {

        bufIn.close();
    }

    /**
     * Read the entire input file. This method will call your handler methods, if defeined, to handle Records it parses.
     * <br>
     * <br>
     * <b>NOTE:</b> All exceptions are consumed and passed to the exception handler method you defined (The offending
     * line is provided just in case you want to do something with it.<br>
     */
    public void read()
    {

        MatchedRecord results = null;
        boolean exception = false;
        do
        {
            exception = true;

            // Attempt to parse the next line
            try
            {
                results = ff.getNextRecord(bufIn);
                exception = false;
            } catch (FlatwormInvalidRecordException ex)
            {
                doCallback(EXCEPTIONS, "FlatwormInvalidRecordException", ff.getLastLine());
            } catch (FlatwormInputLineLengthException ex)
            {
                doCallback(EXCEPTIONS, "FlatwormInputLineLengthException", ff.getLastLine());
            } catch (FlatwormUnsetFieldValueException flatwormUnsetFieldValueError)
            {
                doCallback(EXCEPTIONS, "FlatwormUnsetFieldValueException", ff.getLastLine());
            } catch (FlatwormConversionException ex)
            {
                doCallback(EXCEPTIONS, "FlatwormConversionException", ff.getLastLine());
            } catch (Exception ex)
            {
                doCallback(EXCEPTIONS, ex.getMessage(), ff.getLastLine());
            }

            if (null != results)
            {
                String recordName = results.getRecordName();
                if (callbacks.containsKey(recordName))
                {
                    doCallback(recordName, results, null);
                }
            }
        } while ((null != results) || exception);
    }

    /**
     * Encapsulated details about calling client's handler methods (for exceptions too)
     * 
     * @param String key to lookup callback method
     * @param Object first argument for callback - used for record handlers and exceptions
     * @param Object second argument for callback - used for only for exceptions. Contains a string that contains the
     *            offending line from the input file <br>
     *            <br>
     *            <b>NOTE:</b> All exceptions are consumed and passed to the exception handler method you defined (The
     *            offending line is provided just in case you want to do something with it.<br>
     */
    private void doCallback(String key, Object arg1, Object arg2)
    {
        Callback callback = null;
        try
        {
            callback = (Callback) callbacks.get(key);
            Method method = callback.getMethod();
            Object[] args = null;

            if (null == arg2)
            {
                args = new Object[1];
                args[0] = arg1;
            } else
            {
                args = new Object[2];
                args[0] = arg1;
                args[1] = arg2;
            }

            method.invoke(callback.getInstance(), args);
        } catch (Exception ex)
        {
            // Something happened during the method call
            String details = callback.getInstance().getClass().getName() + "." + callback.getMethod().getName();
            cat.error("Bad handler method call: " + details);
        }
    }
}
