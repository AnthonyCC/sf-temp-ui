package com.freshdirect.cms.ui.editor.reports.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.ui.editor.ReportAttributes;
import com.freshdirect.cms.ui.editor.reports.repository.ReportsRepository;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;

@Service
public class ReportingService {

    // direct subfolders of CMSReports
    public static final ContentKey CMS_REPORTS_FOLDER_KEY = ContentKeyFactory.get(ContentType.FDFolder, "flex_1009");
    public static final ContentKey SMARTSTORE_REPORTS_FOLDER_KEY = ContentKeyFactory.get(ContentType.FDFolder, "flex_1010");
    public static final ContentKey CMS_QUERIES_FOLDER_KEY = ContentKeyFactory.get(ContentType.FDFolder, "flex_1008");

    private static final Function<String, ContentKey> MAKE_REPORT_KEY = new Function<String, ContentKey>() {

        @Override
        public ContentKey apply(String id) {
            return ContentKeyFactory.get(ContentType.CmsReport, id);
        }
    };

    private static final Function<String, ContentKey> MAKE_QUERY_KEY = new Function<String, ContentKey>() {

        @Override
        public ContentKey apply(String id) {
            return ContentKeyFactory.get(ContentType.CmsQuery, id);
        }
    };

    // secondary label reports folders
    public static final List<ContentKey> CMS_REPORT_NODE_KEYS = FluentIterable
            .from(Arrays.asList("circularReferences", "hierarchyReport", "invisibleProducts", "multipleReferences", "primaryHomes", "multiHome", "recentChanges",
                    "recipesSummary", "stackedSkus", "brokenMediaLinks"))
            .transform(MAKE_REPORT_KEY)
            .toList();

    public static final List<ContentKey> SMARTSTORE_REPORT_NODE_KEYS = FluentIterable
            .from(Arrays.asList("scarabRules", "smartCatRecs", "ymalSets"))
            .transform(MAKE_REPORT_KEY)
            .toList();

    public static final List<ContentKey> CMS_QUERY_NODE_KEYS = FluentIterable
            .from(Arrays.asList("orphans", "recent", "unreachable"))
            .transform(MAKE_QUERY_KEY)
            .toList();

    public static final Set<ContentKey> ALL_REPORTING_KEYS = new HashSet<ContentKey>() {
        {
            this.add(CMS_REPORTS_FOLDER_KEY);
            this.add(SMARTSTORE_REPORTS_FOLDER_KEY);
            this.add(CMS_QUERIES_FOLDER_KEY);

            this.addAll(CMS_REPORT_NODE_KEYS);
            this.addAll(SMARTSTORE_REPORT_NODE_KEYS);
            this.addAll(CMS_QUERY_NODE_KEYS);
        }
    };

    @Autowired
    private ReportsRepository repository;

    public List<ContentKey> reportNodesOfFolder(ContentKey reportFolder) {

        List<ContentKey> reportKeysList = new ArrayList<ContentKey>();

        if (CMS_REPORTS_FOLDER_KEY.equals(reportFolder)) {
            reportKeysList = CMS_REPORT_NODE_KEYS;
        } else if (SMARTSTORE_REPORTS_FOLDER_KEY.equals(reportFolder)) {
            reportKeysList = SMARTSTORE_REPORT_NODE_KEYS;
        } else if (CMS_QUERIES_FOLDER_KEY.equals(reportFolder)) {
            reportKeysList = CMS_QUERY_NODE_KEYS;
        }

        return reportKeysList;
    }

    private static final Map<ContentKey, String> LABELS = new HashMap<ContentKey, String>();
    static {
        LABELS.put(RootContentKey.CMS_QUERIES.contentKey, "CMS Queries");

        LABELS.put(CMS_REPORTS_FOLDER_KEY, "Reports");
        LABELS.put(SMARTSTORE_REPORTS_FOLDER_KEY, "Smart Store Reports");
        LABELS.put(CMS_QUERIES_FOLDER_KEY, "Store Objects");

        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "circularReferences"), "Circular References");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "hierarchyReport"), "Hierarchy Report");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "invisibleProducts"), "Invisible products");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "multipleReferences"), "Multiple Virtual References");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "primaryHomes"), "Primary Homes");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "multiHome"), "Products with multiple homes");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "recentChanges"), "Recent changes");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "recipesSummary"), "Recipes summary");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "stackedSkus"), "Stacked Skus");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "brokenMediaLinks"), "Broken Media Links");

        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "scarabRules"), "Scarab merchandising rules");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "smartCatRecs"), "Smart categories");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsReport, "ymalSets"), "YMAL sets");

        LABELS.put(ContentKeyFactory.get(ContentType.CmsQuery, "orphans"), "Orphan Store Objects");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsQuery, "recent"), "Recently modified");
        LABELS.put(ContentKeyFactory.get(ContentType.CmsQuery, "unreachable"), "Unreachable Store Objects");
    };

    public Optional<String> labelOfContentKey(ContentKey key) {
        return Optional.fromNullable(LABELS.get(key));
    }

    public Optional<List<List<ContentKey>>> findContextsOf(ContentKey contentKey) {
        Assert.notNull(contentKey, "Missing content key");

        if (!ALL_REPORTING_KEYS.contains(contentKey)) {
            return Optional.absent();
        }

        List<ContentKey> context = new ArrayList<ContentKey>();
        if (RootContentKey.CMS_QUERIES.contentKey.equals(contentKey)) {
            // first degree context
            context.add(RootContentKey.CMS_QUERIES.contentKey);
        } else if (CMS_REPORTS_FOLDER_KEY.equals(contentKey) || SMARTSTORE_REPORTS_FOLDER_KEY.equals(contentKey) || CMS_QUERIES_FOLDER_KEY.equals(contentKey) ) {
            // second degree contexts
            context.add(contentKey);
            context.add(RootContentKey.CMS_QUERIES.contentKey);
        } else {
            // third degree contexts
            if (CMS_REPORT_NODE_KEYS.contains(contentKey)) {
                context.add(contentKey);
                context.add(CMS_REPORTS_FOLDER_KEY);
            }
            else if (SMARTSTORE_REPORT_NODE_KEYS.contains(contentKey)) {
                context.add(contentKey);
                context.add(SMARTSTORE_REPORTS_FOLDER_KEY);
            }
            else if (CMS_QUERY_NODE_KEYS.contains(contentKey)) {
                context.add(contentKey);
                context.add(CMS_QUERIES_FOLDER_KEY);
            }
            context.add(RootContentKey.CMS_QUERIES.contentKey);
        }

        List<List<ContentKey>> contextList = new ArrayList<List<ContentKey>>();
        contextList.add(context);

        return Optional.of(contextList);
    }

    public void performCmsQuery(ContentKey contentKey, Map<Attribute, Object> values) {
        Assert.notNull(contentKey, "Missing content key");
        Assert.isTrue(ContentType.CmsQuery == contentKey.type, "Invalid key type " + contentKey.type);

        List<ContentKey> keys = executeCmsQueryForResults(contentKey);

        if ("orphans".equals(contentKey.id)) {
            values.put(ReportAttributes.CmsQuery.name, "Orphan Store Objects");
            values.put(ReportAttributes.CmsQuery.description, "Show objects that do not have a parent");
            values.put(ReportAttributes.CmsQuery.results, keys);
        } else if ("recent".equals(contentKey.id)) {
            values.put(ReportAttributes.CmsQuery.name, "Recently modified");
            values.put(ReportAttributes.CmsQuery.description, "Last 100 changes");
            values.put(ReportAttributes.CmsQuery.results, keys);
        } else if ("unreachable".equals(contentKey.id)) {
            values.put(ReportAttributes.CmsQuery.name, "Unreachable Store Objects");
            values.put(ReportAttributes.CmsQuery.description, "Show objects that are unreachable from the Store");
            values.put(ReportAttributes.CmsQuery.results, keys);
        } else {
            values.put(ReportAttributes.CmsQuery.name, "Unknown query " + contentKey.id);
            values.put(ReportAttributes.CmsQuery.description, "");
            values.put(ReportAttributes.CmsQuery.results, Collections.emptyList());
        }

    }

    public List<ContentKey> executeCmsQueryForResults(ContentKey queryKey) {
        Assert.notNull(queryKey, "Missing content key");
        Assert.isTrue(ContentType.CmsQuery == queryKey.type, "Invalid key type " + queryKey.type);

        List<ContentKey> keys = null;

        if ("orphans".equals(queryKey.id)) {
            keys = repository.fetchOrphanObjects();
        } else if ("recent".equals(queryKey.id)) {
            keys = repository.fetchRecentlyModifiedObjects();
        } else if ("unreachable".equals(queryKey.id)) {
            keys = repository.fetchUnreachableStoreObjects();
        }
        return keys;

    }

    public void performCmsReport(ContentKey contentKey, Map<Attribute, Object> values) {
        Assert.notNull(contentKey, "Missing content key");
        Assert.isTrue(ContentType.CmsReport == contentKey.type, "Invalid key type " + contentKey.type);

        if ("multiHome".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchMultiHomeProducts();

            values.put(ReportAttributes.CmsReport.name, "Products with multiple homes");
            values.put(ReportAttributes.CmsReport.description, "");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("recentChanges".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchRecentChanges(7, 1000);

            values.put(ReportAttributes.CmsReport.name, "Recent changes");
            values.put(ReportAttributes.CmsReport.description, "Show the most recent changes in the last N days, grouped by content type");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("invisibleProducts".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchInvisibleProducts();

            values.put(ReportAttributes.CmsReport.name, "Invisible products");
            values.put(ReportAttributes.CmsReport.description, "");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("circularReferences".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchCircularReferences();

            values.put(ReportAttributes.CmsReport.name, "Circular References");
            values.put(ReportAttributes.CmsReport.description, "Shows the reference cycles created with VIRTUAL_GROUP references and sub categories");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("multipleReferences".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchMultipleReferences();

            values.put(ReportAttributes.CmsReport.name, "Multiple Virtual References");
            values.put(ReportAttributes.CmsReport.description, "List categories which VIRTUAL GROUP points to a category which already has a VIRTUAL GROUP");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("hierarchyReport".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchHierarchyReport();

            values.put(ReportAttributes.CmsReport.name, "Hierarchy Report");
            values.put(ReportAttributes.CmsReport.description, "<not working yet>");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("stackedSkus".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchStackedSKUs();

            values.put(ReportAttributes.CmsReport.name, "Stacked Skus");
            values.put(ReportAttributes.CmsReport.description, "Finds products which have more than one Sku");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("recipesSummary".equals(contentKey.id)) {
            List<Map<Attribute, Object>> result = repository.fetchRecipesSummary();

            values.put(ReportAttributes.CmsReport.name, "Recipes summary");
            values.put(ReportAttributes.CmsReport.description, "");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("primaryHomes".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchPrimaryHomes(RootContentKey.STORE_FRESHDIRECT.contentKey.toString());

            values.put(ReportAttributes.CmsReport.name, "Primary Homes");
            values.put(ReportAttributes.CmsReport.description, "Show the product's primary homes (currently only for FRESHDIRECT Store)");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("scarabRules".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchScarabMerchandisingRules();

            values.put(ReportAttributes.CmsReport.name, "Scarab merchandising rules");
            values.put(ReportAttributes.CmsReport.description, "Shows the active Scarab merchandising rules (include,exclude,promote,demote)");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("ymalSets".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchYmalSets();

            values.put(ReportAttributes.CmsReport.name, "YMAL sets");
            values.put(ReportAttributes.CmsReport.description, "Smart YMAL sets");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("smartCatRecs".equals(contentKey.id)) {
            List<Map<Attribute,Object>> result = repository.fetchSmartCategoryRecommenders();

            values.put(ReportAttributes.CmsReport.name, "Smart categories");
            values.put(ReportAttributes.CmsReport.description, "Smart category recommenders");
            values.put(ReportAttributes.CmsReport.results, result);
        } else if ("brokenMediaLinks".equals(contentKey.id)) {
            List<Map<Attribute, Object>> result = repository.fetchBrokenMediaLinks();

            values.put(ReportAttributes.CmsReport.name, "Broken Media Links");
            values.put(ReportAttributes.CmsReport.description, "Shows the nodes and fields where an invalid media is linked");
            values.put(ReportAttributes.CmsReport.results, result);
        } else {
            values.put(ReportAttributes.CmsReport.name, "Unknown report " + contentKey.id);
            values.put(ReportAttributes.CmsReport.description, "");
            values.put(ReportAttributes.CmsReport.results, Collections.emptyList());
        }
    }
}
