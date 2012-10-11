package com.freshdirect.content.nutrition;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.framework.util.log.LoggerFactory;

public class NutritionDrugPanel implements Serializable {

	private static final long serialVersionUID = 3485740087893523863L;
	
	private static final Logger LOGGER = LoggerFactory.getInstance( NutritionDrugPanel.class ); 
	
	private String id;
	private String skuCode;
	private Date lastModifiedDate;
	private List<NutritionDrugSection> sections = new ArrayList<NutritionDrugSection>();
	
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
	
	public List<NutritionDrugSection> getSections() {
		return sections;
	}
	
	public void setSections(List<NutritionDrugSection> sections) {
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
