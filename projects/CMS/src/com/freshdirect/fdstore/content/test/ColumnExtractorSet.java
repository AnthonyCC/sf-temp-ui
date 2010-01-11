package com.freshdirect.fdstore.content.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.CartesianProduct;
import com.freshdirect.framework.util.ListConcatenation;
import com.freshdirect.framework.util.UniqueRandomSequence;


/**
 * Set of related column extractors.
 * 
 * For the case where a set of columns constitute a cross product (or set of cross products),
 * isntances of the ColumnExtractor set can group the columns. Likely they can also share
 * the {@link ColumnExtractor#cacheAttribute} method.
 * 
 * @see ColumnExtractor
 * @author istvan
 *
 */
public interface ColumnExtractorSet {

	public final static String SKU_CODE = "SKU_CODE";
	public final static String SALES_UNIT = "SALES_UNIT";
	public final static String OPTIONS = "OPTIONS";
	
	/**
	 * Get extractor by name.
	 * @param name extractor's name
	 * @return
	 */
	public ColumnExtractor getExtractor(String name);
	
	/**
	 * Calculates the cartesian product of available skus, sales unit and config options.
	 * The config options are themselves a cartesian product of the option values and their
	 * range.
	 * 
	 * The columns can be obtained by the keys: {@link ColumnExtractorSet#SKU_CODE},
	 * {@link ColumnExtractorSet#SALES_UNIT} and {@link ColumnExtractorSet#OPTIONS}.
	 * 
	 * 
	 * @author istvan
	 *
	 */
	public static class ConfigurationVariations implements ColumnExtractorSet {
		
		private Random R;
		private int maxPick;
		
		/**
		 * Constructor.
		 * @param R random number generator
		 * @param maxPick maximum number of elements to pick randomly from the product
		 */
		public ConfigurationVariations(Random R, int maxPick) {
			this.R = R;
			this.maxPick = maxPick;
		}
		
		/**
		 * Constructor. 
		 * @param maxPick max number of elements to pick randomly from the product
		 */
		public ConfigurationVariations(int maxPick) {
			this(new Random(System.currentTimeMillis()),maxPick);
		}
		
		/**
		 * Constructor.
		 * This will cause the entire cross product put into the cache.
		 */
		public ConfigurationVariations() {
			this(null,-1);
		}
		
		protected List getConfigurationVariations(ProductModel productModel) {

			
			ListConcatenation all = new ListConcatenation();
			
			for(Iterator i = productModel.getPrimarySkus().iterator(); i.hasNext(); ) {
				SkuModel skuModel = (SkuModel)i.next();
				
				// only available skus
				if (skuModel.isUnavailable()) continue;
				
				FDProduct product;
				
				try {
					product = skuModel.getProduct();
				} catch (FDResourceException e) {
					continue;
				} catch (FDSkuNotFoundException e) {
					continue;
				}
				
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
		
		protected abstract class CacheVariations extends ColumnExtractor {

			protected CacheVariations(String name) {
				super(name);
			}
			
			public void cacheAttribute(ContentBundle.RowCache cache) throws AbortRowException {
				List configurations = cache.getSequence(CONFIGURATION_VARIATION);
				if (configurations != null) return;
				else {
					ContentNodeModel model = cache.getContentNodeModel();
					if (!(model instanceof ProductModel)) throw new AbortRowException();
					//configurations = getConfigurationVariations((ProductModel)model);
					configurations = getProductConfigurationVariations((ProductModel)model, maxPick, R);
					if (configurations.size() == 0) throw new AbortRowException();
					else cache.putSequence(CONFIGURATION_VARIATION,configurations);
				}
			}
			
			protected Object getTupleValue(ContentBundle.RowCache cache, int i) {
				List tuple = (List)cache.get(CONFIGURATION_VARIATION);
				if (tuple == null) return null;
				return tuple.get(i);
			}
		}
		
		private ColumnExtractor skuExtractor = new CacheVariations("SkuCode") {
			
			public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
				return getTupleValue(cache,0);
			}
		};
		
		private ColumnExtractor salesUnitExtractor = new CacheVariations("SalesUnit") {
			public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
				return ((FDSalesUnit)getTupleValue(cache,1)).getName();
			}
		};
		
		private ColumnExtractor configurationOptionsQueryStringExtractor = new CacheVariations("Options") {
			public Object extract(ContentBundle.RowCache cache) throws AbortRowException {
				List options = (List)getTupleValue(cache,2);
				if (options == null) return null;
				StringBuffer queryString = new StringBuffer();
				for(Iterator o = options.iterator(); o.hasNext(); ) {
					String[] nameValue = (String[])o.next();
					queryString.append(nameValue[0]).append('=').append(nameValue[1]);
					if (o.hasNext()) queryString.append('&');
				}
				return queryString.toString();
			}
		};
		
		public ColumnExtractor getExtractor(String name) {
			if (SKU_CODE.equals(name)) return skuExtractor;
			else if (SALES_UNIT.equals(name)) return salesUnitExtractor;
			else if (OPTIONS.equals(name)) return configurationOptionsQueryStringExtractor;
			else throw new NoSuchElementException();
		}
	}
	
}
