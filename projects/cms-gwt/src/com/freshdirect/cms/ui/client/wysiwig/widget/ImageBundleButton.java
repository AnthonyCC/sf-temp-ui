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

package com.freshdirect.cms.ui.client.wysiwig.widget;


import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.util.TextMetrics;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.Element;

/**
 * The default GXT button supports only images styled via css. This button supports image bundle images instead of css styled images.
 *
 * @author: Peter Quiel <peter.quiel@iverein-networks.de>
 * Date: 31.10.2008
 * Time: 15:14:57
 */
public class ImageBundleButton extends Button {

    /**
     * Default template for this object
     */
    static Template imageBundleTemplate;


    /**
     * Image to render instead of the css style
     */
    private ImageResourcePrototype imagePrototype;

    /**
     * used for image width calculation
     */
    private int additionalImageWidth = 0;

    /**
     * Default constructor
     */
    public ImageBundleButton() {
    }

    /**
     * Contstructor to set the image bundle image to render
     * @param imagePrototype
     */
    public ImageBundleButton(ImageResourcePrototype imagePrototype) {
        super(AbstractImagePrototype.create(imagePrototype).getHTML());
    	this.imagePrototype = imagePrototype;
    }


    /**
     *
     * @param text Text of the button
     * @param imagePrototype
     */
    public ImageBundleButton(String text, ImageResourcePrototype imagePrototype) {
        super(AbstractImagePrototype.create(imagePrototype).getHTML());
        this.imagePrototype = imagePrototype;
    }

    /**
     * Set image bundle image
     * @param imagePrototype
     */
    public void setImagePrototype(ImageResourcePrototype imagePrototype) {
        this.imagePrototype = imagePrototype;
    }

    /**
     * The width of the button is calucalted by the @see ImageBundleButton#autoWidth method. You can increase the width of the button using this method.
     * @param additionalImageWidth additional width for the button
     */
    public void setAdditionalImageWidth(int additionalImageWidth) {
        this.additionalImageWidth = additionalImageWidth;
    }

    /**
     * Autowidth method to calculate the widt of this button
     *
     * This method respects the width of the image bundle button.
     * The widht can be increased with the @see ImageBundleButton#setAdditionalImageWidth
     *
     */
    protected void autoWidth() {
        if (rendered) {
            el().setWidth("auto");
            if (GXT.isIE) {
                if (buttonEl != null && buttonEl.getWidth() > 20) {
                    buttonEl.clip();
                    TextMetrics.get().bind(buttonEl.dom);
                    int adj =  8;
                    
                    
                    adj += (imagePrototype != null) ?  imagePrototype.getWidth() + additionalImageWidth + 6 : 0;
                    int w = TextMetrics.get().getWidth(text) + buttonEl.getFrameWidth("lr") + adj;
                    buttonEl.setWidth(w);
                }
            }
            if (getMinWidth() != Style.DEFAULT) {
                if (el().getWidth() < getMinWidth()) {
                    el().setWidth(getMinWidth());
                }
            }
        }
    }

    /**
     *
     * @param elem
     * @param parent
     * @param index
     */
    protected void setElement(Element elem, Element parent, int index) {
        super.setElement(template.create(text != null && !text.isEmpty() ? text : "&nbsp;", getType(), baseStyle, imagePrototype != null ? AbstractImagePrototype.create(imagePrototype).getHTML() : ""), parent, index);
    }

    /**
     * Inits the template if not set and calls super.onRender
     * @param target
     * @param index
     */
    protected void onRender(Element target, int index) {
        if (template == null) {
            if (imageBundleTemplate == null) {
                imageBundleTemplate = new Template(getTemplateString());
            }
            template = imageBundleTemplate;
        }
        super.onRender(target, index);
    }


    /**
     * Creates the default template for this object
     * @return default template string
     */
    private static String getTemplateString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=0 cellpadding=0 cellspacing=0 class='{2}-wrap {2}-bndl'><tbody><tr>");
        sb.append("<td class={2}-left><i>&#160;</i></td><td class='{2}-center'><em unselectable=on><button class={2}-text type={1}><span class={2}-image>{3}</span><span class={2}-text-span>{0}</span></button></em></td><td class={2}-right><i>&#160;</i></td>");
        sb.append("</tr></tbody></table>");
        return sb.toString();
    }

}
