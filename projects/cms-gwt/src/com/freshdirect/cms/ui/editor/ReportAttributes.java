package com.freshdirect.cms.ui.editor;

import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.stringAttribute;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport;

public class ReportAttributes {

    public static final class CmsQuery {
        public static final Attribute name = stringAttribute("name")
                .readOnly()
                .build();
        public static final Attribute description = stringAttribute("description")
                .readOnly()
                .build();
        public static final Attribute results = AttributeBuilderSupport.linkMany().toName("CmsQuery$results").navigable().readOnly()
                .build();

    }

    public static final class CmsReport {
        public static final Attribute name = stringAttribute("name")
                .readOnly()
                .build();
        public static final Attribute description = stringAttribute("description")
                .readOnly()
                .build();
        public static final Attribute results = AttributeBuilderSupport.linkMany().toName("CmsReport$results")
                .navigable()
                .readOnly()
                .build();

    }
}
