package com.freshdirect.fdstore.content;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class CategoryNodeTree extends ContentNodeTree {


    public final static TreeElementFilter UNNECESSARY_CATEGORY_REMOVER = new UnnecessaryCategoryRemover();
    public final static TreeElementFilter PRODUCT_FILTER = new ProductFilter();
    
    protected static class UnnecessaryCategoryRemover implements TreeElementFilter {
        public boolean accept(TreeElement element) {
            if (element.getChildren().size()==1 && element.getModel() instanceof CategoryModel) {
                
                TreeElement onlyChild = element.getChildren().iterator().next();
                if (onlyChild.getModel() instanceof CategoryModel) {
                    return false;
                }
            }
            return true;
        }
    }
    
    static final class ProductFilter implements TreeElementFilter {

        public boolean accept(TreeElement element) {
            return element.getModel() instanceof ProductModel;
        }

    }
    
    
    public CategoryNodeTree(Comparator rootComparator, Comparator childComparator) {
        super(rootComparator, childComparator);
    }

    
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
     * @param model
     */
    public void addProductModel(ProductModel model) {
        addNode(model);
        CategoryModel primaryHome = model.getPrimaryHome();
        Collection parents = model.getParentKeys();
        if (parents!=null) {
            for (Iterator iter = parents.iterator(); iter.hasNext();) {
                ContentKey parentKey = (ContentKey) iter.next();
                if (!primaryHome.getContentKey().equals(parentKey)) {
                    ProductModel nodeByKey = ContentFactory.getInstance().getProduct(parentKey.getId(), model.getContentKey().getId());
                    if (nodeByKey.isDisplayableBasedOnCms() && nodeByKey.isSearchable()) {
                        //System.out.println("Product "+nodeByKey.getFullName() +" in "+nodeByKey.getParentNode().getFullName()+" is visible");
                        addChildNode(nodeByKey.getParentNode(), model.getContentKey().getId());
                    } else {
                        //System.out.println("NOT DISPLAYABLE: Product "+nodeByKey.getFullName() +" in "+nodeByKey.getParentNode().getFullName());
                    }
                    
                }
            }
        }
    }
    
    
    public static CategoryNodeTree createTree(List products, boolean multipleHome) {
        CategoryNodeTree tree = new CategoryNodeTree(PrioritizedI.PRIORITY_COMPARATOR, ContentNodeModel.FULL_NAME_WITH_ID_COMPARATOR);
        if (multipleHome) {
            for (Iterator it = products.iterator(); it.hasNext();) {
                ProductModel prod = (ProductModel) it.next();
                tree.addProductModel(prod);
            }
        } else {
            for (Iterator it = products.iterator(); it.hasNext();) {
                ProductModel prod = (ProductModel) it.next();
                tree.addNode(prod);
            }
        }
        return tree;
    }
    
}
