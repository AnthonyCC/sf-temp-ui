package com.freshdirect.dataloader.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author kkanuganti
 *
 */
public class FDJcoServerNotification implements Serializable
{
	private static final long serialVersionUID = 2478097799601089203L;
	
	private Map<String, String> messages = new HashMap<String, String>();

	@Override
	public int hashCode()
	{
		HashCodeBuilder bldr = new HashCodeBuilder(17, 37).appendSuper(super.hashCode());
		bldr = bldr.append(getMessages());
		return bldr.toHashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final FDJcoServerNotification rhs = (FDJcoServerNotification) obj;

		EqualsBuilder bldr = new EqualsBuilder().appendSuper(super.equals(rhs));

		bldr = bldr.append(getMessages(), rhs.getMessages());

		return bldr.isEquals();
	}

	@Override
	public String toString()
	{
		final ToStringBuilder bldr = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		bldr.append("messages", getMessages());
		return bldr.toString();
	}

	public Map<String, String> getMessages()
	{
		return messages;
	}

	public void setMessages(final Map<String, String> messages)
	{
		this.messages = messages;
	}
}
