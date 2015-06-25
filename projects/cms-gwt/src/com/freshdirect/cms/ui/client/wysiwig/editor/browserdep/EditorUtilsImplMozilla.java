package com.freshdirect.cms.ui.client.wysiwig.editor.browserdep;


import com.google.gwt.user.client.Element;

class EditorUtilsImplMozilla extends EditorUtilsImplCommon {
  
  public EditorUtilsImplMozilla() {
  }
  
  public native void saveSelection(Element oIframe) /*-{
    var oDoc = oIframe.contentWindow || oIframe.contentDocument;
    if (oDoc.document) {
      oDoc = oDoc.document;
    }
   
    var currange = oIframe.contentWindow.getSelection();
    oDoc._previous_range = currange;
  }-*/;

  public native void restoreSelection(Element oIframe) /*-{
    var oDoc = oIframe.contentWindow || oIframe.contentDocument;
    if (oDoc.document) {
      oDoc = oDoc.document;
    }
  }-*/;
}
