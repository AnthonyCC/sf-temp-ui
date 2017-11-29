package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.webapp.ajax.analytics.data.GoogleAnalyticsData;

/**
 *	Simple java bean for cart contents. 
 *	Class structure is representing the resulting JSON structure. 	
 * 
 * @author treer
 */
public class CartData implements Serializable {
	
	private static final long	serialVersionUID	= -5968293697377155974L;

	/**
	 * Optional global error message
	 */
	private String errorMessage;
	
	/**
	 * Coremetrics script - to run on the client side
	 */
	private String coremetricsScript;
	
	/**
	 * Number of items 
	 */
	private double itemCount;
	
	/**
	 * Order subtotal - formatted string
	 */
	private String subTotal;
	
	/**
	 * Order saveAmount - formatted string
	 */
	private String saveAmount;
	
	/**
	 * Is a Modify Order Cart ?
	 */
	private boolean isModifyOrder = false;
	
	/**
	 * Customer Credit History
	 */
	private double remainingCredits = 0.0; 

	/**
	 * List of cart sections
	 */
	private List<Section> cartSections;
	
	/**
	 * Custom header object sent by the client.
	 * Needs to be sent back as is. 
	 */
	private Object header;
	
    private GoogleAnalyticsData googleAnalyticsData;

	public String getErrorMessage() {
		return errorMessage;
	}	
	public void setErrorMessage( String errorMessage ) {
		this.errorMessage = errorMessage;
	}
	
	public String getCoremetricsScript() {
		return coremetricsScript;
	}	
	public void setCoremetricsScript( String coremetricsScript ) {
		this.coremetricsScript = coremetricsScript;
	}
	
	public List<Section> getCartSections() {
		return cartSections;
	}	
	public void setCartSections( List<Section> cartSections ) {
		this.cartSections = cartSections;
	}

	public double getItemCount() {
		return itemCount;
	}	
	public void setItemCount( double itemCount ) {
		this.itemCount = itemCount;
	}
	
	public String getSubTotal() {
		return subTotal;
	}	
	public void setSubTotal( String subTotal ) {
		this.subTotal = subTotal;
	}
	
	public void setSaveAmount( String saveAmount ) {
		this.saveAmount = saveAmount;
	}
	
	public String getSaveAmount() {
		return saveAmount;
	}
	
	public boolean isModifyOrder() {
		return isModifyOrder;
	}	
	public void setModifyOrder( boolean isModifyOrder ) {
		this.isModifyOrder = isModifyOrder;
	}

	public double getRemainingCredits() {
		return remainingCredits;
	}	
	public void setRemainingCredits( double remainingCredits ) {
		this.remainingCredits = remainingCredits;
	}

	public Object getHeader() {
		return header;
	}	
	public void setHeader( Object header ) {
		this.header = header;
	}	
	
    public GoogleAnalyticsData getGoogleAnalyticsData() {
        return googleAnalyticsData;
    }

    public void setGoogleAnalyticsData(GoogleAnalyticsData googleAnalyticsData) {
        this.googleAnalyticsData = googleAnalyticsData;
    }

    public static class Section implements Serializable {
		
		private static final long	serialVersionUID	= 1965764194639278346L;

		/**
		 * Section title
		 */
		private String title;
		
		/**
		 * Section title image url
		 */
		private String titleImg;
		
		/**
		 * List of cartline items
		 */
		private List<Item> cartLines;
		
		public String getTitle() {
			return title;
		}		
		public void setTitle( String title ) {
			this.title = title;
		}

		public String getTitleImg() {
			return titleImg;
		}		
		public void setTitleImg( String titleImg ) {
			this.titleImg = titleImg;
		}
		
		public List<Item> getCartLines() {
			return cartLines;
		}		
		public void setCartLines( List<Item> cartLines ) {
			this.cartLines = cartLines;
		}		
	}
	
	
	public static class Item implements Serializable {
		
		private static final long	serialVersionUID	= -546124781472359953L;

		/**
		 * Cartline id - currently the bizarre random id is used, as on the view cart page .... FIXME: use some consistent id instead....
		 */
		private int id;
		
		/**
		 * Cartline price - formatted string
		 */
		private String price;

		/**
		 * Cartline amount for numeric quantity type
		 */
		private Quantity qu;
		
		/**
		 * Cartline amount for sales-unit enum type
		 */
		private List<SalesUnit> su;
		
		/**
		 * String description (?=product name?)
		 */
		private String descr;
		
		/**
		 * Configuration description text
		 */
		private String confDescr;
		
		/**
		 * Is recently changed or added?
		 */
		private boolean recent;

		/**
		 * Is new?
		 */
		private boolean newItem;
		
		private boolean isFreeSamplePromoProduct;
		
		public int getId() {
			return id;
		}		
		public void setId( int id ) {
			this.id = id;
		}		
		public String getDescr() {
			return descr;
		}		
		public void setDescr( String descr ) {
			this.descr = descr;
		}		
		public String getConfDescr() {
			return confDescr;
		}		
		public void setConfDescr( String confDescr ) {
			this.confDescr = confDescr;
		}		
		public String getPrice() {
			return price;
		}	
		public void setPrice( String price ) {
			this.price = price;
		}
		public boolean isRecent() {
			return recent;
		}		
		public void setRecent( boolean recent ) {
			this.recent = recent;
		}		
		public boolean isNewItem() {
			return newItem;
		}		
		public void setNewItem( boolean newItem ) {
			this.newItem = newItem;
		}		
		public Quantity getQu() {
			return qu;
		}		
		public void setQu( Quantity qu ) {
			this.qu = qu;
		}		
		public List<SalesUnit> getSu() {
			return su;
		}		
		public void setSu( List<SalesUnit> su ) {
			this.su = su;
		}
		public boolean isFreeSamplePromoProduct() {
			return isFreeSamplePromoProduct;
		}
		public void setFreeSamplePromoProduct(boolean isFreeSamplePromoProduct) {
			this.isFreeSamplePromoProduct = isFreeSamplePromoProduct;
		}		
	}
	
	public static class Quantity implements Serializable {		
		
		private static final long	serialVersionUID	= -9046346863794560757L;

		public Quantity() {
		}
		
		public Quantity( int qMin, int qMax, int qInc, int quantity ) {
			this.qMin = qMin;
			this.qMax = qMax;
			this.qInc = qInc;
			this.quantity = quantity;
		}
		
		/**
		 * Cartline quantity
		 */
		private double quantity;
		
		/**
		 * Minimum quantity
		 */
		private double qMin;
		
		/**
		 * Maximum quantity
		 */
		private double qMax;
		
		/**
		 * Quantity increment
		 */
		private double qInc;		

		
		public double getQuantity() {
			return quantity;
		}	
		public void setQuantity( double quantity ) {
			this.quantity = quantity;
		}		
		public double getqMin() {
			return qMin;
		}		
		public void setqMin( double qMin ) {
			this.qMin = qMin;
		}		
		public double getqMax() {
			return qMax;
		}		
		public void setqMax( double qMax ) {
			this.qMax = qMax;
		}		
		public double getqInc() {
			return qInc;
		}		
		public void setqInc( double qInc ) {
			this.qInc = qInc;
		}
	}

	public static class SalesUnit implements Serializable {
				
		private static final long	serialVersionUID	= -924336302405745446L;

		public SalesUnit() {
		}
		
		public SalesUnit( String id, String name, boolean selected ) {
			this.id = id;
			this.name = name;
			this.selected = selected;
		}
		
		private String id;
		private String name;
		private boolean selected;
		
		public String getId() {
			return id;
		}			
		public void setId( String id ) {
			this.id = id;
		}			
		public String getName() {
			return name;
		}			
		public void setName( String name ) {
			this.name = name;
		}
		
		public boolean isSelected() {
			return selected;
		}			
		public void setSelected( boolean selected ) {
			this.selected = selected;
		}
	}
	
	public static final Comparator<Section> CART_DATA_SECTION_COMPARATOR_BY_TITLE = new Comparator<CartData.Section>() {
		@Override
		public int compare(Section o1, Section o2) {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	};
}
