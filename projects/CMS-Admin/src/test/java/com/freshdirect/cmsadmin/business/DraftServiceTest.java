package com.freshdirect.cmsadmin.business;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftStatus;
import com.freshdirect.cmsadmin.repository.jpa.DraftRepository;
import com.freshdirect.cmsadmin.utils.EntityFactory;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftServiceTest {

    @InjectMocks
    private DraftService underTest;

    @Mock
    private DraftRepository draftRepository;

    @Mock
    private DraftChangeService draftChangeService;

    @Test
    public void testLoadAllUserDrafts() {
        List<Draft> drafts = Arrays.asList(EntityFactory.createDraft());
        Mockito.when(draftRepository.findAll()).thenReturn(drafts);
        List<Draft> loadedDrafts = underTest.loadAllDrafts();
        Assert.assertEquals(loadedDrafts, drafts);
    }

    @Test
    public void testAddDraft() {
        Draft draft = EntityFactory.createDraft();
        underTest.createDraft(draft);
        Mockito.verify(draftRepository).save(draft);
    }

    @Test
    public void testSetStatusDraft() {
        Draft draft = EntityFactory.createDraft();
        underTest.updateDraftStatus(draft, DraftStatus.DROPPED);
        Mockito.verify(draftRepository).save(draft);
    }

    @Test
    public void testLoadNotDroppedOrMergedDrafts() {
        Draft created = EntityFactory.createDraft(1, "created");
        Draft merged = EntityFactory.createDraft(2, "merged");
        Draft failed = EntityFactory.createDraft(3, "failed");
        Draft dropped = EntityFactory.createDraft(4, "dropped");

        underTest.updateDraftStatus(created, DraftStatus.CREATED);
        underTest.updateDraftStatus(merged, DraftStatus.MERGED);
        underTest.updateDraftStatus(failed, DraftStatus.FAILED);
        underTest.updateDraftStatus(dropped, DraftStatus.DROPPED);

        Mockito.when(draftRepository.findByDraftStatusNotIn(Arrays.asList(DraftStatus.DROPPED, DraftStatus.MERGED))).thenReturn(Arrays.asList(created, failed));
        List<Draft> notDroppedOrMergedDrafts = underTest.loadNotDroppedOrMergedDrafts();

        Assert.assertNotNull(notDroppedOrMergedDrafts);
        Assert.assertEquals(notDroppedOrMergedDrafts.size(), 2);
        Assert.assertTrue(notDroppedOrMergedDrafts.contains(created));
        Assert.assertTrue(notDroppedOrMergedDrafts.contains(failed));
    }

}
