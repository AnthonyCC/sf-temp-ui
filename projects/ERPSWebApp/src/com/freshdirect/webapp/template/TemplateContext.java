package com.freshdirect.webapp.template;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeCategory;
import com.freshdirect.fdstore.content.RecipeSubcategory;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.content.BaseTemplateContext;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;

/**
 * Collection of helper methods made available to templates.
 */
public class TemplateContext extends BaseTemplateContext{

	private final static Image IMAGE_BLANK = new Image("/media_stat/images/layout/clear.gif", 1, 1);

	//private static final Category LOGGER = Category.getInstance(TemplateContext.class);

//	private final Map parameters;
	
	/**
	 * Create a template context with no rendering parameters.
	 */
	public TemplateContext() {
//		this(Collections.EMPTY_MAP);
		super();
	}
	
	/**
	 * Create a template context with additional rendering parameters.
	 * 
	 * @param parameters map of optional rendering parameters (never null)
	 */
	public TemplateContext(Map parameters) {
		//this.parameters = Collections.unmodifiableMap(parameters);
		super(parameters);
	}
	
	/**
	 * Get optional rendering parameters.
	 * 
	 * @return parameter map (never null)
	 *//*
	public Map getParameters() {
		return this.parameters;
	}*/
	
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
				
				node = ContentFactory.getInstance().getProduct(catId, prodId);
				
				if (node == null) {
					// not found, fallback to primary home
					id = id.substring(0, sep);
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
	public List getNodes(List ids) {
		ArrayList ret = new ArrayList();
		for (Iterator i = ids.iterator(); i.hasNext();) {
			String id = (String) i.next();
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
	public Map getNodesMap(Map idMap) {		
		
		Map ret = new LinkedHashMap(idMap.size());
		for (Iterator i=idMap.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry) i.next();
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

	public static java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

	/**
	 *  Return the pricing with the correct sales unit for the node.
	 *  Code copied from the JspMethods class.
	 */
	public String getPrice(ContentNodeModel node) {
		String       price = "";
		
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel product    = (ProductModel) node;
			SkuModel     defaultSku = product.getDefaultSku();
			
			try {
				if (defaultSku != null) {
					FDProductInfo pi    = FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
					//pi.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(), pi.getDefaultPriceUnit().toLowerCase())
					price =  currencyFormatter.format(pi.getDefaultPrice())+"/"+ pi.getDisplayableDefaultPriceUnit();
					
			 	}
			} catch (FDResourceException e) {
			} catch (FDSkuNotFoundException e) {
			}
		}
		
		return price;
	}
	
	public String getBasePrice(ContentNodeModel node) {
		String       price = "";
		//System.out.println("********** inside getBasePrice "+node);
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel product    = (ProductModel) node;
			String     defaultSku = product.getDefaultSkuCode();
			//System.out.println("********** inside getBasePrice default sku: "+defaultSku);
			
			try {
				if (defaultSku != null) {
					FDProductInfo pi    = FDCachedFactory.getProductInfo(defaultSku);
					//pi.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(), pi.getDefaultPriceUnit().toLowerCase())
					price =  currencyFormatter.format(pi.getBasePrice());
					//System.out.println("********** inside getBasePrice baseprice: "+price);
			 	}
			} catch (FDResourceException e) {
			} catch (FDSkuNotFoundException e) {
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
			SkuModel     defaultSku = product.getDefaultSku();
			
			try {
				if (defaultSku != null) {
					FDProductInfo pi    = FDCachedFactory.getProductInfo(defaultSku.getSkuCode());
					//pi.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(), pi.getDefaultPriceUnit().toLowerCase())
					price =  currencyFormatter.format(pi.getDefaultPrice());
			 	}
			} catch (FDResourceException e) {
			} catch (FDSkuNotFoundException e) {
			}
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
	public Map retainAvailableMap(Map nodeMap, int maxItemCount) {
		Map ret = new LinkedHashMap(nodeMap);
		int count = 0;
		for (Iterator i = ret.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry e = (Map.Entry) i.next();			
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
	public Object[][] flatten(Map map) {
		Object[][] l = new Object[map.size()][2];
		int c = 0;
		for (Iterator i = map.entrySet().iterator(); i.hasNext(); c++) {
			Map.Entry e = (Entry) i.next();
			l[c] = new Object[] {e.getKey(), e.getValue()};
		}
		
		return l;
	}

	public Object[][] magic(Map idMap, int maxItemCount) {		
		return flatten( retainAvailableMap(getNodesMap(idMap), maxItemCount) );
	}
	
	
	public String getDisplayProductNames(Map idMap, int maxItemCount) {
		StringBuffer strBuffer=new StringBuffer();
		Map avMap=retainAvailableMap(getNodesMap(idMap), maxItemCount);				
		int c = 0;
		for (Iterator i = avMap.entrySet().iterator(); i.hasNext(); c++) {
			Map.Entry e = (Entry) i.next();
			ContentNodeModel nodeI=(ContentNodeModel)e.getKey();
									
			if(nodeI!=null)
			{		
				if(nodeI instanceof ProductModel){
					ProductModel node=(ProductModel)nodeI; 
			     if(c==avMap.size()-1){
			        //strBuffer.append("<a href='/product.jsp?productId="+node.getPK().getId()+"&catId="+node.getParentNode().getPK().getId()+"'>").append(node.getContentName()).append("</a>");
			    	 strBuffer.append("<a href='"+getHref(node,"")+"'>").append(node.getFullName()).append("</a>");
			     }else{
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
	public List retainAvailable(List nodes, int maxItemCount) {
		List ret = new ArrayList();
		int count = 0;
		for (Iterator i = nodes.iterator(); i.hasNext(); ) {
		        ContentNodeModel node = (ContentNodeModel) i.next();
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
//	
//	public static void main(String args[]){
//	     HashMap map=new HashMap();
//	     map.put("Product:product1","");
//	     map.put("Product:product2","");
//	     map.put("Product:product3","");
//	     map.put("Product:product4","");	
//	     TemplateContext ctx=new TemplateContext();
//	     ctx.getDisplayProductNames(map,99);
//	     
//	}
	
	/**
	 * Apple Pricing -[APPDEV-209].
	 * Method to get the price/lb, for display. 
	 */
	public String getAboutPrice(ContentNodeModel node)throws JspException {
		return JspMethods.getAboutPriceForDisplay(node);
	}	
}
