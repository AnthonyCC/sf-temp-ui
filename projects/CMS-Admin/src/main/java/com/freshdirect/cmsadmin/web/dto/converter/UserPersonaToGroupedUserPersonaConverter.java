package com.freshdirect.cmsadmin.web.dto.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.web.dto.GroupedUserPersona;

@Component
public class UserPersonaToGroupedUserPersonaConverter implements Converter<List<UserPersona>, List<GroupedUserPersona>> {

    @Autowired
    private UserPersonaToUserDataConverter userDataConverter;

    @Override
    public List<GroupedUserPersona> convert(List<UserPersona> source) {
        List<GroupedUserPersona> result = new ArrayList<GroupedUserPersona>();
        Map<Persona, List<UserData>> personaUserMapping = new HashMap<Persona, List<UserData>>();
        for (UserPersona userPersona : source) {
            Persona persona = userPersona.getPersona();
            List<UserData> users = personaUserMapping.get(persona);
            if (users == null) {
                users = new ArrayList<UserData>();
                personaUserMapping.put(persona, users);
            }
            UserData userData = userDataConverter.convert(userPersona);
            users.add(userData);
        }
        for (Persona persona : personaUserMapping.keySet()) {
            GroupedUserPersona groupedUserPersona = new GroupedUserPersona();
            groupedUserPersona.setPersona(persona);
            List<UserData> users = personaUserMapping.get(persona);
            PropertyComparator.sort(users, new MutableSortDefinition("userId", true, true));
            groupedUserPersona.setUsers(users);
            result.add(groupedUserPersona);
        }
        PropertyComparator.sort(result, new MutableSortDefinition("persona.name", true, true));
        return result;
    }

}
