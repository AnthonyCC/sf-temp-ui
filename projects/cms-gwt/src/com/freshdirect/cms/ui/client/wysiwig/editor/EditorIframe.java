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


import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;

class EditorIframe extends Component {

    private EditorControler controler;
    private String htmlBuffer;

    protected EditorIframe(EditorControler controler) {
        this.controler = controler;
    }


    @Override
    protected void onRender(Element target, int index) {
        setElement(DOM.createIFrame(), target, index);
        if (GXT.isIE && GXT.isSecure) {
            getElement().setPropertyString("src", GXT.SSL_SECURE_URL);
        }
        el().insertInto(target, index);
    }


    protected void afterRender(){
        initFrame();
        enableDesignMode();
        /**
         * @TODO: Why does this not work without the timer ?
         */
        if(htmlBuffer != null){
            new Timer(){

                public void run() {
                    _setHtml(htmlBuffer);
                    htmlBuffer = null;
                }
            }.schedule(50);
        }
    }

    protected boolean enableDesignMode(){
        if(rendered) {
            controler.enableDesignMode();
            return true;
        }
        return false;
    }


    protected String getHTML(){
        if(rendered && isAttached()){
            return _getHTML();            
        } else {
            return htmlBuffer;
        }
    }

    protected void setHTML(String html){
        if(rendered && isAttached()){
            _setHtml(html);
        } else {
            htmlBuffer = html;
        }
    }
    
    /**
     * Native method to get html directly from the iframe
     *
     * @return
     */
    private native String _getHTML() /*-{
        var oIframe = this.@com.google.gwt.user.client.ui.UIObject::element;
        var oDoc = oIframe.contentWindow || oIframe.contentDocument;
        if (!oDoc) {
          $wnd.alert("bug getHTML");
        }
        if (oDoc.document) {
          oDoc = oDoc.document;
        }
        return oDoc.body.innerHTML;
    }-*/;



    /**
     * Writes html to the iframe
     *
     * @param _html
     */
    private native void _setHtml(String _html) /*-{
        var oIframe = this.@com.google.gwt.user.client.ui.UIObject::element;
        var oDoc = oIframe.contentWindow || oIframe.contentDocument;
        if (!oDoc) {
          $wnd.alert("bug writeHtmlToFrame");
        }
        if (oDoc.document) {
          oDoc = oDoc.document;
        }
        oDoc.body.innerHTML = "";
        oDoc.write(_html);
        oDoc.close();
    }-*/;

    /**
     * inits the frame and write default html to it
     */
    private native void initFrame() /*-{
        var oIframe = this.@com.google.gwt.user.client.ui.UIObject::element;
        var oDoc = oIframe.contentWindow || oIframe.contentDocument;
        if (oDoc.document) {
          oDoc = oDoc.document;
        }
        oDoc.write("<html><body style='border:none; margin:0px; padding: 5px;'><p>&nbsp;</p></body></html>");
        oDoc.close();
        oIframe.contentWindow.focus();
    }-*/;


}