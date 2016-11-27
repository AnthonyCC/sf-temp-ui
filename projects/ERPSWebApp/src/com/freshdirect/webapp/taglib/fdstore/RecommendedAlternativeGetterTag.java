package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.webapp.taglib.AbstractGetterTag;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.SkuModel;

import com.freshdirect.fdstore.content.RecommendedAlternative;

import java.util.Iterator;
import java.util.Map;

public class RecommendedAlternativeGetterTag extends AbstractGetterTag
		implements SessionName {

	private ContentNodeModel contModel;
	
	public void setContentNode(ContentNodeModel contModel) {
		this.contModel = contModel;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6347113300571947949L;

	protected Object getResult() throws Exception {
		
		ProductModel altModel = null;
		
		RecommendedAlternative alternative = null;
		if (contModel instanceof ProductModel) altModel = (ProductModel)contModel;
		else if (contModel instanceof SkuModel) altModel = ((SkuModel)contModel).getProductModel();
		else return alternative; 
		
		if (!(altModel instanceof ConfiguredProduct)) { // Product, but not configured
			alternative = new RecommendedAlternative(altModel);
		
			alternative.addQueryParameter("productId",altModel)
		    		   .addQueryParameter("skuCode", altModel.getPreferredSku())
		    		   .addQueryParameter("catId", altModel.getParentNode());
			
		} else { // Configured product
		    
			ConfiguredProduct altProduct = (ConfiguredProduct)altModel;
		    
		    if (altProduct instanceof ConfiguredProductGroup) {
		    	altProduct = (ConfiguredProduct)altProduct.getProduct();	        
		    	if (altProduct == null) return alternative;
		    }

		    alternative = new RecommendedAlternative(altProduct);
		    
		    alternative.addQueryParameter("skuCode", altProduct.getSkuCode())
		    		   .addQueryParameter("catId", altProduct.getProduct().getParentNode())
		    		   .addQueryParameter("productId", altProduct.getProduct());

		    // Configuration options
		    for(Iterator mi = altProduct.getConfiguration().getOptions().entrySet().iterator(); mi.hasNext(); ) {
		        Map.Entry entry = (Map.Entry)mi.next();
		        alternative.addQueryParameter((String)entry.getKey(), entry.getValue());
		    }

		    // sales unit and quantity
		    alternative.addQueryParameter("salesUnit", altProduct.getSalesUnit())
		    		   .addQueryParameter("quantity", new Double(altProduct.getQuantity()));
		}
		

        
        alternative.addQueryParameter("trk", "alt");
		
        return alternative;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.fdstore.content.RecommendedAlternative";
		}
	}

}
