package com.freshdirect.webapp.taglib.fdstore.layout;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.PrioritizedI;
import com.freshdirect.fdstore.content.ProductModelImpl;
import com.freshdirect.fdstore.content.util.ContentNodeComparator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.TagSupport;

/**@author ekracoff*/
public class ItemSorterTag extends TagSupport {
	private static Category LOGGER = LoggerFactory.getInstance(ItemSorterTag.class);

	private List<ContentNodeModel> nodes;
	private List strategy;

	public void setNodes(List<ContentNodeModel> nodes) {
		this.nodes = nodes;
	}

	public void setStrategy(List strategy) {
		this.strategy = strategy;
	}

	public int doStartTag() throws JspException {

		LOGGER.info(">>>Sorting " + nodes.size() + " items by " + strategy.size() + " attributes");

		Collections.sort(nodes, new ContentNodeComparator(strategy));

		LOGGER.info(">>>Sort complete");
		String debug = pageContext.getRequest().getParameter("debug");
		if ("dumpSortResult".equals(debug)) {
		    JspWriter out = pageContext.getOut();
		    boolean html = false;
		    try {
		        if (!html) {
                            out.println("<!-- ItemSorterTag ");
		        }
                        out.println(" strategy : "+strategy);
                        out.println(" result : ");
                        if (nodes != null) {
                            for (ContentNodeModel m : nodes) {
                                if (html) { out.println("<br/>"); }
                                String prio = (m instanceof PrioritizedI) ? " priority:"+((PrioritizedI)m).getPriority() : "";  
                                if (m instanceof ProductModelImpl) {
                                    out.println("    "+((ProductModelImpl)m).getReverseParentPath(true) + prio);
                                } else {
                                    out.println("    "+m.getFullName()+'('+m.getContentKey()+')'+ prio);
                                }
                            }
                        }
                        if (!html) {
                            out.println("-->");
                        }
		    } catch (IOException e) {
	                LOGGER.info("error during dumping debug information:"+e.getMessage(), e);
	            }
	                
		}

		return super.doStartTag();
	}


}
