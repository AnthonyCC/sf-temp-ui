package com.freshdirect.cmsadmin.web.dto.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.UserPersona;

@Component
public class UserPersonaToUserDataConverter implements Converter<UserPersona, UserData> {

    @Override
    public UserData convert(UserPersona source) {
        UserData result = new UserData();
        result.setId(source.getUserId());
        result.setName(source.getName());
        return result;
    }

}
