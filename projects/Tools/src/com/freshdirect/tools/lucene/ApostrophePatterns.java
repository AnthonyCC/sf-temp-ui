package com.freshdirect.tools.lucene;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.freshdirect.cms.search.BrandNameExtractor;
import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.framework.util.StringUtil;

public class ApostrophePatterns {
	
	public static void main(String[] argv) throws IOException {
		
		int c = 0;
		
		if (argv.length == 0) {
			System.out.println("java com.freshdirect.tools.lucene.ApostrophePatterns <csv-search-terms> [ <output-file> ] [ <min-freq> ]");
			return;
		}
		FileInputStream is = new FileInputStream(argv[c++]);
		
		OutputStream os = argv.length > c ? (OutputStream)new FileOutputStream(argv[c++]) : (OutputStream)System.out;
		int min = argv.length > c ? Integer.parseInt(argv[c]) : 2;
		
		
		BrandNameExtractor extractor = new BrandNameExtractor();
		
		for(Iterator i = CSVUtils.rowIterator(is, false, false); i.hasNext();) {
			List row = (List)i.next();
			if (row.size() < 2) continue;
			if (row.get(0).toString().indexOf('\'') != -1) {
				int freq = Integer.parseInt(row.get(1).toString());
				if (freq < min) continue;
				List brandNames = extractor.extract(row.get(0).toString());
				for(Iterator j = brandNames.iterator(); j.hasNext();) {
					String brandName = StringUtil.removeAllWhiteSpace(j.next().toString());
					if (brandName.indexOf('\'') == -1) continue;
					os.write(CSVUtils.escape(brandName).getBytes());
					os.write(',');
					os.write(("" + freq).getBytes());
					StringTokenizer tokenizer = new StringTokenizer(brandName," \t\n\r\'");
					StringBuffer buff1 = new StringBuffer(brandName.length());
					StringBuffer buff2 = new StringBuffer(brandName.length());
					boolean w = false;
					while(tokenizer.hasMoreTokens()) {
						String token = tokenizer.nextToken();
						buff2.append(token);
						if (token.length() < 2) continue;
						if (w) buff1.append(' ');
						w = true;
						buff1.append(token);
					}
					if (w) {
						os.write(',');
						os.write(CSVUtils.escape(buff1.toString()).getBytes());
						os.write(',');
						os.write(("" + freq).getBytes());
					}
					os.write(',');
					os.write(CSVUtils.escape(buff2.toString()).getBytes());
					os.write(',');
					os.write(("" + freq).getBytes());
					os.write('\n');
					
				}
			}
			
		}
		
		
	}

}
