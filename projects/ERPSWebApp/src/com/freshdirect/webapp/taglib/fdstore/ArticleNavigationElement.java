/*
 * Class.java
 *
 * Created on December 10, 2002, 6:24 PM
 */

package com.freshdirect.webapp.taglib.fdstore;
import com.freshdirect.fdstore.content.*;
 /*
 *
 * @author  rgayle
 * @version 
 * Navigation element that is not a content node, but is owned by a content node.
 */

public class ArticleNavigationElement extends NavigationElement{
    
	private final String url;
        private final String displayText;
        private String articleSortString;
	public ArticleNavigationElement(int depth, ArticleMedia article, int articleIndex, ContentNodeModel owner) {
		super(depth, owner,true);
                String articleDate=null;
                //String sortDate = null;
		this.url = "/about/press/article.jsp?catId="+owner+"&articleIndex=" +articleIndex+ "&trk=snav";
                if (article.getDate()!=null) {
                    articleDate=dateFmtDisplay.format(article.getDate().getTime());
                    //sortDate=dateFmtSort.format(article.getDate().getTime());
                } else {
                    articleDate = "";
                    //sortDate = "";
                }
                this.displayText = article.getSource()+"<br>"+articleDate;
                this.articleSortString=PAD_ZEROS.format((long)articleIndex);
	}
        
	public String getDisplayString() {
		return displayText;
	}
        
        public String getArticleSortString() { return this.articleSortString; }
        public boolean isArticle() {return true;}
       
	public boolean isAvailable() {
		return true;
	}

	public boolean isProduct() {
		return false;
	}

	public int getPriority() {
		return 0;
	}

	public String getURL() {
		return this.url;
	}

	public boolean showLink() {
		return true;
	}

	public boolean isBold() {
		return false;
	}

	public boolean breakBefore() {
		return false;
	}

	public boolean breakAfter() {
		return true;
	}
}
