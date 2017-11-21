package com.freshdirect.storeapi.content;

public interface ContentVisitorI {

	public void visit(StoreModel store);
	public void visit(DepartmentModel dept);
	public void visit(CategoryModel cat);
	public void visit(ProductModel prod);
	public void visit(SkuModel sku);

}
