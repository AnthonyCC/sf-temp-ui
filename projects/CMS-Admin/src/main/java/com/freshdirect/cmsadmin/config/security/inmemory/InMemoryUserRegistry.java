package com.freshdirect.cmsadmin.config.security.inmemory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class InMemoryUserRegistry {

    public static final Map<String, InMemoryUser> inMemoryUsers = new HashMap<String, InMemoryUser>();

    static {
        inMemoryUsers.put("root", new InMemoryUser("root", "12345678"));
        inMemoryUsers.put("admin", new InMemoryUser("admin", "admin"));
    }

    public InMemoryUser getInMemoryUserByUserName(String userName) {
        return inMemoryUsers.get(userName);
    }

}
