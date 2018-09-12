package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponDisplayStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.fdstore.ecoupon.FDCouponProductInfo;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class FDCouponTag extends BodyTagSupport {
	private static final long serialVersionUID = 6807872702704118099L;
	private static Category LOGGER = LoggerFactory.getInstance( FDCouponTag.class );
    private final static NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
    private final static NumberFormat FORMAT_PERCENTAGE = NumberFormat.getPercentInstance(Locale.US);
    private final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");
    FDCustomerCoupon coupon; //required
	
	/* html attributes */
	String namePrefix = "fdCoupon_"; //name will be namePrefix_COUPONID
	String contClass = null; //optional, append this class to the container (for control in different pages)

	/* fd coupon clip check box */
	boolean showClipBox = true; //controls if clip check box is shown
	boolean isClipped = false;   //controls if clip check box is shown checked
	
	/* fd coupon image */
	boolean showCouponImage = true;
	String couponImageUrl = null; //set in tag logic, can be overridden
		/* default images are:
		 * 	badge-purpler.png
		 * 	badge-small.gif
		 * 	logo-purpler.png
		 * 	logo-small.gif
		 * 
		 * badge = "fd"
		 * logo  = "fd coupon"
		 * 
		 */
	
	/* fd coupon msg */
	boolean showMsg = true;
	String couponMsg = null; //set in tag logic, can be overridden
	
	/* fd coupon details */
	boolean showDetailsLink = true;
	String couponDetailsText = null; //comes from FDCustomerCoupon model when null
	
	/* fd coupon status text
	 * this is not directly displayed normally, so it's not in getContent 
	 * */
	boolean showStatusMsg = true;
	String couponStatusText = null; //comes from FDCustomerCoupon model when null
	String couponStatusClass = null;
	String couponStatusPlaceholderId = null; //used with TxSingleProductPricingSupport
	
	boolean pastTense = false; //change "save" tense to "saved"
	private String skuCode;
	private String catId;
		
	public String getCouponStatusPlaceholderId() {
		return couponStatusPlaceholderId;
	}

	public void setCouponStatusPlaceholderId(String couponStatusPlaceholderId) {
		this.couponStatusPlaceholderId = couponStatusPlaceholderId;
	}

	public boolean isPastTense() {
		return pastTense;
	}

	public void setPastTense(boolean pastTense) {
		this.pastTense = pastTense;
	}

	public FDCustomerCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(FDCustomerCoupon coupon) {
		this.coupon = coupon;
	}

	public String getNamePrefix() {
		return namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
	
	public boolean isShowClipBox() {
		return showClipBox;
	}

	public void setShowClipBox(boolean showClipBox) {
		this.showClipBox = showClipBox;
	}

	public boolean isClipped() {
		return isClipped;
	}
	
	public void setIsClipped(boolean isClipped) {
		this.isClipped = isClipped;
	}

	public boolean isShowCouponImage() {
		return showCouponImage;
	}

	public void setShowCouponImage(boolean showCouponImage) {
		this.showCouponImage = showCouponImage;
	}

	public String getCouponImageUrl() {
		if (couponImageUrl == null) {
			setCouponImageUrl("/media/images/ecoupon/logo-small.gif");
		}
		return couponImageUrl;
	}

	public void setCouponImageUrl(String couponImageUrl) {
		this.couponImageUrl = couponImageUrl;
	}

	public boolean isShowMsg() {
		return showMsg;
	}

	public void setShowMsg(boolean showMsg) {
		this.showMsg = showMsg;
	}

	public boolean isShowStatusMsg() {
		return showStatusMsg;
	}

	public void setShowStatusMsg(boolean showStatusMsg) {
		this.showStatusMsg = showStatusMsg;
	}

	public String getContClass() {
		return contClass;
	}

	public void setContClass(String contClass) {
		this.contClass = contClass;
	}

	public String getCouponMsg() {
		return couponMsg;
	}

	public void setCouponMsg(String couponMsg) {
		this.couponMsg = couponMsg;
	}

	public boolean isShowDetailsLink() {
		return showDetailsLink;
	}

	public void setShowDetailsLink(boolean showDetailsLink) {
		this.showDetailsLink = showDetailsLink;
	}

	public String getCouponDetailsText() {
		return couponDetailsText;
	}

	public void setCouponDetailsText(String couponDetailsText) {
		this.couponDetailsText = couponDetailsText;
	}

	public String getCouponStatusText() {
		return couponStatusText;
	}

	public void setCouponStatusText(String couponStatusText) {
		this.couponStatusText = couponStatusText;
	}


    public String getCouponStatusClass() {
		return couponStatusClass;
	}

	public void setCouponStatusClass(String couponStatusClass) {
		this.couponStatusClass = couponStatusClass;
	}
	
	/* util methods */

    public String getName() {
    	if (this.getCoupon() == null) { return ""; }
    	return this.getNamePrefix()+this.getCoupon().getCouponId();
    }

	/* generate a "formatted" short description string
     * do NOT return null here.
     * 
     * this also needs logic for $ versus %
     */
	public String getCouponMsgFormatted() {
    	StringBuilder buf = new StringBuilder();
    	
    	if (coupon != null) {
    		if (this.getCouponMsg() == null) { //to allow override
	    		double couponVal = 0;
	
	    		try {
	    			couponVal = Double.parseDouble(coupon.getValue());
	    		} catch(NumberFormatException nfe) {
	    			LOGGER.debug("Error in FDCouponTag, cannot parseInt on Coupon Value: "+coupon.getValue());
	    		}

				buf.append("Save");
					if (this.isPastTense()) {
						buf.append("d");
					}
				buf.append("&nbsp;");
				
				if(EnumCouponOfferType.PERCENT_OFF.equals(coupon.getOfferType())){
					buf.append(FORMAT_PERCENTAGE.format(couponVal/100));
				}else {
					buf.append(FormatterUtil.formatCurrency(couponVal));
				}
	    		/* single here could be:
	    		 * 	$1.00 off
	    		 * 	$1.00 off w/coupon
	    		 *  */
	    			
	    		int couponQty = 0;
	    		
	    		try {
	    			couponQty = Integer.parseInt(coupon.getQuantity());
	    			
	    			/*
		        		if (couponQty > 1) {
		        			buf.append(" "+coupon.getQuantity());
		        			buf.append(" or more!");
		        		}
		        	*/
	    		} catch(NumberFormatException nfe) {
	    			LOGGER.debug("Error in FDCouponTag, cannot parseInt on Coupon quantity value: "+coupon.getQuantity());
	    		}
	    		
	    		//add "with coupon" when in past tense (like checkout)
	    		if (this.isPastTense()) {
					buf.append("&nbsp;with&nbsp;Coupon");
	    		}
    		} else { //use pre-set msg text
    			buf.append(this.getCouponMsg());
    		}
    		
    	}
    	
		return buf.toString();
	}
	
	/* all-in-one method for setting status text's additional classes */
	public void setStatusClasses() {
		if (this.getCouponStatusClass() == null) { //only sets once, in case it's already been called
			this.setCouponStatusClass("");
			String cStatusText = this.getCouponStatusText();
			if (cStatusText == null) { cStatusText = ""; }
			
			if (coupon != null && coupon.getStatus() != null) {
				if ( coupon.getStatus().equals(EnumCouponStatus.COUPON_MIN_QTY_NOT_MET) || cStatusText.equals(EnumCouponStatus.COUPON_MIN_QTY_NOT_MET.getDescription()) ) {
					this.setCouponStatusClass(this.getCouponStatusClass() + " cStat_minQty");
				}
				if ( coupon.getStatus().equals(EnumCouponStatus.COUPON_APPLIED) || cStatusText.equals(EnumCouponStatus.COUPON_APPLIED.getDescription()) ) {
					this.setCouponStatusClass(this.getCouponStatusClass() + " cStat_applied");
				}
				if ( coupon.getStatus().equals(EnumCouponStatus.COUPON_CLIPPED_EXPIRED) || cStatusText.equals(EnumCouponStatus.COUPON_CLIPPED_EXPIRED.getDescription()) ) {
					this.setCouponStatusClass(this.getCouponStatusClass() + " cStat_clipExp");
				}
				if ( coupon.getStatus().equals(EnumCouponStatus.COUPON_CLIPPED_FILTERED) || cStatusText.equals(EnumCouponStatus.COUPON_CLIPPED_FILTERED.getDescription()) ) {
					this.setCouponStatusClass(this.getCouponStatusClass() + " cStat_filtered");
				}
			}
		}
	}
	
    @Override
    public int doStartTag() {
    	try {
            JspWriter out = pageContext.getOut();
            HttpSession session = pageContext.getSession();
            FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
            out.println(getContent(pageContext));
        } catch (IOException e) {
        }

        return EVAL_BODY_INCLUDE;
    }
    
    /* returns false if there's nothing to do (coupon is null, or no display to be done) */
    public boolean initContent(PageContext pageContext) {
    	//if (pageContext == null) { return true; }
    	//HttpSession session = pageContext.getSession();		
		//FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		/*boolean isCouponsSystemAvailable=false;
		try {
			if(null!=user && user.isCouponsSystemAvailable()){
				isCouponsSystemAvailable = true;
			}
		} catch (FDResourceException e) {}*/
    	if (coupon != null ) {
    		/* setup variables with logic
    		 * this is outside of the individual html calls, so the
    		 * tag can override by using them individually
    		 */
    		/* check for clipped */
    		if (coupon.getStatus() != null) {
    			EnumCouponDisplayStatus cDispStatus = coupon.getDisplayStatus();
    			
    			if (cDispStatus != null && (cDispStatus).equals(EnumCouponDisplayStatus.COUPON_CLIPPED_DISABLED)) {
    				this.setIsClipped(true);
    			} else if (cDispStatus != null && (cDispStatus).equals(EnumCouponDisplayStatus.COUPON_CLIPPABLE)) {
    				this.setIsClipped(false);
    			} else {
    				return false;
    			}
    			
    			if (this.getCouponStatusText() == null ) { //only set with logic if not set yet, to allow override
	    			if (coupon.isDisplayStatusMessage()) {
	    				this.setCouponStatusText(coupon.getStatus().getDescription()); //Enum Description store the UI message to be shown
	    				
	    				/* check for additional classes */
	    				this.setStatusClasses();
	    				
	    				/* once classes are set, replace out spaces from status enum text with html non-breaking spaces */
	    				this.setCouponStatusText( this.getCouponStatusText().replace(" ", "&nbsp;" ) ); //Enum Description store the UI message to be shown
	    				
	    			} else {
	    				this.setShowStatusMsg(false);
	    			}
    			}
    			
    			/* set details text */
    			this.setCouponDetailsText(coupon.getDetailedDescription()+(null !=coupon.getExpirationDate()?". "+coupon.getExpirationDate():""));
    			FDCouponProductInfo couponProductInfo= coupon.getCouponProductInfo();
    			if(null!=couponProductInfo){
    				skuCode = couponProductInfo.getSkuCode();
    				catId = couponProductInfo.getCatId();
    			}
    		}
    		
        	return true;
    	}
		return false;
    }
    
    public String getContent() {
    	return getContent(null);
    }
    
    public String getContent(PageContext pageContext) {
    	StringBuilder buf = new StringBuilder();
    	
		if (this.initContent(pageContext)) {
		
    		buf.append("<div name=\""+this.getName()+"_cont"+"\" class=\"fdCoupon_cont");
    			if (this.getContClass() != null) {
    				buf.append(" "+this.getContClass());
    			}
    		buf.append("\">");
    					
		    	if (showClipBox) {
		    		buf.append(getClipBoxHtml(pageContext));
		    	}
		    	
		    	/* fd coupon image */
		    	if (showCouponImage) {
		    		if (!"".equals(couponImageUrl)) {
		        		buf.append(getImageHtml());
		    		}
		    	}
		    	
		    	/* fd coupon msg */
		    	if (showMsg) {
		    		buf.append(getMsgHtml());
		    	}

		    	/* fd coupon details */
		    	if (showDetailsLink) {
		    		buf.append(" ");//add a space
		    		buf.append(getDetailsHtml()); //displayed "(details)" for hover
		    		buf.append(getDetailsContentHtml()); //text for tool tip
		    	}
		    	
		    	/* fd coupon status text */
		    	if (showStatusMsg || this.getCouponStatusPlaceholderId() != null) {
		    		buf.append(" "+getStatusTextHtml()); /* wrap status msg */
		    	}

	    	buf.append("</div>");
		}
    	
    	return buf.toString();
    }
   

	public String getDetailsHtml() {
    	StringBuilder buf = new StringBuilder();

    	buf.append("<span");
    		buf.append(" name=\""+this.getName()+"_detCont"+"\"");
    		buf.append(" class=\"fdCoupon_detCont\"");
    		buf.append(">");
		    	buf.append("(<a href=\"#\"");
		    		buf.append(" name=\""+this.getName()+"_det"+"\"");
		    		buf.append(" class=\"fdCoupon_det\"");
		    		buf.append(" onclick=\"return false;\"");
		    		buf.append(" >");
		    			buf.append("details");
		    	buf.append("</a>)");

    	buf.append("</span>");
   
    	return buf.toString();
	}

    public String getDetailsContentHtml() {
    	StringBuilder buf = new StringBuilder();
    	buf.append("<div");
    		buf.append(" name=\""+this.getName()+"_detContent"+"\"");
    		buf.append(" class=\"fdCoupon_detContent\"");
    		buf.append(" style=\"display: none;\""); //hidden by default
    	buf.append(">");
    	
    		if (this.getCouponDetailsText() != null) {
    			buf.append(this.getCouponDetailsText());
    		}
    	
    	buf.append("</div>");
    	
		return buf.toString();
 
    }

	public String getMsgHtml() {
    	StringBuilder buf = new StringBuilder();

    	buf.append("<span");
    		buf.append(" name=\""+this.getName()+"_msg"+"\"");
    		buf.append(" class=\"fdCoupon_msg\"");
    		buf.append(">");
    			buf.append(this.getCouponMsgFormatted());
   		buf.append("</span>");
    	
		return buf.toString();
	}

	public String getClipBoxHtml() {
		return getClipBoxHtml(null);
	}
	
	public String getClipBoxHtml(PageContext pageContext) {
		
    	StringBuilder buf = new StringBuilder();
    	buf.append("<span");
    		buf.append(" name=\""+this.getName()+"_cbCont"+"\"");
    		buf.append(" class=\"fdCoupon_cbCont");
		    	if (this.isClipped()) {
			    	buf.append(" isClipped");
		    	}
    		buf.append("\"");
    	buf.append(">");
		    	buf.append("<input");
		    		buf.append(" type=\"checkbox\"");
			    	buf.append(" name=\""+this.getName()+"_cb"+"\"");
			    	buf.append(" class=\"fdCoupon_cb\"");
		
			    	/* user has clipped the coupon already */
			    	if (this.isClipped()) {
			    		buf.append(" checked=\"checked\"");
			    		buf.append(" disabled=\"disabled\"");
			    	} else {
			    		if (this.getCoupon() != null) {
                buf.append(" onclick=\"");
			    			buf.append("fdCouponClip("+this.getCoupon().getCouponId()+")\"");
			    		}
			    	}
		    	
		    	buf.append(" />");

	    buf.append("</span>");
    	return buf.toString();
    }

    public String getImageHtml() {
    	StringBuilder buf = new StringBuilder();
    	
    	buf.append("<img");
    		buf.append(" src=\""+this.getCouponImageUrl()+"\"");
    		buf.append(" alt=\"\"");
    		buf.append(" name=\""+this.getName()+"_img"+"\"");
    		buf.append(" class=\"fdCoupon_img\"");
    	buf.append(" />");
    	
    	return buf.toString();
    }

    /* initContent not called in this method
     * Status text needs to be manually set, or call initContent first
     *  */
    public String getStatusTextHtml() {
    	StringBuilder buf = new StringBuilder();
    	
		/* check for additional classes */
		this.setStatusClasses();
    	
		if (this.getCouponStatusPlaceholderId() != null) { //for ajax ATC
			buf.append("<div id=\""+this.getCouponStatusPlaceholderId()+"\">");
		}
		
    	buf.append("<span");
    		buf.append(" name=\""+this.getName()+"_stat"+"\"");
    		buf.append(" class=\"fdCoupon_stat");
    		String statClass = this.getCouponStatusClass();
    		if ( !"".equals(statClass) ) {
    			buf.append(statClass);
    		}
    		buf.append("\""); //end class attrib
    		    		
    	buf.append(">");
			if (this.getCouponStatusText() != null) { //prevent default "null" text status
				buf.append(this.getCouponStatusText());
			}
    	
	    buf.append("</span>");

		if (this.getCouponStatusPlaceholderId() != null) { //for ajax ATC
			buf.append("</div>");
		}
    	
    	return buf.toString();
		
	}

    public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

}
