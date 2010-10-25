package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.EnumWinePrice;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRatingGroup;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.content.util.WineSorter;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class WineTopRatedFeedTag extends AbstractGetterTag<List<ProductModel>> {
	private static final long serialVersionUID = -354126059075589222L;

	private static final String DEFAULT_IMAGE_HEIGHT_ID = "imageHeight";

	private static ConcurrentHashMap<String, TopRatedProductsLoader> cache = new ConcurrentHashMap<String, TopRatedProductsLoader>(200);

	private static class TopRatedProductsLoader extends BalkingExpiringReference<List<ProductModel>> {
		private static Executor threadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardPolicy());

		private EnumWinePrice price;
		private PricingContext pricingContext;

		private TopRatedProductsLoader(long refreshPeriod, EnumWinePrice price, PricingContext pricingContext) {
			super(refreshPeriod, threadPool);
			this.price = price;
			this.pricingContext = pricingContext;
			// synchronous load
			try {
				set(load());
			} catch (RuntimeException e) {
			}
		}

		@Override
		protected List<ProductModel> load() {
			List<ProductModel> prods;
			WineFilter filter = new WineFilter(pricingContext);
			filter.addFilterValue(price);
			WineSorter sorter = new WineSorter(pricingContext, filter);
			List<ProductRatingGroup> group = sorter.getResults();
			// we definitely have only one group because of the DETAILS view
			// type
			if (group.size() > 0)
				prods = group.get(0).getProducts();
			else
				prods = Collections.emptyList();

			return prods;
		}
	}

	private EnumWinePrice price;
	private int maxItems;
	private boolean useAlternateImage;
	private String imageHeightId = DEFAULT_IMAGE_HEIGHT_ID;
	private long refreshPeriod = 5 * 60 * 1000;

	@Override
	protected List<ProductModel> getResult() throws Exception {
		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		String key = price.name() + "-" + user.getPricingContext().getZoneId();

		if (!cache.containsKey(key))
			cache.put(key, new TopRatedProductsLoader(refreshPeriod, price, user.getPricingContext()));
		
		List<ProductModel> prods = new ArrayList<ProductModel>(cache.get(key).get());
		
		Iterator<ProductModel> it = prods.iterator();
		while (it.hasNext())
			if (!it.next().isFullyAvailable())
				it.remove();
		
		if (imageHeightId != null) {
			int imageHeight = 0;
			for (ProductModel prod : prods) {
				Image img = useAlternateImage ? prod.getAlternateImage() : prod.getProdImage();
				if (img == null)
					img = prod.getProdImage();
				if (imageHeight < img.getHeight())
					imageHeight = img.getHeight();
			}
			pageContext.setAttribute(imageHeightId, imageHeight);
		}
		
		if (maxItems > 0 && prods.size() > maxItems)
			prods = prods.subList(0, maxItems);

		return prods;
	}

	public EnumWinePrice getPrice() {
		return price;
	}

	public void setPrice(EnumWinePrice price) {
		this.price = price;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	public boolean isUseAlternateImage() {
		return useAlternateImage;
	}

	public void setUseAlternateImage(boolean useAlternateImage) {
		this.useAlternateImage = useAlternateImage;
	}

	public String getImageHeightId() {
		return imageHeightId;
	}

	public void setImageHeightId(String imageHeightId) {
		this.imageHeightId = imageHeightId;
	}

	public long getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRefreshPeriod(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] { 
				new VariableInfo(data.getAttributeString("id"), List.class.getName() + "<" + ProductModel.class.getName() + ">", true, VariableInfo.NESTED),
				new VariableInfo(NVL.apply(data.getAttributeString("imageHeightId"), DEFAULT_IMAGE_HEIGHT_ID), Integer.class.getName(), true, VariableInfo.NESTED)				
			};

		}
	}
}
