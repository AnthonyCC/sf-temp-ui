/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** base class for loader components that read data from text files
 * it can return a list of exceptions found while parsing a file
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public abstract class FieldDelimitedFileParser extends FlatFileParser {
    
    /** creates a FlatFileParser
     */    
    protected FieldDelimitedFileParser() {
        super();
    }
    
    /**
     * using a list of fields provided by a concrete subclass
     * chop up a line into tokens and returns the result
     * as a set of key-value pairs in a HashMap
     * @param line a single line from a text file
     * @throws BadDataException any problems encountered converting the line into tokens
     * @return a HashMap of String tokens from a line of a text file,
     * keyed by their field names
     */
    @Override
    protected Map<String, String> tokenize(String line) throws BadDataException {
        //
        // iterate through the list of fields are read each token
        // from the line
        //
        Iterator<Field> iter = fields.iterator();
        HashMap<String, String> retval = new HashMap<String, String>();
        int startPosition = 0;
        while (iter.hasNext()) {
            Field f = iter.next();
            String name = f.getName();
            int length = f.getLength();
            if (length == -1) {
                //
                // read everything until the end of the line
                //
                try {
                    retval.put(name, line.substring(startPosition).trim());
                } catch (StringIndexOutOfBoundsException sie) {
                    throw new BadDataException(sie, "Found a line that was too short, couldn't read " + f.getName());
                }
            } else {
                //
                // just read the specified number of characters
                //
            	startPosition = tokenize(line, retval, startPosition, name,
						length);
            }
        }
        return retval;
    }
    
    protected int tokenize(String line, Map<String, String> retval, int startPosition,
			String name, int length) throws BadDataException {
		int endPosition = startPosition + length;
		try {
		    retval.put(name, line.substring(startPosition, endPosition).trim());
		} catch (StringIndexOutOfBoundsException sie) {
		    throw new BadDataException(sie, "Found a line that was too short, couldn't read " + name);
		}
		startPosition = endPosition;
		return startPosition;
	}
    
}
