package com.freshdirect.cms.ui.model.bulkload;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Only parsedValue is stored in RpcMap (in BaseModelData) as it is an 'any' type object but Object type for attribute cannot be
 * used due to serialization constraints. Therefore we store this value in property map which is type checked in runtime. Yuck! :)
 * 
 * @author csongor
 */
public class GwtBulkLoadCell extends BaseModelData implements Serializable {
	private static final long serialVersionUID = 373607386110495162L;

	private String columnName;
	private String attributeName;
	private String attributeType;
	private String displayValue;
	private GwtBulkLoadPreviewStatus status;

	public GwtBulkLoadCell() {
		this("", "", "_U", "", null, new GwtBulkLoadPreviewStatus());
	}

	@SuppressWarnings("deprecation")
	public GwtBulkLoadCell(String columnName, String attributeName, String attributeType, String displayValue, Object parsedValue,
			GwtBulkLoadPreviewStatus status) {
		super();
		// "_K" is a special value denoting keys
		// "_U" is a special value denoting unsupported types
		if ("S".equals(attributeType) || "TXT".equals(attributeType) || "R".equals(attributeType) || "_K".equals(attributeType) || "E".equals(attributeType)
				|| "I".equals(attributeType) || "D".equals(attributeType) || "B".equals(attributeType)
				|| "DT".equals(attributeType) || "_U".equals(attributeType) || "_RR".equals(attributeType)) {
			this.columnName = columnName;
			this.attributeName = attributeName;
			this.attributeType = attributeType;
			this.displayValue = displayValue;
			if ("DT".equals(attributeType)) {
				Date date = (Date) parsedValue;
				parsedValue = fillWithZeros(date.getYear() + 1900, 4) + "-" + fillWithZeros(date.getMonth() + 1, 2) + "-"
						+ fillWithZeros(date.getDate(), 2);
			}
			if ("_RR".equals(attributeType)) {
				parsedValue = parsedValue != null ? ((BulkLoadReverseRelationship) parsedValue).name() : null;
			}
			set("parsedValue", parsedValue);
			this.status = status;
		} else {
                        throw new IllegalArgumentException("unsupported type:" + attributeType);
		}
	}

	public String getColumnName() {
		return columnName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public Object getParsedValue() {
		Object parsedValue = get("parsedValue");
		if ("_RR".equals(attributeType)) {
			parsedValue = BulkLoadReverseRelationship.valueOf(parsedValue.toString());
		}
		return parsedValue;
	}

	@SuppressWarnings( { "unchecked" })
	public String getParsedValueAsString() {
		Object value = getParsedValue();
		String type = getAttributeType();
		if (value == null)
			return "";
		else {
			if ("B".equals(type))
				return Boolean.toString((Boolean) value);
			else if ("I".equals(type))
				return Integer.toString((Integer) value);
			else if ("D".equals(type))
				return Double.toString((Double) value);
			else if ("_K".equals(type) || "S".equals(type) || "TXT".equals(type) || "E".equals(type) || "DT".equals(type) || "_U".equals(type)
					|| "_RR".equals(type))
				return value.toString();
			else if ("R".equals(type))
				return toString((Collection<String>) value);
			else
				throw new IllegalStateException("unsupported attribute type");
		}
	}

	public GwtBulkLoadPreviewStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		String classSimpleName = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
		buf.append(classSimpleName);
		buf.append('{');
		buf.append("columnName=");
		buf.append(getColumnName());
		buf.append(", attributeName=");
		buf.append(getAttributeName());
		buf.append(", attributeType=");
		buf.append(getAttributeType());
		buf.append(", displayValue=");
		buf.append(getDisplayValue());
		buf.append(", parsedValue=");
		buf.append(getParsedValueAsString());
		buf.append(", state=");
		buf.append(getStatus().getState().name());
		buf.append(", message=");
		buf.append(getStatus().getMessage());
		buf.append('}');
		return buf.toString();
	}

	public static String toString(Collection<String> contentKeys) {
		StringBuilder buf = new StringBuilder();
		Iterator<String> it = contentKeys.iterator();
		if (it.hasNext())
			buf.append(it.next());
		while (it.hasNext()) {
			buf.append(',');
			buf.append(it.next());
		}
		return buf.toString();
	}

	private static String fillWithZeros(int number, int length) {
		String text = Integer.toString(number);
		StringBuilder buf = new StringBuilder(Math.max(text.length(), length));
		int zeros = length - text.length();
		for (int i = 0; i < zeros; i++)
			buf.append('0');
		buf.append(text);
		return buf.toString();
	}
}
