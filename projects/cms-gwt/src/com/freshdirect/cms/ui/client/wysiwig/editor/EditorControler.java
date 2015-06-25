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

package com.freshdirect.cms.ui.client.wysiwig.editor;


import com.freshdirect.cms.ui.client.wysiwig.editor.browserdep.EditorUtilsImpl;
import com.freshdirect.cms.ui.client.wysiwig.editor.browserdep.EditorUtilsImplCommon;
import com.google.gwt.core.client.GWT;

/**
 * The controller should minimise the coupling between the @see EditorUtils and the eventhandling and the Toolbarbuttons.
 * Furthermore this controller should hide the logic to change markup.
 *
 * @author: Pavel Jbanov
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 31.10.2008
 * Time: 16:20:11
 */

public class EditorControler {

    private RteEditor rteEditor;

    private static EditorUtilsImpl impl;

    static {
        impl = (EditorUtilsImplCommon) GWT.create(EditorUtilsImplCommon.class);
    }

    public EditorControler(RteEditor rteEditor) {
        this.rteEditor = rteEditor;
    }

    public void doRemoveFormat() {
        impl.doRemoveFormat(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doUndo() {
        impl.doUndo(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doRedo() {
        impl.doRedo(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doBold() {
        impl.doBold(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doItalic() {
        impl.doItalic(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doUnderline() {
        impl.doUnderline(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doSubscript() {
        impl.doSubscript(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doSuperscript() {
        impl.doSuperscript(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doJustifyLeft() {
        impl.doJustifyLeft(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doJustifyCenter() {
        impl.doJustifyCenter(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doJustifyRight() {
        impl.doJustifyRight(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doJustifyFull() {
        impl.doJustifyFull(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doInsertOrderedList() {
        impl.doInsertOrderedList(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doInsertUnorderedList() {
        impl.doInsertUnorderedList(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doUnLink() {
        impl.doUnLink(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    public void doCreateLink(String url) {
        impl.doCreateLink(rteEditor.getEditorWYSIWYG().getFrameElement(), url);
    }

    public void doInsertImage(String url) {
        impl.doInsertImage(rteEditor.getEditorWYSIWYG().getFrameElement(), url);
    }

    public void doForeColor(String color) {
        impl.doForeColor(rteEditor.getEditorWYSIWYG().getFrameElement(), color);
    }

    public void doBackgroundColor(String color) {
        impl.doBackgroundColor(rteEditor.getEditorWYSIWYG().getFrameElement(), color);
    }

    public void doFontStyle(String style) {
        impl.doFontStyle(rteEditor.getEditorWYSIWYG().getFrameElement(), style);
    }

    public void doFontSize(String size) {
        impl.doFontSize(rteEditor.getEditorWYSIWYG().getFrameElement(), size);
    }

    public void enableDesignMode() {
        impl.enableDesignMode(rteEditor.getEditorWYSIWYG().getFrameElement());

    }


    /**
     * Save seection in the editors IFrame
     */
    public void saveSelection() {
        impl.saveSelection(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    /**
     * Restore selection in the editors IFrame.
     */
    public void restoreSelection() {
        impl.restoreSelection(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    /**
     * Puts the focus on the editors iframe
     */
    public void doFocus() {
        impl.doFocus(rteEditor.getEditorWYSIWYG().getFrameElement());
    }

    /**
     * Changes the mode of the editor from source code mode to design mode.
     */
    public void switchToDesignMode() {
        rteEditor.switchToDesignMode();
    }


    /**
     * Changes the mode of the editor from desgin mode / wysiwig mode to source code mode.
     */
    public void switchToSourceMode() {
        rteEditor.switchToSourceMode();
    }

    public String[][] getSupportedFormats() {
        return impl.getSupportedFormats();
    }
}
