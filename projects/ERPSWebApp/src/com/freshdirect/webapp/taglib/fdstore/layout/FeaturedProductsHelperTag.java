package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;

/**
 * Moved the java parts from i_featured_products.jspf to this tag class with some additional code cleanup.
 * It is still the same mess, only in a java file instead of a jspf page.
 * 
 * TODO replace the whole thing. 
 * 
 * INPUTS :
 * 		DepartmentModel currentFolder
 * 		int 			maxFavsToShow
 * 
 * OUTPUTS
 * 		int 			favoritesToShow
 * 		List<String> 	favHTMLPieces
 * 		List<String> 	favHTMLPieceLinks
 * 
 * @author treer
 */
public class FeaturedProductsHelperTag extends BodyTagSupport {

	private static final long	serialVersionUID	= 8744336738595723913L;

	// === JSP VARIABLES created ===
	public static final String favoritesToShowVariableName 		= "favoritesToShow";
	public static final String favHTMLPiecesVariableName		= "favHTMLPieces";
	public static final String favHTMLPieceLinksVariableName	= "favHTMLPieceLinks";
	
	private DepartmentModel currentFolder;
	private int maxFavsToShow;
	
	
	public DepartmentModel getCurrentFolder() {
		return currentFolder;
	}	
	public void setCurrentFolder( DepartmentModel currentFolder ) {
		this.currentFolder = currentFolder;
	}
	public int getMaxFavsToShow() {
		return maxFavsToShow;
	}	
	public void setMaxFavsToShow( int maxFavsToShow ) {
		this.maxFavsToShow = maxFavsToShow;
	}


	@Override
	public int doStartTag() throws JspException {
		setVariables();
		return EVAL_BODY_INCLUDE;
	}
	
	
	private void setVariables() {
		
	    FDSessionUser yser = (FDSessionUser)pageContext.getSession().getAttribute(SessionName.USER); 

		List<ProductModel> favorites = currentFolder.getFeaturedProducts();
		favorites = ProductPricingFactory.getInstance().getPricingAdapter(favorites, yser.getPricingContext());
	    List<String> favHTMLPieces = new ArrayList<String>();
	    List<String> favHTMLPieceLinks = new ArrayList<String>();
	    
	    Comparator<SkuModel> priceComp = new ProductModel.PriceComparator();
	    int favoritesToShow		= 0;
	    
		ProductModel product;

		for (Iterator<? extends ProductModel> it = favorites.iterator(); it.hasNext(); ) {
			product = it.next();

			ContentNodeModel prodParent = product.getParentNode(); 
	        List<SkuModel> skus = product.getSkus();

	        if (product.isDiscontinued() || product.isUnavailable() || prodParent==null || !(prodParent instanceof CategoryModel) || skus.size()==0)
	        	continue;

	        SkuModel sku = null;
	        String prodPrice = null;
	        String prodBasePrice = null;
	        boolean hasWas = false;
	        boolean isDeal = false;
	        int deal = 0;

	        if (skus.size() == 0) {
	        	continue;  // skip this item..it has no skus.  Hmmm?
	        } else if (skus.size() == 1) {
	            sku = skus.get(0);  // we only need one sku
	        } else {
	            sku = Collections.min(skus, priceComp);
	        }

	        String skuCode = sku.getSkuCode();
	        FDProductInfo productInfo = null;
	        
       		if (skuCode != null) {
				try {
					productInfo = FDCachedFactory.getProductInfo( skuCode );
				} catch (FDSkuNotFoundException ex) {
					// safe to ignore
				} catch ( FDResourceException e ) {
				}
       		}

	        prodPrice = JspMethods.formatDefaultPrice(productInfo, yser.getPricingContext());
	        hasWas = productInfo.getZonePriceInfo(yser.getPricingContext().getZoneId()).isItemOnSale();
	        if( hasWas ) {
	            prodBasePrice=JspMethods.formatSellingPrice(productInfo, yser.getPricingContext());
	        }  
	        deal = productInfo.getZonePriceInfo(yser.getPricingContext().getZoneId()).getHighestDealPercentage();
	        isDeal = deal>0;        
	        
	        if(!isDeal) {
	            continue;
	        }

			String dealsCat="";
			List<CategoryModel> _featuredCats= currentFolder.getFeaturedCategories();

			if (_featuredCats != null && _featuredCats.size() > 0) {
				CategoryModel catRef= _featuredCats.get(0);
				dealsCat= catRef.getContentKey().getId();
			}
			
			String productPageLink_ = ((HttpServletResponse)pageContext.getResponse()).encodeURL("/category.jsp?catId=" + dealsCat
	           +"&sortBy=name&showThumbnails=true&DisplayPerPage=30&sortDescending=false&disp=null&prodCatId="+prodParent
	           +"&productId="+product.getContentName()+"&trk=feat");

	        String dealImage = new StringBuilder("/media_stat/images/deals/brst_sm_").append(deal).append(".gif").toString();
	        Image categoryImage = product.getCategoryImage();;
	        
	        StringBuilder favoriteProducts = new StringBuilder();
	        StringBuilder favoriteProductLinks = new StringBuilder();
	        // append product image
	        favoriteProducts.append("<DIV id=prod_container style=\"WIDTH: 100px; HEIGHT: 100px; TEXT-ALIGN: left\">");
	        if (categoryImage !=null) {
	            favoriteProducts.append("<DIV id=prod_image style=\"PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 0px; LINE-HEIGHT: 0px; PADDING-TOP: 10px; POSITION: absolute; HEIGHT: 0px\">")
	                        .append("<A id=prod_link_img style=\"DISPLAY: block; CURSOR: hand\" HREF=\"" + productPageLink_ + "\" name=prod_link_img>")    
				.append("<IMG style=\"BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px\" ")
				.append(JspMethods.getImageDimensions(categoryImage))
				.append(" alt=\"").append(product.getFullName()).append("\" src=\"").append(categoryImage.getPath()).append("\" ></a></DIV>");
	            favoriteProducts.append("<DIV id=sale_star style=\"POSITION: absolute\"><A id=prod_link_img style=\"DISPLAY: block; CURSOR: hand\" HREF=\"" + productPageLink_ + "\" name=prod_link_img_burst><IMG style=\"BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px\"  alt=\"SAVE ").append(deal).append("%\" src=\"").append(dealImage).append("\"></A></DIV></DIV>");                        
	        }
	        

	        // append product label
	        favoriteProductLinks.append("<DIV id=prod_container style=\"WIDTH: 100px; TEXT-ALIGN: left\"><A id=prod_link_img style=\"DISPLAY: block; CURSOR: hand\" HREF=\"" + productPageLink_ + "\">");
	        favoriteProductLinks.append("<DIV id=prod_container_text style=\"FONT-WEIGHT: normal; FONT-SIZE: 8pt; WIDTH: 100px; TEXT-ALIGN: center\">");
	        favoriteProductLinks.append("<DIV id=prod_text_container><A id=prod_link style=\"COLOR: #360\" href=\"")
	                            .append(productPageLink_).append("\">");

	        String thisProdBrandLabel = product.getPrimaryBrandName();
	        if (thisProdBrandLabel.length()>0) {
	            favoriteProductLinks.append("<B>" + thisProdBrandLabel + "</B><BR>");
	        }
	        favoriteProductLinks.append(product.getFullName().substring(thisProdBrandLabel.length()).trim()); 
	        favoriteProductLinks.append("</A></DIV>");

	        // append product price
	        favoriteProductLinks.append("<DIV id=prod_price_d_container style=\"FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #c00\">").append(prodPrice).append("</DIV>");
	        if (hasWas) {
	        	favoriteProductLinks.append("<DIV id=prod_price_b_container style=\"FONT-SIZE: 7pt; COLOR: #888\">(was ").append(prodBasePrice).append(")</DIV></DIV>");
	        }

	        favHTMLPieces.add(favoriteProducts.toString());
	        favHTMLPieceLinks.add(favoriteProductLinks.toString());
	        
	        favoritesToShow++;
	        if (favoritesToShow == maxFavsToShow) {
	        	break;
	        }
		}
				
		// setting the variables for the jsp page	
		pageContext.setAttribute( favoritesToShowVariableName, favoritesToShow );
		pageContext.setAttribute( favHTMLPiecesVariableName, favHTMLPieces );
		pageContext.setAttribute( favHTMLPieceLinksVariableName, favHTMLPieceLinks );
	}	
	
	
	// ========= TAG Extra Info class =========
	
	public static class TagEI extends TagExtraInfo {
		
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		favoritesToShowVariableName,
	            		"java.lang.Integer",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		favHTMLPiecesVariableName,
	            		"java.util.List<String>",
	            		true, 
	            		VariableInfo.AT_END ),
	            new VariableInfo(
	            		favHTMLPieceLinksVariableName,
	            		"java.util.List<String>",
	            		true, 
	            		VariableInfo.AT_END ),
	        };
	    }
	}
}
