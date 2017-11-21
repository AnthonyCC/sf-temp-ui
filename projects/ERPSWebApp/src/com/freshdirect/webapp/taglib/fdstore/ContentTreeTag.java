package com.freshdirect.webapp.taglib.fdstore;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;

import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.CategoryNodeTree;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ContentNodeTree;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.ContentNodeTree.NodeIterator;
import com.freshdirect.storeapi.content.ContentNodeTree.TreeElement;

public class ContentTreeTag extends BodyTagSupport {
	private static final long serialVersionUID = 3129393723729467345L;

	private String contentNodeName;
	private boolean useFiltered;
	private NodeIterator iterator;
	private String depthName;
	private String childCountName;
	private String selectedName;
	private Set<String> selectedCategories;
	private int expandToDepth = 0;
	private String nextDepthName;
	private boolean includeProducts = false;
	private boolean sortDeptsByProductCount = false;

	public void setContentNodeName(String name) {
		this.contentNodeName = name;
	}

	public boolean isUseFiltered() {
		return useFiltered;
	}

	public void setUseFiltered(boolean useFiltered) {
		this.useFiltered = useFiltered;
	}
	
	public void setSortDeptsByProductCount(boolean sortDeptsByProductCount) {
		this.sortDeptsByProductCount = sortDeptsByProductCount;
	}
	
	public boolean isSortDeptsByProductCount() {
		return sortDeptsByProductCount;
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
		CategoryTreeContainer container = (CategoryTreeContainer) findAncestorWithClass(this, CategoryTreeContainer.class);
		if (container == null)
			throw new JspException("ContentTree tag must be nested in a CategoryTreeContainer (e.g. SmartSearchTag)");
		CategoryNodeTree contentTree = useFiltered ? container.getFilteredCategoryTree() : container.getCategoryTree();

		// calculate the set of the selected categories, to the root
		selectedCategories = new HashSet<String>();
		String categoryId = pageContext.getRequest().getParameter("catId");
		if (categoryId != null) {
			TreeElement rootElement = contentTree.getTreeElement(categoryId);
			TreeElement treeElement = rootElement;
			while (treeElement != null) {
				selectedCategories.add(categoryId);
				ContentNodeModel contentNodeModel = contentTree.getParent(treeElement.getModel());
				if (contentNodeModel != null) {
					categoryId = contentNodeModel.getContentKey().getId();
					treeElement = contentTree.getTreeElement(categoryId);
				} else {
					treeElement = null;
				}
			}
			treeElement = rootElement;
			if (treeElement != null) {
				while (!CategoryNodeTree.UNNECESSARY_CATEGORY_REMOVER.accept(treeElement)) {
					TreeElement child = treeElement.getChildren().iterator().next();
					selectedCategories.add(child.getModel().getContentKey().getId());
					treeElement = child;
				}
			}
		}
		contentTree.setExpandNodesToDepth(expandToDepth);
		contentTree.setExpandedElementIds(selectedCategories);
		ContentNodeTree.TreeElementFilter filter = null;
		if (includeProducts) {
			categoryId = pageContext.getRequest().getParameter("catId");
			/*
			 * if (categoryId!=null) { filter = new CategoryTreeFilter(categoryId); }
			 */
		} else {
			filter = new ContentNodeTree.TreeElementFilter() {
				public boolean accept(TreeElement element) {
					return element.getModel() instanceof CategoryModel || element.getModel() instanceof DepartmentModel;
				}
			};
		}

		iterator = contentTree.iterator(filter, sortDeptsByProductCount);

		if (iterator.hasNext()) {
			setupValues();
			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public int doAfterBody() throws JspException {
		if (iterator.hasNext()) {
			setupValues();
			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	private void setupValues() {
		ContentNodeTree.TreeElement element = iterator.next();
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
			pageContext.setAttribute(selectedName, Boolean.valueOf(selectedCategories.contains(element.getModel().getContentKey()
					.getId())));
		}
		if (nextDepthName != null) {
			Integer nextDepth;
			if (iterator.hasNext()) {
				ContentNodeTree.TreeElement nextElement = (iterator.peekNext());
				nextDepth = new Integer(nextElement.getDepth());
			} else {
				nextDepth = new Integer(0);
			}
			pageContext.setAttribute(nextDepthName, nextDepth);
		}
	}
}
