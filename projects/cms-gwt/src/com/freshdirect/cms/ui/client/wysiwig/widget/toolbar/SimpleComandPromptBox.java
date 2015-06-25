/**
 * Copyright 2008 Peter Quiel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.freshdirect.cms.ui.client.wysiwig.widget.toolbar;


import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.event.Events;
import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.i18n.EditorMessagesAccess;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorCommand;


/**
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 18.08.2008
 * Time: 12:29:21
 */
public class SimpleComandPromptBox {

    private MessageBox box;

    private EditorControler controler;

    public SimpleComandPromptBox(EditorControler c,final EditorCommand command, String title, String fieldLabel, String buttonLabel) {
        this.controler = c;
        controler.saveSelection();
        box = MessageBox.prompt(title, fieldLabel);
        box.setModal(true);
        Listener<ComponentEvent> listener = new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent ce) {
                controler.restoreSelection();
                String value = box.getTextBox().getValue().toString();
                command.exec(new String[]{value});
            }
        };
        box.getDialog().okText = buttonLabel;
        box.getDialog().cancelText = EditorMessagesAccess.getEditorMessages().cancel();
        box.getDialog().getButtonBar().getItemByItemId("ok").addListener(Events.Select, listener);
    }

    public void show() {
        box.getDialog().layout();
    }
}
