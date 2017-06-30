package com.freshdirect.content.nutrition;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpNutritionModel extends ModelSupport {
	
	private static final long serialVersionUID = 165757752576184510L;
	
	private static final Logger LOGGER = LoggerFactory.getInstance( ErpNutritionModel.class ); 


	/** String skuCode to which this nutrition model belongs to */
	private String skuCode;
	/** HashMap of nutrition values keyed by nutrition types from ErpNutritionType */
	private HashMap<String,Object> values;
	/** HashMap of nutrition unit of measures keyed by nutrition types from ErpNutritionType */
	private HashMap<String,String> uom;
	/** String corresponding upc for this skuCode */
	private String upc;
	/** HashMap of assorted product information */
	private HashMap<ErpNutritionInfoType,NutritionInfoAttribute> info;
	
	private Date lastModifiedDate;

	/**
	 * Constructor to create a new object.
	 */
	public ErpNutritionModel() {
		values = new HashMap<String,Object>();
		uom = new HashMap<String,String>();
		info = new HashMap<ErpNutritionInfoType,NutritionInfoAttribute>();
	}

	/**
	 * to get skuCode for this nutrition model
	 *
	 * @return String skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * set sku code for this nutrition model
	 *
	 * @param String skuCode
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode != null ? skuCode.intern() : null;
	}

	/**
	 * get double value for given nutrition type
	 *
	 * @param nutrion type
	 * @return double value if value is not in the HashMap returns -1
	 */
	public double getValueFor(String key) {
		if (values.size() > 0) {
			Double ret = (Double) values.get(key);
			if (ret != null) {
				return ret.doubleValue();
			} else {
				return -999;
			}
		} else {
			List<String> lst = ErpNutritionType.getStarterSetList();
			if (lst.contains(key)) {
				return 0;
			} else {
				return -999;
			}
		}
	}
	
	public void setLastModifiedDate(Date lastModifiedDate){
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public Date getLastModifiedDate(){
		return this.lastModifiedDate;
	}

	/**
	 * set double value for given nutrion type
	 *
	 * @param String nutrion type, double value
	 */
	public void setValueFor(String key, double value) {
		values.put(key, new Double(value));
	}

	/**
	 * get UOM for this nutrion model
	 *
	 * @param nutrition type
	 * @return String uom
	 */
	public String getUomFor(String key) {
		String units = uom.get(key);
		if (units == null) {
			ErpNutritionType.Type type = ErpNutritionType.getType(key);
			units = type.getUom();
		}
		return units;
	}

	// This method is used only for StoreFront 2.0 JSON parser
	public HashMap<String, Object> getValues() {
		return values;
	}
	// This method is used only for StoreFront 2.0 JSON parser
	public HashMap<String, String> getUom() {
		return uom;
	}

	/**
	 * set UOM for this nutrition model
	 *
	 * @param String nutrition type, String uom
	 */
	public void setUomFor(String key, String value) {
		uom.put(key.intern(), value != null ? value.intern() : null);
	}

	/**
	 * get UPC for this nutrition model
	 *
	 * @return String upc
	 */
	public String getUpc() {
		return upc;
	}

	/**
	 * set UPC for this nutrition model
	 *
	 * @param String upc
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}

	/**
	 * get ingredients for this nutrition model
	 *
	 * @return String upc
	 */
	public String getIngredients() {
		NutritionInfoAttribute attr = info.get(ErpNutritionInfoType.INGREDIENTS);
		if (attr != null) {
			String ingredients = (String) attr.getValue();
			if(ingredients != null){
				return ingredients;
			}
		}
		return "";
	}

	/**
	 * set ingredients for this nutrition model
	 *
	 * @param String upc
	 */
	public void setHiddenIngredients(String ing) {
		this.info.put(
			ErpNutritionInfoType.HIDDEN_INGREDIENTS,
			new NutritionInfoAttribute(ErpNutritionInfoType.HIDDEN_INGREDIENTS, ing));
	}

	/**
	 * get ingredients for this nutrition model
	 *
	 * @return String upc
	 */
	public String getHiddenIngredients() {
		NutritionInfoAttribute attr = info.get(ErpNutritionInfoType.HIDDEN_INGREDIENTS);
		if (attr != null) {
			return (String) attr.getValue();
		}
		return "";
	}

	/**
	 * set ingredients for this nutrition model
	 *
	 * @param String upc
	 */
	public void setIngredients(String ing) {
		this.info.put(ErpNutritionInfoType.INGREDIENTS, new NutritionInfoAttribute(ErpNutritionInfoType.INGREDIENTS, ing));
	}

	/**
	 * get heating instructions for this nutrition model
	 *
	 * @return String upc
	 */
	public String getHeatingInstructions() {
		NutritionInfoAttribute attr = info.get(ErpNutritionInfoType.HEATING);
		if (attr != null) {
			return (String) attr.getValue();
		}
		return "";
	}

	/**
	 * set heating instructions for this nutrition model
	 *
	 * @param String upc
	 */
	public void setHeatingInstructions(String heat) {
		this.info.put(ErpNutritionInfoType.HEATING, new NutritionInfoAttribute(ErpNutritionInfoType.HEATING, heat));
	}

	/**
	 * get kosher symbol for this nutrition model
	 *
	 * @return String kosher symbol
	 */
	public EnumKosherSymbolValue getKosherSymbol() {
		NutritionInfoAttribute attr = info.get(ErpNutritionInfoType.KOSHER_SYMBOL);
		if (attr != null) {
			return (EnumKosherSymbolValue) attr.getValue();
		}
		return null;
	}

	/**
	 * set kosher symbol for this nutrition model
	 *
	 * @param String kosher symbol
	 */
	public void setKosherSymbol(EnumKosherSymbolValue ksym) {
		if (ksym == null) {
			this.info.remove(ErpNutritionInfoType.KOSHER_SYMBOL);
		} else {
			this.info.put(ErpNutritionInfoType.KOSHER_SYMBOL, new NutritionInfoAttribute(ErpNutritionInfoType.KOSHER_SYMBOL, ksym));
		}
	}

	/**
	 * get kosher type for this nutrition model
	 *
	 * @return String kosher type
	 */
	public EnumKosherTypeValue getKosherType() {
		NutritionInfoAttribute attr = info.get(ErpNutritionInfoType.KOSHER_TYPE);
		if (attr != null) {
			return (EnumKosherTypeValue) attr.getValue();
		}
		return null;
	}

	/**
	 * set kosher type for this nutrition model
	 *
	 * @param String kosher type
	 */
	public void setKosherType(EnumKosherTypeValue ktyp) {
		if (ktyp == null) {
			this.info.remove(ErpNutritionInfoType.KOSHER_TYPE);
		} else {
			this.info.put(ErpNutritionInfoType.KOSHER_TYPE, new NutritionInfoAttribute(ErpNutritionInfoType.KOSHER_TYPE, ktyp));
		}
	}

	public List getClaims() {
		Object value = info.get(ErpNutritionInfoType.CLAIM);
		if (value == null) {
			return Collections.EMPTY_LIST;
		}
		NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
		return multi.getValues();
	}

	public void setClaims(List claims) {
		NutritionInfoMultiAttribute multi = new NutritionInfoMultiAttribute(ErpNutritionInfoType.CLAIM);
		for (Iterator cIter = claims.iterator(); cIter.hasNext();) {
			multi.addValue(new NutritionInfoAttribute(multi.getNutritionInfoType(), multi.numberOfValues(), cIter.next()));
		}
		info.put(ErpNutritionInfoType.CLAIM, multi);
	}

	public boolean makesClaim(EnumClaimValue claim) {
		Object value = info.get(ErpNutritionInfoType.CLAIM);
		if (value == null) {
			return false;
		}
		NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
		for (Iterator cIter = multi.getValues().iterator(); cIter.hasNext();) {
			EnumClaimValue c = (EnumClaimValue) cIter.next();
			if (c.equals(claim)) {
				return true;
			}
		}
		return false;
	}
	
	public double getNetCarbs() {
		double netCarbs = 0;
		if (this.containsNutritionInfoFor(ErpNutritionType.NET_CARBOHYDRATES))
			netCarbs = this.getValueFor(ErpNutritionType.NET_CARBOHYDRATES);
		else {
			double sugarAlchQuant = this.getValueFor(ErpNutritionType.TOTAL_SUGAR_ALCOHOL_QUANTITY);
			if (sugarAlchQuant == -999.0)
				sugarAlchQuant = 0;
			netCarbs =
				this.getValueFor(ErpNutritionType.TOTAL_CARBOHYDRATE_QUANTITY)
					- this.getValueFor(ErpNutritionType.TOTAL_DIETARY_FIBER_QUANTITY)
					- sugarAlchQuant;
		}

		return netCarbs;
	}

	public List getAllergens() {
		NutritionInfoAttribute value = info.get(ErpNutritionInfoType.ALLERGEN);
		if (value == null) {
			return Collections.EMPTY_LIST;
		}
		NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
		return multi.getValues();
	}

	public void setAllergens(List allergens) {
		NutritionInfoMultiAttribute multi = new NutritionInfoMultiAttribute(ErpNutritionInfoType.ALLERGEN);
		for (Iterator aIter = allergens.iterator(); aIter.hasNext();) {
			multi.addValue(new NutritionInfoAttribute(multi.getNutritionInfoType(), multi.numberOfValues(), aIter.next()));
		}
		info.put(ErpNutritionInfoType.ALLERGEN, multi);
	}

	public boolean hasAllergen(EnumAllergenValue allergen) {
		NutritionInfoAttribute value = info.get(ErpNutritionInfoType.ALLERGEN);
		if (value instanceof NutritionInfoMultiAttribute) {
			NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
			for (Iterator aIter = multi.getValues().iterator(); aIter.hasNext();) {
				EnumAllergenValue a = (EnumAllergenValue) aIter.next();
				if (a.equals(allergen)) {
					return true;
				}
			}
		}
		return false;
	}

	public List getOrganicStatements() {
		Object value = info.get(ErpNutritionInfoType.ORGANIC);
		if (value == null) {
			return Collections.EMPTY_LIST;
		}
		NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
		return multi.getValues();
	}

	public void setOrganicStatements(List organics) {
		NutritionInfoMultiAttribute multi = new NutritionInfoMultiAttribute(ErpNutritionInfoType.ORGANIC);
		for (Iterator oIter = organics.iterator(); oIter.hasNext();) {
			multi.addValue(new NutritionInfoAttribute(multi.getNutritionInfoType(), multi.numberOfValues(), oIter.next()));
		}
		info.put(ErpNutritionInfoType.ORGANIC, multi);
	}

	public boolean hasOrganicStatement(EnumOrganicValue organic) {
		Object value = info.get(ErpNutritionInfoType.ORGANIC);
		if (value == null) {
			return false;
		}
		NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
		for (Iterator oIter = multi.getValues().iterator(); oIter.hasNext();) {
			EnumOrganicValue o = (EnumOrganicValue) oIter.next();
			if (o.equals(organic)) {
				return true;
			}
		}
		return false;
	}

	public Map<ErpNutritionInfoType,NutritionInfoAttribute> getNutritionInfo() {
		return info;
	}

	/**
	 * returns iterator over keys in value HashMap to check what nutrition types this model has
	 *
	 * @return Iterator over keys of values HashMap
	 */
	public Iterator<String> getKeyIterator() {
		List<String> ret = new ArrayList<String>();
		for (Iterator<String> i = ErpNutritionType.getTypesIterator(); i.hasNext();) {
			String key = i.next();
			double value = getValueFor(key);
			if (value > -999) {
				ret.add(key);
			}
		}
		return ret.iterator();
	}

	/**
	 * remove/delete nutrition info about the given nutrition type from this model
	 *
	 * @param String nutrion type to remove
	 */
	public void removeNutritionInfoFor(String key) {
		if (values.containsKey(key)) {
			values.remove(key);
		}
		if (uom.containsKey(key)) {
			uom.remove(key);
		}
	}

	/**
	 * check if this model contains nutrition info about given nutrition type
	 *
	 * @param String nutrition type
	 * @return boolean true if values HashMap contians given nutrition type else returns false
	 */
	public boolean containsNutritionInfoFor(String key) {
		return values.containsKey(key);
	}

	public boolean hasInfo() {
		return (values.size() > 0);
	}

	public void addNutritionAttribute(NutritionInfoAttribute attribute) {
		if (!attribute.getNutritionInfoType().isMultiValued()) {
			//
			// single valued attributes
			//
			info.put(attribute.getNutritionInfoType(), attribute);
		} else {
			//
			// multi valued attributes
			//
			NutritionInfoMultiAttribute attrs = (NutritionInfoMultiAttribute) info.get(attribute.getNutritionInfoType());
			if (attrs == null) {
				attrs = new NutritionInfoMultiAttribute(attribute.getNutritionInfoType());
				info.put(attrs.getNutritionInfoType(), attrs);
			}
			attrs.addValue(attribute);
		}
	}

	public List getNutritionAttributes() {
		List attrs = new ArrayList();

		for (Iterator keyIter = info.keySet().iterator(); keyIter.hasNext();) {
			NutritionInfoAttribute a = info.get(keyIter.next());
			if (a instanceof NutritionInfoMultiAttribute) {
				List multi = (List) a.getValue();
				for (Iterator mIter = multi.iterator(); mIter.hasNext();) {
					attrs.add(mIter.next());
				}
			} else {
				attrs.add(a);
			}
		}

		return attrs;
	}

	public String debug() {
		StringBuffer buff = new StringBuffer();
		for (Iterator aIter = getNutritionAttributes().iterator(); aIter.hasNext();) {
			NutritionInfoAttribute attr = (NutritionInfoAttribute) aIter.next();
			buff.append(attr.getNutritionInfoType().getCode()).append(" : ");
			buff.append(attr.getPriority()).append(" : ");
			if (attr.getValue() instanceof NutritionValueEnum) {
				buff.append(((NutritionValueEnum) attr.getValue()).getCode());
			} else {
				buff.append(attr.getValue());
			}
			buff.append("\n");
		}
		return buff.toString();
	}

	public String getInfo(ErpNutritionInfoType type) {
		NutritionInfoAttribute retval = this.info.get(type);
		if (retval != null) {
			return retval.toString();
		}
		return "";
	}

	public Iterator<ErpNutritionInfoType> getInfoKeyIterator() {
		return this.info.keySet().iterator();
	}
	
	public boolean hasIngredients(){
		NutritionInfoAttribute infoAtt = this.info.get(ErpNutritionInfoType.INGREDIENTS);
		if (infoAtt != null) {
			String ingredients = (String) infoAtt.getValue();
			if ((ingredients != null) && (ingredients.trim().length() > 0)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<ErpNutritionInfoType> getNutritionInfoNames() {
		return this.info.keySet();
	}
	
	public String getNutritionInfoString(ErpNutritionInfoType type) {
		if (type.isMultiValued()) {
			throw new IllegalArgumentException(type + " is multivalued, use getNutritionInfoList instead");
		}
		NutritionInfoAttribute ni = this.info.get(type);
		return ni == null ? null : (String) ni.getValue();
	}
	
	public List getNutritionInfoList(ErpNutritionInfoType type) {
		if (!type.isMultiValued()) {
			throw new IllegalArgumentException(type + " is not multivalued, use getNutritionInfo instead");
		}
		NutritionInfoMultiAttribute ni = (NutritionInfoMultiAttribute) this.info.get(type);
		return ni == null ? null : ni.getValues();
	}
	
	public boolean hasNutritionInfo(ErpNutritionInfoType type) {
		return this.info.get(type) != null;
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
