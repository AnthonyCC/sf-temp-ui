package com.freshdirect.fdstore.content.test;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;

import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.cms.ContentType;

import org.apache.commons.collections.Predicate;

import java.util.List;
import java.util.Iterator;


/**
 * Row filters for {@link ContentBundle}s.
 * 
 * The evaluate method's argument will always be the current row's productModel.
 * @author istvan
 *
 */
public abstract class RowFilter implements Predicate {
	

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
		public boolean evaluateFilter(ContentNodeModel model) {
			return type.equals(model.getContentType());
		}
	}
	
	/**
	 * Filter by Sku availability
	 */
	public static RowFilter Unavailable = new RowFilter() {
		
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
		
		public boolean evaluateFilter(ContentNodeModel ignored) {
			return R.nextInt(64) < sixtyfourth;
		}
		
	};
	
	public static RowFilter HasAvailableYmalProducts = new RowFilter() {
		
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
		
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).getYmalCategories().size() > 0;
		}
	};
	
	public static RowFilter Grocery = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isGrocery();
		}
	};
	
	public static RowFilter Frozen = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isFrozen();
		}
	};
	
	public static RowFilter Perishable = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).isPerishable();
		}
	};
	
	public static RowFilter Hidden = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			return model.isHidden();
		}
	};
	
	public static class LayoutType extends RowFilter {
		
		private EnumLayoutType type;
		
		public LayoutType(EnumLayoutType type) {
			this.type = type;
		}
		
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return type.equals(((ProductModel)model).getLayout());
		}
	}
	
	public static RowFilter Featured = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof CategoryModel)) return false;
			return ((CategoryModel)model).isFeatured();
		}
	};
	
	public static RowFilter SecondaryCategory = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof CategoryModel)) return false;
			return ((CategoryModel)model).isSecondaryCategory();
		}
	};
	
	public static RowFilter Searchable = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			return model.isSearchable();
		}
	};
	
	public static RowFilter Orphan = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			return model.isOrphan();
		}
	};
	
	public static RowFilter HasFeaturedProducts = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof DepartmentModel)) return false;
			return ((DepartmentModel)model).getFeaturedProducts().size() > 0;
		}
	};
	
	public static RowFilter Active = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof Recipe)) return false;
			return ((Recipe)model).isActive();
		}
	};
	
	public static RowFilter HasTerms = new RowFilter() {
		public boolean evaluateFilter(ContentNodeModel model) {
			if (!(model instanceof ProductModel)) return false;
			return ((ProductModel)model).hasTerms();
		}
	};
	
}
