package com.freshdirect.webapp.taglib.fdstore.display;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class CarouselTag extends ContentNodeIteratorTag {

	private static final long	serialVersionUID	= 5507018403840314760L;
	
	// === ATTRIBUTES ===
	private int									numItems					= 1;
	private int									width						= 0;
	private int									height						= 0;	
	
	public int getNumItems() {
		return numItems;
	}

	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}
	
	public int getWidth() {
		return width;
	}	
	public void setWidth( int width ) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}	
	public void setHeight( int height ) {
		this.height = height;
	}
	
	
	@Override
	protected void doFirst() {
		println("<div id=\"carousel-"+id+"\" class=\"fd-carousel fixedheight\" style=\"width:"+width+"\"><ol>");
	}
	
	@Override
	protected void doStart() {
//		println("<li style=\"width:"+((width-20)/numItems)+"px;height:"+height+"px\">");
		println("<li style=\"width:"+((width-20)/numItems)+"px\">");
	}
	
	@Override
	protected void doEnd() {
		println( "</li>" );
	}
	
	@Override
	protected void doLast() {
		println("</ol></div>");
		println("<script>if(window.fd_carousel===undefined) { YAHOO.util.Get.css('/assets/css/carousel.css');YAHOO.util.Get.script('/assets/javascript/carousel.js',{onSuccess:function(){ fd_carousel(\"carousel-"+id+"\","+numItems+") }})} else { fd_carousel(\"carousel-"+id+"\","+numItems+") }</script>");
	}
	

	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "id" ),
	            		"com.freshdirect.webapp.taglib.fdstore.display.CarouselTag",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		currentItemVariableName,
	            		"com.freshdirect.fdstore.content.ContentNodeModel",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		itemIndexVariableName,
	            		"java.lang.Integer",
	            		true, 
	            		VariableInfo.NESTED ),
	            new VariableInfo(
	            		actionUrlVariableName,
	            		"java.lang.String",
	            		true, 
	            		VariableInfo.NESTED )
	        };
	    }
	}
}
