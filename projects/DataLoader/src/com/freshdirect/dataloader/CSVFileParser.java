package com.freshdirect.dataloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public abstract class CSVFileParser {
		    
    protected List<BadDataException> exceptions = null;
    
    
    /** creates a FlatFileParser
     */
    protected CSVFileParser() {
        super();       
        exceptions = new ArrayList<BadDataException>();
    }
    
    public List<BadDataException> getExceptions() {
        return exceptions;
    }
    
	 /** parser the file for the given file name
     * @param filename the path to the file to parse
     * @throws BadDataException any unrecoverable problems found within the file.  any other exceptions are returned from this method.
     * @return a list of exceptions encountered while parsing the file
     */
    public void parseFile(String filename) {
        
    	FileInputStream inputStream = null;
        try {
        	inputStream = new FileInputStream(filename);
        	CSVReader reader=new CSVReader(new InputStreamReader(inputStream));
        	String [] nextLine;
            int lineNumber = 0;
            
            while ((nextLine = reader.readNext()) != null) {
                
                ++lineNumber;
                if(lineNumber != 1) {
	                try {
	                	processLine(nextLine);
	                } catch (BadDataException bde) {
	                    //
	                    // add exceptions to the exception list
	                    //
						exceptions.add(new BadDataException(bde, "Error at line " + lineNumber + " in file " + filename + ": " + bde.getMessage()));
	                }
                }
            }
        } catch (IOException ioe) {
            exceptions.add(new BadDataException(ioe));
        } finally {
            if (inputStream != null) {
                try {
                	inputStream.close();
                } catch (IOException ioe) {
                    exceptions.add(new BadDataException(ioe));
                }
            }
        }
    }
    
    /** parser a single line
     * @param line the String to parse
     * @throws BadDataException any problems found within the line.
     */
    public void processLine(String[] line) throws BadDataException {
        makeObjects(line);
    }
    
    protected abstract void makeObjects(String[] line) throws BadDataException;
        
}
