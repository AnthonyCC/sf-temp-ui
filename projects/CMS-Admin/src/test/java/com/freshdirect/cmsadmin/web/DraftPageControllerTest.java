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

import com.freshdirect.cmsadmin.business.DraftService;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftStatus;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.DraftValidator;
import com.freshdirect.cmsadmin.web.dto.DraftManagementPage;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftPageControllerTest {

    @InjectMocks
    private DraftPageController underTest;

    @Mock
    private DraftService draftService;

    @Mock
    private BindingResult result;

    @Mock
    private DraftValidator draftValidator;

    @Mock
    private PageDecorator pageDecorator;

    @Test
    public void testLoadDraftManagementPage() {
        List<Draft> drafts = Arrays.asList(EntityFactory.createDraft());
        Mockito.when(pageDecorator.decorateDraftManagementPage(Mockito.any(DraftManagementPage.class), Mockito.eq(drafts))).thenReturn(new DraftManagementPage());
        Mockito.when(draftService.loadAllDrafts()).thenReturn(drafts);
        underTest.loadDraftManagementPage();
        Mockito.verify(pageDecorator).decorateDraftManagementPage(Mockito.any(DraftManagementPage.class), Mockito.eq(drafts));
    }

    @Test
    public void testCreateDraft() {
        Draft draft = EntityFactory.createDraft();
        List<Draft> drafts = Arrays.asList(draft);
        Mockito.when(pageDecorator.decorateDraftManagementPage(Mockito.any(DraftManagementPage.class), Mockito.eq(drafts))).thenReturn(new DraftManagementPage());
        Mockito.when(draftService.loadAllDrafts()).thenReturn(drafts);
        DraftManagementPage draftPage = underTest.createDraft(draft, result);
        Mockito.verify(pageDecorator).decorateDraftManagementPage(Mockito.any(DraftManagementPage.class), Mockito.eq(drafts));
        Assert.assertNotNull(draftPage);
    }

    @Test
    public void testDeleteDraft() {
        Draft draft = EntityFactory.createDraft();
        List<Draft> drafts = Arrays.asList(draft);
        Mockito.when(pageDecorator.decorateDraftManagementPage(Mockito.any(DraftManagementPage.class), Mockito.eq(drafts))).thenReturn(new DraftManagementPage());
        Mockito.when(draftService.loadAllDrafts()).thenReturn(drafts);
        DraftManagementPage draftPage = underTest.updateDraftStatus(draft);
        Mockito.verify(draftService).updateDraftStatus(draft, DraftStatus.DROPPED);
        Mockito.verify(pageDecorator).decorateDraftManagementPage(Mockito.any(DraftManagementPage.class), Mockito.eq(drafts));
        Assert.assertNotNull(draftPage);
    }

}
