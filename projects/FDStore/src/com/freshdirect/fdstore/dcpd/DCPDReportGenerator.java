package com.freshdirect.fdstore.dcpd;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspWriter;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryRef;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeSection;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.adapter.OrderPromotionHelper;


/**
 * DCPD Report Generator
 * 
 * Generates DCPD report to HTML or CSV format
 * 
 * @author segabor
 *
 */
public class DCPDReportGenerator {
	JspWriter out;
	DCPDReportQueryContext ctx;
	
	String nodeSeparator;


	public void setNodeSeparator(String nodeSeparator) {
		this.nodeSeparator = nodeSeparator;
	}


	public DCPDReportGenerator(JspWriter out, DCPDReportQueryContext ctx) {
		this.out = out;
		this.ctx = ctx;
	}


	/**
	 * Transforms a collection to a @{link String} by joining members and separators one by one 
	 * @param s {@link AbstractCollection} collection
	 * @return String
	 */
	private String join(Object s[]) {
		String delimiter = ctx.getDelimiter();

		if (s.length == 0) {
			return "";
		} else if (s.length == 1) {
			return s[0].toString();
		} else {
	        StringBuffer buffer = new StringBuffer(s[0].toString());
	        for (int i=1; i<s.length; i++) {
	            buffer.append(delimiter);
	            buffer.append(s[i]);
	        }
	        return buffer.toString();
		}
    }


	
	private void printToCSV(Object s[]) throws IOException {
		out.println(join(s));
    }

	private String quoted(String str) {
		return "\"" + str + "\"";
	}
	

	private String getNodeStyle(ContentNodeModel node, int level) {
		boolean isFoundNode = ctx.getQuery().getNodes().contains(node);
		return "'" + (isFoundNode ? "color: #960; font-weight: bold; " : "") + "padding-left: " + (level*15) + "px'";
	}



	public void generate(List nodes) throws IOException, FDResourceException, FDSkuNotFoundException {
		if (ctx.isRenderCSV()) {
		    // write header
	        if (ctx.isProductsOnlyView()) {
				printToCSV(new Object[]{
					"Available", quoted("Product / Folder ID"), quoted("Full Name"), "SKU", "Rating", "Material", "Eligible","Price","BasePrice","IsDeal"
				});
	            /// out.println("Available;\"Product / Folder ID\";\"Full Name\";SKU;Material;Eligible");
	        } else {
				printToCSV(new Object[]{
					"Depth", "Type", "Flag", "Available", quoted("Product / Folder ID"), quoted("Full Name"), "SKU", "Rating", "Material", "Eligible","Price","BasePrice","IsDeal"
				});
	        }
		}



		// output found items	    
		Iterator it=nodes.iterator();
	    while(it.hasNext()) {
	        ContentNodeModel rootNode = (ContentNodeModel) it.next();
	        
	        if (rootNode instanceof DepartmentModel) {
	        	renderDepartmentNode( (DepartmentModel) rootNode, 1);
	        } else if (rootNode instanceof CategoryModel) {
	        	renderCategoryNode( (CategoryModel) rootNode, 1);
	        } else if (rootNode instanceof Recipe) {
	            renderRecipeNode( (Recipe) rootNode, 1);
	        } else {
	            System.out.println("Nothing to do with " + rootNode.getClass() + "/" + rootNode);
	        }
	        
	        if (!ctx.isRenderCSV() && nodeSeparator != null) {
	        	out.println(nodeSeparator);
	        }
	    }
	}





	public void renderDepartmentNode(DepartmentModel deptNode, int level) throws IOException, FDResourceException, FDSkuNotFoundException {
		// I. RENDER DEPARTMENT
		if (!ctx.isProductsOnlyView()) {
			if (ctx.isRenderCSV()) {
				printToCSV(new Object[]{
					Integer.toString(level), "D", "", "", quoted(deptNode.getContentName()), quoted(deptNode.getFullName()), "", "", "","","",""
				});
		        /// out.println(level + ";D;;;" + deptNode.getContentName() + ";" + deptNode.getFullName() + ";;"); 
		    } else {
				out.println("<tr>");
				out.println("<td style=" + getNodeStyle(deptNode, level) + ">D: " + deptNode.getContentName() + "</td>");
			    out.println("<td>" + deptNode.getFullName() + "</td>");
				out.println("<td>&nbsp;</td>");
				out.println("<td>&nbsp;</td>");
				out.println("<td>&nbsp;</td>");
				out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");				
				out.println("</tr>");
			}
		}
		++level;

		// II. ITERATE CATEGORIES
		Iterator cit = deptNode.getCategories().iterator();
		while(cit.hasNext()) {
			CategoryModel catNode = (CategoryModel) cit.next();
			renderCategoryNode(catNode, level);
		}
	}


	
	public void renderCategoryNode(CategoryModel catNode, int level) throws IOException, FDResourceException, FDSkuNotFoundException {
		// II. ITERATE SUBCATEGORIES
		// I. RENDER DEPARTMENT
		ContentKey alias = catNode.getAliasAttributeValue();
		
	    if (!ctx.isProductsOnlyView()) {
			if (ctx.isRenderCSV()) {
				printToCSV(new Object[]{
					Integer.toString(level), "C", "", "", quoted(catNode.getContentName()), quoted(catNode.getFullName()), "", "", "","","",""
				});
		        /// out.println(level + ";C;;;\"" + catNode.getContentName() + "\";\"" + catNode.getFullName() + "\";;"); 
			} else {
				out.println("<tr>");
				out.println("<td style=" + getNodeStyle(catNode, level) + ">C: " + catNode.getContentName() + "</td>");
			    out.println("<td>" + catNode.getFullName() + "</td>");
				out.println("<td>&nbsp;</td>");
				out.println("<td>&nbsp;</td>");
				out.println("<td>&nbsp;</td>");
				out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");				
				out.println("</tr>");
			}
	    }
		// I. RENDER CATEGORY
		++level;
		
	    // RENDER ALIAS GROUP
	    if (alias != null) {
	        com.freshdirect.cms.ContentNodeI ct = alias.getContentNode();
	        if (!ctx.isProductsOnlyView()) {
		        if (ctx.isRenderCSV()) {
					printToCSV(new Object[]{
						Integer.toString(level), "C", "A", "", quoted(alias.getId()), quoted(ct.getLabel()), "", "", "","","","",""
					});
		            /// out.println(level + ";C;A;;\"" + alias.getId() + "\";\"" + ct.getLabel() + "\";;"); 
		        } else {
			        out.println("<tr>");
			        out.println("<td style='padding-left: " + (level*15) + "px'>C: " + alias.getId() + " (A)</td>");
			        out.println("<td>" + ct.getLabel() + "</td>");
			        out.println("<td>&nbsp;</td>");
			        out.println("<td>&nbsp;</td>");
			        out.println("<td>&nbsp;</td>");
			        out.println("<td>&nbsp;</td>");
				    out.println("<td>&nbsp;</td>");
				    out.println("<td>&nbsp;</td>");
				    out.println("<td>&nbsp;</td>");			        
			        out.println("</tr>");
		        }
	        }
	    }

		// I/a. RENDER PRODUCTS
		Iterator pit = catNode.getPrivateProducts().iterator();
		while(pit.hasNext()) {
			ProductModel prodNode = (ProductModel) pit.next();
			renderSKUs(prodNode.getSkus(), prodNode.getContentName(), level, null);
		}
		

		// RENDER VIRTUAL GROUP ITEMS
	    List virtualGroup = catNode.getVirtualGroupRefs();
	    if (virtualGroup != null) {
	    	Iterator vit = virtualGroup.iterator();
	    	while(vit.hasNext()) {
	    		CategoryRef cref = (CategoryRef) vit.next();
	    		CategoryModel vcatNode = cref.getCategory();

	    	    if (!ctx.isProductsOnlyView()) {
		            if (ctx.isRenderCSV()) {
						printToCSV(new Object[]{
							Integer.toString(level), "C", "V", "", quoted(vcatNode.getContentName()), quoted(vcatNode.getFullName()), "", "", "","","",""
						});
		                /// out.println(level + ";C;V;;\"" + vcatNode.getContentName() + "\";\"" + vcatNode.getFullName() + "\";;");
		            } else {
			    	    out.println("<tr>");
			    	    out.println("<td style='padding-left: " + (level*15) + "px'>C: " + vcatNode.getContentName() + " (V)</td>");
			    	    out.println("<td>" + vcatNode.getFullName() + "</td>");
			    	    out.println("<td>&nbsp;</td>");
			    	    out.println("<td>&nbsp;</td>");
			    	    out.println("<td>&nbsp;</td>");
			    	    out.println("<td>&nbsp;</td>");
					    out.println("<td>&nbsp;</td>");
					    out.println("<td>&nbsp;</td>");
					    out.println("<td>&nbsp;</td>");			    	    
			    	    out.println("</tr>");
		            }
	    	    }
	    	}
	    }
		

	    // RENDER SUBCATEGORIES
		Iterator cit = catNode.getSubcategories().iterator();
		while(cit.hasNext()) {
			CategoryModel subCatNode = (CategoryModel) cit.next();
			renderCategoryNode(subCatNode, level);
		}
	}




	public void renderSKUs(List skuNodes, String parentCName, int level, String recipeSourceId) throws IOException, FDResourceException, FDSkuNotFoundException {
	    Iterator sit = skuNodes.iterator();
	    while(sit.hasNext()) {
	        SkuModel skuNode = (SkuModel) sit.next();
	        boolean isUna = skuNode.isUnavailable();
	        
	        // skip unavailable items if flag is on
	        if (ctx.isSkipUnavailableItems() && isUna)
	        	continue;
	        
	        ctx.increaseProductsCounter();
	        
	        final String cs = (isUna ? "color: grey;" : " ");

	        String sku_val;
	        try {
	            if (skuNode.getProduct() != null) {
	            	sku_val = skuNode.getProduct().getMaterial().getMaterialNumber();
	            } else {
	            	sku_val = null;
	            }
	        } catch (FDSkuNotFoundException e) {
	            sku_val = null;            
	        }

	        
	        //Test for DCPD Promo Eligiblity.
			ProductModel prodModel = skuNode.getProductModel();
			boolean result = OrderPromotionHelper.evaluateProductForDCPDPromo(prodModel, new HashSet(ctx.getGoodKeys()));
			if (!result && recipeSourceId != null) {
				//This SKU is part of a Recipe. Evaluate Recipe.
				result = OrderPromotionHelper.isRecipeEligible(recipeSourceId , new HashSet(ctx.getGoodKeys()));
			}
			String eligible = result ? "Yes" : "No";
	        String rating;
	        String price="N/A";
	        String basePrice="N/A";
	        String isDeal="false";
	        try {
	        	
	            if (skuNode.getProductInfo() != null) {
	            	rating = skuNode.getProductInfo().getRating();
	            	price="$"+String.valueOf(skuNode.getProductInfo().getDefaultPrice());
		        	if(skuNode.getProductInfo().getBasePrice()!=0) {
		        		basePrice="$"+String.valueOf(skuNode.getProductInfo().getBasePrice());
		        		isDeal=String.valueOf(skuNode.getProductInfo().hasWasPrice());
		        	}	            	
	            } else {
	            	rating = null;
	            }
	        } catch (FDSkuNotFoundException e) {
	        	rating = null; 
	        	
	        }

			if (ctx.isRenderCSV()) {
			    if (ctx.isProductsOnlyView()) {
					printToCSV(new Object[]{
						(isUna ? "N" : ""), quoted(parentCName), quoted(skuNode.getFullName()), quoted(skuNode.getContentName()), (rating != null && rating.trim().length() > 0 ? rating: ""), quoted(sku_val!=null ? sku_val : "N/A"), eligible,price,basePrice,isDeal
					});
	                /// out.println((isUna ? "N" : "") + ";\"" + parentCName + "\";\"" + skuNode.getFullName() + "\";\"" + skuNode.getContentName() + "\";\"" + (sku_val!=null ? sku_val : "N/A") + "\"");
			    } else {
					printToCSV(new Object[]{
						Integer.toString(level), "P", "", (isUna ? "N" : ""), quoted(parentCName), quoted(skuNode.getFullName()), quoted(skuNode.getContentName()), (rating != null && rating.trim().length() > 0 ? rating: ""), quoted(sku_val!=null ? sku_val : "N/A"), eligible,price,basePrice,isDeal
					});
			    	/// out.println(level + ";P;;" + (isUna ? "N" : "") + ";\"" + parentCName + "\";\"" + skuNode.getFullName() + "\";\"" + skuNode.getContentName() + "\";\"" + (sku_val!=null ? sku_val : "N/A") + "\"");
			    }
	        } else {
	            out.println("<tr>");
	            if (!ctx.isProductsOnlyView()) {
	                out.println("<td style='padding-left: " + (level*15) + "px; "+cs+"'>"+parentCName+"</td>");
	            }
		        out.println("<td style='"+cs+"'>" + skuNode.getFullName() + "</td>");
		        out.println("<td style='"+cs+"'>" + skuNode.getContentName() + "</td>");
		        

		        out.println("<td style='"+cs+"'>" + (rating != null && rating.trim().length() > 0 ? rating: "&nbsp;") + "</td>");
		        out.println("<td style='"+cs+"'>" + (sku_val!=null ? sku_val : "N/A") + "</td>");
	            out.println("<td style='"+cs+"'>" + (eligible!=null ? eligible : "N/A") + "</td>");
	            out.println("<td style='"+cs+"'>" + (price!=null ? price : "N/A") + "</td>");
	            out.println("<td style='"+cs+"'>" + (basePrice!=null ? basePrice : "N/A") + "</td>");
	            out.println("<td style='"+cs+"'>" + (isDeal!=null ? isDeal : "N/A") + "</td>");
	            out.println("</tr>");
	        }
	    }
	}



	public void renderUnavailableProduct(String parentCName, int level) throws IOException, FDResourceException, FDSkuNotFoundException {
	    final String cs = "color: grey;";
	    if (!ctx.isProductsOnlyView()) {
		    if (ctx.isRenderCSV()) {
				printToCSV(new Object[]{
					Integer.toString(level), "P", "", "M", quoted(parentCName), "", "", "", "","","",""
				});
		        /// out.println(level + ";P;;N;\"" + parentCName + "\";;;");
		    } else {
		        out.println("<tr>");
		        out.println("<td style='padding-left: " + (level*15) + "px; "+cs+"'>"+parentCName+"</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");
		        out.println("<td style='"+cs+"'>&nbsp;</td>");		        
		        out.println("</tr>");
		    }
	    }
	}



	public void renderRecipeNode(Recipe recipeNode, int level) throws IOException, FDResourceException, FDSkuNotFoundException {
	    // I. RENDER RECIPE
	    if (!ctx.isProductsOnlyView()) {
		    if (ctx.isRenderCSV()) {
				printToCSV(new Object[]{
					Integer.toString(level), "R", "V", "", quoted(recipeNode.getContentName()), quoted(recipeNode.getFullName()), "", "", "","","",""
				});
		        /// out.println(level + ";R;V;;\"" + recipeNode.getContentName() + "\";\"" + recipeNode.getFullName() + "\";;");
		    } else {
			    out.println("<tr>");
			    out.println("<td style='padding-left: " + (level*15) + "px'>R: " + recipeNode.getContentName() + "</td>");
			    out.println("<td>" + recipeNode.getFullName() + "</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
		        out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
		        out.println("<td>&nbsp;</td>");
			    out.println("</tr>");
		    }
	    }
	    ++level;

	    // II. ITERATE VARIANTS
	    Iterator cit = recipeNode.getVariants().iterator();
	    while(cit.hasNext()) {
	    	RecipeVariant vNode = (RecipeVariant) cit.next();
	        renderVariantNode(vNode, level, recipeNode.getContentName());
	    }
	}



	public void renderVariantNode(RecipeVariant vNode, int level, String recipeSourceId) throws IOException, FDResourceException, FDSkuNotFoundException {
	    // I. RENDER VARIANT
	    if (!ctx.isProductsOnlyView()) {
		    if (ctx.isRenderCSV()) {
				printToCSV(new Object[]{
					Integer.toString(level), "V", "", "", quoted(vNode.getContentName()), "", "", "", "","","",""
				});
		        /// out.println(level + ";V;;;\"" + vNode.getContentName() + "\";;;");
		    } else {
			    out.println("<tr>");
			    out.println("<td style='padding-left: " + (level*15) + "px'>V: " + vNode.getContentName() + "</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");			    
			    out.println("</tr>");
		    }
	    }
	    ++level;

	    // II. ITERATE SECTIONS
	    Iterator cit = vNode.getSections().iterator();
	    while(cit.hasNext()) {
	    	RecipeSection sNode = (RecipeSection) cit.next();
	        renderSectionNode(sNode, level, recipeSourceId);
	    }
	}



	public void renderSectionNode(RecipeSection rNode, int level, String recipeSourceId) throws IOException, FDResourceException, FDSkuNotFoundException {
	    // I. RENDER SECTION
	    if (!ctx.isProductsOnlyView()) {
		    if (ctx.isRenderCSV()) {
				printToCSV(new Object[]{
					Integer.toString(level), "S", "", "", quoted(rNode.getContentName()), "", "", "", "","","",""
				});
		        /// out.println(level + ";S;;;\"" + rNode.getContentName() + "\";;;");
		    } else {
			    out.println("<tr>");
			    out.println("<td style='padding-left: " + (level*15) + "px'>S: " + rNode.getContentName() + "</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");
			    out.println("<td>&nbsp;</td>");			    
			    out.println("</tr>");
		    }
	    }    
	    ++level;

	    // II. ITERATE SKUs
	    Iterator cit = rNode.getIngredients().iterator();
	    while(cit.hasNext()) {
	        ConfiguredProduct cpNode = (ConfiguredProduct) cit.next();
	        if (cpNode.isUnavailable()) {
	        	if (!ctx.isSkipUnavailableItems()) {
	        	    renderUnavailableProduct(cpNode.getContentName(), level);
	        	}
	        } else {
	            renderSKUs(cpNode.getSkus(), cpNode.getContentName(), level, recipeSourceId);
	        }
	    }
	}




	public JspWriter getWriter() {
		return out;
	}


	public DCPDReportQueryContext getContext() {
		return ctx;
	}
}
