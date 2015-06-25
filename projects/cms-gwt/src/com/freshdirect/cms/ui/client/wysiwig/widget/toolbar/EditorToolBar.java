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


import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;


/**
 * This is a very basic toolbar and wraps a two gxt toolbars for the design mode and one toolbar for the source mode
 * Futhermore a the row where a button should be added to must be specified
 * 
 * This editor toolbar supports two rows for design mode and one row for source mode.
 *
 *
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 31.10.2008
 * Time: 15:14:57
 */


public class EditorToolBar extends LayoutContainer {

    private ToolBar designModeFirstRow = new ToolBar();
    private ToolBar designModeSecondRow = new ToolBar();
    private ToolBar sourceModeFirstRow = new ToolBar();


    public EditorToolBar(){
        super();
        this.setLayoutOnChange(true);
        this.switchToDesignMode();
    }

  /*  *//**
     * Adds an @see SeparatorToolItem to the specified toolbar.
     * @param widgetPostion
     */
    public void addSeparator(WidgetPostion widgetPostion) {
        this.addToolItem(new SeparatorToolItem(), widgetPostion);
    }

    /**
     * Adds a GXT Component to the specified toolbar 
     * @param widget
     * @param widgetPostion
     */
    public void addWidget(Component widget, WidgetPostion widgetPostion) {
        this.addToolItem(widget, widgetPostion);
    }


    /**
     * Adds an @see ToolItem to the specifed toolbar
     * @param toolItem
     * @param widgetPostion
     */
    public void addToolItem(Component toolItem, WidgetPostion widgetPostion){
        switch(widgetPostion){
            case DesignModeFirstRow:
                this.designModeFirstRow.add(toolItem);
                break;
            case DesignModeSecondRow:
                this.designModeSecondRow.add(toolItem);
                break;
            case SourceModeFirstRow:
                this.sourceModeFirstRow.add(toolItem);
                break;
        }
    }


    /**
     * Shows the source mode toolbar and removes the design mode toolbar
     */
    public void switchToSourceMode() {
        removeAll();
        add(this.sourceModeFirstRow);
    }

    /**
     * Shows the design mode toolbar and removes the source code toolbar
     */
    public void switchToDesignMode() {
        removeAll();
        add(this.designModeFirstRow);
        add(this.designModeSecondRow);
    }


    /**
     * Enum to specify the widget position
     */
    public enum WidgetPostion {
        SourceModeFirstRow,
        DesignModeFirstRow,
        DesignModeSecondRow
    }
}
