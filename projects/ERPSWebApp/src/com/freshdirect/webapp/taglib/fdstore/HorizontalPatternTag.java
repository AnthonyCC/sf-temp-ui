/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.util.*;
import javax.servlet.jsp.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class HorizontalPatternTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private String id = null;
	private Object[] itemsToShow = null;
	private Integer[] patternArray = null;


	//work variarible
	private int patternIndex = 0;
	private int itemsToShowIndex = 0;
	private int patternArraySize = 0;
	private int itemsToShowSize = 0;
	public void setId(String id) {
	  this.id = id;
	}

	public void setItemsToShow(Object[] itemsToShow) {
		this.itemsToShow = itemsToShow;
		this.itemsToShowSize = this.itemsToShow.length;
	}

	public void setPatternArray(Integer[] patternArray){
		this.patternArray = patternArray;
		this.patternArraySize = this.patternArray.length;
	}

	public int doStartTag() throws JspException {
		//
		// clean the page context from previous iterations
		//
		if (itemsToShow==null || patternArray==null || itemsToShowSize<1 || patternArraySize<1) {
			return SKIP_BODY;
		} else {
			return EVAL_BODY_BUFFERED;
		}
	}

	public void doInitBody()throws JspException  {
      pageContext.setAttribute(id, buildReturnArray());
	}

  	public int doAfterBody() throws JspException  {

		 Object[] returnItems=buildReturnArray();

		 if (returnItems.length==0){
			 return  SKIP_BODY;
		 }
		 else {
       	pageContext.setAttribute(id, returnItems);
	 	}
		return EVAL_BODY_BUFFERED;
	}


	//the dirty work method.
	private java.lang.Object[] buildReturnArray() {
		ArrayList returnItems = new ArrayList();

		int numOfItemsOnRow = 0;

		//check to see if the pattern index has been exhausted..and reset it
		if (patternIndex>=patternArraySize) patternIndex = 0;
		numOfItemsOnRow = patternArray[patternIndex].intValue();

		for (int rowItemIndex=0;rowItemIndex < numOfItemsOnRow && itemsToShowIndex<itemsToShowSize; rowItemIndex++) {
			returnItems.add(itemsToShow[itemsToShowIndex++]);

		}
		patternIndex++;
		return returnItems.toArray();
	}

}
