package com.freshdirect.cms.ui.translator;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.widget.form.Time;
import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.changecontrol.ChangeDetail;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.changecontrol.ContentNodeChange;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.fdstore.ConfiguredProductValidator;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.ModifiableAttributeI;
import com.freshdirect.cms.ui.model.attributes.MultiEnumAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToManyAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToOneAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute.ProductConfigParams;
import com.freshdirect.cms.ui.model.attributes.SimpleAttribute;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;
import com.freshdirect.cms.ui.model.changeset.GwtChangeDetail;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtNodeChange;
import com.freshdirect.cms.ui.model.draft.GwtDraftChange;
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.freshdirect.cms.ui.service.ServerException;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Static class for converting server-side CMS data types to client-side serializable data types for Gwt.
 * 
 * @author treer
 */

public class TranslatorToGwt {

    private final static String SUFFIX_ATTR = "_ATTRIBUTE$";
    private final static String SUFFIX_KEY = "_KEY$";
    private final static String CLASS_COL = "CLASS$";
    private final static String GROUP_COL = "GROUP$";

    public static final DateFormat US_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.US);

    private static final Logger LOGGER = LoggerFactory.getInstance(TranslatorToGwt.class);

    // =========================== GwtNodeData ===========================

    /**
     * Creates a GwtNodeData object from a ContentNodeI. Fills in the node attributes, tab definitions, contexts (including inherited attributes) Read-only parameter sets a
     * read-only flag in the node.
     * 
     * @param node
     *            Content node to be transformed to lightweight client-side POJO
     * @param permission
     *            permission object shipped with node data
     * @param prototype
     *            This flag denotes a node is just created (not persisted yet) so only a subset of data will be provided By default, use <code>false</code> value.
     * @throws ServerException
     */
    public static GwtNodeData gwtNodeData(ContentNodeI node, GwtNodePermission permission, final boolean prototype, ContentServiceI contentService, DraftContext draftContext)
            throws ServerException {
        TabDefinition tabDef = TranslatorToGwt.gwtTabDefinition(node, contentService, draftContext);
        GwtContentNode gwtNode = TranslatorToGwt.getGwtNode(node, tabDef, contentService, draftContext);

        if (prototype) {
            // prototype nodes cannot have either contexts or preview links
            return new GwtNodeData(gwtNode, tabDef, permission);
        }

        GwtNodeContext ctx = TranslatorToGwt.gwtNodeContext(node, tabDef, contentService, draftContext);

        return new GwtNodeData(gwtNode, tabDef, permission, ctx, PreviewLinkProvider.getLink(node.getKey(), draftContext));
    }

    public static GwtContentNode getGwtNode(ContentNodeI node, TabDefinition tabDefs, ContentServiceI contentService, DraftContext draftContext) throws ServerException {
        ContentKey contentKey = node.getKey();
        GwtContentNode gwtNode = new GwtContentNode(contentKey.getType().getName(), contentKey.getId());
        gwtNode.setLabel(node.getLabel());

        final ContentTypeDefI definition = node.getDefinition();
        for (String key : definition.getAttributeNames()) {
            Object value = node.getAttributeValue(key);
            ModifiableAttributeI attr = translateAttribute(definition.getAttributeDef(key), value, tabDefs != null ? tabDefs.getCustomFieldDefinition(key) : null, node,
                    contentService, draftContext);
            gwtNode.setOriginalAttribute(key, attr);
        }

        return gwtNode;
    }

    // =========================== TAB DEFINITIONS ===========================

    public static TabDefinition gwtTabDefinition(ContentNodeI n, ContentServiceI contentService, DraftContext draftContext) throws ServerException {
        TabDefinition def = gwtTabDefinition(n.getDefinition(), contentService, draftContext);
        return def;
    }

    public static TabDefinition gwtTabDefinition(ContentTypeDefI typeDef, ContentServiceI contentService, DraftContext draftContext) throws ServerException {
        TabDefinition tabDef = new TabDefinition();

        ContentNodeI editor = findEditor(typeDef.getType(), contentService, draftContext);
        if (editor == null) {
            return tabDef;
        }

        Collection<ContentKey> tabs = (Collection<ContentKey>) editor.getAttributeValue("pages");
        for (ContentKey tabKey : tabs) {
            ContentNodeI tab = contentService.getContentNode(tabKey, draftContext);
            String tabId = tabKey.getId();
            tabDef.addTab(tabId, tab.getLabel());

            Collection<ContentKey> sections = (Collection<ContentKey>) tab.getAttributeValue("sections");
            for (ContentKey sectKey : sections) {
                ContentNodeI section = contentService.getContentNode(sectKey, draftContext);
                String sectionId = sectKey.getId();
                tabDef.addSection(tabId, sectionId, section.getLabel());

                Collection<ContentKey> attributes = (Collection<ContentKey>) section.getAttributeValue("fields");
                for (ContentKey attrKey : attributes) {
                    ContentNodeI attr = contentService.getContentNode(attrKey, draftContext);
                    String attributeKey = (String) attr.getAttributeValue("attribute");

                    tabDef.addAttributeKey(sectionId, attributeKey);

                    String attrType = attrKey.getType().getName();

                    if (!"CmsField".equals(attrType)) {

                        if ("CmsGridField".equals(attrType)) {
                            // we have to figure out, what type .. it must be a Relationship ...
                            RelationshipDefI attributeDef = (RelationshipDefI) typeDef.getAttributeDef(attributeKey);

                            if (attributeDef.getContentTypes().size() != 1) {
                                throw new ServerException(
                                        "Relation from " + typeDef.getName() + "::" + attributeKey + " contains more than 1 type: " + attributeDef.getContentTypes());
                            }
                            CustomFieldDefinition cfd = new CustomFieldDefinition(CustomFieldDefinition.Type.Grid);
                            List<ContentKey> values = (List<ContentKey>) attr.getAttributeValue("columns");
                            for (ContentKey k : values) {
                                cfd.addColumn((String) contentService.getContentNode(k, draftContext).getAttributeValue("attribute"));
                            }

                            tabDef.addCustomFieldDefinition(attributeKey, cfd);
                        }

                        if ("CmsMultiColumnField".equals(attrType)) {
                            // we have to figure out, what type .. it must be a Relationship ...
                            CustomFieldDefinition cfd = new CustomFieldDefinition(CustomFieldDefinition.Type.CmsMultiColumnField);
                            List<ContentKey> values = (List<ContentKey>) attr.getAttributeValue("columns");
                            int position = 0;
                            for (ContentKey k : values) {
                                cfd.addColumn((String) contentService.getContentNode(k, draftContext).getAttributeValue("attribute"));
                            }
                            tabDef.addCustomFieldDefinition(attributeKey, cfd);
                        }
                        if ("CmsCustomField".equals(attrType)) {
                            tabDef.addCustomFieldDefinition(attributeKey, new CustomFieldDefinition((String) attr.getAttributeValue("component")));
                        }
                    }
                }
            }
        }
        return tabDef;
    }

    // =========================== CONTEXTS ===========================

    /**
     * Creates a GwtNodeContext for a CMS content node.
     * 
     * @throws ServerException
     */
    public static GwtNodeContext gwtNodeContext(ContentNodeI node, TabDefinition tabs, ContentServiceI svc, DraftContext draftContext) throws ServerException {
        return gwtNodeContext(node.getKey(), tabs, svc, draftContext);
    }

    /**
     * Creates a GwtNodeContext for the supplied content key.
     * 
     * @throws ServerException
     */
    @SuppressWarnings("unchecked")
    public static GwtNodeContext gwtNodeContext(ContentKey key, TabDefinition tabs, ContentServiceI contentService, DraftContext draftContext) throws ServerException {

        GwtNodeContext nodeContext = new GwtNodeContext();

        final ContextService contextService = new ContextService(contentService);

        Collection<Context> contexts = contextService.getAllContextsOf(key, draftContext);

        for (Context cx : contexts) {
            ContextualContentNodeI cxNode = null;
            try {
                cxNode = contextService.getContextualizedContentNode(cx, draftContext);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Invalid context found, skipping.", e);
            }
            if (cxNode == null)
                break;
            Map<String, AttributeI> inheritedAttributes = cxNode.getParentInheritedAttributes();
            nodeContext.addContext(cx.getPath(TreeContentNodeModel.pathSeparator.charAt(0)), cx.getLabel(contentService, draftContext),
                    translateAttributeMap(inheritedAttributes, tabs, key, contentService, draftContext));
        }
        return nodeContext;
    }

    // =========================== ATTRIBUTES ===========================

    /**
     * Translates a map of CMS AttributeI-s to a map of Gwt ContentNodeAttributeI-s.
     * 
     * @param attributeMap
     * @return
     * @throws ServerException
     */
    public static Map<String, ContentNodeAttributeI> translateAttributeMap(Map<String, AttributeI> attributeMap, TabDefinition tabs, ContentKey contentKey,
            ContentServiceI contentService, DraftContext draftContext) throws ServerException {
        if (attributeMap == null)
            return null;

        Map<String, ContentNodeAttributeI> translatedAttributes = new HashMap<String, ContentNodeAttributeI>();

        ContentNodeI node = CmsManager.getInstance().getContentNode(contentKey, draftContext);
        for (String key : attributeMap.keySet()) {
            AttributeI attributeI = attributeMap.get(key);
            ModifiableAttributeI attribute = translateAttribute(attributeI.getDefinition(), attributeI.getValue(), tabs != null ? tabs.getCustomFieldDefinition(key) : null, node,
                    contentService, draftContext);
            translatedAttributes.put(key, attribute);
        }

        return translatedAttributes;
    }

    /**
     * Translates a single attribute from CMS AttributeI to Gwt ModifiableAttributeI
     * 
     * @param attribute
     * @param customFieldDefinition
     * @return
     * @throws ServerException
     */
    public static ModifiableAttributeI translateAttribute(AttributeDefI definition, Object value, CustomFieldDefinition customFieldDefinition, ContentNodeI node,
            ContentServiceI contentService, DraftContext draftContext) throws ServerException {
        ModifiableAttributeI attr = translateAttributeInternal(definition, customFieldDefinition, value, node, contentService, draftContext);
        if (attr == null) {
            throw new ServerException("Unknown attribute type " + definition + ", in node :" + node.getKey());
        }
        return attr;
    }

    @SuppressWarnings("unchecked")
    private static ModifiableAttributeI translateAttributeInternal(AttributeDefI definition, CustomFieldDefinition customFieldDefinition, Object value, ContentNodeI node,
            ContentServiceI contentService, DraftContext draftContext) {
        String name = definition.getLabel();
        EnumAttributeType type = definition.getAttributeType();
        ModifiableAttributeI attr = null;

        if (type == EnumAttributeType.STRING) {
            attr = new SimpleAttribute<String>("string", (String) value, name);
        } else if (type == EnumAttributeType.WYSIWYG) {
            attr = new SimpleAttribute<String>("WYSIWYG", (String) value, name);
        } else if (type == EnumAttributeType.LONG_TEXT) {
            attr = new SimpleAttribute<String>("text", (String) value, name);
        } else if (type == EnumAttributeType.DOUBLE) {
            attr = new SimpleAttribute<Double>("double", (Double) value, name);
        } else if (type == EnumAttributeType.INTEGER) {
            attr = new SimpleAttribute<Integer>("integer", (Integer) value, name);
        } else if (type == EnumAttributeType.DATE) {
            attr = new SimpleAttribute<Date>("date", (Date) value, name);
        } else if (type == EnumAttributeType.TIME) {
            Time time = new Time(0, 0);
            if (value != null) {
                time = new Time((Date) value);
            }
            attr = new SimpleAttribute<Time>("time", time, name);
        } else if (type == EnumAttributeType.BOOLEAN) {
            if (!definition.isInheritable() && value == null) {
                value = Boolean.FALSE;
            }
            attr = new SimpleAttribute<Boolean>("boolean", (Boolean) value, name);
        } else if (type == EnumAttributeType.ENUM) {
            if (value instanceof String && value != null && ((String) value).contains(",")) {
                attr = translateEnumDefToEnumMultiAttribute((EnumDef) definition, name, (Serializable) value);
            } else {
                attr = translateEnumDefToEnumAttribute((EnumDef) definition, name, (Serializable) value);
            }
        } else if (type == EnumAttributeType.RELATIONSHIP) {

            RelationshipDefI relD = (RelationshipDefI) definition;
            HashSet<String> cTypes = new HashSet<String>();

            for (Object k : relD.getContentTypes()) {
                ContentType ct = (ContentType) k;
                cTypes.add(ct.getName());
            }

            if (relD.isCardinalityOne()) {

                ContentKey valueKey = (ContentKey) value;

                if (customFieldDefinition != null && customFieldDefinition.getType() == CustomFieldDefinition.Type.ProductConfigEditor) {
                    ProductConfigAttribute pcAttr = new ProductConfigAttribute();
                    pcAttr.setLabel(name);
                    if (node != null && valueKey != null) {
                        pcAttr.setValue(toContentNodeModel(valueKey, contentService, draftContext));
                        pcAttr.setConfigParams(getProductConfigParams(valueKey, contentService, draftContext));
                        pcAttr.setQuantity((Double) node.getAttributeValue("QUANTITY"));
                        pcAttr.setSalesUnit((String) node.getAttributeValue("SALES_UNIT"));
                        pcAttr.setConfigOptions((String) node.getAttributeValue("OPTIONS"));
                    }
                    attr = pcAttr;
                } else {
                    OneToOneAttribute ooAttr = new OneToOneAttribute();
                    ooAttr.setLabel(name);
                    if (valueKey != null) {
                        ContentNodeModel model = toContentNodeModel(valueKey, contentService, draftContext);
                        ooAttr.setValue(model);
                    }
                    ooAttr.setAllowedTypes(cTypes);
                    attr = ooAttr;
                }

            } else {
                Collection<ContentKey> values = (Collection<ContentKey>) value;
                OneToManyAttribute ooAttr = new OneToManyAttribute(cTypes);
                ooAttr.setLabel(name);
                ooAttr.setNavigable(relD.isNavigable());
                if (values != null) {
                    int idx = 0;
                    for (ContentKey k : values) {
                        if (!ContentType.NULL_TYPE.equals(k.getType())) {
                            ContentNodeI contentNode = contentService.getContentNode(k, draftContext);
                            OneToManyModel model = toOneToManyModel(contentNode, idx);

                            if (customFieldDefinition != null) {
                                if (customFieldDefinition.getGridColumns() != null) {
                                    for (String col : customFieldDefinition.getGridColumns()) {
                                        Object attrValue = translateColumn(contentNode, col, contentService, draftContext);
                                        model.set(col, attrValue);
                                    }
                                }
                                // for variation matrix, we need the skus other
                                // properties too.
                                if (customFieldDefinition.getType() == CustomFieldDefinition.Type.VariationMatrix) {
                                    model.set("VARIATION_MATRIX", toClientValues(contentNode.getAttributeValue("VARIATION_MATRIX")));
                                    model.set("VARIATION_OPTIONS", toClientValues(contentNode.getAttributeValue("VARIATION_OPTIONS")));
                                }
                            }
                            ooAttr.addValue(model);
                        } else {
                            ooAttr.addValue(OneToManyModel.NULL_MODEL);
                        }
                        idx++;
                    }
                }
                attr = ooAttr;
            }

        } else if (type == EnumAttributeType.TABLE) {
            ITable table = (ITable) value;
            TableAttribute tableAttr = createTableAttribute(table, contentService, draftContext);
            attr = tableAttr;
        } else {
            return null;
        }
        attr.setInheritable(definition.isInheritable());
        attr.setReadonly(definition.isReadOnly());

        return attr;
    }

    private static Object translateColumn(ContentNodeI contentNode, String col, ContentServiceI contentService, DraftContext draftContext) {
        String childColumnName = null;
        if (col.contains("$")) {
            String[] columnName = col.split("\\$");
            col = columnName[0];
            childColumnName = columnName[1];
        }

        AttributeI attribute = contentNode.getAttribute(col);

        Object attrValue = contentNode.getAttributeValue(col);
        EnumAttributeType attributeType = attribute.getDefinition().getAttributeType();
        if (EnumAttributeType.DATE.equals(attributeType)) {
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                attrValue = format.format((Date) attrValue);
            } catch (Exception e) {
                // Do not throw exception if not transformed
            }
        } else if (EnumAttributeType.TIME.equals(attributeType)) {
            try {
                DateFormat format = new SimpleDateFormat("HH:mm");
                attrValue = format.format((Date) attrValue);
            } catch (Exception e) {
                // Do not throw exception if not transformed
            }
        } else if (attrValue instanceof ContentKey) {
            ContentKey key = (ContentKey) attrValue;
            ContentNodeI childNode = contentService.getContentNode(key, draftContext);
            if (childColumnName != null) {
                attrValue = childNode.getAttributeValue(childColumnName);
            } else {
                attrValue = null;
            }
        }
        return attrValue;
    }

    private static EnumAttribute translateEnumDefToEnumAttribute(EnumDef enumD, String name, Serializable currValue) {
        EnumAttribute enumA = new EnumAttribute();
        enumA.setLabel(name);
        for (Map.Entry<Object, String> e : enumD.getValues().entrySet()) {
            if (e.getKey() instanceof Serializable) {
                Serializable key = (Serializable) e.getKey();
                String value = e.getValue();
                enumA.addValue(key, value);
                if (key.equals(currValue)) {
                    enumA.setValue(currValue, value);
                }
            }
        }
        return enumA;
    }

    private static MultiEnumAttribute translateEnumDefToEnumMultiAttribute(EnumDef enumD, String name, Serializable currValue) {
        MultiEnumAttribute enumA = new MultiEnumAttribute();
        enumA.setLabel(name);
        for (Map.Entry<Object, String> e : enumD.getValues().entrySet()) {
            if (e.getKey() instanceof Serializable) {
                Serializable key = (Serializable) e.getKey();
                String value = e.getValue();
                enumA.addValue(key, value);

                List<Serializable> list = Arrays.asList(currValue);
                if (currValue instanceof String) {
                    list = Arrays.<Serializable> asList(((String) currValue).split(","));
                }

                if (list.contains(key)) {
                    enumA.addSelectedValue(key, value);
                }
            }
        }
        return enumA;
    }

    /**
     * Converts ITable to a TableAttribute which contains every information for the client to render the table properly.
     * 
     * @param table
     * @return
     */
    public static TableAttribute createTableAttribute(ITable table, ContentServiceI contentService, DraftContext draftContext) {
        AttributeDefI[] columnDefinitions = table.getColumnDefinitions();
        ContentNodeAttributeI[] columns = new ContentNodeAttributeI[columnDefinitions.length];
        TableAttribute.ColumnType[] columnTypes = new TableAttribute.ColumnType[columns.length];

        for (int i = 0; i < columnDefinitions.length; i++) {
            columns[i] = translateAttributeInternal(columnDefinitions[i], null, null, (ContentNodeI) null, contentService, draftContext);
            String name = columnDefinitions[i].getName();
            if (name.toUpperCase().endsWith(SUFFIX_ATTR)) {
                columnTypes[i] = TableAttribute.ColumnType.ATTRIB;
                ((ModifiableAttributeI) columns[i]).setLabel(name.substring(0, name.length() - SUFFIX_ATTR.length()));
            } else if (name.toUpperCase().endsWith(SUFFIX_KEY)) {
                columnTypes[i] = TableAttribute.ColumnType.KEY;
                ((ModifiableAttributeI) columns[i]).setLabel(name.substring(0, name.length() - SUFFIX_KEY.length()));
            } else if (CLASS_COL.equals(name.toUpperCase())) {
                columnTypes[i] = TableAttribute.ColumnType.CLASS;
            } else if (GROUP_COL.equals(name.toUpperCase())) {
                columnTypes[i] = TableAttribute.ColumnType.GROUPING;
            } else {
                columnTypes[i] = TableAttribute.ColumnType.NORMAL;
            }
        }

        TableAttribute tableAttr = new TableAttribute();
        tableAttr.setTypes(columnTypes);
        tableAttr.setColumns(columns);
        for (ITable.Row rw : table.getRows()) {
            Serializable[] rowValue = new Serializable[columnDefinitions.length];
            Object[] serverValue = rw.getValues();
            for (int i = 0; i < rowValue.length; i++) {
                rowValue[i] = toClientValues(serverValue[i]);
                switch (columnTypes[i]) {
                    case ATTRIB:
                        // handle columns which name is "_ATTRIBUTES$" and their value like 'Product:id:attribName'
                        if (rowValue[i] instanceof String) {
                            String[] tokens = ((String) rowValue[i]).split(":");
                            if (tokens.length == 3) {
                                ContentKey key = new ContentKey(ContentType.get(tokens[0]), tokens[1]);
                                ContentNodeI node = contentService.getContentNode(key, draftContext);
                                if (node != null) {
                                    Object attributeValue = node.getAttributeValue(tokens[2]);
                                    if (attributeValue != null) {
                                        rowValue[i] = String.valueOf(attributeValue);
                                    }
                                }
                            }
                        }
                        break;
                    case KEY:
                        if (rowValue[i] instanceof String) {
                            ContentKey ck = ContentKey.decode((String) rowValue[i]);
                            rowValue[i] = toContentNodeModel(ck, contentService, draftContext);
                        }
                        break;
                    case NORMAL:
                    default:
                }
            }
            tableAttr.addRow(rowValue);
        }
        return tableAttr;
    }

    @SuppressWarnings("unchecked")
    private static Serializable toClientValues(Object value) {
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
            return new ContentNodeModel(ck.getType().getName(), null, ck.getEncoded());
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return null;
    }

    // =========================== ContentNodeModel ===========================

    public static TreeContentNodeModel toTreeContentNodeModel(ContentNodeI node, ContentServiceI svc, DraftContext draftContext) {
        return toTreeContentNodeModel(node, null, svc, draftContext);
    }

    public static TreeContentNodeModel toTreeContentNodeModel(ContentNodeI node, TreeContentNodeModel parent, ContentServiceI svc, DraftContext draftContext) {
        if (node == null)
            return null;

        ContentKey key = node.getKey();
        TreeContentNodeModel result = new TreeContentNodeModel(key.getType().getName(), node.getLabel(), key.getEncoded(), parent);
        prepareModel(node, result);
        return result;
    }

    public static ContentNodeModel toContentNodeModel(ContentKey key, ContentServiceI contentService, DraftContext draftContext) {
        return toContentNodeModel(contentService.getContentNode(key, draftContext));
    }

    public static ContentNodeModel toContentNodeModel(ContentNodeI node) {
        if (node == null)
            return null;

        ContentKey key = node.getKey();
        ContentNodeModel result = new ContentNodeModel(key.getType().getName(), node.getLabel(), key.getEncoded());
        prepareModel(node, result);
        return result;
    }

    private static OneToManyModel toOneToManyModel(ContentNodeI node, int idx) {
        ContentKey key = node.getKey();
        OneToManyModel result = new OneToManyModel(key.getType().getName(), key.getEncoded(), node != null ? node.getLabel() : key.getId(), idx);
        prepareModel(node, result);
        return result;
    }

    private static void prepareModel(ContentNodeI node, ContentNodeModel result) {
        if (node != null) {
            if (result instanceof TreeContentNodeModel) {
                ((TreeContentNodeModel) result).setHasChildren(node.getChildKeys().size() > 0);
            }
            if (result.isHtmlType()) {
                Object path = node.getAttributeValue("path");
                result.setPreviewUrl(FDStoreProperties.getCmsMediaBaseURL() + path);
            }
            if (result.isImageType()) {
                Object path = node.getAttributeValue("path");
                result.setPreviewUrl(FDStoreProperties.getCmsMediaBaseURL() + path);
                Object width = node.getAttributeValue("width");
                result.setWidth((Integer) width);
                Object height = node.getAttributeValue("height");
                result.setHeight((Integer) height);
            }
        }
    }

    // =========================== PUBLISH ===========================

    public static GwtPublishData getPublishData(Publish p) {
        GwtPublishData d = new GwtPublishData();
        d.setId(p.getId());
        d.setComment(p.getDescription());
        d.setPublisher(p.getUserId());
        d.setCreated(p.getTimestamp() != null ? US_DATE_FORMAT.format(p.getTimestamp()) : "");
        d.setLastModified(p.getLastModified() != null ? US_DATE_FORMAT.format(p.getLastModified()) : "");
        d.setStatus(p.getStatus().getName());
        return d;
    }

    public static GwtPublishMessage getPublishMessage(PublishMessage pm) {

        String contentKey = pm.getContentKey() == null ? null : pm.getContentKey().getEncoded();
        GwtPublishMessage g = new GwtPublishMessage(pm.getContentType(), contentKey);
        g.setMessage(pm.getMessage());
        g.setTimestamp(pm.getTimestamp());
        g.setSeverity(pm.getSeverity());
        return g;
    }

    public static List<GwtPublishMessage> getPublishMessages(List<PublishMessage> list, int start, int end) {
        start = Math.max(start, 0);
        end = Math.min(end, list.size());
        List<GwtPublishMessage> result = new ArrayList<GwtPublishMessage>(end - start);
        for (int i = start; i < end; i++) {
            result.add(getPublishMessage(list.get(i)));
        }
        return result;
    }

    public static List<GwtPublishMessage> getPublishMessages(Publish publish, ChangeSetQuery query) {
        if (query.getMessageSeverity() == -1) {
            return getPublishMessages(publish.getMessages(), query.getPublishMessageStart(), query.getPublishMessageEnd());
        }
        List<PublishMessage> filteredList = new ArrayList<PublishMessage>();
        for (PublishMessage message : publish.getMessages()) {
            if (message.getSeverity() == query.getMessageSeverity()) {
                filteredList.add(message);
            }
        }
        return getPublishMessages(filteredList, query.getPublishMessageStart(), query.getPublishMessageEnd());
    }

    // =========================== CHANGE SETS ===========================

    public static List<GwtChangeSet> getGwtChangeSets(List<ChangeSet> changeSet, ChangeSetQuery query, ContentServiceI contentService, DraftContext draftContext) {
        List<GwtChangeSet> result = new ArrayList<GwtChangeSet>(changeSet.size());
        for (ChangeSet s : changeSet) {
            /*
             * Contributor filter
             */
            if (query.getContributor() == null || s.getUserId().equals(query.getContributor())) {
                result.add(getGwtChangeSet(s, query, contentService, draftContext));
            }
        }
        return result;
    }

    public static List<GwtChangeSet> getGwtChangeSets(ChangeSet changeSet, ContentServiceI contentService, DraftContext draftContext) {
        List<GwtChangeSet> result = new ArrayList<GwtChangeSet>(1);
        result.add(getGwtChangeSet(changeSet, new ChangeSetQuery(), contentService, draftContext));
        return result;
    }

    public static GwtChangeSet getGwtChangeSet(ChangeSet changeSet, ChangeSetQuery query, ContentServiceI contentService, DraftContext draftContext) {
        GwtChangeSet gwtChangeSet = new GwtChangeSet(changeSet.getId(), changeSet.getUserId(), changeSet.getModifiedDate(), changeSet.getNote());

        for (ContentNodeChange nodeChange : changeSet.getNodeChanges()) {
            /*
             * Content type filter
             */
            if (query.getContentType() == null || nodeChange.getContentKey().getType().getName().equals(query.getContentType())) {
                gwtChangeSet.addChange(getGwtContentNodeChange(nodeChange, contentService, draftContext));
            }
        }
        return gwtChangeSet;
    }

    private static GwtNodeChange getGwtContentNodeChange(ContentNodeChange cnc, ContentServiceI contentService, DraftContext draftContext) {
        ContentKey key = cnc.getContentKey();
        ContentNodeI node = contentService.getContentNode(key, draftContext);

        GwtNodeChange gcnc = new GwtNodeChange(key.getType().getName(), node == null ? "" : node.getLabel() == null ? "" : node.getLabel(), key.getEncoded(),
                cnc.getChangeType().getName(), PreviewLinkProvider.getLink(key, draftContext));

        for (ChangeDetail cd : cnc.getChangeDetails()) {
            String oldValue = ensureAttributeValue(node, cd.getAttributeName(), cd.getOldValue());
            String newValue = ensureAttributeValue(node, cd.getAttributeName(), cd.getNewValue());
            gcnc.addDetail(new GwtChangeDetail(cd.getAttributeName(), oldValue, newValue));
        }
        return gcnc;
    }

    // =========================== ... ===========================

    private static ContentNodeI findEditor(ContentType type, ContentServiceI contentService, DraftContext draftContext) {
        String typeName = type.getName();
        Set<ContentKey> editorKeys = contentService.getContentKeysByType(ContentType.get("CmsEditor"), draftContext);
        Map<ContentKey, ContentNodeI> nodes = contentService.getContentNodes(editorKeys, draftContext);
        for (ContentNodeI node : nodes.values()) {
            if (typeName.equals(node.getAttributeValue("contentType").toString())) {
                return node;
            }
        }
        return null;
    }

    /**
     * Extra infos for ProductConfigEditor component Sales units and config parameter enums.
     * 
     * @param skuKey
     * @return
     */
    public static ProductConfigParams getProductConfigParams(ContentKey skuKey, ContentServiceI contentService, DraftContext draftContext) {
        ContentNodeI sku = contentService.getContentNode(skuKey, draftContext);
        Map<String, String> salesUnitsMap = new TreeMap<String, String>();
        List<EnumModel> salesUnits = new ArrayList<EnumModel>();
        for (ContentNodeI su : ConfiguredProductValidator.getSalesUnits(sku, contentService, draftContext).values()) {
            String id = su.getKey().getId();
            String label = ContentNodeUtil.getLabel(su, draftContext);
            salesUnitsMap.put(id, label);

            EnumModel em = new EnumModel(id, label);
            salesUnits.add(em);
        }
        Collections.sort(salesUnits);

        Map<String, EnumDef> enumDefMap = ConfiguredProductValidator.getDefinitionMap(sku, contentService, draftContext);
        List<EnumAttribute> enumAttrs = new ArrayList<EnumAttribute>(enumDefMap.size());

        for (Map.Entry<String, EnumDef> def : enumDefMap.entrySet()) {
            enumAttrs.add(translateEnumDefToEnumAttribute(def.getValue(), def.getKey(), null));
        }

        return new ProductConfigParams(salesUnits, enumAttrs);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, List<ContentNodeModel>> getDomainValues(List<ContentNodeModel> domains, ContentServiceI contentService, DraftContext draftContext) {
        Map<String, List<ContentNodeModel>> result = new HashMap<String, List<ContentNodeModel>>();

        for (ContentNodeModel domain : domains) {
            ContentKey key = (ContentKey) TranslatorFromGwt.getServerValue(domain);
            ContentNodeI node = contentService.getContentNode(key, draftContext);
            List<ContentKey> domainValues = (List<ContentKey>) node.getAttributeValue("domainValues");

            List<ContentNodeModel> tempList = new ArrayList<ContentNodeModel>();
            for (ContentKey domainValueKey : domainValues) {
                tempList.add(TranslatorToGwt.toContentNodeModel(contentService.getContentNode(domainValueKey, draftContext)));
            }

            result.put(domain.getKey(), tempList);
        }
        return result;

    }

    /**
     * This method encapsulates the stack trace into the message of the server
     * 
     * @param ex
     * @throws ServerException
     */
    public static ServerException wrap(Throwable ex) {
        StringBuilder sw = new StringBuilder();
        sw.append(ex.getClass().toString()).append(" : ").append(ex.getMessage()).append('\n');
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace != null) {
            boolean prevSkipped = false;
            for (int i = 0; i < stackTrace.length; i++) {
                String cname = stackTrace[i].getClassName();
                if (cname.startsWith("$") || cname.startsWith("com.google.gwt") || cname.startsWith("javax.servlet.") || cname.startsWith("sun.reflect.")
                        || cname.startsWith("java.lang.") || cname.startsWith("weblogic.") || cname.startsWith("org.mortbay.")) {
                    if (!prevSkipped) {
                        sw.append("...\n");
                        prevSkipped = true;
                    }
                } else {
                    String line = stackTrace[i].toString();
                    sw.append(line).append('\n');
                    prevSkipped = false;
                }
            }
        }
        return new ServerException(sw.toString(), ex);
    }

    public static Set<String> mapCollectionToString(Set<?> types) {
        final Set<String> result = new HashSet<String>(types.size());
        for (Object t : types) {
            if (t instanceof ContentKey) {
                result.add(((ContentKey) t).getEncoded());
            } else {
                result.add(t.toString());
            }
        }
        return result;
    }

    public static GwtUser getGwtUser(CmsUser cmsUser) {
        final GwtUser gwtUser = new GwtUser(cmsUser.getName());
        gwtUser.setPersonaName(cmsUser.getPersonaName());
        gwtUser.setHasAccessToAdminTab(cmsUser.isHasAccessToAdminTab());
        gwtUser.setHasAccessToBulkLoaderTab(cmsUser.isHasAccessToBulkLoaderTab());
        gwtUser.setHasAccessToChangesTab(cmsUser.isHasAccessToChangesTab());
        gwtUser.setHasAccessToFeedPublishTab(cmsUser.isHasAccessToFeedPublishTab());
        gwtUser.setHasAccessToPublishTab(cmsUser.isHasAccessToPublishTab());
        gwtUser.setHasAccessToDraftBranches(cmsUser.isHasAccessToDraftBranches());
        gwtUser.setCanChangeFDStore(cmsUser.isCanChangeFDStore());
        gwtUser.setCanChangeFDXStore(cmsUser.isCanChangeFDXStore());
        gwtUser.setCanChangeOtherNodes(cmsUser.isCanChangeOtherNodes());
        gwtUser.setLoginErrorMessage(cmsUser.getLoginErrorMessage());
        final DraftContext draftContext = cmsUser.getDraftContext();
        gwtUser.setDraftActive(!DraftContext.MAIN.equals(draftContext));
        gwtUser.setDraftName(draftContext.getDraftName());
        if (cmsUser.isHasAccessToPermissionEditorApp()) {
            gwtUser.setCmsAdminURL(FDStoreProperties.getCMSAdminUiURL());
        }
        return gwtUser;
    }

    public static GwtChangeSet getGwtChangeSet(String id, Collection<DraftChange> draftChanges, ContentServiceI contentService, DraftContext draftContext) {
        String username = null;
        long modifiedDate = 0l;

        List<GwtNodeChange> nodeChanges = new ArrayList<GwtNodeChange>();
        for (final DraftChange draftChange : draftChanges) {
            username = draftChange.getUserName();
            modifiedDate = draftChange.getCreatedAt();
            nodeChanges.add(getGwtContentNodeChange(draftChange, contentService, draftContext));
        }

        GwtChangeSet gwtChangeSet = new GwtChangeSet(id, username, new Date(modifiedDate), null);
        gwtChangeSet.addChanges(nodeChanges);
        return gwtChangeSet;
    }

    private static GwtNodeChange getGwtContentNodeChange(DraftChange draftChange, ContentServiceI contentService, DraftContext draftContext) {
        ContentKey key = ContentKey.decode(draftChange.getContentKey());
        ContentNodeI node = contentService.getContentNode(key, draftContext);

        GwtNodeChange gwtNodeChange = new GwtNodeChange(key.getType().getName(), node == null ? "" : node.getLabel() == null ? "" : node.getLabel(), key.getEncoded(), null, null);

        String newValue = ensureAttributeValue(node, draftChange.getAttributeName(), draftChange.getValue());
        gwtNodeChange.addDetail(new GwtChangeDetail(draftChange.getAttributeName(), null, newValue));

        return gwtNodeChange;
    }

    private static String ensureAttributeValue(ContentNodeI node, String attributeName, String attributeValue) {
        if (node != null) {
            AttributeDefI attrDef = node.getAttribute(attributeName).getDefinition();
            // try to get the human-readable string representation of the enum attribute value if available
            // complicated tricks to do it in a fail-safe way
            if (attrDef instanceof EnumDefI) {
                EnumDefI enumD = (EnumDefI) attrDef;
                Map<Object, String> valMap = enumD.getValues();

                String newS = valMap.get(attributeValue);

                if (newS != null) {
                    attributeValue = newS;
                } else {
                    String oldI = null;
                    String newI = null;
                    try {
                        newI = attributeValue == null ? null : (String) valMap.get(new Integer(attributeValue));
                    } catch (NumberFormatException ex) {
                    }

                    if (oldI != null || newI != null) {
                        attributeValue = newI;
                    }
                }
            }
        }
        return attributeValue;
    }

    public static List<GwtDraftChange> convertDraftChangesToGwtDraftChanges(Collection<DraftChange> collection) {
        final List<GwtDraftChange> draftChanges = new ArrayList<GwtDraftChange>();
        if (collection != null && !collection.isEmpty()) {
            for (final DraftChange source : collection) {
                draftChanges.add(convertDraftChangeToGwtDraftChange(source));
            }
        }
        return draftChanges;
    }

    public static GwtDraftChange convertDraftChangeToGwtDraftChange(DraftChange draftChange) {
        GwtDraftChange target = new GwtDraftChange();

        target.setDraftId(draftChange.getDraft().getId());
        target.setUserName(draftChange.getUserName());
        target.setCreatedAt(new Date(draftChange.getCreatedAt()));
        target.setContentKey(draftChange.getContentKey());
        target.setAttributeName(draftChange.getAttributeName());
        target.setChangedValue(draftChange.getValue());

        return target;
    }
}
