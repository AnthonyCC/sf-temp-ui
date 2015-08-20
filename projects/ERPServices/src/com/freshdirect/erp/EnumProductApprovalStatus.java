package com.freshdirect.erp;

/**
 * Enumeration for product / material approval status.
 *  
 * @author kkanuganti
 */
@SuppressWarnings("javadoc")
public class EnumProductApprovalStatus implements java.io.Serializable
{
	private static final long serialVersionUID = 3178596300238354516L;
		
	public final static EnumProductApprovalStatus APPROVED = new EnumProductApprovalStatus("Y", "Approved");
	public final static EnumProductApprovalStatus UNAPPROVED = new EnumProductApprovalStatus("N", "Unapproved");

	private final String statusCode;
	private final String displayName;

	
	public static EnumProductApprovalStatus getApprovalStatus(String code) {
      if (APPROVED.getStatusCode().equalsIgnoreCase(code))
      {
          return APPROVED;
      }
      else if (UNAPPROVED.getStatusCode().equalsIgnoreCase(code))
      {
          return UNAPPROVED;
      }
      return null;
   }
	
	private EnumProductApprovalStatus(String statusCode, String displayName)
	{
		this.statusCode = statusCode;
		this.displayName = displayName;
	}

	public String getStatusCode()
	{
		return this.statusCode;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public String toString()
	{
		return this.displayName;
	}

	public boolean equals(Object o)
	{
		if (o instanceof EnumProductApprovalStatus)
		{
			return this.statusCode == ((EnumProductApprovalStatus) o).statusCode;
		}
		return false;
	}

}
