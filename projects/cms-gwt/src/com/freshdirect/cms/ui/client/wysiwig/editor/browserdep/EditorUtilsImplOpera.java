package com.freshdirect.cms.ui.client.wysiwig.editor.browserdep;

import com.google.gwt.user.client.Element;

class EditorUtilsImplOpera extends EditorUtilsImplCommon {
    
  public native void saveSelection(Element oIframe) /*-{
//    var oDoc = oIframe.contentWindow || oIframe.contentDocument;
//    if (oDoc.document) {
//      oDoc = oDoc.document;
//    }
//   
//    var currange = oDoc.selection.createRange();
//    oDoc._previous_range = currange;
  }-*/;

  public native void restoreSelection(Element oIframe) /*-{
//    var oDoc = oIframe.contentWindow || oIframe.contentDocument;
//    if (oDoc.document) {
//      oDoc = oDoc.document;
//    }
//   
//    oDoc._previous_range.select();
//    oDoc.focus();
  }-*/;
}
