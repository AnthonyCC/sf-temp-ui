package com.freshdirect.crm;

import java.util.Collection;
import java.util.Iterator;

import com.freshdirect.enum.EnumModel;
import com.freshdirect.framework.core.PrimaryKey;

public class CrmMessageBuffer {

	private final StringBuffer sb = new StringBuffer();

	public CrmMessageBuffer append(Object o) {
		if (o instanceof String) {
			return this.append((String) o);
		}
		if (o instanceof Collection) {
			return this.append((Collection) o);
		}
		if (o instanceof EnumModel) {
			return this.append((EnumModel) o);
		}
		if (o instanceof PrimaryKey) {
			return this.append((PrimaryKey)o);
		}
		this.sb.append(o);
		return this;
	}

	public CrmMessageBuffer append(String string) {
		sb.append(string);
		return this;
	}

	public CrmMessageBuffer append(Collection coll) {
		boolean first = true;
		for (Iterator i = coll.iterator(); i.hasNext();) {
			if (!first) {
				sb.append(", ");
			} else {
				first = false;
			}
			this.append(i.next());
		}
		return this;
	}

	public CrmMessageBuffer append(EnumModel enum) {
		return this.append(enum.getName());
	}
	
	public CrmMessageBuffer append(PrimaryKey key) {
		return this.append(key.getId());
	}

	public int length() {
		return sb.length();
	}

	public String toString() {
		return sb.toString();
	}

}