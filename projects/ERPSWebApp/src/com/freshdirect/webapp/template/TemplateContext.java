package com.freshdirect.webapp.template;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeCategory;
import com.freshdirect.fdstore.content.RecipeSubcategory;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.framework.content.BaseTemplateContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.display.GetContentNodeWebIdTag;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;

/**
 * Collection of helper methods made available to templates.
 */
public class TemplateContext extends BaseTemplateContext{
	
	private PricingContext pricingContext;

	private final static Image IMAGE_BLANK = new Image("/media_stat/images/layout/clear.gif", 1, 1);

	private final static Logger LOGGER = LoggerFactory.getInstance(TemplateContext.class);

	
	/**
	 * Create a template context with no rendering parameters.
	 */
	public TemplateContext() {
		super();
	}
	
	/**
	 * Create a template context with additional rendering parameters.
	 * 
	 * @param parameters map of optional rendering parameters (never null)
	 */
	@SuppressWarnings("unchecked")
	public TemplateContext(Map parameters) {
		//this.parameters = Collections.unmodifiableMap(parameters);
		super(parameters);
	}
	
	@SuppressWarnings("unchecked")
	public TemplateContext(Map parameters, PricingContext pCtx) {
		super(parameters);
		this.pricingContext = pCtx;
	}
	
	
	/**
	 * Get a relative URL to a contentnode.
	 * 
	 * @param node the node to get the URL for
	 * @param trackingCode trackingCode to append to link as <code>trk</code> parameter
	 * @return relative URL string, or null if no link is available.
	 */
	public String getHref(ContentNodeModel node, String trackingCode) {
		String link;
		if (node instanceof ProductModel) {
			// link to product in its category			
			/// link = "/product.jsp?catId=" + node.getParentNode().getContentName() + "&productId=" + node.getContentName();
			return FDURLUtil.getProductURI( (ProductModel)node, trackingCode);
		} else {
			link = PreviewLinkProvider.getLink(node.getContentKey());
		}
		if (link == null) {
			return null;
		}
		if (link.indexOf('?')<0) {
			return link + "?trk=" + trackingCode;
		} else {
			return link + "&trk=" + trackingCode;
		}
	}
	
	/**
	 * Get a content node by ID.
	 * 
	 * Special treat
	 * 
	 * @param id encoded ID String ("ContentType:id")
	 * @return the contentnode or null if it does not exist
	 */
	public ContentNodeModel getNode(String id) {
		ContentNodeModel node = null;
		if (id.startsWith("Product:")) {
			int sep = id.indexOf('@');
			if (sep!=-1) {
				String prodId = id.substring("Product:".length(), sep);
				
				String catId = id.substring(sep+1, id.length());
				
				node = ContentFactory.getInstance().getProductByName(catId, prodId);
				
				if (node == null) {
					// not found, fallback to primary home
					id = id.substring(0, sep);
				} else {
					//Return ProductModelPricingAdapter for zone pricing.
					return ProductPricingFactory.getInstance().getPricingAdapter(((ProductModel)node), pricingContext);
				}
			}
		}
		if (node != null) {
			return node;
		}
		return ContentFactory.getInstance().getContentNodeByKey(ContentKey.decode(id));
	}

	/**
	 * Get multiple content nodes by ID.
	 * 
	 * @param ids List of encoded ID Strings ("ContentType:id") (never null)
	 * @return List of {@link ContentNodeModel} (never null)
	 */
	public List<ContentNodeModel> getNodes(List<String> ids) {
		List<ContentNodeModel> ret = new ArrayList<ContentNodeModel>();
		
		for (String id : ids) {
			ContentNodeModel node = getNode(id);
			if (node != null) {
				ret.add(node);
			}
		}
		return ret;
	}

	/**
	 * Get multiple content nodes by IDs with associated parameters.
	 * 
	 * @param idMap Map of encoded ID Strings ("ContentType:id") -> Object
	 * @return Map of {@link ContentNodeModel} -> Object
	 */
	public Map<ContentNodeModel, Object> getNodesMap(Map<String, Object> idMap) {		
		
		Map<ContentNodeModel, Object> ret = new LinkedHashMap<ContentNodeModel, Object>(idMap.size());
		for (Iterator<Map.Entry<String, Object>> i=idMap.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry<String, Object> e = i.next();
			String contentId = (String) e.getKey();
			ContentNodeModel node = getNode(contentId);
			ret.put(node, e.getValue());
		}		
		return ret;
	}

	/**
	 * Determine if a content node is available for display.
	 * 
	 * @param node Content node
	 * @return false if the node is null, or unavailable
	 */
	public boolean isAvailable(ContentNodeModel node) {
		if (node == null) {
			return false;
		}
		if (node instanceof ProductModel) {
			ProductModel productModel = (ProductModel) node;
			if (productModel.isUnavailable()) {
				return false;
			}
		} else if (node instanceof Recipe) {
			Recipe recipe = (Recipe) node;
			if (!recipe.isAvailable()) {
				return false;
			}
		}
		return true;
	}

	public static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

	/**
	 *  Return the pricing with the correct sales unit for the node.
	 *  Code copied from the JspMethods class.
	 */
	public String getPrice(ContentNodeModel node) {
		String       price = "";
		
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel product    = (ProductModel) node;
			PriceCalculator calc = new PriceCalculator(pricingContext, product);
			price= calc.getDefaultPrice();
		}
		
		return price;
	}
	
	public String getBasePrice(ContentNodeModel node) {
		String       price = "";
		//System.out.println("********** inside getBasePrice "+node);
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel product    = (ProductModel) node;
			PriceCalculator calc = new PriceCalculator(pricingContext, product);
			try{
				if(calc.getZonePriceInfoModel().isItemOnSale()){
					price = calc.getSellingPriceOnly();	
				}else{
					
				}
			}catch(FDResourceException fe){
				LOGGER.error("Error Occurred while getting base price "+fe.getMessage());
			}catch(FDSkuNotFoundException fse){
				LOGGER.error("Error Occurred while getting base price "+fse.getMessage());
			}
			
			
		}
		
		return price;
	}
	
	/**
	 *  Return the pricing with the correct sales unit for the node.
	 *  Code copied from the JspMethods class.
	 */
	public String getWinePrice(ContentNodeModel node) {
		String       price = "";
		
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel product    = (ProductModel) node;
			PriceCalculator calc = new PriceCalculator(pricingContext, product);
			price= calc.getDefaultPriceOnly();
		}
		
		return price;
	}
		
	/**
	 * Filter a Map of content nodes with parameters, retaining available
	 * nodes up to a specified maximum count.
	 * 
	 * @param nodeMap Map of {@link ContentNodeI} -> Object
	 * @param maxItemCount maximum number of items to retain
	 * @return Map of {@link ContentNodeI} -> Object
	 */
	public Map<ContentNodeModel, Object> retainAvailableMap(Map<ContentNodeModel, Object> nodeMap, int maxItemCount) {
		Map<ContentNodeModel, Object> ret = new LinkedHashMap<ContentNodeModel, Object>(nodeMap);
		int count = 0;
		for (Iterator<Map.Entry<ContentNodeModel, Object>> i = ret.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry<ContentNodeModel, Object> e = i.next();			
			ContentNodeModel node = (ContentNodeModel) e.getKey();
			if (count>=maxItemCount || !isAvailable(node)) {
				i.remove();
			} else {
				count++;
			}
		}
		return ret;
	}

	/**
	 * @param map Map of Object -> Object
	 * @return array of object pairs (Object [key], Object [value])
	 */
	public Object[][] flatten(Map<ContentNodeModel, Object> map) {
		Object[][] l = new Object[map.size()][2];
		int c = 0;
		for (Iterator<Map.Entry<ContentNodeModel, Object>> i = map.entrySet().iterator(); i.hasNext(); c++) {
			Map.Entry<ContentNodeModel, Object> e =  i.next();
			l[c] = new Object[] {e.getKey(), e.getValue()};
		}
		
		return l;
	}

	public Object[][] magic(Map<String, Object> idMap, int maxItemCount) {		
		return flatten( retainAvailableMap(getNodesMap(idMap), maxItemCount) );
	}
	
	
	public String getDisplayProductNames(Map<String, Object> idMap, int maxItemCount) {
		StringBuilder strBuffer = new StringBuilder();
		Map<ContentNodeModel, Object> avMap=retainAvailableMap(getNodesMap(idMap), maxItemCount);				
		int c = 0;
		for (Iterator<Map.Entry<ContentNodeModel, Object>> i = avMap.entrySet().iterator(); i.hasNext(); c++) {
			Map.Entry<ContentNodeModel, Object> e = i.next();
			ContentNodeModel nodeI=(ContentNodeModel)e.getKey();
									
			if (nodeI!=null) {
				if (nodeI instanceof ProductModel){
					ProductModel node=(ProductModel)nodeI; 
					if (c==avMap.size()-1) {
						strBuffer.append("<a href='"+getHref(node,"")+"'>").append(node.getFullName()).append("</a>");
					} else{
						strBuffer.append("<a href='"+getHref(node,"")+"'>").append(node.getFullName()).append("</a>").append(" | ");
					}
				}
			}
		}
		return strBuffer.toString();
	}
	
	/**
	 * Filter a list of content nodes, retaining available nodes up to
	 * a specified maximum count.
	 * 
	 * @param nodes List of {@link ContentNodeI} (never null)
	 * @param maxItemCount maximum number of items to retain
	 * @return List of available {@link ContentNodeI}s (never null)
	 */
	public List<ContentNodeModel> retainAvailable(List<ContentNodeModel> nodes, int maxItemCount) {
		List<ContentNodeModel> ret = new ArrayList<ContentNodeModel>();
		int count = 0;
		
		for (ContentNodeModel node : nodes) {
			if (!isAvailable(node)) {
				continue;
			}
			ret.add(node);
			count++;
			if (count >= maxItemCount) {
				break;
			}
		}
		return ret;
	}

	/**
	 * Get the image to be presented in PODs.
	 * 
	 * @param node the node to be presented (never null)
	 * @return the image (never null)
	 */
	public Image getDefaultImage(ContentNodeModel node) {
		Image img = null;
		if (node instanceof ProductModel) {
			img = ((ProductModel) node).getCategoryImage();
		} else if (node instanceof Recipe) {
			img = ((Recipe) node).getPhoto();
		} else if (node instanceof DepartmentModel) {
			img = ((DepartmentModel) node).getPhoto();
		} else if (node instanceof RecipeCategory) {
			img = ((RecipeCategory) node).getPhoto();
		} else if (node instanceof RecipeSubcategory) {
			img = ((RecipeSubcategory) node).getLabel();
		} else if (node instanceof CategoryModel) {
			img = ((CategoryModel) node).getCategoryPhoto();
		}
		return img == null ? IMAGE_BLANK : img;
	}
	
	/**
	 * Apple Pricing -[APPDEV-209].
	 * Method to get the price/lb, for display. 
	 */
	public String getAboutPrice(ContentNodeModel node)throws JspException {
		return JspMethods.getAboutPriceForDisplay(node, pricingContext);
	}


	/**
	 * Returns a web and JavaScript safe ID.
	 * 
	 * @param prod
	 * @return
	 */
	public String getWebId(ProductModel prod) {
		return GetContentNodeWebIdTag.getWebId(null, prod, true);
	}



	public ProductModelPricingAdapter getProduct(String catId, String prodId) {
		ProductModel node = ContentFactory.getInstance().getProductByName(catId, prodId);

		if (node == null) {
			// fall back to primary home
			LOGGER.warn("No Category '"+catId+"' for Product '"+prodId+"'");
			node = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(new ContentKey(ContentType.get("Product"), prodId));
		}
		
		return ProductPricingFactory.getInstance().getPricingAdapter(node, pricingContext);
	}
}
