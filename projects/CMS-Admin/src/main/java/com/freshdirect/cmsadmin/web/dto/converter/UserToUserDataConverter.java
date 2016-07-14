package com.freshdirect.cmsadmin.web.dto.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.User;

@Component
public class UserToUserDataConverter implements Converter<List<User>, List<UserData>> {

    @Override
    public List<UserData> convert(List<User> sources) {
        List<UserData> results = new ArrayList<UserData>(sources.size());
        for (User source : sources) {
            UserData userData = new UserData();
            userData.setId(source.getAccountName());
            userData.setName(source.getFullName());
            results.add(userData);
        }
        return results;
    }
}
