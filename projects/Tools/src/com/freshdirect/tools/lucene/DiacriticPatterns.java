package com.freshdirect.tools.lucene;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import com.freshdirect.cms.search.ISOLatin1AccentFilter;
import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.framework.util.StringUtil;

public class DiacriticPatterns {
	public static void main(String[] argv) throws IOException {
		
		int c = 0;
		
		if (argv.length == 0) {
			System.out.println("java com.freshdirect.tools.lucene.DiacriticPatterns <csv-search-terms> [ <output-file> ] [ <min-freq> ]");
			return;
		}
		FileInputStream is = new FileInputStream(argv[c++]);
		
		OutputStream os = argv.length > c ? (OutputStream)new FileOutputStream(argv[c++]) : (OutputStream)System.out;
		int min = argv.length > c ? Integer.parseInt(argv[c]) : 2;
		
		
		
		for(Iterator i = CSVUtils.rowIterator(is, false, false); i.hasNext();) {
			List row = (List)i.next();
			if (row.size() < 2) continue;
			int freq = Integer.parseInt(row.get(1).toString());
			if (freq < min) continue;
			StringTokenizer tokenizer = new StringTokenizer(row.get(0).toString());
			while(tokenizer.hasMoreTokens()) {
				String queryToken = tokenizer.nextToken().toLowerCase();
				if (!StringUtil.hasDiacritic(queryToken)) continue;
				ISOLatin1AccentFilter filter = new ISOLatin1AccentFilter(new LowerCaseTokenizer(new StringReader(queryToken)));
				filter.incrementToken();
				String maskedToken = filter.getAttribute(TermAttribute.class).term();
				os.write(queryToken.getBytes("utf-8"));
				os.write(',');
				os.write(("" + freq).getBytes());
				os.write(',');
				os.write(maskedToken.getBytes("utf-8"));
				os.write(',');
				os.write(("" + freq).getBytes());
				os.write('\n');
			}
			
		}
		
		
	}
}
