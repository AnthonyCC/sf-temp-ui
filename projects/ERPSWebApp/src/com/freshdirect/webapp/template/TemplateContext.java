package com.freshdirect.webapp.template;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
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
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.zone.FDZoneInfoManager;
import com.freshdirect.framework.content.BaseTemplateContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.ProductCartStatusMessageTag;
import com.freshdirect.webapp.taglib.fdstore.TxProductControlTag;
import com.freshdirect.webapp.taglib.fdstore.TxSingleProductPricingSupportTag;
import com.freshdirect.webapp.taglib.fdstore.display.FDCouponTag;
import com.freshdirect.webapp.taglib.fdstore.display.GetContentNodeWebIdTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductAboutPriceTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductBurstClassTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductDefaultPriceTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductGroupLinkTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductGroupPricingTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductPriceDescriptionTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductSavingTag;
import com.freshdirect.webapp.taglib.fdstore.display.ProductWasPriceTag;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.TransactionalProductImpression;
import com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy;

/**
 * Collection of helper methods made available to templates.
 */
public class TemplateContext extends BaseTemplateContext{
	
	private PricingContext pricingContext;
	private static List<PricingContext> pricingContexts = null;

	private final static Logger LOGGER = LoggerFactory.getInstance(TemplateContext.class);
		
	/**
	 * Create a template context with no rendering parameters.
	 */
	public TemplateContext() {
		super();
	}
	
	private static List<PricingContext> loadPricingContexts() {
		List<PricingContext> pricingContexts = new ArrayList<PricingContext>();
		try {
			Collection<String> zones =FDZoneInfoManager.loadAllZoneInfoMaster();
			if(null != zones && !zones.isEmpty()){
				for (Iterator iterator = zones.iterator(); iterator.hasNext();) {
					String zone = (String) iterator.next();
					PricingContext context = new PricingContext(new ZoneInfo(zone, "0001", "01"));//Default SalesOrg info for FD.
					pricingContexts.add(context);
				}
			}
		} catch (FDResourceException e) {
			LOGGER.error("Failed to get all pricing zones:"+e);
		}
		
		return pricingContexts;
	}
	
	private static List<PricingContext> getPricingContexts(){
		if(null == pricingContexts){
			pricingContexts = loadPricingContexts();
		}
		return pricingContexts;
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
		Map<String, String[]> reqParams = new HashMap<String, String[]>();
		return getHref(node, trackingCode, false, reqParams);
	}
	
	public String getHref(ContentNodeModel node, String trackingCode, boolean appendWineParams, Map<String, String[]> reqParams) {
		String link = null;
		if (node instanceof ProductModel) {
			// link to product in its category			
			/// link = "/product.jsp?catId=" + node.getParentNode().getContentName() + "&productId=" + node.getContentName();

			String url;
			if (appendWineParams) {
				url = FDURLUtil.getWineProductURI((ProductModel)node, trackingCode, (Map<String, String[]>) reqParams);
			} else {
				url = FDURLUtil.getProductURI((ProductModel)node, trackingCode);
			}
			return url;
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

		//check for a product model...
		ContentNodeModel nodePMCheck = ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(id));
		if (nodePMCheck != null && nodePMCheck instanceof ProductModel) {
			//...product model, return back a pricing context
			return ProductPricingFactory.getInstance().getPricingAdapter(((ProductModel)nodePMCheck), pricingContext);
		}else{
			//...not product model, do normal decode
			return ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(id));
		}
	}
	
	/* returns a configured product impression (required for several other display tags) */
	public ProductImpression getConfProdImpression(String id, FDSessionUser user) {
		ContentNodeModel node = getNode(id);
		if (node!= null && node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel pm = (ProductModel) node;

			ConfigurationContext confContext = new ConfigurationContext();
			confContext.setFDUser((FDUserI) user);
			ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();
			
			return confStrat.configure(pm, confContext);
		}
		return null;
	}
	
	//this does TxSingleProductPricingSupportTag
	public String getTxSingleProductPricingSupportFromTag(String id, FDSessionUser user, String formName, String namespace, String statusPlaceholder, String subTotalPlaceholderId, ProductImpression imp) throws JspException {
		String content = null;
		
		if (id != null && user != null) { //these are required
			ProductImpression impression = null;
			
			if (imp != null) {
				impression = imp;
			} else {
				impression = getConfProdImpression(id, user);
			}
			
			if (impression != null) {
				TxSingleProductPricingSupportTag singleProdPricing = new TxSingleProductPricingSupportTag();
				singleProdPricing.setCustomer((FDUserI) user);
				singleProdPricing.setImpression(impression);
				singleProdPricing.setFormName(formName);
				singleProdPricing.setNamespace(namespace);
				singleProdPricing.setStatusPlaceholder(statusPlaceholder);
				singleProdPricing.setSubTotalPlaceholderId(subTotalPlaceholderId);
				
				content = singleProdPricing.getContent();
			}
		}
		
		if (content != null) {
			try {
				return content;
			} catch (Exception e) {
				LOGGER.error("Error Occurred while getting TxSingleProductPricingSupportTag (Freemarker) "+e.getMessage());
			}
		}
		
		return "";
	}
	
	//this does TxProductControlTag
	public String getTxProductControlFromTag(String id, FDSessionUser user, String inputNamePostfix, int txNumber, String namespace, boolean disabled, boolean setMinimumQt, TransactionalProductImpression imp) {
		if (id != null) {
			TransactionalProductImpression impression = null;
			
			if (imp != null) {
				impression = imp;
			} else {
				impression = (TransactionalProductImpression) getConfProdImpression(id, user);
			}
			
			if (impression != null) {
				return TxProductControlTag.getHTMLFragment(impression, inputNamePostfix, txNumber, namespace, disabled, setMinimumQt);
			}
		}
		
		return "";
	}
	
	//ProductWasPriceTag
	public String getProductWasPriceFromTag(String id) throws JspException {
		if (id != null) {
			ContentNodeModel node = getNode(id);
			if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
				ProductModel pm = (ProductModel) node;
				
				ProductWasPriceTag prodWasPriceTag = new ProductWasPriceTag();
				prodWasPriceTag.setProduct(pm);

				try {
					return prodWasPriceTag.getContent().toString();
				} catch (Exception e) {
					LOGGER.error("Error Occurred while getting ProductWasPriceTag (Freemarker) "+e.getMessage());
				}
			}
		}
		
		return "";
	}
	
	//ProductAboutPriceTag
	public String getProductAboutPriceFromTag(String id) throws JspException {

		if (id != null) {
			ContentNodeModel node = getNode(id);
			if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
				ProductModel pm = (ProductModel) node;
				
				ProductAboutPriceTag prodAboutPriceTag = new ProductAboutPriceTag();
				prodAboutPriceTag.setProduct(pm);

				try {
					return prodAboutPriceTag.getContent().toString();
				} catch (Exception e) {
					LOGGER.error("Error Occurred while getting ProductAboutPriceTag (Freemarker) "+e.getMessage());
				}
			}
		}
		
		return "";
	}
	
	//this does ProductPriceDescriptionTag
	public String getProductPriceDescriptionFromTag(String id, FDSessionUser user, ProductImpression imp) {
		if (id != null && user != null) { //these are required
			ProductImpression impression = null;
			
			if (imp != null) {
				impression = imp;
			} else {
				impression = getConfProdImpression(id, user);
			}
			
			if (impression != null) {
				ProductPriceDescriptionTag prodPriceDescrip = new ProductPriceDescriptionTag();
				prodPriceDescrip.setImpression(impression);
				
				return prodPriceDescrip.getContent().toString();
			}
			
		}
		
		return "";
	}
	
	//this does ProductGroupLinkTag
	public String getProductGroupLinkFromTag(String id, FDSessionUser user, String trackingCode, ProductImpression imp) {
		StringBuilder linkHtml = new StringBuilder();
		if (id != null && user != null) { //these are required
			ProductImpression impression = null;
			
			if (imp != null) {
				impression = imp;
			} else {
				impression = getConfProdImpression(id, user);
			}
			
			if (impression != null) {
				ProductGroupLinkTag prodGroupLinkTag = new ProductGroupLinkTag();
				prodGroupLinkTag.setImpression(impression);
				prodGroupLinkTag.setTrackingCode(trackingCode);
				
				linkHtml.append(prodGroupLinkTag.getContentStart());
					//get price info
					linkHtml.append(getProductGroupPricingFromTag(id));
				linkHtml.append(prodGroupLinkTag.getContentEnd());
			}
			
		}

		return linkHtml.toString();
	}
	
	//this does ProductGroupPricingTag
	public String getProductGroupPricingFromTag(String id) {
		if (id != null) { //this is required
			ContentNodeModel node = getNode(id);
			if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
				ProductModel pm = (ProductModel) node;
				
				ProductGroupPricingTag prodGroupPricingTag = new ProductGroupPricingTag();
				prodGroupPricingTag.setProduct(pm);

				try {
					return prodGroupPricingTag.getContent();
				} catch (Exception e) {
					LOGGER.error("Error Occurred while getting ProductGroupPricingTag (Freemarker) "+e.getMessage());
				}
				
			}
		}
		return "";
	}
	
	//this does ProductSavingTag
	public String getProductSavingFromTag(String id) {
		if (id != null) { //this is required
			ContentNodeModel node = getNode(id);
			if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
				ProductModel pm = (ProductModel) node;
				
				ProductSavingTag prodSavingTag = new ProductSavingTag();
				prodSavingTag.setProduct(pm);

				try {
					return prodSavingTag.getContent();
				} catch (Exception e) {
					LOGGER.error("Error Occurred while getting ProductSavingTag (Freemarker) "+e.getMessage());
				}
				
			}
		}
		return "";
	}
	
	//this does ProductDefaultPriceTag
	public String getProductDefaultPriceFromTag(String id, boolean showDesc) {
		if (id != null) { //this is required
			ContentNodeModel node = getNode(id);
			if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
				ProductModel pm = (ProductModel) node;
				
				ProductDefaultPriceTag prodDefaultPriceTag = new ProductDefaultPriceTag();
				prodDefaultPriceTag.setProduct(pm);
				prodDefaultPriceTag.setShowDescription(showDesc);

				try {
					return prodDefaultPriceTag.getContent();
				} catch (Exception e) {
					LOGGER.error("Error Occurred while getting ProductDefaultPriceTag (Freemarker) "+e.getMessage());
				}
				
			}
		}
		return "";		
	}

	//this does ProductBurstClassTag
	public String getProductBurstClassFromTag(String id, FDSessionUser user, boolean hideFave, boolean hideDeal, boolean hideNewAndBack, boolean useRegularDealOnly) {
		if (id != null && user != null) { //these are required
			ContentNodeModel node = getNode(id);
			if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
				ProductModel pm = (ProductModel) node;
				
				ProductBurstClassTag prodBurstClassTag = new ProductBurstClassTag();
				prodBurstClassTag.setProduct(pm);
				if (hideFave) {
					prodBurstClassTag.setHideFave(hideFave);
				}
				if (hideDeal) {
					prodBurstClassTag.setHideFave(hideDeal);
				}
				if (hideNewAndBack) {
					prodBurstClassTag.setHideFave(hideNewAndBack);					
				}
				if (useRegularDealOnly) {
					prodBurstClassTag.setHideFave(useRegularDealOnly);	
				}

				try {
					return prodBurstClassTag.getContent(user);
				} catch (Exception e) {
					LOGGER.error("Error Occurred while getting ProductBurstClassTag (Freemarker) "+e.getMessage());
				}
				
			}
		}
		return "";		
	}
	
	public String getProductBurstClassFromTag(String id, FDSessionUser user) {
		return getProductBurstClassFromTag(id, user, false, false, false, false);
	}
	
	
	public String getProductDefaultPriceFromTag(String id) {
		return getProductDefaultPriceFromTag(id, true);
	}
	
	//this does ProductCartStatusMessageTag
	public String getProductCartStatusMessageFromTag(String id, FDSessionUser user) {
		String content = null;
		if (id != null && user != null) { //these are required
			ContentNodeModel node = getNode(id);
			if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
				
				ProductCartStatusMessageTag prodCartStatusMessage = new ProductCartStatusMessageTag();
				prodCartStatusMessage.setFDUser(user);
				prodCartStatusMessage.setProduct((ProductModel) node);
				
				content = prodCartStatusMessage.getContent();
			}
		}
		
		if (content != null) {
			try {
				return content;
			} catch (Exception e) {
				LOGGER.error("Error Occurred while getting ProductCartStatusMessagetag (Freemarker) "+e.getMessage());
			}
		}
		
		return "";
	
	}
	
	public ContentNodeModel getNode(String id,PricingContext pricingContext) {
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

		//check for a product model...
		ContentNodeModel nodePMCheck = ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(id));
		if (nodePMCheck != null && nodePMCheck instanceof ProductModel) {
			//...product model, return back a pricing context
			return ProductPricingFactory.getInstance().getPricingAdapter(((ProductModel)nodePMCheck), pricingContext);
		}else{
			//...not product model, do normal decode
			return ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(id));
		}
	}

	public Object[] getNodes(String id) {
		Object[] obj = new Object[getPricingContexts().size()];
		int i=0;
		for (Iterator<PricingContext> iterator = getPricingContexts().iterator(); iterator.hasNext();) {
			PricingContext pricingContext = (PricingContext) iterator.next();
			ContentNodeModel node =getNode(id, pricingContext);
			obj[i++]= node;
		}
		return obj;
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
	
	public Map<Object[], Object> getNodesMapForAllZones(Map<String, Object> idMap) {		
		
		Map<Object[], Object> ret = new LinkedHashMap<Object[], Object>(idMap.size());
		for (Iterator<Map.Entry<String, Object>> i=idMap.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry<String, Object> e = i.next();
			String contentId = (String) e.getKey();
			Object[] node = getNodes(contentId);
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
	
	private final static DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");

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
				if(calc.getSkuModel() == null) return price;//SAFE CHECK: Returns empty string when product's sku is unavailable or null.
				if(calc.getZonePriceInfoModel().isItemOnSale()){
					price = calc.getSellingPriceOnly();	
				}
			}catch(FDResourceException fe){
				LOGGER.error("Error Occurred while getting base price (Freemarker) "+fe.getMessage());
			}catch(FDSkuNotFoundException fse){
				LOGGER.error("Error Occurred while getting base price (Freemarker) "+fse.getMessage());
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

	public Object[][] flattenAll(Map<Object[], Object> map) {
		Object[][] l = new Object[map.size()][2];
		int c = 0;
		for (Iterator<Map.Entry<Object[], Object>> i = map.entrySet().iterator(); i.hasNext(); c++) {
			Map.Entry<Object[], Object> e =  i.next();
			l[c] = new Object[] {e.getKey(), e.getValue()};
		}
		
		return l;
	}
	
	public Object[][] magic(Map<String, Object> idMap, int maxItemCount) {		
		return flatten( retainAvailableMap(getNodesMap(idMap), maxItemCount) );
	}
	
	public Object[][] magicNodes(Map<String, Object> idMap, int maxItemCount) {		
		return flattenAll( getNodesMapForAllZones(idMap));
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
		return img == null ? Image.BLANK_IMAGE : img;
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
			LOGGER.warn("No Category '"+catId+"' for Product '"+prodId+"' (Freemarker)");
			node = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(ContentType.get("Product"), prodId));
		}
		
		return ProductPricingFactory.getInstance().getPricingAdapter(node, pricingContext);
	}
	
	public String getScaleDisplay(ContentNodeModel node) {
		String scaleDisplay = "";
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel productNode = (ProductModel) node;
			ProductImpression impression = new ProductImpression(productNode);
			FDGroup group = impression.getFDGroup(); //Returns if group is associated with any sku linked to this product.
			MaterialPrice matPrice = null;
			if(null !=group){
				try {
					matPrice = GroupScaleUtil.getGroupScalePrice(group, impression.getPricingZone());
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(matPrice !=null) {
				try {
//					MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, impression.getPricingZoneId());
//					if(matPrice != null) {
							double displayPrice = 0.0;
							boolean isSaleUnitDiff = false;
							if(matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
								displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
							else {
								displayPrice = matPrice.getPrice();
								isSaleUnitDiff = true;
							}
							GroupScalePricing grpPricing = GroupScaleUtil.lookupGroupPricing(group);
							StringBuffer buf1 = new StringBuffer();
							if(matPrice.getScaleUnit().equals("LB")) {//Other than eaches append the /pricing unit for clarity.
								buf1.append( "<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+"\" class=\"text10rbold\" style=\"color: #CC0000;\">Any " );
								buf1.append( quantityFormatter.format( matPrice.getScaleLowerBound() ) );
								buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
								buf1.append( " " );
								buf1.append( "of any " );
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( currencyFormatter.format( displayPrice) );
								buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</a>" );

							} else {
								buf1.append( "<a href=\"/group.jsp?grpId="+group.getGroupId()+"&version="+group.getVersion()+"\" class=\"text10rbold\" style=\"color: #CC0000;\">Any " );
								buf1.append( quantityFormatter.format( matPrice.getScaleLowerBound() ) );
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( currencyFormatter.format( displayPrice) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</a>" );

							}
							scaleDisplay= buf1.toString();
//					}
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//no group, do the normal scale string fetch
					PriceCalculator priceCalculator = impression.getProductModel().getPriceCalculator();
					scaleDisplay = priceCalculator.getTieredPrice(0);
				}
			}else{
				//no group, do the normal scale string fetch
				PriceCalculator priceCalculator = impression.getProductModel().getPriceCalculator();
				scaleDisplay = priceCalculator.getTieredPrice(0);
			}
		}
		return scaleDisplay != null ? scaleDisplay : "";
	}
	
	public String getScalePrice(ContentNodeModel node) {
		String scaleDisplay = "";
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel productNode = (ProductModel) node;
			ProductImpression impression = new ProductImpression(productNode);
			FDGroup group = impression.getFDGroup(); //Returns if group is associated with any sku linked to this product.
			MaterialPrice matPrice = null;
			if(null !=group){
				try {
					matPrice = GroupScaleUtil.getGroupScalePrice(group, impression.getPricingZone());
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(matPrice !=null) {
				try {
//					MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, impression.getPricingZoneId());
//					if(matPrice != null) {
							double displayPrice = 0.0;
							boolean isSaleUnitDiff = false;
							if(matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
								displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
							else {
								displayPrice = matPrice.getPrice();
								isSaleUnitDiff = true;
							}
							GroupScalePricing grpPricing = GroupScaleUtil.lookupGroupPricing(group);
							StringBuffer buf1 = new StringBuffer();
							if(matPrice.getScaleUnit().equals("LB")) {//Other than eaches append the /pricing unit for clarity.
								buf1.append( "Any " );
								buf1.append( quantityFormatter.format( matPrice.getScaleLowerBound() ) );
								buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
								buf1.append( " " );
								buf1.append( "of any " );
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( currencyFormatter.format( displayPrice) );
								buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</a>" );

							} else {
								buf1.append( "Any " );
								buf1.append( quantityFormatter.format( matPrice.getScaleLowerBound() ) );
								buf1.append( " " );
								buf1.append( grpPricing.getShortDesc() );
								buf1.append( " for " );
								buf1.append( currencyFormatter.format( displayPrice) );
								if(isSaleUnitDiff)
									buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
								buf1.append( "</a>" );

							}
							scaleDisplay= buf1.toString();
//					}
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					PriceCalculator priceCalculator = impression.getProductModel().getPriceCalculator();
					scaleDisplay = priceCalculator.getTieredPrice(0);
				}
			}else{
				//no group, do the normal scale string fetch
				PriceCalculator priceCalculator = impression.getProductModel().getPriceCalculator();
				scaleDisplay = priceCalculator.getTieredPrice(0);
			}
		}
		return scaleDisplay != null ? scaleDisplay : "";
	}
	
	public String getRatingInfo(ContentNodeModel node){
		String rating ="";
		if (node.getContentType().equals(ContentNodeModel.TYPE_PRODUCT)) {
			ProductModel productNode = (ProductModel) node;
			rating =null!=productNode.getRatingRelatedImage()?productNode.getRatingRelatedImage().toHtml():"";
		}
		return rating;
	}
	
	public static String getScaleDisplay(ProductModel productNode,ZoneInfo zoneInfo){
		String scaleDisplay = "";
		ProductImpression impression = new ProductImpression(productNode);
		FDGroup group = impression.getFDGroup(); //Returns if group is associated with any sku linked to this product.
		MaterialPrice matPrice = null;
		if(null !=group){
			try {
				matPrice = GroupScaleUtil.getGroupScalePrice(group, zoneInfo);
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(matPrice !=null) {
			try {
//				MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, impression.getPricingZoneId());
//				if(matPrice != null) {
						double displayPrice = 0.0;
						boolean isSaleUnitDiff = false;
						if(matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
							displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
						else {
							displayPrice = matPrice.getPrice();
							isSaleUnitDiff = true;
						}
						GroupScalePricing grpPricing = GroupScaleUtil.lookupGroupPricing(group);
						StringBuffer buf1 = new StringBuffer();
						if(matPrice.getScaleUnit().equals("LB")) {//Other than eaches append the /pricing unit for clarity.
							
							buf1.append( quantityFormatter.format( matPrice.getScaleLowerBound() ) );
							buf1.append(matPrice.getScaleUnit().toLowerCase()).append("s");
							buf1.append( " " );							
							buf1.append( " for " );
							buf1.append( currencyFormatter.format( displayPrice) );
							buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());
							

						} else {
							buf1.append( quantityFormatter.format( matPrice.getScaleLowerBound() ) );
							buf1.append( " " );
							buf1.append( " for " );
							buf1.append( currencyFormatter.format( displayPrice) );
							if(isSaleUnitDiff)
								buf1.append("/").append(matPrice.getPricingUnit().toLowerCase());

						}
						scaleDisplay= buf1.toString();
//				}
			} catch (FDResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//no group, do the normal scale string fetch
				PriceCalculator priceCalculator = impression.getProductModel().getPriceCalculator();
				scaleDisplay = priceCalculator.getTieredPrice(0);
			}
		}else{
			//no group, do the normal scale string fetch
			PriceCalculator priceCalculator = impression.getProductModel().getPriceCalculator();
			scaleDisplay = priceCalculator.getTieredPrice(0);
		}
		return scaleDisplay != null ? scaleDisplay : "";
	}
	
	/* for test page purposes */
	public ArrayList<String> getMethodsFromClass(Class classObj) {
		ArrayList<String> methodList = new ArrayList<String>();
		Method[] methods = classObj.getDeclaredMethods();
		for (Method method : methods) {
			methodList.add(method.toGenericString());
        }
		
		return methodList;
	}

	public Field getDeclaredField(Class classObj, String name) {
		if (classObj == null) { return null; }
		Field field = null;
		try {
			field = classObj.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return field;
	}

	public ArrayList<String> getDeclaredFieldsFromClass(Class classObj) {
		ArrayList<String> fieldList = new ArrayList<String>();
		Field[] fields = classObj.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				//field.setAccessible(true);
            	fieldList.add((String)field.getName()+":"+field.get(null));
				//fieldList.add((String)field.get(null));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
        }
		
		return fieldList;
	}

	
	
	private static final Map<String, Class<?>> BUILT_IN_MAP = 
	    new ConcurrentHashMap<String, Class<?>>();

	static {
	    for (Class<?> c : new Class[]{void.class, boolean.class, byte.class, char.class,  
	            short.class, int.class, float.class, double.class, long.class})
	        BUILT_IN_MAP.put(c.getName(), c);
	}

	public Class<?> forName(String name) throws ClassNotFoundException {
	    Class<?> c = BUILT_IN_MAP.get(name);
	    if (c == null)
	        // assumes you have only one class loader!
	        BUILT_IN_MAP.put(name, c = Class.forName(name));
	    return c;
	}
	/* for test page purposes */
	public Object getNewInstanceByStringName(String fullClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object instanceObject = null;
		try {
//			Class cls = null;
//			cls = this.forName(fullClassName);
//			if (cls == null) {
//				cls = Class.forName(fullClassName);
//				instanceObject = cls.newInstance();
//			} else {
//				instanceObject = cls;
//			}
			Class<?> cls;

			if (fullClassName.endsWith("[]")) {
				instanceObject = java.lang.reflect.Array.newInstance(Class.forName(fullClassName.replaceAll("\\[\\]", "")), 0);
			} else {
				cls = Class.forName(fullClassName);

			    Constructor c;
				try {
					c = cls.getDeclaredConstructor();
					c.setAccessible(true);   // solution
				    instanceObject = c.newInstance();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
				//doesn't always work
				//instanceObject = cls.newInstance();
			}
		} catch (ClassNotFoundException e) {
			LOGGER.debug("Unable to find Class with name: " + fullClassName + " (Freemarker)");
			e.printStackTrace();
		} catch (InstantiationException e) {
			LOGGER.debug("Unable to initialize Class with name: " + fullClassName + " (Freemarker)");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOGGER.debug("Unable to access Class with name: " + fullClassName + " (Freemarker)");
			e.printStackTrace();
		}
		
		return instanceObject;
	}
	
	/* expose coupon to freemarker */
	public FDCustomerCoupon getCoupon(FDSessionUser user, String id, String skuOverride, String coupContext) {
		FDCustomerCoupon curCoupon = null;
		
		if (user == null) {
			LOGGER.warn("required user object not set in getCoupon (Freemarker)");
			return null;
		}
		
		if (id != null && id != "") {
			ContentNodeModel node = null;
		
			node = getNode(id);
			if (node instanceof ProductModel || node instanceof ProductModelPricingAdapter || node instanceof SkuModel) {
				String skuString = null;
				
				if (node instanceof ProductModel && !(node instanceof ProductModelPricingAdapter)) {
					ProductModel nodePM = (ProductModel)node;
					node = getNode("Product:"+nodePM.toString()+"@"+nodePM.getCategory()); //turn in to ProductModelPricingAdapter
				}
				if (node instanceof ProductModelPricingAdapter) {
					ProductModel nodePM = (ProductModel)node;
					/* match against skuOverride */
					if ( skuOverride != null && !"".equals(skuOverride) ) { //look for a match
						List<String> skus = nodePM.getSkuCodes();
						for (String sku : skus) {
							if (sku.equals(skuOverride)) {
								skuString = sku;
								break;
							}
						}
					}
					if (skuString == null) { //no match, fall back to default
						skuString = nodePM.getDefaultSkuCode();
					}
					
				}
				if (node instanceof SkuModel) {
					SkuModel nodeSM = (SkuModel)node;
					skuString = nodeSM.getSkuCode();
				}
				
				curCoupon = getCouponBySku(user, skuString, coupContext);
			}
		
		}
		
		return curCoupon;
	}
	/* without coupon context */
	public FDCustomerCoupon getCoupon(FDSessionUser user, String id, String skuOverride) {
		return getCoupon(user, id, skuOverride, null);
	}
	/* without sku override OR coupon context */
	public FDCustomerCoupon getCoupon(FDSessionUser user, String id) {
		return getCoupon(user, id, null, null);
	}

	/* get coupon by sku */
	public FDCustomerCoupon getCouponBySku(FDSessionUser user, String id, String couponContext) {
		if (user == null) {
			LOGGER.warn("required user object not set in getCouponBySku (Freemarker)");
			return null;
		}
		if (id == null) {
			LOGGER.warn("required id object not set in getCouponBySku (Freemarker)");
			return null;
		}
		
		FDCustomerCoupon curCoupon = null;

		if (id != "") {
			SkuModel skuNode = (SkuModel) getNode("Sku:"+id);

			try {

				if (skuNode == null) {
					LOGGER.warn("skuNode is null in getCouponBySku (Freemarker)");
					return null;
				}
				if (skuNode.getProductInfo() == null) {
					LOGGER.warn("ProductInfo is null for sku "+skuNode.getSkuCode()+" in getCouponBySku (Freemarker)");
					return null;
				}
				
				EnumCouponContext coupContextEnum = null;
				if (couponContext == null) {
					couponContext = "PRODUCT"; //default
				}

				coupContextEnum = EnumCouponContext.getEnum(couponContext);

				if (coupContextEnum == null) {
					LOGGER.warn("coupContextEnum is null for the context: "+couponContext+" in getCouponBySku (Freemarker)");
					return null;
				}
				
				curCoupon = user.getCustomerCoupon(skuNode.getProductInfo().getUpc(), coupContextEnum);
				
			} catch (Exception e) {
				LOGGER.error("Error Occurred while getting coupon in getCouponBySku (Freemarker) "+e.getMessage());
			}
			
		}
		
		return curCoupon;
	}
	/* no context */
	public FDCustomerCoupon getCouponBySku(FDSessionUser user, String id) {
		return getCouponBySku(user, id, null);
	}
	
	/* get coupon display */
	public String getCouponDisplay(FDCustomerCoupon coupon) {
		String displayHtml = "";
		
		if (coupon != null) {
			FDCouponTag fdCouponTag = new FDCouponTag();
			fdCouponTag.setCoupon(coupon);
			displayHtml = fdCouponTag.getContent();
		} else {
			LOGGER.warn("required coupon not set in getCouponDisplay (Freemarker)");
		}
		
		return displayHtml;
	}
	
	/* helper method for making current wine id avail to ftls */
	public String getWineAssociateId() {
		return JspMethods.getWineAssociateId();
	}
}
