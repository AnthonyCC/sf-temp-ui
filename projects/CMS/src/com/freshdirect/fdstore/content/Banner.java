/*
 * PromotionModel.java
 *
 * Created on November 30, 2001, 1:33 PM
 */

package com.freshdirect.fdstore.content;

import java.io.Serializable;

/**
 *
 * @author  mrose
 * @version 
 */
public class Banner implements Serializable {

    /** Holds value of property media. */
    private MediaModel media;
    
    /** Holds value of property subject. */
    private ContentRef subject;
    // the "other" subject property..
    private String contentLink;  // if this property is not null then it will appear in a Popup window
    /** Creates new BannerModel */
    public Banner(MediaModel mm, ContentRef subj) {
        super();
        this.media = mm;
        this.subject = subj;
        this.contentLink = null;
    }
    public Banner(MediaModel mm, String cLink) {
        super();
        this.media = mm;
        this.subject = null;
        this.contentLink = cLink;
    }

    /** Getter for property media.
     * @return Value of property media.
     */
    public MediaModel getMedia() {
        return media;
    }
    
    /** Getter for property subject.
     * @return Value of property subject.
     */
    public ContentRef getSubject() {
        return subject;
    }

    public String getContentLink() {
        return this.contentLink;
    }
    
    public boolean isSubjectALink() {
        return (this.contentLink!=null);
    }
}
