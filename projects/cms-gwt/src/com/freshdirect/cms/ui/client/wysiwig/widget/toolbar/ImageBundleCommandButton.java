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


import com.freshdirect.cms.ui.client.wysiwig.widget.ImageBundleButton;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorCommand;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 * 
 *
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 31.10.2008
 * Time: 17:25:30
 */
public class ImageBundleCommandButton extends ImageBundleButton {

    
    public ImageBundleCommandButton(ImageResourcePrototype imagePrototype, final EditorCommand command) {
        this(null,imagePrototype,command);
    }
    
    public ImageBundleCommandButton(String buttonName, ImageResourcePrototype imagePrototype, final EditorCommand command) {
        super(buttonName,imagePrototype);
        this.addListener(Events.Select, new Listener<BaseEvent>(){
            public void handleEvent(BaseEvent event) {
                command.exec(null);
            }
        });
    }
}
