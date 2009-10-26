package com.freshdirect.cms.ui.client.fields;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.attributes.EnumAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute;
import com.freshdirect.cms.ui.model.attributes.ProductConfigAttribute.ProductConfigParams;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ProductConfigEditor extends MultiField<ProductConfigAttribute> {
	
	private OneToOneRelationField		skuField;
	private NumberField					quantityField;
	private ComboBox<EnumModel>			salesUnitField;
	private Map<String,ComboBox<EnumModel>> optionFields;

	protected ProductConfigAttribute	attribute;
	protected boolean					readonly;
	
	private ContentPanel mainPanel;
	
	public ProductConfigEditor( boolean readonly, ProductConfigAttribute pcAttr ) {
		super();
	
		this.readOnly = readonly;
		this.attribute = pcAttr;
		
		mainPanel = new ContentPanel();
		mainPanel.setWidth( OneToOneRelationField.MAIN_LABEL_WIDTH + 50 );
		mainPanel.setLayout( new RowLayout( Orientation.VERTICAL ) );
		mainPanel.setHeaderVisible( false );
		mainPanel.setBorders( false );	
		
		// === SKU ===
		skuField = new OneToOneRelationField( attribute.getAllowedTypes(), readonly );
		skuField.setValue( attribute.getValue() );
		
		skuField.addListener( Events.Change, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				rebuildFields();
			}
		});
		
		initFields();
		
		AdapterField f = new AdapterField( mainPanel );
		f.setWidth( OneToOneRelationField.MAIN_LABEL_WIDTH + 70 );		
		if ( readonly ) {
			f.setReadOnly( true );
		}		
		add(f);		
	}
	
	private void initFields() {
		
		// === QUANTITY ===
		quantityField = new NumberField();
		quantityField.setPropertyEditorType( Double.class );
		quantityField.setAllowNegative( false );
		quantityField.setAllowDecimals( true );
//		quantityField.setFormat( ... );
		quantityField.setValue( attribute.getQuantity() );
		quantityField.setFieldLabel( "Quantity" );
		quantityField.setHideLabel( false );
		
		
		// === SALES UNIT ===
		List<EnumModel> salesUnits = attribute.getSalesUnits();
		if ( salesUnits != null ) {
			ListStore<EnumModel> suStore = new ListStore<EnumModel>();
			suStore.add( salesUnits );
			
			salesUnitField = new ComboBox<EnumModel>();
			salesUnitField.setStore( suStore );
			salesUnitField.setValueField( "key" );
			salesUnitField.setDisplayField( "label" );
			salesUnitField.setEditable( false );
			salesUnitField.setForceSelection( true );
			salesUnitField.setAllowBlank( false );
			salesUnitField.setFieldLabel( "Sales unit" );
			
			String su = attribute.getSalesUnit();
			if ( su == null ) {
				salesUnitField.setValue( suStore.getAt( 0 ) );
			} else {
				for ( EnumModel em : suStore.getModels() ) {
					if ( em.getKey().equals( su ) ) {
						salesUnitField.setValue( em );
					}
				}
			}
		} else {
			salesUnitField = null;
		}
		
		// === CONFIGURATION OPTIONS ===
		VerticalPanel configPanel = new VerticalPanel();
		configPanel.setBorders( false );
		
		optionFields = new HashMap<String,ComboBox<EnumModel>>();
				
		List<EnumAttribute> configEnums = attribute.getConfigEnums();
		if ( configEnums != null ) {
			
			for ( EnumAttribute ea : configEnums ) {
				
				String id = ea.getLabel();			
				ListStore<EnumModel> configStore = new ListStore<EnumModel>();
				configStore.add( ea.getValues() );
							
				ComboBox<EnumModel> configField = new ComboBox<EnumModel>();
				configField.setStore( configStore );
				configField.setValueField( "key" );
				configField.setDisplayField( "label" );
				configField.setEditable( false );
				configField.setForceSelection( true );
				configField.setAllowBlank( false );
				configField.setFieldLabel( id );
				
				String optionValue = attribute.getConfigOption( id );	
				if ( optionValue == null ) {
					configField.setValue( configStore.getAt( 0 ) );
				} else {
					for ( EnumModel em : configStore.getModels() ) {
						if ( em.getKey().equals( optionValue ) ) {
							configField.setValue( em );
						}
					}
				}
				
				optionFields.put( id, configField );
			
				HorizontalPanel configRow = new HorizontalPanel();
				configRow.setBorders( false );	
				configRow.add( new Text( id ) );
				configRow.add( configField );
				
				configPanel.add( configRow );
			}
		}
		
		// === LAYOUT ===
		
		HorizontalPanel quantityPanel = new HorizontalPanel();
		quantityPanel.setBorders( false );	
		
		mainPanel.add( skuField );
		quantityPanel.add( new Text("Quantity:") );
		quantityPanel.add( quantityField );
		if ( salesUnitField != null ) {
			quantityPanel.add( salesUnitField );
		}
		mainPanel.add( quantityPanel );
		mainPanel.add( configPanel );		
	}
	
	private void rebuildFields() {
		
		if ( skuField.getValue() == null ) {
			attribute.setSalesUnits( null );	
			attribute.setSalesUnit( null );
			attribute.setConfigEnums( null );
			attribute.setConfigOptions( (Map<String, String>)null );
			
			mainPanel.removeAll();
			initFields();		
			mainPanel.layout();

		} else {		
			String skuKey = skuField.getValue().getKey();
			
			CmsGwt.getContentService().getProductConfigParams( skuKey, new AsyncCallback<ProductConfigParams>() {				
				@Override
				public void onSuccess( ProductConfigParams pcp ) {
					attribute.setConfigParams( pcp );
					attribute.setSalesUnit( null );
					attribute.setConfigOptions( (Map<String, String>)null );
					
					mainPanel.removeAll();
					initFields();		
					mainPanel.layout();
				}				
				@Override
				public void onFailure( Throwable caught ) {
					MessageBox.alert( "Error", "Error while loading product config parameters for the selected sku.", null );
				}
			} );			
		}				
	}
	
	@Override
	public ProductConfigAttribute getValue() {
		updateAttributeValues();
		return attribute;		
	}
	
	protected void updateAttributeValues() {		
		attribute.setValue( skuField.getValue() );
		attribute.setQuantity( quantityField.getValue().doubleValue() );
		attribute.setSalesUnit( salesUnitField != null ? salesUnitField.getValue().getKey().toString() : null );

		Map<String,String> confOpts = new HashMap<String, String>( optionFields.size() );
		
		for ( Map.Entry<String,ComboBox<EnumModel>> optField : optionFields.entrySet() ) {
			confOpts.put( optField.getKey(), optField.getValue().getValue().getKey().toString() );
		}		
		attribute.setConfigOptions( confOpts );		
	}

}
