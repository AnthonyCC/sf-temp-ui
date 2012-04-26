package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class PagerTag extends BodyTagSupport {
	
	private static final long serialVersionUID = -3279461498748988816L;
	private FilteringNavigator nav;
	private int productsSize;

	@Override
	public int doStartTag() throws JspException {

		StringBuffer buf = new StringBuffer();

		final int pg		= nav.getPageNumber();
		final int psize		= nav.getPageSize();
		final int pages		= psize==0 ? 1 : ( productsSize / psize + Math.min( 1, productsSize % psize ));
		
		if(pg>0) {
			buf.append("<span class=\"pager-prev\">");
    		buf.append("<a href=\"");
    		buf.append(nav.getJumpToPageAction(psize*(pg-1)));
    		buf.append("\">");
			buf.append("&lt;");
    		buf.append("</a>");
    		buf.append("</span>");
		}
		
		if (pages == 1) {
			// pages == 1
    		buf.append("<span  class=\"pager-item pager-current\">");
			buf.append("1");
    		buf.append("</span>");
		} else {
			if (pages <= 7) {
				// [A] NORMAL PAGER (pages > 1)

				int currentPage = pg;
				int off = 0;
				for (int i=0;i<pages;i++) {
			    	if (currentPage==i) {
			    		buf.append("<span  class=\"pager-item pager-current\">");
			    		buf.append(i+1);
			    		buf.append("</span>");
			    	} else {
			    		buf.append("<span  class=\"pager-item\">");
			    		buf.append("<a href=\"");
			    		buf.append(nav.getJumpToPageAction(off));
			    		buf.append("\">");
			    		buf.append(i+1);
			    		buf.append("</a>");
			    		buf.append("</span>");
					}

		           	off += psize;
		       	}
			
			} else {
				// [B] EXTENDED PAGER (pages >= 15)
				final int R = 3;
				
				if (pg < R+2) {
					// (1) Case 1,2,3,....,N
					int off = 0;
					for (int k=0; k<(2*R+1); ++k) {
						if (k!=pg) {
				    		buf.append("<span  class=\"pager-item\">");
				    		buf.append("<a href=\"");
				    		buf.append(nav.getJumpToPageAction(off));
				    		buf.append("\">");
				    		buf.append(k+1);
				    		buf.append("</a>");
				    		buf.append("</span>");
						} else {
				    		buf.append("<span  class=\"pager-item pager-current\">");
				    		buf.append(k+1);
				    		buf.append("</span>");
						}
						off += psize;
					}
					buf.append("<span class=\"pager-separator\">...</span>");
		    		buf.append("<span  class=\"pager-item\">");
		    		buf.append("<a href=\"");
		    		buf.append(nav.getJumpToPageAction(psize*(pages-1)));
		    		buf.append("\">");
		    		buf.append(pages);
		    		buf.append("</a>");
		    		buf.append("</span>");
					
					
				} else if (pg >= pages-(R+2)) {
					// (2) Case 1,...,N-2,N-1,N
		    		buf.append("<span  class=\"pager-item\">");
		    		buf.append("<a href=\"");
		    		buf.append(nav.getJumpToPageAction(0));
		    		buf.append("\">");
		    		buf.append(1);
		    		buf.append("</a>");
		    		buf.append("</span>");
					buf.append("<span class=\"pager-separator\">...</span>");

					int off = psize*(pages-(R+2)-1);
					for (int k=pages-(R+2)-1; k<pages; ++k) {
						if (k!=pg) {
				    		buf.append("<span  class=\"pager-item\">");
				    		buf.append("<a href=\"");
				    		buf.append(nav.getJumpToPageAction(off));
				    		buf.append("\">");
				    		buf.append(k+1);
				    		buf.append("</a>");
				    		buf.append("</span>");
						} else {
				    		buf.append("<span  class=\"pager-item pager-current\">");
				    		buf.append(k+1);
				    		buf.append("</span>");
						}
						off += psize;
					}
				} else {
					// (3) Case 1,...,K-1,K,K+1,...,N							
		    		buf.append("<span  class=\"pager-item\">");
		    		buf.append("<a href=\"");
		    		buf.append(nav.getJumpToPageAction(0));
		    		buf.append("\">");
		    		buf.append(1);
		    		buf.append("</a>");
		    		buf.append("</span>");
					buf.append("<span class=\"pager-separator\">...</span>");

					int off = psize*(pg-R+1);
					for (int k=pg-R+1; k<=pg+R-1; ++k) {
						if (k!=pg) {
				    		buf.append("<span  class=\"pager-item\">");
				    		buf.append("<a href=\"");
				    		buf.append(nav.getJumpToPageAction(off));
				    		buf.append("\">");
				    		buf.append(k+1);
				    		buf.append("</a>");
				    		buf.append("</span>");
						} else {
				    		buf.append("<span  class=\"pager-item pager-current\">");
				    		buf.append(k+1);
				    		buf.append("</span>");
						}
						off += psize;
					}

					buf.append("<span class=\"pager-separator\">...</span>");
		    		buf.append("<span  class=\"pager-item\">");
		    		buf.append("<a href=\"");
		    		buf.append(nav.getJumpToPageAction(psize*(pages-1)));
		    		buf.append("\">");
		    		buf.append(pages);
		    		buf.append("</a>");
		    		buf.append("</span>");
				}
			}

			/*
			// display NEXT if pages are more than 3
			if (pages > 3 && pg < pages-1) {
				int new_offset = psize*(pg+1);
				%><span  class="pager-next"><a href="<%= nav.getJumpToPageAction( new_offset ) %>">NEXT</a></span><%
			}
			*/
		}
		
		if(pg<pages-1) {
			buf.append("<span class=\"pager-next\">");
    		buf.append("<a href=\"");
    		buf.append(nav.getJumpToPageAction(psize*(pg+1)));
    		buf.append("\">");
			buf.append("&gt;");
    		buf.append("</a>");
    		buf.append("</span>");
		}
		
		try {
			pageContext.getOut().print( buf );
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
	
	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}
	
	public FilteringNavigator getNav() {
		return nav;
	}
	
	public void setProductsSize(int productsSize) {
		this.productsSize = productsSize;
	}
	
	public int getProductsSize() {
		return productsSize;
	}
	
}
