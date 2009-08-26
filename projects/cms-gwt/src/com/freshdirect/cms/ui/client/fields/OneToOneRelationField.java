package com.freshdirect.cms.ui.client.fields;

import java.util.HashSet;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.freshdirect.cms.ui.client.nodetree.ContentNodeModel;
import com.freshdirect.cms.ui.client.nodetree.ContentTreePopUp;

public class OneToOneRelationField extends MultiField<ContentNodeModel> {

	private IconButton relationButton;
	private IconButton showButton;
	private LabelField valueField;
	
	private ContentNodeModel value;
	private HashSet<String> allowedTypes;
	
	public OneToOneRelationField(HashSet<String> aTypes) {
		super();
		allowedTypes = aTypes;
		initialize();
	}
	
	@Override
	public ContentNodeModel getValue() {		
		return value;	
	}
	
	@Override
	public void setValue(ContentNodeModel v) {
		this.value = v;
		
		if ( showButton != null ) {
			if ( value != null && value.isMediaType() ) {
				showButton.enable();
				showButton.show();
	//			showButton.changeStyle( "normal" );
			} else {
				showButton.disable();
				showButton.hide();
	//			showButton.changeStyle( "disabled" );
			}
		}
			
		if ( value != null ) {
			valueField.setValue("<table class=\"content-label\"><tr><td><img src=\"img/icons/" + value.getType() + ".gif\"></td><td><a href=\"#" + value.getKey() + "\">" + value.getLabel() + "</a></td></tr></table>");			
		}
	}
	
	@Override
	public void setReadOnly( boolean readOnly ) {
		super.setReadOnly( readOnly );
		relationButton.setEnabled( !readOnly );		

		// TODO only works good if setReadOnly is called at most once!
//		if( readOnly ) {
//			setFieldLabel( "[R/O]" + getFieldLabel() );
//		}
	}
	
	private void initialize() {
		
		relationButton = new IconButton("rel-button");
		relationButton.setToolTip( "Change relationship" );
		relationButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				final ContentTreePopUp popup = ContentTreePopUp.getInstance( allowedTypes );
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
		add(new AdapterField(relationButton));
		
		// if this is some "media" type
		if ( allowedTypes.contains( "Image" ) || allowedTypes.contains( "Html" ) ) {
			showButton = new IconButton("show-button");
			showButton.setToolTip( "Show media" );
			showButton.addListener(Events.OnClick, new Listener<BaseEvent>() {
	
				public void handleEvent(BaseEvent be) {
					//TODO implement
					MessageBox.info( "Show media", "Showing media content here.", null );
				}
				
			});
			showButton.disable();
			add(new AdapterField(showButton));
		}
				
		valueField = new LabelField();		
		add(valueField);		
	}
	
    @Override
    public void disable() {
    	super.disable();
    	if ( relationButton != null )
    		relationButton.disable();
    }
    @Override
    public void enable() {
    	super.enable();
    	if ( relationButton != null )
    		relationButton.enable();
    }
}
