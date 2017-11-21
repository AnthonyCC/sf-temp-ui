package com.freshdirect.cms.changecontrol.service;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.repository.ContentChangeEntitySetRepository;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.util.EntityFactory;
import com.google.common.base.Optional;

@Category(UnitTest.class)
@RunWith(MockitoJUnitRunner.class)
public class DatabaseChangeControlServiceTest {

    @InjectMocks
    private DatabaseChangeControlService testService;

    @Mock
    private ContentChangeEntitySetRepository repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ContentNodeChangeResultSetExtractor extractor;

    @Test
    public void fetchChangeSet() {
        ContentChangeSetEntity changeSet = EntityFactory.createBasicChangeSet();
        Mockito.when(repository.findOne(EntityFactory.CHANGESET_SINGLE_ID)).thenReturn(changeSet);
        Optional<ContentChangeSetEntity> optionalChangeSet = testService.fetchChangeSet(EntityFactory.CHANGESET_SINGLE_ID);
        Assert.assertTrue("Non-nullable value contains null value", optionalChangeSet.isPresent());
        Assert.assertEquals("Mismatched changeset ID", EntityFactory.CHANGESET_SINGLE_ID, optionalChangeSet.get().getId());
        Assert.assertEquals("Mismatched changeset ID", changeSet, optionalChangeSet.get());
    }

    @Test
    public void fetchHistory() throws DataAccessException {
        final String query = "SELECT cs.ID AS changeset_id, cs.TIMESTAMP, cs.user_id, cs.note, cnc.changetype, cnc.contenttype, cnc.contentnode_id, cd.ATTRIBUTENAME, "
                + "cd.oldvalue, cd.newvalue, cnc.ID as CHANGENODE_ID FROM cms_changeset cs, cms_contentnodechange cnc, cms_changedetail cd "
                + "WHERE cs.ID=cnc.changeset_id AND cnc.ID = cd.CONTENTNODECHANGE_ID(+) AND cnc.contenttype = ? AND cnc.contentnode_id = ? ORDER BY cs.ID, contentnode_id";
        final ContentKey productkey = ContentKeyFactory.get(ContentType.Product, "productId");
        final Object[] parameters = { productkey.getType().name(), productkey.getId() };
        final Set<ContentChangeSetEntity> changeSet = EntityFactory.createChangeHistory();

        Mockito.when(jdbcTemplate.query(query, parameters, extractor)).thenReturn(changeSet);
        Set<ContentChangeSetEntity> result = testService.getHistory(productkey);
        Assert.assertEquals("Changeset is not matched", changeSet, result);
    }

    @Test
    public void saveChangeset() {
        final ContentChangeSetEntity changeSet = EntityFactory.createBasicChangeSet();
        Mockito.when(repository.save(changeSet)).thenReturn(changeSet);
        ContentChangeSetEntity result = testService.save(changeSet);
        Assert.assertEquals("Changeset is not matched", changeSet, result);
    }

    @Test
    public void fetchQueryChangeSetEntitiesWithNullParameters() throws DataAccessException {
        final String query = "SELECT cs.ID AS changeset_id, cs.TIMESTAMP, cs.user_id, cs.note, cnc.changetype, cnc.contenttype, cnc.contentnode_id, cd.ATTRIBUTENAME, "
                + "cd.oldvalue, cd.newvalue, cnc.ID as CHANGENODE_ID FROM cms_changeset cs, cms_contentnodechange cnc, cms_changedetail cd "
                + "WHERE cs.ID=cnc.changeset_id AND cnc.ID = cd.CONTENTNODECHANGE_ID(+) ORDER BY cs.ID, contentnode_id";
        final Object[] parameters = {};
        final Set<ContentChangeSetEntity> changeSet = EntityFactory.createChangeHistory();

        Mockito.when(jdbcTemplate.query(query, parameters, extractor)).thenReturn(changeSet);
        Set<ContentChangeSetEntity> result = testService.queryChangeSetEntities(null, null, null, null);
        Assert.assertEquals("Changeset is not matched", changeSet, result);
    }

    @Test
    public void fetchQueryChangeSetEntitiesWithValidProductKey() throws DataAccessException {
        final String query = "SELECT cs.ID AS changeset_id, cs.TIMESTAMP, cs.user_id, cs.note, cnc.changetype, cnc.contenttype, cnc.contentnode_id, cd.ATTRIBUTENAME, "
                + "cd.oldvalue, cd.newvalue, cnc.ID as CHANGENODE_ID FROM cms_changeset cs, cms_contentnodechange cnc, cms_changedetail cd "
                + "WHERE cs.ID=cnc.changeset_id AND cnc.ID = cd.CONTENTNODECHANGE_ID(+) AND cnc.contenttype = ? AND cnc.contentnode_id = ? ORDER BY cs.ID, contentnode_id";
        final ContentKey productkey = ContentKeyFactory.get(ContentType.Product, "productId");
        final Object[] parameters = { productkey.getType().name(), productkey.getId() };
        final Set<ContentChangeSetEntity> changeSet = EntityFactory.createChangeHistory();

        Mockito.when(jdbcTemplate.query(query, parameters, extractor)).thenReturn(changeSet);
        Set<ContentChangeSetEntity> result = testService.queryChangeSetEntities(productkey, null, null, null);
        Assert.assertEquals("Changeset is not matched", changeSet, result);
    }

    @Test
    public void fetchQueryChangeSetEntitiesWithValidAuthor() throws DataAccessException {
        final String query = "SELECT cs.ID AS changeset_id, cs.TIMESTAMP, cs.user_id, cs.note, cnc.changetype, cnc.contenttype, cnc.contentnode_id, cd.ATTRIBUTENAME, "
                + "cd.oldvalue, cd.newvalue, cnc.ID as CHANGENODE_ID FROM cms_changeset cs, cms_contentnodechange cnc, cms_changedetail cd "
                + "WHERE cs.ID=cnc.changeset_id AND cnc.ID = cd.CONTENTNODECHANGE_ID(+) AND user_id = ? ORDER BY cs.ID, contentnode_id";
        final String author = "author";
        final Object[] parameters = {author};
        final Set<ContentChangeSetEntity> changeSet = EntityFactory.createChangeHistory();

        Mockito.when(jdbcTemplate.query(query, parameters, extractor)).thenReturn(changeSet);
        Set<ContentChangeSetEntity> result = testService.queryChangeSetEntities(null, author, null, null);
        Assert.assertEquals("Changeset is not matched", changeSet, result);
    }

}
