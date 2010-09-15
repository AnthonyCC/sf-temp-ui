package com.freshdirect.content.attributes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class ErpsAttributesKey implements Serializable {
	private static final long serialVersionUID = -1452979683439075612L;

	private String rootId;
	private String childId;
	private String grandChildId;

	public ErpsAttributesKey(String rootId, String childId, String grandChildId) {
		super();
		this.rootId = rootId;
		this.childId = childId;
		this.grandChildId = grandChildId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rootId == null) ? 0 : rootId.hashCode());
		result = prime * result + ((childId == null) ? 0 : childId.hashCode());
		result = prime * result + ((grandChildId == null) ? 0 : grandChildId.hashCode());
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
		ErpsAttributesKey other = (ErpsAttributesKey) obj;
		if (rootId == null) {
			if (other.rootId != null)
				return false;
		} else if (!rootId.equals(other.rootId))
			return false;
		if (childId == null) {
			if (other.childId != null)
				return false;
		} else if (!childId.equals(other.childId))
			return false;
		if (grandChildId == null) {
			if (other.grandChildId != null)
				return false;
		} else if (!grandChildId.equals(other.grandChildId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[" + rootId + ", " + childId + ", " + grandChildId + "]";
	}

	public String getRootId() {
		return rootId;
	}

	public String getChildId() {
		return childId;
	}

	public String getGrandChildId() {
		return grandChildId;
	}
	
	public String getQueryFragment() {
	    return (rootId != null ? "root_id = ?" : "root_id is null") + " and " + 
                (childId != null ? "child1_id = ?" : "child1_id is null") + " and "
                + (grandChildId != null ? "child2_id = ?" : "child2_id is null");
	}
	
    public int fillStatement(PreparedStatement ps, int pos, boolean fillNulls) throws SQLException {
        if (getRootId() != null) {
            ps.setString(pos++, getRootId());
        } else {
            if (fillNulls) {
                ps.setNull(pos++, Types.VARCHAR);
            }
        }
        if (getChildId() != null) {
            ps.setString(pos++, getChildId());
        } else {
            if (fillNulls) {
                ps.setNull(pos++, Types.VARCHAR);
            }
        }
        if (getGrandChildId() != null) {
            ps.setString(pos++, getGrandChildId());
        } else {
            if (fillNulls) {
                ps.setNull(pos++, Types.VARCHAR);
            }
        }
        return pos;

    }
	
}
