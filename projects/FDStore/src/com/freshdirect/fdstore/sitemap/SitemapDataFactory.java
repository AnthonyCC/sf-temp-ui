package com.freshdirect.fdstore.sitemap;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.ProductContainer;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SuperDepartmentModel;

public class SitemapDataFactory {

    private static final Category LOGGER = LoggerFactory.getInstance(SitemapDataFactory.class);
    private static SitemapData cache;

    public static synchronized SitemapData create() {
        if (FDStoreProperties.isSiteMapEnabled()) {
            if (cache == null) {
                cache = processStore();
            }
        } else {
            cache = null;
        }
        return cache;
    }

    public static void evictSitemapData() {
        cache = null;
    }

    private static SitemapData processStore() {
        LOGGER.info("Load start");
        Date startTime = new Date();

        boolean isQuick = false;
        SitemapData rootData = new SitemapData();

        Set<DepartmentModel> processedDepartments = new HashSet<DepartmentModel>();

        for (ContentKey superDeptKey : CmsManager.getInstance().getContentKeysByType(ContentType.SuperDepartment)) {
            ContentNodeModel superDeptNode = ContentFactory.getInstance().getContentNodeByKey(superDeptKey);

            if (superDeptNode instanceof SuperDepartmentModel) {
                SuperDepartmentModel superDept = (SuperDepartmentModel) superDeptNode;
                SitemapData superDeptData = new SitemapData();
                rootData.children.add(superDeptData);
                superDeptData.id = superDept.getContentName();
                superDeptData.name = superDept.getFullName();

                for (DepartmentModel dept : superDept.getDepartments()) {
                    if (processedDepartments.add(dept)) {
                        // out.println(" "+dept.getFullName()+"...");
                        // response.flushBuffer();
                        SitemapData deptData = processProductContainer(superDeptData, dept, isQuick);
                        superDeptData.countAll += deptData.countAll;
                        superDeptData.countAvailable += deptData.countAvailable;
                        superDeptData.countTempUnavailable += deptData.countTempUnavailable;
                        superDeptData.countDiscontinued += deptData.countDiscontinued;
                    }
                }
            }
        }

        SitemapData emptySuperDeptData = new SitemapData();
        rootData.children.add(emptySuperDeptData);
        emptySuperDeptData.id = "none";
        emptySuperDeptData.name = "No Super Department";

        // remaining departments
        for (DepartmentModel dept : ContentFactory.getInstance().getStore().getDepartments()) {
            if (!processedDepartments.contains(dept)) {
                // out.println(" "+dept.getFullName()+"...");
                // response.flushBuffer();
                SitemapData deptData = processProductContainer(emptySuperDeptData, dept, isQuick);
                emptySuperDeptData.countAll += deptData.countAll;
                emptySuperDeptData.countAvailable += deptData.countAvailable;
                emptySuperDeptData.countTempUnavailable += deptData.countTempUnavailable;
                emptySuperDeptData.countDiscontinued += deptData.countDiscontinued;
            }
        }

        long loadTimeDiffSec = (new Date().getTime() - startTime.getTime()) / 1000l;

        LOGGER.info("Load finished, it took " + loadTimeDiffSec + " seconds");
        return rootData;
    }

    private static SitemapData processProductContainer(SitemapData parentData, ProductContainer container, Boolean isQuick) {
        SitemapData SitemapData = new SitemapData();
        parentData.children.add(SitemapData);

        SitemapData.id = container.getContentName();
        SitemapData.name = container.getFullName();

        if (container instanceof CategoryModel) {
            for (ProductModel prod : ((CategoryModel) container).getProducts()) {
                if (isQuick) {
                    SitemapData.countAvailable++;
                } else {
                    if (prod.isDiscontinued()) {
                        SitemapData.countDiscontinued++;
                    } else if (prod.isTempUnavailable()) {
                        SitemapData.countTempUnavailable++;
                    } else {
                        SitemapData.countAvailable++;
                    }
                }
                SitemapData.countAll++;
            }
        }

        for (CategoryModel subCat : container.getSubcategories()) {
            SitemapData childData = processProductContainer(SitemapData, subCat, isQuick);
            SitemapData.countAll += childData.countAll;
            SitemapData.countAvailable += childData.countAvailable;
            SitemapData.countTempUnavailable += childData.countTempUnavailable;
            SitemapData.countDiscontinued += childData.countDiscontinued;
        }
        return SitemapData;
    }

}
