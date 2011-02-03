package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.freshdirect.fdstore.DealsHelper;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetDealsSKUTag extends AbstractGetterTag<List<SkuModel>> {

	private static final long					serialVersionUID		= -2573584105288604782L;

	public static final int						COMPARE_NAME			= 0;
	private final static Comparator<SkuModel>	PRODUCT_NAME_COMPARATOR	= new ProductSorter( COMPARE_NAME );

	protected List<SkuModel> getResult() throws FDResourceException {

		List<SkuModel> products = null;
		String upperLimit = DealsHelper.getDealsUpperLimit() + ".99";

		Collection<?> _products = FDCachedFactory.findSKUsByDeal( DealsHelper.getDealsLowerLimit(), Double.parseDouble( upperLimit ), getSKUPrefixes() );
		if ( _products != null && _products.size() != 0 ) {
			products = new ArrayList<SkuModel>( _products.size() );
			FDProductInfo productInfo = null;
			String sku = "";
			for ( Iterator<?> i = _products.iterator(); i.hasNext(); ) {
				sku = i.next().toString();
				try {
					productInfo = FDCachedFactory.getProductInfo( sku );
					if ( productInfo.isAvailable() && productInfo.getZonePriceInfo( getPricingZoneId() ).isItemOnSale() ) {

						try {
							SkuModel sm = ContentFactory.getInstance().getProduct( sku ).getSku( sku );
							if ( !sm.isUnavailable() )
								products.add( sm );
						} catch ( Exception e ) {
						}
					}
				} catch ( FDSkuNotFoundException e ) {
					throw new FDResourceException( e );
				}
			}
		}

		if ( products != null )
			Collections.sort( products, PRODUCT_NAME_COMPARATOR );

		return products;
	}

	private List<String> getSKUPrefixes() {
		List<String> skuPrefixes = new ArrayList<String>( 5 );
		String _skuPrefixes = DealsHelper.getDealsSkuPrefixes();
		if ( _skuPrefixes != null && !"".equals( _skuPrefixes ) && !DealsHelper.ALL_SKUS.equals( _skuPrefixes ) ) {
			StringTokenizer st = new StringTokenizer( _skuPrefixes, DealsHelper.SKU_PREFIX_SEPARATOR );
			while ( st.hasMoreElements() ) {
				skuPrefixes.add( st.nextToken() + "%" );
			}
		}
		return skuPrefixes;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.List<SkuModel>";
		}

	}

	private static class ProductSorter implements Comparator<SkuModel> {

		private int	compareBy;

		public ProductSorter( int compareBy ) {
			this.compareBy = compareBy;
		}

		public int compare( SkuModel n1, SkuModel n2 ) {
			switch ( compareBy ) {
				case COMPARE_NAME:
					int comp = n1.getProductModel().getFullName().compareTo( n2.getProductModel().getFullName() );
					return comp;
				default:
					return 0;
			}
		}
	}

	private String getPricingZoneId() {
		FDUserI user = FDSessionUser.getFDSessionUser( pageContext.getSession() );
		if ( user == null ) {
			throw new FDRuntimeException( "User object is Null" );
		}
		return user.getPricingZoneId();
	}
}
