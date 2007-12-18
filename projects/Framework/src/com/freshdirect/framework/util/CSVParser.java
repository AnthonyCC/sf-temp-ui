package com.freshdirect.framework.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.regex.Pattern;

/**
 * Parse comma separated CSV files.
 * 
 * @todo handle escaping, quoting
 * 
 * @author istvan
 *
 */
public class CSVParser {
	
	private static Pattern commaPattern = Pattern.compile(",");
	
	/**
	 * Parse a comma separated CSV file.
	 * 
	 * @param is input stream
	 * @param keepEmpty whether to keep empty rows
	 * @return list of rows (List<List<String>>)
	 * @throws IOException
	 */
	public static List parse(InputStream is, boolean keepEmpty) throws IOException {

	      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	      List result = new ArrayList();

	      for(;;) {
	         String line = reader.readLine();
	         if (line == null) break;
	         if (line.length() == 0 && !keepEmpty) continue;
	         int c = 0; // needed, so split retains trailing empty columns
	         for(int i=0; i< line.length(); ++i) if (line.charAt(i) == ',') ++c;
	         result.add(Arrays.asList(commaPattern.split(line,c+1)));
	      }

	      return result;
	   }
}
