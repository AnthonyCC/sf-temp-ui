package com.freshdirect.customer;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

public class ErpClientCode implements Serializable {
	private static final long serialVersionUID = 1240988236132112162L;

	private String clientCode;

	private int quantity;
	
	public ErpClientCode() {
		super();
	}

	public ErpClientCode(String clientCode, int quantity) {
		this.clientCode = clientCode;
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientCode == null) ? 0 : clientCode.hashCode());
		result = prime * result + quantity;
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
		ErpClientCode other = (ErpClientCode) obj;
		if (clientCode == null) {
			if (other.clientCode != null)
				return false;
		} else if (!clientCode.equals(other.clientCode))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return clientCode + " (" + quantity + ")";
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String toHtml() {
		StringBuilder out = new StringBuilder();
		out.append("<span style=\"white-space: nowrap;\">");
		out.append(StringEscapeUtils.escapeHtml(clientCode));
		out.append("&nbsp;&ndash;&nbsp;");
		out.append(Integer.toString(quantity));
		out.append("&nbsp;item");
		if (quantity > 1)
			out.append("s");
		out.append("</span>");
		return out.toString();
	}
	
	public void toHtmlWriter(Writer out) throws IOException {
		out.append("<span style=\"white-space: nowrap;\">");
		out.append(StringEscapeUtils.escapeHtml(clientCode));
		out.append("&nbsp;&ndash;&nbsp;");
		out.append(Integer.toString(quantity));
		out.append("&nbsp;item");
		if (quantity > 1)
			out.append("s");
		out.append("</span>");
	}
	
	public static boolean equalsList(List<ErpClientCode> one, List<ErpClientCode> other) {
		if (one.size() != other.size())
			return false;
		
		for (int i = 0; i < one.size(); i++)
			if (!one.get(i).equals(other.get(i)))
				return false;

		return true;
	}
	
	public static String toHtml(List<ErpClientCode> clientCodes, int noCode) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < clientCodes.size() - 1; i++) {
			out.append(clientCodes.get(i).toHtml());
			out.append("&nbsp;&nbsp; |&nbsp;&nbsp; ");
		}
		if (clientCodes.size() > 0)
			out.append(clientCodes.get(clientCodes.size() - 1).toHtml());
		if (noCode > 0) {
			if (clientCodes.size() > 0)
				out.append("&nbsp;&nbsp; |&nbsp;&nbsp; ");
			out.append(new ErpClientCode("NO CLIENT CODE", noCode).toHtml());
		}
		return out.toString();
	}
	
	public static void toHtmlWriter(List<ErpClientCode> clientCodes, int noCode, Writer out) throws IOException {
		for (int i = 0; i < clientCodes.size() - 1; i++) {
			clientCodes.get(i).toHtmlWriter(out);
			out.append("&nbsp;&nbsp; |&nbsp;&nbsp; ");
		}
		if (clientCodes.size() > 0)
			clientCodes.get(clientCodes.size() - 1).toHtmlWriter(out);
		if (noCode > 0) {
			if (clientCodes.size() > 0)
				out.append("&nbsp;&nbsp; |&nbsp;&nbsp; ");
			new ErpClientCode("NO CLIENT CODE", noCode).toHtmlWriter(out);
		}
	}
}
