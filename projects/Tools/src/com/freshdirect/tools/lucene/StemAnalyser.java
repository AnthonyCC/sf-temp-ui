package com.freshdirect.tools.lucene;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import com.freshdirect.cms.search.ISOLatin1AccentFilter;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.SearchQuery;
import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.tools.lucene.SearchQueries.QueryFrequencies;
import com.freshdirect.tools.lucene.SearchQueries.QueryMap;
import com.freshdirect.tools.lucene.SearchQueries.QueryFrequency;


public class StemAnalyser extends QueryFrequencies {
	
	
	public interface Stemmer {
		
		public String getStem(String s);
	}
	
	public static Stemmer istvanStemmer = new Stemmer() {

		public String getStem(String s) {
			 
			if (s.endsWith("ies")) {
				return s.substring(0, s.length()-3) + "y";
			} else if (s.endsWith("es")) {
				return s.substring(0, s.length()-2);
			} else if (s.endsWith("s")) {
				return s.substring(0,s.length()-1);
			} else {
				return s;
			}
		}
	};
	
	public static Stemmer porterStemmer = new Stemmer() {
		
		public String getStem(String s) {
		    TokenStream ts =  new PorterStemFilter(new ISOLatin1AccentFilter(new LowerCaseTokenizer(new StringReader(s))));
		    try {
		        ts.incrementToken();
                        } catch (IOException e) {
                            return s.toLowerCase();
                        }
		    return ts.getAttribute(TermAttribute.class).term();
		}
	};
	
	
	
	private static void buildMap(InputStream is, Stemmer stemmer, Map stemMap) throws IOException {
	
		
		for(Iterator i = CSVUtils.rowIterator(is, false, false); i.hasNext();) {
			
			
			List row = (List)i.next();
			
			if (row.size() >= 2) {
				StringTokenizer st = new StringTokenizer(row.get(0).toString());
				int freq = Integer.parseInt(row.get(1).toString());
				while(st.hasMoreElements()) {
					String s = st.nextElement().toString().toLowerCase();
					stemMap.put(stemmer.getStem(s), new QueryFrequency(s,freq));
				}
			} 
		}
	}
	
	
	public static void main(String[] argv) throws Exception {
		Map map = new QueryMap();
		
		int c = 0;
	
		if (argv.length == 0) {
			System.out.println("java com.freshdirect.tools.lucene.StemAnalyser <csv-search-terms> [ <output-file> ] [ <min-freq> ]");
			return;
		}
		buildMap(new FileInputStream(argv[c++]),porterStemmer,map);
		OutputStream os = argv.length > c ? (OutputStream)new FileOutputStream(argv[c++]) : (OutputStream)System.out;
		int min = argv.length > c ? Integer.parseInt(argv[c]) : 2;
		
		List sortedEntries = new ArrayList(map.size());
		
		for(Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry)i.next();
			QueryFrequencies qf = (QueryFrequencies)e.getValue();
			if (qf.size() > 1) sortedEntries.add(e);
		}
		
		Collections.sort(
			sortedEntries,
			new Comparator() {

				public int compare(Object o1, Object o2) {
					QueryFrequencies qf1 = (QueryFrequencies)((Map.Entry)o1).getValue();
					QueryFrequencies qf2 = (QueryFrequencies)((Map.Entry)o2).getValue();
					
					
					return - (qf1.getMaxFrequency() - qf2.getMaxFrequency());
				}
				
			}
		);
		
		
		
		for(Iterator i = sortedEntries.iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry)i.next();
			QueryFrequencies qfs = (QueryFrequencies)e.getValue();
			int fc = 0;
			for(int j=0; j< qfs.size(); ++j) {
			   if (qfs.get(j).getFrequency() >= min) ++fc;	
			}
			if (fc < 2) continue;
			for(int j=0, w = 0; j< qfs.size(); ++j) {
				QueryFrequency qf = qfs.get(j);
				if (qf.getFrequency() < min) continue;
				if (w > 0) os.write(',');
				os.write(CSVUtils.escape(qf.getValue()).getBytes("utf-8"));
				os.write(',');
				os.write(("" + qf.getFrequency()).getBytes());
				++w;
			}
			os.write("\n".getBytes());
		}	
	}

}
