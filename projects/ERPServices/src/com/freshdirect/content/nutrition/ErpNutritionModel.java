package com.freshdirect.content.nutrition;

import java.util.*;

import java.util.List;
import com.freshdirect.framework.core.*;

public class ErpNutritionModel extends ModelSupport {
	/** String skuCode to wich this nutrition model belongs to */
	private String skuCode;
	/** HashMap of nutrition values keyed by nutriotn types from ErpNutritionType */
	private HashMap values;
	/** HashMap of nutrition unit of measures keyed by nutrition types from ErpNutritionType */
	private HashMap uom;
	/** String corresponding upc for this skuCode */
	private String upc;
	/** HashMap<String,String> of assorted product information */
	private HashMap info;
	
	private Date lastModifiedDate;

	/**
	 * Constructor to create a new object.
	 */
	public ErpNutritionModel() {
		values = new HashMap();
		uom = new HashMap();
		info = new HashMap();
	}

	/**
	 * to get skuCode for this nutrition model
	 *
	 * @retrun String skuCode
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
		this.skuCode = skuCode;
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
			List lst = ErpNutritionType.getStarterSetList();
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
		String units = (String) uom.get(key);
		if (units == null) {
			ErpNutritionType.Type type = ErpNutritionType.getType(key);
			units = type.getUom();
		}
		return units;
	}

	/**
	 * set UOM for this nutrition model
	 *
	 * @param String nutrition type, String uom
	 */
	public void setUomFor(String key, String value) {
		uom.put(key, value);
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
		NutritionInfoAttribute attr = (NutritionInfoAttribute) info.get(ErpNutritionInfoType.INGREDIENTS);
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
		NutritionInfoAttribute attr = (NutritionInfoAttribute) info.get(ErpNutritionInfoType.HIDDEN_INGREDIENTS);
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
		NutritionInfoAttribute attr = (NutritionInfoAttribute) info.get(ErpNutritionInfoType.HEATING);
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
		NutritionInfoAttribute attr = (NutritionInfoAttribute) info.get(ErpNutritionInfoType.KOSHER_SYMBOL);
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
		NutritionInfoAttribute attr = (NutritionInfoAttribute) info.get(ErpNutritionInfoType.KOSHER_TYPE);
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
		} else {
			NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
			return multi.getValues();
		}
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
		} else {
			NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
			for (Iterator cIter = multi.getValues().iterator(); cIter.hasNext();) {
				EnumClaimValue c = (EnumClaimValue) cIter.next();
				if (c.equals(claim)) {
					return true;
				}
			}
			return false;
		}
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
		Object value = info.get(ErpNutritionInfoType.ALLERGEN);
		if (value == null) {
			return Collections.EMPTY_LIST;
		} else {
			NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
			return multi.getValues();
		}
	}

	public void setAllergens(List allergens) {
		NutritionInfoMultiAttribute multi = new NutritionInfoMultiAttribute(ErpNutritionInfoType.ALLERGEN);
		for (Iterator aIter = allergens.iterator(); aIter.hasNext();) {
			multi.addValue(new NutritionInfoAttribute(multi.getNutritionInfoType(), multi.numberOfValues(), aIter.next()));
		}
		info.put(ErpNutritionInfoType.ALLERGEN, multi);
	}

	public boolean hasAllergen(EnumAllergenValue allergen) {
		Object value = info.get(ErpNutritionInfoType.ALLERGEN);
		if (value == null) {
			return false;
		} else {
			NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
			for (Iterator aIter = multi.getValues().iterator(); aIter.hasNext();) {
				EnumAllergenValue a = (EnumAllergenValue) aIter.next();
				if (a.equals(allergen)) {
					return true;
				}
			}
			return false;
		}
	}

	public List getOrganicStatements() {
		Object value = info.get(ErpNutritionInfoType.ORGANIC);
		if (value == null) {
			return Collections.EMPTY_LIST;
		} else {
			NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
			return multi.getValues();
		}
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
		} else {
			NutritionInfoMultiAttribute multi = (NutritionInfoMultiAttribute) value;
			for (Iterator oIter = multi.getValues().iterator(); oIter.hasNext();) {
				EnumOrganicValue o = (EnumOrganicValue) oIter.next();
				if (o.equals(organic)) {
					return true;
				}
			}
			return false;
		}
	}

	public Map getNutritionInfo() {
		return info;
	}

	/**
	 * returns iterator over keys in value HashMap to check what nutrition types this model has
	 *
	 * @return Iterator over keys of values HashMap
	 */
	public Iterator getKeyIterator() {
		List ret = new ArrayList();
		for (Iterator i = ErpNutritionType.getTypesIterator(); i.hasNext();) {
			String key = (String) i.next();
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

	/*
	public void addInfo(ErpNutritionInfoType type, String info) {
	    this.info.put(type, info);
	}
	*/

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
			NutritionInfoAttribute a = (NutritionInfoAttribute) info.get(keyIter.next());
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
		String retval = (String) this.info.get(type);
		if (retval != null) {
			return retval;
		}
		return "";
	}

	public Iterator getInfoKeyIterator() {
		return this.info.keySet().iterator();
	}
	
	public boolean hasIngredients(){
		NutritionInfoAttribute infoAtt = (NutritionInfoAttribute) this.info.get(ErpNutritionInfoType.INGREDIENTS);
		if (infoAtt != null) {
			String ingredients = (String) infoAtt.getValue();
			if ((ingredients != null) && (ingredients.trim().length() > 0)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public Set getNutritionInfoNames() {
		return this.info.keySet();
	}
	
	public String getNutritionInfoString(ErpNutritionInfoType type) {
		if (type.isMultiValued()) {
			throw new IllegalArgumentException(type + " is multivalued, use getNutritionInfoList instead");
		}
		NutritionInfoAttribute ni = (NutritionInfoAttribute) this.info.get(type);
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


	/*
	public String debug() {
	    StringBuffer buff = new StringBuffer();
	    for (Iterator i = getInfoKeyIterator(); i.hasNext(); ) {
	        ErpNutritionInfoType et = (ErpNutritionInfoType) i.next();
	        buff.append(skuCode).append(" ").append(et.getCode()).append(" ").append(getInfo(et)).append("\n");
	    }
	    return buff.toString();
	}
	*/
}
