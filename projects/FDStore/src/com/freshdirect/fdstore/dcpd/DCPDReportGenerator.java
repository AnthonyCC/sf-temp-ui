package com.freshdirect.fdstore.dcpd;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.customer.adapter.OrderPromotionHelper;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ConfiguredProduct;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.RecipeSection;
import com.freshdirect.storeapi.content.RecipeVariant;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.storeapi.util.ProductInfoUtil;

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

    private void printToCSV(String s[]) throws IOException {
        out.println(StringUtils.join(s, ctx.getDelimiter()));
    }

    private String quoted(String str) {
        if (str == null) {
            str = "";
        }
        // escape quotes already in string
        return "\"" + str.replaceAll("\"", "\"\"") + "\"";
    }

    private String getNodeStyle(ContentNodeModel node, int level) {
        boolean isFoundNode = ctx.getQuery().getNodes().contains(node);
        return "'" + (isFoundNode ? "color: #960; font-weight: bold; " : "") + "padding-left: " + (level * 15) + "px'";
    }

    public void generate(List<ContentNodeModel> nodes) throws IOException, FDResourceException, FDSkuNotFoundException {
        if (ctx.isRenderCSV()) {
            // write header
            if (ctx.isProductsOnlyView()) {
                printToCSV(new String[] { "Available", quoted("Product / Folder ID"), quoted("Full Name"), "SKU", "Rating", "Sustainability Rating", "Material", "Eligible",
                        "Price", "BasePrice", "IsDeal" });
                /// out.println("Available;\"Product / Folder ID\";\"Full Name\";SKU;Material;Eligible");
            } else {
                printToCSV(new String[] { "Depth", "Type", "Flag", "Available", quoted("Product / Folder ID"), quoted("Full Name"), "SKU", "Rating", "Sustainability Rating",
                        "Material", "Eligible", "Price", "BasePrice", "IsDeal" });
            }
        }

        // output found items
        Iterator<ContentNodeModel> it = nodes.iterator();
        while (it.hasNext()) {
            ContentNodeModel rootNode = it.next();

            if (rootNode instanceof DepartmentModel) {
                renderDepartmentNode((DepartmentModel) rootNode, 1);
            } else if (rootNode instanceof CategoryModel) {
                renderCategoryNode((CategoryModel) rootNode, 1);
            } else if (rootNode instanceof Recipe) {
                renderRecipeNode(UserContext.createUserContext(CmsManager.getInstance().getEStoreEnum()), (Recipe) rootNode, 1);
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
                printToCSV(new String[] { Integer.toString(level), "D", "", "", quoted(deptNode.getContentName()), quoted(deptNode.getFullName()), "", "", "", "", "", "" });
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
        Iterator<CategoryModel> cit = deptNode.getCategories().iterator();
        while (cit.hasNext()) {
            CategoryModel catNode = cit.next();
            renderCategoryNode(catNode, level);
        }
    }

    public void renderCategoryNode(CategoryModel catNode, int level) throws IOException, FDResourceException, FDSkuNotFoundException {
        // II. ITERATE SUBCATEGORIES
        // I. RENDER DEPARTMENT
        ContentKey alias = catNode.getAliasAttributeValue();

        if (!ctx.isProductsOnlyView()) {
            if (ctx.isRenderCSV()) {
                printToCSV(new String[] { Integer.toString(level), "C", "", "", quoted(catNode.getContentName()), quoted(catNode.getFullName()), "", "", "", "", "", "" });
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
            ContentNodeI ct = CmsManager.getInstance().getContentNode(alias);
            if (!ctx.isProductsOnlyView()) {
                if (ctx.isRenderCSV()) {
                    printToCSV(new String[] { Integer.toString(level), "C", "A", "", quoted(alias.getId()), quoted(ct.getLabel()), "", "", "", "", "", "", "" });
                    /// out.println(level + ";C;A;;\"" + alias.getId() + "\";\"" + ct.getLabel() + "\";;");
                } else {
                    out.println("<tr>");
                    out.println("<td style='padding-left: " + (level * 15) + "px'>C: " + alias.getId() + " (A)</td>");
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
        for (ProductModel prodNode : catNode.getPrivateProducts()) {
            renderSKUs(UserContext.createUserContext(CmsManager.getInstance().getEStoreEnum()), prodNode, level, null);
        }

        // RENDER VIRTUAL GROUP ITEMS
        List<CategoryModel> virtualGroup = catNode.getVirtualGroupRefs();
        if (virtualGroup != null) {
            Iterator<CategoryModel> vit = virtualGroup.iterator();
            while (vit.hasNext()) {
                CategoryModel vcatNode = vit.next();

                if (!ctx.isProductsOnlyView()) {
                    if (ctx.isRenderCSV()) {
                        printToCSV(
                                new String[] { Integer.toString(level), "C", "V", "", quoted(vcatNode.getContentName()), quoted(vcatNode.getFullName()), "", "", "", "", "", "" });
                        /// out.println(level + ";C;V;;\"" + vcatNode.getContentName() + "\";\"" + vcatNode.getFullName() + "\";;");
                    } else {
                        out.println("<tr>");
                        out.println("<td style='padding-left: " + (level * 15) + "px'>C: " + vcatNode.getContentName() + " (V)</td>");
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
        Iterator<CategoryModel> cit = catNode.getSubcategories().iterator();
        while (cit.hasNext()) {
            CategoryModel subCatNode = cit.next();
            renderCategoryNode(subCatNode, level);
        }
    }

    public void renderSKUs(UserContext userCtx, ProductModel productModel, int level, String recipeSourceId) throws IOException, FDResourceException, FDSkuNotFoundException {

        String parentCName = productModel.getContentName();
        List<SkuModel> skuNodes = productModel.getSkus();

        Iterator<SkuModel> sit = skuNodes.iterator();
        while (sit.hasNext()) {
            SkuModel skuNode = sit.next();
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

            // Test for DCPD Promo Eligiblity.
            ProductModel prodModel = skuNode.getProductModel();
            boolean result = OrderPromotionHelper.evaluateProductForDCPDPromo(prodModel, new HashSet<ContentKey>(ctx.getGoodKeys()));
            if (!result && recipeSourceId != null) {
                // This SKU is part of a Recipe. Evaluate Recipe.
                result = OrderPromotionHelper.isRecipeEligible(recipeSourceId, new HashSet<ContentKey>(ctx.getGoodKeys()));
            }
            String eligible = result ? "Yes" : "No";
            EnumOrderLineRating rating;
            String price = "N/A";
            String basePrice = "N/A";
            String isDeal = "false";
            EnumSustainabilityRating sustainabilityRating = null;
            try {

                final FDProductInfo productInfo = skuNode.getProductInfo();
                if (productInfo != null) {
                    // rating = skuNode.getProductInfo().getRating(userCtx.getFulfillmentContext().getPlantId());
                    // sustainabilityRating=skuNode.getProductInfo().getSustainabilityRating(userCtx.getFulfillmentContext().getPlantId());
                    final String pickingPlantId = ProductInfoUtil.getPickingPlantId(productInfo);
                    final ZonePriceInfoModel zonePriceInfo = productInfo.getZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO);

                    rating = productInfo.getRating(pickingPlantId);
                    sustainabilityRating = productInfo.getSustainabilityRating(pickingPlantId);
                    price = "$" + String.valueOf(zonePriceInfo.getDefaultPrice());
                    if (zonePriceInfo.getSellingPrice() != 0) {
                        basePrice = "$" + String.valueOf(zonePriceInfo.getSellingPrice());
                        isDeal = String.valueOf(zonePriceInfo.isItemOnSale());
                    }
                } else {
                    rating = null;
                }
            } catch (FDSkuNotFoundException e) {
                rating = null;

            }

            if (ctx.isRenderCSV()) {
                if (ctx.isProductsOnlyView()) {
                    printToCSV(new String[] { (isUna ? "N" : ""), quoted(parentCName), quoted(productModel.getFullName()), quoted(skuNode.getContentName()),
                            (rating != null ? rating.getStatusCode() : ""), (sustainabilityRating != null ? sustainabilityRating.getStatusCode() : ""),
                            quoted(sku_val != null ? sku_val : "N/A"), eligible, price, basePrice, isDeal });
                    /// out.println((isUna ? "N" : "") + ";\"" + parentCName + "\";\"" + skuNode.getFullName() + "\";\"" + skuNode.getContentName() + "\";\"" + (sku_val!=null ?
                    /// sku_val : "N/A") + "\"");
                } else {
                    printToCSV(new String[] { Integer.toString(level), "P", "", (isUna ? "N" : ""), quoted(parentCName), quoted(productModel.getFullName()),
                            quoted(skuNode.getContentName()), (rating != null ? rating.getStatusCode() : ""),
                            (sustainabilityRating != null ? sustainabilityRating.getStatusCode() : ""), quoted(sku_val != null ? sku_val : "N/A"), eligible, price, basePrice,
                            isDeal });
                    /// out.println(level + ";P;;" + (isUna ? "N" : "") + ";\"" + parentCName + "\";\"" + skuNode.getFullName() + "\";\"" + skuNode.getContentName() + "\";\"" +
                    /// (sku_val!=null ? sku_val : "N/A") + "\"");
                }
            } else {
                out.println("<tr>");
                if (!ctx.isProductsOnlyView()) {
                    out.println("<td style='padding-left: " + (level * 15) + "px; " + cs + "'>" + parentCName + "</td>");
                }
                out.println("<td style='" + cs + "'>" + productModel.getFullName() + "</td>");
                out.println("<td style='" + cs + "'>" + skuNode.getContentName() + "</td>");

                out.println("<td style='" + cs + "'>" + (rating != null ? rating.getStatusCode() : "&nbsp;") + "</td>");
                out.println("<td style='" + cs + "'>" + (sustainabilityRating != null ? sustainabilityRating.getStatusCode() : "&nbsp;") + "</td>");
                out.println("<td style='" + cs + "'>" + (sku_val != null ? sku_val : "N/A") + "</td>");
                out.println("<td style='" + cs + "'>" + (eligible != null ? eligible : "N/A") + "</td>");
                out.println("<td style='" + cs + "'>" + (price != null ? price : "N/A") + "</td>");
                out.println("<td style='" + cs + "'>" + (basePrice != null ? basePrice : "N/A") + "</td>");
                out.println("<td style='" + cs + "'>" + (isDeal != null ? isDeal : "N/A") + "</td>");
                out.println("</tr>");
            }
        }
    }

    public void renderUnavailableProduct(String parentCName, int level) throws IOException, FDResourceException, FDSkuNotFoundException {
        final String cs = "color: grey;";
        if (!ctx.isProductsOnlyView()) {
            if (ctx.isRenderCSV()) {
                printToCSV(new String[] { Integer.toString(level), "P", "", "M", quoted(parentCName), "", "", "", "", "", "", "" });
                /// out.println(level + ";P;;N;\"" + parentCName + "\";;;");
            } else {
                out.println("<tr>");
                out.println("<td style='padding-left: " + (level * 15) + "px; " + cs + "'>" + parentCName + "</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("<td style='" + cs + "'>&nbsp;</td>");
                out.println("</tr>");
            }
        }
    }

    public void renderRecipeNode(UserContext userCtx, Recipe recipeNode, int level) throws IOException, FDResourceException, FDSkuNotFoundException {
        // I. RENDER RECIPE
        if (!ctx.isProductsOnlyView()) {
            if (ctx.isRenderCSV()) {
                printToCSV(new String[] { Integer.toString(level), "R", "V", "", quoted(recipeNode.getContentName()), quoted(recipeNode.getFullName()), "", "", "", "", "", "" });
                /// out.println(level + ";R;V;;\"" + recipeNode.getContentName() + "\";\"" + recipeNode.getFullName() + "\";;");
            } else {
                out.println("<tr>");
                out.println("<td style='padding-left: " + (level * 15) + "px'>R: " + recipeNode.getContentName() + "</td>");
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
        Iterator<RecipeVariant> cit = recipeNode.getVariants().iterator();
        while (cit.hasNext()) {
            RecipeVariant vNode = cit.next();
            renderVariantNode(userCtx, vNode, level, recipeNode.getContentName());
        }
    }

    public void renderVariantNode(UserContext userCtx, RecipeVariant vNode, int level, String recipeSourceId) throws IOException, FDResourceException, FDSkuNotFoundException {
        // I. RENDER VARIANT
        if (!ctx.isProductsOnlyView()) {
            if (ctx.isRenderCSV()) {
                printToCSV(new String[] { Integer.toString(level), "V", "", "", quoted(vNode.getContentName()), "", "", "", "", "", "", "" });
                /// out.println(level + ";V;;;\"" + vNode.getContentName() + "\";;;");
            } else {
                out.println("<tr>");
                out.println("<td style='padding-left: " + (level * 15) + "px'>V: " + vNode.getContentName() + "</td>");
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
        Iterator<RecipeSection> cit = vNode.getSections().iterator();
        while (cit.hasNext()) {
            RecipeSection sNode = cit.next();
            renderSectionNode(userCtx, sNode, level, recipeSourceId);
        }
    }

    public void renderSectionNode(UserContext userCtx, RecipeSection rNode, int level, String recipeSourceId) throws IOException, FDResourceException, FDSkuNotFoundException {
        // I. RENDER SECTION
        if (!ctx.isProductsOnlyView()) {
            if (ctx.isRenderCSV()) {
                printToCSV(new String[] { Integer.toString(level), "S", "", "", quoted(rNode.getContentName()), "", "", "", "", "", "", "" });
                /// out.println(level + ";S;;;\"" + rNode.getContentName() + "\";;;");
            } else {
                out.println("<tr>");
                out.println("<td style='padding-left: " + (level * 15) + "px'>S: " + rNode.getContentName() + "</td>");
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
        Iterator<ConfiguredProduct> cit = rNode.getIngredients().iterator();
        while (cit.hasNext()) {
            ConfiguredProduct cpNode = cit.next();
            if (cpNode.isUnavailable()) {
                if (!ctx.isSkipUnavailableItems()) {
                    renderUnavailableProduct(cpNode.getContentName(), level);
                }
            } else {
                renderSKUs(userCtx, cpNode, level, recipeSourceId);
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
