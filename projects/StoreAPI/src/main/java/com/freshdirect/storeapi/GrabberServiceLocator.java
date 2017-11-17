package com.freshdirect.storeapi;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.freshdirect.storeapi.content.grabber.GrabberServiceI;

@Component
public class GrabberServiceLocator implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GrabberServiceLocator.applicationContext = applicationContext;
    }

    public static GrabberServiceI grabberService() {
        final Map<String, GrabberServiceI> beansOfType = applicationContext.getBeansOfType(GrabberServiceI.class);
        return beansOfType.isEmpty() ? null : beansOfType.values().iterator().next();
    }
}
