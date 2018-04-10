package com.freshdirect.cms.ui.editor.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.extjs.gxt.ui.client.widget.form.Time;
import com.freshdirect.cms.contentvalidation.validator.UniqueContentKeyValidator;
import com.freshdirect.cms.contentvalidation.validator.WhitespaceValidator;
import com.freshdirect.cms.core.converter.ScalarValueConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.persistence.erps.data.ErpCharacteristicKey;
import com.freshdirect.cms.persistence.erps.data.ErpCharacteristicValueKey;
import com.freshdirect.cms.persistence.erps.data.MaterialAvailabilityStatus;
import com.freshdirect.cms.persistence.erps.data.MaterialData;
import com.freshdirect.cms.persistence.service.ERPSDataService;
import com.freshdirect.cms.ui.editor.UnmodifiableContent;
import com.freshdirect.cms.ui.editor.domain.AttributeValueSource;
import com.freshdirect.cms.ui.editor.domain.VirtualAttributes;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.ModifiableAttributeI;
import com.freshdirect.cms.ui.model.attributes.OneToManyAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToOneAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute.ProductConfigParams;
import com.freshdirect.cms.ui.model.attributes.SimpleAttribute;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.service.ServerException;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.base.Optional;

@Service
public class ContentLoaderService {



	private static final Logger LOGGER = LoggerFactory.getLogger(ContentLoaderService.class);

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private EditorService editorService;

    @Autowired
    private ERPSDataService erpsDataService;

    @Autowired
    private LabelProviderService labelProviderService;

    @Value("${cms.media.base.url}")
    private String mediaBaseURL;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private PreviewLinkProvider previewLinkService;

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private StoreContextService storeContextService;

    @Autowired
    private UniqueContentKeyValidator uniqueContentKeyValidator;

    @Autowired
    private WhitespaceValidator whitespaceValidator;

    public void decorateModel(ContentKey key, ContentNodeModel model) {

        if (ContentType.Department.equals(key.getType()) || ContentType.SuperDepartment.equals(key.getType())) {
            decorateIconOverride(key, model);
        }

        if (Media.isMediaType(key)) {
            Optional<Media> optionalMedia = mediaService.getMediaByContentKey(key);

            if (optionalMedia.isPresent()) {
                final Media media = optionalMedia.get();
                model.setPreviewUrl(mediaBaseURL + media.getUri());

                if (ContentType.Image == key.type) {
                    model.setWidth(media.getWidth());
                    model.setHeight(media.getHeight());
                }
            } else {
                LOGGER.error("No media record found for media node " + key);
            }

        }

    }

	public String decorateContextOverride(ContentKey key,boolean useDefault) {
        String contextStyle = "";

        if (useDefault) {
            contextStyle = GwtNodeContext.COS_CONTEXTOVERRIDE_COLOR_NOOVERRIDE;
        }

        Optional<Attribute> catalogAttribute = contentTypeInfoService.findAttributeByName(key.type, "catalog");
        Optional<Object> catalogAttributeValue = contentProviderService.getAttributeValue(key, catalogAttribute.get());

        if (catalogAttribute.isPresent() && catalogAttributeValue.isPresent()) {
            String catalogValue = catalogAttributeValue.get().toString();

            if ("ALL".equals(catalogValue)) {
                contextStyle = GwtNodeContext.COS_CONTEXTOVERRIDE_COLOR_RED;
            } else if ("CORPORATE".equals(catalogValue)) {
                contextStyle = GwtNodeContext.COS_CONTEXTOVERRIDE_COLOR_GREEN;
            }
        }
        return contextStyle;
	}

    public Map<Attribute, Object> getAllAttributesForVirtualNode(ContentKey contentKey) {
        Map<Attribute, Object> values = new HashMap<Attribute, Object>();

        if (RootContentKey.CMS_QUERIES.contentKey.equals(contentKey)) {
            values.put(ContentTypes.FDFolder.name, labelProviderService.labelOfVirtualContentKey(contentKey));
            values.put(ContentTypes.FDFolder.children,
                    Arrays.asList(ReportingService.CMS_QUERIES_FOLDER_KEY, ReportingService.SMARTSTORE_REPORTS_FOLDER_KEY, ReportingService.CMS_REPORTS_FOLDER_KEY));
        } else if (ReportingService.CMS_QUERIES_FOLDER_KEY.equals(contentKey)) {
            values.put(ContentTypes.FDFolder.name, labelProviderService.labelOfVirtualContentKey(contentKey));
            values.put(ContentTypes.FDFolder.children, reportingService.reportNodesOfFolder(contentKey));
        } else if (ReportingService.SMARTSTORE_REPORTS_FOLDER_KEY.equals(contentKey)) {
            values.put(ContentTypes.FDFolder.name, labelProviderService.labelOfVirtualContentKey(contentKey));
            values.put(ContentTypes.FDFolder.children, reportingService.reportNodesOfFolder(contentKey));
        } else if (ReportingService.CMS_REPORTS_FOLDER_KEY.equals(contentKey)) {
            values.put(ContentTypes.FDFolder.name, labelProviderService.labelOfVirtualContentKey(contentKey));
            values.put(ContentTypes.FDFolder.children, reportingService.reportNodesOfFolder(contentKey));
        } else if (ContentType.CmsQuery.equals(contentKey.type)) {
            reportingService.performCmsQuery(contentKey, values);
        } else if (ContentType.CmsReport.equals(contentKey.type)) {
            reportingService.performCmsReport(contentKey, values);
        }

        return values;
    }

    public GwtContentNode getGwtNode(ContentKey contentKey, TabDefinition tabDef) {
        GwtContentNode gwtNode = new GwtContentNode(contentKey.getEncoded());

        Map<Attribute, Object> virtualValues = getAllAttributesForVirtualNode(contentKey);
        if (!virtualValues.isEmpty()) {
            gwtNode.setLabel(labelProviderService.labelOfVirtualContentKey(contentKey));
            for (Attribute attribute : virtualValues.keySet()) {
                String key = attribute.getName();
                ModifiableAttributeI gwtAttr = translateAttribute(contentKey, attribute, tabDef.getCustomFieldDefinition(key), virtualValues.get(attribute), AttributeValueSource.MODEL);
                gwtNode.setOriginalAttribute(key, gwtAttr);
            }
        } else {
            gwtNode.setLabel(labelProviderService.labelOfContentKey(contentKey));
            virtualValues = contentProviderService.getAllAttributesForContentKey(contentKey);
            for (Attribute attribute : contentTypeInfoService.selectAttributes(contentKey.type)) {
                String key = attribute.getName();
                ModifiableAttributeI gwtAttr = translateAttribute(contentKey, attribute, tabDef.getCustomFieldDefinition(key), virtualValues.get(attribute), AttributeValueSource.MODEL);
                gwtNode.setOriginalAttribute(key, gwtAttr);
            }

            // Handle mixed types
            if (ContentType.Sku == contentKey.type) {

                String materialId = erpsDataService.fetchSkuMaterialAssociations().get(contentKey.id);

                MaterialData materialData = erpsDataService.fetchMaterialData().get(materialId);

                List<ContentKey> materialKeys = new ArrayList<ContentKey>();
                if (materialId != null) {
                    materialKeys.add(ContentKeyFactory.get(ContentType.ErpMaterial, materialId));
                }

                if (materialData != null) {
                    populateScalarAttribute(contentKey, VirtualAttributes.Sku.promoPrice, materialData.getPromoPrice(), gwtNode);
                    final MaterialAvailabilityStatus availabilityStatus = materialData.getAvailabilityStatus();
                    populateScalarAttribute(contentKey, VirtualAttributes.Sku.UNAVAILABILITY_STATUS, (availabilityStatus != null ? availabilityStatus.toString() : ""), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.Sku.materialVersion, materialData.getLatestMaterialVersion(), gwtNode);
                }

                populateRelationshipAttribute(contentKey, (Relationship) ContentTypes.Sku.materials, materialKeys, gwtNode);

            } else if (ContentType.ErpMaterial == contentKey.type) {
                Map<String, String> su = erpsDataService.fetchSalesUnits().get(contentKey.id);

                MaterialData materialData = erpsDataService.fetchMaterialData().get(contentKey.id);
                if (materialData != null) {
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.UPC, materialData.getUpc(), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.DESCRIPTION, WordUtils.capitalizeFully(materialData.getDescription()), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.NAME, materialData.getName(), gwtNode);

                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.atpRule, materialData.getAtpRule(), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.leadTime, materialData.getLeadTime(), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.alcoholicContent, materialData.isAlcoholicContent(), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.taxable, materialData.isTaxable(), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.kosher, materialData.isKosher(), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.platter, materialData.isPlatter(), gwtNode);
                    populateScalarAttribute(contentKey, VirtualAttributes.ErpMaterial.blockedDays, materialData.getBlockedDays(), gwtNode);
                }

                // erp classes
                List<ContentKey> erpClassKeys = new ArrayList<ContentKey>();
                List<String> classes = erpsDataService.fetchClasses(contentKey.id);
                for (String erpClass : classes) {
                    erpClassKeys.add(ContentKeyFactory.get(ContentType.ErpClass, erpClass));
                }

                populateRelationshipAttribute(contentKey, VirtualAttributes.ErpMaterial.classes, erpClassKeys, gwtNode);


                // sales units
                List<ContentKey> salesUnitKeys = new ArrayList<ContentKey>();
                for (String salesUnitId : su.keySet()) {
                    salesUnitKeys.add(ContentKeyFactory.get(ContentType.ErpSalesUnit, su.get(salesUnitId)));
                }

                populateRelationshipAttribute(contentKey, VirtualAttributes.ErpMaterial.salesUnits, salesUnitKeys, gwtNode);

            } else if (ContentType.ErpClass == contentKey.type) {
                // show characteristics
                String classId = contentKey.id;
                List<ContentKey> characteristicKeys = new ArrayList<ContentKey>();
                List<String> charsOfClass = erpsDataService.fetchCharacteristics(classId);
                for (String erpChar : charsOfClass) {
                    characteristicKeys.add(ContentKeyFactory.get(ContentType.ErpCharacteristic, classId + "/" + erpChar));
                }

                populateRelationshipAttribute(contentKey, VirtualAttributes.ErpClass.characteristics, characteristicKeys, gwtNode);

            } else if (ContentType.ErpCharacteristic == contentKey.type) {
                ErpCharacteristicKey erpKey = new ErpCharacteristicKey(contentKey);

                gwtNode.setLabel(erpKey.getCharacteristicName());

                List<ContentKey> erpValueKeys = new ArrayList<ContentKey>();
                Map<String, String> erpValues = erpsDataService.fetchCharacteristicValues(erpKey);
                for (String erpValueName : erpValues.keySet()) {
                    ContentKey key = ContentKeyFactory.get(ContentType.ErpCharacteristicValue, erpKey.createValueKey(erpValueName).toString());
                    erpValueKeys.add(key);
                }

                populateScalarAttribute(contentKey, VirtualAttributes.ErpCharacteristic.name, erpKey.getCharacteristicName(), gwtNode);
                populateRelationshipAttribute(contentKey, VirtualAttributes.ErpCharacteristic.values, erpValueKeys, gwtNode);

            } else if (ContentType.ErpCharacteristicValue == contentKey.type) {
                ErpCharacteristicValueKey erpKey = new ErpCharacteristicValueKey(contentKey);

                gwtNode.setLabel(erpKey.getCharacteristicValueName());

                Map<String, String> erpValues = erpsDataService.fetchCharacteristicValues(erpKey.getCharacteristicKey());

                String erpValueDesc = erpValues.get(erpKey.getCharacteristicValueName());

                populateScalarAttribute(contentKey, VirtualAttributes.ErpCharacteristicValue.FULL_NAME, erpValueDesc, gwtNode);
                populateScalarAttribute(contentKey, VirtualAttributes.ErpCharacteristicValue.name, erpKey.getCharacteristicValueName(), gwtNode);
            } else if (Media.isMediaType(contentKey)) {
                Optional<Media> associatedMedia = mediaService.getMediaByContentKey(contentKey);
                if (associatedMedia.isPresent()) {
                    final Media media = associatedMedia.get();

                    populateScalarAttribute(contentKey, (Scalar) ContentTypes.Image.path, media.getUri(), gwtNode);
                    populateScalarAttribute(contentKey, (Scalar) ContentTypes.Image.lastmodified, media.getLastModified(), gwtNode);

                    if (ContentType.Image == contentKey.type) {
                        populateScalarAttribute(contentKey, (Scalar) ContentTypes.Image.width, media.getWidth(), gwtNode);
                        populateScalarAttribute(contentKey, (Scalar) ContentTypes.Image.height, media.getHeight(), gwtNode);
                    } else if (ContentType.Html == contentKey.type) {
                        Map<Attribute, Object> htmlValues = contentProviderService.getAttributeValues(contentKey,
                                Arrays.asList(new Attribute[] { ContentTypes.Html.popupSize, ContentTypes.Html.title }));

                        populateScalarAttribute(contentKey, (Scalar) ContentTypes.Html.popupSize, htmlValues.get(ContentTypes.Html.popupSize), gwtNode);
                        populateScalarAttribute(contentKey, (Scalar) ContentTypes.Html.title, htmlValues.get(ContentTypes.Html.title), gwtNode);
                    } else if (ContentType.MediaFolder == contentKey.type) {
                        Set<ContentKey> childKeys = mediaService.getChildMediaKeys(contentKey);

                        List<ContentKey> mediaItemKeys = new ArrayList<ContentKey>();
                        List<ContentKey> subFolderKeys = new ArrayList<ContentKey>();

                        for (ContentKey mediaKey : childKeys) {
                            if (ContentType.MediaFolder == mediaKey.type) {
                                subFolderKeys.add(mediaKey);
                            } else {
                                mediaItemKeys.add(mediaKey);
                            }
                        }

                        populateRelationshipAttribute(contentKey, (Relationship) ContentTypes.MediaFolder.files, mediaItemKeys, gwtNode);
                        populateRelationshipAttribute(contentKey, (Relationship) ContentTypes.MediaFolder.subFolders, subFolderKeys, gwtNode);
                    }
                }
            }
        }

        return gwtNode;
    }

    public GwtNodeData getGwtNodeData(ContentKey key, GwtNodePermission permission) throws ServerException {
        TabDefinition tabDef = editorService.gwtTabDefinition(key);
        GwtContentNode gwtNode = getGwtNode(key, tabDef);
        GwtNodeContext ctx = storeContextService.contextsOf(key);
        String previewLink = previewLinkService.getLink(key);

        final GwtNodeData gwtNodeData = new GwtNodeData(gwtNode, tabDef, permission, ctx, previewLink);
        gwtNodeData.setParentMap(collectParentsPerStore(key));
        return gwtNodeData;
    }

    public ProductConfigParams getProductConfigParams(ContentKey skuKey) {
        String materialId = erpsDataService.fetchMaterialsBySku(skuKey);
        Map<String, Map<String, String>> characteristics = erpsDataService.fetchMaterialCharacteristics().get(materialId);
        Map<String, String> erpSalesUnits = erpsDataService.fetchSalesUnitsBySku(skuKey);

        List<EnumModel> salesUnits = new ArrayList<EnumModel>();
        if (erpSalesUnits != null) {
            for (Map.Entry<String, String> salesUnitEntry : erpSalesUnits.entrySet()) {
                String id = salesUnitEntry.getKey();
                String label = salesUnitEntry.getValue();

                EnumModel em = new EnumModel(id, label);
                salesUnits.add(em);
            }
            Collections.sort(salesUnits);
        }

        List<EnumAttribute> enumAttrs = new ArrayList<EnumAttribute>();
        if (characteristics != null) {
            for (Map.Entry<String, Map<String, String>> entry : characteristics.entrySet()) {
                EnumAttribute enumAttr = new EnumAttribute();

                enumAttr.setLabel(entry.getKey());

                for (Map.Entry<String, String> charEntry : entry.getValue().entrySet()) {
                    enumAttr.addValue(charEntry.getKey(), charEntry.getValue());
                }

                enumAttrs.add(enumAttr);
            }
        }

        return new ProductConfigParams(salesUnits, enumAttrs);
    }

    // prototype nodes cannot have either contexts or preview links
    public GwtNodeData getPrototypeGwtNodeData(ContentKey key, GwtNodePermission permission) throws ServerException {

        validatePrototypeNodeId(key);

        // 2. check if key exists
        if (contentProviderService.containsContentKey(key)) {
            // there is already a node, return null.
            return null;
        }

        TabDefinition tabDef = editorService.gwtTabDefinition(key);
        GwtContentNode gwtNode = getGwtNode(key, tabDef);
        return new GwtNodeData(gwtNode, tabDef, permission);
    }

    public ModifiableAttributeI translateAttribute(ContentKey contentKey, Attribute cmsAttribute, CustomFieldDefinition customFieldDefinition, Object value, AttributeValueSource valueSource) {

        ModifiableAttributeI attr = null;

        if (cmsAttribute instanceof Scalar) {
            Scalar scalarAttribute = (Scalar) cmsAttribute;

            if (scalarAttribute.isEnumerated()) {
                attr = translateEnumeratedAttribute(contentKey.type, scalarAttribute, value);
            } else {
                attr = translateScalarAttribute(contentKey.type, scalarAttribute, value);
            }

        } else if (cmsAttribute instanceof Relationship) {
            Relationship relationship = (Relationship) cmsAttribute;

            if (RelationshipCardinality.ONE == relationship.getCardinality()) {

                if (customFieldDefinition != null && CustomFieldDefinition.Type.ProductConfigEditor == customFieldDefinition.getType()) {
                    attr = translateProductConfiguration(contentKey, relationship, value);
                } else {
                    attr = translateRelationshipWithOneCardinality(contentKey.type, relationship, value);
                }

            } else {
                if ("CmsReport$results".equals(relationship.getName())) {
                    attr = translateCmsReport(contentKey, value);
                    attr.setLabel("results");
                } else {
                    attr = translateRelationshipWithManyCardinality(contentKey.type, relationship, customFieldDefinition, value, valueSource);
                }
            }

        } else {
            LOGGER.error("Unknown attribute " + cmsAttribute.toString());
        }

        attr.setInheritable(cmsAttribute.getFlags().isInheritable());
        attr.setReadonly(isAttributeReadOnly(contentKey, cmsAttribute));

        return attr;
    }

    public TableAttribute translateCmsReport(ContentKey contentKey, Object value) {
        @SuppressWarnings("unchecked")
        List<Map<Attribute, Object>> results = (List<Map<Attribute, Object>>) value;

        TableAttribute attr = new TableAttribute();

        if (results.isEmpty()) {
            attr.setTypes(new TableAttribute.ColumnType[] {});
            attr.setColumns(new ContentNodeAttributeI[] {});
        } else {
            Map<Attribute, Object> firstRecord = results.get(0);

            populateCmsReportHeader(contentKey, attr, firstRecord);
            populateCmsReportRows(attr, results);
        }

        return attr;
    }

    private Map<String, Set<String>> collectParentsPerStore(ContentKey contentKey) {
        Map<String, Set<String>> parentCategoriesPerStore = new HashMap<String, Set<String>>();

        // fetch buckets first
        Map<ContentKey, Set<ContentKey>> parentKeyBuckets = contentProviderService.collectParentsPerStore(contentKey);

        // transform content keys to strings
        for (Map.Entry<ContentKey, Set<ContentKey>> entry : parentKeyBuckets.entrySet()) {
            final ContentKey storeKey = entry.getKey();
            final Set<ContentKey> parentKeys = entry.getValue();

            Set<String> parentStrings = new HashSet<String>();
            for (ContentKey parentKey : parentKeys) {
                parentStrings.add(parentKey.toString());
            }

            parentCategoriesPerStore.put(storeKey.toString(), parentStrings);
        }

        return parentCategoriesPerStore;
    }

    private ContentNodeModel createContentNodeModel(ContentKey key) {
        ContentNodeModel result = null;
        if (key != null) {
            result = new ContentNodeModel(key.type.name(), labelProviderService.labelOfContentKey(key), key.toString());
            decorateModel(key, result);
        }
        return result;
    }

    private Object getContentKeyAttributeValue(ContentKey key, String childColumnName) {
        Object childAttrValue = null;

        Optional<Attribute> optionalChildAttribute = contentTypeInfoService.findAttributeByName(key.type, childColumnName);
        if (optionalChildAttribute.isPresent()) {
            Optional<Object> optionalChildValue = contentProviderService.getAttributeValue(key, optionalChildAttribute.get());

            if (optionalChildValue.isPresent()) {
                childAttrValue = optionalChildValue.get();
            }
        }
        return childAttrValue;
    }

    private boolean isAttributeReadOnly(ContentKey contentKey, Attribute attribute) {
        return UnmodifiableContent.isModifiable(contentKey) ? attribute.isReadOnly() : false;
    }

    private void populateCmsReportHeader(ContentKey contentKey, TableAttribute attr, Map<Attribute, Object> headerRecord) {
        ContentNodeAttributeI[] columns = new ContentNodeAttributeI[headerRecord.keySet().size()];
        TableAttribute.ColumnType[] columnTypes = new TableAttribute.ColumnType[columns.length];

        int i = 0;
        for (Map.Entry<Attribute, Object> entry : headerRecord.entrySet()) {
            columns[i] = translateAttribute(contentKey, entry.getKey(), null, entry.getValue(), AttributeValueSource.MODEL);

            final String[] parts = entry.getKey().getName().split("\\|");
            TableAttribute.ColumnType columnType = TableAttribute.ColumnType.valueOf(parts[0]);
            String label = parts[1];

            columnTypes[i] = columnType;
            ((ModifiableAttributeI) columns[i]).setLabel(label);

            i++;
        }

        attr.setTypes(columnTypes);
        attr.setColumns(columns);
    }

    private void populateCmsReportRows(TableAttribute attr, List<Map<Attribute, Object>> rows) {
        TableAttribute.ColumnType columnTypes[] = attr.getTypes();
        for (Map<Attribute, Object> row : rows) {
            Serializable[] rowValue = new Serializable[row.size()];

            int k = 0;
            for (Map.Entry<Attribute, Object> entry : row.entrySet()) {

                rowValue[k] = toClientValues(entry.getValue());

                switch (columnTypes[k]) {
                    case ATTRIB:
                        // Never used, not ported from legacy
                        break;
                    case KEY: {
                        String stringValue = (String) entry.getValue();
                        if (stringValue != null) {
                            ContentKey key = ContentKeyFactory.get(stringValue);
                            rowValue[k] = createContentNodeModel(key);
                        } else {
                            rowValue[k] = null;
                        }
                    }
                        break;
                    default:
                        break;
                }

                k++;
            }

            attr.addRow(rowValue);
        }
    }

    private void populateRelationshipAttribute(ContentKey contentKey, Relationship relationship, Object value, GwtContentNode gwtNode) {
        ModifiableAttributeI gwtAttr;
        if (RelationshipCardinality.ONE == relationship.getCardinality()) {
            gwtAttr = translateRelationshipWithOneCardinality(contentKey.type, relationship, value);
        } else {
            gwtAttr = translateRelationshipWithManyCardinality(contentKey.type, relationship, null, value, AttributeValueSource.MODEL);
        }
        gwtAttr.setReadonly(true);

        gwtNode.setOriginalAttribute(relationship.getName(), gwtAttr);
    }

    // use this method only to include additional attributes to gwt node
    private void populateScalarAttribute(ContentKey contentKey, Scalar scalar, Object value, GwtContentNode gwtNode) {
        ModifiableAttributeI gwtAttr = translateScalarAttribute(contentKey.type, scalar, value);
        gwtAttr.setReadonly(true);

        gwtNode.setOriginalAttribute(scalar.getName(), gwtAttr);
    }

    private Serializable toClientValues(Object value) {
        if (value instanceof List) {
            List<Object> result = new ArrayList<Object>();
            for (Object item : (List<Object>) value) {
                result.add(toClientValues(item));
            }
            return (Serializable) result;
        }
        if (value instanceof String) {
            return (String) value;
        }
        if (value instanceof ContentKey) {
            ContentKey ck = ((ContentKey) value);
            return new ContentNodeModel(ck.type.name(), null, ck.toString());
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return null;
    }

    private OneToManyModel toOneToManyModel(ContentKey key, int idx) {
        OneToManyModel result = null;
        if (key != null) {
            result = new OneToManyModel(key.type.name(), key.toString(), labelProviderService.labelOfVirtualContentKey(key), idx);
            decorateModel(key, result);
        }
        return result;
    }

    private Object translateColumn(ContentKey contentKey, String col) {
        Object attrValue = null;

        String childColumnName = null;
        if (col.contains("$")) {
            String[] columnName = col.split("\\$");
            col = columnName[0];
            childColumnName = columnName[1];
        }

        Optional<Attribute> optionalAttribute = contentTypeInfoService.findAttributeByName(contentKey.type, col);
        if (optionalAttribute.isPresent()) {
            Attribute attribute = optionalAttribute.get();
            Optional<Object> optionalValue = contentProviderService.getAttributeValue(contentKey, attribute);

            if (optionalValue.isPresent()) {
                attrValue = optionalValue.get();

                if (attribute instanceof Scalar) {
                    // FIXME: this is weird: we are converting Objects to Objects by serialize() and casting the resulting String back to Object
                    attrValue = ScalarValueConverter.serializeToString((Scalar) attribute, attrValue);
                } else if (attrValue instanceof ContentKey) {
                    attrValue = getContentKeyAttributeValue((ContentKey) attrValue, childColumnName);
                }
            }

        } else {
            LOGGER.error("Failed to lookup attribute " + col + " for key " + contentKey.toString());
        }

        return attrValue;
    }

    private Set<String> translateDestinationTypes(Relationship relationship) {
        Set<String> destinationTypes = new HashSet<String>();
        for (ContentType ct : relationship.getDestinationTypes()) {
            destinationTypes.add(ct.name());
        }
        return destinationTypes;
    }

    private EnumAttribute translateEnumeratedAttribute(ContentType type, Scalar scalarAttribute, Object value) {
        EnumAttribute enumA = new EnumAttribute();

        enumA.setLabel(labelProviderService.getAttributeLabel(type, scalarAttribute));
        for (Object enumeratedValue : scalarAttribute.getEnumeratedValues()) {

            Map<String, String> enumValueLabels = labelProviderService.getEnumLabels(type, scalarAttribute);

            String enumValueLabel = enumValueLabels.get(String.valueOf(enumeratedValue));
            enumA.addValue((Serializable) enumeratedValue, enumValueLabel);

            if (enumeratedValue.equals(value)) {
                enumA.setValue((Serializable) value, enumValueLabel);
            }
        }
        return enumA;
    }

    private OneToManyModel translateGridEntry(ContentKey contentKey, CustomFieldDefinition customFieldDefinition, int idx) {
        OneToManyModel model = toOneToManyModel(contentKey, idx);

        for (String col : customFieldDefinition.getGridColumns()) {
            Object attrValue = translateColumn(contentKey, col);
            model.set(col, attrValue);
        }

        return model;
    }

    private ProductConfigAttribute translateProductConfiguration(ContentKey configuredProductKey, Relationship relationship, Object value) {
        ProductConfigAttribute pcAttr = new ProductConfigAttribute();
        pcAttr.setLabel(labelProviderService.getAttributeLabel(configuredProductKey.type, relationship));

        ContentKey skuKey = (ContentKey) value;

        if (skuKey != null) {

            pcAttr.setValue(createContentNodeModel(skuKey));
            pcAttr.setConfigParams(getProductConfigParams(skuKey));

            Map<Attribute, Object> configValues = contentProviderService.getAttributeValues(configuredProductKey,
                    Arrays.asList(ContentTypes.ConfiguredProduct.QUANTITY, ContentTypes.ConfiguredProduct.SALES_UNIT, ContentTypes.ConfiguredProduct.OPTIONS));

            pcAttr.setQuantity((Double) configValues.get(ContentTypes.ConfiguredProduct.QUANTITY));
            pcAttr.setSalesUnit((String) configValues.get(ContentTypes.ConfiguredProduct.SALES_UNIT));
            pcAttr.setConfigOptions((String) configValues.get(ContentTypes.ConfiguredProduct.OPTIONS));

        }

        return pcAttr;
    }

    private OneToManyAttribute translateRelationshipWithManyCardinality(ContentType type, Relationship relationship, CustomFieldDefinition customFieldDefinition, Object value, AttributeValueSource valueSource) {
        List<ContentKey> contentKeyList = (List<ContentKey>) value;

        final boolean isVariationMatrix = customFieldDefinition != null && customFieldDefinition.getType() == CustomFieldDefinition.Type.VariationMatrix;
        final boolean isGrid = customFieldDefinition != null && customFieldDefinition.getGridColumns() != null;

        OneToManyAttribute ooAttr = new OneToManyAttribute(translateDestinationTypes(relationship));
        ooAttr.setLabel(labelProviderService.getAttributeLabel(type, relationship));
        ooAttr.setNavigable(relationship.isNavigable());

        if (contentKeyList != null) {

            if (AttributeValueSource.MODEL == valueSource && relationship.getFlags().isInheritable() && contentKeyList.isEmpty()) {
                ooAttr.addValue(OneToManyModel.NULL_MODEL);
            } else {

                int idx = 0;
                for (ContentKey contentKey : contentKeyList) {

                    OneToManyModel model = null;

                    if (isGrid) {
                        model = translateGridEntry(contentKey, customFieldDefinition, idx);
                    } else if (isVariationMatrix) {
                        model = translateVariationMatrixEntry(contentKey, customFieldDefinition, idx);
                    } else {
                        model = translateSimpleEntry(contentKey, idx);
                    }

                    ooAttr.addValue(model);

                    idx++;
                }
            }
        }

        return ooAttr;
    }

    private OneToOneAttribute translateRelationshipWithOneCardinality(ContentType type, Relationship relationship, Object value) {

        OneToOneAttribute attr = null;

        ContentKey valueKey = (ContentKey) value;
        OneToOneAttribute ooAttr = new OneToOneAttribute();

        ooAttr.setLabel(labelProviderService.getAttributeLabel(type, relationship));
        ooAttr.setAllowedTypes(translateDestinationTypes(relationship));

        if (valueKey != null) {
            ContentNodeModel model = createContentNodeModel(valueKey);
            ooAttr.setValue(model);
        }
        attr = ooAttr;

        return attr;
    }

    private SimpleAttribute<?> translateScalarAttribute(ContentType type, Scalar scalarAttribute, Object value) {
        SimpleAttribute<?> attr = null;

        String label = labelProviderService.getAttributeLabel(type, scalarAttribute);

        if (Integer.class.equals(scalarAttribute.getType())) {
            attr = new SimpleAttribute<Integer>("integer", (Integer) value, label);
        } else if (Boolean.class.equals(scalarAttribute.getType())) {
            if (!scalarAttribute.getFlags().isInheritable() && value == null) {
                value = Boolean.FALSE;
            }
            attr = new SimpleAttribute<Boolean>("boolean", (Boolean) value, label);
        } else if (Double.class.equals(scalarAttribute.getType())) {
            attr = new SimpleAttribute<Double>("double", (Double) value, label);
        } else if (Date.class.isAssignableFrom(scalarAttribute.getType())) {
            if (ContentTypes.Schedule.StartTime.equals(scalarAttribute) || ContentTypes.Schedule.EndTime.equals(scalarAttribute)) {
                Time time = new Time(0, 0);
                if (value != null) {
                    time = new Time((Date) value);
                }
                attr = new SimpleAttribute<Time>("time", time, label);
            } else {
                attr = new SimpleAttribute<Date>("date", (Date) value, label);
            }
        } else {
            attr = new SimpleAttribute<String>("string", (String) value, label);
        }

        return attr;
    }

    private OneToManyModel translateSimpleEntry(ContentKey contentKey, int idx) {
        return toOneToManyModel(contentKey, idx);
    }

    private OneToManyModel translateVariationMatrixEntry(ContentKey contentKey, CustomFieldDefinition customFieldDefinition, int idx) {
        final List<Attribute> matrixAttributes = Arrays.asList(ContentTypes.Product.VARIATION_MATRIX, ContentTypes.Product.VARIATION_OPTIONS);

        OneToManyModel model = toOneToManyModel(contentKey, idx);

        Map<Attribute, Object> values = contentProviderService.getAttributeValues(contentKey, matrixAttributes);

        model.set("VARIATION_MATRIX", toClientValues(values.get(ContentTypes.Product.VARIATION_MATRIX)));
        model.set("VARIATION_OPTIONS", toClientValues(values.get(ContentTypes.Product.VARIATION_OPTIONS)));

        return model;
    }

    private void validatePrototypeNodeId(ContentKey key) throws ServerException {

        ValidationResults result = whitespaceValidator.validate(key, Collections.<Attribute, Object> emptyMap(), contentProviderService);
        result.addAll(uniqueContentKeyValidator.validate(key, Collections.<Attribute, Object> emptyMap(), contentProviderService));

        if (result.hasError()) {
            StringBuilder errorTextBuilder = new StringBuilder();
            for (ValidationResult error : result.getValidationResultsForLevel(ValidationResultLevel.ERROR)) {
                errorTextBuilder.append(error.getMessage()).append("; ");
            }
            throw new ServerException(errorTextBuilder.toString());
        }
    }

    private void decorateIconOverride(ContentKey key, ContentNodeModel model) {
        String iconOverride = decorateContextOverride(key, false);
        model.setIconOverride(iconOverride);
    }
}
