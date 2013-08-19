package com.freshdirect.payment;

import com.freshdirect.common.customer.EnumCardType;

public class BINInfo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3093611324330374477L;


	private String id;
	

	private Long lowRange;
	private Long highRange;
	private Long sequence;
	private EnumCardType cardType;
	
	

	/**
	 * @param lowRange
	 * @param highRange
	 * @param sequence
	 * @param cardType
	 */
	public BINInfo(String id, Long lowRange, Long highRange, Long sequence,
			EnumCardType cardType) {
		super();
		
		this.id=id;
		this.lowRange = lowRange;
		this.highRange = highRange;
		this.sequence = sequence;
		this.cardType = cardType;
	}
	public BINInfo( Long lowRange, Long highRange, Long sequence,
			EnumCardType cardType) {
		super();
		
		this.lowRange = lowRange;
		this.highRange = highRange;
		this.sequence = sequence;
		this.cardType = cardType;
	}
	
	public String getId() {
		return id;
	}

	public Long getLowRange() {
		return lowRange;
	}

	public Long getHighRange() {
		return highRange;
	}

	public Long getSequence() {
		return sequence;
	}

	public EnumCardType getCardType() {
		return cardType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cardType == null) ? 0 : cardType.hashCode());
		result = prime * result
				+ ((highRange == null) ? 0 : highRange.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lowRange == null) ? 0 : lowRange.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BINInfo))
			return false;
		BINInfo other = (BINInfo) obj;
		if (cardType == null) {
			if (other.cardType != null)
				return false;
		} else if (!cardType.equals(other.cardType))
			return false;
		if (highRange == null) {
			if (other.highRange != null)
				return false;
		} else if (!highRange.equals(other.highRange))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lowRange == null) {
			if (other.lowRange != null)
				return false;
		} else if (!lowRange.equals(other.lowRange))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BINInfo [id=" + id + ", lowRange=" + lowRange + ", highRange="
				+ highRange + ", sequence=" + sequence + ", cardType="
				+ cardType + "]";
	}

	
	

}

