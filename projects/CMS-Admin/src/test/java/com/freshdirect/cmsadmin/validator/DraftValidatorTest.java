package com.freshdirect.cmsadmin.validator;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.freshdirect.cmsadmin.business.DraftService;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.validation.DraftValidator;
import com.freshdirect.cmsadmin.validation.ValidationService;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DraftValidatorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private ValidationService validationService;

    @Mock
    private DraftService draftService;

    @InjectMocks
    private DraftValidator underTest;

    @Test
    public void testValidatorAssigneToDraftClass() {
        Assert.assertTrue(underTest.supports(Draft.class));
    }

    @Test
    public void testValidatorNotAssigneToOtherClasses() {
        Assert.assertFalse(underTest.supports(Object.class));
    }

    @Test
    public void testValidateHttpMethodPostWhenDraftPropertiesAreOk() {
        Draft draft = EntityFactory.createDraft();
        BindingResult errors = new BeanPropertyBindingResult(draft, "draft", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(draft, errors);
        Mockito.verify(validationService, Mockito.never()).createErrorResults(errors);
        Assert.assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenDraftNameIsEmpty() {
        Draft draft = EntityFactory.createDraft(5, "");
        BindingResult errors = new BeanPropertyBindingResult(draft, "draft", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(draft, errors);
        Mockito.verify(validationService, Mockito.atLeastOnce()).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenDraftNameIsNull() {
        Draft draft = EntityFactory.createDraft(5, null);
        BindingResult errors = new BeanPropertyBindingResult(draft, "draft", true, 256);
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(draft, errors);
        Mockito.verify(validationService, Mockito.atLeastOnce()).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

    @Test
    public void testValidateHttpMethodPostWhenDraftNameIsDuplicate() {
        Draft draft1 = EntityFactory.createDraft(5, "draft 1");
        Draft draft2 = EntityFactory.createDraft(6, "draft 1");
        BindingResult errors = new BeanPropertyBindingResult(draft2, "draft", true, 256);
        Mockito.when(draftService.loadAllDrafts()).thenReturn(Arrays.asList(draft1));
        Mockito.when(request.getMethod()).thenReturn("POST");
        underTest.validate(draft2, errors);
        Mockito.verify(validationService, Mockito.atLeastOnce()).createErrorResults(errors);
        Assert.assertTrue(errors.hasErrors());
    }

}
