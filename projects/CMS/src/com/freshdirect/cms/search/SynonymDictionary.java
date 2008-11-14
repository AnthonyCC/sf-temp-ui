/**
 * 
 */
package com.freshdirect.cms.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.framework.conf.ResourceUtil;

/**
 * @author zsombor
 *
 */
public class SynonymDictionary {
	
	public static final String SYNONYM_LIST_KEY = "FDFolder:synonymList";
	
	
	Map wordMap = new HashMap(); 
	
	public String translate(String query) {
		if (query!=null) {
			String[] translation ;
			synchronized(wordMap) {
				translation = (String[]) wordMap.get(query.toLowerCase().trim());
			}
			if (translation!=null) {
				StringBuffer buf = new StringBuffer(query);
				for (int i=0;i<translation.length;i++) {
					buf.append(" OR ").append(translation[i]);
				}
				return buf.toString();
			}
		}
		return query;
	}
	
	public String getAdditionalKeywords(String fullName) {
		synchronized(wordMap) {
			String lowerCased = fullName.toLowerCase();
			StringBuffer additionalKeywords = new StringBuffer();
			for (Iterator keySetIterator =wordMap.keySet().iterator();keySetIterator .hasNext();) {
				String key = (String) keySetIterator.next();
				if (lowerCased.indexOf(key)!=-1) {
					String[] values = (String[]) wordMap.get(key);
					for (int i=0;i<values.length;i++) {
						additionalKeywords.append(',').append(values[i]);
					}
				}
			}
			return additionalKeywords.toString();
		}
	}
	

	public void addSynonym(String word, String[] alternates) {
		synchronized(wordMap) {
			String key = word.toLowerCase().trim();
			String[] result = (String[]) wordMap.get(key);
			if (result==null) {
				wordMap.put(key, alternates);
			} else {
				String[] merged = new String[result.length + alternates.length];
				System.arraycopy(result, 0, merged, 0, result.length);
				System.arraycopy(alternates, 0, merged, result.length, alternates.length);
				wordMap.put(key, merged);
			}
		}
	}

	public void addSynonym(String word, String synonymTo) {
		addSynonym(word, StringUtils.split(synonymTo, ','));
	}
	
	public void parseSynonymes(String resource) throws IOException {
		parseSynonymes(ResourceUtil.openResource(resource));
	}

	public void parseSynonymes(InputStream input) throws IOException {
		parseSynonymes(new BufferedReader(new InputStreamReader(input)));
	}

	public void parseSynonymes(URL url) throws IOException {
		parseSynonymes(new BufferedReader(new InputStreamReader(url.openStream())));
	}
	
	public void parseSynonymes(File file) throws IOException {
		parseSynonymes(new BufferedReader(new FileReader(file)));
	}
	
	
	
	public void parseSynonymes(BufferedReader reader) throws IOException {
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.trim();
			if (line.length()>0 && line.charAt(0)!='#') {
				parseSynonymLine(line);
			}
		}
	}

	private void parseSynonymLine(String line) {
		String[] keyValue = StringUtils.split(line, ':');
		if (keyValue.length>=2) {
			String keys = keyValue[0];
			String values = keyValue[1];
			String[] keyArray = StringUtils.split(keys, ',');
			String[] synonym = StringUtils.split(values, ',');
			for (int i=0;i<synonym.length;i++) {
				synonym[i] = synonym[i].trim();
			}
			synchronized(wordMap) {
				for (int i=0;i<keyArray.length;i++) {
					String key = keyArray[i].toLowerCase().trim();
					if (key.length()>0) {
						wordMap.put(key, synonym);
					}
				}
			}
		}
	}

	public static SynonymDictionary createFromCms() {
		SynonymDictionary dict = new SynonymDictionary();
		CmsManager instance = CmsManager.getInstance();
		
		ContentNodeI synRootNode = instance.getContentNode(ContentKey.decode(SynonymDictionary.SYNONYM_LIST_KEY));
		Set synonymKeys = ContentNodeUtil.collectReachableKeys(synRootNode, FDContentTypes.SYNONYM);
		Map synonymNodes = instance.getContentNodes(synonymKeys);
		for (Iterator contentNodeIterator = synonymNodes.values().iterator(); contentNodeIterator
				.hasNext();) {
			ContentNodeI node = (ContentNodeI) contentNodeIterator.next();
			String from = getAttribute(node, "word");
			String synonymTo = getAttribute(node, "synonymValue");
			if (from != null && synonymTo != null) {
				dict.addSynonym(from, synonymTo);
			}
		}
		return dict;
	}
	
	private static String getAttribute(ContentNodeI node, String name) {
		AttributeI attribute = node.getAttribute(name);
		if (attribute!=null) {
			Object value = attribute.getValue();
			if (value instanceof String) {
				return (String) value;
			}
		}
		return null;
	}
	
}
