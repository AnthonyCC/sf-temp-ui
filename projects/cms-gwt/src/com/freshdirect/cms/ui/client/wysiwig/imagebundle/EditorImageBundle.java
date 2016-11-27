package com.freshdirect.cms.ui.client.wysiwig.imagebundle;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;

public interface EditorImageBundle  extends ClientBundle {

    /**
     * @return button image, switch to html source code
     */
    @Source("html.png")
    ImageResourcePrototype html();

    /**
     * @return button image, switch from html source to wysiwyg mode
     */
    @Source("page_white_text.png")
    ImageResourcePrototype page_white_text();

    /**
     * @return button image for the remove formatting button
     */
    @Source("page_white_delete.png")
    ImageResourcePrototype page_white_delete();

    /**
     * @return button image, for text bold button
     */
    @Source("text_bold.png")
    ImageResourcePrototype text_bold();


    /**
     * @return button image for the text italic button
     */
    @Source("text_italic.png")
    ImageResourcePrototype text_italic();


    /**
     * @return button image for the text underline button
     */
    @Source("text_underline.png")
    ImageResourcePrototype text_underline();


    /**
     * @return button image for the text subscript button
     */
    @Source("text_subscript.png")
    ImageResourcePrototype text_subscript();


    /**
     * @return button image for the text superscript button
     */
    @Source("text_superscript.png")
    ImageResourcePrototype text_superscript();


    /**
     * @return button imag for the text align left button
     */
    @Source("text_align_left.png")
    ImageResourcePrototype text_align_left();


    /**
     * @return button image for the text align right button
     */
    @Source("text_align_right.png")
    ImageResourcePrototype text_align_right();


    /**
     * @return button image for the text align center button
     */
    @Source("text_align_center.png")
    ImageResourcePrototype text_align_center();


    /**
     * @return button image for the button text justify button
     */
    @Source("text_align_justify.png")
    ImageResourcePrototype text_align_justify();


    /**
     * @return button image for the button list bullets button
     */
    @Source("text_list_bullets.png")
    ImageResourcePrototype text_list_bullets ();


    /**
     * @return button image for the button list numbers
     */
    @Source("text_list_numbers.png")
    ImageResourcePrototype text_list_numbers();


    /**
     * @return button image for the add link button
     */
    @Source("link.png")
    ImageResourcePrototype link();


    /**
     * @return button image for the remove link button
     */
    @Source("link_break.png")
    ImageResourcePrototype link_break();


    /**
     * @TODO: implement the link edit button 
     * @return button image for the edit link button.
     */
    @Source("link_edit.png")
    ImageResourcePrototype link_edit();


    /**
     * @return button image for all accept buttons
     */
    @Source("accept.png")
    ImageResourcePrototype accept();

    /**
     * @return button image for all cancel buttons
     */
    @Source("cancel.png")
    ImageResourcePrototype cancel();


    /**
     * @return h1 image for the select text style list
     */
    @Source("text_heading_1.png")
    ImageResourcePrototype text_heading_1();

    /**
     * @return h1 image for the select text style list
     */
    @Source("text_heading_2.png")
    ImageResourcePrototype text_heading_2();

    /**
     * @return h2 image for the select text style list
     */                             
    @Source("text_heading_3.png")
    ImageResourcePrototype text_heading_3();

    /**
     * @return h3 image for the select text style list
     */
    @Source("text_heading_4.png")
    ImageResourcePrototype text_heading_4();

    /**
     * @return h4 image for the select text style list
     */
    @Source("text_heading_5.png")
    ImageResourcePrototype text_heading_5();

    /**
     * @return h5 image for the select text style list
     */
    @Source("text_heading_6.png")
    ImageResourcePrototype text_heading_6();


    /**
     * @return button image to select background color
     */
    @Source("palette.png")
    ImageResourcePrototype palette();


    /**
     * @return button image to select foreground image 
     */
    @Source("script_palette.png")
    ImageResourcePrototype script_palette();

    /**
     * @return button image to redo
     */
    @Source("arrow_redo.png")
    ImageResourcePrototype arrow_redo();

    /**
     * @return button image to undo
     */
    @Source("arrow_undo.png")
    ImageResourcePrototype arrow_undo();

    /**
     * @return button image to insert an im
     */
    @Source("image_add.png")
    ImageResourcePrototype image_add();
}