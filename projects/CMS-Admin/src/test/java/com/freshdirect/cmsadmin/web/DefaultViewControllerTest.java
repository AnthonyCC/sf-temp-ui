package com.freshdirect.cmsadmin.web;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.web.dto.BasicPage;
import com.freshdirect.cmsadmin.web.dto.DefaultPage;

/**
 * Unit test cases for default view controller.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DefaultViewControllerTest {

    @InjectMocks
    private DefaultViewController underTest;

    @Mock
    private PageDecorator pageDecorator;

    @Test
    public void testLoadDefaultView() {
        Mockito.when(pageDecorator.decorateBasicPage(Mockito.any(BasicPage.class))).thenReturn(new DefaultPage());
        DefaultPage defaultPage = underTest.loadDefaultView();
        Mockito.verify(pageDecorator).decorateBasicPage(Mockito.any(BasicPage.class));
        Assert.assertNotNull(defaultPage);
    }
}
