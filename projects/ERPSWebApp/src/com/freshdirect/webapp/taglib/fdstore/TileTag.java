package com.freshdirect.webapp.taglib.fdstore;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Tile;
import com.freshdirect.fdstore.content.TileList;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.content.WineFilterValue;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.ParametersTag;
import com.freshdirect.webapp.taglib.content.WineFilterTag;

public class TileTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 3368706698729964635L;

	private static final String TRKD_ID_DEFAULT = "trkd";

	private String productId;
	private ContentNodeModel contentNode;
	private DepartmentModel department;
	private PricingContext pricingContext;
	private WineFilter wineFilter;
	private String trkdId;
	private Random random;

	public TileTag() {
		random = new Random();
	}

	@Override
	public int doStartTag() throws JspException {
		ParametersTag parent = (ParametersTag) findAncestorWithClass(this, ParametersTag.class);
		if (parent == null)
			throw new JspException("Tile tag must be nested in a Parameters tag");
		contentNode = parent.getContentNode();
		department = parent.getDepartment();
		pricingContext = parent.getPricingContext();
		WineFilterTag filterTag = (WineFilterTag) findAncestorWithClass(this, WineFilterTag.class);
		if (filterTag != null) {
			wineFilter = (WineFilter) pageContext.getAttribute(filterTag.getFilterId());
			if (wineFilter != null && wineFilter.isFiltering() && department == null)
				department = (DepartmentModel) ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, "usq");
		}

		if ((contentNode != null || wineFilter != null ) && department != null) {
			List<TileList> tileLists = department.getTileList();
			if (tileLists.size() > 0) {
				for (TileList tileList : tileLists) {
					if (matchList(tileList) && displayList(tileList))
						return EVAL_BODY_INCLUDE;
				}
			}
		}
		pageContext.setAttribute("id", null);
		pageContext.setAttribute(productId, null);
		pageContext.setAttribute(trkdId, null);
		return SKIP_BODY;
	}

	private boolean matchList(TileList tileList) {
		List<ContentKey> filterKeys = tileList.getFilterKeys();
		if (filterKeys.size() == 0)
			return false;

		Set<DomainValue> wineDomainValues = new HashSet<DomainValue>();
		for (ContentKey key : filterKeys)
			if (FDContentTypes.DOMAINVALUE.equals(key.getType())) {
				wineDomainValues.add((DomainValue) ContentFactory.getInstance().getContentNodeByKey(key));
			} else if (contentNode != null && key.equals(contentNode.getContentKey()))
				return true;

		if (wineDomainValues.size() > 0) {
			BLOCK: {
				if (wineFilter != null && wineFilter.isFiltering()) {
					WineFilter filter = new WineFilter(pricingContext);
					for (DomainValue dv : wineDomainValues)
						try {
							filter.addFilterValue(dv);
						} catch (IllegalArgumentException e) {
							// we ignore adding illegal domain values
						}
					if (filter.isFiltering()) {
						for (WineFilterValue fv : filter.getFilterValues())
							if (!wineFilter.hasFilterValue(fv))
								break BLOCK;
						return true;
					}
					break BLOCK;
				}
			}
			if (contentNode != null && FDContentTypes.PRODUCT.equals(contentNode.getContentKey().getType())) {
				WineFilter filter = new WineFilter(pricingContext);
				for (DomainValue dv : wineDomainValues)
					try {
						filter.addFilterValue(dv);
					} catch (IllegalArgumentException e) {
						// we ignore adding illegal domain values
					}
				if (filter.getProducts().contains(contentNode))
					return true;
			}
		}

		return false;
	}

	private boolean displayList(TileList tileList) {
		Set<Tile> tiles = new LinkedHashSet<Tile>(tileList.getTiles());
		while (!tiles.isEmpty()) {
			int index = random.nextInt(tiles.size());
			Iterator<Tile> it = tiles.iterator();
			int i = 0;
			while (i < (index + 1) && it.hasNext()) {
				Tile tile = it.next();
				if (i == index) {
					if (tile.isGoesGreatWith()) {
						ProductModel product = getQuickbuyProduct(tile);
						if (product != null) {
							pageContext.setAttribute(id, tile);
							pageContext.setAttribute(trkdId, tileList.getContentName());
							pageContext.setAttribute(productId, product);
							return true;
						} else {
							it.remove();
							break;
						}
					} else {
						ProductModel product = getQuickbuyProduct(tile);
						pageContext.setAttribute(id, tile);
						pageContext.setAttribute(trkdId, tileList.getContentName());
						if (product != null)
							pageContext.setAttribute(productId, product);
						return true;
					}
				}
				i++;
			}
		}
		return false;
	}

	public ProductModel getQuickbuyProduct(Tile tile) {
		List<ContentNodeModel> items = tile.getQuickbuyItems();

		Set<ProductModel> products = new LinkedHashSet<ProductModel>();
		for (ContentNodeModel item : items) {
			if (FDContentTypes.CATEGORY.equals(item.getContentKey().getType())) {
				CategoryModel node = (CategoryModel) item;
				products.addAll(node.getAllChildProducts());
			} else if (FDContentTypes.PRODUCT.equals(item.getContentKey().getType())) {
				products.add((ProductModel) item);
			} else if (FDContentTypes.CONFIGURED_PRODUCT.equals(item.getContentKey().getType())) {
				products.add((ConfiguredProduct) item);
			} else if (FDContentTypes.CONFIGURED_PRODUCT_GROUP.equals(item.getContentKey().getType())) {
				ConfiguredProductGroup node = (ConfiguredProductGroup) item;
				products.add(node.getProduct());
			}
		}
		while (!products.isEmpty()) {
			int index = random.nextInt(products.size());
			Iterator<ProductModel> it = products.iterator();
			int i = 0;
			while (i < (index + 1) && it.hasNext()) {
				ProductModel p = it.next();
				if (i == index) {
					if (p.isFullyAvailable())
						return p;
					else {
						it.remove();
						break;
					}
				}
				i++;
			}
		}
		return null;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setContentNode(ContentNodeModel contentNode) {
		this.contentNode = contentNode;
	}

	public ContentNodeModel getContentNode() {
		return contentNode;
	}

	public DepartmentModel getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentModel department) {
		this.department = department;
	}

	public String getTrkdId() {
		return trkdId;
	}

	public void setTrkdId(String trkdId) {
		this.trkdId = trkdId;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
					declareVariableInfo(data.getAttributeString("id"), Tile.class, VariableInfo.AT_BEGIN),
					declareVariableInfo(data.getAttributeString("productId"), ProductModel.class, VariableInfo.NESTED),
					declareVariableInfo(NVL.apply(data.getAttributeString("trkdId"), TRKD_ID_DEFAULT), String.class, VariableInfo.NESTED)
				};
		}
	}
}
