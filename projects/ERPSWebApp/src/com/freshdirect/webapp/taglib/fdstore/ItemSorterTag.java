package com.freshdirect.webapp.taglib.fdstore;

import java.util.Collections;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;


import com.freshdirect.fdstore.content.util.ContentNodeComparator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.TagSupport;

/**@author ekracoff*/
public class ItemSorterTag extends TagSupport {
	private static Category LOGGER = LoggerFactory.getInstance(ItemSorterTag.class);

	private List nodes;
	private List strategy;

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}

	public void setStrategy(List strategy) {
		this.strategy = strategy;
	}

	public int doStartTag() throws JspException {

		LOGGER.info(">>>Sorting " + nodes.size() + " items by " + strategy.size() + " attributes");

		Collections.sort(nodes, new ContentNodeComparator(strategy));

		LOGGER.info(">>>Sort complete");

		return super.doStartTag();
	}


}
