package com.freshdirect.fdstore.content.productfeed.taxonomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.fdstore.content.ContentFactory;

public class TaxonomyFeedCreator {

    private static final TaxonomyFeedCreator INSTANCE = new TaxonomyFeedCreator();

    public static TaxonomyFeedCreator getInstance() {
        return INSTANCE;
    }

    public Store createTaxonomyFeed() {

        Set<ContentKey> departments = ContentFactory.getInstance().getContentKeysByType(ContentType.get("Department"));
        Set<ContentKey> categories = ContentFactory.getInstance().getContentKeysByType(ContentType.get("Category"));
        Set<ContentKey> products = ContentFactory.getInstance().getContentKeysByType(ContentType.get("Product"));

        Map<ContentKey, ContentNodeI> allNodes = ContentFactory.getInstance().getContentNodes(departments);
        ;
        allNodes.putAll(ContentFactory.getInstance().getContentNodes(categories));
        allNodes.putAll(ContentFactory.getInstance().getContentNodes(products));

        Store storeTaxonomy = new Store();

        List<Department> taxonomyDepartments = new ArrayList<Department>();

        for (ContentKey department : departments) {
            String departmentName = allNodes.get(department).getAttributeValue("FULL_NAME") == null ? "" : allNodes.get(department).getAttributeValue("FULL_NAME").toString();
            
            Department actualDepartment = new Department();
            actualDepartment.setDepartmentId(allNodes.get(department).getKey().getId());
            actualDepartment.setDepartmentName(StringEscapeUtils.unescapeHtml(departmentName));

            for (ContentKey child : allNodes.get(department).getChildKeys()) {
                actualDepartment.getCategory().add(populateCategoryTaxonomyInfo(allNodes, allNodes.get(child)));
            }

            taxonomyDepartments.add(actualDepartment);
        }
        storeTaxonomy.getDepartment().addAll(taxonomyDepartments);
        return storeTaxonomy;
    }

    private Category populateCategoryTaxonomyInfo(Map<ContentKey, ContentNodeI> allNodes, ContentNodeI categoryNode) {
        Category taxonomyCategory = new Category();
        if (categoryNode != null) {
            String name = categoryNode.getAttributeValue("FULL_NAME") == null ? "" : categoryNode.getAttributeValue("FULL_NAME").toString();
            String keyword = categoryNode.getAttributeValue("KEYWORDS") == null ? "" : categoryNode.getAttributeValue("KEYWORDS").toString();
            
            taxonomyCategory.setCategoryId(categoryNode.getKey().getId());
            taxonomyCategory.setCategoryName(StringEscapeUtils.unescapeHtml(name));
            taxonomyCategory.setKeywords(keyword);

            for (ContentKey child : categoryNode.getChildKeys()) {
                if (child.getType().getName().equals("Product")) {
                    taxonomyCategory.getProduct().add(populateProductTaxonomyInfo(allNodes, allNodes.get(child), categoryNode));
                } else if (child.getType().getName().equals("Category")) {
                    taxonomyCategory.getSubcategory().add(populateCategoryTaxonomyInfo(allNodes, allNodes.get(child)));
                }
            }
        }
        return taxonomyCategory;
    }

    private Product populateProductTaxonomyInfo(Map<ContentKey, ContentNodeI> allNodes, ContentNodeI productNode, ContentNodeI parentCategory) {

        Product taxonomyProduct = new Product();
        if (productNode != null) {
            String prodKeyword = productNode.getAttributeValue("KEYWORDS") == null ? "" : productNode.getAttributeValue("KEYWORDS").toString();
            taxonomyProduct.setKeywords(prodKeyword);
            taxonomyProduct.setProductId(productNode.getKey().getId());
            String prodName = productNode.getAttributeValue("FULL_NAME") == null ? "" : productNode.getAttributeValue("FULL_NAME").toString();
            taxonomyProduct.setProductName(StringEscapeUtils.unescapeHtml(prodName));

            ContentKey primaryHomeCategory = CmsManager.getInstance().getPrimaryHomeKey(productNode.getKey(), DraftContext.MAIN);
            taxonomyProduct.setPrimaryHome(primaryHomeCategory.equals(parentCategory.getKey()));
        }
        return taxonomyProduct;
    }

}
