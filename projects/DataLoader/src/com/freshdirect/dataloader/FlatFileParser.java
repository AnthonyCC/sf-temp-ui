/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader;

import java.util.*;
import java.io.*;


/** base class for loader components that read data from text files
 * it assumes that data in the file contains one record per line
 * it can return a list of exceptions found while parsing a file
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class FlatFileParser {
    
    /** the list of fields contained in each line of a file
     */
    protected List fields = null;
    
    protected List exceptions = null;
    
    /** creates a FlatFileParser
     */
    protected FlatFileParser() {
        super();
        fields = new ArrayList();
        exceptions = new ArrayList();
    }
    
    /**
     * gets the list of fields that are contained each line of a file
     *
     * @return a List of Field objects in the order they appear on each line of a text file to be parsed
     */
    protected List getFields() {
        return fields;
    }
    
    public List getExceptions() {
        return exceptions;
    }
    
    /** parser the file for the given file name
     * @param filename the path to the file to parse
     * @throws BadDataException any unrecoverable problems found within the file.  any other exceptions are returned from this method.
     * @return a list of exceptions encountered while parsing the file
     */
    public void parseFile(String filename) {
        BufferedReader lines = null;
        //
        // keep a list of excepttions found in this file
        //
        try {
            
            //
            // run through every line in the file
            //
            lines = this.createReader(filename);
            String line = null;
            int lineNumber = 0;
            while (null != (line = lines.readLine())) {
                //
                // increment line count for error messages
                //
                ++lineNumber;
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
            if (lines != null) {
                try {
                    lines.close();
                } catch (IOException ioe) {
                    exceptions.add(new BadDataException(ioe));
                }
            }
        }
    }
    
    protected BufferedReader createReader(String fileName) throws IOException {
		return new BufferedReader(new FileReader(fileName));
    }
    
    /** parser a single line
     * @param line the String to parse
     * @throws BadDataException any problems found within the line.
     */
    public void parseLine(String line) throws BadDataException {
        makeObjects(tokenize(line));
    }
    
    /**
     * provides special line by line handling for subclasses that need to massage
     * or examine the data a bit before parsing
     *
     * @param lineNumber the line number of the file the parsing in currently operating on
     * @param line the contents of the current line before it is tokenized/parsed
     *
     * @return true, if the parser should attempt to interperet this line, false if this line should be skipped
     */
    protected boolean processLine(int lineNumber, String line) {
        // do nothing by default
        return true;
    }
    
    /**
     * a template method that must be defined by implementors
     * subclasses will know how to assemble model objects
     * from a a hash of tokens
     * @param tokens a HashMap containing parsed tokens from a single line
     * of a text file, keyed by their field names
     * @throws BadDataException an problems while trying to assemble objects from the
     * supplied tokens
     */
    protected abstract void makeObjects(HashMap tokens) throws BadDataException;
    
    /**
     * using a list of fields provided by a concrete subclass
     * chop up a line into tokens and returns the result
     * as a set of key-value pairs in a HashMap
     * @param line a single line from a text file
     * @throws BadDataException any problems encountered converting the line into tokens
     * @return a HashMap of String tokens from a line of a text file,
     * keyed by their field names
     */
    protected abstract HashMap tokenize(String line) throws BadDataException;
    
    /** a convenience method to return a token value from a HashMap of
     * tokens by a field name
     * @param tokens a HashMap of parsed tokens
     * @param fieldName the name of the field to retreive the value of
     * @throws BadDataException any problems encountered retreiving the token's value
     * @return the token's value as a String
     */
    protected String getString(HashMap tokens, String fieldName) throws BadDataException {
        //
        // make sure the field name is valid
        //
        Field field = null;
        Iterator fieldIter = fields.iterator();
        while (fieldIter.hasNext()) {
            Field f = (Field) fieldIter.next();
            if (f.getName().equals(fieldName)) {
                field = f;
                break;
            }
        }
        if (field == null)
            throw new BadDataException("Unknown field name \"" + fieldName + "\" is not defined in " + this.getClass().getName());
        //
        // get the field and trim any extra whitespace
        //
        String s = ((String)tokens.get(fieldName)).trim();
        //
        // check if this is a required field
        //
        if (field.isRequired() && s.equals("")) {
            throw new BadDataException("Required field \"" + fieldName + "\" was empty");
        }
        return s;
    }
    
    /** retreives the value of a token as an int from a field name
     * @param tokens a HashMap of parsed tokens
     * @param fieldName the name of the field to retreive
     * @throws BadDataException an problems retieving the token's value
     * @return the token's int value
     */
    protected int getInt(HashMap tokens, String fieldName) throws BadDataException {
        //
        // first get as a string
        //
        String s = getString(tokens, fieldName);
        //
        // try to convert to an int
        //
        if(s == null || "".equals(s)){
        	return 0;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            throw new BadDataException(nfe, "Unable to read field \"" + fieldName + "\" as an integer");
        }
    }
    
    /** retrieves the value of a token as a double given the field's name
     * @param tokens a HashMap of parsed tokens
     * @param fieldName the name of the field to retreive
     * @throws BadDataException an problems getting the token's value as a double
     * @return the token's value as a double
     */
    protected double getDouble(HashMap tokens, String fieldName) throws BadDataException {
        //
        // first get as a string
        //
        String s = getString(tokens, fieldName);
        //
        // try to convert to a double
        //
        if(s == null || "".equals(s)){
        	return 0.0;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            throw new BadDataException(nfe, "Unable to read field \"" + fieldName + "\" as a double");
        }
    }
    
    /** retrieves the value of a token as a double given the field's name and a number of digits after the decimal
     * used for fields that have an implied decimal
     * @param tokens a HashMap of parsed tokens
     * @param fieldName the name of the field to retreive
     * @param decimals number of digits after the implied decimal
     * @throws BadDataException an problems getting the token's value as a double
     * @return the token's value as a double
     */
    protected double getDouble(HashMap tokens, String fieldName, int decimals) throws BadDataException {
        //
        // first get as a string
        //
        String s = getString(tokens, fieldName);
        s = s.substring(0, s.length()-decimals) + "." + s.substring(s.length()-decimals);
        //
        // try to convert to a double
        //
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            throw new BadDataException(nfe, "Unable to read field \"" + fieldName + "\" as a double");
        }
    }
    
    /** retrieves the value of a token as a double given the field's name and a number of digits after the decimal
     * used for fields that have an implied decimal in COBOL signed format
     * @param tokens a HashMap of parsed tokens
     * @param fieldName the name of the field to retreive
     * @param decimals number of digits after the implied decimal
     * @throws BadDataException an problems getting the token's value as a double
     * @return the token's value as a double
     */
    protected double getSignedDouble(HashMap tokens, String fieldName, int decimals) throws BadDataException {
        //
        // first get as a string
        //
        String s = getString(tokens, fieldName);
        String numeric = s.substring(0, s.length()-1);
        char lastChar = s.charAt(s.length()-1);
        //
        // convert last character to a digit and a sign
        //
        String lastDigit = null;
        double sign = 0.0;
        if ('}' == lastChar) {
            lastDigit = "0";
            sign = -1.0;
        } else if ('{' == lastChar) {
            lastDigit = "0";
            sign = 1.0;
        } else if (('A' <= lastChar) && ('R' >= lastChar)) {
            if ('I' >= lastChar) {
                sign = 1.0;
                lastDigit = Integer.toString(1 + Character.getNumericValue(lastChar) - Character.getNumericValue('A'));
            } else {
                sign = -1.0;
                lastDigit = Integer.toString(1 + Character.getNumericValue(lastChar) - Character.getNumericValue('J'));
            }
        } else {
            throw new BadDataException("Last character of signed double \"" + s + "\" is invalid.");
        }
        //
        // tack on the last digit and put the decimal point in the right place
        //
        numeric += lastDigit;
        numeric = numeric.substring(0, numeric.length()-decimals) + "." + numeric.substring(numeric.length()-decimals);
        //
        // try to convert to a double and multiply by the sign
        //
        try {
            return sign * Double.parseDouble(numeric);
        } catch (NumberFormatException nfe) {
            throw new BadDataException(nfe, "Unable to convert field \"" + fieldName + "\" to a double");
        }
        
    }
    
    /** converts a String to a Date
     * @param dateString the string representation of a Date
     * @param dateFormat a format string used to parse the date fields
     * @throws BadDataException an problems getting the token's value as a Date
     * @return the string's value as a Date
     */
    protected java.util.Date getDate(String dateString, String dateFormat) throws BadDataException {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat);
            return sdf.parse(dateString);
        } catch (java.text.ParseException pe) {
            throw new BadDataException(pe, "Unable to convert \"" + dateString + "\" to a Date formatted by \"" + dateFormat + "\"");
        }
    }
    
    /** retrieves the value of a token as a Date given the field's name
     * @param tokens a HashMap of parsed tokens
     * @param fieldName the name of the field to retreive
     * @param dateFormat a format string used to parse the date fields
     * @throws BadDataException an problems getting the token's value as a Date
     * @return the token's value as a Date
     */
    protected java.util.Date getDate(HashMap tokens, String fieldName, String dateFormat) throws BadDataException {
        //
        // first get as a string
        //
        String s = getString(tokens, fieldName);
        //
        // try to convert to a Date
        //
        java.util.Date d = getDate(s, dateFormat);
        if (!"MMdd".equals(dateFormat)) {
            return d;
        }
        //
        // date format didn't specify where to find the year, so guess at it
        // assume it's within the current year
        //
        GregorianCalendar today = new GregorianCalendar();
        today.setTime(new Date());
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        if(cal.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.MONTH) == Calendar.JANUARY){
        	cal.set(Calendar.YEAR, today.get(Calendar.YEAR)-1);
        }else{
        	cal.set(Calendar.YEAR, today.get(Calendar.YEAR));
        }
        return cal.getTime();
    }
    
    protected String stripLeadingZeros(String in) {
        StringBuffer buff = new StringBuffer();
        for (int i=0; i<in.length(); i++) {
            char c = in.charAt(i);
            if ((c == '0') && (buff.length() == 0)) continue;
            buff.append(c);
        }
        return buff.toString();
    }
    
    public String debug() {
        return "";
    }
    
    /** an inner class taht represents an individual field value on a line of text
     */
    protected class Field {
        
        /** the name of the field
         */
        protected String name;
        /** the number of characters in the field
         */
        protected int length;
        /** indicates whether this field is required to be defined on each line
         */
        protected boolean required;
        
        /** constructor specifying a name, length, and whether this field is required
         * @param tn the field name
         * @param len the length of the field
         * @param req true if this is a required field
         */
        public Field(String tn, int len, boolean req) {
            name = tn;
            length = len;
            required = req;
        }
        
        /** the name of a field
         * @return the field's name
         */
        public String getName() {
            return name;
        }
        
        /** the length of a field
         * @return the field's length
         */
        public int getLength() {
            return length;
        }
        
        /** indicates whether this is a required field
         * @return true if this field is required
         */
        public boolean isRequired() {
            return required;
        }
        
    }
    
    
}
