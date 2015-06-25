/*
 * Copyright 2006 Pavel Jbanov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.freshdirect.cms.ui.client.wysiwig.widget.toolbar;

import com.extjs.gxt.ui.client.widget.Component;
import com.freshdirect.cms.ui.client.wysiwig.editor.EditorControler;
import com.freshdirect.cms.ui.client.wysiwig.imagebundle.EditorImageBundleAccess;
import com.freshdirect.cms.ui.client.wysiwig.toolbar.item.*;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorCommand;


public class EditorToolbarWidgetsFactory {

    private EditorControler controler;


    public EditorToolbarWidgetsFactory(EditorControler controler) {
        this.controler = controler;
    }

    public Component getRemoveFormattingWidget() {
        return new ImageBundleCommandButton("Remove Formatting",EditorImageBundleAccess.getEditorImageBundle().page_white_delete(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doRemoveFormat();
            }
        });
    }

    public Component getUndoWidget() {
        return new ImageBundleCommandButton("Undo",EditorImageBundleAccess.getEditorImageBundle().arrow_undo(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doUndo();
            }
        });
    }

    public Component getRedoWidget() {
        return new ImageBundleCommandButton("Redo",EditorImageBundleAccess.getEditorImageBundle().arrow_redo(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doRedo();
            }
        });
    }

    public Component getBoldWidget() {
        return new ImageBundleCommandButton("Bold",EditorImageBundleAccess.getEditorImageBundle().text_bold(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doBold();
            }
        });
    }

    public Component getItalicWidget() {
        return new ImageBundleCommandButton("Italic",EditorImageBundleAccess.getEditorImageBundle().text_italic(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doItalic();
            }
        });
    }

    public Component getUnderlineWidget() {
        return new ImageBundleCommandButton("Underline",EditorImageBundleAccess.getEditorImageBundle().text_underline(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doUnderline();
            }
        });
    }

    public Component getSubscriptWidget() {
        return new ImageBundleCommandButton("Subscript",EditorImageBundleAccess.getEditorImageBundle().text_subscript(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doSubscript();
            }
        });
    }

    public Component getSuperscriptWidget() {
        return new ImageBundleCommandButton("Superscript",EditorImageBundleAccess.getEditorImageBundle().text_superscript(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doSuperscript();
            }
        });
    }

    public Component getJustifyLeftWidget() {
        return new ImageBundleCommandButton("Left",EditorImageBundleAccess.getEditorImageBundle().text_align_left(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doJustifyLeft();
            }
        });
    }

    public Component getJustifyCenterWidget() {
        return new ImageBundleCommandButton("Center",EditorImageBundleAccess.getEditorImageBundle().text_align_center(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doJustifyCenter();
            }
        });
    }

    public Component getJustifyRightWidget() {
        return new ImageBundleCommandButton("Right",EditorImageBundleAccess.getEditorImageBundle().text_align_right(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doJustifyRight();
            }
        });
    }

    public Component getJustifyFullWidget() {
        return new ImageBundleCommandButton("Justify",EditorImageBundleAccess.getEditorImageBundle().text_align_justify(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doJustifyFull();
            }
        });
    }

    public Component getOrderedListWidget() {
        return new ImageBundleCommandButton("OList",EditorImageBundleAccess.getEditorImageBundle().text_list_numbers(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doInsertOrderedList();
            }
        });
    }

    public Component getUnorderedListWidget() {
        return new ImageBundleCommandButton("UList",EditorImageBundleAccess.getEditorImageBundle().text_list_bullets(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doInsertUnorderedList();
            }
        });
    }

    public Component getUnlinkWidget() {
        return new ImageBundleCommandButton("Line Break",EditorImageBundleAccess.getEditorImageBundle().link_break(), new EditorCommand() {
            public void exec(String[] params) {
                controler.doUnLink();
            }
        });
    }

    public Component getLinkWidget() {
        return new LinkButton(controler);
    }
    
    public Component getInsertImageWidget() {
        return new InsertImageButton(controler);
    }

    public Component getForegroundColorWidget() {
        return new ForegroundColorButton(controler);
    }

    public Component getBackgroundColorWidget() {
        return new BackgroundColorButton(controler);
    }

    public Component getFontStyleWidget() {
        return new FontStyleButton(controler);
    }

    public Component getFontSizeWidget() {
        return new FontSizesCombo(controler);
    }

    public Component getShowSourceWidget() {
        return new ImageBundleCommandButton("Source",EditorImageBundleAccess.getEditorImageBundle().html(), new EditorCommand() {
            public void exec(String[] params) {
                controler.switchToSourceMode();
            }
        });
    }

    public Component getBackToRichTextWidget() {
        return new ImageBundleCommandButton("Rich Text",EditorImageBundleAccess.getEditorImageBundle().page_white_text(), new EditorCommand() {
            public void exec(String[] params) {
                controler.switchToDesignMode();
            }
        });
    }
}
