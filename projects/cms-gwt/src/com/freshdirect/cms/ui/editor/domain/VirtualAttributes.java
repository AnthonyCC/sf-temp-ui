package com.freshdirect.cms.ui.editor.domain;

import static com.freshdirect.cms.core.domain.ContentType.Banner;
import static com.freshdirect.cms.core.domain.ContentType.Category;
import static com.freshdirect.cms.core.domain.ContentType.Department;
import static com.freshdirect.cms.core.domain.ContentType.ErpCharacteristic;
import static com.freshdirect.cms.core.domain.ContentType.ErpCharacteristicValue;
import static com.freshdirect.cms.core.domain.ContentType.ErpClass;
import static com.freshdirect.cms.core.domain.ContentType.ErpSalesUnit;
import static com.freshdirect.cms.core.domain.ContentType.ImageBanner;
import static com.freshdirect.cms.core.domain.ContentType.Module;
import static com.freshdirect.cms.core.domain.ContentType.Section;
import static com.freshdirect.cms.core.domain.ContentType.Store;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.booleanAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.doubleAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.integerAttribute;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.linkManyOf;
import static com.freshdirect.cms.core.domain.builder.AttributeBuilderSupport.stringAttribute;

import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.Scalar;

/**
 * Collection of definitions of attributes generally not part of CMS
 * available only for CMS Editor.
 *
 * Their values are obtained by various other sources.
 *
 * @author segabor
 *
 */
public final class VirtualAttributes {

    public static final class Sku {
        public static final Scalar promoPrice = (Scalar) doubleAttribute("price")
            .readOnly()
            .build();
        public static final Scalar UNAVAILABILITY_STATUS = (Scalar) stringAttribute("UNAVAILABILITY_STATUS")
            .readOnly()
            .build();
        public static final Scalar materialVersion = (Scalar) integerAttribute("version")
            .readOnly()
            .build();
    }

    public static final class ErpMaterial {
        public static final Scalar NAME = (Scalar) stringAttribute("name")
            .readOnly()
            .build();
        public static final Scalar DESCRIPTION = (Scalar) stringAttribute("DESCRIPTION")
            .readOnly()
            .build();
        public static final Scalar UPC = (Scalar) stringAttribute("UPC")
            .readOnly()
            .build();
        public static final Scalar atpRule = (Scalar) integerAttribute("ATP_RULE")
            .readOnly()
            .build();
        public static final Scalar leadTime = (Scalar) integerAttribute("LEAD_TIME")
            .readOnly()
            .build();
        public static final Scalar alcoholicContent = (Scalar) booleanAttribute("ALCOHOLIC_CONTENT")
            .readOnly()
            .build();
        public static final Scalar taxable = (Scalar) booleanAttribute("TAXABLE")
            .readOnly()
            .build();
        public static final Scalar kosher = (Scalar) booleanAttribute("KOSHER_PRODUCTION")
            .readOnly()
            .build();
        public static final Scalar platter = (Scalar) booleanAttribute("PLATTER")
            .readOnly()
            .build();
        public static final Scalar blockedDays = (Scalar) stringAttribute("BLOCKED_DAYS")
            .readOnly()
            .build();

        public static final Relationship classes = (Relationship) linkManyOf(ErpClass).toName("classes")
            .readOnly()
            .build();

        public static final Relationship salesUnits = (Relationship) linkManyOf(ErpSalesUnit).toName("salesUnits")
            .readOnly()
            .build();
    }

    public static final class ErpClass {
        public static final Relationship characteristics = (Relationship) linkManyOf(ErpCharacteristic).toName("characteristics")
            .readOnly()
            .build();
    }

    public static final class ErpCharacteristic {
        public static final Scalar name = (Scalar) stringAttribute("name")
            .readOnly()
            .build();

        public static final Relationship values = (Relationship) linkManyOf(ErpCharacteristicValue).toName("values")
            .readOnly()
            .build();
    }

    public static final class ErpCharacteristicValue {
        public static final Scalar FULL_NAME = (Scalar) stringAttribute("FULL_NAME")
            .readOnly()
            .build();

        public static final Scalar name = (Scalar) stringAttribute("name")
            .readOnly()
            .build();
    }

    public static final class Category {

        public static final Relationship consumedByVirtualCategory = (Relationship) linkManyOf(Category).toName("consumedByVirtualCategory")
                .readOnly()
                .build();
        public static final Relationship consumedByDepartmentCarousel = (Relationship) linkManyOf(Department).toName("consumedByDepartmentCarousel")
                .readOnly()
                .build();
        public static final Relationship consumedByCategoryCarousel = (Relationship) linkManyOf(Category).toName("consumedByCategoryCarousel")
                .readOnly()
                .build();
        public static final Relationship consumedByStoreCarousel = (Relationship) linkManyOf(Store).toName("consumedByStoreCarousel")
                .readOnly()
                .build();

        public static final Relationship consumedBySection = (Relationship) linkManyOf(Section).toName("consumedBySection")
                .readOnly()
                .build();
        public static final Relationship consumedByImageBanner = (Relationship) linkManyOf(ImageBanner).toName("consumedByImageBanner")
                .readOnly()
                .build();
        public static final Relationship consumedByTabletFeaturedCategories = (Relationship) linkManyOf(Category).toName("consumedByTabletFeaturedCategories")
                .readOnly()
                .build();
        public static final Relationship consumedByModule = (Relationship) linkManyOf(Module).toName("consumedByModule")
                .readOnly()
                .build();
        public static final Relationship consumedByBanner = (Relationship) linkManyOf(Banner).toName("consumedByBanner")
                .readOnly()
                .build();
    }
}
