package com.freshdirect.cms.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.persistence.entity.NavTreeElement;

@Profile("database")
@Repository
public class NavigationTreeRepository {

    private static final String QUERY = "select CHILD_CONTENTNODE_ID as child_key, PARENT_CONTENTNODE_ID as parent_key " + " from navtree order by CHILD_CONTENTNODE_ID";

    private static final String PARENTS_OF_KEYS = "select CHILD_CONTENTNODE_ID as child_key, PARENT_CONTENTNODE_ID as parent_key from navtree where CHILD_CONTENTNODE_ID IN (:keys)";
    private static final String PARENTS_OF_KEY = "select PARENT_CONTENTNODE_ID as parent_key from navtree where CHILD_CONTENTNODE_ID = :key";
    private static final String CHILDREN_OF_KEYS = "select CHILD_CONTENTNODE_ID as child_key from navtree where PARENT_CONTENTNODE_ID IN (:keys)";

    @Autowired
    @Qualifier("cmsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public Map<ContentKey, Set<ContentKey>> fetchParentKeysMap() {

        return jdbcTemplate.query(QUERY, new ResultSetExtractor<Map<ContentKey, Set<ContentKey>>>() {

            @Override
            public Map<ContentKey, Set<ContentKey>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                final Map<ContentKey, Set<ContentKey>> result = new HashMap<ContentKey, Set<ContentKey>>();

                while (resultSet.next()) {
                    final String childId = resultSet.getString(1);
                    final String parentId = resultSet.getString(2);

                    if (parentId != null && childId != null) {
                        ContentKey childKey = ContentKeyFactory.get(childId);
                        ContentKey parentKey = ContentKeyFactory.get(parentId);

                        Set<ContentKey> parents = result.get(childKey);
                        if (parents == null) {
                            parents = new HashSet<ContentKey>();
                            result.put(childKey, parents);
                        }
                        parents.add(parentKey);
                    }
                }
                return result;
            }
        });
    }

    public Set<NavTreeElement> queryParentsOfContentKeys(Set<ContentKey> contentKeys) {
        Set<String> contentKeyParams = new HashSet<String>();
        for (ContentKey key : contentKeys) {
            contentKeyParams.add(key.toString());
        }
        Map<String, Set<String>> keys = Collections.singletonMap("keys", contentKeyParams);
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        return namedJdbcTemplate.query(PARENTS_OF_KEYS, keys, new ResultSetExtractor<Set<NavTreeElement>>() {

            @Override
            public Set<NavTreeElement> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Set<NavTreeElement> results = new HashSet<NavTreeElement>();

                while (resultSet.next()) {
                    final String childKey = resultSet.getString(1);
                    final String parentKey = resultSet.getString(2);
                    if (childKey != null && parentKey != null) {
                        NavTreeElement actualNavTreeElement = new NavTreeElement(ContentKeyFactory.get(childKey), ContentKeyFactory.get(parentKey));
                        results.add(actualNavTreeElement);
                    }
                }

                return results;
            }

        });
    }

    public Set<ContentKey> queryParentsOfContentKey(ContentKey contentKey) {
        Map<String, String> keys = Collections.singletonMap("key", contentKey.toString());
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        return namedJdbcTemplate.query(PARENTS_OF_KEY, keys, new ResultSetExtractor<Set<ContentKey>>() {

            @Override
            public Set<ContentKey> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Set<ContentKey> results = new HashSet<ContentKey>();

                while (resultSet.next()) {
                    final String parentKey = resultSet.getString(1);
                    if (parentKey != null) {
                        results.add(ContentKeyFactory.get(parentKey));
                    }
                }

                return results;
            }

        });
    }

    public Set<ContentKey> queryChildrenOfContentKey(Set<ContentKey> contentKeys) {
        Iterator<ContentKey> keyIterator = contentKeys.iterator();
        Set<ContentKey> childKeys = new HashSet<ContentKey>();

        // fetch child keys in batches to avoid ORA-01795 error
        // which occurs when parameter limit is exceeded
        while (keyIterator.hasNext()) {
            int batchCounter = 900;

            Set<String> contentKeyParams = new HashSet<String>();
            while (keyIterator.hasNext() && batchCounter >= 0) {
                contentKeyParams.add(keyIterator.next().toString());
                batchCounter--;
            }

            Map<String, Set<String>> keys = Collections.singletonMap("keys", contentKeyParams);
            NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
            childKeys.addAll(namedJdbcTemplate.query(CHILDREN_OF_KEYS, keys, new ResultSetExtractor<Set<ContentKey>>() {

                @Override
                public Set<ContentKey> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                    Set<ContentKey> results = new HashSet<ContentKey>();

                    while (resultSet.next()) {
                        String childKey = resultSet.getString(1);
                        if (childKey != null) {
                            results.add(ContentKeyFactory.get(childKey));
                        }
                    }

                    return results;
                }

            }));
        }

        return childKeys;
    }
}
