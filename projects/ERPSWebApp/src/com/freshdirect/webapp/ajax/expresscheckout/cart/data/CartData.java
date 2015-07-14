package com.freshdirect.webapp.ajax.expresscheckout.cart.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.webapp.ajax.AbstractCoremetricsResponse;

/**
 * Simple java bean for cart contents. Class structure is representing the resulting JSON structure.
 * 
 * @author treer
 */
public class CartData extends AbstractCoremetricsResponse implements Serializable {

	private static final long serialVersionUID = -5968293697377155974L;

	/**
	 * Optional global error message
	 */
	private String errorMessage;

	/**
	 * Number of items
	 */
	private double itemCount;

	/**
	 * Order subtotal - formatted string
	 */
	private String subTotal;

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
	 * Custom header object sent by the client. Needs to be sent back as is.
	 */
	private Object header;

	private Map<String, List<CartSubTotalFieldData>> subTotalBox = new HashMap<String, List<CartSubTotalFieldData>>();

	private List<CartSubTotalFieldData> additionalInfo = new ArrayList<CartSubTotalFieldData>();

	private boolean goGreen;

	private boolean userRecognized;

	private boolean cartChangedByCleanUp;

	private boolean promotionApplied;

	private boolean deliveryPassTrialExpired;

	private String deliveryCharge;

	private String estimatedTotal;

	private String warningMessage;

	private String couponMessage;

	private ModifyCartData modifyCartData;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Section> getCartSections() {
		return cartSections;
	}

	public void setCartSections(List<Section> cartSections) {
		this.cartSections = cartSections;
	}

	public double getItemCount() {
		return itemCount;
	}

	public void setItemCount(double itemCount) {
		this.itemCount = itemCount;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public boolean isModifyOrder() {
		return isModifyOrder;
	}

	public void setModifyOrder(boolean isModifyOrder) {
		this.isModifyOrder = isModifyOrder;
	}

	public double getRemainingCredits() {
		return remainingCredits;
	}

	public void setRemainingCredits(double remainingCredits) {
		this.remainingCredits = remainingCredits;
	}

	public Object getHeader() {
		return header;
	}

	public void setHeader(Object header) {
		this.header = header;
	}

	public Map<String, List<CartSubTotalFieldData>> getSubTotalBox() {
		return subTotalBox;
	}

	public void setSubTotalBox(Map<String, List<CartSubTotalFieldData>> subTotalBox) {
		this.subTotalBox = subTotalBox;
	}

	public List<CartSubTotalFieldData> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(List<CartSubTotalFieldData> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public boolean isGoGreen() {
		return goGreen;
	}

	public void setGoGreen(boolean goGreen) {
		this.goGreen = goGreen;
	}

	public boolean isCartChangedByCleanUp() {
		return cartChangedByCleanUp;
	}

	public void setCartChangedByCleanUp(boolean cartChangedByCleanUp) {
		this.cartChangedByCleanUp = cartChangedByCleanUp;
	}

	public boolean isUserRecognized() {
		return userRecognized;
	}

	public void setUserRecognized(boolean userRecognized) {
		this.userRecognized = userRecognized;
	}

	public boolean isPromotionApplied() {
		return promotionApplied;
	}

	public void setPromotionApplied(boolean promotionApplied) {
		this.promotionApplied = promotionApplied;
	}

	public boolean isDeliveryPassTrialExpired() {
		return deliveryPassTrialExpired;
	}

	public void setDeliveryPassTrialExpired(boolean deliveryPassTrialExpired) {
		this.deliveryPassTrialExpired = deliveryPassTrialExpired;
	}

	public String getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(String deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public String getEstimatedTotal() {
		return estimatedTotal;
	}

	public void setEstimatedTotal(String estimatedTotal) {
		this.estimatedTotal = estimatedTotal;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public String getCouponMessage() {
		return couponMessage;
	}

	public void setCouponMessage(String couponMessage) {
		this.couponMessage = couponMessage;
	}

	public static class Section implements Serializable {

		private static final long serialVersionUID = 1965764194639278346L;

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

		private SectionInfo info;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getTitleImg() {
			return titleImg;
		}

		public void setTitleImg(String titleImg) {
			this.titleImg = titleImg;
		}

		public List<Item> getCartLines() {
			return cartLines;
		}

		public void setCartLines(List<Item> cartLines) {
			this.cartLines = cartLines;
		}

		public SectionInfo getInfo() {
			return info;
		}

		public void setInfo(SectionInfo info) {
			this.info = info;
		}
	}

	public static class SectionInfo implements Serializable {

		private static final long serialVersionUID = 8395882429272205635L;

		private String sectionTitle;
		private String externalGroup;
		private boolean recipe;
		private boolean wine;
		private String subTotal;
		private String taxTotal;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((sectionTitle == null) ? 0 : sectionTitle.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SectionInfo other = (SectionInfo) obj;
			if (sectionTitle == null) {
				if (other.sectionTitle != null)
					return false;
			} else if (!sectionTitle.equals(other.sectionTitle))
				return false;
			return true;
		}

		public String getSectionTitle() {
			return sectionTitle;
		}

		public void setSectionTitle(String sectionTitle) {
			this.sectionTitle = sectionTitle;
		}

		public String getExternalGroup() {
			return externalGroup;
		}

		public void setExternalGroup(String externalGroup) {
			this.externalGroup = externalGroup;
		}

		public boolean isRecipe() {
			return recipe;
		}

		public void setRecipe(boolean recipe) {
			this.recipe = recipe;
		}

		public boolean isWine() {
			return wine;
		}

		public void setWine(boolean wine) {
			this.wine = wine;
		}

		public String getSubTotal() {
			return subTotal;
		}

		public void setSubTotal(String subTotal) {
			this.subTotal = subTotal;
		}

		public String getTaxTotal() {
			return taxTotal;
		}

		public void setTaxTotal(String taxTotal) {
			this.taxTotal = taxTotal;
		}
	}

	public static class Item implements Serializable {

		private static final long serialVersionUID = -546124781472359953L;

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

		private boolean hasEstimatedPrice;
		private boolean hasTax;
		private boolean hasDepositValue;
		private boolean hasScaledPricing;

		private String image;
		private String unitPrice;
		private String unitScale;

		private Discount discount;

		private FDCustomerCoupon coupon;
		private boolean couponDisplay;
		private boolean couponClipped;
		private String couponStatusText;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDescr() {
			return descr;
		}

		public void setDescr(String descr) {
			this.descr = descr;
		}

		public String getConfDescr() {
			return confDescr;
		}

		public void setConfDescr(String confDescr) {
			this.confDescr = confDescr;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public boolean isRecent() {
			return recent;
		}

		public void setRecent(boolean recent) {
			this.recent = recent;
		}

		public boolean isNewItem() {
			return newItem;
		}

		public void setNewItem(boolean newItem) {
			this.newItem = newItem;
		}

		public Quantity getQu() {
			return qu;
		}

		public void setQu(Quantity qu) {
			this.qu = qu;
		}

		public List<SalesUnit> getSu() {
			return su;
		}

		public void setSu(List<SalesUnit> su) {
			this.su = su;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getUnitPrice() {
			return unitPrice;
		}

		public void setUnitPrice(String unitPrice) {
			this.unitPrice = unitPrice;
		}

		public String getUnitScale() {
			return unitScale;
		}

		public void setUnitScale(String unitScale) {
			this.unitScale = unitScale;
		}

		public Discount getDiscount() {
			return discount;
		}

		public void setDiscount(Discount discount) {
			this.discount = discount;
		}

		public FDCustomerCoupon getCoupon() {
			return coupon;
		}

		public void setCoupon(FDCustomerCoupon coupon) {
			this.coupon = coupon;
		}

		public boolean isCouponDisplay() {
			return couponDisplay;
		}

		public void setCouponDisplay(boolean couponDisplay) {
			this.couponDisplay = couponDisplay;
		}

		public boolean isCouponClipped() {
			return couponClipped;
		}

		public void setCouponClipped(boolean couponClipped) {
			this.couponClipped = couponClipped;
		}

		public String getCouponStatusText() {
			return couponStatusText;
		}

		public void setCouponStatusText(String couponStatusText) {
			this.couponStatusText = couponStatusText;
		}

		public boolean getHasEstimatedPrice() {
			return hasEstimatedPrice;
		}

		public void setHasEstimatedPrice(boolean hasEstimatedPrice) {
			this.hasEstimatedPrice = hasEstimatedPrice;
		}

		public boolean getHasTax() {
			return hasTax;
		}

		public void setHasTax(boolean hasTax) {
			this.hasTax = hasTax;
		}

		public boolean getHasDepositValue() {
			return hasDepositValue;
		}

		public void setHasDepositValue(boolean hasDepositValue) {
			this.hasDepositValue = hasDepositValue;
		}

		public boolean getHasScaledPricing() {
			return hasScaledPricing;
		}

		public void setHasScaledPricing(boolean hasScaledPricing) {
			this.hasScaledPricing = hasScaledPricing;
		}

		public static class Discount implements Serializable {

			private static final long serialVersionUID = 8370395169692317105L;

			private String discountPrice;
			private String skuLimit;
			private String description;

			public String getDiscountPrice() {
				return discountPrice;
			}

			public void setDiscountPrice(String discountPrice) {
				this.discountPrice = discountPrice;
			}

			public String getSkuLimit() {
				return skuLimit;
			}

			public void setSkuLimit(String skuLimit) {
				this.skuLimit = skuLimit;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}
		}
	}

	public static class Quantity implements Serializable {

		private static final long serialVersionUID = -9046346863794560757L;

		public Quantity() {
		}

		public Quantity(int qMin, int qMax, int qInc, int quantity) {
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

		public void setQuantity(double quantity) {
			this.quantity = quantity;
		}

		public double getqMin() {
			return qMin;
		}

		public void setqMin(double qMin) {
			this.qMin = qMin;
		}

		public double getqMax() {
			return qMax;
		}

		public void setqMax(double qMax) {
			this.qMax = qMax;
		}

		public double getqInc() {
			return qInc;
		}

		public void setqInc(double qInc) {
			this.qInc = qInc;
		}
	}

	public static class SalesUnit implements Serializable {

		private static final long serialVersionUID = -924336302405745446L;

		public SalesUnit() {
		}

		public SalesUnit(String id, String name, boolean selected) {
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

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}

	public static final Comparator<Section> CART_DATA_SECTION_COMPARATOR_BY_TITLE = new Comparator<CartData.Section>() {
		@Override
		public int compare(Section o1, Section o2) {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	};

	public static final Comparator<Section> CART_DATA_SECTION_COMPARATOR_BY_EXTERNAL_GROUP = new Comparator<Section>() {

		@Override
		public int compare(Section o1, Section o2) {
			String externalGroup1 = null;
			if (o1.getInfo() != null) {
				externalGroup1 = o1.getInfo().getExternalGroup();
			}
			String externalGroup2 = null;
			if (o2.getInfo() != null) {
				externalGroup2 = o2.getInfo().getExternalGroup();
			}

			if (externalGroup1 == null) {
				if (externalGroup2 == null) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if (externalGroup2 == null) {
					return -1;
				} else {
					return externalGroup1.compareTo(externalGroup2);
				}
			}
		}
	};

	public static final Comparator<Section> CART_DATA_COMPARATOR_BY_RECIPE_FLAG = new Comparator<Section>() {

		@Override
		public int compare(Section o1, Section o2) {
			Boolean recipe1 = null;
			if (o1.getInfo() != null) {
				recipe1 = o1.getInfo().isRecipe();
			}
			Boolean recipe2 = null;
			if (o2.getInfo() != null) {
				recipe2 = o2.getInfo().isRecipe();
			}
			if (recipe1 == null) {
				if (recipe2 == null) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return recipe1.compareTo(recipe2);
			}
		}
	};

	public static final Comparator<Section> CART_DATA_COMPARATOR_BY_WINE_FLAG = new Comparator<Section>() {

		@Override
		public int compare(Section o1, Section o2) {
			Boolean wine1 = null;
			if (o1.getInfo() != null) {
				wine1 = o1.getInfo().isWine();
			}
			Boolean wine2 = null;
			if (o2.getInfo() != null) {
				wine2 = o2.getInfo().isWine();
			}
			if (wine1 == null) {
				if (wine2 == null) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return wine1.compareTo(wine2);
			}
		}
	};

	@SuppressWarnings("unchecked")
	public static final Comparator<Section> CART_DATA_SECTION_COMPARATOR_CHAIN_BY_EXTERNAL_GROUP_AND_TITLE = ComparatorChain.create(CART_DATA_COMPARATOR_BY_WINE_FLAG,
			CART_DATA_SECTION_COMPARATOR_BY_EXTERNAL_GROUP, CART_DATA_SECTION_COMPARATOR_BY_TITLE);

	public ModifyCartData getModifyCartData() {
		return modifyCartData;
	}

	public void setModifyCartData(ModifyCartData modifyCartData) {
		this.modifyCartData = modifyCartData;
	}

}
