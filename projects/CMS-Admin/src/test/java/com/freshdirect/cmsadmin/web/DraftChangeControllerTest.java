package com.freshdirect.cmsadmin.web;

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
import org.springframework.validation.BindingResult;

import com.freshdirect.cmsadmin.business.DraftChangeService;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.cmsadmin.utils.EntityFactory;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftChangeControllerTest {

    @InjectMocks
    private DraftChangeController underTest;

    @Mock
    private DraftChangeService draftChangeService;

    @Mock
    private BindingResult result;

    // @Mock
    // private DraftChangeValidator draftValidator;


    @Test
    public void testLoadAllDraftChanges() {
        List<DraftChange> draftChanges = Arrays.asList(EntityFactory.createDraftChange());
        Mockito.when(draftChangeService.loadAllDraftChanges()).thenReturn(draftChanges);
        List<DraftChange> loadedDraftChanges = underTest.loadDraftChanges();
        Mockito.verify(draftChangeService, Mockito.times(1)).loadAllDraftChanges();
        Assert.assertEquals(draftChanges, loadedDraftChanges);
    }

    @Test
    public void testCreateDraftChange() {
        Draft draft = EntityFactory.createDraft();
        List<DraftChange> draftChanges = Arrays.asList(EntityFactory.createDraftChange(draft));
        Mockito.when(draftChangeService.loadAllChangesByDraft((Mockito.eq(draft)))).thenReturn(draftChanges);
        List<DraftChange> loadedDraftChangesAfterCreation = underTest.createDraftChange(draftChanges);
        Mockito.verify(draftChangeService).createDraftChange(draftChanges);
        Mockito.verify(draftChangeService).loadAllChangesByDraft((Mockito.eq(draft)));
        Assert.assertEquals(draftChanges, loadedDraftChangesAfterCreation);
    }

    @Test
    public void testloadDraftChangesByDraft() {
        Draft draft = EntityFactory.createDraft();
        List<DraftChange> draftChanges = Arrays.asList(EntityFactory.createDraftChange(draft));
        Mockito.when(draftChangeService.loadAllChangesByDraft(Mockito.eq(draft))).thenReturn(draftChanges);
        List<DraftChange> loadedDraftChanges = underTest.loadDraftChangesByDraft(draft);
        Mockito.verify(draftChangeService).loadAllChangesByDraft(draft);
        Assert.assertEquals(draftChanges, loadedDraftChanges);
    }

}
