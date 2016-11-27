package com.freshdirect.content.nutrition.panel;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class NutritionPanel implements Serializable {

	private static final long serialVersionUID = 3485740087893523863L;
	
	private static final Logger LOGGER = LoggerFactory.getInstance( NutritionPanel.class ); 
	
	private String id;
	private String skuCode;
	private Date lastModifiedDate;
	private List<NutritionSection> sections = new ArrayList<NutritionSection>();
	private NutritionPanelType type = NutritionPanelType.DRUG; // FIXME: default value?
	
	/**
	 * Special 'Copy Constructor' (static factory method)
	 * Does a deep copy (will duplicate sections and items)
	 * Does NOT keep the ID-s, new panel will have null ID-s 
	 * (as newly created panels do)
	 * Also updates last modified date to current time.
	 * 
	 * @param panel original nutrition panel
	 */
	public static NutritionPanel deepCopy( NutritionPanel oldP ) {
		if ( oldP == null )
			return null;
		
		NutritionPanel newP = new NutritionPanel();
		newP.id = null;
		newP.skuCode = oldP.skuCode;
		newP.lastModifiedDate = new Date();
		newP.type = oldP.type;
		
		for ( NutritionSection s : oldP.sections ) {
			newP.sections.add( NutritionSection.deepCopy(s) );
		}		
		return newP;
	}
	
	public NutritionPanelType getType() {
		return type;
	}
	
	public void setType( NutritionPanelType type ) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSkuCode() {
		return skuCode;
	}
	
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	public List<NutritionSection> getSections() {
		return sections;
	}
	
	public void setSections(List<NutritionSection> sections) {
		this.sections = sections;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public String toJSON() {
		ObjectMapper mapper = new ObjectMapper();
		Writer writer = new StringWriter();
		try {
			mapper.writeValue(writer, this);
			return writer.toString();
		} catch (JsonGenerationException e) {
			LOGGER.error("Cannot convert panel to JSON", e);
		} catch (JsonMappingException e) {
			LOGGER.error("Cannot convert panel to JSON", e);
		} catch (IOException e) {
			LOGGER.error("Cannot convert panel to JSON", e);
		}
		return null;
	}
	

}
