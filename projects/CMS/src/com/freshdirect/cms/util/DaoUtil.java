/*
 * Created on Feb 18, 2005
 */
package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;

/**
 * Utility methods that are useful in DAOs dealing with 
 * content objects.
 */
public class DaoUtil {

	private DaoUtil() {
	}

	/**
	 * Converts a set of keys into chunks of 1000 (encoded, comma separated,
	 * SQL-quoted). Useful for SQL "IN" clauses.
	 * 
	 * @param keys Set of {@link ContentKey} (never null)
	 * @return
	 */
	public static String[] chunkContentKeys(Set keys) {
		return chunkContentKeys(keys, true);
	}
	
	/**
	 * Converts a set of keys into chunks of 1000 (encoded or just the ID,
	 * comma separated, SQL-quoted). Useful for SQL "IN" clauses.
	 * 
	 * @param keys Set of {@link ContentKey} (never null)
	 * @param encoded if true the keys are encoded, if false only the IDs are printed
	 * @return 
	 */
	public static String[] chunkContentKeys(Set keys, boolean encoded) {
		/** List of StringBuffer */
		List bufs = new ArrayList();
		int count = 0;
		StringBuffer currIds = new StringBuffer();
		for (Iterator i = keys.iterator(); i.hasNext(); count++) {
			ContentKey key = (ContentKey) i.next();
			if (count == 1000) {
				currIds = new StringBuffer();
				count = 0;
			}

			if (count > 0) {
				currIds.append(',');
			}

			currIds.append("'");
			if(encoded)
				currIds.append(escapeKey(key));
			else
				currIds.append(escape(key.getId()));
			currIds.append("'");

			if (count == 0)
				bufs.add(currIds);
		}

		String[] s = new String[bufs.size()];
		for (int i = 0; i < bufs.size(); i++) {
			s[i] = ((StringBuffer) bufs.get(i)).toString();
		}
		return s;
	}

	/**
	 * Encode and SQL-escape a a content key.
	 * 
	 * @param key keu to escape (never null)
	 * @return encoded, escaped key as string (never null)
	 */
	public static String escapeKey(ContentKey key) {
		return escape(key.getEncoded());
	}

	/**
	 * SQL-escape a string by replacing single quotes with two single quotes.
	 * 
	 * @param str string to escape (never null)
	 * @return string with escaping applied (never null)
	 */
	public static String escape(String str) {
		return str.replaceAll("'", "''");
	}
	
}