package com.freshdirect.dataloader.response;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * @author kkanuganti
 *
 */
@SuppressWarnings("javadoc")
public class FDJcoServerResult implements Serializable
{
	private static final long serialVersionUID = -6851628699408348451L;
	
	public final static String OK_STATUS = "OK".intern();
	public final static String WARNING_STATUS = "Warning".intern();
	public final static String ERROR_STATUS = "Error".intern();

	private String status;
	private FDJcoServerNotification error = new FDJcoServerNotification();
	private FDJcoServerNotification warning = new FDJcoServerNotification();
	private FDJcoServerNotification info = new FDJcoServerNotification();

	@Override
	public int hashCode()
	{
		HashCodeBuilder bldr = new HashCodeBuilder(17, 37).appendSuper(super.hashCode());
		bldr = bldr.append(getStatus());

		bldr = bldr.append(getError());
		bldr = bldr.append(getWarning());
		bldr = bldr.append(getInfo());

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
		final FDJcoServerResult rhs = (FDJcoServerResult) obj;

		EqualsBuilder bldr = new EqualsBuilder().appendSuper(super.equals(rhs));

		bldr = bldr.append(getStatus(), rhs.getStatus());
		bldr = bldr.append(getError(), rhs.getError());
		bldr = bldr.append(getWarning(), rhs.getWarning());
		bldr = bldr.append(getInfo(), rhs.getInfo());

		return bldr.isEquals();
	}

	@Override
	public String toString()
	{
		final ToStringBuilder bldr = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);
		bldr.append("status", getStatus());

		bldr.append("error", getError());
		bldr.append("warning", getWarning());
		bldr.append("info", getInfo());
		return bldr.toString();
	}

	public String getStatus()
	{
		if (StringUtils.isBlank(status))
		{
			if (hasErrors())
			{
				return ERROR_STATUS;
			}
			else if (hasWarnings())
			{
				return WARNING_STATUS;
			}
			else
			{
				return OK_STATUS;
			}
		}
		else
		{
			return status;
		}
	}

	private boolean hasErrors()
	{
		return error != null && error.getMessages().size() > 0;
	}

	private boolean hasWarnings()
	{
		return error != null && error.getMessages().size() > 0;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public FDJcoServerNotification getWarning()
	{
		return warning;
	}

	public void setWarning(final FDJcoServerNotification warning)
	{
		this.warning = warning;
	}

	public FDJcoServerNotification getError()
	{
		return error;
	}

	public void setError(final FDJcoServerNotification error)
	{
		this.error = error;
	}

	public FDJcoServerNotification getInfo()
	{
		return info;
	}

	public void setInfo(final FDJcoServerNotification info)
	{
		this.info = info;
	}

	public void addError(final String errorMessage)
	{
		this.addError(errorMessage, errorMessage);
	}

	public void addError(final String code, final String errorMessage)
	{
		final FDJcoServerNotification error = getError();
		final Map<String, String> messages = error.getMessages();
		messages.put(code, errorMessage);

	}

}
