package com.freshdirect.cms.changecontrol.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.repository.ContentChangeEntitySetRepository;
import com.freshdirect.cms.core.domain.ContentKey;
import com.google.common.base.Optional;

@Profile({ "database", "test" })
@Service
public class DatabaseChangeControlService implements ContentChangeControlService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseChangeControlService.class);

    @Autowired
    @Qualifier("cmsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ContentChangeEntitySetRepository repository;

    @Autowired
    private ContentNodeChangeResultSetExtractor extractor;

    @Override
    @Transactional
    public Optional<ContentChangeSetEntity> fetchChangeSet(int id) {
        return Optional.fromNullable(repository.findOne(id));
    }

    @Override
    @Transactional
    public Set<ContentChangeSetEntity> getHistory(ContentKey key) {
        return queryChangeSetEntities(key, null, null, null);
    }

    @Override
    @Transactional
    public ContentChangeSetEntity save(ContentChangeSetEntity changeSet) {
        return repository.save(changeSet);
    }

    @Override
    @Transactional
    public Set<ContentChangeSetEntity> queryChangeSetEntities(ContentKey contentKey, String author, Date startDate, Date endDate) {
        final String query = assembleQuery(contentKey, author, startDate, endDate);
        final Object[] parameters = assembleParameters(contentKey, author, startDate, endDate);
        LOGGER.debug("QUERY = " + query);
        return jdbcTemplate.query(query, parameters, extractor);
    }

    private String assembleQuery(ContentKey contentKey, String author, Date startDate, Date endDate) {
        StringBuilder query = new StringBuilder(
                "SELECT cs.ID AS changeset_id, cs.TIMESTAMP, cs.user_id, cs.note, cnc.changetype, cnc.contenttype, cnc.contentnode_id, cd.ATTRIBUTENAME, "
                        + "cd.oldvalue, cd.newvalue, cnc.ID as CHANGENODE_ID " + "FROM cms_changeset cs, cms_contentnodechange cnc, cms_changedetail cd "
                        + "WHERE cs.ID=cnc.changeset_id " + "AND cnc.ID = cd.CONTENTNODECHANGE_ID(+) ");
        if (contentKey != null) {
            query.append("AND cnc.contenttype = ? AND cnc.contentnode_id = ? ");
        }
        if (author != null) {
            query.append("AND user_id = ? ");
        }
        if (startDate != null && endDate != null) {
            // both dates are set
            query.append("AND cs.timestamp between ? and ? ");
        } else if (startDate != null) {
            // only startDate is set
            query.append("AND cs.timestamp > ? ");
        } else if (endDate != null) {
            // only endDate is set
            query.append("AND cs.timestamp < ? ");
        }
        query.append("ORDER BY cs.ID, contentnode_id");

        return query.toString();
    }

    private Object[] assembleParameters(ContentKey contentKey, String author, Date startDate, Date endDate) {
        List<Object> parameters = new ArrayList<Object>();
        if (contentKey != null) {
            parameters.add(contentKey.getType().name());
            parameters.add(contentKey.getId());
        }
        if (author != null) {
            parameters.add(author);
        }
        if (startDate != null) {
            parameters.add(new Timestamp(startDate.getTime()));
        }
        if (endDate != null) {
            parameters.add(new Timestamp(endDate.getTime()));
        }

        return parameters.toArray();
    }

}
