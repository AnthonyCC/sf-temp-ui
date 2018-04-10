package com.freshdirect.cms.ui.editor.reports.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.entity.ContentChangeDetailEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.service.ContentChangeControlService;
import com.freshdirect.cms.contentvalidation.correction.PrimaryHomeCorrectionService;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.ContextualAttributeFetchScope;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.ui.editor.reports.data.HiddenProduct;
import com.freshdirect.cms.ui.editor.reports.data.HiddenProductByChange;
import com.freshdirect.cms.ui.editor.reports.data.HiddenProductReason;
import com.google.common.base.Optional;

@Service
public class HiddenProductsService {

    private static final ContentKey ARCHIVE_DEPARTMENT_KEY = ContentKeyFactory.get(ContentType.Department, "Archive");

    private static final Comparator<HiddenProductByChange> SORT_HIDDEN_PRODUCTS_BY_CHANGE = new Comparator<HiddenProductByChange>() {

        @Override
        public int compare(HiddenProductByChange p1, HiddenProductByChange p2) {

            int n = -(p1.getChangeDate().compareTo(p2.getChangeDate()));
            if (n == 0) {
                n = p1.getProductKey().id.compareTo(p2.getProductKey().id);
            }

            return n;
        }
    };

    private static final Comparator<HiddenProduct> SORT_HIDDEN_PRODUCTS = new Comparator<HiddenProduct>() {

        @Override
        public int compare(HiddenProduct p1, HiddenProduct p2) {

            int n = p1.getStoreKey().id.compareTo(p2.getStoreKey().id);
            if (n == 0) {
                n = p1.getProductKey().id.compareTo(p2.getProductKey().id);
            }

            return n;
        }
    };

    @Autowired
    private ContentProviderService contentProviderService;

    @Autowired
    private ContentChangeControlService contentChangeControlService;

    @Autowired
    private PrimaryHomeCorrectionService primaryHomeCorrectionService;

    public List<HiddenProduct> queryHiddenProducts() {
        List<HiddenProduct> queryResult = new ArrayList<HiddenProduct>();

        for (final ContentKey productKey : contentProviderService.getContentKeysByType(ContentType.Product)) {
            Map<ContentKey, ContentKey> primaryHomesMap = contentProviderService.findPrimaryHomes(productKey);

            for (Map.Entry<ContentKey, ContentKey> primaryHomeInStore : primaryHomesMap.entrySet()) {
                final ContentKey storeKey = primaryHomeInStore.getKey();
                final ContentKey homeCategoryKey = primaryHomeInStore.getValue();
                final HiddenProductReason reason = isProductHidden(storeKey, productKey, homeCategoryKey);
                if (reason.isHidden()) {
                    HiddenProduct hiddenProduct = new HiddenProduct(storeKey, productKey, homeCategoryKey, reason);
                    queryResult.add(hiddenProduct);
                }
            }
        }

        Collections.sort(queryResult, SORT_HIDDEN_PRODUCTS);

        return queryResult;
    }

    public List<HiddenProductByChange> queryHiddenProductsInChanges() {
        List<HiddenProductByChange> queryResult = new ArrayList<HiddenProductByChange>();

        // determine time frame
        final Date endDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, -7);
        final Date startDate = cal.getTime();

        final Set<ContentChangeSetEntity> changeSets = contentChangeControlService.queryChangeSetEntities(null, null, startDate, endDate);

        for (ContentChangeSetEntity changeSet : changeSets) {
            for (ContentChangeEntity changedNode : changeSet.getChanges()) {
                final ContentKey contentKey = changedNode.getContentKey().get();

                for (ContentChangeDetailEntity detail : changedNode.getDetails()) {
                    final String attributeName = detail.getAttributeName();

                    if (ContentType.Product == contentKey.type && ContentTypes.Product.PRIMARY_HOME.getName().equals(attributeName)) {

                        Optional<HiddenProductByChange> hiddenProduct = checkProductHiddenInPrimaryHomes(contentKey, changeSet);
                        if (hiddenProduct.isPresent()) {
                            queryResult.add(hiddenProduct.get());
                        }

                    } else if ((ContentTypes.Product.HIDE_URL.getName().equals(attributeName) || ContentTypes.Product.REDIRECT_URL.getName().equals(attributeName))
                            && isRedirectUrlMeaningful(detail.getNewValue())) {

                        Collection<ContentKey> candidateProducts = new HashSet<ContentKey>();
                        collectProductKeys(contentKey, candidateProducts);

                        for (ContentKey productKey : candidateProducts) {
                            Optional<HiddenProductByChange> testResult = checkProductHiddenInPrimaryHomes(productKey, changeSet);
                            if (testResult.isPresent()) {
                                queryResult.add(testResult.get());
                            }
                        }
                    }
                }
            }
        }

        Collections.sort(queryResult, SORT_HIDDEN_PRODUCTS_BY_CHANGE);

        return queryResult;
    }

    private Optional<HiddenProductByChange> checkProductHiddenInPrimaryHomes(ContentKey productKey, ContentChangeSetEntity changeSet) {
        Map<ContentKey, ContentKey> storesAndHomes = primaryHomeCorrectionService.pickPrimaryHomes(productKey, contentProviderService);
        if (storesAndHomes == null || storesAndHomes.isEmpty()) {
            return Optional.absent();
        }

        HiddenProductByChange hiddenProduct = null;
        for (Map.Entry<ContentKey, ContentKey> storeAndHome : storesAndHomes.entrySet()) {
            final ContentKey storeKey = storeAndHome.getKey();
            final ContentKey homeCategoryKey = storeAndHome.getValue();
            final HiddenProductReason reason = isProductHidden(storeKey, productKey, homeCategoryKey);
            if (reason.isHidden()) {
                hiddenProduct = new HiddenProductByChange(changeSet.getTimestamp(), changeSet.getUserId(), changeSet.getNote(), productKey, homeCategoryKey, reason);
            }
        }

        return Optional.fromNullable(hiddenProduct);
    }

    private HiddenProductReason isProductHidden(ContentKey storeKey, ContentKey productKey, ContentKey primaryHome) {
        Assert.notNull(storeKey);
        Assert.notNull(productKey);
        Assert.notNull(primaryHome);
        Assert.isTrue(ContentType.Store == storeKey.type);
        Assert.isTrue(ContentType.Product == productKey.type);
        Assert.isTrue(ContentType.Category == primaryHome.type);

        if (contentProviderService.isOrphan(productKey, storeKey)) {
            return HiddenProductReason.ORPHAN;
        }

        if (checkProductIsArchived(productKey)) {
            return HiddenProductReason.ARCHIVED;
        }

        if (checkProductIsHiddenByHideURL(productKey, primaryHome)) {
            return HiddenProductReason.HIDDEN_BY_HIDE_URL;
        }

        if (checkProductIsHiddenByRedirectURL(productKey, primaryHome)) {
            return HiddenProductReason.HIDDEN_BY_REDIRECT_URL;
        }

        return HiddenProductReason.VISIBLE;
    }

    private boolean checkProductIsHiddenByRedirectURL(ContentKey productKey, ContentKey primaryHome) {
        Optional<Object> redirectUrlAttr = contentProviderService.fetchContextualizedAttributeValue(productKey, ContentTypes.Product.REDIRECT_URL, primaryHome,
                ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
        if (redirectUrlAttr.isPresent()) {
            String urlValue = (String) redirectUrlAttr.get();

            if (isRedirectUrlMeaningful(urlValue)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkProductIsHiddenByHideURL(ContentKey productKey, ContentKey primaryHome) {
        Optional<Object> hideUrlAttr = contentProviderService.fetchContextualizedAttributeValue(productKey, ContentTypes.Product.HIDE_URL, primaryHome,
                ContextualAttributeFetchScope.INCLUDE_MODEL_VALUES);
        if (hideUrlAttr.isPresent()) {
            String urlValue = (String) hideUrlAttr.get();

            if (isRedirectUrlMeaningful(urlValue)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkProductIsArchived(ContentKey productKey) {
        List<List<ContentKey>> contexts = contentProviderService.findContextsOf(productKey);
        if (!contexts.isEmpty()) {
            for (List<ContentKey> context : contexts) {
                final int size = context.size();
                if (size > 2 && RootContentKey.STORE_FRESHDIRECT.contentKey.equals(context.get(size - 1)) && ARCHIVE_DEPARTMENT_KEY.equals(context.get(size - 2))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isRedirectUrlMeaningful(String redirectUrl) {
        return redirectUrl != null && !"".equals(redirectUrl) && !"nm".equals(redirectUrl);
    }

    private void collectProductKeys(ContentKey contentKey, Collection<ContentKey> productKeyBucket) {
        Assert.notNull(contentKey);
        Assert.notNull(productKeyBucket);

        switch (contentKey.type) {
            case Department:
                {
                    Optional<Object> value = contentProviderService.getAttributeValue(contentKey, ContentTypes.Department.categories);
                    if (value.isPresent()) {
                        List<ContentKey> categoryKeys = (List<ContentKey>) value.get();
                        for (ContentKey categoryKey : categoryKeys) {
                            collectProductKeys(categoryKey, productKeyBucket);
                        }
                    }
                }
                break;
            case Category:
                {
                    Optional<Object> value = contentProviderService.getAttributeValue(contentKey, ContentTypes.Category.subcategories);
                    if (value.isPresent()) {
                        List<ContentKey> subcategoryKeys = (List<ContentKey>) value.get();
                        for (ContentKey categoryKey : subcategoryKeys) {
                            collectProductKeys(categoryKey, productKeyBucket);
                        }
                    }
                    value = contentProviderService.getAttributeValue(contentKey, ContentTypes.Category.products);
                    if (value.isPresent()) {
                        List<ContentKey> productKeys = (List<ContentKey>) value.get();
                        productKeyBucket.addAll(productKeys);
                    }
                }
                break;
            case Product:
                productKeyBucket.add(contentKey);
                break;
            default:
                break;
        }
    }
}
