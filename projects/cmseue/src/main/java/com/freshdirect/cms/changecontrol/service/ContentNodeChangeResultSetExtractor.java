package com.freshdirect.cms.changecontrol.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.freshdirect.cms.changecontrol.entity.ContentChangeDetailEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeType;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;

public class ContentNodeChangeResultSetExtractor implements ResultSetExtractor<Set<ContentChangeSetEntity>> {

    @Override
    public Set<ContentChangeSetEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
        final Set<ContentChangeSetEntity> result = new HashSet<ContentChangeSetEntity>();

        String previousNodeId = null;
        String previousSetId = null;
        ContentChangeSetEntity changeSet = null;
        ContentChangeEntity contentNodeChange = null;

        while (rs.next()) {
            if (!rs.getObject("CHANGESET_ID").equals(previousSetId)) {
                changeSet = new ContentChangeSetEntity();
                changeSet.setId(rs.getInt("CHANGESET_ID"));
                changeSet.setUserId(rs.getString("USER_ID"));
                changeSet.setTimestamp(new Date(rs.getTimestamp("TIMESTAMP").getTime()));
                changeSet.setNote(rs.getString("NOTE"));
                result.add(changeSet);
                previousSetId = rs.getString("CHANGESET_ID");
            }

            if (!rs.getString("CHANGENODE_ID").equals(previousNodeId) && changeSet != null) {
                contentNodeChange = new ContentChangeEntity();
                contentNodeChange.setContentKey(ContentKeyFactory.get(ContentType.valueOf(rs.getString("CONTENTTYPE")), rs.getString("CONTENTNODE_ID")));
                contentNodeChange.setChangeType(ContentChangeType.valueOf(rs.getString("CHANGETYPE")));
                List<ContentChangeDetailEntity> details = new ArrayList<ContentChangeDetailEntity>();
                contentNodeChange.setDetails(details);
                List<ContentChangeEntity> changes = changeSet.getChanges();
                if (changes == null) {
                    changes = new ArrayList<ContentChangeEntity>();
                    changeSet.setChanges(changes);
                }
                changes.add(contentNodeChange);
                previousNodeId = rs.getString("CHANGENODE_ID");
            }

            if (rs.getString("ATTRIBUTENAME") != null && contentNodeChange != null) {
                ContentChangeDetailEntity changeDetail = new ContentChangeDetailEntity();
                changeDetail.setAttributeName(rs.getString("ATTRIBUTENAME"));
                changeDetail.setOldValue(rs.getString("OLDVALUE"));
                changeDetail.setNewValue(rs.getString("NEWVALUE"));
                List<ContentChangeDetailEntity> details = contentNodeChange.getDetails();
                details.add(changeDetail);
            }

        }
        return result;
    }

}
