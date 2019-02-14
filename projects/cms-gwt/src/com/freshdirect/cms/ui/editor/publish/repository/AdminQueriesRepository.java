package com.freshdirect.cms.ui.editor.publish.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.freshdirect.cms.ui.model.publish.PublishType;

@Profile("database")
@Repository
public class AdminQueriesRepository {

    private static final String UPDATE_STUCK_PUBLISH_SQL = "update publish " +
            "set status='FAILED' " +
            "where id in ( " +
            "  select id from ( " +
            "    select id, extract( minute from interval_difference ) " +
            "          + extract( hour from interval_difference ) * 60 " +
            "          + extract( day from interval_difference ) * 60 * 24 as dmins " +
            "    from ( " +
            "      select id, systimestamp - TIMESTAMP as interval_difference " +
            "      from publish " +
            "      where STATUS='PROGRESS' " +
            "    ) " +
            "  ) " +
            "  where dmins > 120 " +
            ") ";

    private static final String UPDATE_STUCK_FEED_PUBLISH_SQL = "update publishx " +
            "set status='FAILED' " +
            "where id in ( " +
            "  select id from ( " +
            "    select id, extract( minute from interval_difference ) " +
            "          + extract( hour from interval_difference ) * 60 " +
            "          + extract( day from interval_difference ) * 60 * 24 as dmins " +
            "    from ( " +
            "      select id, systimestamp - CRO_MOD_DATE as interval_difference " +
            "      from publishx " +
            "      where STATUS='PROGRESS' " +
            "    ) " +
            "  ) " +
            "  where dmins > 60 " +
            ") ";

    @Autowired
    @Qualifier("cmsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void updateStuckPublishStatus(PublishType type) {
        switch (type) {
            case STORE_PUBLISH:
                jdbcTemplate.update(UPDATE_STUCK_PUBLISH_SQL);
                break;
            case PUBLISH_X:
                jdbcTemplate.update(UPDATE_STUCK_FEED_PUBLISH_SQL);
                break;
        }
    }
}
