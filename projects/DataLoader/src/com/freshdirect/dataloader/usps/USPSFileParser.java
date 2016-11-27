/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.usps;

import java.io.FileReader;
import java.io.IOException;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.FieldDelimitedFileParser;

/** base class for loader components that read data from text files
 * it assumes that data in the file contains one record per line
 * it can return a list of exceptions found while parsing a file
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class USPSFileParser extends FieldDelimitedFileParser {
    
    protected int charsPerLine;
    
    /** creates a FlatFileParser
     */
    protected USPSFileParser(int charsPerLine) {
        super();
        this.charsPerLine = charsPerLine;
    }
    
    /** parser the file for the given file name
     * @param filename the path to the file to parse
     * @throws BadDataException any unrecoverable problems found within the file.  any other exceptions are returned from this method.
     * @return a list of exceptions encountered while parsing the file
     */
    @Override
    public void parseFile(String filename) {
        FileReader reader = null;
        //
        // keep a list of exceptions found in this file
        //
        try {
            //
            // run through every line in the file
            //
            reader = new FileReader(filename);
            String line = null;
            int lineNumber = 0;
            char[] charBuffer = new char[charsPerLine];
            while (-1 != reader.read(charBuffer)) {
                //
                // increment line count for error messages
                //
                ++lineNumber;
                //
                // make a new String from the characters just read
                //
                line = new String(charBuffer);
                //
                // ignore lines that are blank or are comments
                //
                if ("".equals(line.trim()) || line.startsWith("#"))
                    continue;
                //
                // hook for extra line by line data massaging
                // skip this line if filterLine returns false
                //
                if (!processLine(lineNumber, line))
                    continue;
                //
                // concrete subclasses know how to make objects
                // from a hash of tokens
                //
                try {
                    parseLine(line);
                } catch (BadDataException bde) {
                    //
                    // add exceptions to the exception list
                    //
					exceptions.add(new BadDataException(bde, "Error at line " + lineNumber + " in file " + filename + ": " + bde.getMessage()));
                }
            }
        } catch (IOException ioe) {
            exceptions.add(new BadDataException(ioe));
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    exceptions.add(new BadDataException(ioe));
                }
            }
        }
    }
    
}
