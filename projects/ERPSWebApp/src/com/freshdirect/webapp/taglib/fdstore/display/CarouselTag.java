package com.freshdirect.webapp.taglib.fdstore.display;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.ProductModel;

public class CarouselTag extends ContentNodeIteratorTag {

	private static final long serialVersionUID = 5507018403840314760L;

	// === ATTRIBUTES ===
	private int numItems = 1;
	private int width = 0;
	private int height = 0;
	private String carouselId;
	private String bottomHeader = null;
	private String bottomHeaderClass = null;
	private String hideContainer = null;
	private boolean useAlternateImage = false;
	private String eventHandlersObj;
	
	private String style; /* Additional styles for the carousel container element (optional) */
	
	/**
	 * Parent container to update with max. value
	 */
	private String parentId;
	
	
	private int offset = 0;
	private int maxHeight = 0;
	
	public int getNumItems() {
		return numItems;
	}

	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getCarouselId() {
		return carouselId;
	}

	public void setCarouselId(String carouselId) {
		this.carouselId = carouselId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}



	public String getHideContainer() {
		return hideContainer;
	}

	public void setHideContainer(String hideContainer) {
		this.hideContainer = hideContainer;
	}

	public String getBottomHeader() {
		return bottomHeader;
	}

	public void setBottomHeader(String bottomHeader) {
		this.bottomHeader = bottomHeader;
	}

	public String getBottomHeaderClass() {
		return bottomHeaderClass;
	}

	public void setBottomHeaderClass(String bottomHeaderClass) {
		this.bottomHeaderClass = bottomHeaderClass;
	}

	public boolean isUseAlternateImage() {
		return useAlternateImage;
	}	
	public void setUseAlternateImage( boolean useAlternateImage ) {
		this.useAlternateImage = useAlternateImage;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public void setEventHandlersObj(String eventHandlersObj) {
		this.eventHandlersObj = eventHandlersObj;
	}

	/**
	 * May not be initialized if called before doFirst()!
	 * @return maximum image height
	 */
	public int getMaxImageHeight() {
		return maxHeight;
	}
	
	@Override
	protected void doFirst() {
		
		// first calculate maximum image height
		maxHeight = 0;
		for ( ContentNodeModel node : itemsToShow ) {
			Image img = null;
			if ( node instanceof ProductModel && useAlternateImage ) {			
				img = ((ProductModel)node).getAlternateImage();
				// fall back to normal image if no alternate available
				if ( img == null )
					img = ((ProductModel)node).getProdImage();
			} else if ( node instanceof ProductModel ) {				
				img = ((ProductModel)node).getProdImage();
			} else if ( node instanceof CategoryModel ) {
				img = ((CategoryModel)node).getCategoryPhoto();
			}
			
			if ( img != null )
				maxHeight = Math.max( maxHeight, img.getHeight() );
		}		


		StringBuilder __style_buf = new StringBuilder();
		__style_buf.append("width: ");
		__style_buf.append(Integer.toString(width));
		__style_buf.append("px");
		if (this.style != null) {
			__style_buf.append("; ");
			__style_buf.append(this.style);
		}


		println("<div id=\"carousel-" + carouselId + "\" class=\"fd-carousel fixedheight\" style=\"" + __style_buf + "\"><ol>");
	}

	@Override
	protected void doStart() {
		Integer index = (Integer) pageContext.getAttribute(itemIndexVariableName);
		println("<li style=\"width:" + ((width - 20) / numItems) + "px;" + (height > 0 ? "height:" + height + "px;" : "") + "\"" +
				((index % numItems) == 0 ? "class=\"fd-carousel-first-item\"" : "") + ">");
	}

	@Override
	protected void doEnd() {
		println("</li>");
	}

	@Override
	protected void doLast() {
		println("</ol></div>");


		StringBuilder carouselCall = new StringBuilder();

		// param #1 ID
		carouselCall.append("fd_carousel(\"carousel-");
		carouselCall.append(carouselId);
		carouselCall.append("\", ");

		// param #2 numItems
		carouselCall.append(numItems);
		carouselCall.append(", \"");

		// param #3 hideContainer
		carouselCall.append(hideContainer != null && hideContainer.trim().length() > 0 ? hideContainer.trim() : "@");
		carouselCall.append("\"");
		
		// param #4 text
		if (bottomHeader != null) {
			carouselCall.append(", \"");
			carouselCall.append(StringEscapeUtils.escapeJavaScript(bottomHeader));
			carouselCall.append("\"");
		} else {
			carouselCall.append(", null");
		}

		// param #5 cName
		if (bottomHeaderClass != null) {
			if (bottomHeader == null)
				carouselCall.append(", \"\"");
			carouselCall.append(", \"");
			carouselCall.append(StringEscapeUtils.escapeJavaScript(bottomHeaderClass));
			carouselCall.append("\"");
		} else {
			carouselCall.append(", null");
		}

		// param #6 parentId
		carouselCall.append(", \"");
		carouselCall.append( parentId != null ? StringEscapeUtils.escapeJavaScript(parentId) : "null" );
		carouselCall.append("\"");

		// param #7 offset in pixels
		carouselCall.append(", ");
		carouselCall.append( offset > 0 ? offset : "0" );
		// carouselCall.append("");

		// param #8 eventHandlers
		carouselCall.append(", ");
		carouselCall.append( eventHandlersObj == null ? "null" : eventHandlersObj );

		carouselCall.append(");");
		println("<script>" + carouselCall + "</script>");
	}

	public static class TagEI extends TagExtraInfo {
		/**
		 * Return information about the scripting variables to be created.
		 */
		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
					new VariableInfo(data.getAttributeString("id"), CarouselTag.class.getName(), true, VariableInfo.NESTED),
					new VariableInfo(currentItemVariableName, ContentNodeModel.class.getName(), true, VariableInfo.NESTED),
					new VariableInfo(itemIndexVariableName, Integer.class.getName(), true, VariableInfo.NESTED),
					new VariableInfo(actionUrlVariableName, String.class.getName(), true, VariableInfo.NESTED) };
		}
	}
}
