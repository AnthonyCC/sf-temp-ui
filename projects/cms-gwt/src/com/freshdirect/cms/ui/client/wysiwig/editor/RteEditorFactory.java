package com.freshdirect.cms.ui.client.wysiwig.editor;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.freshdirect.cms.ui.client.wysiwig.editor.RteEditor;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorToolBar;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorToolbarWidgetsFactory;
import com.freshdirect.cms.ui.client.wysiwig.widget.toolbar.EditorToolBar.WidgetPostion;

public class RteEditorFactory {

    private static RteEditorFactory instance;

    public static RteEditorFactory instance(){
        if(instance == null){
            instance = new RteEditorFactory();
        }
        return instance;
    }

    private RteEditorFactory(){}

    
    public RteEditor createDefault(){
        RteEditor rteEditor = new RteEditor();
        rteEditor.setEditorToolBar(createDefaultToolBar(rteEditor));
        rteEditor.setHeaderVisible(false);
        rteEditor.setLayout(new FitLayout());
        return rteEditor;
    }


    public EditorToolBar createDefaultToolBar(RteEditor rteEditor) {

        EditorToolbarWidgetsFactory toolbarWidgetsFactory = new EditorToolbarWidgetsFactory(rteEditor.getController());
        EditorToolBar editorToolBar = new EditorToolBar();

        editorToolBar.addWidget(toolbarWidgetsFactory.getShowSourceWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addSeparator(WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getRemoveFormattingWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addSeparator( EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getUndoWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getRedoWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addSeparator( EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getBoldWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getItalicWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getUnderlineWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addSeparator( EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getSubscriptWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getSuperscriptWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addSeparator( EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getJustifyLeftWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getJustifyCenterWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getJustifyRightWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getJustifyFullWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addSeparator( EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getOrderedListWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getUnorderedListWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addSeparator( EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getLinkWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getUnlinkWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getInsertImageWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addSeparator( EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getForegroundColorWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getBackgroundColorWidget(), EditorToolBar.WidgetPostion.DesignModeFirstRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getFontStyleWidget(), EditorToolBar.WidgetPostion.DesignModeSecondRow);
        editorToolBar.addWidget(toolbarWidgetsFactory.getFontSizeWidget(),  EditorToolBar.WidgetPostion.DesignModeSecondRow);

        editorToolBar.addWidget(toolbarWidgetsFactory.getBackToRichTextWidget(),  EditorToolBar.WidgetPostion.SourceModeFirstRow);
        return editorToolBar;
    }
}
