package com.freshdirect.storeapi.content.test;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.EnumLayoutType;
import com.freshdirect.storeapi.content.EnumProductLayout;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;


/**
 * Row filters for {@link ContentBundle}s.
 *
 * The evaluate method's argument will always be the current row's productModel.
 * @author istvan
 *
 */
@CmsLegacy
public abstract class RowFilter implements Predicate {


	@Override
    public boolean evaluate(Object arg) {
		if (!(arg instanceof ContentNodeModel)) return false;
		return evaluateFilter((ContentNodeModel)arg);
	}

	public abstract boolean evaluateFilter(ContentNodeModel model);

	/**
	 * Filter by Product availability.
	 *
	 */
	public static class ProductAvailable extends RowFilter {

		private int days;

		public ProductAvailable(int days) {
			this.days = days;
		}

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isAvailableWithin(days);
		}
	}

	/**
	 * Filter by Product availability.
	 *
	 */
	public static RowFilter RecipeAvailable = new RowFilter() {

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof Recipe)) return false;
			return ((Recipe)model).isAvailable();
		}
	};

	/**
	 *
	 * Filter by layout.
	 *
	 */
	public static class ProductLayout extends RowFilter {
		private EnumProductLayout layout;

		public ProductLayout(EnumProductLayout layout) {
			this.layout = layout;
		}

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			EnumProductLayout prodLayout = ((ProductModel)model).getProductLayout();
			if (prodLayout == null) return false;
			return layout.equals(prodLayout);
		}
	}

	/**
	 *
	 * Filter by auto configuration.
	 *
	 */
	public static RowFilter AutoConfigurable = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isAutoconfigurable();
		}
	};


	/** Filter by content type.
	 *
	 *
	 */
	public static class Type extends RowFilter {
		private ContentType type;


		public Type(ContentType type) {
			this.type = type;
		}
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			return type.equals(model.getContentType());
		}
	}

	/**
	 * Filter by Sku availability
	 */
	public static RowFilter Unavailable = new RowFilter() {

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (! (model instanceof ProductModel)) return false;
			return ((ProductModel)model).isUnavailable();
		}
	};


	public static class Random extends RowFilter {

		private java.util.Random R = new java.util.Random();

		private int sixtyfourth;

		public Random(int sixtyfourth, long seed) {
			this.sixtyfourth = sixtyfourth;
			R = new java.util.Random(seed);
		}

		public Random(int sixtyfourth) {
			this(sixtyfourth,System.currentTimeMillis());
		}

		@Override
        public boolean evaluateFilter(ContentNodeModel ignored) {
			return R.nextInt(64) < sixtyfourth;
		}

	};

	public static RowFilter HasAvailableYmalProducts = new RowFilter() {

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (! (model instanceof ProductModel)) return false;
			List relatedProducts = ((ProductModel)model).getYmalProducts();
			for(Iterator i = relatedProducts.iterator(); i.hasNext(); ) {
				ProductModel relProduct = (ProductModel)i.next();
				if (!relProduct.isUnavailable()) return true;
			}
			return false;
		}
	};

	public static RowFilter HasAvailableYmalRecipes = new RowFilter() {

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			List relatedRecepies = ((ProductModel)model).getRelatedRecipes();
			for(Iterator i = relatedRecepies.iterator(); i.hasNext(); ) {
				Recipe recipe = (Recipe)i.next();
				if (recipe.isAvailable()) return true;
			}
			return false;
		}
	};

	public static RowFilter HasYmalCategories = new RowFilter() {

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).getYmalCategories().size() > 0;
		}
	};

	public static RowFilter Grocery = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isGrocery();
		}
	};

	public static RowFilter Frozen = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isFrozen();
		}
	};

	public static RowFilter Perishable = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isPerishable();
		}
	};

	public static RowFilter Hidden = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			return model.isHidden();
		}
	};

	public static class LayoutType extends RowFilter {

		private EnumLayoutType type;

		public LayoutType(EnumLayoutType type) {
			this.type = type;
		}

		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return type.equals(((ProductModel)model).getLayout());
		}
	}

	public static RowFilter Featured = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof CategoryModel)) return false;
			return ((CategoryModel)model).isFeatured();
		}
	};

	public static RowFilter SecondaryCategory = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof CategoryModel)) return false;
			return ((CategoryModel)model).isSecondaryCategory();
		}
	};

	public static RowFilter Searchable = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			return model.isSearchable();
		}
	};

	public static RowFilter Orphan = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			return model.isOrphan();
		}
	};

	public static RowFilter HasFeaturedProducts = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof DepartmentModel)) return false;
			return ((DepartmentModel)model).getFeaturedProducts().size() > 0;
		}
	};

	public static RowFilter Active = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof Recipe)) return false;
			return ((Recipe)model).isActive();
		}
	};

	public static RowFilter HasTerms = new RowFilter() {
		@Override
        public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).hasTerms();
		}
	};

}
