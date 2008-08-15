package com.freshdirect.webapp.util;

/**
 * Used for displaying Categories or products in the jsp's 
 * Methods in JSP methods will return this object populated, to the jsp pages.
 */
public class DisplayObject {
		private String itemName = null;
		private String itemURL = null;
		private String imagePath = null;
		private String imageWidth = null;
		private String imageHeight = null;
		private String imageName = "";
		private String altText = null;
		private String price = null;
		private String rolloverString = null;
		private String indicators=null;
		private String salesUnitDescription=null;
		private String rating=null;

		public void setItemName(String itemName) {
			this.itemName=itemName;
		}
		public void setItemURL(String itemURL) {this.itemURL=itemURL;}
		public void setImageWidth(String imageWidth) {this.imageWidth = imageWidth;}
		public void setImageHeight(String imageHeight) {this.imageHeight=imageHeight;}
		public void setImageName(String imageName) {this.imageName = imageName;}
		public void setImagePath(String imagePath) {this.imagePath = imagePath;}
		public void setAltText(String altText) {this.altText=altText;}
		public void setRolloverString(String rolloStuff) {this.rolloverString=rolloStuff;}
		public void setIndicators(String indicators) {this.indicators=indicators;}
		public void setPrice(String price) {this.price = price;}
		public void setSalesUnitDescription(String pd) {this.salesUnitDescription = pd;}
		
		public String getItemName() {return this.itemName;}
		public String getItemNameWithoutBreaks() {
			String s = this.itemName.replaceAll("<BR>"," ");
			return s.replaceAll("<br>"," ");
		}
		public String getItemURL() {return this.itemURL;}
		public String getImageName() {return this.imageName;}
		public String getImageWidth() {return this.imageWidth;}
		public String getImagePath() {return this.imagePath;}
		public String getImageHeight() {return this.imageHeight;}
		public String getAltText() {return this.altText;}
		public String getRolloverString() {return this.rolloverString;}
		public String getIndicators() {return this.indicators;}
		public String getPrice() {return this.price;}
		public String getSalesUnitDescription() {return this.salesUnitDescription; }
		
		public int getImageWidthAsInt() {
			if (this.imageWidth==null) return 0; 
			try {
				return Integer.parseInt(this.imageWidth);
			} catch (NumberFormatException nfe) {
				return 0;
			}
		}
		
		public String getImageDimensions() {
				StringBuffer rtnString = new StringBuffer(40);
				if (this.imageWidth!=null) {
					rtnString.append(" width=\"");
					rtnString.append(this.imageWidth);
					rtnString.append("\"");
				}
				if (this.imageHeight!=null) {
					rtnString.append(" height=\"");
					rtnString.append(this.imageHeight);
					rtnString.append("\"");
				}
				return rtnString.toString();
			}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}

}
