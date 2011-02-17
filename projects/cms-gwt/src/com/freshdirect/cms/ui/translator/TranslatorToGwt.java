package com.freshdirect.cms.ui.translator;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
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

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.ITable;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
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
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.ModifiableAttributeI;
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
import com.freshdirect.cms.ui.model.publish.GwtPublishData;
import com.freshdirect.cms.ui.model.publish.GwtPublishMessage;
import com.freshdirect.cms.ui.service.ServerException;
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
    
    private static final Logger LOGGER = LoggerFactory.getInstance( TranslatorToGwt.class );
    
	
	// =========================== GwtNodeData ===========================

	/**
	 * 	Creates a GwtNodeData object from a ContentNodeI. 
	 * 	Fills in the node attributes, tab definitions, contexts (including inherited attributes)
	 * 	Read-only parameter sets a read-only flag in the node.
	 * @throws ServerException 
	 */
	public static GwtNodeData gwtNodeData( ContentNodeI node, boolean readOnly ) throws ServerException {
		TabDefinition tabDef = TranslatorToGwt.gwtTabDefinition( node );
		GwtContentNode gwtNode = TranslatorToGwt.getGwtNode( node, tabDef );
		GwtNodeContext ctx = TranslatorToGwt.gwtNodeContext( node, tabDef );
	    
	    return new GwtNodeData( gwtNode, tabDef, ctx, readOnly, PreviewLinkProvider.getLink( node.getKey() ) );
	}

	/**
	 *	Creates a new GwtNodeData object, with the supplied type and id.
	 *	Fills in tab definitions, but no context data. 	
	 */
	public static GwtNodeData gwtNodeDataSkeleton ( String type, String id ) throws ServerException {
            ContentNodeI node;
            try {
                ContentKey keey = ContentKey.create(ContentType.get(type), id);
                ContentNodeI oldNode = CmsManager.getInstance().getContentNode(keey);
                if (oldNode != null) {
                    // there is already a node, return null.
                    return null;
                }
                
                node = CmsManager.getInstance().createPrototypeContentNode(keey);
            } catch (InvalidContentKeyException e) {
                e.printStackTrace();
                throw new ServerException("Invalid content key : " + type + ":" + id);
            }

	    TabDefinition tabDef = TranslatorToGwt.gwtTabDefinition(node);
	    GwtContentNode gwtNode = TranslatorToGwt.getGwtNode(node, tabDef);
	    
	    return new GwtNodeData( gwtNode, tabDef, false );
	}
	
	public static GwtContentNode getGwtNode( ContentNodeI node, TabDefinition tabDefs ) throws ServerException {
		ContentKey contentKey = node.getKey();
		GwtContentNode gwtNode = new GwtContentNode( contentKey.getType().getName(), contentKey.getId() );
		gwtNode.setLabel( node.getLabel() );
		
		final ContentTypeDefI definition = node.getDefinition();
		for ( String key : (Set<String>)definition.getAttributeNames() ) {
			Object value = node.getAttributeValue( key );
			ModifiableAttributeI attr = translateAttribute(definition.getAttributeDef(key), value, tabDefs != null ? tabDefs.getCustomFieldDefinition(key) : null, node);
			gwtNode.setOriginalAttribute( key, attr );
		}

		return gwtNode;
	}	
	
	// =========================== TAB DEFINITIONS ===========================
	
	public static TabDefinition gwtTabDefinition( ContentNodeI n ) throws ServerException {
		TabDefinition def = gwtTabDefinition( n.getDefinition() );
		return def;
	}

	@SuppressWarnings( "unchecked" )
	public static TabDefinition gwtTabDefinition( ContentTypeDefI typeDef ) throws ServerException {
		
		TabDefinition tabDef = new TabDefinition();
		
		ContentNodeI editor = findEditor( typeDef.getType() );
		if ( editor == null ) {
			return tabDef;
		}

		Collection<ContentKey> tabs = (Collection<ContentKey>)editor.getAttributeValue( "pages" );
		for ( ContentKey tabKey : tabs ) {
			ContentNodeI tab = tabKey.getContentNode();			
			String tabId = tabKey.getId();			
			tabDef.addTab( tabId, tab.getLabel() );

			Collection<ContentKey> sections = (Collection<ContentKey>)tab.getAttributeValue( "sections" );
			for ( ContentKey sectKey : sections ) {
				ContentNodeI section = sectKey.getContentNode();
				String sectionId = sectKey.getId();
				tabDef.addSection( tabId, sectionId, section.getLabel() );
				
				Collection<ContentKey> attributes = (Collection<ContentKey>)section.getAttributeValue( "fields" );
				for ( ContentKey attrKey : attributes ) {
					ContentNodeI attr = attrKey.getContentNode();
					String attributeKey = (String) attr.getAttributeValue("attribute");					
					
					tabDef.addAttributeKey( sectionId, attributeKey );
					
					String attrType = attrKey.getType().getName();								
					
					if ( !"CmsField".equals( attrType ) ) {
						
						if ( "CmsGridField".equals( attrType ) ) {
							// we have to figure out, what type .. it must be a Relationship ...
							RelationshipDefI attributeDef = (RelationshipDefI)typeDef.getAttributeDef( attributeKey );
							
							if ( attributeDef.getContentTypes().size() != 1 ) {
								throw new ServerException( "Relation from " + typeDef.getName() + "::" + attributeKey
										+ " contains more than 1 type: " + attributeDef.getContentTypes() );
							}
							CustomFieldDefinition cfd = new CustomFieldDefinition( CustomFieldDefinition.Type.Grid );

							List<ContentKey> values = (List<ContentKey>)attr.getAttributeValue("columns");
							for ( ContentKey k : values ) {
								cfd.addColumn( (String)k.getContentNode().getAttributeValue("attribute"));
							}

							tabDef.addCustomFieldDefinition( attributeKey, cfd );
						}
						if ( "CmsCustomField".equals( attrType ) ) {
							tabDef.addCustomFieldDefinition( attributeKey, new CustomFieldDefinition( (String)attr.getAttributeValue( "component" ) ) );
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
	 * @throws ServerException 
	 */
	public static GwtNodeContext gwtNodeContext( ContentNodeI node, TabDefinition tabs) throws ServerException {
		return gwtNodeContext(node.getKey(), tabs);
	}
	
	/**
	 *	Creates a GwtNodeContext for the supplied content key. 
	 * @throws ServerException 
	 */
	@SuppressWarnings("unchecked")
	public static GwtNodeContext gwtNodeContext( ContentKey key, TabDefinition tabs ) throws ServerException {
		
		GwtNodeContext nodeContext = new GwtNodeContext();
		
		Collection<Context> contexts = (Collection<Context>)ContextService.getInstance().getAllContextsOf( key );
		
		for ( Context cx : contexts ) {
			ContextualContentNodeI cxNode = null;
			try { 
				cxNode = ContextService.getInstance().getContextualizedContentNode( cx );
			} catch ( IllegalArgumentException e ) {
				LOGGER.warn( "Invalid context found, skipping.", e );
			}
			if ( cxNode == null )
				break;
			Map<String,AttributeI> inheritedAttributes = (Map<String,AttributeI>)cxNode.getParentInheritedAttributes();			
			nodeContext.addContext( cx.getPath(TreeContentNodeModel.pathSeparator.charAt(0)), cx.getLabel(), translateAttributeMap( inheritedAttributes, tabs, key ) );
		}		
		return nodeContext;
	}

	
	
	// =========================== ATTRIBUTES ===========================  
	
	/**
	 * 	Translates a map of CMS AttributeI-s to a map of Gwt ContentNodeAttributeI-s.
	 * 
	 * @param attributeMap
	 * @return
	 * @throws ServerException 
	 */
	public static Map<String, ContentNodeAttributeI> translateAttributeMap( Map<String, AttributeI> attributeMap, TabDefinition tabs,ContentKey contentKey ) throws ServerException {
		if ( attributeMap == null )
			return null;
		
		Map<String, ContentNodeAttributeI> translatedAttributes = new HashMap<String, ContentNodeAttributeI>();

		ContentNodeI node = contentKey.getContentNode();
		for ( String key : attributeMap.keySet() ) {
		    AttributeI attributeI = attributeMap.get( key );
		    ModifiableAttributeI attribute = translateAttribute( attributeI.getDefinition(), attributeI.getValue(), tabs != null ? tabs.getCustomFieldDefinition( key ) : null, node );
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
	 * @throws ServerException 
	 */
    public static ModifiableAttributeI translateAttribute(AttributeDefI definition, Object value, CustomFieldDefinition customFieldDefinition,ContentNodeI node) throws ServerException {
        ModifiableAttributeI attr = translateAttributeInternal(definition, customFieldDefinition, value, node );
        if (attr == null) {
            throw new ServerException("Unknown attribute type "+ definition + ", in node :"+node.getKey());
        }
        return attr;
    }

    @SuppressWarnings( "unchecked" )
	private static ModifiableAttributeI translateAttributeInternal(AttributeDefI definition, CustomFieldDefinition customFieldDefinition, Object value, ContentNodeI node) {
        String name = definition.getLabel();
        EnumAttributeType type = definition.getAttributeType();
        ModifiableAttributeI attr = null;

        if (type == EnumAttributeType.STRING) {
            attr = new SimpleAttribute<String>("string", (String) value, name);
            
        } else if (type == EnumAttributeType.LONG_TEXT) {
            attr = new SimpleAttribute<String>("text", (String) value, name);
        } else if (type == EnumAttributeType.DOUBLE) {
            attr = new SimpleAttribute<Double>("double", (Double) value, name);
            
        } else if (type == EnumAttributeType.INTEGER) {
            attr = new SimpleAttribute<Integer>("integer", (Integer) value, name);
            
        } else if (type == EnumAttributeType.DATE) {
            attr = new SimpleAttribute<Date>("date", (Date) value, name);
            
        } else if (type == EnumAttributeType.BOOLEAN) {
        	if ( !definition.isInheritable() && value == null ) {
        		value = Boolean.FALSE;
        	}
            attr = new SimpleAttribute<Boolean>("boolean", (Boolean) value, name);
            
        } else if (type == EnumAttributeType.ENUM) {
        	attr = translateEnumDefToEnumAttribute( (EnumDef)definition, name, (Serializable)value );
        	
		} else if ( type == EnumAttributeType.RELATIONSHIP ) {
        	
            RelationshipDefI relD = (RelationshipDefI) definition;
            HashSet<String> cTypes = new HashSet<String>();

            for (Object k : relD.getContentTypes()) {
                ContentType ct = (ContentType) k;
                cTypes.add(ct.getName());
            }

			if ( relD.isCardinalityOne() ) {

				ContentKey valueKey = (ContentKey)value;
				
				if ( customFieldDefinition != null && customFieldDefinition.getType() == CustomFieldDefinition.Type.ProductConfigEditor ) {
					ProductConfigAttribute pcAttr = new ProductConfigAttribute();
					pcAttr.setLabel( name );
					if ( node != null && valueKey != null ) { 
						pcAttr.setValue( toContentNodeModel( valueKey ) );
						pcAttr.setConfigParams( getProductConfigParams( valueKey ) );
						pcAttr.setQuantity( (Double)node.getAttributeValue( "QUANTITY" ) );
						pcAttr.setSalesUnit( (String)node.getAttributeValue( "SALES_UNIT" ) );
						pcAttr.setConfigOptions( (String)node.getAttributeValue( "OPTIONS" ) );
					}
					attr = pcAttr;
				} else {
					OneToOneAttribute ooAttr = new OneToOneAttribute();
					ooAttr.setLabel( name );
					if ( valueKey != null ) {
						ContentNodeModel model = toContentNodeModel( valueKey );
						ooAttr.setValue( model );
					}
					ooAttr.setAllowedTypes( cTypes );				
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
                            ContentNodeI contentNode = k.getContentNode();
                            OneToManyModel model = toOneToManyModel(contentNode, idx);
                            
                            if (customFieldDefinition != null) {
                                if (customFieldDefinition.getGridColumns() != null) {
                                    for (String col : customFieldDefinition.getGridColumns()) {
                                        Object attrValue = contentNode.getAttributeValue(col);
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
            TableAttribute tableAttr = createTableAttribute(table);
            attr = tableAttr;
        } else {
            return null;
        }
        attr.setInheritable(definition.isInheritable());
        attr.setReadonly(definition.isReadOnly());

        return attr;
    }
    
    private static EnumAttribute translateEnumDefToEnumAttribute( EnumDef enumD, String name, Serializable currValue ) {    	
        EnumAttribute enumA = new EnumAttribute();
        enumA.setLabel(name);
        for ( Map.Entry<Object, String> e : enumD.getValues().entrySet() ) {
        	if ( e.getKey() instanceof Serializable ) {
	        	Serializable key = (Serializable)e.getKey();
	        	String value = e.getValue();
	            enumA.addValue(key, value);
	            if (key.equals(currValue)) {
	                enumA.setValue(key, value);
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
    public static TableAttribute createTableAttribute(ITable table) {
        AttributeDefI[] columnDefinitions = table.getColumnDefinitions();
        ContentNodeAttributeI[] columns = new ContentNodeAttributeI[columnDefinitions.length];
        TableAttribute.ColumnType[] columnTypes = new TableAttribute.ColumnType[columns.length];
        
        for (int i=0;i<columnDefinitions.length;i++) {
            columns[i] = translateAttributeInternal(columnDefinitions[i], null, null, (ContentNodeI)null);
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
                                    Object attributeValue = node.getAttributeValue(tokens[2]);
                                    if (attributeValue != null) {
                                        rowValue[i] = String.valueOf(attributeValue);
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
    

	// =========================== ContentNodeModel ===========================  
    
    public static TreeContentNodeModel toTreeContentNodeModel( ContentNodeI node ) {
    	return toTreeContentNodeModel( node, null );
    }    
    
    public static TreeContentNodeModel toTreeContentNodeModel( ContentNodeI node, TreeContentNodeModel parent ) {
    	if ( node == null ) 
    		return null;
    	
    	ContentKey key = node.getKey();
    	TreeContentNodeModel result = new TreeContentNodeModel( key.getType().getName(), node.getLabel(), key.getEncoded(), parent );
    	prepareModel( node, result );
    	return result;
    }
    
	public static ContentNodeModel toContentNodeModel( ContentKey key ) {
		return toContentNodeModel( key.getContentNode() );
	}
	
    public static ContentNodeModel toContentNodeModel( ContentNodeI node ) {
    	if ( node == null )
    		return null;
    	
    	ContentKey key = node.getKey();
        ContentNodeModel result = new ContentNodeModel (
        		key.getType().getName(), 
        		node.getLabel(), 
        		key.getEncoded()
        );
        prepareModel(node, result);
        return result;
    }
    
    private static OneToManyModel toOneToManyModel(ContentNodeI node, int idx ) {
        ContentKey key = node.getKey();
        OneToManyModel result = new OneToManyModel(
                key.getType().getName(), 
                key.getEncoded(), 
                node != null ? node.getLabel() : key.getId(), 
                idx);
        prepareModel(node, result);
        return result;
    }

    private static void prepareModel( ContentNodeI node, ContentNodeModel result ) {
        if (node != null) {
        	if ( result instanceof TreeContentNodeModel ) {
                ( (TreeContentNodeModel)result ).setHasChildren( node.getChildKeys().size() > 0 );
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

	public static GwtPublishData getPublishData( Publish p ) {
		GwtPublishData d = new GwtPublishData();
		d.setId( p.getId() );
		d.setComment( p.getDescription() );
		d.setPublisher( p.getUserId() );
		d.setCreated( p.getTimestamp() != null ?
				US_DATE_FORMAT.format(p.getTimestamp()) : "" );
		d.setLastModified( p.getLastModified() != null ?
				US_DATE_FORMAT.format(p.getLastModified()) : "" );
		d.setStatus( p.getStatus().getName() );
		return d;
    }

    public static GwtPublishMessage getPublishMessage( PublishMessage pm ) {
    	
    	String contentKey = pm.getContentKey() == null ? null : pm.getContentKey().getEncoded();
        GwtPublishMessage g = new GwtPublishMessage( pm.getContentType(), contentKey );
		g.setMessage( pm.getMessage() );
		g.setTimestamp( pm.getTimestamp() );
		g.setSeverity( pm.getSeverity() );
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
	
	public static List<GwtChangeSet> getGwtChangeSets( List<ChangeSet> changeSet, ChangeSetQuery query ) {
		List<GwtChangeSet> result = new ArrayList<GwtChangeSet>( changeSet.size() );
		for ( ChangeSet s : changeSet ) {
			/*
			 * Contributor filter
			 */
			if (query.getContributor() == null || s.getUserId().equals(query.getContributor())) {
				result.add( getGwtChangeSet( s, query ) );
			}
		}
		return result;
	}

	public static List<GwtChangeSet> getGwtChangeSets( ChangeSet changeSet ) {
		List<GwtChangeSet> result = new ArrayList<GwtChangeSet>( 1 );
		result.add( getGwtChangeSet( changeSet, new ChangeSetQuery() ) );
		return result;
	}

	public static GwtChangeSet getGwtChangeSet( ChangeSet changeSet, ChangeSetQuery query ) {
		GwtChangeSet gwt = new GwtChangeSet( 
				changeSet.getId(), 
				changeSet.getUserId(), 
				changeSet.getModifiedDate(), 
				changeSet.getNote() 
			);		
		
		for ( ContentNodeChange cnc : (List<ContentNodeChange>)changeSet.getNodeChanges() ) {
			/*
			 * Content type filter
			 */
			try {
				if (query.getContentType() == null || cnc.getContentKey().getType().getName().equals(query.getContentType())) {
					gwt.addChange( getGwtContentNodeChange( cnc ) );
				}
			} catch (NullPointerException e) {}
		}
		return gwt;
	}

	private static GwtNodeChange getGwtContentNodeChange( ContentNodeChange cnc ) {
		ContentKey key = cnc.getContentKey();
		ContentNodeI node = key.getContentNode();
		
		GwtNodeChange gcnc = new GwtNodeChange( 
				key.getType().getName(),				
				node == null ? "" : node.getLabel() == null ? "" : node.getLabel(), 
				key.getEncoded(), 
				cnc.getChangeType().getName(), 
				PreviewLinkProvider.getLink( key ) 
			);		
		
		for ( ChangeDetail cd : (List<ChangeDetail>)cnc.getChangeDetails() ) {
			String oldValue = cd.getOldValue();
			String newValue = cd.getNewValue();
			if ( node != null ) {
				AttributeDefI attrDef = node.getAttribute( cd.getAttributeName() ).getDefinition();
				// try to get the human-readable string representation of the enum attribute value if available
				// complicated tricks to do it in a fail-safe way 
				if ( attrDef instanceof EnumDefI ) {
					EnumDefI enumD = (EnumDefI)attrDef;
					Map<Object, String> valMap = enumD.getValues();
					
					String oldS = (String)valMap.get( oldValue );
					String newS = (String)valMap.get( newValue );
					
					if ( oldS != null || newS != null ) {
						oldValue = oldS;
						newValue = newS;
					} else {
						String oldI = null;
						String newI = null;
						try { 
							oldI = oldValue == null ? null : (String)valMap.get( new Integer(oldValue) );
							newI = newValue == null ? null : (String)valMap.get( new Integer(newValue) );
						} catch ( NumberFormatException ex ) {}
						
						if ( oldI != null || newI != null ) {
							oldValue = oldI;
							newValue = newI;
						}
					}
				}				
			}
			gcnc.addDetail( new GwtChangeDetail( cd.getAttributeName(), oldValue, newValue ) );
		}
		return gcnc;
	}	
		
    // =========================== ... ===========================  
    
    private static ContentNodeI findEditor(ContentType type) {
        String typeName = type.getName();
        Set<ContentKey> editorKeys = CmsManager.getInstance().getContentKeysByType(ContentType.get("CmsEditor"));
        Map<ContentKey, ContentNodeI> nodes = CmsManager.getInstance().getContentNodes(editorKeys);
        for (ContentNodeI node : nodes.values()) {
            if (typeName.equals(node.getAttributeValue("contentType").toString())) {
                return node;
            }
        }
        return null;
    }

    /**
     *  Extra infos for ProductConfigEditor component
     *  Sales units and config parameter enums. 
     *  
     * @param skuKey 
     * @return
     */
    public static ProductConfigParams getProductConfigParams( ContentKey skuKey ) {
		ContentNodeI sku = skuKey.getContentNode();
		Map<String,String> salesUnitsMap = new TreeMap<String,String>();
		List<EnumModel> salesUnits = new ArrayList<EnumModel>();
		for ( ContentNodeI su : ConfiguredProductValidator.getSalesUnits( sku ).values() ) {
			String id = su.getKey().getId();
			String label = ContentNodeUtil.getLabel( su );
			salesUnitsMap.put( id, label );
			
			EnumModel em = new EnumModel( id, label );
			salesUnits.add( em );
		}
		Collections.sort( salesUnits );
    	
    	
    	Map<String,EnumDef> enumDefMap = ConfiguredProductValidator.getDefinitionMap( sku );
    	List<EnumAttribute> enumAttrs = new ArrayList<EnumAttribute>( enumDefMap.size() );
    	
    	for ( Map.Entry<String, EnumDef> def : enumDefMap.entrySet() ) {
    		enumAttrs.add( translateEnumDefToEnumAttribute( def.getValue(), def.getKey(), null ) );                		
    	}

    	return new ProductConfigParams( salesUnits, enumAttrs );
    }
    
    @SuppressWarnings( "unchecked" )
	public static Map<String, List<ContentNodeModel>> getDomainValues( List<ContentNodeModel> domains ) {
        Map<String, List<ContentNodeModel>> result = new HashMap<String, List<ContentNodeModel>>();
        
        for (ContentNodeModel domain : domains) {
            ContentKey key = (ContentKey) TranslatorFromGwt.getServerValue(domain);
            ContentNodeI node = key.getContentNode();
            List<ContentKey> domainValues = (List<ContentKey>) node.getAttributeValue("domainValues");
            
            List<ContentNodeModel> tempList = new ArrayList<ContentNodeModel>();
            for (ContentKey domainValue : domainValues) {
                tempList.add(TranslatorToGwt.toContentNodeModel(domainValue.getContentNode()));
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

}
