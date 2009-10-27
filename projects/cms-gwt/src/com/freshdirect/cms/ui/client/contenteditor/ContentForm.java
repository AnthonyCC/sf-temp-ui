package com.freshdirect.cms.ui.client.contenteditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.freshdirect.cms.ui.client.fields.CustomGridField;
import com.freshdirect.cms.ui.client.fields.InheritanceField;
import com.freshdirect.cms.ui.client.fields.OneToManyRelationField;
import com.freshdirect.cms.ui.client.fields.OneToOneRelationField;
import com.freshdirect.cms.ui.client.fields.PrimaryHomeSelectorField;
import com.freshdirect.cms.ui.client.fields.ProductConfigEditor;
import com.freshdirect.cms.ui.client.fields.TableField;
import com.freshdirect.cms.ui.client.fields.VariationMatrixField;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.TabDefinition;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToManyAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToOneAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute.ProductConfigParams;

public class ContentForm extends FormPanel {
	
	public static final int FORM_WIDTH = 700;
	
	protected void initialize() {
		setLabelAlign(LabelAlign.RIGHT);
		setHeaderVisible(false);
        setBorders(false);
        setBodyBorder(false);
	}
	
	/**
	 * Render the full node, without considering the tab definitions.
	 * @param node
	 */
	public ContentForm( GwtNodeData node, String contextPath ) {
		super();
		initialize();
		setStyleName("notab-form");
		
		TemplateFormLayout layout = new TemplateFormLayout();		
		layout.setParameterFactory(new AlternateRenderer(layout));
		
		FieldSet section = new FieldSet();
		section.setLayout( layout );
		section.setWidth( FORM_WIDTH );
		section.setCollapsible( false );
		section.setBorders( false );
			
		List<String> attrKeys = new ArrayList<String>( node.getNode().getAttributeKeys() );
		Collections.sort( attrKeys );
		
		String ctxPath = contextPath == null ? node.getDefaultContextPath() : contextPath;
		
		for ( String attributeKey : attrKeys ) {	
			Field<Serializable> field = createField( attributeKey, node, ctxPath );
			if (field != null) {
			    section.add(field);			
			}
		}	
		
		add(section);
	}
	
	/**
	 * 	Renders a tab page, considering tab definitions.
	 * @param tabId
	 * @param nodeData
	 */
	public ContentForm( String tabId, GwtNodeData nodeData, String contextPath ) {
		super();
		initialize();
		TabDefinition tabDefinition = nodeData.getTabDefinition();
		GwtContentNode node = nodeData.getNode();
		
		setHeading( node.getLabel() );
		int containerIndex = 0;
		
		for ( String sectionId : tabDefinition.getSectionIds( tabId ) ) {
			String sectionLabel = tabDefinition.getSectionLabel( sectionId );
			
			TemplateFormLayout layout = new TemplateFormLayout();
			layout.setParameterFactory( new SectionAlternateRenderer( layout, containerIndex ) );
			
			FieldSet section = new FieldSet();
			section.setHeading( sectionLabel );
			section.setLayout( layout );
			section.setWidth( FORM_WIDTH );
			section.setCollapsible( true );
			
			String ctxPath = contextPath == null ? nodeData.getDefaultContextPath() : contextPath;
			
			for ( String attributeKey : tabDefinition.getAttributeKeys( sectionId ) ) {
				Field<Serializable> field = createField( attributeKey, nodeData, ctxPath );
				if ( field != null ) {
					section.add( field );
					containerIndex++;
				}
			}
			
			add(section);			
		}
	}	
	
	/**
	 * 	Creates a content editor field. This will be an InheritanceField for inheritable attributes.
	 * 
	 * @param attributeKey
	 * @param node
	 * @return
	 */
    @SuppressWarnings("unchecked")
    protected Field<Serializable> createField(String attributeKey, GwtNodeData nodeData, String contextPath) {

    	GwtContentNode node = nodeData.getNode();
        ContentNodeAttributeI attribute = node.getOriginalAttribute(attributeKey);
        Serializable value = node.getAttributeValue(attributeKey);
        if (value instanceof Collection && ((Collection) value).isEmpty()) {
            value = null;
        }
        
        boolean readonly = attribute.isReadonly() || nodeData.isReadonly() ;
        
		final Field<Serializable> innerField = createFieldInner( attributeKey, nodeData, value, attribute, readonly );
		if ( innerField == null ) {
			return null;
		}
		
		innerField.setReadOnly( readonly );
		
        Field<Serializable> field;
		if ( attribute.isInheritable() ) {
			field = new InheritanceField<Serializable>( innerField, value == null, attributeKey, readonly );
			ContentNodeAttributeI attr = nodeData.getContexts() == null ? null : nodeData.getContexts().getInheritedAttribute( contextPath, attributeKey );
			Serializable inhvalue = attr == null ? null : attr.getValue();
			( (InheritanceField<Serializable>)field ).setInheritedValue( inhvalue );
			
		} else {
			field = innerField;
		}

		attribute.setFieldObject( field );
		
		if ( readonly ) {
            field.setFieldLabel( "<span class=\"readonly\">" + attribute.getLabel() + "</span>" );
        } else {
            field.setFieldLabel( attribute.getLabel() );
        }

        // TODO contentkey tooltip...
        field.setToolTip(new ToolTipConfig(attributeKey));

        return field;
    }
	
	/**
	 * 	Creates the inner field for content editors depending on the attribute type. This will be optionally wrapped with an InheritanceField.
	 * 
	 * @param attributeKey
	 * @param node
	 * @param value
	 * @param attribute
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Field createFieldInner( String attributeKey, GwtNodeData node, Serializable value, ContentNodeAttributeI attribute, boolean readonly ) {
	    
		CustomFieldDefinition customFieldDefinition = node.getTabDefinition() == null ? null : node.getTabDefinition().getCustomFieldDefinition( attributeKey );
		String type = attribute.getType();

		if ( customFieldDefinition != null ) {
			if ( customFieldDefinition.getType() == CustomFieldDefinition.Type.PrimaryHomeSelection ) {
				return new PrimaryHomeSelectorField( (ContentNodeModel)value, node.getContexts() );
			}
			if ( customFieldDefinition.getType() == CustomFieldDefinition.Type.ProductConfigEditor ) {
				
				OneToOneAttribute attr = (OneToOneAttribute) attribute;	//Sku
				ContentNodeModel model = attr.getValue();
				Double quantity = (Double)node.getNode().getAttributeValue( "QUANTITY" );
				String salesUnit = (String)node.getNode().getAttributeValue( "SALES_UNIT" );
				ProductConfigParams pcp = model.get( "PCE_CONFIG_PARAMS" );
				String configOptions = (String)node.getNode().getAttributeValue( "OPTIONS" );
				
				ProductConfigAttribute pcAttr = new ProductConfigAttribute();
				pcAttr.setLabel( attr.getLabel() );
				pcAttr.setValue( model );
				pcAttr.setReadonly( attr.isReadonly() );				
            	pcAttr.setConfigParams( pcp );
            	pcAttr.setQuantity( quantity );
            	pcAttr.setSalesUnit( salesUnit );
            	pcAttr.setConfigOptions( configOptions );				
				
				return new ProductConfigEditor( readonly, pcAttr );
			}
		}
	    
		if (type.equals("string")) {
			TextField<String> field = new TextField<String>();
			field.setValue((String) value);
			return field;
		}
		
		if (type.equals("double")) {
			NumberField field = new NumberField();	
			field.setPropertyEditorType(Double.class);			
			field.setValue((Number) value);
			return field;
		}
		
		if (type.equals("integer")) {
			NumberField field = new NumberField();			
			field.setPropertyEditorType(Integer.class);
			field.setValue((Number) value);
			return field;
		}
		
		if (type.equals("date")) {
			DateField field = new DateField();			
			field.setValue((Date) value);
			return field;
		}
		
		if (type.equals("boolean")) {
			CheckBox field = new CheckBox();
			field.setValue((Boolean) value);
			return field;
		}
		
		if (type.equals("enum")) {			
			EnumAttribute attr = (EnumAttribute)attribute;
			ListStore<EnumModel> store = new ListStore<EnumModel>();
			store.add( attr.getValues() );
			
			ComboBox<EnumModel> combo = new ComboBox<EnumModel>();
			combo.setStore( store );
			combo.setValueField( "key" );
			combo.setDisplayField( "label" );
			combo.setEditable( false );
			combo.setForceSelection( true );
			
			combo.setValue( (EnumModel) value );
				 			
			return combo;			
		}
		
		if (type.equals("onetoone")) {
			OneToOneAttribute attr = (OneToOneAttribute) attribute;
			OneToOneRelationField field = new OneToOneRelationField( attr.getAllowedTypes(), readonly );
			if (value != null) {
				field.setValue((ContentNodeModel)value);					
			}				
			return field;
		}
		
		if (type.equals("onetomany")) {
			OneToManyAttribute attr = (OneToManyAttribute) attribute;
			OneToManyRelationField field = null;
			if (customFieldDefinition!=null) {
			    if (customFieldDefinition.getType() == CustomFieldDefinition.Type.VariationMatrix) {
			        field = new VariationMatrixField(attr.getAllowedTypes(), node.getNode());
			    }
			    if (customFieldDefinition.getGridColumns() != null) {
			        field = new CustomGridField(attributeKey, attr.getAllowedTypes(), attr.isNavigable(), customFieldDefinition, readonly);
			    }
			} 
			if (field == null) {
				field = new OneToManyRelationField( attributeKey, attr.getAllowedTypes(), attr.isNavigable(), readonly );
			}
			
			if (value != null) {
			    field.setValue((List<OneToManyModel>)value);
			}
			return field;
		}
		if (type.equals("table")) {
		    TableAttribute tableAttr = (TableAttribute) attribute;
		    TableField t = new TableField( tableAttr, node.getNode().getKey(), attributeKey );
		    
		    return t;
		}
		
		return null;
	}
}
