package com.freshdirect.cms.ui.editor.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.persistence.erps.data.ErpCharacteristicKey;
import com.freshdirect.cms.persistence.erps.data.ErpCharacteristicValueKey;
import com.freshdirect.cms.persistence.service.ERPSDataService;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.editor.ContentKeyDisplayOrderComparator;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;

@Service
public class TreeNodeService {

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ERPSDataService erpsDataService;

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private LabelProviderService labelProviderService;

    @Autowired
    private ContentLoaderService contentLoaderService;

    private final List<ContentKey> cmsQueryFolders = new ArrayList<ContentKey>(3);

    private TreeNodeService() {
        cmsQueryFolders.add(ReportingService.CMS_REPORTS_FOLDER_KEY);
        cmsQueryFolders.add(ReportingService.SMARTSTORE_REPORTS_FOLDER_KEY);
        cmsQueryFolders.add(ReportingService.CMS_QUERIES_FOLDER_KEY);
    }

    public List<TreeContentNodeModel> createTreeChildren(TreeContentNodeModel parentNode) {
        List<TreeContentNodeModel> children;

        if (parentNode == null) {
            children = createRootNodes();
        } else {
            children = createInnerNodes(parentNode);
        }

        return children;
    }

    private List<TreeContentNodeModel> createRootNodes() {
        List<TreeContentNodeModel> children = new ArrayList<TreeContentNodeModel>();

        for (RootContentKey rootKey : RootContentKey.values()) {
            children.add(createTreeNode(rootKey.contentKey, null));
        }
        return children;
    }

    private List<TreeContentNodeModel> createInnerNodes(TreeContentNodeModel parentNode) {
        List<TreeContentNodeModel> children = new ArrayList<TreeContentNodeModel>();

        ContentKey parentKey = ContentKeyFactory.get(parentNode.getKey());
        List<ContentKey> childKeys = getChildKeys(parentKey);

        List<ContentKey> orderedChildKeys = new ArrayList<ContentKey>(childKeys);
        Collections.sort(orderedChildKeys, new ContentKeyDisplayOrderComparator( labelProviderService.labelsOf(childKeys) ));

        for (ContentKey childKey : orderedChildKeys) {
            children.add(createTreeNode(childKey, parentNode));
        }

        return children;
    }

    public TreeContentNodeModel createTreeNode(ContentKey key, TreeContentNodeModel parentNode) {
        String label = labelProviderService.labelOfVirtualContentKey(key);

        TreeContentNodeModel treeNode = new TreeContentNodeModel(key.getType().toString(), label, key.toString(), parentNode);
        treeNode.setHasChildren(!getChildKeys(key).isEmpty());
        contentLoaderService.decorateModel(key, treeNode);

        return treeNode;

    }

    private List<ContentKey> getChildKeys(ContentKey contentKey) {
        List<ContentKey> children = new ArrayList<ContentKey>();

        if (RootContentKey.CMS_QUERIES.contentKey.equals(contentKey)) {
            children.addAll(cmsQueryFolders);
        } else if (RootContentKey.ORPHANS.contentKey.equals(contentKey)) {
            children.addAll(reportingService.executeCmsQueryForResults(contentKey));
        } else if (cmsQueryFolders.contains(contentKey)) {
            children.addAll(reportingService.reportNodesOfFolder(contentKey));
        } else if (ContentType.MediaFolder == contentKey.type) {
            children.addAll(mediaService.getChildMediaKeys(contentKey));
        } else if (ContentType.Sku == contentKey.type) {
            String erpMaterialId = erpsDataService.fetchMaterialsBySku(contentKey);
            if (erpMaterialId != null) {
                children.add(ContentKeyFactory.get(ContentType.ErpMaterial, erpMaterialId));
            }
        } else if (ContentType.ErpMaterial == contentKey.type) {
            // include classes first
            List<String> classes = erpsDataService.fetchClasses(contentKey.id);
            for (String erpClass : classes) {
                children.add(ContentKeyFactory.get(ContentType.ErpClass, erpClass));
            }

            // include sales units
            Map<String, Map<String, String>> salesUnitsPerMaterial = erpsDataService.fetchSalesUnits();
            Map<String, String> salesUnits = salesUnitsPerMaterial.get(contentKey.id);
            if (salesUnits != null) {
                for (String salesUnitId : salesUnits.keySet()) {
                    children.add(ContentKeyFactory.get(ContentType.ErpSalesUnit, salesUnitId));
                }
            }
        } else if (ContentType.ErpClass == contentKey.type) {
            String classId = contentKey.id;
            List<String> charsOfClass = erpsDataService.fetchCharacteristics(classId);
            for (String erpChar : charsOfClass) {
                children.add(ContentKeyFactory.get(ContentType.ErpCharacteristic, classId + "/" + erpChar));
            }
        } else if (ContentType.ErpCharacteristic == contentKey.type) {
            // contentKey like "ErpCharacteristic:CL_FDX_807038/C_FDXCLSCCHICK1"
            final ErpCharacteristicKey charKey = new ErpCharacteristicKey(contentKey);
            Map<String, String> valueMap = erpsDataService.fetchCharacteristicValues(charKey);
            if (valueMap != null) {
                for (String valueName : valueMap.keySet()) {
                    children.add(ContentKeyFactory.get(ContentType.ErpCharacteristicValue, new ErpCharacteristicValueKey(charKey, valueName).toString()));
                }
            }
        } else {
            children.addAll(contentProviderService.getChildKeys(contentKey, true));
        }

        return children;
    }
}
