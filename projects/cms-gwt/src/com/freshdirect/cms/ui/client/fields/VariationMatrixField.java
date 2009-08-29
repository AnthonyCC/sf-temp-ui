package com.freshdirect.cms.ui.client.fields;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.freshdirect.cms.ui.client.WorkingSet;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.model.GwtContentNode;
import com.freshdirect.cms.ui.model.OneToManyModel;
import com.freshdirect.cms.ui.model.attributes.ContentNodeAttributeI;
import com.freshdirect.cms.ui.model.attributes.OneToManyAttribute;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class VariationMatrixField extends OneToManyRelationField implements SaveListenerField {

    private final class DropDownChangeListener implements Listener<FieldEvent> {
        private final List<ContentNodeModel>               skuDomainValues;
        private ContentNodeModel oldValue;

        private DropDownChangeListener(List<ContentNodeModel> skuDomainValues, ContentNodeModel oldValue) {
            this.skuDomainValues = skuDomainValues;
            this.oldValue = oldValue;
        }

        @Override
        public void handleEvent(FieldEvent be) {
           ContentNodeModel newValue  = (ContentNodeModel) be.getField().getValue();
           
           if (oldValue!=null) {
               skuDomainValues.remove(oldValue);
           }
           if (newValue!=null) {
               skuDomainValues.add(newValue);
           }
           oldValue = newValue;
        }
    }

    public class DropDownRenderer implements GridCellRenderer<OneToManyModel> {

        String domain;
        
        DropDownRenderer (String modelProperty) {
            this.domain = modelProperty;
        }
        
        @Override
        public Object render(OneToManyModel model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<OneToManyModel> store,
                Grid<OneToManyModel> grid) {
            // we know the domain, which we want to display in this column
            // we have to figure out 
            if (domainMap!=null && domainMap.get(domain)!=null) {
                
                final List<ContentNodeModel> skuDomainValues = model.get(SKU_DOMAIN_VALUES);
                if (skuDomainValues!=null) {
                    for (int i = 0; i < skuDomainValues.size(); i++) {
                    
                        ContentNodeModel skuDomainValue = skuDomainValues.get(i);
                        String skuDomain = reverseDomainValueMap.get(skuDomainValue.getKey());
                        if (domain.equals(skuDomain)) {
                            return createComboBoxField(skuDomainValues, skuDomainValue);
                        }
                    }
                }
                return createComboBoxField(skuDomainValues, null);
            }
            return "Domain : "+ domain;
        }

        private Object createComboBoxField(final List<ContentNodeModel> skuDomainValues, final ContentNodeModel skuDomainValue) {
            List<ContentNodeModel> list = domainMap.get(domain);
            final ComboBox<ContentNodeModel> field = createField(list, skuDomainValue);
            field.addListener(Events.Blur, new DropDownChangeListener(skuDomainValues, skuDomainValue));
            return field;
        }
        
        ComboBox<ContentNodeModel> createField(List<ContentNodeModel> domainValues, ContentNodeModel value) {
            ListStore<ContentNodeModel> store = new ListStore<ContentNodeModel>();
            store.add(new ContentNodeModel("DomainValue","Select one", null));
            store.add(domainValues);
            
            ComboBox<ContentNodeModel> field = new ComboBox<ContentNodeModel>();
            field.setStore(store);
            field.setValueField("key");
            field.setDisplayField("label");     
            field.setEditable(false);
            if (value == null) {
                field.setValue(null);
            } else {
                for (ContentNodeModel t : domainValues) {
                    if (t.getKey().equals(value.getKey())) {
                        field.setValue(t);
                        break;
                    }
                }
            }

            return field;
        }
        
    }

    private final static String SKU_DOMAIN_VALUES = "VARIATION_MATRIX";

    private final static String ATTR_DOMAIN_LIST = "VARIATION_MATRIX";
//    private final static String ATTR_DOMAIN_VALUES = "domainValues";

    GwtContentNode node;
    /**
     * Maps from Domain keys to DomainValues.
     */
    Map<String, List<ContentNodeModel>> domainMap;
    /**
     * Maps from DomainValue keys to Domain keys.
     */
    Map<String, String> reverseDomainValueMap = new HashMap<String, String>();
    
    
    @SuppressWarnings("unchecked")
	public VariationMatrixField(Set<String> allowedTypes, GwtContentNode node) {
        super();
        this.allowedTypes = allowedTypes;
        this.navigable = true;
        this.node = node;
        ContentNodeAttributeI attribute2 = this.node.getOriginalAttribute(ATTR_DOMAIN_LIST);
        if (attribute2 instanceof OneToManyAttribute) {
            OneToManyAttribute attribute = (OneToManyAttribute) attribute2;
            
            Field<Serializable> field = attribute.getFieldObject();
            // we hope that field is already initialized ... if not, we are in a deep trouble ..
            field.addListener(AttributeChangeEvent.TYPE, new Listener<AttributeChangeEvent>() {
                @Override
                public void handleEvent(AttributeChangeEvent be) {
                    Object newValue = be.getField().getValue();
                    if (newValue instanceof List) {
                        reloadDomains((List<ContentNodeModel>) newValue, true);
                    }                    
                }
            });
            
            List<ContentNodeModel> domains = (List)attribute.getModelValues();
            
            reloadDomains(domains, false);
        }
        
        initialize();
    }

    void reloadDomains(List<ContentNodeModel> domains, final boolean refreshColumns) {
        ContentServiceAsync cs = (ContentServiceAsync) GWT.create(ContentService.class);
        cs.getDomainValues(domains, new AsyncCallback<Map<String, List<ContentNodeModel>>> () {
            @Override
            public void onFailure(Throwable caught) {
                // TODO Show error message?                
            }
            
            @Override
            public void onSuccess(Map<String, List<ContentNodeModel>> result) {
                VariationMatrixField.this.resetDomainMap(result);
                if (refreshColumns) {
                    VariationMatrixField.this.refreshColumns();
                }
            }
        });

        this.extraColumns = new ArrayList<GridCellRenderer<OneToManyModel>>();
        this.extraColumns.add(new Renderers.LabelRenderer("label"));
        for (ContentNodeModel domain : domains) {
            extraColumns.add(new DropDownRenderer(domain.getKey()));
        }
        
    }
    
    void resetDomainMap(Map<String, List<ContentNodeModel>> result) {
        this.domainMap = result;
        
        // we have to recalculate the reverse domain maps ...
        this.reverseDomainValueMap.clear();
        for (Map.Entry<String, List<ContentNodeModel>> e : domainMap.entrySet()) {
            for(ContentNodeModel domainValue : e.getValue()) {
                reverseDomainValueMap.put(domainValue.getKey(), e.getKey());
            }
        }
        this.grid.getView().refresh(false);
    }

    @Override
    protected OneToManyModel createModel(String type, String key, String label) {
        OneToManyModel s = super.createModel(type, key, label);
        ensureSkuDomainValues(s);
        return s;
    }

    /**
     * Variation matrix doesn't like when there is no domain values specified for a sku.
     * @param s
     */
    private void ensureSkuDomainValues(OneToManyModel s) {
        if (s.get(SKU_DOMAIN_VALUES)==null) {
            s.set(SKU_DOMAIN_VALUES, new ArrayList<ContentNodeModel>());
        }
    }

    @Override
    public void setValue(List<OneToManyModel> values) {
        for (OneToManyModel v : values) {
        	ensureSkuDomainValues((OneToManyModel) v);
        }
        super.setValue(values);
    }

    @Override
    public void onSave() {
        for (int i = 0; i < this.store.getCount(); i++) {
            OneToManyModel model2 = this.store.getAt(i);
            
            GwtContentNode newNode = new GwtContentNode(model2.getKey());
//            List<String> skuDomainValues = model2.get(SKU_DOMAIN_VALUES);
//            List<ContentTreeModel> serverSkuDomainValues = new ArrayList()
            newNode.changeValue(SKU_DOMAIN_VALUES, (Serializable) model2.get(SKU_DOMAIN_VALUES));
            WorkingSet.add(newNode);
        }
    }
}
