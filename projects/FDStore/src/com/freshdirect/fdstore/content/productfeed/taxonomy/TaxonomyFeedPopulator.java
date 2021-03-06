package com.freshdirect.fdstore.content.productfeed.taxonomy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductGrabberModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.grabber.GrabberServiceI;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

public class TaxonomyFeedPopulator {

    private static final TaxonomyFeedPopulator INSTANCE = new TaxonomyFeedPopulator();

    public static TaxonomyFeedPopulator getInstance() {
        return INSTANCE;
    }

    public StoreTaxonomyFeedElement populateStoreTaxonomyFeed() {
        CmsManager cmsManager = CmsManager.getInstance();

        Set<ContentKey> departments = cmsManager.getContentKeysByType(FDContentTypes.DEPARTMENT);
        Set<ContentKey> categories = cmsManager.getContentKeysByType(FDContentTypes.CATEGORY);
        Set<ContentKey> products = cmsManager.getContentKeysByType(FDContentTypes.PRODUCT);

        Map<ContentKey, ContentNodeI> allNodes = cmsManager.getContentNodes(departments);

        allNodes.putAll(cmsManager.getContentNodes(categories));
        allNodes.putAll(cmsManager.getContentNodes(products));

        StoreTaxonomyFeedElement storeTaxonomy = new StoreTaxonomyFeedElement();

        List<DepartmentTaxonomyFeedElement> departmentElements = new ArrayList<DepartmentTaxonomyFeedElement>();

        for (ContentKey department : departments) {
            String departmentName = allNodes.get(department).getAttributeValue("FULL_NAME") == null ? "" : allNodes.get(department).getAttributeValue("FULL_NAME").toString();
            String departmentCatalog = allNodes.get(department).getAttributeValue("catalog") == null ? "" : allNodes.get(department).getAttributeValue("catalog").toString();

            DepartmentTaxonomyFeedElement actualDepartmentElement = new DepartmentTaxonomyFeedElement();
            actualDepartmentElement.setDepartmentId(allNodes.get(department).getKey().getId());
            actualDepartmentElement.setDepartmentName(StringEscapeUtils.unescapeHtml(departmentName));
            actualDepartmentElement.setDepartmentCatalog(departmentCatalog);

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
                } else if (child.getType().equals(FDContentTypes.PRODUCTGRABBER)) {
                    GrabberServiceI grabber = ContentFactory.getInstance().getProductGrabberService();
                    if (grabber != null) {
                        ProductGrabberModel grabberModel = (ProductGrabberModel) ContentFactory.getInstance().getContentNodeByKey(child);
                        Collection<ProductModel> grabbedProducts = grabber.getProducts(grabberModel);
                        for (ProductModel productModel : grabbedProducts) {
                            ProductTaxonomyFeedElement childProductElement = populateProductTaxonomyFeedElementTaxonomyInfo(allNodes, allNodes.get(productModel.getContentKey()),
                                    categoryNode);
                            categoryElement.getProduct().add(childProductElement);
                        }
                    }

                }
            }

            populateProductTaxonomyFeedElementTaxanomyInfoFromVirtualGroup(allNodes, categoryNode, categoryElement);
        }

        return categoryElement;
    }

    private void populateProductTaxonomyFeedElementTaxanomyInfoFromVirtualGroup(Map<ContentKey, ContentNodeI> allNodes, ContentNodeI categoryNode,
            CategoryTaxonomyFeedElement categoryElement) {
        Object virtualCategories = categoryNode.getAttributeValue("VIRTUAL_GROUP");
        if (virtualCategories != null && (virtualCategories instanceof List<?>)) {
            List<ContentKey> referedCategories = (List<ContentKey>) virtualCategories;
            for (ContentKey referedCategory : referedCategories) {
                ContentNodeI virtualCategoryContentNode = CmsManager.getInstance().getContentNode(referedCategory);

                if (virtualCategoryContentNode != null) {
                    Object referedCategoryProducts = virtualCategoryContentNode.getAttributeValue("products");

                    if (referedCategoryProducts instanceof List<?>) {
                        for (ContentKey productContentKey : (List<ContentKey>) referedCategoryProducts) {
                            ProductTaxonomyFeedElement childProductElement = populateProductTaxonomyFeedElementTaxonomyInfo(allNodes, allNodes.get(productContentKey), categoryNode);
                            categoryElement.getProduct().add(childProductElement);
                        }
                    }

                    if (virtualCategoryContentNode.getAttributeValue("VIRTUAL_GROUP") instanceof List<?>) {
                        populateProductTaxonomyFeedElementTaxanomyInfoFromVirtualGroup(allNodes, virtualCategoryContentNode, categoryElement);
                    }
                }
            }
        }
    }

    private ProductTaxonomyFeedElement populateProductTaxonomyFeedElementTaxonomyInfo(Map<ContentKey, ContentNodeI> allNodes, ContentNodeI productNode,
            ContentNodeI parentCategory) {

        ProductTaxonomyFeedElement productElement = new ProductTaxonomyFeedElement();
        if (productNode != null) {
            String prodKeyword = productNode.getAttributeValue("KEYWORDS") == null ? "" : productNode.getAttributeValue("KEYWORDS").toString();
            String prodName = productNode.getAttributeValue("FULL_NAME") == null ? "" : productNode.getAttributeValue("FULL_NAME").toString();
            ContentKey primaryHomeCategoryKey = CmsManager.getInstance().getPrimaryHomeKey(productNode.getKey());

            productElement.setKeywords(prodKeyword);
            productElement.setProductId(productNode.getKey().getId());
            productElement.setProductName(StringEscapeUtils.unescapeHtml(prodName));
            productElement.setPrimaryHome(primaryHomeCategoryKey.equals(parentCategory.getKey()));
        }

        return productElement;
    }

}
