package com.freshdirect.cms.contentvalidation.validator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.collect.ImmutableSet;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class CyclicReferenceValidatorTest {

    private CyclicReferenceValidator underTest = new CyclicReferenceValidator();

    @Mock
    private ContextualContentProvider contentSource;

    @Test
    public void testParentOk() {
        ContentKey key = ContentKeyFactory.get("FDFolder:folder");
        ContentKey parent = ContentKeyFactory.get("FDFolder:parent_folder");
        ContentKey grandparent = ContentKeyFactory.get("FDFolder:grandparent_folder");

        Mockito.when(contentSource.getParentKeys(key)).thenReturn(ImmutableSet.of(parent));
        Mockito.when(contentSource.getParentKeys(parent)).thenReturn(ImmutableSet.of(grandparent));

        ValidationResults result = underTest.validate(key, null, contentSource);

        Assert.assertTrue(result.getValidationResults().isEmpty());
        Assert.assertTrue(!result.hasError());
        Assert.assertTrue(!result.hasWarning());
        Assert.assertTrue(!result.hasInfo());
    }

    @Test
    public void testDirectCycle() {
        ContentKey key = ContentKeyFactory.get("FDFolder:folder");

        Mockito.when(contentSource.getParentKeys(key)).thenReturn(ImmutableSet.of(key));

        ValidationResults result = underTest.validate(key, null, contentSource);

        Assert.assertTrue(!result.getValidationResults().isEmpty());
        Assert.assertTrue(result.hasError());
        Assert.assertTrue(!result.hasWarning());
        Assert.assertTrue(!result.hasInfo());
    }

    @Test
    public void testIndirectParentCycle() {
        ContentKey key = ContentKeyFactory.get("FDFolder:folder");
        ContentKey parent = ContentKeyFactory.get("FDFolder:parent_folder");

        Mockito.when(contentSource.getParentKeys(key)).thenReturn(ImmutableSet.of(parent));
        Mockito.when(contentSource.getParentKeys(parent)).thenReturn(ImmutableSet.of(key));

        ValidationResults result = underTest.validate(key, null, contentSource);

        Assert.assertTrue(!result.getValidationResults().isEmpty());
        Assert.assertTrue(result.hasError());
        Assert.assertTrue(!result.hasWarning());
        Assert.assertTrue(!result.hasInfo());
    }

    @Test
    public void testIndirectGrandParentCycle() {
        ContentKey key = ContentKeyFactory.get("FDFolder:folder");
        ContentKey parent = ContentKeyFactory.get("FDFolder:parent_folder");
        ContentKey grandparent = ContentKeyFactory.get("FDFolder:grandparent_folder");

        Mockito.when(contentSource.getParentKeys(key)).thenReturn(ImmutableSet.of(parent));
        Mockito.when(contentSource.getParentKeys(parent)).thenReturn(ImmutableSet.of(grandparent));
        Mockito.when(contentSource.getParentKeys(grandparent)).thenReturn(ImmutableSet.of(key));

        ValidationResults result = underTest.validate(key, null, contentSource);

        Assert.assertTrue(!result.getValidationResults().isEmpty());
        Assert.assertTrue(result.hasError());
        Assert.assertTrue(!result.hasWarning());
        Assert.assertTrue(!result.hasInfo());
    }

    @Test
    public void testChildKeyNotPartOfUpperIndirectCycle() {
        ContentKey child = ContentKeyFactory.get("FDFolder:innocent_child");
        ContentKey parent = ContentKeyFactory.get("FDFolder:evil_parent_folder");
        ContentKey grandparent = ContentKeyFactory.get("FDFolder:evil_grandparent_folder");

        Mockito.when(contentSource.getParentKeys(child)).thenReturn(ImmutableSet.of(parent));
        Mockito.when(contentSource.getParentKeys(parent)).thenReturn(ImmutableSet.of(grandparent));
        Mockito.when(contentSource.getParentKeys(grandparent)).thenReturn(ImmutableSet.of(parent));

        ValidationResults result = underTest.validate(child, null, contentSource);

        Assert.assertTrue(!result.getValidationResults().isEmpty());
        Assert.assertTrue(result.hasError());
        Assert.assertTrue(!result.hasWarning());
        Assert.assertTrue(!result.hasInfo());
    }
}
