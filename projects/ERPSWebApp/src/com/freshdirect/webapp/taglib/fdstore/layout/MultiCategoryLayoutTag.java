package com.freshdirect.webapp.taglib.fdstore.layout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModel;

public class MultiCategoryLayoutTag extends BodyTagSupport {
	private static final long serialVersionUID = -32807021341062812L;




	// Attribute names
	

	/**
	 * Is first section
	 */
	public static final String ATTR_IS_FIRST = "mcl_first";

	/**
	 * Nodes list contains product
	 */
	public static final String ATTR_CONTAINS_PRODUCT = "mcl_containsProduct";
	
	/**
	 * List of nodes
	 */
	public static final String ATTR_NODES = "mcl_nodes";

	/**
	 * Container category
	 */
	public static final String ATTR_CATEGORY = "mcl_category";


	private static final Logger LOGGER = Logger.getLogger(MultiCategoryLayoutTag.class);
	

	ContentKey parentKey;
	List<ContentNodeModel> sortedCollection;
	

	LinkedList<Pair> stack;
	Pair currentSection = null;
	

	public void setParentKey(ContentKey parentKey) {
		this.parentKey = parentKey;
	}
	
	/**
	 * List of content nodes in the following sequence
	 * 
	 * (Cat_1?,) Prd_1_1, SubCat_1_2, Prd_1_3, ..., (Cat_2, Prd_2_1, Prd_2_2, SubCat_2_3, ... )
	 * 
	 * The first group of products can be headless (ie. no leading "top" category)
	 * 
	 * @param sortedCollection
	 */
	public void setSortedCollection(List<ContentNodeModel> sortedCollection) {
		this.sortedCollection = sortedCollection;
	}
	
	@Override
	public int doStartTag() throws JspException {
		// INITIALIZE section
		stack = new LinkedList<Pair>( compute() );


		currentSection = stack.poll();
		if (currentSection == null) {
			return SKIP_BODY;
		}
		setContext(currentSection, true);
		
		return EVAL_BODY_INCLUDE;
	}
	
	/**
	 * Iterate 
	 */
	@Override
	public int doAfterBody() throws JspException {
		currentSection = stack.poll();
		if (currentSection == null) {
			return SKIP_BODY;
		}
		setContext(currentSection, false);

		return EVAL_BODY_AGAIN;
	}

	protected void setContext(Pair p, boolean first) {
		// initialize block part
		pageContext.setAttribute(ATTR_CATEGORY, p.getCategory());
		pageContext.setAttribute(ATTR_NODES, p.getChildren());
		pageContext.setAttribute(ATTR_CONTAINS_PRODUCT, p.hasProduct());
		pageContext.setAttribute(ATTR_IS_FIRST, first);
	}

	/**
	 * Return true if the given node is a direct subcategory
	 * 
	 * @param node
	 * @return
	 */
	CategoryModel isDirectSubCategory(ContentNodeModel node) {
		return (
					(node instanceof CategoryModel)
					&&
					parentKey.equals( ((CategoryModel) node ).getParentNode().getContentKey() )
				)
				?
				(CategoryModel) node
				:
				null;
	}
	


	protected static class Pair {
		/**
		 * Header category, can be null
		 */
		public CategoryModel category;
		
		/**
		 * Child products or categories
		 */
		public List<ContentNodeModel> children = new ArrayList<ContentNodeModel>();

		public boolean containsProduct = false;
		
		public Pair(CategoryModel category) {
			this.category = category;
		}
		
		public void setCategory(CategoryModel category) {
			this.category = category;
		}
		
		public CategoryModel getCategory() {
			return category;
		}
		
		public void addNode(ContentNodeModel node) {
			containsProduct |= node instanceof ProductModel;
			children.add( node );
		}
		
		public List<ContentNodeModel> getChildren() {
			return children;
		}
		
		public boolean hasProduct() {
			return containsProduct;
		}
	}


	/**
	 * Transform input list to a sequence of Pairs. A Pair is composed of
	 * a parent category and a list of all descendant nodes
	 * @return
	 */
	protected List<Pair> compute() {
		List<Pair> multiList = new ArrayList<Pair>();
		
		Pair p = null;
		
		for (ContentNodeModel node : sortedCollection) {
			CategoryModel c = null;
			if ( (c = isDirectSubCategory(node)) != null) {
				//LOGGER.debug( "Create a new list with header cat " + c.getFullName() );
				p = new Pair(c);
				multiList.add(p);
			} else if ( node instanceof ProductModel || node instanceof CategoryModel ) {
				// what is it?
				if ( p == null) {
					//LOGGER.debug( "Create a headless list (ie. no header category)!");
					p = new Pair( null );
					multiList.add(p);
				}
				
				p.addNode(node);
				//LOGGER.debug( "  .. << " + node.getFullName() + " / " + node.getClass().getSimpleName() );
			} else {
				//skip node
				//LOGGER.debug( "  .. discard " + node.getFullName() + " ..");
			}
		}

		return multiList;
	}

	public static final class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
	        return new VariableInfo[] {
		            new VariableInfo(
		            		ATTR_IS_FIRST,
		            		"java.lang.Boolean",
		            		true, 
		            		VariableInfo.NESTED
		            ),
		            new VariableInfo(
		            		ATTR_CONTAINS_PRODUCT,
		            		"java.lang.Boolean",
		            		true, 
		            		VariableInfo.NESTED
		            ),
		            new VariableInfo(
		            		ATTR_NODES,
		            		"java.util.List<com.freshdirect.storeapi.content.ContentNodeModel>",
		            		true, 
		            		VariableInfo.NESTED
		            ),
		            new VariableInfo(
		            		ATTR_CATEGORY,
		            		"com.freshdirect.storeapi.content.CategoryModel",
		            		true, 
		            		VariableInfo.NESTED
		            )
	        };
		}
	}
}
