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
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.BulkEditModel;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeContext;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtPublishData;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.ModifiableAttributeI;
import com.freshdirect.cms.ui.model.attributes.OneToManyAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToOneAttribute;
import com.freshdirect.cms.ui.model.attributes.SimpleAttribute;
import com.freshdirect.cms.ui.model.changeset.GwtChangeDetail;
import com.freshdirect.cms.ui.model.changeset.GwtChangeSet;
import com.freshdirect.cms.ui.model.changeset.GwtContentNodeChange;

/**
 * Static class for converting server-side CMS data types to client-side serializable data types for Gwt.
 * 
 * @author treer
 */

public class TranslatorToGwt {

	public static ContentNodeModel getContentNodeModel( ContentNodeI node ) {
		return new ContentNodeModel(node.getDefinition().getType().getName(), node.getLabel(), node.getKey().getEncoded(), node.getChildKeys().size() > 0 );
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

	@SuppressWarnings("unchecked")
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
			translatedAttributes.put( key, translateAttribute( attributeMap.get( key ), tabs != null ? tabs.getCustomFieldDefinition( key ) : null ) );
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
	@SuppressWarnings("unchecked")
	public static ModifiableAttributeI translateAttribute(AttributeI attribute, CustomFieldDefinition customFieldDefinition) {
		
		AttributeDefI definition = attribute.getDefinition();
		String name = definition.getLabel();
		EnumAttributeType type = definition.getAttributeType();
		Object value = attribute.getValue();
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
			EnumDef enumD = (EnumDef)definition;
			EnumAttribute enumA = new EnumAttribute();
			enumA.setLabel( name );
			Serializable currValue = (Serializable)value;

			for ( Map.Entry<Serializable, String> e : ((Map<Serializable,String>)enumD.getValues()).entrySet() ) {			
				enumA.addValue( e.getKey(), e.getValue() );
				if ( e.getKey().equals( currValue ) ) {
					enumA.setValue( e.getKey(), e.getValue() );
				}
			}

			attr = enumA;
		} else if ( type == EnumAttributeType.RELATIONSHIP ) {
			RelationshipDef relD = (RelationshipDef)definition;
			HashSet<String> cTypes = new HashSet<String>();

			for ( Object k : relD.getContentTypes() ) {
				ContentType ct = (ContentType)k;
				cTypes.add( ct.getName() );
			}

			if ( relD.isCardinalityOne() ) {
				ContentKey v = (ContentKey)value;
				OneToOneAttribute ooAttr = new OneToOneAttribute();
				ooAttr.setLabel( name );
				if ( v != null ) {
					ContentNodeModel model = new ContentNodeModel(v.getType().getName(), v.getContentNode().getLabel(), v.getEncoded() );
					ooAttr.setValue( model );
				}
				ooAttr.setAllowedTypes( cTypes );
				attr = ooAttr;
			} else {
				Collection<ContentKey> values = (Collection<ContentKey>) value;
				OneToManyAttribute ooAttr = new OneToManyAttribute( cTypes );
				ooAttr.setLabel( name );
				ooAttr.setNavigable( relD.isNavigable() );
				if ( values != null ) {
					int idx = 0;
					for ( ContentKey k : values ) {
					    ContentNodeI contentNode = k.getContentNode();
						OneToManyModel model = new OneToManyModel( k.getType().getName(), k.getEncoded(), contentNode.getLabel(),
								idx );
						if (customFieldDefinition != null) {
						    if (customFieldDefinition.getGridColumns() != null) {
						        for (String col : customFieldDefinition.getGridColumns()) {
						            Object attrValue = contentNode.getAttribute(col).getValue();
						            model.set(col, attrValue);
						        }
                            }
						    // for variation matrix, we need the skus other properties too.
						    if (customFieldDefinition.getType() == CustomFieldDefinition.Type.VariationMatrix) {
                                model.set("VARIATION_MATRIX", toClientValues(contentNode.getAttribute("VARIATION_MATRIX").getValue()));
                                model.set("VARIATION_OPTIONS", toClientValues(contentNode.getAttribute("VARIATION_OPTIONS").getValue()));
						    }
						}
						
						ooAttr.addValue( model );
						idx++;
					}
				}
				attr = ooAttr;
			}
		}
		attr.setInheritable( definition.isInheritable() );
		attr.setReadonly( definition.isReadOnly() );
		
		return attr;
		
	}	


	@SuppressWarnings("unchecked")
	private static Object toClientValues( Object value ) {
	    if ( value instanceof List ) {
	        List<Object> result = new ArrayList<Object>();
	        for ( Object item : (List<Object>)value ) {
				result.add( toClientValues( item ) );
			}
	        return result;
	    }
	    if ( value instanceof String ) {
			return value;
		}
		if ( value instanceof ContentKey ) {
			ContentKey ck = ( (ContentKey)value );
			return new ContentNodeModel( ck.getType().getName(), null, ck.getEncoded() );
		}
		return null;
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
        gcnc.setContentKey(cnc.getContentKey().getEncoded());
        for ( ChangeDetail cd : (List<ChangeDetail>)cnc.getChangeDetails() ) {
            gcnc.addDetail(new GwtChangeDetail(cd.getAttributeName(), cd.getOldValue(), cd.getNewValue()));
        }
        return gcnc;
    }
	
	// =========================== private methods  ===========================
	
	@SuppressWarnings("unchecked")
	private static ContentNodeI findEditor( ContentType type ) {
		String typeName = type.getName();
		Set<String> editorKeys = CmsManager.getInstance().getContentKeysByType( ContentType.get( "CmsEditor" ) );
		Map<String,ContentNodeI> nodes = CmsManager.getInstance().getContentNodes( editorKeys );
		for ( ContentNodeI node : nodes.values() ) {
			if ( typeName.equals( node.getAttribute( "contentType" ).getValue().toString() ) ) {
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
	
}
