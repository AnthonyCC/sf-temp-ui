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
import com.freshdirect.cms.ui.model.attributes.TableAttribute;

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
			
			for (String attributeKey : tabDefinition.getAttributeKeys(sectionId)) {
                            Field<Serializable> field = createField(attributeKey, nodeData, ctxPath);
                            if (field != null) {
                                section.add(field);
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
    protected Field<Serializable> createField(String attributeKey, GwtNodeData node, String contextPath) {

        ContentNodeAttributeI attribute = node.getNode().getOriginalAttribute(attributeKey);
        Serializable value = node.getNode().getAttributeValue(attributeKey);
        if (value instanceof Collection && ((Collection) value).isEmpty()) {
            value = null;
        }

        final Field<Serializable> innerField = createFieldInner(attributeKey, node, value, attribute);
        if (innerField == null) {
            return null;
        }
        innerField.setReadOnly(attribute.isReadonly() || node.isReadonly());

        Field<Serializable> field;
        if (attribute.isInheritable()) {
            field = new InheritanceField<Serializable>(innerField, value == null, attributeKey);
            ContentNodeAttributeI attr = node.getContexts() == null ? null : node.getContexts().getInheritedAttribute(contextPath, attributeKey);
            // Serializable inhvalue = attr == null ? null : attr instanceof
            // EnumAttribute ? ((EnumAttribute)attr).getEnumModel() :
            // attr.getValue();
            Serializable inhvalue = attr == null ? null : attr.getValue();
            ((InheritanceField<Serializable>) field).setInheritedValue(inhvalue);
        } else {
            field = innerField;
        }

        attribute.setFieldObject(field);

        if (attribute.isReadonly() || node.isReadonly()) {
            field.setFieldLabel( "<span class=\"readonly\">" + attribute.getLabel() + "</span>");
        } else {
            field.setFieldLabel(attribute.getLabel());
        }
        
        field.setLabelSeparator( "" );

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
	protected Field createFieldInner( String attributeKey, GwtNodeData node, Serializable value, ContentNodeAttributeI attribute ) {
	    
		CustomFieldDefinition customFieldDefinition = node.getTabDefinition() == null ? null : node.getTabDefinition().getCustomFieldDefinition( attributeKey );
		String type = attribute.getType();

	    if (customFieldDefinition != null) {
	        if (customFieldDefinition.getType() == CustomFieldDefinition.Type.PrimaryHomeSelection) {
	            return new PrimaryHomeSelectorField((ContentNodeModel) value, node.getContexts());
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
			OneToOneRelationField field = new OneToOneRelationField( attr.getAllowedTypes() );
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
			        field = new CustomGridField(node, attributeKey, attr.getAllowedTypes(), attr.isNavigable(), customFieldDefinition);
			    }
			} 
			if (field == null) {
			    field = new OneToManyRelationField(node, attributeKey, attr.getAllowedTypes(), attr.isNavigable());    
			}
			
			if (value != null) {
			    field.setValue((List<OneToManyModel>)value);
			}
			return field;
		}
		if (type.equals("table")) {
		    TableAttribute tableAttr = (TableAttribute) attribute;
		    TableField t = new TableField(tableAttr);
		    
		    return t;
		}
		
		return null;
	}
}
