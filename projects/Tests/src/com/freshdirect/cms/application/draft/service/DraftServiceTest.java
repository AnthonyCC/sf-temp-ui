package com.freshdirect.cms.application.draft.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.http.HttpService;

@RunWith(MockitoJUnitRunner.class)
public class DraftServiceTest {

    private static final Long CMS_DRAFT_DEFAULT_ID = 0L;
    private static final Long CMS_DRAFT_OTHER_ID = 1L;
    private static final String PROP_CMS_ADMIN_REST_URL = "cms.adminapp.path";
    private static final String CMS_ADMIN_REST_URL_PATH = "localhost:8080/cmsadmin";
    private static final String CMS_DRAFT_CONTEXT_SESSION_NAME = "CMS_DRAFT_CONTEXT_SESSION_NAME";
    private static final String CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME = "CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME";
    private static final String INVALID_DRAFT_SELECTED_ERROR_MESSAGE = "Selected draft is not valid, you will work with main branch.";

    @Mock
    private HttpService httpService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private HttpService realHttpService;
    private DraftService draftService;

    @Before
    public void setUp() {
        realHttpService = HttpService.defaultService();
        HttpService.setHttpService(httpService);
        FDStoreProperties.set(PROP_CMS_ADMIN_REST_URL, CMS_ADMIN_REST_URL_PATH);

        draftService = DraftService.defaultService();
    }

    @Test
    public void getDefaultDraftContextWhenDraftIdIsNull() {
        DraftContext draftContext = draftService.getValidDraftContext(null);
        Assert.assertEquals(DraftContext.MAIN.getDraftId(), draftContext.getDraftId());
        Assert.assertEquals(DraftContext.MAIN.getDraftName(), draftContext.getDraftName());
    }

    @Test
    public void getDefaultDraftContextWhenCmsAdminGivesBackEmptyCollection() {
        DraftContext draftContext = draftService.getValidDraftContext(CMS_DRAFT_DEFAULT_ID);
        Assert.assertEquals(DraftContext.MAIN.getDraftId(), draftContext.getDraftId());
        Assert.assertEquals(DraftContext.MAIN.getDraftName(), draftContext.getDraftName());
    }

    @Test
    public void getDefaultDraftContextWhenCmsAdminGivesNotMatchedDraftIds() throws IOException {
        List<Draft> drafts = Arrays.asList(createDraft(1L, "draftName1"), createDraft(2L, "draftName2"));
        Mockito.when(httpService.getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_PATH, Draft.class)).thenReturn(drafts);
        DraftContext draftContext = draftService.getValidDraftContext(CMS_DRAFT_DEFAULT_ID);
        Assert.assertEquals(DraftContext.MAIN.getDraftId(), draftContext.getDraftId());
        Assert.assertEquals(DraftContext.MAIN.getDraftName(), draftContext.getDraftName());
    }

    @Test
    public void getDefaultDraftContextWhenCmsAdminThrowsIoException() throws IOException {
        Mockito.when(httpService.getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_PATH, Draft.class)).thenThrow(new IOException());
        DraftContext draftContext = draftService.getValidDraftContext(CMS_DRAFT_DEFAULT_ID);
        Assert.assertEquals(DraftContext.MAIN.getDraftId(), draftContext.getDraftId());
        Assert.assertEquals(DraftContext.MAIN.getDraftName(), draftContext.getDraftName());
    }

    @Test
    public void getMachedDraftContextWhenCmsAdminGivesMatchedDraftIds() throws IOException {
        List<Draft> drafts = Arrays.asList(createDraft(1L, "draftName1"), createDraft(2L, "draftName2"));
        Mockito.when(httpService.getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_PATH, Draft.class)).thenReturn(drafts);
        DraftContext draftContext = draftService.getValidDraftContext(CMS_DRAFT_OTHER_ID);
        Assert.assertEquals(1L, draftContext.getDraftId());
        Assert.assertEquals("draftName1", draftContext.getDraftName());
    }

    @Test
    public void getDraftsWhenHttpServiceIsWorking() throws IOException {
        List<Draft> drafts = Arrays.asList(createDraft(1L, "draftName1"), createDraft(2L, "draftName2"));
        Mockito.when(httpService.getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_PATH, Draft.class)).thenReturn(drafts);
        List<Draft> populatedDrafts = draftService.getDrafts();
        Assert.assertEquals(2, populatedDrafts.size());
        Assert.assertEquals("draftName1", populatedDrafts.get(0).getName());
        Assert.assertEquals("draftName2", populatedDrafts.get(1).getName());
    }

    @Test
    public void getDraftsEmptyListWhenHttpServiceNotWorking() throws IOException {
        Mockito.when(httpService.getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_PATH, Draft.class)).thenThrow(new IOException());
        List<Draft> drafts = draftService.getDrafts();
        Assert.assertEquals(0, drafts.size());
    }

    @Test
    public void getDraftChangesEmptyListWhenHttpServiceNotWorking() throws IOException {
        Mockito.when(httpService.getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_CHANGE_PATH, Draft.class)).thenThrow(new IOException());
        List<Draft> draftChanges = draftService.getDrafts();
        Assert.assertEquals(0, draftChanges.size());
    }

    @Test
    public void setupDraftContextWithMainBranch() {
        Mockito.when(request.getSession()).thenReturn(session);
        draftService.setupDraftContext(CMS_DRAFT_DEFAULT_ID, request);
        Mockito.verify(session).setAttribute(CMS_DRAFT_CONTEXT_SESSION_NAME, DraftContext.MAIN);
        Mockito.verify(session, Mockito.never()).setAttribute(CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME, INVALID_DRAFT_SELECTED_ERROR_MESSAGE);
    }

    @Test
    public void setupDraftContextWithDraftBranch() throws IOException {
        List<Draft> drafts = Arrays.asList(createDraft(1L, "draftName1"), createDraft(2L, "draftName2"));
        Mockito.when(httpService.getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_PATH, Draft.class)).thenReturn(drafts);
        Mockito.when(request.getSession()).thenReturn(session);
        draftService.setupDraftContext(CMS_DRAFT_OTHER_ID, request);
        Mockito.verify(session).setAttribute(Mockito.eq(CMS_DRAFT_CONTEXT_SESSION_NAME), Mockito.any(DraftContext.class));
        Mockito.verify(session, Mockito.never()).setAttribute(CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME, INVALID_DRAFT_SELECTED_ERROR_MESSAGE);
    }

    @Test
    public void setupDraftContextWithMainBranchWhenDraftBranchWantToUse() {
        Mockito.when(request.getSession()).thenReturn(session);
        draftService.setupDraftContext(CMS_DRAFT_OTHER_ID, request);
        Mockito.verify(session).setAttribute(CMS_DRAFT_CONTEXT_SESSION_NAME, DraftContext.MAIN);
        Mockito.verify(session).setAttribute(CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME, INVALID_DRAFT_SELECTED_ERROR_MESSAGE);
    }

    @Test
    public void getDraftChangesWhenHttpServiceIsWorking() {
        List<DraftChange> draftChanges = draftService.getDraftChanges(CMS_DRAFT_DEFAULT_ID);
        Assert.assertTrue(draftChanges.isEmpty());
    }

    @Test
    public void getDraftChangesWhenHttpServiceIsNotWorking() throws IOException {
        Mockito.doThrow(new IOException()).when(httpService).getList(CMS_ADMIN_REST_URL_PATH + DraftService.CMS_DRAFT_CHANGE_PATH + "/draft/" + CMS_DRAFT_DEFAULT_ID,
                DraftChange.class);
        List<DraftChange> draftChanges = draftService.getDraftChanges(CMS_DRAFT_DEFAULT_ID);
        Assert.assertTrue(draftChanges.isEmpty());
    }

    @After
    public void tearDown() {
        HttpService.setHttpService(realHttpService);
    }

    private Draft createDraft(long draftId, String draftName) {
        Draft draft = new Draft();
        draft.setId(draftId);
        draft.setName(draftName);
        return draft;
    }

}
