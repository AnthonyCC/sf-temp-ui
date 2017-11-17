package com.freshdirect.storeapi.content;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;

public class CategoryNodeTree extends ContentNodeTree {
	public final static TreeElementFilter UNNECESSARY_CATEGORY_REMOVER = new UnnecessaryCategoryRemover();

	protected static class UnnecessaryCategoryRemover implements TreeElementFilter {
		@Override
        public boolean accept(TreeElement element) {
			if (element.getChildren().size() == 1 && element.getModel() instanceof CategoryModel) {

				TreeElement onlyChild = element.getChildren().iterator().next();
				if (onlyChild.getModel() instanceof CategoryModel) {
					return false;
				}
			}
			return true;
		}
	}

	public CategoryNodeTree(Comparator<ContentNodeModel> rootComparator, Comparator<ContentNodeModel> childComparator) {
		super(rootComparator, childComparator);
	}

	@Override
    public ContentNodeModel getParent(ContentNodeModel model) {
		if (model instanceof CategoryModel) {
			ContentNodeModel parentNode = model.getParentNode();
			if ((parentNode instanceof CategoryModel) || (parentNode instanceof DepartmentModel)) {
				return parentNode;
			}
		}
		if (model instanceof ProductModel) {
			ProductModel p = (ProductModel) model;
			return p.getParentNode();
		}
		return null;
	}

	/**
	 * Add product model to the tree, to multiple places.
	 *
	 * @param model
	 */
	public void addProductModel(ProductModel model) {
		TreeElement primary = addNode(model);
		if (model.isDisplayableBasedOnCms() && model.isSearchable()) {
			incrementParents(getParentElement(primary), model.getContentKey());
		}
		CategoryModel primaryHome = model.getPrimaryHome();
		Collection<ContentKey> parents = ContentFactory.getInstance().getParentKeys(model.getContentKey());
		if (parents != null) {
			for (ContentKey parentKey : parents) {
				if (!primaryHome.getContentKey().equals(parentKey)) {
					ProductModel nodeByKey = ContentFactory.getInstance().getProductByName(parentKey.id,
							model.getContentKey().id);
					if (nodeByKey.isDisplayableBasedOnCms() && nodeByKey.isSearchable()) {
						addChildNode(nodeByKey.getParentNode(), model.getContentKey().id);
					}
				}
			}
		}
	}

	/**
	 * Count the number of top-level categories.
	 *
	 * @return
	 */
	public int getCategoryCount() {
		int categoryCount = 0;
		for (TreeElement e : roots) {
			categoryCount += e.getChildren().size();
		}
		return categoryCount;
	}

	public static CategoryNodeTree createTree(List<ProductModel> products, boolean multipleHome) {
		CategoryNodeTree tree = new CategoryNodeTree(ContentNodeModel.PRIORITY_COMPARATOR,
				ContentNodeModel.FULL_NAME_WITH_ID_COMPARATOR);
		if (multipleHome)
			for (ProductModel prod : products)
				tree.addProductModel(prod);
		else
			for (ProductModel prod : products)
				tree.addNode(prod);
		return tree;
	}

	public static CategoryNodeTree createTree(SearchResults results, boolean multipleHome) {
		CategoryNodeTree tree = new CategoryNodeTree(ContentNodeModel.PRIORITY_COMPARATOR,
				ContentNodeModel.FULL_NAME_WITH_ID_COMPARATOR);
		if (multipleHome)
			for (FilteringSortingItem<ProductModel> item : results.getProducts())
				tree.addProductModel(item.getModel());
		else
			for (FilteringSortingItem<ProductModel> item : results.getProducts())
				tree.addNode(item.getModel());
		return tree;
	}
}
