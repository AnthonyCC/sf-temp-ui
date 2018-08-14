package com.freshdirect.webapp.unbxdanalytics.autosuggest;

import com.freshdirect.fdstore.content.EnumWinePrice;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Logger;

public class AutoSuggestData {
	private final String autosuggest_type;
	private final String autosuggest_suggestion;
	private final String field_value;
	private final String field_name;
	private final String src_field;
	private final String pid;
	private int unbxdprank;
	private final String internal_query;
	
	private static final Logger LOG = LoggerFactory.getInstance(AutoSuggestData.class);
	private AutoSuggestData(String autosuggest_type, String autosuggest_suggestion, String field_value, String field_name,
			String src_field, String pid, int unbxdPrank, String internal_query) {
		this.autosuggest_type= autosuggest_type;
		this.autosuggest_suggestion = autosuggest_suggestion;
		this.field_value = field_value;
		this.field_name = field_name;
		this.src_field=src_field;
		this.pid=pid;
		this.unbxdprank = unbxdPrank;
		this.internal_query= internal_query;
	}
	
	public static AutoSuggestData withData(String autosuggest,String doctype , String internQuery, int score, String id) {
		final String autosuggest_type = doctype;
		final String autosuggest_suggestion = autosuggest;
		String field_value = null;
		String field_name = null;
		if(autosuggest_type != null && AutoSuggestType.IN_FIELD.name().equalsIgnoreCase(autosuggest_type)) {
			field_value = doctype;
			field_name= autosuggest;
		}

		String src_field=null;
		if(doctype != null) {
			src_field= null; 		/* Currently for freshdirect we do not use infields, we might use this in future */
		}
		
		 String pid = null;
		if(AutoSuggestType.POPULAR_PRODUCTS.name().equalsIgnoreCase(autosuggest_type))
			pid=id;
		final int unbxdprank = score;
		final String internal_query= internQuery;
		
		return new AutoSuggestData(autosuggest_type, autosuggest_suggestion, field_value,
				field_name, src_field, pid, unbxdprank, internal_query);
	}
	
	public enum AutoSuggestType{
		PROMOTED_SUGGESTION,IN_FIELD, POPULAR_PRODUCTS, TOP_SEARCH_QUERIES, KEYWORD_SUGGESTION;
	}

	public int getUnbxdprank() {
		return unbxdprank;
	}

	public void setUnbxdprank(int unbxdprank) {
		this.unbxdprank = unbxdprank;
	}

	public String getAutosuggest_type() {
		return autosuggest_type;
	}

	public String getAutosuggest_suggestion() {
		return autosuggest_suggestion;
	}

	public String getField_value() {
		return field_value;
	}

	public String getField_name() {
		return field_name;
	}

	public String getSrc_field() {
		return src_field;
	}

	public String getPid() {
		return pid;
	}

	public String getInternal_query() {
		return internal_query;
	}
}
