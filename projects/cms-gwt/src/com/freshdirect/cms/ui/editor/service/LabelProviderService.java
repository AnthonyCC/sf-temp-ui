package com.freshdirect.cms.ui.editor.service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.ContentTypes.ConfiguredProduct;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.persistence.erps.data.ErpCharacteristicKey;
import com.freshdirect.cms.persistence.erps.data.ErpCharacteristicValueKey;
import com.freshdirect.cms.persistence.erps.data.MaterialData;
import com.freshdirect.cms.persistence.service.ERPSDataService;
import com.freshdirect.cms.ui.editor.AttributeLabelKey;
import com.freshdirect.cms.ui.editor.AttributeLabels;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class LabelProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LabelProviderService.class);

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ERPSDataService erpsDataService;

    @Autowired
    private ReportingService reportingService;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    public String labelOfVirtualContentKey(ContentKey key) {
        StringBuilder label = new StringBuilder();

        Optional<String> optionalReportingLabel = reportingService.labelOfContentKey(key);
        if (optionalReportingLabel.isPresent()) {
            label.append(optionalReportingLabel.get());
        } else {
            label.append(labelOfContentKey(key));
        }

        return label.toString();
    }

    public String labelOfContentKey(ContentKey key) {
        String label = null;

        switch (key.type) {
            case DomainValue:
                label = getDomainValueLabel(key);
                break;
            case Html:
            case Image:
            case MediaFolder:
                label = getMediaLabel(key);
                break;
            case Sku:
                label = getSkuLabel(key);
                break;
            case ConfiguredProduct:
                label = getConfiguredProductLabel(key);
                break;
            case ErpMaterial:
                label = getMaterialLabel(key.getId());
                break;
            case ErpSalesUnit:
                label = getSalesUnitLabel(key.id);
                break;
            case ErpClass:
                label = key.id;
                break;
            case ErpCharacteristic:
                label = getErpCharacteristicLabel(key.id);
                break;
            case ErpCharacteristicValue:
                label = getErpCharacteristicValueLabel(key.id);
                break;
            case SearchRelevancyList:
            case SearchRelevancyHint:
            case Synonym:
            case SpellingSynonym:
            case WordStemmingException:
                label = getSearchRelevancyLabel(key);
                break;
            case Tile:
            case TileList:
                label = getTileLabel(key);
                break;
            default:
                break;
        }

        if (label == null) {
            label = getAttributeLabel(key);
        }

        return label != null ? label : key.id;
    }

    private String getSalesUnitLabel(String salesUnitId) {
        return erpsDataService.getSalesUnitLabel(salesUnitId);
    }

    private String getErpCharacteristicLabel(String erpCharacteristicId) {
        return ErpCharacteristicKey.nameExtractedFromContentId(erpCharacteristicId);
    }

    private String getErpCharacteristicValueLabel(String erpCharacteristicValueId) {
        final ErpCharacteristicValueKey valueKey = new ErpCharacteristicValueKey(erpCharacteristicValueId);

        String label = null;
        Map<String, Map<String, Map<String, String>>> materialCharacteristics = erpsDataService.fetchMaterialCharacteristics();
        charValueLabelLookupLoop: for (Map<String, Map<String, String>> charData : materialCharacteristics.values() ) {
            Map<String, String> charValueData = charData.get(valueKey.getCharacteristicName());
            if (charValueData != null) {
                label = charValueData.get(valueKey.getCharacteristicValueName());
                if (label != null) {
                    break charValueLabelLookupLoop;
                }
            }
        }
        return label != null ? label : valueKey.getCharacteristicValueName();
    }

    public String getMaterialLabel(String materialId) {
        String label = materialId;

        Map<String, MaterialData> materialDataMap = erpsDataService.fetchMaterialData();

        if (materialDataMap.containsKey(materialId)) {
            MaterialData materialData = materialDataMap.get(materialId);

            String name = materialData.getName();
            String desc = materialData.getDescription();

            if (Strings.isNullOrEmpty(name)) {
                name = materialId;
            }

            label = Strings.isNullOrEmpty(desc) ? name : MessageFormat.format("{0} ({1})", name, WordUtils.capitalizeFully(desc));
        }

        return label;
    }

    public String getAttributeLabel(ContentType type, Attribute attribute) {
        String label = null;

        AttributeLabelKey key = AttributeLabelKey.keyOf(type, attribute);

        // lookup attribute in type first
        label = AttributeLabels.ATTRIBUTE_LABELS.get(key);

        if (label == null && attribute.getFlags().isInheritable()) {
            // extend attribute lookup to inherited types
            Set<ContentType> subTypes = contentTypeInfoService.getReachableContentTypes(type);
            if (subTypes != null && !subTypes.isEmpty()) {
                for (ContentType subType : subTypes) {
                    key = AttributeLabelKey.keyOf(subType, attribute);
                    label = AttributeLabels.ATTRIBUTE_LABELS.get(key);

                    if (label != null) {
                        break;
                    }
                }
            }
        }

        return Strings.isNullOrEmpty(label) ? attribute.getName() : label;
    }

    public Map<String, String> getEnumLabels(ContentType type, Attribute attribute) {
        Map<String, String> enumValueLabels = null;

        AttributeLabelKey key = AttributeLabelKey.keyOf(type, attribute);

        // lookup attribute in type first
        enumValueLabels = AttributeLabels.ENUM_LABELS.get(key);

        if (enumValueLabels == null && attribute.getFlags().isInheritable()) {
            // extend attribute lookup to inherited types
            Set<ContentType> subTypes = contentTypeInfoService.getReachableContentTypes(type);
            if (subTypes != null && !subTypes.isEmpty()) {
                for (ContentType subType : subTypes) {
                    key = AttributeLabelKey.keyOf(subType, attribute);
                    enumValueLabels = AttributeLabels.ENUM_LABELS.get(key);

                    if (enumValueLabels != null) {
                        break;
                    }
                }
            }
        }

        return enumValueLabels;
    }

    public Map<ContentKey, String> labelsOf(Collection<ContentKey> keys) {
        Map<ContentKey, String> labels = new HashMap<ContentKey, String>();

        for (ContentKey key : keys) {
            labels.put(key, labelOfVirtualContentKey(key));
        }

        return labels;
    }

    private String getAttributeLabel(ContentKey contentKey) {
        final List<Attribute> attributeList = Arrays.<Attribute> asList(ContentTypes.Sku.FULL_NAME, ContentTypes.Sku.NAV_NAME, ContentTypes.Sku.GLANCE_NAME,
                ContentTypes.FDFolder.name, ContentTypes.Html.title, ContentTypes.ImageBanner.Name, ContentTypes.SuperDepartment.PAGE_TITLE,
                ContentTypes.SuperDepartment.PAGE_TITLE_FDX);

        Map<Attribute, Object> values = contentProviderService.getAttributeValues(contentKey, attributeList);

        for (Attribute attribute : attributeList) {
            if (values.keySet().contains(attribute) && values.get(attribute) != null) {
                return (String) values.get(attribute);
            }
        }

        return null;
    }

    private String getTileLabel(ContentKey contentKey) {
        if (ContentType.Tile == contentKey.type) {
            Optional<Object> attributeValue = contentProviderService.getAttributeValue(contentKey, ContentTypes.Tile.media);
            if (attributeValue.isPresent()) {
                return "Tile[" + contentKey.id + ", media:" + attributeValue.get() + "]";
            }
        } else if (ContentType.TileList == contentKey.type) {
            Optional<Object> attributeValue = contentProviderService.getAttributeValue(contentKey, ContentTypes.TileList.filter);
            if (attributeValue.isPresent()) {
                return "TileList[" + contentKey.id + ", filter:" + attributeValue.get() + "]";
            }
        }

        return null;
    }

    private String getSearchRelevancyLabel(ContentKey contentKey) {
        String label = null;

        switch (contentKey.type) {
            case SearchRelevancyList:
                label = formattedLabelFromAttribute(contentKey, ContentTypes.SearchRelevancyList.Keywords, "Search for: {0}");
                break;
            case SearchRelevancyHint:
                label = formattedLabelFromTwoAttributes(contentKey, ContentTypes.SearchRelevancyHint.category, ContentTypes.SearchRelevancyHint.score, "{0} score : {1}");
                break;
            case Synonym:
                label = formattedLabelFromTwoAttributes(contentKey, ContentTypes.Synonym.word, ContentTypes.Synonym.synonymValue, "Synonyms of ''{0}'': ''{1}''");
                break;
            case SpellingSynonym:
                label = formattedLabelFromTwoAttributes(contentKey, ContentTypes.SpellingSynonym.word, ContentTypes.SpellingSynonym.synonymValue,
                        "Spelling synonyms of ''{0}'': ''{1}''");
                break;
            case WordStemmingException:
                label = formattedLabelFromAttribute(contentKey, ContentTypes.WordStemmingException.word, "DEPRECATED Bad singular form : {0}");
                break;
            default:
                break;
        }

        return label;
    }

    private String getConfiguredProductLabel(ContentKey contentKey) {

        Optional<Object> optionalSkuKey = contentProviderService.getAttributeValue(contentKey, ConfiguredProduct.SKU);
        if (!optionalSkuKey.isPresent()) {
            return null;
        }

        ContentKey skuKey = (ContentKey) optionalSkuKey.get();

        Set<ContentKey> parentKeys = contentProviderService.getParentKeys((ContentKey) optionalSkuKey.get());
        if (parentKeys == null || parentKeys.isEmpty()) {
            return null;
        }

        return getAttributeLabel(parentKeys.iterator().next());
    }

    private String getSkuLabel(ContentKey skuKey) {
        Map<String, String> skuMaterialAssociations = erpsDataService.fetchSkuMaterialAssociations();
        Map<String, MaterialData> materialData = erpsDataService.fetchMaterialData();

        String materialId = skuMaterialAssociations.get(skuKey.id);

        String label = skuKey.id;
        if (materialId != null && materialData.get(materialId) != null && materialData.get(materialId).getAvailabilityStatus() != null) {
            MaterialData matData = materialData.get(materialId);
            label = skuKey.id + " (" + matData.getAvailabilityStatus().name() + ")";
        }

        return label;
    }

    private String getMediaLabel(ContentKey contentKey) {
        String label = null;

        Optional<Media> optionalMedia = mediaService.getMediaByContentKey(contentKey);
        if (optionalMedia.isPresent()) {
            final Media media = optionalMedia.get();
            String path = media.getUri();
            if ("/".equals(path)) {
                label = "Media";
            } else {
                int idx = path.lastIndexOf("/");
                String fileName = path.substring(idx + 1);
                String title = null;
                if (ContentType.Html.equals(media.getContentKey().type)) {
                    Optional<Object> optionalTitle = contentProviderService.getAttributeValue(contentKey, ContentTypes.Html.title);
                    if(optionalTitle.isPresent()){
                        title = optionalTitle.get().toString();
                    }
                }
                label = title == null ? fileName : fileName + " (" + title + ")";
            }
        } else {
            LOGGER.error("Label for media " + contentKey + " could not be provided as it does not exist");
        }
        return label;
    }

    private String getDomainValueLabel(ContentKey contentKey) {
        StringBuilder sb = new StringBuilder();
        Set<ContentKey> parentKeys = contentProviderService.getParentKeys(contentKey);

        if (!parentKeys.isEmpty()) {
            ContentKey parentKey = parentKeys.iterator().next();

            String domainLabel = formattedLabelFromAttribute(parentKey, ContentTypes.Domain.Label, null);
            if (domainLabel == null) {
                domainLabel = parentKey.id;
            }

            sb.append(domainLabel).append(": ");
        }

        String domainValueLabel = formattedLabelFromAttribute(contentKey, ContentTypes.DomainValue.Label, null);
        if (domainValueLabel == null) {
            domainValueLabel = contentKey.id;
        }

        sb.append(domainValueLabel);

        return sb.toString();
    }

    private String formattedLabelFromAttribute(ContentKey contentKey, Attribute attribute, String format) {
        String label = null;

        Optional<Object> attributeValue = contentProviderService.getAttributeValue(contentKey, attribute);
        final String stringValue = attributeValue.isPresent() ? attributeValue.get().toString() : "<not-set>";
        if (format != null) {
            label = MessageFormat.format(format, stringValue);
        } else {
            label = stringValue;
        }

        return label;
    }

    private String formattedLabelFromTwoAttributes(ContentKey contentKey, Attribute attribute, Attribute attribute2, String format) {
        String label = null;

        Optional<Object> attributeValue = contentProviderService.getAttributeValue(contentKey, attribute);
        Optional<Object> secondAttributeValue = contentProviderService.getAttributeValue(contentKey, attribute2);
        final String stringValue = attributeValue.isPresent() ? attributeValue.get().toString() : "<not-set>";
        final String stringValue2 = secondAttributeValue.isPresent() ? secondAttributeValue.get().toString() : "<not-set>";
        if (format != null) {
            label = MessageFormat.format(format, stringValue, stringValue2);
        } else {
            label = stringValue;
        }

        return label;
    }

}
