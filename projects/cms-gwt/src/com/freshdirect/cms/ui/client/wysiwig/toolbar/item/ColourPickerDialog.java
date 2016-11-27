package com.freshdirect.cms.ui.client.wysiwig.toolbar.item;

import net.auroris.ColorPicker.client.ColorPicker;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.Events;


public class ColourPickerDialog extends Dialog {

    ColorPicker colourPicker;

    Listener<BaseEvent> listener;

    public ColourPickerDialog(){
        super();
        this.colourPicker = new ColorPicker();
        this.setLayout(new FitLayout() );
        this.add(colourPicker);
        this.setButtons(OKCANCEL);
        this.setHideOnButtonClick(true);
        this.setWidth(420);
    }

    /**
     * Returns the selected colour as hex value
     *
     * @return color as hex 000000-FFFFFF
     */
    public String getColour(){
        return this.colourPicker.getHexColor();
    }

    /**
     * @param hex from 000000-FFFFFF
     */
    public void setColor(String hex){
        try {
            this.colourPicker.setHex(hex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addColourSelectedListener(Listener<BaseEvent> listener){
        this.getButtonBar().getItemByItemId("ok").addListener(Events.Select, listener);
    }
}

