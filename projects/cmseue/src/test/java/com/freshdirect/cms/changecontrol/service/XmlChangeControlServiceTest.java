package com.freshdirect.cms.changecontrol.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.google.common.base.Optional;

@Category(UnitTest.class)
@RunWith(MockitoJUnitRunner.class)
public class XmlChangeControlServiceTest {

    @InjectMocks
    private XmlChangeControlService testService;

    @Test
    public void fetchChangeSetByAnyIdReturnNull() {
        Assert.assertEquals(Optional.absent(), testService.fetchChangeSet(0));
    }

    @Test
    public void getHistoryByAnyContentKeyReturnNull() {
        Assert.assertTrue(testService.getHistory(ContentKeyFactory.get(ContentType.Product, "anyProductId")).isEmpty());
    }

    @Test
    public void saveChangeSetByAnyIdReturnNull() {
        Assert.assertNull(testService.save(new ContentChangeSetEntity()));
    }

    @Test
    public void queryChangeSetByAnyIdReturnNull() {
        Assert.assertTrue(testService.queryChangeSetEntities(ContentKeyFactory.get(ContentType.Product, "anyProductId"), "author", null, null).isEmpty());
    }
}
