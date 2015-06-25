package com.freshdirect.cms.ui.client.wysiwig.toolbar.item;

import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorCommand;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.SimpleComandComboBox;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.SimpleComboBoxModel;

import java.util.ArrayList;

public class FontSizesCombo extends SimpleComandComboBox {

    EditorControler controler;

    public FontSizesCombo(final EditorControler c) {
        ArrayList<SimpleComboBoxModel> list = new ArrayList<SimpleComboBoxModel>();
        for (int i = 1; i < 8; i++) {
            list.add (new SimpleComboBoxModel(""+i, ""+i));
        }
        setModel(list);
        setEditorCommand( new EditorCommand(){
            public void exec(String[] params) {
                c.doFontSize(params[0]);
                c.doFocus();

            }
        });
        this.setWidth(100);
    }
}