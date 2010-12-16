package com.freshdirect.webapp.taglib.fdstore.display;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.fdstore.content.ContentNodeModel;

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

	/**
	 * Parent container to update with max. value
	 */
	private String parentId;
	private int offset = 0;
	
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

	@Override
	protected void doFirst() {
		println("<div id=\"carousel-" + carouselId + "\" class=\"fd-carousel fixedheight\" style=\"width:" + width + "\"><ol>");
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


		carouselCall.append(");\n");
		println("<script>\n" + 
				"if (window.fd_carousel === undefined) {\n" +
				"  YAHOO.util.Get.css('/assets/css/carousel.css');\n" +
				"  YAHOO.util.Get.script('/assets/javascript/carousel.js', {\n" +
				"    onSuccess: function() {\n" +
				carouselCall +
				"    }\n" +
				"  });\n" +
				"} else {\n" +
				carouselCall +
				"}\n</script>\n");
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
