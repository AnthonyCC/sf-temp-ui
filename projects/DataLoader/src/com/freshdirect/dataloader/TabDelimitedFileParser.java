/*
 * NutritionParser.java
 *
 * Created on August 20, 2001, 8:39 PM
 */

package com.freshdirect.dataloader;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author  knadeem
 * @version
 */
public abstract class TabDelimitedFileParser extends FlatFileParser {
    
    /** Creates new NutritionParser */
    protected TabDelimitedFileParser() {
        super();
    }
    
    @Override
    protected HashMap<String, String> tokenize(String line) throws BadDataException {
        //
        // iterate through the list of fields are read each token
        // from the line
        //
        Iterator<Field> iter = fields.iterator();
        HashMap<String, String> retval = new HashMap<String, String>();
        int start = 0;
        int end = line.indexOf("\t", start);
        //
        // check if the very first field is empty if it is add a empty string to the
        // token hash map so the subsequent values are kept in right order
        //
        if ((end == start) && (iter.hasNext())) {
            Field f = iter.next();
            retval.put(f.getName(), "");
            start++;
            end = line.indexOf("\t", start);
        }
        
        while (iter.hasNext()) {
            Field f = iter.next();
            String value = "";
            if (end == -1) {
                value = line.substring(start, line.length());
            } else {
                value = line.substring(start, end);
            }
            retval.put(f.getName(), value.trim());
            start = end;
            end = line.indexOf("\t", start+1);
        }
        return retval;
    }
    
}