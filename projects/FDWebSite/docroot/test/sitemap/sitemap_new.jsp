<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.fdstore.content.ProductContainer"%>

<%!
public static class Data {
	String name;
	String id;
	int countAll;
	List<Data> children = new ArrayList<Data>();
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		sb.append("id:\"").append(id).append("\",\n");
		sb.append("name:\"").append(name == null ? "" : name.replace("\"", "\\\"")).append("\",\n");;
		sb.append("countAll:").append(countAll).append(",\n");;
		sb.append("children: [\n");
		boolean first = true;
		for (Data childData : children){
			if (first){
				first = false;
			} else { 
				sb.append(",");
			}
			
			sb.append(childData);
		}
		sb.append("]\n");
		sb.append("}").append("\n");
		return sb.toString();
	}
}


private void process(Data parentData, ProductContainer container) {
	Data data = new Data();
	parentData.children.add(data);
	
	data.id = container.getContentName();
	data.name = container.getFullName();
	
	if (container instanceof CategoryModel){
		CategoryModel cat = (CategoryModel) container;
		data.countAll = cat.getProducts().size();
	}
	
	for (CategoryModel subCat : container.getSubcategories()){
		process(data, subCat);
	}
}
%>

<%
Data data = new Data();
for (DepartmentModel dep: ContentFactory.getInstance().getStore().getDepartments()){
  process(data, dep);
}
%>
<script>data = <%=data%></script>