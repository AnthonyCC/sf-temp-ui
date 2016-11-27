package com.freshdirect.cms.ui.client.wysiwig.editor.browserdep;

import com.google.gwt.user.client.Element;

class EditorUtilsImplSafari extends EditorUtilsImplCommon {

    public native void saveSelection(Element oIframe) /*-{
        var oDoc = oIframe.contentWindow || oIframe.contentDocument;
        if (oDoc.document) {
          oDoc = oDoc.document;
        }

        var oWin = oIframe.contentWindow;
        var sel = oWin.getSelection();

        oDoc._previous_range = sel;
        // oDoc._previous_range.baseNode = sel.baseNode;
        // oDoc._previous_range.baseOffset = sel.baseOffset;
        // oDoc._previous_range.extentNode = sel.extentNode;
        // oDoc._previous_range.extentOffset = sel.extentOffset;
    }-*/;

    public native void restoreSelection(Element oIframe) /*-{
        var oDoc = oIframe.contentWindow || oIframe.contentDocument;
        if (oDoc.document) {
          oDoc = oDoc.document;
        }

        var oWin = oIframe.contentWindow;
        var sel = oWin.getSelection();
        sel.setBaseAndExtent(oDoc._previous_range.baseNode,
        oDoc._previous_range.baseOffset,
        oDoc._previous_range.extentNode,
        oDoc._previous_range.extentOffset);
    }-*/;
}
