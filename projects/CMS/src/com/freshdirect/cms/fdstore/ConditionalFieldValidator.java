package com.freshdirect.cms.fdstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.fdstore.content.ProductFilterModel;
import com.freshdirect.fdstore.content.ProductFilterType;

public class ConditionalFieldValidator implements ContentValidatorI {

	public static class FieldRules {
		private List<String> requiredFields = new ArrayList<String>();
		private List<String> optionalFields = new ArrayList<String>();
		private List<List<String>> requiredFieldGroups = new ArrayList<List<String>>(); //at least one is required from one group 
		private List<String> usedFields = new ArrayList<String>(); //automatically maintained collection of declared fields
		
		public FieldRules(){
		}

		public FieldRules(FieldRules src){
			this.requiredFields = new ArrayList<String>(src.requiredFields);
			this.optionalFields = new ArrayList<String>(src.optionalFields);
			this.requiredFieldGroups = new ArrayList<List<String>>(src.requiredFieldGroups);
			this.usedFields = new ArrayList<String>(src.usedFields);
		}

		public List<String> getRequiredFields() {
			return requiredFields;
		}
		public List<String> getOptionalFields() {
			return optionalFields;
		}
		public List<List<String>> getRequiredFieldGroups() {
			return requiredFieldGroups;
		}
		
		public List<String> getUsedFields(){
			return usedFields;
		}
		
		public FieldRules addRequired(String field){
			requiredFields.add(field);
			usedFields.add(field);
			return this;
		}

		public FieldRules addOptional(String field){
			optionalFields.add(field);
			usedFields.add(field);
			return this;
		}

		public FieldRules addRequiredGroup(String... requiredGroup){
			List<String> requiredGroupList = Arrays.asList(requiredGroup);
			requiredFieldGroups.add(requiredGroupList);
			usedFields.addAll(requiredGroupList);
			return this;
		}
	}
	
	public static FieldRules PRODUCT_FILTER_BINARY 			= new FieldRules().addRequired(ProductFilterModel.NAME).addRequired(ProductFilterModel.TYPE).addOptional(ProductFilterModel.INVERT);
	public static FieldRules PRODUCT_FILTER_COMBINATION 	= new FieldRules(PRODUCT_FILTER_BINARY).addRequired(ProductFilterModel.FILTERS);
	public static FieldRules PRODUCT_FILTER_ERPSY_FLAG 		= new FieldRules(PRODUCT_FILTER_BINARY).addRequired(ProductFilterModel.ERPSY_FLAG_CODE); 
	public static FieldRules PRODUCT_FILTER_BRAND 			= new FieldRules(PRODUCT_FILTER_BINARY).addRequired(ProductFilterModel.BRAND);
	public static FieldRules PRODUCT_FILTER_DOMAIN_VALUE 	= new FieldRules(PRODUCT_FILTER_BINARY).addRequired(ProductFilterModel.DOMAIN_VALUE);
	public static FieldRules PRODUCT_FILTER_TAG 			= new FieldRules(PRODUCT_FILTER_BINARY).addRequired(ProductFilterModel.TAG);
	public static FieldRules PRODUCT_FILTER_RANGE 			= new FieldRules(PRODUCT_FILTER_BINARY).addRequiredGroup(ProductFilterModel.FROM_VALUE, ProductFilterModel.TO_VALUE);
	public static FieldRules PRODUCT_FILTER_RANGE_NUTRITION = new FieldRules(PRODUCT_FILTER_RANGE).addRequired(ProductFilterModel.NUTRITION_CODE);
	
	@Override
	public void validate(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode) {
		ContentType t = node.getKey().getType();

		if (FDContentTypes.PRODUCT_FILTER.equals(t)) {

			String type = (String) node.getAttributeValue("type");
			if (type==null){
				delegate.record( node.getKey(), "Type is empty" );
			} else {
				switch (ProductFilterType.toEnum(type)){
					case AND:
					case OR:	
						validateFields(PRODUCT_FILTER_COMBINATION, delegate, node);
						break;
						
					case BACK_IN_STOCK:
					case NEW:
					case KOSHER:
					case ON_SALE:
						validateFields(PRODUCT_FILTER_BINARY, delegate, node);
						break;
						
					case ALLERGEN:
					case CLAIM:
					case ORGANIC:
						validateFields(PRODUCT_FILTER_ERPSY_FLAG, delegate, node);
						break;
						
					case BRAND:
						validateFields(PRODUCT_FILTER_BRAND, delegate, node);
						break;
						
					case DOMAIN_VALUE:
						validateFields(PRODUCT_FILTER_DOMAIN_VALUE, delegate, node);
						break;
		
					case TAG:
						validateFields(PRODUCT_FILTER_TAG, delegate, node);
						break;
		
					case CUSTOMER_RATING:
					case EXPERT_RATING:
					case FRESHNESS:
					case SUSTAINABILITY_RATING:
					case PRICE:
						validateFields(PRODUCT_FILTER_RANGE, delegate, node);
						break;
		
					case NUTRITION:
						validateFields(PRODUCT_FILTER_RANGE_NUTRITION, delegate, node);
						break;
					
					default:
						delegate.record( node.getKey(), "Type '"+type+"' is invalid" );
						break;
				
				}
			}

		}
	}
	
	private void validateFields (FieldRules fieldRules, ContentValidationDelegate delegate, ContentNodeI node){
		StringBuilder errorText = new StringBuilder(); 
		
		for (String attributeName : node.getAttributes().keySet()){
			Object attributeValue = node.getAttributeValue(attributeName);
			
			checkRequired(fieldRules, attributeName, attributeValue, errorText);
			checkUsed(fieldRules, attributeName, attributeValue, errorText);
		}

		checkRequiredGroups(fieldRules, errorText, node);

		if (errorText.length()!=0){
			delegate.record( node.getKey(), errorText.toString() );
		}

	}
	
	private void checkRequired(FieldRules fieldRules, String attributeName, Object attributeValue, StringBuilder errorText) {
		if (fieldRules.getRequiredFields().contains(attributeName) && !isSet(attributeValue)){
			errorText.append("<br>Please set '" + attributeName + "'" );
		}
	}

	private void checkUsed(FieldRules fieldRules, String attributeName, Object attributeValue, StringBuilder errorText) {
		if (!fieldRules.getUsedFields().contains(attributeName) && isSet(attributeValue)){
			errorText.append("<br>Please clear '" + attributeName + "'\n" );
		}
	}

	private void checkRequiredGroups(FieldRules fieldRules, StringBuilder errorText, ContentNodeI node) {
		for (List<String> requiredGroup : fieldRules.getRequiredFieldGroups()){
			boolean allEmpty = true;
			StringBuilder fieldList = new StringBuilder();
			
			String delimiter = "', '";
			for (String attributeName : requiredGroup){
				fieldList.append(attributeName + delimiter);
				if (isSet(node.getAttributeValue(attributeName))){
					allEmpty = false;
					break;
				} 
			}
			
			if (allEmpty){
				String fieldListStr = fieldList.toString();
				errorText.append("<br>Please set at least one of the following: '" + fieldListStr.substring(0, fieldListStr.length() - delimiter.length()) + "'\n");
			}
		}
	}

	private boolean isSet(Object attr){
		return attr!=null && !"".equals(attr) && !(attr instanceof Collection<?> && ((Collection<?>) attr).isEmpty());
	}

}
