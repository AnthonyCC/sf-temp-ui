package com.freshdirect.cms.ui.client.fields;

import java.util.HashSet;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;

public class OneToOneRelationField extends MultiField<ContentNodeModel> {

    private IconButton relationButton;
    private IconButton deleteButton;
    
    private LabelField valueField;

    private ContentNodeModel value;
    private HashSet<String> allowedTypes;
    
    protected boolean readonly;

	final static int                       MAIN_LABEL_WIDTH = 415;
	
    public OneToOneRelationField(HashSet<String> aTypes, boolean readonly) {
        super();
        allowedTypes = aTypes;
        this.readonly = readonly;
        initialize();
    }

    @Override
    public ContentNodeModel getValue() {
        return value;
    }
	
    @Override
    public void setValue(ContentNodeModel model) {
        this.value = model;

        if (value != null) {
            valueField.setValue(model.renderLink(true));
            valueField.setToolTip(model.getPreviewToolTip());
        } else {
            valueField.setValue("");
        }
    }
	
    private void initialize() {
    	
		ContentPanel cp = new ContentPanel();		
		cp.setWidth(MAIN_LABEL_WIDTH + 50);
		cp.setLayout( new ColumnLayout() );
		cp.setHeaderVisible( false );
		cp.setBorders( false );
		
        relationButton = new IconButton("rel-button");
        relationButton.setToolTip("Change relationship");
        relationButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            public void handleEvent(BaseEvent be) {
                final ContentTreePopUp popup = ContentTreePopUp.getInstance(allowedTypes);
                popup.setHeading(getFieldLabel());
                popup.addListener(Events.Select, new Listener<BaseEvent>() {
                    public void handleEvent(BaseEvent be) {
                        setValue(popup.getSelected());
                        fireEvent(AttributeChangeEvent.TYPE, new AttributeChangeEvent(OneToOneRelationField.this));
                    }
                });
                popup.show();
            }
        });
		cp.add( relationButton, new ColumnData( 20.0 ) );

		valueField = new LabelField();
		cp.add( valueField, new ColumnData( 420.0 ) );

        deleteButton = new IconButton("delete-button");
        deleteButton.setToolTip(new ToolTipConfig("DELETE", "Delete relation."));
        deleteButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                OneToOneRelationField.this.setValue(null);
            }
        });
		cp.add( deleteButton, new ColumnData( 20.0 ) );
        
		AdapterField f = new AdapterField( cp );
		f.setWidth( MAIN_LABEL_WIDTH + 70 );
		
		if ( readonly ) {
			cp.disable();
			f.setReadOnly( true );
			f.disable();
		}
		
		add(f);
		
    }
	
    @Override
    public void disable() {
        super.disable();
        if (relationButton != null) {
            relationButton.disable();
        }
        if (deleteButton != null) {
        	deleteButton.disable();
        }
    }

    @Override
    public void enable() {
        super.enable();
        if (relationButton != null) {
            relationButton.enable();
        }
        if (deleteButton != null) {
        	deleteButton.enable();
        }
    }
        
    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        setEnabled(!readOnly);
    }    
}
