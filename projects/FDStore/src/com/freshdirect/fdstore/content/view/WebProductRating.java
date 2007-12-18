/**
 * 
 * WebProductRating.java
 * Created Dec 9, 2002
 */
package com.freshdirect.fdstore.content.view;

/**
 *
 *  @author knadeem
 */
import java.util.List;

public class WebProductRating {
	
	private String ratingLabel;
	private List ratings;
        private List textRatings;
	private String compareByLabel;
	private String linkParams;
	
	public WebProductRating(String ratingLabel, List ratings, List textRatings, String compareByLabel, String linkParams){
                this.ratingLabel = ratingLabel;
		this.ratings = ratings;
                this.textRatings = textRatings;
		this.compareByLabel = compareByLabel;
		this.linkParams = linkParams;
        }	
	public String getRatingLabel(){
		return this.ratingLabel;
	}
	
	public void setRatingLabel(String ratingLabel){
		this.ratingLabel = ratingLabel;
	}
	
	public List getRatings(){
		return this.ratings;
	}
	
	public void setRatings(List ratings){
		this.ratings = ratings;
	}
	
        public List getTextRatings() {
            return this.textRatings;
        }
        
        public void setTextRatings(List textRatings) {
            this.textRatings=textRatings;
        }
        
	public String getCompareByLabel(){
		return this.compareByLabel;
	}
	
	public void setCompareByLabel(String compareByLabel){
		this.compareByLabel = compareByLabel;
	}
	
	public String getLinkParams(){
		return this.linkParams;
	}
	
	public void setLinkParams(String linkParams){
		this.linkParams = linkParams;
	}
}
