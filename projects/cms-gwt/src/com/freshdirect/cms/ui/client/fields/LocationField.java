package com.freshdirect.cms.ui.client.fields;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.freshdirect.cms.ui.client.Anchor;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;

public class LocationField extends MultiField<String> {
	
	private TextField<String> innerField;
	private Anchor mapsLink;
	
	private final Geocoder geocoder;
	private final MapWidget map;
	
	private FlowPanel mapContainer;
	
	private Marker marker;
	private boolean isMapVisible;
	
	private static String FD_LOCATION = "40.739364084323874,-73.94764423370361";
	
	public LocationField() {
		super();
		geocoder = new Geocoder();
		isMapVisible = false;
		map = new MapWidget();
		map.addStyleName("google-map");
		map.addControl(new SmallMapControl());
    	map.addControl(new MapTypeControl());    	
    	map.setSize("100%", "400px");
    	map.setZoomLevel(15);
    	map.setCurrentMapType(MapType.getHybridMap());
    	map.addMapClickHandler(new MapClickHandler() {			
			@Override
			public void onClick(MapClickEvent event) {
				LatLng pos = event.getLatLng();
				setValue( String.valueOf(pos.getLatitude()) + "," + String.valueOf(pos.getLongitude()), false);				
			}
		});
    	
		innerField = new TextField<String>();
		innerField.addListener(Events.Change, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				LocationField f = LocationField.this;
				f.fireChangeEvent(f.getOriginalValue(), innerField.getValue());
				setMarker(stringToPos(innerField.getValue()), true);
			}			
		});
		mapsLink = new Anchor("Google Maps");
		mapsLink.setStyleAttribute("margin-left", "10px");
		mapsLink.addListener(Events.OnClick, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				toggleMap();
			}			
		});
		
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		
		hp.add(innerField);
		hp.add(mapsLink);
		
		vp.add(hp);
	
		mapContainer = new FlowPanel();	
		vp.add(mapContainer);
			
		this.add(new AdapterField(vp));
	}	
	
    @Override
    public void disable() {
		super.disable();		
    }
    
    @Override
    public void enable() {
    	super.enable();    
    }
    
    @Override
    public void setReadOnly( boolean readOnly ) {
    	innerField.setReadOnly(readOnly);
    }
        
    @Override
    public String getValue() {        
    	return innerField.getValue();        
    }
    
    /**
     * Sets value of the field and adjusts its internal state.
     * Non null value means explicit / overridden state.
     */
    @Override
    public void setValue(String value) {    	    	
    	 setValue(value, true);   	
    }
    
    public void setValue(String value, boolean center) {
    	innerField.setValue(value);    	
    	innerField.fireEvent(Events.Change);
    	if (isMapVisible) {
    		setMarker(stringToPos(getValue()), center);
    	}
    }
            
    @Override
    protected void onRender(Element target, int index) {
    	setOriginalValue(getValue());
    	super.onRender(target, index);
    }
    
    @Override
    public void setWidth(int width) {
    	// The length of the Google Maps link is 90 pixel
    	innerField.setWidth(width - 90);
    }
    
    @Override
    public void reset() {    
    	innerField.reset();
    	if (isMapVisible) {
    		setMarker(stringToPos(getValue()));
    	}
    }
    
    public LatLng stringToPos(String value) {
    	String[] a = value.split(",");
    	return LatLng.newInstance(Double.valueOf(a[0]), Double.valueOf(a[1]));
    }

    public void toggleMap() {
    	if (mapContainer.getWidgetIndex(map) != -1) {
    		map.removeOverlay(marker);
    		mapContainer.clear();    		
    		isMapVisible = false;
    		return;
    	}    	
    	
    	HorizontalPanel hp = new HorizontalPanel();
    	hp.addStyleName("google-maps-panel");
    	hp.getElement().getStyle().setMarginTop(5, Unit.PX);    	
    	final TextField<String> search = new TextField<String>();
    	search.setEmptyText("Enter address");
    	search.setWidth(innerField.getWidth());
    	search.addKeyListener(new KeyListener() {
    		@Override
    		public void componentKeyPress(ComponentEvent event) {    		
    			if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
					showAddress(search.getValue());
				}
    		}
    	});
    	
    	Button b = new Button("Search");    	
    	b.addSelectionListener(new SelectionListener<ButtonEvent>() {			
			@Override
			public void componentSelected(ButtonEvent ce) {
				showAddress(search.getValue());
			}
		});
    	
    	hp.add(search);
    	hp.add(b);    	
    	mapContainer.add(hp);
    	mapContainer.add(map);
    	
    	LatLng pos = stringToPos(FD_LOCATION);
    	try {
    		String[] a = getValue().split(",");
    		pos = LatLng.newInstance(Double.valueOf(a[0]), Double.valueOf(a[1]));
    	} catch (Exception e) {    		
    	}
    	
    	marker = new Marker(pos);    	
    	map.addOverlay(marker);
    	map.setCenter(pos);
    	isMapVisible = true;
    }        
        
    
    public void setMarker(LatLng pos) {
    	setMarker(pos, true);    	    	
    }
    
    public void setMarker(LatLng pos, boolean center) {
    	if (isMapVisible) {
	    	map.removeOverlay(marker);
	    	marker = new Marker(pos);
	    	map.addOverlay(marker);    	    	
	    	if (center) {
	    		map.setCenter(pos);
	    	}
    	}
    }
    
	private void showAddress(final String address) {
		final InfoWindow info = map.getInfoWindow();
		geocoder.getLocations(address, new LocationCallback() {
			public void onFailure(int statusCode) {
				Window.alert("Sorry, we were unable to geocode that address");
			}

			public void onSuccess(JsArray<Placemark> locations) {
				Placemark place = locations.get(0);
				LatLng pos = place.getPoint();
				map.removeOverlay(marker);
				marker = new Marker(pos);
				map.addOverlay(marker);
				String message = place.getAddress() + "<br>"
						+ "<b>Country code:</b> " + place.getCountry();
				info.open(marker, new InfoWindowContent(message));				
			}
		});
	}

}
