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

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * @TODO: Documentation of org.iverein.gxt.rte.client.widget.toolbar
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 03.11.2008
 * Time: 13:38:11
 */
public class SimpleComboBoxModel<T> extends BaseModelData {

    public SimpleComboBoxModel(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public void setKey(String key){
        set("key", key);
    }

    public String getKey(){
        return get("key");
    }

    public void setValue(String value){
        set("value", value);
    }

    public String getValue(){
        return get("value");
    }
}
