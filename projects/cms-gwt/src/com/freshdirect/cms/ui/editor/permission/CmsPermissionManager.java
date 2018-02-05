package com.freshdirect.cms.ui.editor.permission;

import static com.google.common.collect.Iterables.transform;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.ui.editor.UnmodifiableContent;
import com.freshdirect.cms.ui.editor.permission.domain.Permit;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;
import com.freshdirect.cms.ui.model.CmsPermissionHolder;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.GwtUser;
import com.google.common.base.Function;
import com.google.common.collect.Sets;

/**
 * This utility confirms a changeset if payload contains permitted nodes
 *
 * @author segabor
 *
 */
public class CmsPermissionManager {

    private static ContentTypeInfoService contentTypeInfoService = CmsServiceLocator.contentTypeInfoService();
    private static ContextualContentProvider contentProviderService = CmsServiceLocator.contentProviderService();

    private static final Function<ContentType, String> TYPE_TO_STRING_MAPPER = new Function<ContentType, String>() {

        @Override
        public String apply(ContentType contentType) {
            return contentType.toString();
        }
    };

    private static final Function<ContentKey, String> KEY_TO_STRING_MAPPER = new Function<ContentKey, String>() {

        @Override
        public String apply(ContentKey contentKey) {
            return contentKey.toString();
        }
    };

    private CmsPermissionManager() {
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(CmsPermissionManager.class);

    /**
     * Request permission to edit a node in CMS editor
     *
     * @param node
     *            Content node subjected to edit by permission holder
     * @param permissionHolder
     * @param svc
     *
     * @return
     */
    public static Permit requestEditPermission(ContentKey contentKey, Map<Attribute, Object> attributesWithValues, final CmsPermissionHolder permissionHolder) {
        Permit permit = Permit.valueOf(UnmodifiableContent.isModifiable(contentKey));
        if (Permit.ALLOW == permit) {
            Map<ContentKey, Map<Attribute, Object>> changedNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
            changedNodes.put(contentKey, attributesWithValues);

            Map<ContentKey, Permit> result = checkStoreMembershipPermission(changedNodes, permissionHolder);

            permit = Permit.negativeValueOf(Permit.isAnyRejected(result.values()));
        }

        return permit;
    }

    /**
     * Request permission to store changed nodes edited in CMS Editor
     *
     * THe implementation has one limitation: Changed and original nodes should be tested in their respected environment. Contexts must be walked upwards in both unmodified and
     * modified content set. This might result false test when changes interfere.
     *
     * Currently, no other change is respected during test than the changed node itself.
     *
     * @param changedNodes
     *            nodes in scope
     * @param permission
     *            permission holder
     * @param svc
     *            content service
     * @return
     */
    public static Permit requestSaveChangesetPermission(GwtUser user, Map<ContentKey, Map<Attribute, Object>> changedNodes, ContentChangeSource origin) {
        if (!(user instanceof CmsPermissionHolder)) {
            // user object most conform to permissions protocol
            LOGGER.error("[REJECT] User " + user.getName() + " is not conforming to permissions protocol");
            return Permit.REJECT;
        }
        final CmsPermissionHolder permissionHolder = user;

        if (ContentChangeSource.NODE_EDITOR == origin) {

            if (changedNodes.isEmpty()) {
                // no nodes to check ==> PASSED
                // Reason: type of changed node does not belong to store tree
                // no permission is required
                LOGGER.info("[ACCEPT] Empty changeset.");
                return Permit.ALLOW;
            }

            // collect keys
            Set<ContentKey> keys = new HashSet<ContentKey>(changedNodes.keySet());

            // collect nodes with unchanged state
            Map<ContentKey, Map<Attribute, Object>> oldNodes = new HashMap<ContentKey, Map<Attribute, Object>>(keys.size());
            for (final ContentKey key : keys) {
                final Map<Attribute, Object> node = contentProviderService.getAllAttributesForContentKey(key);
                if (node != null)
                    oldNodes.put(key, node);
            }

            final boolean hasAnyTypeBasedPermission = permissionHolder.isAnyContentTypeBasedPermission();

            // test permission on unchanged content objects first
            final Map<ContentKey, Permit> oldNodeStoreResult = checkStoreMembershipPermission(oldNodes, permissionHolder);
            for (Map.Entry<ContentKey, Permit> entry : oldNodeStoreResult.entrySet()) {
                if (!Permit.ALLOW.equals(entry.getValue())) {
                    // TODO create and delete checks are missing
                    if (hasAnyTypeBasedPermission) {
                        entry.setValue(checkTypeBasedEditPermission(permissionHolder, entry.getKey().type));
                    } else {
                        entry.setValue(Permit.REJECT);
                    }
                }
            }
            if (Permit.isAnyRejected(oldNodeStoreResult.values())) {
                LOGGER.error("[REJECT] User " + user.getName() + " (old nodes)");
                return Permit.REJECT;
            }
            // handle prototype node, which is not part of CMSManager content service yet.
            Map<ContentKey, Map<Attribute, Object>> completeNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
            for (ContentKey changedNodeKey : changedNodes.keySet()) {
                Map<Attribute, Object> originalAttributes = oldNodes.get(changedNodeKey);
                Map<Attribute, Object> changedAttributes = changedNodes.get(changedNodeKey);
                Map<Attribute, Object> completeNode = new HashMap<Attribute, Object>();
                for (Attribute attribute : originalAttributes.keySet()) {
                    completeNode.put(attribute, originalAttributes.get(attribute));
                }
                for (Attribute attribute : changedAttributes.keySet()) {
                    completeNode.put(attribute, changedAttributes.get(attribute));
                }
                completeNodes.put(changedNodeKey, completeNode);
            }
            // test permission on changed content objects
            final Map<ContentKey, Permit> newNodeStoreResult = checkStoreMembershipPermission(completeNodes, permissionHolder);
            for (Map.Entry<ContentKey, Permit> entry : newNodeStoreResult.entrySet()) {
                if (!Permit.ALLOW.equals(entry.getValue())) {
                    if (hasAnyTypeBasedPermission) {
                        entry.setValue(checkTypeBasedEditPermission(permissionHolder, entry.getKey().type));
                    } else {
                        entry.setValue(Permit.REJECT);
                    }
                }
            }
            if (Permit.isAnyRejected(newNodeStoreResult.values())) {
                LOGGER.error("[REJECT] User " + user.getName() + " (new nodes)");
                return Permit.REJECT;
            }

            return Permit.ALLOW;
        }

        return Permit.ALLOW;
    }

    private static final Set<ContentKey> OTHER_ROOT_KEYS = new HashSet<ContentKey>();
    static {
        OTHER_ROOT_KEYS.add(RootContentKey.SHARED_RESOURCES.contentKey);
        OTHER_ROOT_KEYS.add(RootContentKey.MEDIA_ROOT.contentKey); // not editable !!
        OTHER_ROOT_KEYS.add(RootContentKey.RECIPES.contentKey); // FD only
        OTHER_ROOT_KEYS.add(RootContentKey.STARTER_LISTS.contentKey);
        OTHER_ROOT_KEYS.add(RootContentKey.DONATION_ORGANIZATIONS.contentKey);
    }

    private static final Set<ContentType> OTHER_CONTENT_TYPES = new HashSet<ContentType>();
    static {
        OTHER_CONTENT_TYPES.add(ContentType.CmsQuery);
        OTHER_CONTENT_TYPES.add(ContentType.CmsReport);
    }

    private static final Set<ContentKey> BUILTIN_CONTENT_KEYS = new HashSet<ContentKey>() {

        {
            add(ReportingService.CMS_REPORTS_FOLDER_KEY);
            add(ReportingService.SMARTSTORE_REPORTS_FOLDER_KEY);
            add(ReportingService.CMS_QUERIES_FOLDER_KEY);

            addAll(ReportingService.CMS_REPORT_NODE_KEYS);
            addAll(ReportingService.SMARTSTORE_REPORT_NODE_KEYS);
            addAll(ReportingService.CMS_QUERY_NODE_KEYS);
        }
    };

    private static final Collection<CustomPermissionRule> CUSTOM_RULES = Arrays.asList(CustomRuleValidator.FOODKICK_FEED_RULE, CustomRuleValidator.FOODKICK_PICKLIST_RULE,
            CustomRuleValidator.FOODKICK_SCHEDULE_RULE, CustomRuleValidator.FOODKICK_SECTION_RULE, CustomRuleValidator.FOODKICK_DARKSTORE_RULE);

    public static class StorePermissionInput {

        public final Collection<ContentKey> allowedStoreKeys;
        public final Permit permitForAnyStore;
        public final Permit permitForFD;
        public final Permit permitForOther;

        public StorePermissionInput(CmsPermissionHolder permissionHolder) {
            this.allowedStoreKeys = Collections.unmodifiableCollection(CmsPermissionUtility.getAllowedStoreKeys(permissionHolder));

            // Required for 'other' cases
            this.permitForAnyStore = Permit.valueOf(CmsPermissionUtility.isAllowedInAnyStoreContext(permissionHolder));
            this.permitForFD = Permit.valueOf(permissionHolder.isCanChangeFDStore());
            this.permitForOther = Permit.valueOf(permissionHolder.isCanChangeOtherNodes());
        }
    }

    /**
     * Perform store permission check on tree-type nodes
     *
     * @param nodes
     * @param permissionHolder
     * @param svc
     *
     * @return set of permits assigned to keys of tested nodes
     */
    protected static Map<ContentKey, Permit> checkStoreMembershipPermission(final Map<ContentKey, Map<Attribute, Object>> nodes, final CmsPermissionHolder permissionHolder) {
        final Map<ContentKey, Permit> result = new HashMap<ContentKey, Permit>(nodes.size());

        final StorePermissionInput input = new StorePermissionInput(permissionHolder);

        for (final ContentKey nodeKey : nodes.keySet()) {
            final Permit permit = checkStorePermission(nodeKey, input);
            if (permit != null) {
                result.put(nodeKey, permit);
            }
        }

        return result;
    }

    /**
     * Run a store permission check on a single node
     *
     * @param node
     * @param input
     * @param svc
     *
     * @return Permit result or null if node is not subject to store permissons (ie. orphan)
     */
    public static Permit checkStorePermission(final ContentKey nodeKey, final StorePermissionInput input) {

        final Set<ContentKey> rootKeys = new HashSet<ContentKey>();
        // collect top keys
        if (RootContentKey.isRootKey(nodeKey)) {
            rootKeys.add(nodeKey);
        } else {
            List<List<ContentKey>> contexts = contentProviderService.findContextsOf(nodeKey);
            for (List<ContentKey> context : contexts) {
                if (!context.isEmpty()) {
                    ContentKey k = context.get(context.size() - 1);
                    if (RootContentKey.isRootKey(k)) {
                        rootKeys.add(k);
                    }
                }
            }
        }

        // <1> SINGLE-ROOT MATCH
        if (rootKeys.size() == 1 && OTHER_ROOT_KEYS.contains(rootKeys.iterator().next())) {
            final ContentKey theOnlyKey = rootKeys.iterator().next();

            if (RootContentKey.RECIPES.contentKey.equals(theOnlyKey)) {
                // recipes are actually members of FD store
                return input.permitForFD;
            } else if (RootContentKey.SHARED_RESOURCES.contentKey.equals(theOnlyKey)) {
                return input.permitForAnyStore;
            } else {
                return input.permitForOther;
            }
        }

        // <2> SELF-REFERENCE MATCH
        if (rootKeys.size() == 1 && nodeKey.equals(rootKeys.iterator().next())) {
            // CmsQueries and Cms Forms nodes fall into this case
            final ContentKey theOnlyKey = rootKeys.iterator().next();
            if (OTHER_CONTENT_TYPES.contains(theOnlyKey.type)) {
                // CMS Queries and Reports cannot be modified
                // they are always read-only

                /// LOGGER.debug("<2> " + nodeKey + " => " + result.get(nodeKey));
                return Permit.REJECT;
            }
        }

        // <3> ORPHAN TEST
        if (rootKeys.size() == 0) {
            /// LOGGER.debug("<3> " + nodeKey + " => SKIPPED");
            return input.permitForOther;
        }

        // <4> STORE MEMBERSHIP
        {
            rootKeys.retainAll(input.allowedStoreKeys);
            return Permit.negativeValueOf(rootKeys.isEmpty());

            /// LOGGER.debug("<4> " + nodeKey + " => " + result.get(nodeKey));
        }
    }

    /**
     * It ensures that permission holder is granted to edit a node with the given type
     *
     * @param permissionHolder
     * @param contentType
     *
     * @return
     */
    public static Permit checkTypeBasedEditPermission(final CmsPermissionHolder permissionHolder, final ContentType contentType) {
        return checkTypeBasedPermission(permissionHolder, contentType);
    }

    public static Set<ContentType> getTypesDisallowedToCreate(final ContentKey nodeKey, final CmsPermissionHolder permissionHolder) {
        Set<ContentType> disallowedContentTypes = new HashSet<ContentType>();
        final ContentType nodeType = nodeKey.type;
        for (ContentType relatedContentType : collectRelatedContentTypes(nodeType)) {
            if (Permit.REJECT == checkTypeBasedRelationshipPermission(permissionHolder, nodeType, relatedContentType)) {
                disallowedContentTypes.add(relatedContentType);
            }
        }
        return disallowedContentTypes;
    }

    public static Set<ContentType> getTypesDisallowedToDelete(final ContentKey nodeKey, final CmsPermissionHolder permissionHolder) {
        Set<ContentType> disallowedContentTypes = new HashSet<ContentType>();
        ContentType nodeType = nodeKey.type;
        for (ContentType relatedContentType : collectRelatedContentTypes(nodeType)) {
            if (Permit.REJECT == applyCustomDeleteRules(nodeKey, relatedContentType)) {
                disallowedContentTypes.add(relatedContentType);
            }
        }
        return disallowedContentTypes;
    }

    public static GwtNodePermission setupClientPermissions(final ContentKey ncKey, final CmsPermissionHolder permissionHolder) {
        GwtNodePermission nodePermission = new GwtNodePermission();

        Map<Attribute, Object> nodeValues = contentProviderService.getAllAttributesForContentKey(ncKey);

        /*
         * Evaluate node permissions Input: scope permission, type permissions Output: node editable (or read-only) create and delete-type restrictions
         */
        final Permit scopePermit = CmsPermissionManager.requestEditPermission(ncKey, nodeValues, permissionHolder);
        final boolean hasAnyTypeBasedPermission = permissionHolder.isAnyContentTypeBasedPermission();
        boolean doCalculateCreateDeleteSets = false;
        if (scopePermit == Permit.REJECT) {
            /// ** No scope based permission **

            if (!hasAnyTypeBasedPermission) {
                /// Neither allowed types => node will be READ-ONLY
                nodePermission.setEditable(false);
            } else {
                /// Node has type based permissions
                if (permissionHolder.isContentTypeBasedPermissionEnabled(ncKey.type.toString())) {
                    /// Node type is allowed
                    nodePermission.setEditable(true);

                    doCalculateCreateDeleteSets = true;
                } else {
                    /// Node is not allowed (not in allowed types)
                    nodePermission.setEditable(false);
                }
            }
        } else {
            /// *** Scope based permission is active ***
            nodePermission.setEditable(true);
        }

        // calculate additional restrictions
        if (doCalculateCreateDeleteSets) {
            final Set<ContentType> typesNoCreate = CmsPermissionManager.getTypesDisallowedToCreate(ncKey, permissionHolder);
            final Set<ContentType> typesNoDelete = CmsPermissionManager.getTypesDisallowedToDelete(ncKey, permissionHolder);
            nodePermission.setDisallowedForCreateTypes(Sets.newHashSet(transform(typesNoCreate, TYPE_TO_STRING_MAPPER)));
            nodePermission.setDisallowedForDeleteTypes(Sets.newHashSet(transform(typesNoDelete, TYPE_TO_STRING_MAPPER)));
        }

        // multi-homed products
        final ContentType type = ncKey.type;
        if (type.equals(ContentType.Product) || type.equals(ContentType.ConfiguredProduct) || type.equals(ContentType.ConfiguredProductGroup)) {
            final Set<ContentKey> allowedStoreKeys = CmsPermissionUtility.getAllowedStoreKeys(permissionHolder);
            nodePermission.setAllowedStores(Sets.newHashSet(transform(allowedStoreKeys, KEY_TO_STRING_MAPPER)));
        }

        return nodePermission;
    }

    /**
     * Override original permission by checking additional rules on the given node
     *
     * @param typePermit
     * @param node
     * @param relatedType
     * @return
     */
    private static Permit applyCustomDeleteRules(ContentKey nodeKey, ContentType relatedType) {
        boolean isAnyRuleApplied = CustomRuleValidator.getInstance().validate(nodeKey, relatedType, CUSTOM_RULES);
        if (isAnyRuleApplied) {
            return Permit.REJECT;
        } else {
            return Permit.ALLOW;
        }
    }

    /**
     * Collect all related content types of the given node.
     *
     * @param type
     *            Source type
     * @param contentService
     *            Content Service
     *
     * @return Type of children keys reachable via relationships of the given type
     */
    private static Set<ContentType> collectRelatedContentTypes(final ContentType type) {
        Set<ContentType> relatedContentTypes = new HashSet<ContentType>();
        Set<Attribute> selfAttributes = contentTypeInfoService.collectContentTypeAttributes(type);
        for (Attribute attributeDef : selfAttributes) {
            if (attributeDef instanceof Relationship) {
                relatedContentTypes.addAll(Arrays.asList(((Relationship) attributeDef).getDestinationTypes()));
            }
        }
        return relatedContentTypes;
    }

    private static Permit checkTypeBasedRelationshipPermission(CmsPermissionHolder permissionHolder, ContentType parentType, ContentType childType) {
        final Permit childPermit = checkTypeBasedPermission(permissionHolder, childType);
        final Permit parentPermit = checkTypeBasedPermission(permissionHolder, parentType);
        return Permit.negativeValueOf(Permit.isAnyRejected(Arrays.asList(childPermit, parentPermit)));
    }

    /**
     * Check if given type is allowed for permission holder to modify
     *
     * @param permissionHolder
     * @param type
     * @return
     */
    private static Permit checkTypeBasedPermission(final CmsPermissionHolder permissionHolder, final ContentType type) {
        return Permit.valueOf(permissionHolder.isContentTypeBasedPermissionEnabled(type.toString()));
    }
}
