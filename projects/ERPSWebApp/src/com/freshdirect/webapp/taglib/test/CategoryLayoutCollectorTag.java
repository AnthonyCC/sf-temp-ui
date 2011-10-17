package com.freshdirect.webapp.taglib.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.EnumLayoutType;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.StoreModel;

public class CategoryLayoutCollectorTag extends com.freshdirect.framework.webapp.TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7054785315714222439L;

	private Map<ProductContainer, EnumLayoutType> layoutStore = new HashMap<ProductContainer, EnumLayoutType>();
	private Map<EnumLayoutType, List<ProductContainer>> groupedResult = new HashMap<EnumLayoutType, List<ProductContainer>>();

	@Override
	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		JspWriter out = pageContext.getOut();

		layoutStore.clear();

		StoreModel store = ContentFactory.getInstance().getStore();

		for (DepartmentModel dept : store.getDepartments()) {
			collectAllCategories(dept);
		}

		groupResult();

		if (request.getParameter("toCsv") != null) {

			try {
				out.clear();

				File file = writeToCsv();

				String filename = "layouts.csv";

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + '"');

				FileInputStream fileInputStream;
				fileInputStream = new FileInputStream(file);

				int i;
				while ((i = fileInputStream.read()) != -1) {
					out.write(i);
				}

				fileInputStream.close();
				out.close();

				return SKIP_BODY;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		request.setAttribute("groupedResult", groupedResult);

		return EVAL_PAGE;
	}

	private void collectAllCategories(ProductContainer cat) {

		if (!cat.isHidden()) {
			layoutStore.put(cat, cat.getLayout());
		}

		if (cat.getSubcategories() != null) {
			for (ProductContainer con : cat.getSubcategories()) {
				collectAllCategories(con);
			}
		}
	}

	private void groupResult() {

		for (ProductContainer key : layoutStore.keySet()) {

			if (groupedResult.get(layoutStore.get(key)) != null) {
				groupedResult.get(layoutStore.get(key)).add(key);
			} else {
				List<ProductContainer> localList = new ArrayList<ProductContainer>();
				localList.add(key);
				groupedResult.put(layoutStore.get(key), localList);
			}
		}
	}

	private File writeToCsv() {

		File f = null;

		try {
			f = new File("layouts.csv");

			FileWriter writer = new FileWriter(f);
			writer.append("Layout id|Layout|Category|Category fullname|Parent id|Parent fullname|Has editorial\n");

			for (EnumLayoutType key : groupedResult.keySet()) {

				for (ProductContainer cont : groupedResult.get(key)) {

					boolean hasEditorial = false;

					if (cont instanceof CategoryModel) {
						CategoryModel cat = (CategoryModel) cont;
						try {
							if (cat.getSeparatorMedia() != null || cat.getAlternateContent() != null) {
								hasEditorial = true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (cont instanceof DepartmentModel) {
						DepartmentModel dept = (DepartmentModel) cont;
						if (dept.getDepartmentMiddleMedia() != null || dept.getAssocEditorial() != null) {
							hasEditorial = true;
						}
					}

					if (cont.getEditorial() != null || cont.getMediaContent() != null) {
						hasEditorial = true;
					}

					writer.append((key == null ? "null|null" : key.getId() + "|" + key.getName()) + "|" + cont + "|" + cont.getFullName() + "|" + cont.getParentId() + "|" + cont.getParentNode().getFullName() + "|" + hasEditorial + "\n");
				}

			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}
}
