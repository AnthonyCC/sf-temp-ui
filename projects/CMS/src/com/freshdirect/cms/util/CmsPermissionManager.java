package com.freshdirect.cms.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsPermissionI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.CmsRequestI.Source;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.UserI;
import com.freshdirect.cms.application.service.MaskContentService;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.fdstore.FDRootKey;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * This utility confirms a changeset if payload contains permitted nodes
 * 
 * @author segabor
 *
 */
public class CmsPermissionManager {

    private CmsPermissionManager() {
    }

    private final static Logger LOGGER = LoggerFactory.getInstance(CmsPermissionManager.class);

    /**
     * Result of permission check
     * 
     * @author segabor
     */
    public static enum Permit {
        ALLOW, REJECT;

        public static Permit valueOf(boolean flag) {
            return flag ? ALLOW : REJECT;
        }

        public static Permit negativeValueOf(boolean flag) {
            return flag ? REJECT : ALLOW;
        }

        public static boolean isAnyRejected(Collection<Permit> permits) {
            for (final Permit p : permits) {
                if (Permit.REJECT == p) {
                    return true;
                }
            }
            return false;
        }
    }

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
    public static Permit requestEditPermission(final ContentNodeI node, final CmsPermissionI permissionHolder, final ContentServiceI svc, final DraftContext draftContext) {
        Set<ContentNodeI> singleSet = new HashSet<ContentNodeI>(1);
        singleSet.add(node);

        Map<ContentKey, Permit> result = checkStoreMembershipPermission(singleSet, permissionHolder, svc, draftContext);

        return Permit.negativeValueOf(Permit.isAnyRejected(result.values()));
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
    public static Permit requestSaveChangesetPermission(final CmsRequestI request, final ContentServiceI svc) {
        // break down request into permissions and content nodes
        final CmsRequestI.Source origin = request.getSource();
        final DraftContext draftContext = request.getDraftContext();
        
        // Extract permission holder
        final UserI aUser = request.getUser();
        if (!(aUser instanceof CmsPermissionI)) {
            // user object most conform to permissions protocol
            LOGGER.error("[REJECT] User " + aUser.getName() + " is not conforming to permissions protocol");
            return Permit.REJECT;
        }
        final CmsPermissionI permissionHolder = (CmsPermissionI) aUser;

        if (Source.NODE_EDITOR == origin) {
            // list of changed nodes
            final Collection<ContentNodeI> changedNodes = request.getNodes();

            if (changedNodes.isEmpty()) {
                // no nodes to check ==> PASSED
                // Reason: type of changed node does not belong to store tree
                // no permission is required
                LOGGER.info("[ACCEPT] Empty changeset.");
                return Permit.ALLOW;
            }

            // collect keys
            Set<ContentKey> keys = new HashSet<ContentKey>(changedNodes.size());
            for (final ContentNodeI n : changedNodes) {
                keys.add(n.getKey());
            }

            // collect nodes with unchanged state
            Set<ContentNodeI> oldNodes = new HashSet<ContentNodeI>(keys.size());
            for (final ContentKey k : keys) {
                final ContentNodeI n = svc.getContentNode(k, draftContext);
                if (n != null)
                    oldNodes.add(n);
            }

            final boolean hasAnyTypeBasedPermission = permissionHolder.isAnyContentTypeBasedPermission();

            // handle prototype node, which is not part of CMSManager content service yet. 
            MaskContentService maskContentService = new MaskContentService(svc, new SimpleContentService(svc.getTypeService()));
            maskContentService.handle(request);

            // test permission on unchanged content objects first
            final Map<ContentKey, Permit> oldNodeStoreResult = checkStoreMembershipPermission(oldNodes, permissionHolder, maskContentService, draftContext);
            for (Map.Entry<ContentKey, Permit> entry : oldNodeStoreResult.entrySet()) {
                if (!Permit.ALLOW.equals(entry.getValue())) {
                    // TODO create and delete checks are missing
                    if (hasAnyTypeBasedPermission) {
                        entry.setValue(checkTypeBasedEditPermission(permissionHolder, entry.getKey().getType()));
                    } else {
                        entry.setValue(Permit.REJECT);
                    }
                }
            }
            if (Permit.isAnyRejected(oldNodeStoreResult.values())) {
                return Permit.REJECT;
            }

            // test permission on changed content objects
            final Map<ContentKey, Permit> newNodeStoreResult = checkStoreMembershipPermission(changedNodes, permissionHolder, maskContentService, draftContext);
            for (Map.Entry<ContentKey, Permit> entry : newNodeStoreResult.entrySet()) {
                if (!Permit.ALLOW.equals(entry.getValue())) {
                    // TODO create and delete checks are missing
                    if (hasAnyTypeBasedPermission) {
                        entry.setValue(checkTypeBasedEditPermission(permissionHolder, entry.getKey().getType()));
                    } else {
                        entry.setValue(Permit.REJECT);
                    }
                }
            }
            if (Permit.isAnyRejected(newNodeStoreResult.values())) {
                return Permit.REJECT;
            }

            return Permit.ALLOW;
        }

        return Permit.ALLOW;
    }

    private static final Set<ContentKey> OTHER_ROOT_KEYS = new HashSet<ContentKey>();
    static {
        OTHER_ROOT_KEYS.add(FDRootKey.SHARED_RESOURCES.getContentKey());
        OTHER_ROOT_KEYS.add(FDRootKey.MEDIA.getContentKey()); // not editable !!
        OTHER_ROOT_KEYS.add(FDRootKey.RECIPES.getContentKey()); // FD only
        OTHER_ROOT_KEYS.add(FDRootKey.STARTER_LISTS.getContentKey());
        OTHER_ROOT_KEYS.add(FDRootKey.DONATION_ORGS.getContentKey());
        OTHER_ROOT_KEYS.add(FDRootKey.YOUTUBE_MEDIA.getContentKey());
    }

    private static final Set<ContentType> OTHER_CONTENT_TYPES = new HashSet<ContentType>();
    static {
        // CMS Form
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsFolder") );
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsEditor") );
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsPage") );
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsSection") );
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsField") );
        
        // CMS Query
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsQueryFolder") );
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsQuery") );
        OTHER_CONTENT_TYPES.add( ContentType.get("CmsReport") );
    }

    private static final Collection<CustomPermissionRule> CUSTOM_RULES = Arrays.asList(CustomRuleValidator.FOODKICK_FEED_RULE, CustomRuleValidator.FOODKICK_PICKLIST_RULE,
            CustomRuleValidator.FOODKICK_SCHEDULE_RULE, CustomRuleValidator.FOODKICK_SECTION_RULE, CustomRuleValidator.FOODKICK_DARKSTORE_RULE);

    public static class StorePermissionInput {
        public final Collection<ContentKey> allowedStoreKeys;
        public final Permit permitForAnyStore;
        public final Permit permitForFD;
        public final Permit permitForOther;
        
        public StorePermissionInput( CmsPermissionI permissionHolder ) {
            this.allowedStoreKeys = Collections.unmodifiableCollection( CmsPermissionUtility.getAllowedStoreKeys(permissionHolder) );

            // Required for 'other' cases
            this.permitForAnyStore = Permit.valueOf(CmsPermissionUtility.isAllowedInAnyStoreContext(permissionHolder));
            this.permitForFD = Permit.valueOf( permissionHolder.isCanChangeFDStore() );
            this.permitForOther = Permit.valueOf( permissionHolder.isCanChangeOtherNodes() );
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
    protected static Map<ContentKey, Permit> checkStoreMembershipPermission(final Collection<ContentNodeI> nodes, final CmsPermissionI permissionHolder,
            final ContentServiceI svc, final DraftContext draftContext) {
        final Map<ContentKey, Permit> result = new HashMap<ContentKey, Permit>(nodes.size());

        final StorePermissionInput input = new StorePermissionInput( permissionHolder );
        
        for (final ContentNodeI n : nodes) {
            final Permit permit = checkStorePermission(n, input, svc, draftContext);
            if (permit != null) {
                result.put(n.getKey(), permit);
            }
        }

        if (result.size() != nodes.size()) {
            LOGGER.warn("Store Permission test was partial");
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
    public static Permit checkStorePermission(final ContentNodeI node, final StorePermissionInput input, final ContentServiceI svc, final DraftContext draftContext) {
        final ContentKey nodeKey = node.getKey();

        // collect root keys
        final Set<ContentKey> rootKeys = TopKeyCollectorUtility.collect(nodeKey, svc, draftContext);
 
        // <1> SINGLE-ROOT MATCH
        if (rootKeys.size() == 1 && OTHER_ROOT_KEYS.contains(rootKeys.iterator().next()) ) {
            final ContentKey theOnlyKey = rootKeys.iterator().next();

            if (FDRootKey.RECIPES.getContentKey().equals(theOnlyKey)) {
                // recipes are actually members of FD store
                return input.permitForFD;
            } else if (FDRootKey.SHARED_RESOURCES.getContentKey().equals(theOnlyKey)) {
                return input.permitForAnyStore;
            } else {
                return input.permitForOther;
            }
        }


        // <2> SELF-REFERENCE MATCH
        if (rootKeys.size() == 1 && nodeKey.equals(rootKeys.iterator().next()) ) {
            // CmsQueries and Cms Forms nodes fall into this case
            final ContentKey theOnlyKey = rootKeys.iterator().next();
            if (OTHER_CONTENT_TYPES.contains( theOnlyKey.getType() )) {
                // CMS Queries and Reports cannot be modified
                // they are always read-only

                /// LOGGER.debug("<2> " + nodeKey + " => " + result.get(nodeKey));
                return Permit.REJECT;
            }
        }


        // <3> ORPHAN TEST
        if (rootKeys.size() == 0) {
            /// LOGGER.debug("<3> " + nodeKey + " => SKIPPED");
            return null;
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
    public static Permit checkTypeBasedEditPermission(final CmsPermissionI permissionHolder, final ContentType contentType) {
        return checkTypeBasedPermission(permissionHolder, contentType);
    }

    public static Set<ContentType> getTypesDisallowedToCreate(final ContentNodeI node, final CmsPermissionI permissionHolder, final ContentServiceI contentService) {
        Set<ContentType> disallowedContentTypes = new HashSet<ContentType>();
        final ContentType nodeType = node.getKey().getType();
        for (ContentType relatedContentType : collectRelatedContentTypes(nodeType, contentService)) {
            if (Permit.REJECT == checkTypeBasedRelationshipPermission(permissionHolder, nodeType, relatedContentType)) {
                disallowedContentTypes.add(relatedContentType);
            }
        }
        return disallowedContentTypes;
    }

    public static Set<ContentType> getTypesDisallowedToDelete(final ContentNodeI node, final CmsPermissionI permissionHolder, final ContentServiceI contentService) {
        Set<ContentType> disallowedContentTypes = new HashSet<ContentType>();
        ContentType nodeType = node.getKey().getType();
        for (ContentType relatedContentType : collectRelatedContentTypes(nodeType, contentService)) {
            if (Permit.REJECT == applyCustomDeleteRules(node, relatedContentType)) {
                disallowedContentTypes.add(relatedContentType);
            }
        }
        return disallowedContentTypes;
    }

    
    /**
     * Override original permission by checking additional rules on the given node
     * 
     * @param typePermit
     * @param node
     * @param relatedType
     * @return
     */
    private static Permit applyCustomDeleteRules(ContentNodeI node, ContentType relatedType) {
        boolean isAnyRuleApplied = CustomRuleValidator.getInstance().validate(node, relatedType, CUSTOM_RULES);
        if (isAnyRuleApplied){
            return Permit.REJECT;
        } else {
            return Permit.ALLOW;
        }
    }

    
    /**
     * Collect all related content types of the given node.
     * 
     * @param type Source type
     * @param contentService Content Service
     * 
     * @return Type of children keys reachable via relationships of the given type
     */
    private static Set<ContentType> collectRelatedContentTypes(final ContentType type, final ContentServiceI contentService) {
        Set<ContentType> relatedContentTypes = new HashSet<ContentType>();
        final ContentTypeDefI contentTypeDefinition = contentService.getTypeService().getContentTypeDefinition(type);
        for (AttributeDefI attributeDef : contentTypeDefinition.getSelfAttributeDefs()) {
            if (attributeDef instanceof RelationshipDefI) {
                relatedContentTypes.addAll(((RelationshipDefI) attributeDef).getContentTypes());
            }
        }
        return relatedContentTypes;
    }

    private static Permit checkTypeBasedRelationshipPermission(CmsPermissionI permissionHolder, ContentType parentType, ContentType childType) {
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
    private static Permit checkTypeBasedPermission(final CmsPermissionI permissionHolder, final ContentType type) {
        return Permit.valueOf(permissionHolder.isContentTypeBasedPermissionEnabled(type));
    }
}
