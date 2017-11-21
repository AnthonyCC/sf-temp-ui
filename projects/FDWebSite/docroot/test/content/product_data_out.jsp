<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.storeapi.content.*'%>
<%@ page import='com.freshdirect.cms.core.domain.ContentKey'%>
<%@ page import='com.freshdirect.cms.core.domain.ContentKeyFactory'%>
<%@ page import='com.freshdirect.cms.core.domain.ContentType'%>
<%@ page import='com.freshdirect.storeapi.util.ProductInfoUtil'%>
<%
	//check for a passed pId
		String pId = NVL.apply(request.getParameter("pId"), "");
		String pData = NVL.apply(request.getParameter("pData"), "");
%>
<%
	if (!"".equals(pId)) {
		//make sure the pId leads us to a product (this can be updated later)
		ProductModel pRef = null;
		SkuModel sRef = null;
		
		pRef = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get("Product:"+pId));
		String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
		//if not a product, check for a SKU instead
		if (pRef == null) {
			//get sku model
			sRef = (SkuModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get("Sku:"+pId));
		}

		if (pRef instanceof ProductModel || sRef instanceof SkuModel) {
			//success

			//get rating
			if ( "rating".equalsIgnoreCase(pData) ) {
				//check if product or sku
				if (pRef instanceof ProductModel) {
					//product
					out.print(pRef.getProductRating());
				}else if (sRef instanceof SkuModel) {
					//sku
					out.print(sRef.getProductInfo().getRating(ProductInfoUtil.getPickingPlantId(sRef.getProductInfo())));
				}
			}
		}
	}
%>