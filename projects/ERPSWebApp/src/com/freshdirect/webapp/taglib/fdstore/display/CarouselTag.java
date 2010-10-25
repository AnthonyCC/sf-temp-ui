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
//		println("<li style=\"width:"+((width-20)/numItems)+"px;height:"+height+"px\">");
//		println("<li style=\"width:"+((width-20)/numItems)+"px\">");
	}

	@Override
	protected void doEnd() {
		println("</li>");
	}

	@Override
	protected void doLast() {
		println("</ol></div>");
		StringBuilder carouselCall = new StringBuilder();
		carouselCall.append("fd_carousel(\"carousel-");
		carouselCall.append(carouselId);
		carouselCall.append("\", ");
		carouselCall.append(numItems);
		carouselCall.append(", \"");
		carouselCall.append(hideContainer != null && hideContainer.trim().length() > 0 ? hideContainer.trim() : "@");
		carouselCall.append("\"");
		if (bottomHeader != null) {
			carouselCall.append(", \"");
			carouselCall.append(StringEscapeUtils.escapeJavaScript(bottomHeader));
			carouselCall.append("\"");
		}
		if (bottomHeaderClass != null) {
			if (bottomHeader == null)
				carouselCall.append(", \"\"");
			carouselCall.append(", \"");
			carouselCall.append(StringEscapeUtils.escapeJavaScript(bottomHeaderClass));
			carouselCall.append("\"");
		}
		carouselCall.append(");");
		println("<script>if (window.fd_carousel === undefined) { " +
				"YAHOO.util.Get.css('/assets/css/carousel.css'); " +
				"YAHOO.util.Get.script('/assets/javascript/carousel.js', " +
				"{ onSuccess: function() { " + carouselCall + " } }) }" +
				" else { " + carouselCall + " }</script>");
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
