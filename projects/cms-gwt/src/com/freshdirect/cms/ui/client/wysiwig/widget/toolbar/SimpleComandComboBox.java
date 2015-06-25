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

import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;

import java.util.ArrayList;

/**
 * @TODO: Documentation of org.iverein.gxt.rte.client.widget.toolbar.item
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 03.11.2008
 * Time: 13:18:48
 */
abstract public class SimpleComandComboBox extends ComboBox {
    protected void setEditorCommand(final EditorCommand command) {
        this.addSelectionChangedListener( new SelectionChangedListener<SimpleComboBoxModel>(){
            public void selectionChanged(SelectionChangedEvent<SimpleComboBoxModel> se) {
                command.exec(new String[]{se.getSelectedItem().getKey()});
            }
        });
    }

    protected void setModel(ArrayList<SimpleComboBoxModel> list) {
        ListStore<SimpleComboBoxModel> store = new ListStore<SimpleComboBoxModel>();
        store.add(list);
        setStore(store);
        setDisplayField("value");
    }
}