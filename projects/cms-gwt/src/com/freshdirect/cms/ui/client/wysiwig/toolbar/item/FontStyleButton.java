package com.freshdirect.cms.ui.client.wysiwig.toolbar.item;


import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorCommand;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.SimpleComandComboBox;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.SimpleComboBoxModel;

import java.util.ArrayList;

public class FontStyleButton extends SimpleComandComboBox {


    EditorControler controler;


    public FontStyleButton(EditorControler c) {
        this.controler = c;
        String[][] formats = controler.getSupportedFormats();
        ArrayList<SimpleComboBoxModel> list = new ArrayList<SimpleComboBoxModel>();
        setWidth(100);
        for (int i = 0; i < formats.length; i++) {
          list.add (new SimpleComboBoxModel(formats[i][1], formats[i][0]));
        }
        setModel(list);
        setEditorCommand( new EditorCommand(){
            public void exec(String[] params) {
                controler.doFocus();
                controler.doFontStyle(params[0]);
            }
        });
    }
}
