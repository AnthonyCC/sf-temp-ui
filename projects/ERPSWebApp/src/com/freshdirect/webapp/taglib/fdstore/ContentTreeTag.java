package com.freshdirect.webapp.taglib.fdstore;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeTree;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ContentNodeTree.NodeIterator;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class ContentTreeTag extends BodyTagSupport {

    private final class CategoryTreeFilter implements ContentNodeTree.TreeElementFilter {
        String catId;
        Set childSelected = new HashSet();

        CategoryTreeFilter(String catId) {
            this.catId = catId;
        }
        public boolean accept(TreeElement element) {
            ContentNodeModel model = element.getModel();
            if (model instanceof ProductModel) {
                return true;
            }
            if (selectedCategories.contains(model.getContentKey().getId())) {
                return true;
            }
            return acceptModel(model);
        }

        boolean acceptModel(ContentNodeModel model) {
            if (catId.equals(model.getContentKey().getId())) {
                return true;
            }
            if (childSelected.contains(model.getContentKey().getId())) {
                return true;
            }
            ContentNodeModel parent = model.getParentNode();
            if (parent != null) {
                boolean result = acceptModel(parent);
                if (result) {
                    childSelected.add(model.getContentKey().getId());
                }
                return result;
            }
            return false;
        }
    }

    private String tree;
    private String contentNodeName;
    NodeIterator       iterator;
    private String depthName;
    private String childCountName;
    private String selectedName;
    private Set    selectedCategories;
    private int    expandToDepth = 0;
    private String nextDepthName;
    private boolean includeProducts = false;
    

    public void setTree(String tree) {
        this.tree = tree;
    }

    public void setContentNodeName(String name) {
        this.contentNodeName = name;
    }

    public void setDepthName(String name) {
        this.depthName = name;
    }

    public void setChildCountName(String childCountName) {
        this.childCountName = childCountName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }
    
    public void setExpandToDepth(int expandToDepth) {
        this.expandToDepth = expandToDepth;
    }
    
    public void setNextDepthName(String name) {
        this.nextDepthName = name;
    }
    
    public void setIncludeProducts(boolean flag) {
        this.includeProducts = flag;
    }
    
    public int doStartTag() throws JspException {
        ContentNodeTree contentTree = (ContentNodeTree) pageContext.getAttribute(tree);
        
        // calculate the set of the selected categories, to the root
        selectedCategories = new HashSet();
        String categoryId = pageContext.getRequest().getParameter("catId");
        if (categoryId!=null) {
            TreeElement rootElement = contentTree.getTreeElement(categoryId);
            TreeElement treeElement = rootElement;
            while(treeElement!=null) {
                selectedCategories.add(categoryId);
                ContentNodeModel contentNodeModel = contentTree.getParent(treeElement.getModel());
                if (contentNodeModel!=null) {
                    categoryId = contentNodeModel.getContentKey().getId();
                    treeElement = contentTree.getTreeElement(categoryId);
                } else {
                    treeElement = null;
                }
            }
            treeElement = rootElement;
            while (!CategoryNodeTree.UNNECESSARY_CATEGORY_REMOVER.accept(treeElement)) {
                TreeElement child = (TreeElement) treeElement.getChildren().iterator().next();
                selectedCategories.add(child.getModel().getContentKey().getId());
                treeElement = child;
            }
        }
        contentTree.setExpandNodesToDepth(expandToDepth);
        contentTree.setExpandedElementIds(selectedCategories);
        ContentNodeTree.TreeElementFilter filter = null;
        if (includeProducts) {
            categoryId = pageContext.getRequest().getParameter("catId");
            /*if (categoryId!=null) {
                filter = new CategoryTreeFilter(categoryId);
            }*/
        } else {
            filter =  new ContentNodeTree.TreeElementFilter() {
                public boolean accept(TreeElement element) {
                    return element.getModel() instanceof CategoryModel || element.getModel() instanceof DepartmentModel;
                }
            };
        }
        
        iterator = contentTree.iterator(filter);
        
        if (iterator.hasNext()) {
            setupValues();
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    public int doAfterBody() throws JspException {
        if (iterator.hasNext()) {
            setupValues();
            return EVAL_BODY_AGAIN;
        }
        return SKIP_BODY;
    }

    private void setupValues() {
        ContentNodeTree.TreeElement element = (TreeElement) iterator.next();
        if (contentNodeName != null) {
            pageContext.setAttribute(contentNodeName, element.getModel());
        }
        if (depthName != null) {
            pageContext.setAttribute(depthName, new Integer(element.getDepth()));
        }
        if (childCountName != null) {
            pageContext.setAttribute(childCountName, new Integer(element.getChildCount()));
        }
        if (selectedName != null) {
            pageContext.setAttribute(selectedName, Boolean.valueOf(selectedCategories.contains(element.getModel().getContentKey().getId())));
        }
        if (nextDepthName!=null) {
            Integer nextDepth ;
            if (iterator.hasNext()) {
                ContentNodeTree.TreeElement nextElement =((TreeElement) iterator.peekNext());
                nextDepth = new Integer(nextElement.getDepth());
            } else {
                nextDepth = new Integer(0);
            }
            pageContext.setAttribute(nextDepthName, nextDepth);
        }
    }
}
