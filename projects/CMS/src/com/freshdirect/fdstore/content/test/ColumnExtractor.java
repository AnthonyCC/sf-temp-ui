package com.freshdirect.fdstore.content.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.CartesianProduct;
import com.freshdirect.framework.util.ListConcatenation;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.UniqueRandomSequence;



/**
 * Extract a column for a {@link ContentBundle}.
 * 
 * The column extraction proceeds in two steps.
 * First, before a row is added to the {@link ContentBundle}, the column extractos'
 * cacheAttribute methods are invoked. The extractors are expected to put the value
 * they extract in the cache so other extractors do not need to calculate it. More
 * importantly, the cacheAttribute method may call the 
 * {@link ContentBundle.RowCache#putSequence(String, List)} method, which will
 * interpret this that for each vaule in this sequence a new row is to be created. Moreover,
 * if more than one extractors put sequences into the cache, the builder will generate
 * their cross product automatically. For example, putting a list of available SKU codes
 * into the cache will cause the builder to serve the SKU codes individually to the
 * extract() method. E.g. in
 * <pre>
 * Object extract(ProductBundleBuilder.RowCache cache) throws AbortRowException {
 *     cache.putSequence("SKU_CODE",Arrays.asList(new String[] { "SKU001", "SKU002", ..., "SKU125" }));
 * }
 * 
 * Object extract(ProductBundleBuilder.RowCache cache) throws AbortRowException {
 * 	   return cache.get("SKU_CODE");
 * }
 * </pre>
 * 
 * The extract method will be called 125 times with each sku code (and maybe several times over since
 * another column extractor may have put another sequence into the row cache resulting in a
 * cartesioan product). 
 * @see ColumnExtractorSet
 * 
 * @author istvan
 *
 */
public abstract class ColumnExtractor {
	
	
	// Keys used in the cache.
	public final static String SKU_CODE = "SKU_CODES";
	public final static String AVAILABLE_SKU_CODE = "AVAILABLE_SKU_CODES";
	public final static String CONFIGURATION = "CONFIGURATION";
	public final static String CONFIGURATION_VARIATION = "CONFIGURATION_VARIATION";	
	public final static String RANDOM_CONFIGURATION = "RANDOM_CONFIGURATION";
	public final static String RENDER_PATH_PARAMS = "RENDER_PATH_PARAMS";
	public final static String CHILD = "CHILD";
	
	
	// name of extractor
	private String name;
	
	/** Constructor.
	 * @param name
	 */
	protected ColumnExtractor(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	
	private final static AbortRowException abortRowException = new AbortRowException();
	
	protected void abortRow() throws AbortRowException {
		throw abortRowException;
	}
	
	
	/**
	 * Extract the value from the cache.
	 * @param cache
	 * @return a column value of the product bundle in the current row 
	 * @throws AbortRowException to remove the corresponding row
	 */
	public abstract Object extract(ContentBundle.RowCache cache) throws AbortRowException;
	
	/**
	 * Cache any information needed for the extract method.
	 * @param cache
	 * @throws AbortRowException to remove the corresponding row
	 */
	public void cacheAttribute(ContentBundle.RowCache cache) throws AbortRowException {
	}
   
	
	public static ColumnExtractor Id = new ColumnExtractor("Id") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			return cache.getContentNodeModel().getContentKey().getId();
		}
		
	};
	
	public static ColumnExtractor ParentNodeId = new ColumnExtractor("ParentNodeId") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			ContentNodeModel parent = cache.getContentNodeModel().getParentNode();
			return parent == null ? null : parent.toString();
		}
	};
	
	public static ColumnExtractor ProductLayout = new ColumnExtractor("ProductLayout") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			if (!(cache.getContentNodeModel() instanceof ProductModel)) abortRow();
			return ((ProductModel)cache.getContentNodeModel()).getProductLayout().getLayoutPath();
		}
	};
	
	public static ColumnExtractor ProductLayoutId = new ColumnExtractor("ProductLayoutId") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			if (!(cache.getContentNodeModel() instanceof ProductModel)) abortRow();
			return new Integer(((ProductModel)cache.getContentNodeModel()).getProductLayout().getId());
		}
	};
	
	
	public static ColumnExtractor ContentType = new ColumnExtractor("ContentType") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			return cache.getContentNodeModel().getContentType();
		}
	};
	
	public static ColumnExtractor ContentKey = new ColumnExtractor("ConentKey") {

		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			return cache.getContentNodeModel().getContentKey().getId();
		}
	};
	
	public static ColumnExtractor SkuCodes = new ColumnExtractor("SkuCodes") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			return cache.get(SKU_CODE);
		}
		
		public void cacheAttribute(ContentBundle.RowCache cache) throws AbortRowException {
			if (!(cache.getContentNodeModel() instanceof ProductModel)) abortRow();
			cache.putSequence(SKU_CODE, ((ProductModel)cache.getContentNodeModel()).getSkuCodes());
		}
	};
	
	public static ColumnExtractor Children = new ColumnExtractor("Children") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			return null;
		}
		
		public void cacheAttribute(ContentBundle.RowCache cache) throws AbortRowException {
			ContentNodeModel model = cache.getContentNodeModel();
			cache.putSequence(CHILD,new ArrayList(model.getContentKey().getContentNode().getChildKeys()));
		}
	};

	public static ColumnExtractor AvailableSkuCodes = new ColumnExtractor("AvailableSkuCodes") {

		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			return cache.get(AVAILABLE_SKU_CODE);
		}
		
		public void cacheAttribute(ContentBundle.RowCache cache) throws AbortRowException {
			if (!(cache.getContentNodeModel() instanceof ProductModel)) abortRow();
			List skus = new ArrayList();
			for(Iterator i = ((ProductModel)cache.getContentNodeModel()).getPrimarySkus().iterator(); i.hasNext(); ) {
				SkuModel skuModel = (SkuModel)i.next();
				if (!skuModel.isUnavailable()) skus.add(skuModel.getSkuCode());
				
			}
			cache.putSequence(AVAILABLE_SKU_CODE, skus);
		}
	};
	
	public static ColumnExtractor MinimumQuantity = new ColumnExtractor("MinimumQuantity") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			if (!(cache.getContentNodeModel() instanceof ProductModel)) abortRow();
			return new Float(((ProductModel)cache.getContentNodeModel()).getQuantityMinimum());
		}
	};
	
	public static ColumnExtractor Depth = new ColumnExtractor("Depth") {
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			int d = -1;
			for(ContentNodeModel m = cache.getContentNodeModel(); m != null; m = m.getParentNode(), ++d);
			return new Integer(d);
		}
	};
	
	public static ColumnExtractor ServletPath = new ColumnExtractor("ServletPath") {
		
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			com.freshdirect.cms.ContentType type = cache.getContentNodeModel().getContentKey().getType();
			if (FDContentTypes.PRODUCT.equals(type)) {
				EnumLayoutType layout = ((ProductModel)cache.getContentNodeModel()).getLayout();
				if (EnumLayoutType.GROCERY_DEPARTMENT.equals(layout) ||
					EnumLayoutType.GROCERY_CATEGORY.equals(layout) ||
					EnumLayoutType.GROCERY_PRODUCT.equals(layout)) return "/category.jsp";
				else return "/product.jsp";
			} else if (FDContentTypes.CATEGORY.equals(type)) return "/category.jsp";
			else if (FDContentTypes.DEPARTMENT.equals(type)) return "/department.jsp";
			else if (FDContentTypes.RECIPE.equals(type)) return "/recipe.jsp";
			else throw new AbortRowException();
		}
	};
	
	public static class BrowsePath extends ColumnExtractor {
		
		protected boolean linked = false;
		protected int maxPick = -1;
		protected java.util.Random R = null;
		
		public BrowsePath(boolean linked) {
			this(linked,null,-1);
		}
		
		public BrowsePath(boolean linked, java.util.Random R, int maxPick) {
			super("BrowsePath");
			this.linked = linked;
			this.R = R;
			this.maxPick = maxPick;
		}
	
		public void cacheAttribute(ContentBundle.RowCache cache) throws AbortRowException {
			com.freshdirect.cms.ContentType type = cache.getContentNodeModel().getContentKey().getType();
			
			if (FDContentTypes.PRODUCT.equals(type)) {
				ProductModel productModel = (ProductModel)cache.getContentNodeModel();
				List allVars = getProductConfigurationVariations(productModel, maxPick, R);
				if (allVars.size() == 0) throw new AbortRowException();
				cache.putSequence(RENDER_PATH_PARAMS,allVars);
			} else {
				StringBuffer params = new StringBuffer();
				String id = cache.getContentNodeModel().getContentKey().getId();
				if (FDContentTypes.RECIPE.equals(type)) params.append("recipeId=").append(id);
				else if (FDContentTypes.CATEGORY.equals(type)) params.append("catId=").append(id);
				else if (FDContentTypes.DEPARTMENT.equals(type)) params.append("deptId=").append(id);
				cache.put(RENDER_PATH_PARAMS, params);
			}
		}
		
		public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
			Object params = cache.get(RENDER_PATH_PARAMS);
			StringBuffer path = new StringBuffer(ServletPath.extract(cache).toString());
			
			if (params instanceof StringBuffer) {
				path.append('?').append(params);
			} else if (params instanceof List) {
				
				List skuXSalesUnitXConfigOptions = (List)params;
				
				if (skuXSalesUnitXConfigOptions.size() == 0) throw new AbortRowException();
				
				ProductModel productModel = (ProductModel)cache.getContentNodeModel();
				
				String productId = productModel.getContentKey().getId();
				String catId = productModel.getParentNode().getContentKey().getId();
				path.
					append('?').append("productId=").append(productId).
					append('&').append("catId=").append(catId).
					append('&').append("prodCatId=").append(catId).
					append('&').append("quantity=").append(productModel.getQuantityMinimum()).
					append('&').append("skuCode=").append(skuXSalesUnitXConfigOptions.get(0)).
					append('&').append("salesUnit=").append(((FDSalesUnit)skuXSalesUnitXConfigOptions.get(1)).getName());
				
				List options = (List)skuXSalesUnitXConfigOptions.get(2);
				if (options != null ) {
				
					for(Iterator o = options.iterator(); o.hasNext(); ) {
						String[] nameValue = (String[])o.next();
						path.append('&').append(nameValue[0]).append('=').append(nameValue[1]);
					}
				}
				
			} else if (params == null) {
				path.append("!null");
			} else {
				path.append(':').append(params.getClass().getName());
			}
			
			if (linked) {
				return new StringBuffer("<a href=\"").
				append(StringUtil.escapeHTML(path.toString())).
				append("\">").append(StringUtil.escapeHTML(path.toString())).append("</a>").toString();
			} else return path;
		}
	};
	
	protected static List getProductConfigurationVariations(ProductModel productModel, int maxPick, java.util.Random R) {

		
		ListConcatenation all = new ListConcatenation();
		
		for(Iterator i = productModel.getSkus().iterator(); i.hasNext(); ) {
			SkuModel skuModel = (SkuModel)i.next();
			
			
			FDProduct product;
			
			try {
				product = skuModel.getProduct();
			} catch (FDResourceException e) {
				continue;
			} catch (FDSkuNotFoundException e) {
				continue;
			}
			
			// only available skus
		    if (skuModel.isUnavailable()) continue;
			
			CartesianProduct optionsMatrix = new CartesianProduct();
			
			
			FDVariation[] variations = product.getVariations();
			for(int v = 0; v < variations.length; ++v) {
	            List variationRange = new ArrayList();
			    for(int vo = 0; vo < variations[v].getVariationOptions().length; ++vo) {
			    	FDVariationOption opt = variations[v].getVariationOptions()[vo];
			    	variationRange.add(new String[] {variations[v].getName(), opt.getName()});
			    }
		        optionsMatrix.addSequence(variationRange);
			}
			
			
			CartesianProduct skuXSalesUnitXConfigOptions = new CartesianProduct();
			
			
			skuXSalesUnitXConfigOptions.setSequences(new List[] {
				Arrays.asList(new String[] {skuModel.getSkuCode()}),
				Arrays.asList(product.getSalesUnits()),
				optionsMatrix.size() == 0 ? Arrays.asList(new String[]{null}) : optionsMatrix});
			
			all.addList(skuXSalesUnitXConfigOptions);
		}
		
		if (R == null || all.size() < maxPick) { 
			return all;
		} else { // pick maxPick at random
			
			List configurations = new ArrayList();
			
			UniqueRandomSequence randomSequence = 
				UniqueRandomSequence.getInstance(maxPick,all.size(),R);
			
			for(int r=0; r< maxPick; ++r) {
				configurations.add(all.get(randomSequence.next()));
			}
		
			// [sku,salesUnit,[[option1,name1],[option2,name2], ...]]
			return configurations;
		}
	}

}
