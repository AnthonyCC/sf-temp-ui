package com.freshdirect.cms.ui.translator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.changecontrol.ChangeDetail;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.changecontrol.ContentNodeChange;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.fdstore.PreviewLinkProvider;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.meta.RelationshipDef;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.BulkEditModel;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtPublishData;
import com.freshdirect.cms.ui.model.GwtPublishMessage;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.ModifiableAttributeI;
import com.freshdirect.cms.ui.model.attributes.OneToManyAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToOneAttribute;
import com.freshdirect.cms.ui.model.attributes.SimpleAttribute;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.model.changeset.GwtChangeDetail;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtContentNodeChange;
import com.freshdirect.fdstore.FDStoreProperties;

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
    
    
	public static ContentNodeModel getContentNodeModel( ContentNodeI node ) {
		return toContentNodeModel( node.getKey() );
//		return new ContentNodeModel( 
//				node.getDefinition().getType().getName(), 
//				node.getLabel(), 
//				node.getKey().getEncoded(), 
//				node.getChildKeys().size() > 0 
//		);
	}

	public static BulkEditModel getBulkModel( ContentNodeI node ) {
		BulkEditModel model = new BulkEditModel( node.getDefinition().getType().getName(), node.getLabel(), node.getKey().getEncoded() );
		if ( node.getAttribute( "FULL_NAME" ) != null ) {
			if ( node.getAttribute( "FULL_NAME" ).getValue() != null ) {
				model.setFullName( String.valueOf( node.getAttribute( "FULL_NAME" ).getValue() ) );
			}
		}
		if ( node.getAttribute( "GLANCE_NAME" ) != null ) {
			if ( node.getAttribute( "GLANCE_NAME" ).getValue() != null ) {
				model.setGlanceName( String.valueOf( node.getAttribute( "GLANCE_NAME" ).getValue() ) );
			}
		}
		if ( node.getAttribute( "NAV_NAME" ) != null ) {
			if ( node.getAttribute( "NAV_NAME" ).getValue() != null ) {
				model.setNavName( String.valueOf( node.getAttribute( "NAV_NAME" ).getValue() ) );
			}
		}
		return model;
	}

	public static GwtContentNode getGwtNode( ContentNodeI node, TabDefinition tabDefs ) {
		GwtContentNode gwtNode = new GwtContentNode( node.getKey().getType().getName(), node.getKey().getId() );
		gwtNode.setLabel( node.getLabel() );
		
		for ( String key : (Set<String>)node.getAttributes().keySet() ) {
			AttributeI attribute = node.getAttribute( key );
			ModifiableAttributeI attr = translateAttribute(attribute, tabDefs != null ? tabDefs.getCustomFieldDefinition(key) : null);
			gwtNode.setOriginalAttribute( key, attr );
		}

		return gwtNode;
	}
	
	
	
	// =========================== GwtNodeData ===========================

	/**
	 * 	Creates a GwtNodeData object from a ContentNodeI. 
	 * 	Fills in the node attributes, tab definitions, contexts (including inherited attributes)
	 * 	Read-only parameter sets a read-only flag in the node.
	 */
	public static GwtNodeData gwtNodeData( ContentNodeI node, boolean readOnly ) {
		TabDefinition tabDef = TranslatorToGwt.gwtTabDefinition( node );
		GwtContentNode gwtNode = TranslatorToGwt.getGwtNode( node, tabDef );
		GwtNodeContext ctx = TranslatorToGwt.gwtNodeContext( node, tabDef );
	    
	    return new GwtNodeData( gwtNode, tabDef, ctx, readOnly, PreviewLinkProvider.getLink( node.getKey() ) );
	}

	/**
	 *	Creates a new GwtNodeData object, with the supplied type and id.
	 *	Fills in tab definitions, but no context data. 	
	 */
	public static GwtNodeData gwtNodeDataSkeleton ( String type, String id ) {
		ContentNodeI node;
		try {
			node = CmsManager.getInstance().createPrototypeContentNode(ContentKey.create(ContentType.get(type), id));
	    } catch (InvalidContentKeyException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Invalid content key : " + e.getMessage());
	    }

	    TabDefinition tabDef = TranslatorToGwt.gwtTabDefinition(node);
	    GwtContentNode gwtNode = TranslatorToGwt.getGwtNode(node, tabDef);
	    
	    return new GwtNodeData( gwtNode, tabDef, false );
	}
	
	// =========================== TAB DEFINITIONS ===========================
	
	public static TabDefinition gwtTabDefinition( ContentNodeI n ) {
		TabDefinition def = gwtTabDefinition( n.getDefinition() );
		return def;
	}

	@SuppressWarnings( "unchecked" )
	public static TabDefinition gwtTabDefinition( ContentTypeDefI typeDef ) {
		
		TabDefinition tabDef = new TabDefinition();
		
		ContentNodeI editor = findEditor( typeDef.getType() );
		if ( editor == null ) {
			return tabDef;
		}

		Collection<ContentKey> tabs = (Collection<ContentKey>)editor.getAttribute( "pages" ).getValue();
		for ( ContentKey tabKey : tabs ) {
			ContentNodeI tab = tabKey.getContentNode();			
			String tabId = tabKey.getId();			
			tabDef.addTab( tabId, tab.getLabel() );

			Collection<ContentKey> sections = (Collection<ContentKey>)tab.getAttribute( "sections" ).getValue();
			for ( ContentKey sectKey : sections ) {
				ContentNodeI section = sectKey.getContentNode();
				String sectionId = sectKey.getId();
				tabDef.addSection( tabId, sectionId, section.getLabel() );
				
				Collection<ContentKey> attributes = (Collection<ContentKey>)section.getAttribute( "fields" ).getValue();
				for ( ContentKey attrKey : attributes ) {
					ContentNodeI attr = attrKey.getContentNode();
					String attributeKey = (String)attr.getAttribute( "attribute" ).getValue();					
					
					tabDef.addAttributeKey( sectionId, attributeKey );
					
					String attrType = attrKey.getType().getName();
					if ( !"CmsField".equals( attrType ) ) {
						// we have to figure out, what type .. it must be a Relationship ...
						RelationshipDefI attributeDef = (RelationshipDefI)typeDef.getAttributeDef( attributeKey );

						if ( "CmsGridField".equals( attrType ) ) {
							if ( attributeDef.getContentTypes().size() != 1 ) {
								throw new RuntimeException( "Relation from " + typeDef.getName() + "::" + attributeKey
										+ " contains more than 1 type: " + attributeDef.getContentTypes() );
							}
							CustomFieldDefinition cfd = new CustomFieldDefinition( CustomFieldDefinition.Type.Grid );

							List<ContentKey> values = (List<ContentKey>)attr.getAttribute( "columns" ).getValue();
							for ( ContentKey k : values ) {
								cfd.addColumn( (String)k.getContentNode().getAttribute( "attribute" ).getValue() );
							}

							tabDef.addCustomFieldDefinition( attributeKey, cfd );
						}
						if ( "CmsCustomField".equals( attrType ) ) {
							tabDef.addCustomFieldDefinition( attributeKey, new CustomFieldDefinition( (String)attr.getAttribute( "component" ).getValue() ) );
						}
					}
				}
			}
		}
		return tabDef;
	}
	
	
	// =========================== CONTEXTS ===========================  
	
	/**
	 * 	Creates a GwtNodeContext for a CMS content node.
	 */
	public static GwtNodeContext gwtNodeContext( ContentNodeI node, TabDefinition tabs) {
		return gwtNodeContext(node.getKey(), tabs);
	}
	
	/**
	 *	Creates a GwtNodeContext for the supplied content key. 
	 */
	@SuppressWarnings("unchecked")
	public static GwtNodeContext gwtNodeContext( ContentKey key, TabDefinition tabs ) {
		
		GwtNodeContext nodeContext = new GwtNodeContext();
		
		Collection<Context> contexts = (Collection<Context>)ContextService.getInstance().getAllContextsOf( key );
		
		for ( Context cx : contexts ) {
			ContextualContentNodeI cxNode = ContextService.getInstance().getContextualizedContentNode( cx );
			if ( cxNode == null )
				break;
			Map<String,AttributeI> inheritedAttributes = (Map<String,AttributeI>)cxNode.getInheritedAttributes();			
			nodeContext.addContext( cx.getPath(), cx.getLabel(), translateAttributeMap( inheritedAttributes, tabs ) );
		}		
		return nodeContext;
	}

	
	
	// =========================== ATTRIBUTES ===========================  
	
	/**
	 * 	Translates a map of CMS AttributeI-s to a map of Gwt ContentNodeAttributeI-s.
	 * 
	 * @param attributeMap
	 * @return
	 */
	public static Map<String, ContentNodeAttributeI> translateAttributeMap( Map<String, AttributeI> attributeMap, TabDefinition tabs ) {
		if ( attributeMap == null )
			return null;
		
		Map<String, ContentNodeAttributeI> translatedAttributes = new HashMap<String, ContentNodeAttributeI>();

		for ( String key : attributeMap.keySet() ) {
		    ModifiableAttributeI attribute = translateAttribute( attributeMap.get( key ), tabs != null ? tabs.getCustomFieldDefinition( key ) : null );
                    translatedAttributes.put( key, attribute );
		}

		return translatedAttributes;
	}
	
	/**
	 *	Translates a single attribute from CMS AttributeI to Gwt ModifiableAttributeI
	 * 
	 * @param attribute
	 * @param customFieldDefinition 
	 * @return
	 */
    public static ModifiableAttributeI translateAttribute(AttributeI attribute, CustomFieldDefinition customFieldDefinition) {
        AttributeDefI definition = attribute.getDefinition();
        Object value = attribute.getValue();
        ModifiableAttributeI attr = translateAttribute(definition, customFieldDefinition, value);
        if (attr == null) {
            throw new RuntimeException("Unknown attribute type "+ attribute.getDefinition() + ", in node :"+attribute.getContentNode().getKey());
        }
        return attr;
    }

    @SuppressWarnings( "unchecked" )
	private static ModifiableAttributeI translateAttribute(AttributeDefI definition, CustomFieldDefinition customFieldDefinition, Object value) {
        String name = definition.getLabel();
        EnumAttributeType type = definition.getAttributeType();
        ModifiableAttributeI attr = null;

        if (type == EnumAttributeType.STRING) {
            attr = new SimpleAttribute<String>("string", (String) value, name);
        } else if (type == EnumAttributeType.DOUBLE) {
            attr = new SimpleAttribute<Double>("double", (Double) value, name);
        } else if (type == EnumAttributeType.INTEGER) {
            attr = new SimpleAttribute<Integer>("integer", (Integer) value, name);
        } else if (type == EnumAttributeType.DATE) {
            attr = new SimpleAttribute<Date>("date", (Date) value, name);
        } else if (type == EnumAttributeType.BOOLEAN) {
            attr = new SimpleAttribute<Boolean>("boolean", (Boolean) value, name);
        } else if (type == EnumAttributeType.ENUM) {
            EnumDef enumD = (EnumDef) definition;
            EnumAttribute enumA = new EnumAttribute();
            enumA.setLabel(name);
            Serializable currValue = (Serializable) value;

            for (Map.Entry<Serializable, String> e : ((Map<Serializable, String>) enumD.getValues()).entrySet()) {
                enumA.addValue(e.getKey(), e.getValue());
                if (e.getKey().equals(currValue)) {
                    enumA.setValue(e.getKey(), e.getValue());
                }
            }

            attr = enumA;
        } else if (type == EnumAttributeType.RELATIONSHIP) {
            RelationshipDef relD = (RelationshipDef) definition;
            HashSet<String> cTypes = new HashSet<String>();

            for (Object k : relD.getContentTypes()) {
                ContentType ct = (ContentType) k;
                cTypes.add(ct.getName());
            }

            if (relD.isCardinalityOne()) {
                ContentKey v = (ContentKey) value;
                OneToOneAttribute ooAttr = new OneToOneAttribute();
                ooAttr.setLabel(name);
                if (v != null) {
                    ContentNodeModel model = toContentNodeModel( v );
                    ooAttr.setValue(model);
                }
                ooAttr.setAllowedTypes(cTypes);
                attr = ooAttr;
            } else {
                Collection<ContentKey> values = (Collection<ContentKey>) value;
                OneToManyAttribute ooAttr = new OneToManyAttribute(cTypes);
                ooAttr.setLabel(name);
                ooAttr.setNavigable(relD.isNavigable());
                if (values != null) {
                    int idx = 0;
                    for (ContentKey k : values) {
                        ContentNodeI contentNode = k.getContentNode();
                        OneToManyModel model = toOneToManyModel( k, idx );
                        
                        if (customFieldDefinition != null) {
                            if (customFieldDefinition.getGridColumns() != null) {
                                for (String col : customFieldDefinition.getGridColumns()) {
                                    Object attrValue = contentNode.getAttribute(col).getValue();
                                    model.set(col, attrValue);
                                }
                            }
                            // for variation matrix, we need the skus other
                            // properties too.
                            if (customFieldDefinition.getType() == CustomFieldDefinition.Type.VariationMatrix) {
                                model.set("VARIATION_MATRIX", toClientValues(contentNode.getAttribute("VARIATION_MATRIX").getValue()));
                                model.set("VARIATION_OPTIONS", toClientValues(contentNode.getAttribute("VARIATION_OPTIONS").getValue()));
                            }
                        }

                        ooAttr.addValue(model);
                        idx++;
                    }
                }
                attr = ooAttr;
            }
            
        } else if (type == EnumAttributeType.TABLE) {
            ITable table = (ITable) value;
            TableAttribute tableAttr = createTableAttribute(table);
            attr = tableAttr;
        } else {
            return null;
        }
        attr.setInheritable(definition.isInheritable());
        attr.setReadonly(definition.isReadOnly());

        return attr;
    }

    /**
     * Converts ITable to a TableAttribute which contains every information for the client to render the table properly.
     * 
     * @param table
     * @return
     */
    private static TableAttribute createTableAttribute(ITable table) {
        AttributeDefI[] columnDefinitions = table.getColumnDefinitions();
        ContentNodeAttributeI[] columns = new ContentNodeAttributeI[columnDefinitions.length];
        TableAttribute.ColumnType[] columnTypes = new TableAttribute.ColumnType[columns.length];
        
        for (int i=0;i<columnDefinitions.length;i++) {
            columns[i] = translateAttribute(columnDefinitions[i], null, null);
            String name = columnDefinitions[i].getName();
            if (name.toUpperCase().endsWith(SUFFIX_ATTR)) {
                columnTypes[i] = TableAttribute.ColumnType.ATTRIB;
                ((ModifiableAttributeI)columns[i]).setLabel(name.substring(0, name.length() - SUFFIX_ATTR.length()));
            } else if (name.toUpperCase().endsWith(SUFFIX_KEY)) {
                columnTypes[i] = TableAttribute.ColumnType.KEY;
                ((ModifiableAttributeI)columns[i]).setLabel(name.substring(0, name.length() - SUFFIX_KEY.length()));
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
                    case ATTRIB :
                        // handle columns which name is "_ATTRIBUTES$" and their value like 'Product:id:attribName'
                        if (rowValue[i] instanceof String) {
                            String[] tokens = ((String) rowValue[i]).split(":");
                            if (tokens.length == 3) {
                                ContentKey key = new ContentKey(ContentType.get(tokens[0]), tokens[1]);
                                ContentNodeI node = key.getContentNode();
                                if (node != null) {
                                    AttributeI attribute = node.getAttribute(tokens[2]);
                                    if (attribute != null && attribute.getValue() != null) {
                                        rowValue[i] = String.valueOf(String.valueOf(attribute.getValue()));
                                    }
                                }
                            }
                        }
                        break;
                    case KEY : 
                        if (rowValue[i] instanceof String) {
                            ContentKey ck = ContentKey.decode((String) rowValue[i]);
                            rowValue[i] = toContentNodeModel(ck);
                        }
                        break;
                    case NORMAL :
                    default :
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
    
    private static ContentNodeModel toContentNodeModel( ContentKey key ) {
        ContentNodeI node = key.getContentNode();
        ContentNodeModel result = new ContentNodeModel (
        		key.getType().getName(), 
        		node != null ? node.getLabel() : key.getId(), 
        		key.getEncoded(),
        		false
        );
        prepareModel(node, result);
        return result;
    }
    
    private static OneToManyModel toOneToManyModel(ContentKey key, int idx) {
        ContentNodeI node = key.getContentNode();
        OneToManyModel result = new OneToManyModel(
                key.getType().getName(), 
                key.getEncoded(), 
                node != null ? node.getLabel() : key.getId(), 
                idx);
        prepareModel(node, result);
        return result;
    }

    private static void prepareModel(ContentNodeI node, ContentNodeModel result) {
        if (node != null) {
            result.setHasChildren(node.getChildKeys().size() > 0);
            if (result.isHtmlType()) {
                Object path = node.getAttribute("path").getValue();
                result.setPreviewUrl(FDStoreProperties.getCmsMediaBaseURL() + path);
            }
            if (result.isImageType()) {
                Object path = node.getAttribute("path").getValue();
                result.setPreviewUrl(FDStoreProperties.getCmsMediaBaseURL() + path);
                Object width = node.getAttribute("width").getValue();
                result.setWidth((Integer) width);
                Object height = node.getAttribute("height").getValue();
                result.setHeight((Integer) height);
            }
        }
    }

    // =========================== CHANGE SETS ===========================  
	
    public static List<GwtChangeSet> getGwtChangeSets(List<ChangeSet> changeSet) {
        List<GwtChangeSet> result = new ArrayList<GwtChangeSet>(changeSet.size());
        for (ChangeSet s : changeSet) {
            result.add(getGwtChangeSet(s));
        }
        return result;
    }

	public static List<GwtChangeSet> getGwtChangeSets(ChangeSet changeSet) {
        List<GwtChangeSet> result = new ArrayList<GwtChangeSet>(1);
        result.add(getGwtChangeSet(changeSet));
        return result;
    }

    @SuppressWarnings("unchecked")
	public static GwtChangeSet getGwtChangeSet(ChangeSet changeSet) {
        GwtChangeSet gwt = new GwtChangeSet ();
        gwt.setId(changeSet.getId());
        gwt.setModifiedDate(changeSet.getModifiedDate());
        gwt.setUserId(changeSet.getUserId());
        gwt.setNote(changeSet.getNote());
        for ( ContentNodeChange cnc : (List<ContentNodeChange>)changeSet.getNodeChanges() ) {
            gwt.addChange(getGwtContentNodeChange(cnc));
        }
        return gwt;
    }

    @SuppressWarnings("unchecked")
	private static GwtContentNodeChange getGwtContentNodeChange(ContentNodeChange cnc) {
        GwtContentNodeChange gcnc = new GwtContentNodeChange();
        gcnc.setChangeType(cnc.getChangeType().getName());
        gcnc.setContentType(cnc.getContentKey().getType().getName());
        gcnc.setContentKey(cnc.getContentKey().getId());
        ContentNodeI contentNode = cnc.getContentKey().getContentNode();
        gcnc.setLabel(contentNode != null ? contentNode.getLabel() : cnc.getContentKey().getEncoded());
        for ( ChangeDetail cd : (List<ChangeDetail>)cnc.getChangeDetails() ) {
            gcnc.addDetail(new GwtChangeDetail(cd.getAttributeName(), cd.getOldValue(), cd.getNewValue()));
        }
        return gcnc;
    }
	
	// =========================== private methods  ===========================
	
    private static ContentNodeI findEditor(ContentType type) {
        String typeName = type.getName();
        Set<ContentKey> editorKeys = CmsManager.getInstance().getContentKeysByType(ContentType.get("CmsEditor"));
        Map<ContentKey, ContentNodeI> nodes = CmsManager.getInstance().getContentNodes(editorKeys);
        for (ContentNodeI node : nodes.values()) {
            if (typeName.equals(node.getAttribute("contentType").getValue().toString())) {
                return node;
            }
        }
        return null;
    }

	
    public static GwtPublishData getPublishData(Publish p) {
        GwtPublishData d = new GwtPublishData();
        d.setId(p.getId());
        d.setComment(p.getDescription());
        d.setPublisher(p.getUserId());
        d.setCreated(p.getTimestamp());
        d.setStatus(p.getStatus().getDescription());
        return d;
    }

    public static GwtPublishMessage getPublishMessage(PublishMessage pm) {
        GwtPublishMessage g = new GwtPublishMessage ();
        g.setContentId(pm.getContentId());
        g.setContentType(pm.getContentType());
        g.setMessage(pm.getMessage());
        g.setTimestamp(pm.getTimestamp());
        g.setSeverity(pm.getSeverity());
        return g;
    }
    
    public static List<GwtPublishMessage> getPublishMessages(Collection<PublishMessage> list) {
        List<GwtPublishMessage> result = new ArrayList<GwtPublishMessage>(list.size());
        for (PublishMessage pm : list) {
            result.add(getPublishMessage(pm));
        }
        return result;
    }

    public static List<GwtPublishMessage> getPublishMessages(Publish publish) {
        return getPublishMessages(publish.getMessages());
    }
    
}
