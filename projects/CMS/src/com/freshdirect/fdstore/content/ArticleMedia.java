/*
 * Class.java
 *
 * Created on December 7, 2002, 5:48 PM
 */

package com.freshdirect.fdstore.content;
import java.util.Calendar;
 
/**
 *
 * @author  rgayle
 * @version 
 */
public class ArticleMedia extends TitledMedia {

    private String articleBlurb;
    private String articleSource;
    private Calendar articleDate;

    public ArticleMedia(MediaModel media, String blurb, String title, String source, Calendar articleDate) throws IllegalArgumentException{
        super(media,title,null,MediaI.TYPE_ARTICLEMEDIA);
        this.articleBlurb = blurb;
        this.articleDate = articleDate;
        this.articleSource = source;
    }
	public void setBlurb(String blurb) {this.articleBlurb = blurb; }
	public void setSource(String source)  { this.articleSource = source; }
	public void setDate(Calendar date) { this.articleDate = date; }

    public String getBlurb()    { return this.articleBlurb; }
    public String getSource()   { return this.articleSource; }
    public Calendar getDate()   { return this.articleDate; }
    
    public String toString() {
        String stDate=null;
        if (articleDate!=null) {
            stDate = articleDate.get(Calendar.MONTH)+"/"+articleDate.get(Calendar.DATE)+"/"+articleDate.get(Calendar.YEAR);
        }
        return "ArticleMedia: ["+this.getMediaTitle()+", "+this.articleSource+", "+this.articleBlurb+", "+stDate+", "+this.getMedia()+" ]";
    }
    
}
