package com.freshdirect.delivery;

import org.apache.hivemind.Resource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.freshdirect.framework.conf.ResourceUtil;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	Resource		   resource = ResourceUtil.getResource("classpath:/com/freshdirect/delivery/hibernate.cfg.xml");
            sessionFactory = new Configuration().configure(resource.getResourceURL()).buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
