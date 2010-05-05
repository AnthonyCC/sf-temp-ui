<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%-- 

  YMAL display test page
  ----------------------
  
  @author segabor

--%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ page import="java.util.Set"
%><%@ page import="java.util.List"
%><%@ page import="java.util.Map"
%><%@ page import="java.util.HashMap"
%><%@ page import="java.util.ArrayList"
%><%@ page import="java.util.HashSet"
%><%@ page import="java.util.LinkedList"
%><%@ page import="java.util.Iterator"
%><%@ page import="java.util.Collections"
%><%@ page import="java.util.Comparator"
%><%@ page import="com.freshdirect.cms.ContentNodeI"
%><%@ page import="com.freshdirect.cms.ContentType"
%><%@ page import="com.freshdirect.cms.ContentKey"
%><%@ page import="com.freshdirect.cms.fdstore.FDContentTypes"
%><%@ page import="com.freshdirect.cms.application.CmsManager"
%><%@ page import="com.freshdirect.cms.application.service.query.CmsQueryTypes"
%><%@ page import="com.freshdirect.fdstore.content.ContentFactory"
%><%@ page import="com.freshdirect.fdstore.content.ContentNodeModel"
%><%@ page import="com.freshdirect.fdstore.content.ProductModel"
%><%@ page import="com.freshdirect.fdstore.content.Recipe"
%><%@ page import="com.freshdirect.fdstore.content.YmalSet"
%><%@ page import="com.freshdirect.fdstore.content.YmalSource"
%><%@ page import="com.freshdirect.webapp.util.FDURLUtil"
%><%
	final CmsManager mgr = CmsManager.getInstance();
	
	LinkedList s = new LinkedList();
	s.add(mgr.getContentNode(ContentKey.decode("Store:FreshDirect"))); // store
	s.add(mgr.getContentNode(ContentKey.decode("FDFolder:recipes"))); // recipes
%>
<html>
<head>
	<title>YMAL Display Test Page</title>
	<style type="text/css">
		td {
			font-size: 11px;
		}
		th {
			font-size: 11px;
		}
	</style>
</head>
<body>
<%
	Set ymalSets = new HashSet();
	Map t = new HashMap();

	while (!s.isEmpty()) {
		ContentNodeI e = (ContentNodeI) s.removeFirst();
		
		for(Iterator it = e.getChildKeys().iterator(); it.hasNext();) {
			ContentKey k = (ContentKey) it.next();
			
			if ( FDContentTypes.PRODUCT.equals(k.getType()) ) {
				// product
				ProductModel prd = ContentFactory.getInstance().getProduct(e.getKey().getId(), k.getId());
				
				YmalSet yset = prd.getActiveYmalSet();
				if (yset != null) {
					ymalSets.add(yset);
					t.put(yset.getContentKey(), prd);
				}
			} else if ( FDContentTypes.RECIPE.equals(k.getType()) ) {
				// recipe
				Recipe rec = (Recipe) ContentFactory.getInstance().getContentNodeByKey(k);
				YmalSet yset = rec.getActiveYmalSet();
				if (yset != null) {
					ymalSets.add(yset);
					t.put(yset.getContentKey(), rec);
				}
			} else if ( FDContentTypes.DEPARTMENT.equals(k.getType()) ) {
				s.add(mgr.getContentNode(k));
			} else if ( FDContentTypes.CATEGORY.equals(k.getType()) ) {
				s.add(mgr.getContentNode(k));
			} else if ( FDContentTypes.FDFOLDER.equals(k.getType()) ) {
				s.add(mgr.getContentNode(k));
			}
		}
	}


	List sets = new ArrayList();
	sets.addAll(ymalSets);
	
	Collections.sort(sets, new Comparator() {
		public int compare(Object o1, Object o2) {
			return compare2(o1,o2)*-1;	
		}

		private int compare2(Object o1, Object o2) {
			YmalSet s1 = (YmalSet)o1;
			YmalSet s2 = (YmalSet)o2;

			int p = cmp(s1.getYmalProducts().size(), s2.getYmalProducts().size());
			if (p == 0) {
				int c = cmp(s1.getYmalCategories().size(), s2.getYmalCategories().size());
				if (c == 0) {
					int r = cmp(s1.getYmalRecipes().size(), s2.getYmalRecipes().size());
					return r;
				}
				return c;
			}
			return p;
		}
		
		// compare ints
		private int cmp(int a, int b) {
			if (a == b) return 0;
			return a < b ? -1 : 1;
		}
	});
%>
	<div style="border: 2px solid #bbb; height: 250px; overflow: scroll;">
		<table>
			<tr>
				<th>&nbsp;</th>
				<th>ID</th>
				<th>Pr</th>
				<th>Ca</th>
				<th>Re</th>
				<th>Owner</th>
			</tr>
<%
	// list ymal sets
	int z = 1;
	for (Iterator it=sets.iterator(); it.hasNext();) {
		YmalSet yset = (YmalSet)it.next();
		ContentNodeModel mdl = (ContentNodeModel) t.get(yset.getContentKey());
		
		boolean valid = (yset.getYmalProducts().size() > 0 && yset.getYmalCategories().size() > 0 && yset.getYmalRecipes().size() > 0);
		String uri = "/test/content/ymal_set_preview.jsp?ymalSetId="+yset.getContentName();

		if (mdl instanceof ProductModel) {
			uri += "&amp;productId="+mdl.getContentKey().getId();
		}
		
		String bkg = (z%2==0 ? "background-color: white; " : "background-color: #eee; ");
		%>
			<tr>
				<td style="<%= bkg %> vertical-align: top;"><%= z %></td>
				<td style="<%= bkg %> vertical-align: top;"><a href="#" onclick="document.getElementById('preview_frame').src = '<%= uri %>'; return false;"><%= yset.getContentName() %></a></td>
				<td style="<%= bkg %> vertical-align: top; text-align: center; <%= yset.getYmalProducts().size() == 0 ? "color: gray;" : "" %>"><%= yset.getYmalProducts().size() %></td>
				<td style="<%= bkg %> vertical-align: top; text-align: center; <%= yset.getYmalCategories().size() == 0 ? "color: gray;" : "" %>"><%= yset.getYmalCategories().size() %></td>
				<td style="<%= bkg %> vertical-align: top; text-align: center; <%= yset.getYmalRecipes().size() == 0 ? "color: gray;" : "" %>"><%= yset.getYmalRecipes().size() %></td>
				<td style="<%= bkg %> vertical-align: top; text-align: left;">
				<% if (mdl instanceof ProductModel) {
					ProductModel prd = (ProductModel) mdl;
					if (prd.isFullyAvailable()) {
						%><%= prd.getParentNode().getFullName() %> &gt; <a href="<%= FDURLUtil.getProductURI(prd, "test") %>" target="prodwin"><%= prd.getFullName() %></a><%
					} else {
						%><span style="color: gray;"><%= prd.getFullName() %></span><%
					}
				} %>
				<% if (mdl instanceof Recipe) {
					Recipe rec = (Recipe) mdl;
					%>REC: <%= rec.getContentName() %><%
				} %>
				</td>
			</tr>
<%
		z++;
	}
%>
		</table>
	</div>

	<iframe id="preview_frame" src="/test/content/ymal_set_preview.jsp" style="width: 100%; height: 500px;">
	</iframe>
</body>
</html>
