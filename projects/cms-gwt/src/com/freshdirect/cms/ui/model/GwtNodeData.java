package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.freshdirect.cms.ui.client.fields.ChangeTrackingField;
import com.freshdirect.cms.ui.client.fields.InheritanceField;
import com.freshdirect.cms.ui.client.fields.PrimaryHomeSelectorField;
import com.freshdirect.cms.ui.client.fields.SaveListenerField;
import com.freshdirect.cms.ui.client.nodetree.TreeContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;
import com.google.gwt.i18n.client.DateTimeFormat;

public class GwtNodeData implements Serializable {

    private static final long serialVersionUID = -5024274253307157621L;

    GwtContentNode node;
    TabDefinition tabDefinition;
    GwtNodeContext contexts;

    String previewUrl;

    private GwtNodePermission permission;

    private String currentContext;

    private Map<String, Set<String>> parentMap;

    @SuppressWarnings("unused")
    private GwtNodeData() {
        // NOTE: GWT compiler demands default constructor!
        // DO NOT USE IT
    }

    /**
     * Constructor for prototype nodes
     */
    public GwtNodeData(GwtContentNode n, TabDefinition t, GwtNodePermission permission) {
        this.node = n;
        this.tabDefinition = t;

        this.permission = permission;
    }

    /**
     * Constructor for normal (persisted) nodes
     */
    public GwtNodeData(GwtContentNode n, TabDefinition t, GwtNodePermission permission, GwtNodeContext context, String previewUrl) {
        this.node = n;
        this.tabDefinition = t;

        this.permission = permission;

        this.contexts = context;
        this.previewUrl = previewUrl;
    }

    public GwtContentNode getNode() {
        return node;
    }

    public void setNode(GwtContentNode node) {
        this.node = node;
    }

    public TabDefinition getTabDefinition() {
        return tabDefinition;
    }

    public void setTabDefinition(TabDefinition tabDefinition) {
        this.tabDefinition = tabDefinition;
    }

    /**
     * Return permission set for the given node. NOTE: it can be null for prototype nodes!
     * 
     * @return permission set or null
     */
    public GwtNodePermission getPermission() {
        return this.permission;
    }

    public GwtNodeContext getContexts() {
        return contexts;
    }

    public void setContexts(GwtNodeContext contexts) {
        this.contexts = contexts;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Map<String, Set<String>> getParentMap() {
        return parentMap;
    }

    public void setParentMap(Map<String, Set<String>> parentMap) {
        this.parentMap = parentMap;
    }

    public String getDefaultContextPath() {
        if (contexts == null || contexts.size() == 0)
            return null;

        if (contexts.size() == 1)
            return contexts.getPaths().iterator().next();

        // FIXME FDX
        List<OneToManyModel> _homes = (List<OneToManyModel>) node.getOriginalAttributeValue("PRIMARY_HOME");

        if (_homes != null && !_homes.isEmpty()) {
            // FIXME FIXME FIXME FDX
            ContentNodeModel primaryHome = _homes.get(0) /* (ContentNodeModel)attr.getValue() */;
            String PHKey = primaryHome.getKey();
            for (String path : contexts.getPaths()) {
                if (path.contains(PHKey)) {
                    return path;
                }
            }
        }

        return null;
    }

    public Map<String, ContentNodeAttributeI> getDefaultContextMap() {
        return contexts.getInheritedAttributes(getDefaultContextPath());
    }

    public boolean hasTabs() {
        return !(tabDefinition == null || tabDefinition.getTabIds() == null || tabDefinition.getTabIds().size() == 0);
    }

    /**
     * This method collects the values from the UI fields into the attributes before sending to the server.
     * 
     * @return the extra, related nodes.
     * 
     */
    public void collectValuesFromFields() {
        for (Map.Entry<String, ContentNodeAttributeI> e : node.getOriginalAttributes().entrySet()) {
            if (!e.getValue().isReadonly()) {
                Field<? extends Serializable> fieldObject = e.getValue().getFieldObject();
                // field object can be null, if the field not rendered
                if (fieldObject != null) {
                    getValueFromField(e.getKey(), e.getValue().getFieldObject());
                }
            }
        }
    }

    private void getValueFromField(String name, Field<? extends Serializable> fieldObject) {
        if (fieldObject instanceof SaveListenerField) {
            // onSave will add nodes to the workingset
            ((SaveListenerField) fieldObject).onSave();
        }
        if (fieldObject instanceof ChangeTrackingField) {
            if (((ChangeTrackingField) fieldObject).isFieldValueChanged()) {
                Serializable value = ((ChangeTrackingField) fieldObject).getChangedValue();
                this.node.changeValue(name, value);
            }
        } else {
            Serializable value = (Serializable) fieldObject.getValue();
            if (value instanceof ProductConfigAttribute) {
                ProductConfigAttribute pcAttr = (ProductConfigAttribute) value;
                this.node.changeValue(name, pcAttr.getValue()); // Sku
                this.node.changeValue("QUANTITY", pcAttr.getQuantity());
                this.node.changeValue("SALES_UNIT", pcAttr.getSalesUnit());
                this.node.changeValue("OPTIONS", pcAttr.getConfigOptionsString());
            } else {
                this.node.changeValue(name, value);
            }
        }
    }

    /**
     * Returns node value suitable as form item
     * 
     * @param attributeKey
     * @return
     */
    public Serializable getFormValue(String attributeKey) {
        Serializable value = this.node.getAttributeValue(attributeKey);
        if (value instanceof Collection<?> && ((Collection<?>) value).isEmpty()) {
            value = null;
        }
        return value;
    }

    public Serializable getFieldValue(String attributeKey) {
        ContentNodeAttributeI attr = this.node.getOriginalAttribute(attributeKey);
        Field<? extends Serializable> field = attr.getFieldObject();
        if (field == null) {
            return attr.getValue();
        }
        return field.getValue();
    }

    public boolean isChanged() {
        for (Map.Entry<String, ContentNodeAttributeI> e : node.getOriginalAttributes().entrySet()) {
            if (isDirty(e.getKey(), e.getValue()))
                return true;
        }
        return false;
    }

    public boolean isAttributeChanged(String attributeKey) {
        final ContentNodeAttributeI a = node.getOriginalAttribute(attributeKey);
        return a != null && isDirty(attributeKey, a);
    }

    protected boolean isDirty(String attributeKey, ContentNodeAttributeI attr) {
        Field<? extends Serializable> fieldObject = attr.getFieldObject();
        // field object can be null, if the field not rendered
        if (fieldObject != null) {
            Serializable value = fieldObject.getValue();
            Serializable oldValue = node.getOriginalAttributeValue(attributeKey);
            if (!equal(value, oldValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equal(Serializable value, Serializable oldValue) {
        if (value != null) {
            if (value instanceof String) {
                value = ((String) value).replace('\n', ' ');
            }
            if (oldValue instanceof String) {
                oldValue = ((String) oldValue).replace('\n', ' ');
            }
            if (value instanceof Date && oldValue instanceof Date) {
                DateTimeFormat dateFormat = DateTimeFormat.getMediumDateFormat();
                value = dateFormat.format((Date) value);
                oldValue = dateFormat.format((Date) oldValue);
            }
            if (value instanceof TreeContentNodeModel) {
                return ((TreeContentNodeModel) value).getKey().equals(((TreeContentNodeModel) oldValue).getKey());
            }
            return value.equals(oldValue);
        } else {
            return oldValue == null;
        }
    }

    /**
     * Adjust field values of inherited attributes according to the given context path
     * 
     * @param contextPath
     */
    @SuppressWarnings("unchecked")
    public void changeContext(String contextPath, String storeKey) {
        setCurrentContext(contextPath);
        Map<String, ContentNodeAttributeI> attributes = getNode().getOriginalAttributes();
        Map<String, ContentNodeAttributeI> inheritedAttrs = getContexts().getInheritedAttributes(getCurrentContext());

        if (inheritedAttrs == null || attributes == null) {
            return;
        }

        for (String key : attributes.keySet()) {
            ContentNodeAttributeI attribute = attributes.get(key);
            Field<Serializable> fieldObject = (Field<Serializable>) attribute.getFieldObject();

            if (fieldObject != null && fieldObject instanceof InheritanceField && attribute.isInheritable()) {

                InheritanceField<Serializable> inheritanceField = (InheritanceField<Serializable>) fieldObject;

                ContentNodeAttributeI inhAttr = inheritedAttrs.get(key);
                if (inhAttr != null) {
                    inheritanceField.setInheritedValue(inhAttr.getValue());
                } else {
                    inheritanceField.setInheritedValue(null);
                }
            }
        }

        if (attributes.get("PRIMARY_HOME") != null) {
            ContentNodeAttributeI attribute = attributes.get("PRIMARY_HOME");
            @SuppressWarnings("unused")
            PrimaryHomeSelectorField field = (PrimaryHomeSelectorField) ((Field) attribute.getFieldObject());
            if (field != null) {
                field.changeStoreKey(storeKey);
            }
        }
    }

    public String getCurrentContext() {
        if (currentContext == null) {
            return getDefaultContextPath();
        }
        return currentContext;
    }

    public void setCurrentContext(String context) {
        if (context != null) {
            currentContext = context;
        } else {
            currentContext = getDefaultContextPath();
        }
    }
}
