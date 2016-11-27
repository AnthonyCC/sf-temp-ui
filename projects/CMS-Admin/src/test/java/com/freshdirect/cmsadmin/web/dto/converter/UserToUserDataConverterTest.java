package com.freshdirect.cmsadmin.web.dto.converter;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.User;

/**
 * Unit test cases for user to user data converter methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class UserToUserDataConverterTest {

    private static final String ID = "dn=id";
    private static final String FULL_NAME = "fullName";
    private static final String ACCOUNT_NAME = "accountName";

    @InjectMocks
    private UserToUserDataConverter underTest;

    @Test
    public void testConvertUsersToUserDatas() {
        User user = new User();
        user.setId(ID);
        user.setFullName(FULL_NAME);
        user.setAccountName(ACCOUNT_NAME);
        List<User> users = Arrays.asList(user);
        List<UserData> userDatas = underTest.convert(users);
        Assert.assertEquals(1, userDatas.size());
        Assert.assertEquals(ACCOUNT_NAME, userDatas.get(0).getId());
        Assert.assertEquals(FULL_NAME, userDatas.get(0).getName());
    }

}
