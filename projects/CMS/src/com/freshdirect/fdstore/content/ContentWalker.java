package com.freshdirect.fdstore.content;

import java.util.Iterator;

public class ContentWalker {

	private final ContentVisitorI visitor;

	public ContentWalker(ContentVisitorI visitor) {
		this.visitor = visitor;
	}

	public void walk(StoreModel store) {
		this.visitor.visit(store);
		for (Iterator i = store.getDepartments().iterator(); i.hasNext();) {
			this.walk((DepartmentModel) i.next());
		}
	}

	public void walk(DepartmentModel dept) {
		this.visitor.visit(dept);
		for (Iterator i = dept.getCategories().iterator(); i.hasNext();) {
			this.walk((CategoryModel) i.next());
		}
	}

	public void walk(CategoryModel cat) {
		this.visitor.visit(cat);
		for (Iterator i = cat.getSubcategories().iterator(); i.hasNext();) {
			this.walk((CategoryModel) i.next());
		}
		for (Iterator i = cat.getProducts().iterator(); i.hasNext();) {
			this.walk((ProductModel) i.next());
		}
	}

	public void walk(ProductModel prod) {
		this.visitor.visit(prod);
		for (Iterator i = prod.getSkus().iterator(); i.hasNext();) {
			this.walk((SkuModel) i.next());
		}
	}

	private void walk(SkuModel sku) {
		this.visitor.visit(sku);
	}

}
