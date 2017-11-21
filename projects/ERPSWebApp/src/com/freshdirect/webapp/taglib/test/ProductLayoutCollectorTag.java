package com.freshdirect.webapp.taglib.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.EnumProductLayout;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

public class ProductLayoutCollectorTag extends SimpleTagSupport {

	private Map<EnumProductLayout, List<ProductModel>> groupedResult = new HashMap<EnumProductLayout, List<ProductModel>>();
	
	public void doTag(){
		PageContext ctx = (PageContext)getJspContext();

		Collection<ContentKey> products = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);
		for (ContentKey product : products){
			ProductModel productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(product);
			
			if(!productModel.isOrphan() && !productModel.isHidden() && !productModel.isInvisible()){

				EnumProductLayout productLayout = productModel.getProductLayout();
				
				List<ProductModel> productModelsForLayout = groupedResult.get(productLayout);

				if (productModelsForLayout == null) {
					productModelsForLayout = new ArrayList<ProductModel>();
					groupedResult.put(productLayout, productModelsForLayout);
				}
				
				productModelsForLayout.add(productModel);
			}
		}
		
		
		//sort by catId and prodId
		for(List<ProductModel> productModelsForLayout : groupedResult.values()){
			Collections.sort(productModelsForLayout, new Comparator<ProductModel>(){

				@Override
				public int compare(ProductModel p1, ProductModel p2) {

					String p1Par = p1.getParentNode().toString();
					String p2Par = p2.getParentNode().toString();
					
					int ret = p1Par.compareTo(p2Par);

					if (ret == 0) {
						ret = p1.toString().compareTo(p2.toString());
					}
					
					return ret;
				}
			});
		}
		
		
//		HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
//		if (request.getParameter("toCsv") != null) {
//
//			try {
//				JspWriter out = ctx.getOut();
//				out.clear();
//
//				File file = writeToCsv();
//
//				String filename = "layouts.csv";
//
//				HttpServletResponse response = (HttpServletResponse) ctx.getResponse();
//				response.setContentType("application/octet-stream");
//				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + '"');
//
//				FileInputStream fileInputStream;
//				fileInputStream = new FileInputStream(file);
//
//				int i;
//				while ((i = fileInputStream.read()) != -1) {
//					out.write(i);
//				}
//
//				fileInputStream.close();
//				out.close();
//
//				return; //SKIP_BODY;
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

		ctx.setAttribute("groupedResult", groupedResult);

		try {
			getJspBody().invoke(null);
		} catch (JspException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;// EVAL_PAGE;
	}

}
