package com.freshdirect.webapp.util;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryRef;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentRef;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.framework.util.NVL;

public class SearchResultUtil {

	private final static NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

	////////////////////////////////////////
	///////// tree builder methods /////////
	////////////////////////////////////////

	public static Map buildNodeTree(List products) {
		Map map = new HashMap();
		for (Iterator i = products.iterator(); i.hasNext();) {
			buildNodeTree(map, (ProductModel) i.next());
		}
		return map;
	}

	private final static Comparator NODE_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			if (o1 instanceof ProductModel && o2 instanceof ProductModel) {
				ProductModel p1 = (ProductModel) o1;
				ProductModel p2 = (ProductModel) o2;

				String unav1 = p1.isUnavailable() ? "_" : "";
				String unav2 = p2.isUnavailable() ? "_" : "";

				return (unav1 + p1.getFullName() + p1.getContentName()).compareTo(unav2 + p2.getFullName() + p2.getContentName());

			} else if (o1 instanceof DepartmentModel && o2 instanceof DepartmentModel) {
				int p1 = ((DepartmentModel) o1).getPriority();
				int p2 = ((DepartmentModel) o2).getPriority();
				return p1 == p2 ? 0 : (p1 < p2 ? -1 : 1);
			}

			ContentNodeModel n1 = (ContentNodeModel) o1;
			ContentNodeModel n2 = (ContentNodeModel) o2;
			

			String s1 = n1.getContentType() + "_" + NVL.apply(n1.getFullName(), "");
			String s2 = n2.getContentType() + "_" + NVL.apply(n2.getFullName(), "");

			return s1.compareTo(s2);
		}
	};

//	private final static DecimalFormat ZERO_PAD = new DecimalFormat("000000");

	public final static Comparator RECURSIVE_NODE_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			StringBuffer sb1 = new StringBuffer();
			generateKey(sb1, (ContentNodeModel) o1);

			StringBuffer sb2 = new StringBuffer();
			generateKey(sb2, (ContentNodeModel) o2);
			
			return sb1.toString().compareTo(sb2.toString());
		}

		private void generateKey(StringBuffer key, ContentNodeModel n) {
			ContentNodeModel p = n.getParentNode();
			if (p == null) {
				return;
			}
			key.insert(0, ":");
			// if (p instanceof DepartmentModel) {
			// key.insert(0, ZERO_PAD.format(((DepartmentModel) p).getPriority()));
			// } else {

			key.insert(0, n.getFullName());
			generateKey(key, p);
		}

	};

	private static void buildNodeTree(Map map, ContentNodeModel node) {
		ContentNodeModel parent = node.getParentNode();
		
		Collection ls = (Collection) map.get(parent);
		if (ls == null) {
			ls = new TreeSet(NODE_COMPARATOR);
			map.put(parent, ls);
		}
		ls.add(node);

		if (parent != null) {
			buildNodeTree(map, parent);
		}
	}

	////////////////////////////////////////
	///////// tree display methods /////////
	////////////////////////////////////////

	public static void displayNodeTree(JspWriter out, Map map, String trk) throws FDResourceException, IOException {
		Collection root = (Collection) map.get(null);
		displayNodeTree(out, map, 0, root, trk);
	}

	private static void displayNodeTree(JspWriter out, Map map, int depth, Collection ls, String trk) throws FDResourceException, IOException {
		for (Iterator i = ls.iterator(); i.hasNext();) {
			Object o = i.next();

			for (int c = 1; c < depth; c++)
				out.print("&nbsp;&nbsp;");

			if (o instanceof ProductModel) {
				displayTreeProduct(out, (ProductModel) o, trk);

			} else if (o instanceof CategoryModel) {
				displayTreeCategory(out, (CategoryModel) o, trk);
				displayNodeTree(out, map, depth + 1, (Collection) map.get(o), trk);
				out.println("<br>");

			} else if (o instanceof DepartmentModel) {
				displayTreeDepartment(out, (DepartmentModel) o);
				displayNodeTree(out, map, depth + 1, (Collection) map.get(o), trk);
				out.println("<br>");

			} else {
				displayNodeTree(out, map, depth + 1, (Collection) map.get(o), trk);
				//out.println("<br>");
			}
		}
	}

	private static void displayTreeProduct(JspWriter out, ProductModel product, String trk) throws FDResourceException, IOException {
		boolean unav = product.isUnavailable();
		String productName = product.getFullName();
		String brandName = product.getPrimaryBrandName();
		if (brandName != null
			&& brandName.length() > 0
			&& (productName.length() >= brandName.length())
			&& productName.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {
			String shortenedProductName = productName.substring(brandName.length()).trim();
			productName = "<b>" + brandName + "</b> " + shortenedProductName;
		}

		out.print("<a href='/product.jsp?productId=");
		out.print(product);
		out.print("&catId=");
		out.print(product.getParentNode());
		out.print("&trk=");
		out.print(trk);
		out.print("\'");
		if (unav)
			out.print(" style='color:#999999'");
		out.print(">");
		out.print(productName);
		out.print("</a>");

		String productPrice = null;
		Comparator priceComp = new ProductModel.PriceComparator();
		List skus = product.getSkus();
		for (ListIterator li = skus.listIterator(); li.hasNext();) {
			SkuModel sku = (SkuModel) li.next();
			if (sku.isUnavailable()) {
				li.remove();
			}
		}
		int skuSize = skus.size();

		SkuModel sku = null;

		// skip this item..it has no skus.  Hmmm?
		if (skuSize == 1) {
			sku = (SkuModel) skus.get(0); // we only need one sku
		} else if (skus.size() > 1) {
			sku = (SkuModel) Collections.min(skus, priceComp);
		}

		FDProductInfo pi;

		if (sku != null) {
			try {
				pi = FDCachedFactory.getProductInfo(sku.getSkuCode());
				productPrice = currencyFormatter.format(pi.getDefaultPrice())
					+ "/"
					+ pi.getDisplayableDefaultPriceUnit().toLowerCase();
			} catch (FDSkuNotFoundException ex) {
				// safe to ignore
			}
		}

		if (productPrice == null) {
			productPrice = "&nbsp;";
		} else if (product.isUnavailable()) {
			productPrice = "Not Avail.";
		}

		String sizeDesc = product.getSizeDescription();
		if (sizeDesc != null) {
			if (unav)
				out.print("<font color='#999999'>");
			out.print(" (" + sizeDesc + ") ");
			if (unav)
				out.print("</font>");
		} else {
			out.print("&nbsp;");
		}
		
		if (product.getAka() != null && !"".equals(product.getAka())) {
			if (unav)
				out.print("<font color='#999999'>");
			out.print(" (" + product.getAka() + ") ");
			if (unav)
				out.print("</font>");
		}
		
		if (productPrice != null && !"&nbsp;".equals(productPrice)) {
			out.print("- " + productPrice);
		}

		out.println("<br>");
	}

	private static void displayTreeCategory(JspWriter out, CategoryModel category, String trk) throws IOException {
		out.print("<b><a href='/category.jsp?catId=");
		out.print(getLinkCat(category));
		out.print("&trk=");
		out.print(trk);
		out.print("\'>");
		out.print(category.getFullName());
		out.println("</a></b><br>");
	}

	private static void displayTreeDepartment(JspWriter out, DepartmentModel department) throws IOException {
		out.print("<font class='text11orbold'><a name='");
		out.print(department.getContentName());
		out.print("'>");
		out.print(department.getFullName().toUpperCase());
		out.println("</a></font><br>");
	}

	////////////////////////////////////////
	///////// path display methods /////////
	////////////////////////////////////////

	public static String getPathDisplay(ProductModel prod, String trk) throws FDResourceException {
		StringBuffer buf = new StringBuffer();

		buf.append("<div style='padding: 1em;'>");
		buf.append("<a href='/product.jsp?productId=").append(prod);
		buf.append("&catId=").append(prod.getParentNode()).append("&trk=srch'>");

		Image img = prod.getThumbnailImage();
		//Image img  = product.getCategoryImage();
		if (img!=null) {
			buf.append("<img border='0' hspace='10' src='").append(img.getPath());
			buf.append("' width='").append(img.getWidth());
			buf.append("' height='").append(img.getHeight());
			buf.append("'>");
			buf.append("</a>");
			buf.append("<a href='/product.jsp?productId=").append(prod);
			buf.append("&catId=").append(prod.getParentNode()).append("&trk=").append(trk).append("\'>");
		}
		
		buf.append("<b>");
		buf.append(prod.getFullName());
		buf.append("</a></b>");

		String sizeDesc = prod.getSizeDescription();
		if (sizeDesc != null) {
			buf.append(" - ").append(sizeDesc);
		}
		
		SkuModel sku = prod.getDefaultSku();

		if (sku != null) {
			try {
				FDProductInfo pi = FDCachedFactory.getProductInfo(sku.getSkuCode());
				String productPrice = currencyFormatter.format(pi.getDefaultPrice())
					+ "/"
					+ pi.getDisplayableDefaultPriceUnit().toLowerCase();
				
				buf.append(" - ").append(productPrice);

			} catch (FDSkuNotFoundException ex) {
				// safe to ignore
			}
		}
		buf.append("</div>");

		generatePath(true, buf, prod.getParentNode(),trk);
		return buf.toString();
	}

	public static String getPathDisplay(CategoryModel cat, String trk) throws FDResourceException {
		StringBuffer buf = new StringBuffer();
		generatePath(false, buf, cat,trk);
		return buf.toString();
	}

	private static void generatePath(boolean separator, StringBuffer buf, ContentNodeModel node, String trk) throws FDResourceException {
		boolean firstItem = buf.length() == 0;
		
		if (separator)
			buf.insert(0, "&nbsp;&gt;&nbsp;");

		if (firstItem) {
			buf.insert(0, "</b>");
		}
		
		if (node instanceof CategoryModel) {
			buf.insert(0, getPathCategory((CategoryModel) node,trk));
		} else if (node instanceof DepartmentModel) {
			buf.insert(0, getPathDepartment((DepartmentModel) node,trk));
		}
		
		if (firstItem) {
			buf.insert(0, "<b>");
		}
		
		if (!(node.getParentNode() instanceof StoreModel)) {
			generatePath(true, buf, node.getParentNode(),trk);
		}
	}

	private static String getPathCategory(CategoryModel cat, String trk) {
		return "<a href='/category.jsp?catId=" + getLinkCat(cat) + "&trk=" + trk + "\'>" + cat.getFullName() + "</a>";
	}

	private static String getPathDepartment(DepartmentModel dept, String trk) {
		return "<a href='/department.jsp?deptId=" + dept + "&trk=" + trk + "\'>" + dept.getFullName().toUpperCase() + "</a>";
	}

	///////////////////////////////////
	///////// utility methods /////////
	///////////////////////////////////

	private static CategoryModel getLinkCat(CategoryModel category) {
		Attribute alias = category.getAttribute("ALIAS");
		if (alias != null) {
			ContentRef aliasRef = (ContentRef) alias.getValue();
			if (aliasRef instanceof CategoryRef)
				return ((CategoryRef) aliasRef).getCategory();
			//else if (aliasRef instanceof DepartmentRef) return (DepartmentRef)aliasRef;

		}
		return category;
	}

}
