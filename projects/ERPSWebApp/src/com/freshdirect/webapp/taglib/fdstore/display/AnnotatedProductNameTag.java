package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.webapp.BodyTagSupport;


/**
 * Renders product name in product page
 * 
 * @author segabor
 *
 */
public class AnnotatedProductNameTag extends BodyTagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private ProductModel	product;
	private LayoutType		type = LayoutType.NORMAL;
	private	String			cssClass = "title18";
	
	private boolean isWineLayout = false;
	private boolean isQuickBuy = false;
	private boolean isAnnotated = FDStoreProperties.isAnnotationMode();
	
	public void setProduct(ProductModel product) {
		this.product = product;
	}


	public void setType(LayoutType type) {
		this.type = type;
	}


	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}


	/**
	 * Render for wine layout
	 * @param isWineLayout
	 */
	public void setWineLayout(boolean isWineLayout) {
		this.isWineLayout = isWineLayout;
	}


	/**
	 * @param isQuickBuy
	 */
	public void setQuickBuy(boolean isQuickBuy) {
		this.isQuickBuy = isQuickBuy;
	}
	
	
	/**
	 * Is annotated view?
	 * 
	 * @param isAnnotated
	 */
	public void setAnnotated(boolean isAnnotated) {
		this.isAnnotated = isAnnotated;
	}
	
	@Override
	public int doStartTag() throws JspException {
		String productTitle = isQuickBuy ? product.getFullName() : product.getFullName().toUpperCase(); 
		
		
		cssClass = isQuickBuy ? "title14" : "title18";
		
		/**
		 * PRODUCT TITLE
		 */
		switch(type) {
		case NORMAL:
			// Don't append wine vintage in Quick Buy panel
			if ( !isQuickBuy && isWineLayout ) {
				List<DomainValue> wineVintage= (List<DomainValue>) product.getWineVintage();		
				if ( wineVintage != null && wineVintage.size() > 0 ) {
					DomainValue dValue = (DomainValue)wineVintage.get(0);
					
					productTitle += " " + dValue.getLabel();
				}    	
			}
			break;
		case CONFIGURED:
		    if ( isWineLayout ) {
		        String thisProdBrandLabel = product.getPrimaryBrandName();

		        StringBuilder tmpName = new StringBuilder();
		        if (thisProdBrandLabel.length() > 0) {
		            tmpName.append(thisProdBrandLabel.toUpperCase());
		            tmpName.append("<br>");
		            tmpName.append("<span class=\"title14\">");
		            tmpName.append(product.getFullName().substring(thisProdBrandLabel.length()).trim().toUpperCase());
		            tmpName.append("</span>\n");

		            productTitle = tmpName.toString();
		        }
		    }
		    break;
		}
		


		StringBuilder buf = new StringBuilder();
		if (isAnnotated) {
			/**
			 * Annotated display
			 */
			buf.append("<script type=\"text/javascript\">\n");
			buf.append("  var annotation = '<b>SKUs</b><br>';\n");
			buf.append("  var detailAnnotation = annotation;\n");


			for (SkuModel skuModel : product.getSkus()) {
		    	String variation = "";

				List<DomainValue> varMtx = skuModel.getVariationMatrix();
				if (varMtx != null && varMtx.size() > 0) {
					StringBuilder vbuf = new StringBuilder();

					vbuf.append("<b>");
					for (DomainValue v : varMtx) {
						vbuf.append(v.getContentKey().getId());
						vbuf.append(" ");
					}
					vbuf.append("</b>");

					variation = vbuf.toString();
				}

		    	FDProductInfo fdProdInfo = null;
		    	FDProduct fdProd = null;
		    	try {
		    		fdProdInfo = FDCachedFactory.getProductInfo( skuModel.getSkuCode() );
		    		fdProd = FDCachedFactory.getProduct( fdProdInfo );
		    	} catch (FDSkuNotFoundException ex) {
		    		throw new JspException(ex);
		    	} catch (FDResourceException e) {
		    		throw new JspException(e);
				}

				
				buf.append("  ann = '&nbsp;';\n");
				
				buf.append("  ann += '<a target=\"_blank\" href=\"");
				buf.append(FDStoreProperties.getAnnotationErpsy());
				buf.append("/attribute/material/material_search.jsp?searchterm=");
				buf.append(skuModel.getSkuCode());
				buf.append("&searchtype=WEBID\">");
				buf.append(skuModel.getSkuCode());
				buf.append("</a>';\n");

				buf.append("  ann += ' " + variation + "';\n");
				buf.append("  ann += '<br>';\n");

				buf.append("  annotation += ann;\n");
				buf.append("  detailAnnotation += ann;\n");

				buf.append("  detailAnnotation += '&nbsp;&nbsp;&nbsp;';\n");
				if (fdProd != null) {
					buf.append("  detailAnnotation += '");
					buf.append(fdProd.getMaterial().getMaterialNumber().substring(9));
					buf.append(" ';\n");
				} else {
					buf.append("  detailAnnotation += 'NO PROD';\n");
				}
				
				if (fdProdInfo != null) {
					buf.append("  detailAnnotation += '");
					buf.append(fdProdInfo.getAvailabilityStatus().getShortDescription());
					buf.append("';\n");
				} else {
					buf.append("  detailAnnotation += 'NO PRODINFO';\n");
				}

				buf.append("  detailAnnotation += '<br><br>';\n");
			}

			buf.append("</script>\n");
			
			// annotated script end
			
			// <div id="annotate" onmouseover="return overlib(annotation, WIDTH, 240);" onclick="return overlib(detailAnnotation, STICKY, CLOSECLICK, CAPTION, 'Product details', WIDTH, 240);" onmouseout="return nd();" class="title18"><%= productTitle %></div>
			buf.append("<div id=\"annotate\"");
			buf.append(" onmouseover=\"return overlib(annotation, WIDTH, 240);\"");
			buf.append(" onclick=\"return overlib(detailAnnotation, STICKY, CLOSECLICK, CAPTION, 'Product details', WIDTH, 240);\"");
			buf.append(" onmouseout=\"return nd();\"");
			buf.append(" class=\""+cssClass+"\">");
			buf.append( productTitle );
			buf.append("</div>\n");
		} else {
			buf.append("<div class=\""+cssClass+"\">");
			buf.append( productTitle );
			buf.append("</div>\n");
		}
		
		
		try {
			pageContext.getOut().write(buf.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}

		return SKIP_BODY;
	}



	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}

	public static enum LayoutType {
		NORMAL, CONFIGURED
	}
}
