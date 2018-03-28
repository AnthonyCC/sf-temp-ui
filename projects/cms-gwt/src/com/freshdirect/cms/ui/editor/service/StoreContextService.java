package com.freshdirect.cms.ui.editor.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContextualAttributeFetchScope;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.ui.editor.domain.AttributeValueSource;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@Service
public class StoreContextService {

    private static final String PATH_SEPARATOR = "|";
    private static final String LABEL_SEPARATOR = " > ";

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private LabelProviderService labelProviderService;

    @Autowired
    private ContentLoaderService contentLoaderService;

    @Autowired
    private ReportingService reportingService;

    public GwtNodeContext contextsOf(ContentKey key) {
        GwtNodeContext gwtContext = new GwtNodeContext();

        List<List<ContentKey>> contextList;
        if (ReportingService.ALL_REPORTING_KEYS.contains(key)) {
            contextList = reportingService.findContextsOf(key).or(Collections.<List<ContentKey>>emptyList());
        } else if (RootContentKey.isRootKey(key)) {
            // [LP-309] actually, context service returns empty list for root keys
            // workaround by making a list of a single context having just the root key
            contextList = Collections.<List<ContentKey>>emptyList();
        } else {
            contextList = contentProviderService.findContextsOf(key);
        }

        if (contextList.isEmpty()) {
            List<ContentKey> singleContext = new ArrayList<ContentKey>();
            singleContext.add(key);

			gwtContext.addContext(buildPath(singleContext), buildLabel(singleContext),
                    new HashMap<String, ContentNodeAttributeI>(), GwtNodeContext.COS_CONTEXTOVERRIDE_COLOR_NOOVERRIDE);
        } else {

            for (List<ContentKey> context : contextList) {
                Map<String, ContentNodeAttributeI> inheritedAttr;
                if (context.size() > 1) {
                    inheritedAttr = buildInheritedAttributes(key, context.get(1));
                } else {
                    inheritedAttr = new HashMap<String, ContentNodeAttributeI>();
                }

				gwtContext.addContext(buildPath(context), buildLabel(context), inheritedAttr,
						buildCosContextColor(context));
            }
        }

        return gwtContext;
    }

	private String buildCosContextColor(List<ContentKey> context) {
        String cosContextStyle = GwtNodeContext.COS_CONTEXTOVERRIDE_COLOR_NOOVERRIDE;
        ContentKey departmentContentKey = context.get(context.size() - 2);
        if (ContentType.Department.equals(departmentContentKey.getType())) {
            cosContextStyle = contentLoaderService.decorateContextOverride(departmentContentKey, true);
        }

        return cosContextStyle;
	}

    // Label: "FDX > FDX FRU > FDX OTHER > Lady Apple"
    private String buildLabel(List<ContentKey> context) {
        final Joiner labelJoiner = Joiner.on(LABEL_SEPARATOR).skipNulls();

        List<String> labels = new ArrayList<String>();
        for (ContentKey key : Lists.reverse(context)) {
            labels.add(labelProviderService.labelOfVirtualContentKey(key));
        }

        return labelJoiner.join(labels);
    }

    // Path:  "|Store:FDX|Department:fdx_fru|Category:fdx_other|Product:apl_lady"
    private String buildPath(List<ContentKey> context) {
        final Joiner pathJoiner = Joiner.on(PATH_SEPARATOR).skipNulls();
        return PATH_SEPARATOR + pathJoiner.join(Lists.reverse(context));
    }

    private Map<String, ContentNodeAttributeI> buildInheritedAttributes(ContentKey key, ContentKey parentKey) {
        Map<String, ContentNodeAttributeI> inheritedAttr = new HashMap<String, ContentNodeAttributeI>();

        Map<Attribute, Object> allAttributesInContext = contentProviderService.fetchAllContextualizedAttributesForContentKey(key, parentKey, ContextualAttributeFetchScope.EXCLUDE_MODEL_VALUES);
        for (Map.Entry<Attribute, Object> entry : allAttributesInContext.entrySet()) {
            final Attribute attribute = entry.getKey();
            if (attribute.getFlags().isInheritable()) {

                ContentNodeAttributeI translatedAttribute = contentLoaderService.translateAttribute(key, attribute, null, entry.getValue(), AttributeValueSource.INHERITED);
                inheritedAttr.put(attribute.getName(), translatedAttribute);
            }
        }

        return inheritedAttr;
    }
}
