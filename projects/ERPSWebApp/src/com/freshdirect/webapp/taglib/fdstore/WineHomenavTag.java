package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.EnumWineFilterDomain;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.content.WineFilterValue;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class WineHomenavTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -5877276624288513988L;

	private static ConcurrentHashMap<String, WineFilterValueListLoader> cache = new ConcurrentHashMap<String, WineFilterValueListLoader>(200);

	private static class WineFilterMenuItem implements Serializable {
		private static final long serialVersionUID = -8415168654537198767L;

		private String encoded;
		private String representation;

		private WineFilterMenuItem(String encoded, String representation) {
			super();
			this.encoded = encoded;
			this.representation = representation;
		}
	}

	private static class WineFilterValueListLoader extends BalkingExpiringReference<List<WineFilterMenuItem>> {
		private static Executor threadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardPolicy());

		private EnumWineFilterDomain domain;
		private PricingContext pricingContext;

		private WineFilterValueListLoader(long refreshPeriod, EnumWineFilterDomain domain, PricingContext pricingContext) {
			super(refreshPeriod, threadPool);
			this.domain = domain;
			this.pricingContext = pricingContext;
			// synchronous load
			try {
				set(load());
			} catch (RuntimeException e) {
			}
		}

		@Override
		protected List<WineFilterMenuItem> load() {
			List<WineFilterMenuItem> items;
			List<WineFilterValue> values = new ArrayList<WineFilterValue>(domain.getFilterValues());
			items = new ArrayList<WineFilterMenuItem>(values.size());
			Iterator<WineFilterValue> it = values.iterator();
			while (it.hasNext()) {
				WineFilterValue value = it.next();
				WineFilter filter = new WineFilter(pricingContext);
				filter.addFilterValue(value);
				try {
					if (filter.getProducts().isEmpty()) {
						it.remove();
					}
				} catch (Exception e) {
					it.remove();
				}
			}
			
			Collections.sort(values, domain.getComparator());
						
			for (WineFilterValue value : values) {
				items.add(new WineFilterMenuItem(value.getEncoded(), value.getFilterRepresentation()));
			}
			return items;
		}
	}

	private long refreshPeriod = 5 * 60 * 1000;

	@Override
	public int doStartTag() throws JspException {
		String catId = request.getParameter("catId");
		String domId = request.getParameter("domId");
		List<WineFilterMenuItem> items = null;
		if (catId != null) {
			CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", catId);
			if (category != null && category.getShowChildren() != null && category.getShowChildren().getId() == 1) {
				List<CategoryModel> subcategories = category.getSubcategories();
				Iterator<CategoryModel> it = subcategories.iterator();
				while (it.hasNext()) {
					CategoryModel subcategory = it.next();
					if (subcategory.isHidden() || !subcategory.getShowSelf())
						it.remove();
				}
				Collections.sort(subcategories, new Comparator<CategoryModel>() {
					public int compare(CategoryModel o1, CategoryModel o2) {
						if (o1.getFullName() == null) {
							if (o2.getFullName() == null)
								return o1.getContentName().compareTo(o2.getContentName());
							else
								return -1;
						} else {
							if (o2.getFullName() == null)
								return 1;
							else
								return o1.getFullName().compareTo(o2.getFullName());
						}
					}
				});
				if (!subcategories.isEmpty()) {
					items = new ArrayList<WineFilterMenuItem>(subcategories.size());
					for (CategoryModel subcategory : subcategories) {
						items.add(new WineFilterMenuItem(subcategory.getContentName(), subcategory.getFullName()));
					}
				}
			}
		} else if (domId != null) {
			FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
			try {
				EnumWineFilterDomain domain = EnumWineFilterDomain.valueOf(domId);
				PricingContext pricingContext = user != null ? user.getPricingContext() : PricingContext.DEFAULT;
				String key = domain.name() + ":" + pricingContext.getZoneId();
				if (!cache.containsKey(key))
					cache.put(key, new WineFilterValueListLoader(refreshPeriod, domain, pricingContext));
				items = cache.get(key).get();
				if (items.isEmpty())
					items = null;
			} catch (NullPointerException e) {
			} catch (IllegalArgumentException e) {
			}

		}
		if (items != null)
			try {
				JspWriter out = pageContext.getOut();
				for (WineFilterMenuItem item : items)
					out.println(StringEscapeUtils.escapeHtml(item.encoded) + "|" + StringEscapeUtils.escapeHtml(item.representation));
			} catch (IOException e) {
			}
		return SKIP_BODY;
	}

	public long getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRefreshPeriod(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}
}
