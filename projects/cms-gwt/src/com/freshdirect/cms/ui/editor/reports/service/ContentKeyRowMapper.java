package com.freshdirect.cms.ui.editor.reports.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;

@Service
public class ContentKeyRowMapper implements RowMapper<ContentKey> {

    @Override
    public ContentKey mapRow(ResultSet resultSet, int position) throws SQLException {
        return ContentKeyFactory.get(resultSet.getString(1));
    }

}
