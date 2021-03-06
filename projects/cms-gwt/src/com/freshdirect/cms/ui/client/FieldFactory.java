package com.freshdirect.cms.ui.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.freshdirect.cms.ui.client.fields.CmsMultiColumnField;
import com.freshdirect.cms.ui.client.fields.CustomGridField;
import com.freshdirect.cms.ui.client.fields.EnumField;
import com.freshdirect.cms.ui.client.fields.FieldResetPlugin;
import com.freshdirect.cms.ui.client.fields.InheritanceField;
import com.freshdirect.cms.ui.client.fields.LocationField;
import com.freshdirect.cms.ui.client.fields.OneToManyRelationField;
import com.freshdirect.cms.ui.client.fields.OneToOneRelationField;
import com.freshdirect.cms.ui.client.fields.PrimaryHomeSelectorField;
import com.freshdirect.cms.ui.client.fields.ProductConfigEditor;
import com.freshdirect.cms.ui.client.fields.TableField;
import com.freshdirect.cms.ui.client.fields.VariationMatrixField;
import com.freshdirect.cms.ui.client.views.ManageStoreView;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.GwtNodeData;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToManyAttribute;
import com.freshdirect.cms.ui.model.attributes.OneToOneAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * 	Creates the inner field for content editors depending on the attribute type. This will be optionally wrapped with an InheritanceField.
 *
 * @param source
 * @param key Attribute key
 * @param value
 * @param attribute
 * @return
 */
public final class FieldFactory {

	/**
	 * Creates a field for CMS attribute
	 *
	 * @param nodeData
	 * @param key
	 * @param readonly
	 * @param value Value of field (can be null)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Field<Serializable> createInnerField( final GwtNodeData nodeData, final GwtNodePermission permission, final String key, final Serializable value ) {
        final boolean readonly = permission.isReadonly();

    	final GwtContentNode aNode = nodeData.getNode();
        ContentNodeAttributeI attribute = aNode.getOriginalAttribute(key);

		CustomFieldDefinition customFieldDefinition = nodeData.getTabDefinition() == null ? null : nodeData.getTabDefinition().getCustomFieldDefinition( key );

		Field aField = null;


		/**
		 * Custom field types
		 */
		if ( customFieldDefinition != null ) {
			switch(customFieldDefinition.getType()) {
			case PrimaryHomeSelection:
				String _key = ManageStoreView.getInstance().getStoreKey();

				aField = new PrimaryHomeSelectorField( nodeData.getContexts(), nodeData.getParentMap(), _key, permission );
				((OneToManyRelationField)aField).setValue((List<OneToManyModel>)value);

				break;
			case ProductConfigEditor:
				ProductConfigAttribute pcAttr = null;
				if ( attribute instanceof ProductConfigAttribute ) {
					pcAttr = (ProductConfigAttribute)attribute;
				} else {
					pcAttr = new ProductConfigAttribute();
				}

            	ProductConfigEditor editor = new ProductConfigEditor( permission, pcAttr );
            	pcAttr.setFieldObject( editor );

            	aNode.setOriginalAttribute( key, new ProductConfigAttribute( pcAttr ) );

				aField = editor;
				break;
			case GmapsLocation:
				if ( CmsGwt.isGmapsApiLoaded() ) {
					aField = new LocationField();
				} else {
					aField = new TextField<String>();
				}
				aField.setOriginalValue(attribute.getValue());
				aField.setValue(attribute.getValue());
				break;
			case MonthDay:
				DateField field = new DateField();
				field.setValue((Date) attribute.getValue());
				field.getPropertyEditor().setFormat(DateTimeFormat.getFormat("MM/dd"));
				aField = field;
				break;
			default:
                break;
			}
		}


		if (aField == null) {
			final String type = attribute.getType();

			/**
			 * Simple field types
			 */
			if ("string".equals(type)) {
				TextField<String> field = new TextField<String>();
				field.setValue((String) value);
				aField = field;
			} else if ("text".equals(type)) {
                TextArea field = new TextArea();
                field.setValue((String) value);
                aField = field;

			} else if ("double".equals(type)) {
				NumberField field = new NumberField();
				field.setPropertyEditorType(Double.class);
				field.setValue((Number) value);
				aField = field;
			} else if ("integer".equals(type)) {
				NumberField field = new NumberField();
				field.setPropertyEditorType(Integer.class);
				field.setValue((Number) value);
				aField = field;
			} else if ("date".equals(type)) {
				DateField field = new DateField();
				field.setValue((Date) value);
				aField = field;
			} else if ("boolean".equals(type)) {
				CheckBox field = new CheckBox();
				field.setValue((Boolean) value);
				aField = field;
			} else if ("enum".equals(type)) {
				/* Following field can be used to select multiple items in the enum dropdown.
				EnumMultiSelectField field = new EnumMultiSelectField((MultiEnumAttribute)attribute);
				*/
				aField = new EnumField((EnumAttribute)attribute, (EnumModel) value );

			} else if ("time".equals(type)) {
				TimeField field = new TimeField();
				field.setTriggerAction(TriggerAction.ALL);
				if( value instanceof Time){
					Time time = field.findModel(((Time)value).getDate());
					field.select(time);
					field.setValue(time);
				}
				aField = field;
			} else if ("onetoone".equals(type)) {
				OneToOneAttribute attr = (OneToOneAttribute) attribute;
				OneToOneRelationField field = new OneToOneRelationField( attr.getAllowedTypes(), permission );
				if (value != null) {
					field.setValue((ContentNodeModel)value);
				}
				aField = field;
			} else if ("onetomany".equals(type)) {
				OneToManyAttribute attr = (OneToManyAttribute) attribute;
				OneToManyRelationField field = null;
				if (customFieldDefinition!=null) {
				    if (customFieldDefinition.getType() == CustomFieldDefinition.Type.VariationMatrix) {
				        field = new VariationMatrixField(key, attr.getAllowedTypes(), permission, aNode);
				    } else if (customFieldDefinition.getType() == CustomFieldDefinition.Type.CmsMultiColumnField) {
				    	field = new CmsMultiColumnField(key, attr.getAllowedTypes(), attr.isNavigable(), customFieldDefinition, permission, nodeData.getNode().getType());
				    } else if (customFieldDefinition.getGridColumns() != null) {
				        field = new CustomGridField(key, attr.getAllowedTypes(), attr.isNavigable(), customFieldDefinition, permission, nodeData.getNode().getType());
				    }
				}
				if (field == null) {
					field = new OneToManyRelationField(key, attr.getAllowedTypes(), attr.isNavigable(), permission, nodeData.getNode().getType());
				}

				if (value != null) {
				    field.setValue((List<OneToManyModel>)value);
				}
				else {
					field.setValue(new ArrayList<OneToManyModel>());
				}
				aField = field;
			} else if ("table".equals(type)) {
			    TableAttribute tableAttr = (TableAttribute) attribute;
			    TableField t = new TableField( tableAttr, aNode.getKey(), key );

				aField = t;
			}
		}

		/**
		 * Set field attributes
		 */
		if (aField != null) {
			aField.setReadOnly( readonly );
		}

		return aField;
	}



	/**
	 * Creates a standard form field for attribute specified by key parameter.
	 *
	 * This will be an InheritanceField for inheritable attributes.
	 *
	 * @param source
	 * @param key
	 * @param contextPath
	 *
	 * @return
	 */
    public static Field<Serializable> createStandardField(GwtNodeData cn, String key) {
    	final Serializable value = cn.getFormValue(key);
		Field<Serializable> field = createOtherField(cn, key, value, true);
    	if (field != null)
    		cn.getNode().getOriginalAttribute(key).setFieldObject(field);

    	return field;
    }

    @SuppressWarnings("unchecked")
	public static Field<Serializable> createOtherField(final GwtNodeData nodeData, final String key, Serializable value, boolean wrapInheritedField) {
    	ContentNodeAttributeI attribute = nodeData.getNode().getOriginalAttribute(key);

    	if ( attribute == null ) {
    		CmsGwt.log( "Null attribute [" + key + "] found in node [" + nodeData.getNode().getKey() + "]", true );
    		return null;
    	}

    	final GwtNodePermission permission = new GwtNodePermission( nodeData.getPermission() );

    	permission.setAttributeEditable( !attribute.isReadonly() );

        final boolean readonly = permission.isReadonly() ;


        Field<Serializable> field = null;

        /**
         * Create appropriate field editor
         */
		field = createInnerField(nodeData, permission, key, value);
		if ( field == null ) {
			return null;
		}

		// wrap field in inherited value editor if attribute is inheritable
		field = (wrapInheritedField && attribute.isInheritable() ) ? decorateInheritedValue(nodeData, permission, key, field) : field;

		field.setData("contentKey", key);

		/**
		 * Set label
		 */
		if ( readonly ) {
            field.setFieldLabel( "<span class=\"readonly\">" + attribute.getLabel() + "</span>" );
        } else {
            field.setFieldLabel( attribute.getLabel() );
		}


		field.addPlugin(new FieldResetPlugin());

        return field;
    }



    /**
     * Wrap field into an inheritable field
     */
	private static Field<Serializable> decorateInheritedValue( final GwtNodeData nodeData, final GwtNodePermission permission, String key, final Field<Serializable> innerField ) {
	    final boolean readonly = permission.isReadonly();

		final boolean isInherited = nodeData.getFormValue(key) == null;
		Field<Serializable> field = new InheritanceField<Serializable>( innerField, isInherited, readonly );
		ContentNodeAttributeI attr = nodeData.getContexts() == null ? null : nodeData.getContexts().getInheritedAttribute( nodeData.getCurrentContext(), key );
		Serializable inhvalue = attr == null ? null : attr.getValue();

		( (InheritanceField<Serializable>)field ).setValue( nodeData.getNode().getOriginalAttributeValue(key) );
		( (InheritanceField<Serializable>)field ).setInheritedValue( inhvalue );

		return field;
	}
}
