package com.freshdirect.fdstore.content.productfeed.taxonomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class TaxonomyFeedPopulator {

    private static final TaxonomyFeedPopulator INSTANCE = new TaxonomyFeedPopulator();

    public static TaxonomyFeedPopulator getInstance() {
        return INSTANCE;
    }

    public StoreTaxonomyFeedElement populateStoreTaxonomyFeed() {
        CmsManager cmsManager = CmsManager.getInstance();

        Set<ContentKey> departments = cmsManager.getContentKeysByType(FDContentTypes.DEPARTMENT, DraftContext.MAIN);
        Set<ContentKey> categories = cmsManager.getContentKeysByType(FDContentTypes.CATEGORY, DraftContext.MAIN);
        Set<ContentKey> products = cmsManager.getContentKeysByType(FDContentTypes.PRODUCT, DraftContext.MAIN);

        Map<ContentKey, ContentNodeI> allNodes = cmsManager.getContentNodes(departments, DraftContext.MAIN);

        allNodes.putAll(cmsManager.getContentNodes(categories, DraftContext.MAIN));
        allNodes.putAll(cmsManager.getContentNodes(products, DraftContext.MAIN));

        StoreTaxonomyFeedElement storeTaxonomy = new StoreTaxonomyFeedElement();

        List<DepartmentTaxonomyFeedElement> departmentElements = new ArrayList<DepartmentTaxonomyFeedElement>();

        for (ContentKey department : departments) {
            String departmentName = allNodes.get(department).getAttributeValue("FULL_NAME") == null ? "" : allNodes.get(department).getAttributeValue("FULL_NAME").toString();

            DepartmentTaxonomyFeedElement actualDepartmentElement = new DepartmentTaxonomyFeedElement();
            actualDepartmentElement.setDepartmentId(allNodes.get(department).getKey().getId());
            actualDepartmentElement.setDepartmentName(StringEscapeUtils.unescapeHtml(departmentName));

            for (ContentKey child : allNodes.get(department).getChildKeys()) {
                CategoryTaxonomyFeedElement childCategoryElement = populateCategoryTaxonomyFeedElementTaxonomyInfo(allNodes, allNodes.get(child));
                actualDepartmentElement.getCategory().add(childCategoryElement);
            }

            departmentElements.add(actualDepartmentElement);
        }
        storeTaxonomy.getDepartment().addAll(departmentElements);
        return storeTaxonomy;
    }

    private CategoryTaxonomyFeedElement populateCategoryTaxonomyFeedElementTaxonomyInfo(Map<ContentKey, ContentNodeI> allNodes, ContentNodeI categoryNode) {
        CategoryTaxonomyFeedElement categoryElement = new CategoryTaxonomyFeedElement();
        if (categoryNode != null) {
            String name = categoryNode.getAttributeValue("FULL_NAME") == null ? "" : categoryNode.getAttributeValue("FULL_NAME").toString();
            String keyword = categoryNode.getAttributeValue("KEYWORDS") == null ? "" : categoryNode.getAttributeValue("KEYWORDS").toString();

            categoryElement.setCategoryId(categoryNode.getKey().getId());
            categoryElement.setCategoryName(StringEscapeUtils.unescapeHtml(name));
            categoryElement.setKeywords(keyword);

            for (ContentKey child : categoryNode.getChildKeys()) {
                if (child.getType().equals(FDContentTypes.PRODUCT)) {
                    ProductTaxonomyFeedElement childProductElement = populateProductTaxonomyFeedElementTaxonomyInfo(allNodes, allNodes.get(child), categoryNode);
                    categoryElement.getProduct().add(childProductElement);
                } else if (child.getType().equals(FDContentTypes.CATEGORY)) {
                    CategoryTaxonomyFeedElement subCategoryElement = populateCategoryTaxonomyFeedElementTaxonomyInfo(allNodes, allNodes.get(child));
                    categoryElement.getSubcategory().add(subCategoryElement);
                }
            }
        }

        return categoryElement;
    }

    private ProductTaxonomyFeedElement populateProductTaxonomyFeedElementTaxonomyInfo(Map<ContentKey, ContentNodeI> allNodes, ContentNodeI productNode,
            ContentNodeI parentCategory) {

        ProductTaxonomyFeedElement productElement = new ProductTaxonomyFeedElement();
        if (productNode != null) {
            String prodKeyword = productNode.getAttributeValue("KEYWORDS") == null ? "" : productNode.getAttributeValue("KEYWORDS").toString();
            String prodName = productNode.getAttributeValue("FULL_NAME") == null ? "" : productNode.getAttributeValue("FULL_NAME").toString();
            ContentKey primaryHomeCategoryKey = CmsManager.getInstance().getPrimaryHomeKey(productNode.getKey(), DraftContext.MAIN);

            productElement.setKeywords(prodKeyword);
            productElement.setProductId(productNode.getKey().getId());
            productElement.setProductName(StringEscapeUtils.unescapeHtml(prodName));
            productElement.setPrimaryHome(primaryHomeCategoryKey.equals(parentCategory.getKey()));
        }

        return productElement;
    }

}
